// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class ClearAction extends Action {

  private TableViewer viewer;

  public ClearAction( final TableViewer tv ) {
    this.viewer = tv;
  }
  
  public void run() {
    Playlist.getInstance().setElements( new Object[ 0 ] );
    if( viewer != null && !viewer.getControl().isDisposed() ) {
      viewer.refresh();
    }
  }
}