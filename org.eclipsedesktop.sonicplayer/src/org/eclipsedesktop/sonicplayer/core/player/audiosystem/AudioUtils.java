// Copyright (c) 2004 by Innoopract Informationssysteme GmbH.
// All rights reserved.
package org.eclipsedesktop.sonicplayer.core.player.audiosystem;

import java.io.IOException;
import javax.sound.sampled.*;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (jboehme@innoopract.de)
 */
public class AudioUtils {

  public static void closeDataLine( final SourceDataLine line,
                                    final AudioInputStream ais ) 
                                                            throws IOException {
    line.drain();
    line.stop();
    line.close();
    ais.close();
  }

  public static SourceDataLine openLine( final AudioFormat audioFormat )
    throws LineUnavailableException
  {
    DataLine.Info dataLineInfo = new DataLine.Info( SourceDataLine.class,
                                                    audioFormat );
    Line line = AudioSystem.getLine( dataLineInfo );
    SourceDataLine sourceDataLine = ( SourceDataLine )line;
    sourceDataLine.open( audioFormat );
    sourceDataLine.start();
    return sourceDataLine;
  }
}