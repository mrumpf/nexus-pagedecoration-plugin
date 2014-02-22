/*
 * Copyright (c) 2014 jCoderz.org, Inc. All rights reserved.
 * 
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package org.sonatype.nexus.plugins.pagedecoration.persist;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.locks.ReentrantLock;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.configuration.validation.InvalidConfigurationException;
import org.sonatype.nexus.configuration.application.ApplicationConfiguration;
import org.sonatype.nexus.logging.AbstractLoggingComponent;
import org.sonatype.nexus.plugins.pagedecoration.persist.model.Configuration;
import org.sonatype.nexus.plugins.pagedecoration.persist.model.PageDecorationConfigDTO;
import org.sonatype.nexus.plugins.pagedecoration.persist.model.io.xpp3.PageDecorationConfigurationXpp3Reader;
import org.sonatype.nexus.plugins.pagedecoration.persist.model.io.xpp3.PageDecorationConfigurationXpp3Writer;
import org.sonatype.nexus.util.file.DirSupport;
import org.sonatype.sisu.goodies.common.io.FileReplacer;
import org.sonatype.sisu.goodies.common.io.FileReplacer.ContentWriter;

public abstract class AbstractPageDecorationConfiguration
    extends AbstractLoggingComponent
    implements PageDecorationConfiguration
{
  private final ApplicationConfiguration applicationConfiguration;

  private final ReentrantLock lock = new ReentrantLock();

  private Configuration configuration;

  public AbstractPageDecorationConfiguration(ApplicationConfiguration applicationConfiguration)
  {
    this.applicationConfiguration = checkNotNull(applicationConfiguration);
  }

  protected File getConfigurationFile() {
    return new File(applicationConfiguration.getConfigurationDirectory(), "pagedecoration.xml");
  }

  @Override
  public PageDecorationConfigDTO readPageDecorationConfig() {
	  PageDecorationConfigDTO dto = getConfiguration().getPageDecorationConfigDTO();

    return dto;
  }

  @Override
  public void updatePageDecorationConfig(PageDecorationConfigDTO pageDecorationConfig)
      throws InvalidConfigurationException
  {
    lock.lock();

    try {
      getConfiguration().setPageDecorationConfigDTO(pageDecorationConfig);
    }
    finally {
      lock.unlock();
    }

  }

  @Override
  public Configuration getConfiguration() {
    lock.lock();
    try {
      if (configuration != null) {
        return configuration;
      }
      final File configurationFile = getConfigurationFile();

      try (final Reader fr = new InputStreamReader(new FileInputStream(configurationFile))) {
        PageDecorationConfigurationXpp3Reader reader = new PageDecorationConfigurationXpp3Reader();
        configuration = reader.read(fr);
      }
      catch (FileNotFoundException e) {
        getLogger().warn("Configuration not found", e);
        // This is ok, may not exist first time around
        configuration = this.getDefaultConfiguration();
      }
      catch (IOException e) {
        getLogger().error("IOException while retrieving configuration file", e);
      }
      catch (XmlPullParserException e) {
        getLogger().error("Invalid XML Configuration", e);
      }
    }
    finally {
      lock.unlock();
    }

    return configuration;
  }

  @Override
  public void save() {
    lock.lock();
    try {
      final File configurationFile = getConfigurationFile();
      try {
        DirSupport.mkdir(configurationFile.getParentFile().toPath());
      }
      catch (IOException e) {
        final String message =
            "\r\nCould not create configuration file '"
                + configurationFile.toString()
                + "'. Application cannot start properly until the process has read+write permissions to this folder.";
        getLogger().error(message, e);
        throw new IOException("Could not create configuration file " + configurationFile.getAbsolutePath(), e);
      }

      final Configuration configuration = this.configuration.clone();
      getLogger().debug("Saving configuration: {}", configurationFile);
      final FileReplacer fileReplacer = new FileReplacer(configurationFile);
      fileReplacer.setDeleteBackupFile(true);

      fileReplacer.replace(new ContentWriter()
      {
        @Override
        public void write(final BufferedOutputStream output)
            throws IOException
        {
          new PageDecorationConfigurationXpp3Writer().write(output, configuration);
        }
      });
    }
    catch (IOException e) {
      getLogger().error("IOException while storing configuration file", e);
    }
    finally {
      lock.unlock();
    }
  }

  public Configuration getDefaultConfiguration() {
    Configuration defaultConfig = null;

    Reader fr = null;
    InputStream is = null;
    try {
      is = getClass().getResourceAsStream("/META-INF/pagedecoration/pagedecoration.xml");
      PageDecorationConfigurationXpp3Reader reader = new PageDecorationConfigurationXpp3Reader();
      fr = new InputStreamReader(is);
      defaultConfig = reader.read(fr);
    }
    catch (IOException e) {
      this.getLogger().error(
          "Failed to read default Page Decoration configuration. This may be corrected while the application is running.",
          e);
      defaultConfig = new Configuration();
    }
    catch (XmlPullParserException e) {
      this.getLogger().error(
          "Failed to read default Page Decoration configuration. This may be corrected while the application is running.",
          e);
      defaultConfig = new Configuration();
    }
    finally {
      if (fr != null) {
        try {
          fr.close();
        }
        catch (IOException e) {
        }
      }

      if (is != null) {
        try {
          is.close();
        }
        catch (IOException e) {
        }
      }
    }
    getLogger().debug("default configuration: " + defaultConfig);
    return defaultConfig;
  }
}
