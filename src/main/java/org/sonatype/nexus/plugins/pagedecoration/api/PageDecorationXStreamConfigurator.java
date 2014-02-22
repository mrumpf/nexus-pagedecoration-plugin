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

import org.slf4j.LoggerFactory;
import org.sonatype.nexus.plugins.pagedecoration.api.dto.PageDecorationConfigResponse;
import org.sonatype.nexus.plugins.pagedecoration.persist.model.PageDecorationConfigDTO;
import org.sonatype.nexus.rest.model.HtmlUnescapeStringConverter;

import com.thoughtworks.xstream.XStream;

/**
 * XStream configurator for page decoration.
 */
public class PageDecorationXStreamConfigurator
{
  @Inject
  private org.slf4j.Logger logger = LoggerFactory.getLogger(PageDecorationPlexusResource.class);

  public static XStream configureXStream(XStream xstream) {
    xstream.processAnnotations(PageDecorationConfigResponse.class);

    final HtmlUnescapeStringConverter converter = new HtmlUnescapeStringConverter(true);

    xstream.registerLocalConverter(PageDecorationConfigDTO.class, "preHead", converter);
    xstream.registerLocalConverter(PageDecorationConfigDTO.class, "postHead", converter);
    xstream.registerLocalConverter(PageDecorationConfigDTO.class, "preBody", converter);
    xstream.registerLocalConverter(PageDecorationConfigDTO.class, "postBody", converter);

    return xstream;
  }

}
