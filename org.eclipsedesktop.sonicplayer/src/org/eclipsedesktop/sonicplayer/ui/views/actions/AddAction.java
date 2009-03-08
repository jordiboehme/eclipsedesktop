// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import java.io.File;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.*;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.ISonicPlayerConstants;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class AddAction extends Action implements ISonicPlayerConstants {

  private TableViewer viewer;

  
  public AddAction( final TableViewer tv ) {
    this.viewer = tv;
  }

  public void run() {
    if( viewer != null && !viewer.getControl().isDisposed() ) {
      IWorkbench workbench = PlatformUI.getWorkbench();
      IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
      DirectoryDialog dd = new DirectoryDialog( activeWindow.getShell() );
      IPreferenceStore preferenceStore 
        = SonicPlayerPlugin.getDefault().getPreferenceStore();
      String rootDir = preferenceStore.getString( ROOTDIR_KEY );
      if( rootDir == null || rootDir.equals( "" ) ) {
        rootDir = Util.getSystemRoot();
      }
      dd.setFilterPath( rootDir );
      String pathName = dd.open();
      Playlist pl = Playlist.getInstance();
      if( pathName != null ) {
        File path = new File( pathName );
        File[] fileList = path.listFiles();
        for( int i = 0; i < fileList.length; i++ ) {
          File file = fileList[ i ];
          if(    file.isFile() 
              && (    file.getName().toLowerCase().endsWith( ".mp3" )
                   || file.getName().toLowerCase().endsWith( ".ogg" ) 
                 ) ) {
            pl.addElement( file );
          }
        }
        viewer.refresh();
      }
    }
  }
}