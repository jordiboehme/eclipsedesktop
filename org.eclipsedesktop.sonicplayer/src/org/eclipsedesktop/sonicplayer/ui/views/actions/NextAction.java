// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import java.io.File;
import org.eclipse.jface.action.Action;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;
import org.eclipsedesktop.sonicplayer.core.player.SonicPlayer;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class NextAction extends Action {

  public void run() {
    Playlist pl = Playlist.getInstance();
    SonicPlayerPlugin plugin = SonicPlayerPlugin.getDefault();
    ISonicPlayer player = plugin.getActivePlayer();
    File file = null;
    if( player == null ) {
      file = ( File )pl.getElements()[0];
    } else {
      file = ( File )pl.getNext( player.getNowPlaying() );
      player.stop();
    }
    player = new SonicPlayer( file ).getPlayer();
    plugin.setActivePlayer( player );
    if( player != null ) {
      player.play();
    }
  }
}