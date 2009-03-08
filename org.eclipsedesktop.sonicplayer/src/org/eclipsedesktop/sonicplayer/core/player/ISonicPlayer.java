// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.player;

import java.io.File;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public interface ISonicPlayer {

  public void play();

  public void startEngine();
  
  public void pause();

  public void resume();

  public void stop();

  public void forward();

  public void rewind();

  public void seek( final float progress );
  
  public File getNowPlaying();

  public boolean isPlaying();

  public boolean isPaused();

  public String getCurrentKBitRate();

  public String getSampleRate();

  public String getChannels();

  public String getTitle();

  public String getArtist();

  public String getTime();

  public int getProgress();
}