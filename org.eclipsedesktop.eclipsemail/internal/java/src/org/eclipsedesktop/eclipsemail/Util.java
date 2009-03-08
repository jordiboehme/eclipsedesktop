package org.eclipsedesktop.eclipsemail;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.util.SWTResourceUtil;

public class Util {
  
  public static Image getImageByFilename( final String fileName) {
    Image result = null;
    IWorkbench workbench = PlatformUI.getWorkbench();
    ImageDescriptor descriptor = workbench.getEditorRegistry().getImageDescriptor( fileName );
    if( descriptor == null ) {
      result = workbench.getSharedImages().getImage( ISharedImages.IMG_OBJ_FILE );
    } else {
      result = ( Image )SWTResourceUtil.getImageTable().get( descriptor );
      if( result == null ) {
        result = descriptor.createImage();
        SWTResourceUtil.getImageTable().put( descriptor, result );
      }
    }
    if( result == null ) {
      result = workbench.getSharedImages().getImage( ISharedImages.IMG_OBJ_FILE );
    }
    return result;
  }

  public static void showNewMessagesTray( final int count ) {
    Display display = Display.getCurrent();
    Tray tray = display.getSystemTray();
    TrayItem trayItem = new TrayItem( tray, SWT.NONE );
    trayItem.setVisible( true );
    trayItem.setText( "Eclipsemail" );
    trayItem.setToolTipText( count + " new mail messages" );
    String iconLocation = "icons/full/eview16/icon.gif";
    trayItem.setImage( new Image( display, getResourceString( iconLocation ) ) );
  }

  private static String getResourceString( final String location ) {
    String result = "";
    EclipsemailPlugin plugin = EclipsemailPlugin.getDefault();
    URL rFileLocation = plugin.getBundle().getEntry( location );
    try {
      result = FileLocator.resolve( rFileLocation ).getFile();
    } catch( IOException ex ) {
      ex.printStackTrace();
    }
    return result;
  }

}
