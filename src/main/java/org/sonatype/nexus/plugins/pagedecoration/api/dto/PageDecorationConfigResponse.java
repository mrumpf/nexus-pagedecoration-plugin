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

package org.sonatype.nexus.plugins.pagedecoration.api.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.sonatype.nexus.plugins.pagedecoration.persist.model.PageDecorationConfigDTO;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is a wrapper to work with the ExtJS form population as it expects a JSON attribute with name "data".
 *
 */
@XStreamAlias(value = "pageDecorationConfig")
@XmlRootElement(name = "pageDecorationConfig")
public class PageDecorationConfigResponse {

	private PageDecorationConfigDTO data;

	/**
	 * @return the pageDecorationConfig
	 */
	public PageDecorationConfigDTO getData() {
		return data;
	}

	/**
	 * @param pageDecorationConfig
	 *            the pageDecorationConfig to set
	 */
	public void setData(PageDecorationConfigDTO pageDecoration) {
		this.data = pageDecoration;
	}

}
