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

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.enunciate.contract.jaxrs.ResourceMethodSignature;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.slf4j.LoggerFactory;
import org.sonatype.configuration.validation.InvalidConfigurationException;
import org.sonatype.nexus.plugins.pagedecoration.api.dto.PageDecorationConfigResponse;
import org.sonatype.nexus.plugins.pagedecoration.persist.model.PageDecorationConfigDTO;
import org.sonatype.plexus.rest.resource.PathProtectionDescriptor;
import org.sonatype.plexus.rest.resource.PlexusResource;

/**
 * Resource for managing page decoration settings.
 */
@Path("/pagedecoration/snippets")
@Produces({"application/xml", "application/json"})
@Consumes({"application/xml", "application/json"})
@Singleton
@Named("PageDecorationPlexusResource")
@Typed(PlexusResource.class)
public class PageDecorationPlexusResource
    extends AbstractPageDecorationPlexusResource
{
  @Inject
  private org.slf4j.Logger logger = LoggerFactory.getLogger(PageDecorationPlexusResource.class);

  public PageDecorationPlexusResource() {
    this.setModifiable(true);
  }

  @Override
  public Object getPayloadInstance() {
    return new PageDecorationConfigResponse();
  }

  @Override
  public PathProtectionDescriptor getResourceProtection() {
    return new PathProtectionDescriptor(getResourceUri(), "authcBasic");
  }

  @Override
  public String getResourceUri() {
    return "/pagedecoration/snippets";
  }

  /**
   * Retrieves the page decoration snippets.
   */
  @Override
  @GET
  @ResourceMethodSignature(output = PageDecorationConfigResponse.class)
  public Object get(Context context, Request request, Response response, Variant variant)
      throws ResourceException
  {
    PageDecorationConfigDTO dto = this.getConfiguration().readPageDecorationConfig();
    
    PageDecorationConfigResponse result = new PageDecorationConfigResponse();

    result.setData(dto);

    return result;
  }

  /**
   * Sets the page decoration snippets.
   */
  @Override
  @PUT
  @ResourceMethodSignature(input = PageDecorationConfigResponse.class, output = PageDecorationConfigResponse.class)
  public Object put(Context context, Request request, Response response, Object payload)
      throws ResourceException
  {
    PageDecorationConfigResponse pageDecorationResponse = (PageDecorationConfigResponse) payload;

    if (pageDecorationResponse.getData() == null) {
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
          "Page Decoration Snippets were missing from Request.");
    }

    PageDecorationConfigDTO pageDecorationConfig = pageDecorationResponse.getData();

    logger.info("put called2: " + pageDecorationConfig);
    try {
      // validation happens in this method
      this.getConfiguration().updatePageDecorationConfig(pageDecorationConfig);
      // if it didn't throw an InvalidConfigurationException, we are good to go.
      this.getConfiguration().save();
    }
    catch (InvalidConfigurationException e) {
      // this will build and thrown an exception.
      this.handleConfigurationException(e);
    }

    return this.get(context, request, response, null);
  }
}
