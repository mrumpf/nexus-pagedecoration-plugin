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

define('Sonatype/repoServer/PageDecorationConfigPanel', function() {

  Sonatype.repoServer.PageDecorationConfigPanel = function( config ) {
    var config = config || {};
    var defaultConfig = {
      title: 'Page Decoration'
    };
    Ext.apply(this, config, defaultConfig);

    this.servicePath = {
      snippets: Sonatype.config.servicePath + '/pagedecoration/snippets',
    };

    this.referenceData = {
      snippets: {
        preHead: '',
        postHead: '',
        preBody: '',
        postBody: ''
      }
    };

    this.formPanel = new Ext.FormPanel({
      region: 'center',
      trackResetOnLoad: true,
      autoScroll: true,
      border: false,
      frame: true,
      collapsible: false,
      collapsed: false,
      labelWidth: 175,
      layoutConfig: {
        labelSeparator: ''
      },

      items: [
        {
          xtype: 'panel',
          buttonAlign: 'left',
          items: [
            {
              xtype: 'fieldset',
              checkboxToggle: false,
              title: 'Snippets',
              anchor: Sonatype.view.FIELDSET_OFFSET_WITH_SCROLL,
              collapsible: true,
              autoHeight: true,
              layoutConfig: {
                labelSeparator: ''
              },

              items: [
                {
                  xtype: 'textarea',
                  fieldLabel: 'Pre Head',
                  helpText: 'The snippet to render before the HTML head tag.',
                  name: 'preHead',
                  anchor: Sonatype.view.FIELD_OFFSET_WITH_SCROLL,
                  allowBlank: true
                },
                {
                  xtype: 'textarea',
                  fieldLabel: 'Post Head',
                  helpText: 'The snippet to render after the HTML head tag.',
                  name: 'postHead',
                  anchor: Sonatype.view.FIELD_OFFSET_WITH_SCROLL,
                  allowBlank: true
                },
                {
                  xtype: 'textarea',
                  fieldLabel: 'Pre Body',
                  helpText: 'The snippet to render before the HTML body tag.',
                  name: 'preBody',
                  anchor: Sonatype.view.FIELD_OFFSET_WITH_SCROLL,
                  allowBlank: true
                },
                {
                  xtype: 'textarea',
                  fieldLabel: 'Post Body',
                  helpText: 'The snippet to render after the HTML body tag.',
                  name: 'postBody',
                  anchor: Sonatype.view.FIELD_OFFSET_WITH_SCROLL,
                  allowBlank: true
                }
              ]
            }
          ]
        }
      ],
      buttons: [
        {
          text: 'Save',
          handler: this.saveButtonHandler,
          scope: this
        },
        {
          text: 'Cancel',
          handler: this.cancelButtonHandler,
          scope: this
        }
      ]
    });

    Sonatype.repoServer.PageDecorationConfigPanel.superclass.constructor.call(this, {
      autoScroll: false,
      layout: 'border',
      items: [
        this.formPanel
      ]
    });

    this.formPanel.on( 'beforerender', this.beforeRenderHandler, this.formPanel );
    this.formPanel.on( 'afterlayout', this.afterLayoutHandler, this, { single: true } );
    this.formPanel.form.on( 'actioncomplete', this.actionCompleteHandler, this );
    this.formPanel.form.on( 'actionfailed', this.actionFailedHandler, this.formPanel );
  };

  Ext.extend(Sonatype.repoServer.PageDecorationConfigPanel, Ext.Panel, {
    beforeRenderHandler : function(){
    },

    actionCompleteHandler : function( form, action ) {
      if ( action.type == 'sonatypeSubmit' ) {
      }
      else if ( action.type == 'sonatypeLoad' ) {
      }
    },

    hideComponent : function( component ) {
      component.disable();
      component.hide();
    },

    showComponent : function( component ) {
      component.enable();
      component.show();
    },

    actionFailedHandler : function( form, action ) {
      if ( action.failureType == null ) {
        Sonatype.utils.connectionError( action.response, null, null, action.options );
      }
      else if(action.failureType == Ext.form.Action.CLIENT_INVALID){
        Sonatype.MessageBox.alert('Missing or Invalid Fields', 'Please change the missing or invalid fields.').setIcon(Sonatype.MessageBox.WARNING);
      }
      else if(action.failureType == Ext.form.Action.CONNECT_FAILURE){
        Sonatype.utils.connectionError( action.response, 'There is an error communicating with the server.' )
      }
      else if(action.failureType == Ext.form.Action.LOAD_FAILURE){
        Sonatype.MessageBox.alert('Load Failure', 'The data failed to load from the server.').setIcon(Sonatype.MessageBox.ERROR);
      }
    },

    saveButtonHandler : function() {
      var form = this.formPanel.form;
      form.doAction('sonatypeSubmit', {
        method: 'PUT',
        url: this.servicePath.snippets,
        waitMsg: 'Updating HTML snippets...',
        fpanel: this.formPanel,
        serviceDataObj: this.referenceData.snippets
      });
    },

    cancelButtonHandler : function() {
      Sonatype.view.mainTabPanel.remove( this.id, true );
    },

    afterLayoutHandler : function(){
      this.formPanel.getForm().doAction( 'sonatypeLoad', {
        url: this.servicePath.snippets,
        method: 'GET',
        fpanel: this.formPanel
      } );
    }
  });

  Sonatype.Events.addListener( 'nexusNavigationInit', function( nexusPanel ) {
    nexusPanel.add( {
      enabled: Sonatype.lib.Permissions.checkPermission( 'nexus:settings', Sonatype.lib.Permissions.READ ),
      sectionId: 'st-nexus-config',
      title: 'Page Decoration',
      tabId: 'pagedecoration-configuration',
      tabCode: Sonatype.repoServer.PageDecorationConfigPanel
    } );
  } );

});
