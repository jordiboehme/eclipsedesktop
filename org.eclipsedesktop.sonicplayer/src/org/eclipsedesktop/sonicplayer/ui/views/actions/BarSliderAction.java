// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class BarSliderAction implements MouseListener{

  public void mouseDoubleClick( final MouseEvent evnt ) {
    //empty
  }

  public void mouseDown( final MouseEvent evnt ) {
    ISonicPlayer player = SonicPlayerPlugin.getDefault().getActivePlayer();
    if( player != null) {
      float relPos = evnt.x;
      float maxPos = ( ( ProgressBar )evnt.widget ).getSize().x;
      player.seek( ( ( relPos / maxPos ) * 100 ) );
    }
  }

  public void mouseUp( final MouseEvent evnt ) {
    //empty
  }
}
