//Copyright (c) 2004 by Innoopract Informationssysteme GmbH
//All rights reserved.
package org.eclipsedesktop.weblookup.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.weblookup.WebLookupPlugin;


/** <p>TODO</p>
 * 
 * @author Jordi Boehme Lopez
 */
public class ActionProvider {
  private static Action lastAction;

  public static Action[] getActions() {
    IConfigurationElement[] elements = getConfigurationElements();
    Action[] result = new Action[ elements.length ];
    for( int i = 0; i < elements.length; i++ ) {
      String name = elements[ i ].getAttribute( "name" );
      String icon = elements[ i ].getAttribute( "icon" );
      String pluginId = elements[ i ].getDeclaringExtension().getNamespaceIdentifier();
      Object obj = null;
      try {
        obj = elements[ i ].createExecutableExtension( "class" );
      } catch( CoreException exc1 ) {
        exc1.printStackTrace();
      }
      if( obj != null && obj instanceof IWebLookup ) {
        final IWebLookup lookupObj = ( IWebLookup )obj; 
        ImageDescriptor imageDescr 
          = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, icon );
        result[ i ] = new Action( name, imageDescr ) {
          public void run() {
            String searchString = getSearchText();
            URL searchURL = null;
            try {
              lastAction = this;
              searchURL = lookupObj.getSearchURL( searchString );
              Util.showBrowserView( searchURL );
            } catch( MalformedURLException exc ) {
              exc.printStackTrace();
            }
          }
        };
      }
    }
    return result;
  }

  public static Action getLastAction() {
    return lastAction;
  }
  
  public static IConfigurationElement[] getConfigurationElements() {
    IExtensionRegistry extReg = Platform.getExtensionRegistry();
    String id = WebLookupPlugin.EXTPOINT_LOOKUPS;
    IConfigurationElement[] elements = extReg.getConfigurationElementsFor( id );
    return elements;
  }

  public static String getSearchText() {
    String result = "";
    String selection = Util.getSelectionText();
    if( selection != null && !selection.equals( "" ) ) {
      result = selection;
    } else {
      result = Util.getClipboardText();
    }
    return result;
  }
  
}
