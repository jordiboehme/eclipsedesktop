// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.player;

import java.io.File;
import org.eclipsedesktop.sonicplayer.core.player.engine.MP3Engine;
import org.eclipsedesktop.sonicplayer.core.player.engine.OggEngine;

/**
  * <p>TODO</p>
  * 
  * @author Jordi Böhme López (mail@jordi-boehme.de)
  */
public class SonicPlayer {
  
  ISonicPlayer playerEngine = null;
  
  public SonicPlayer( final File file ) {
    if( file != null ) {
      if( isOggFormat( file ) ) {
        playerEngine = new OggEngine( file );
      } else if( isMP3Format( file ) ) {
        playerEngine = new MP3Engine( file );
      }
    }
  }

  public ISonicPlayer getPlayer() {
    return playerEngine;
  }

  // helping methods
  //////////////////
  
  private boolean isOggFormat( final File file ) {
    return file.getName().toLowerCase().endsWith( ".ogg" );
  }

  private boolean isMP3Format( final File file ) {
    return file.getName().toLowerCase().endsWith( ".mp3" );
  }
}