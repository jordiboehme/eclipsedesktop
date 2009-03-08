// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.packer;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.packer.core.IPackerEngine;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class PackerPlugin extends AbstractUIPlugin {

  //The shared instance.
  private static PackerPlugin plugin;
  //Resource bundle.
  private ResourceBundle resourceBundle;

  private IPackerEngine[] engines;
  
  public PackerPlugin() {
    super();
    plugin = this;
  }

  public void start( final BundleContext context ) throws Exception {
    super.start( context );
  }

  public void stop( final BundleContext context ) throws Exception {
    super.stop( context );
    plugin = null;
    resourceBundle = null;
  }

  public static PackerPlugin getDefault() {
    return plugin;
  }

  public boolean isSupported( final String ext ) {
    boolean result = false;
    IPackerEngine[] available = getEngines();
    for( int i = 0; !result && i < available.length; i++ ) {
      result = available[ i ].isExtensionSupported( ext );
    }
    return result;
  }

  public IPackerEngine getPackerEngine( final IStorageEditorInput input )
    throws CoreException
  {
    IPackerEngine result = null;
    String fileExt = new Path( input.getStorage().getName() ).getFileExtension();

    IPackerEngine[] available = getEngines();
    for( int i = 0; result == null && i < available.length; i++ ) {
      if( ( available[ i ] ).isExtensionSupported( fileExt ) ) {
        result = available[ i ];
      }
    }
    return result;
  }

  public static String getResourceString( final String key ) {
    String result = "";
    ResourceBundle bundle = PackerPlugin.getDefault().getResourceBundle();
    try {
      result = ( bundle != null )
                               ? bundle.getString( key )
                               : key;
    } catch( MissingResourceException e ) {
      result = key;
    }
    return result;
  }

  public ResourceBundle getResourceBundle() {
    try {
      if( resourceBundle == null )
        resourceBundle = ResourceBundle.getBundle( "org.eclipsedesktop.packer.PackerPluginResources" );
    } catch( MissingResourceException x ) {
      resourceBundle = null;
    }
    return resourceBundle;
  }
  
  // helping methods
  //////////////////
  
  private IPackerEngine[] getEngines() {
    if( this.engines == null ) {
      ArrayList<IPackerEngine> foundEngines = new ArrayList<IPackerEngine>();
      IExtensionRegistry extReg = Platform.getExtensionRegistry();
      String id = "org.eclipsedesktop.packer.engine";
      IConfigurationElement[] elements = extReg.getConfigurationElementsFor( id );
      for( int i = 0; i < elements.length; i++ ) {
        Object obj = null;
        try {
          obj = elements[ i ].createExecutableExtension( "class" );
        } catch( CoreException ce ) {
          getLog().log( ce.getStatus() );
        }
        if( obj instanceof IPackerEngine ) {
          foundEngines.add( ( IPackerEngine )obj );
        }
      }
      this.engines = new IPackerEngine[ foundEngines.size() ];
      foundEngines.toArray( this.engines );
    }
    return this.engines;
  }

}