// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.base.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.AccessController;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.browser.WebBrowserView;

import sun.security.action.GetPropertyAction;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class Util {
  
  public static String getSystemRoot() {
    String result = "";
    int i = 0;
    while(    i < File.listRoots().length && result.equals( "" ) ) {
      if( !(    File.listRoots()[ i ].toString().toLowerCase().startsWith( "a" )
             || File.listRoots()[ i ].toString().toLowerCase().startsWith( "b" ) 
           ) ) {
        result = File.listRoots()[ i ].toString();
      }
      i++;
    }
    return result;
  }
  
  public static void showBrowserView( final URL searchURL ) {
	
    IViewPart vp;
    try {
      String viewId = WebBrowserView.WEB_BROWSER_VIEW_ID;
	  vp = Util.getActiveWindow().getActivePage().showView( viewId );
      if( vp != null && vp instanceof WebBrowserView ) {
        ( ( WebBrowserView )vp ).setURL( searchURL.toExternalForm() );
      }
    } catch( PartInitException e ) {
      e.printStackTrace();
    }
  }

  public static GridLayout getGridLayout( final int numColumns, 
                                          final boolean makeColumnsEqualWidth,
                                          final int spacing ) {
    GridLayout result = new GridLayout( numColumns, makeColumnsEqualWidth );
    result.horizontalSpacing = spacing;
    result.verticalSpacing = spacing;
    result.marginHeight = spacing;
    result.marginWidth = spacing;
    return result;
  }
  
  public static String getSelectionText() {
    String result = "";
    ISelection sel = getActiveWindow().getSelectionService().getSelection();
    if( sel != null && sel instanceof ITextSelection ) {
      result = ( ( ITextSelection )sel ).getText();
    }
    return result;
  }
  
  public static String getClipboardText() {
    String result = "";
    Clipboard cb = new Clipboard( Display.getDefault() );
    TextTransfer transfer = TextTransfer.getInstance();
    String data = ( String )cb.getContents(transfer);
    if( data != null ) {
      result = data;
    }
    cb.dispose();
    return result;
  }

  public static IWorkbenchWindow getActiveWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  public static String getFileContent( final File file ) {
    String result = "";
    try {
      FileInputStream fis = new FileInputStream( file );
      int nextchar;
      StringBuffer filecontents = new StringBuffer();
      while ( ( nextchar = fis.read() ) != -1 ) {
        filecontents.append( ( char )nextchar );
      }
      result = filecontents.toString();
      fis.close();
    } catch ( Exception e ) {
      System.out.println( e.getMessage() );
      e.printStackTrace( System.out );
    }
    return result;
  }

  public static String doURLEncode( final String text, final String encoding ) {
    String result = text;
    try {
      result = URLEncoder.encode( text, encoding );
    } catch( UnsupportedEncodingException exception ) {
      try {
        result = URLEncoder.encode( text, getDefaultEncoding() );
      } catch( UnsupportedEncodingException exception1 ) {
        // not possible -> default encoding always available
      }
    }
    return result;
  }

  // helping functions
  ////////////////////

  private static String getDefaultEncoding() {
    GetPropertyAction propertyAction = new GetPropertyAction( "file.encoding" );
    return (String)AccessController.doPrivileged ( propertyAction );
  }

}
