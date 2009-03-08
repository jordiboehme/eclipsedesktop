//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.monitorcenter.ui.view;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.monitorcenter.core.IMonitorCenterContributor;
import org.eclipsedesktop.monitorcenter.core.Monitor;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme
 */
public class MonitorCenterView extends ViewPart {

  // interface methods of ViewPart
  ////////////////////////////////
  public void createPartControl( final Composite parent ) {
    GridLayout gridLayout = Util.getGridLayout( 1, false, 0 );
    parent.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    parent.setLayoutData( gd );
    fillView( parent );
  }

  public void setFocus() {
    // TODO
  }

  // helping functions
  ////////////////////
  private void fillView( final Composite parent ) {
    IExtensionRegistry extReg = Platform.getExtensionRegistry();
    String id = "org.eclipsedesktop.monitorcenter.contributor";
    IConfigurationElement[] elements = extReg.getConfigurationElementsFor( id );
    for( int i = 0; i < elements.length; i++ ) {
      String name = elements[ i ].getAttribute( "name" );
      String icon = elements[ i ].getAttribute( "icon" );
      String pluginId = elements[ i ].getDeclaringExtension().getNamespaceIdentifier();
      ImageDescriptor imageDescr 
        = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, icon );
      Object obj = null;
      try {
        obj = elements[ i ].createExecutableExtension( "class" );
      } catch( CoreException exc1 ) {
        exc1.printStackTrace();
      }
      if( obj != null && obj instanceof IMonitorCenterContributor ) {
        Monitor monitor = new Monitor();
        monitor.setMonitor( ( IMonitorCenterContributor )obj );
        monitor.setName( name );
        monitor.setImageDescr( imageDescr );
        contributeMonitor( monitor, parent );
      }
    }
    if( elements.length == 0 ) {
      Label message = new Label( parent, SWT.NONE );
      message.setText( "No installed Eclipsedesktop.org Monitors found." );
    }
  }

  private void contributeMonitor( final Monitor monitor, 
                                  final Composite parent ) {
    Group monitorComposite = new Group( parent, SWT.SHADOW_OUT );
    GridLayout gridLayout = Util.getGridLayout( 1, false, 0 );
    monitorComposite.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    monitorComposite.setLayoutData( gd );

    //monitorComposite.setText( monitor.getName() );
    
    contributeMonitorTitle( monitor, monitorComposite );
    monitor.getMonitor().setMonitor( monitorComposite );
  }

  private void contributeMonitorTitle( final Monitor monitor, 
                                       final Composite parent ) {
    Composite titleComposite = new Composite( parent, SWT.NONE );
    GridLayout gridLayout = Util.getGridLayout( 3, false, 1 );
    titleComposite.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    titleComposite.setLayoutData( gd );
    
    Label icon = new Label( titleComposite, SWT.NONE );
    icon.setImage( monitor.getImageDescr().createImage() );
    icon.setToolTipText( monitor.getName() );

    Label name = new Label( titleComposite, SWT.NONE );
    name.setText( monitor.getName() );

    IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
    monitor.getMonitor().setActions( tbm );
  }
}