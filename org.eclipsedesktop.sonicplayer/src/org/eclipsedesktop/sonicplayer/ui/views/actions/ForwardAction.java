// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class ForwardAction extends Action {

  public void run() {
    ISonicPlayer player = SonicPlayerPlugin.getDefault().getActivePlayer();
    if(    player != null 
        && (    player.isPlaying() 
             || player.isPaused() )) {
      player.forward();
    }
  }
}