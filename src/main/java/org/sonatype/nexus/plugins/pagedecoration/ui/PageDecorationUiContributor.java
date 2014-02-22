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

package org.sonatype.nexus.plugins.pagedecoration.ui;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.plugins.ui.contribution.UiContributionBuilder;
import org.sonatype.nexus.plugins.ui.contribution.UiContributor;

@Named
@Singleton
public class PageDecorationUiContributor
    implements UiContributor
{

  public static final String ARTIFACT_ID = "nexus-pagedecoration-plugin";

  @Override
  public UiContribution contribute(final boolean debug) {
    return new UiContributionBuilder(this, OSS_PLUGIN_GROUP, ARTIFACT_ID).build(debug);
  }
}
