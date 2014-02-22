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

package org.sonatype.nexus.plugins.pagedecoration;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.plugins.pagedecoration.api.PageDecorationPlexusResource;
import org.sonatype.nexus.plugins.rest.AbstractNexusIndexHtmlCustomizer;
import org.sonatype.nexus.plugins.rest.NexusIndexHtmlCustomizer;

@Named
@Singleton
public class NexusIndexHtmlCustomizerImpl
    extends AbstractNexusIndexHtmlCustomizer
    implements NexusIndexHtmlCustomizer
{
    @Inject
    private PageDecorationPlexusResource resource;

    @Override
    public String getPreHeadContribution( final Map<String, Object> context )
    {
        return resource.getConfiguration().getConfiguration().getPageDecorationConfigDTO().getPreHead();
    }

    @Override
    public String getPostHeadContribution( final Map<String, Object> context )
    {
        return resource.getConfiguration().getConfiguration().getPageDecorationConfigDTO().getPostHead();
    }

    @Override
    public String getPreBodyContribution( final Map<String, Object> context )
    {
        return resource.getConfiguration().getConfiguration().getPageDecorationConfigDTO().getPreBody();
    }

    @Override
    public String getPostBodyContribution( final Map<String, Object> context )
    {
        return resource.getConfiguration().getConfiguration().getPageDecorationConfigDTO().getPostBody();
    }
}