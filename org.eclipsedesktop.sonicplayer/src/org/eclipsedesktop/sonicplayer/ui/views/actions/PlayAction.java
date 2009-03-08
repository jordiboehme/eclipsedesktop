// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import java.io.File;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;
import org.eclipsedesktop.sonicplayer.core.player.SonicPlayer;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class PlayAction extends Action {

  private TableViewer viewer;

  public PlayAction( final TableViewer tv ) {
    this.viewer = tv;
  }
  
  public void run() {
    SonicPlayerPlugin plugin = SonicPlayerPlugin.getDefault();
    ISonicPlayer player = plugin.getActivePlayer();
    if( player == null && viewer != null && !viewer.getControl().isDisposed() ) {
      StructuredSelection strctSel = ( StructuredSelection )viewer.getSelection();
      Object obj = ( strctSel ).getFirstElement();
      player = new SonicPlayer( (( File )obj) ).getPlayer();
      plugin.setActivePlayer( player );
    }
    if( player != null ) {
      if( player.isPaused() ) {
        player.resume();
      } else if( player.isPlaying() ) {
        player.pause();
      } else {
        player.play();
      }
    } 
  }
}