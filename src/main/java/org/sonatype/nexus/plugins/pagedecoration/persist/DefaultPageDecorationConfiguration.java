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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.configuration.application.ApplicationConfiguration;
import org.sonatype.sisu.goodies.eventbus.EventBus;

@Singleton
@Named
public class DefaultPageDecorationConfiguration
    extends AbstractPageDecorationConfiguration
    implements PageDecorationConfiguration
{
  @Inject
  public DefaultPageDecorationConfiguration(ApplicationConfiguration applicationConfiguration, EventBus eventBus)
  {
    super(applicationConfiguration);
  }
}
