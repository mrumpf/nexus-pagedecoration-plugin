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

package org.sonatype.nexus.plugins.pagedecoration.api;

import javax.inject.Inject;

import org.restlet.data.Status;
import org.sonatype.configuration.validation.InvalidConfigurationException;
import org.sonatype.configuration.validation.ValidationMessage;
import org.sonatype.configuration.validation.ValidationResponse;
import org.sonatype.nexus.plugins.pagedecoration.persist.PageDecorationConfiguration;
import org.sonatype.plexus.rest.resource.PlexusResourceException;
import org.sonatype.plexus.rest.resource.error.ErrorMessage;
import org.sonatype.plexus.rest.resource.error.ErrorResponse;
import org.sonatype.security.rest.AbstractSecurityPlexusResource;

import com.thoughtworks.xstream.XStream;

public abstract class AbstractPageDecorationPlexusResource
    extends AbstractSecurityPlexusResource
{
  @Inject
  private PageDecorationConfiguration configuration;

  public PageDecorationConfiguration getConfiguration() {
    return configuration;
  }

  protected ErrorMessage createNexusError(String id, String msg) {
    ErrorMessage ne = new ErrorMessage();
    ne.setId(id);
    ne.setMsg(msg);
    return ne;
  }

  protected void handleConfigurationException(InvalidConfigurationException e)
	      throws PlexusResourceException
  {
    getLogger().debug("Configuration error!", e);

    ErrorResponse nexusErrorResponse = new ErrorResponse();

    ValidationResponse vr = ((InvalidConfigurationException) e).getValidationResponse();

    if (vr != null && vr.getValidationErrors().size() > 0) {
      for (ValidationMessage vm : vr.getValidationErrors()) {
        nexusErrorResponse.addError(createNexusError(vm.getKey(), vm.getShortMessage()));
      }
    }
    else {
      nexusErrorResponse.addError(createNexusError("*", e.getMessage()));
    }
    throw new PlexusResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Configuration error.", nexusErrorResponse);
  }

  @Override
  public void configureXStream(XStream xstream) {
    super.configureXStream(xstream);
    PageDecorationXStreamConfigurator.configureXStream(xstream);
  }
}
