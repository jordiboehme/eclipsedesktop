// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.player.engine;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.sound.sampled.*;
import javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipsedesktop.sonicplayer.core.listener.*;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;
import org.eclipsedesktop.sonicplayer.core.player.audiosystem.AudioUtils;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;


/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class MP3Engine implements ISonicPlayer {

  private File file;
  private boolean running = false;
  private boolean finished = false;
  private int resumeBytePos;
  private DecodedMpegAudioInputStream mStream;
  private AudioFileFormat audioFileFormat;
  private AudioInputStream audioInputStream;

  public MP3Engine( final File file ) {
    this.file = file;
  }
  
  public File getNowPlaying() {
    return this.file;
  }
  
  public void play() {
    running = true;
    Playlist.getInstance().firePlayingEvent( new PlayingEvent( this.file ) );
    Job engineJob = new Job( "Sonic Player Engine" ) {
      
      protected IStatus run( final IProgressMonitor monitor ) {
        startEngine();
        return Status.OK_STATUS;
      }
    };
    engineJob.setPriority( Job.LONG );
    engineJob.setSystem( true );
    engineJob.schedule();
  }

  public void startEngine() {
    MpegAudioFileReader mpegReader = new MpegAudioFileReader();
    try {
      audioInputStream = mpegReader.getAudioInputStream( file );
      audioFileFormat = mpegReader.getAudioFileFormat( file );
      int channels = audioInputStream.getFormat().getChannels();
      float sRate = audioInputStream.getFormat().getSampleRate();
      AudioFormat audioFormat = new AudioFormat( sRate,
                                                 16,
                                                 channels,
                                                 true,
                                                 true );
      mStream = new DecodedMpegAudioInputStream( audioFormat, audioInputStream );
      SourceDataLine sourceDataLine = AudioUtils.openLine( audioFormat );
      
      byte[] buffer = new byte[ 4096 ];
      int cnt = 0, offset = 0;
      int total = 0;
      
      while( running ) {
        if( resumeBytePos > 0 ) {
          mStream.skip( resumeBytePos );
          resumeBytePos = 0;
        }
        offset = 0;
        while( offset < buffer.length
               && ( cnt = mStream.read( buffer, offset, buffer.length - offset ) ) > 0 )
        {
          offset += cnt;
        }
        if( cnt == -1 ) {
          running = false;
          finished = true;
        }
        if( offset > 0 ) {
          sourceDataLine.write( buffer, 0, offset );
          total += offset;
        }
        offset = 0;
        cnt = 0;
      }
      AudioUtils.closeDataLine( sourceDataLine, mStream );
    } catch( UnsupportedAudioFileException exc ) {
      exc.printStackTrace();
    } catch( IOException exc ) {
      exc.printStackTrace();
    } catch( LineUnavailableException exc ) {
      exc.printStackTrace();
    } finally {
      try {
        if( audioInputStream != null ) {
          audioInputStream.close();
        }
      } catch( IOException e ) {
        //ignore
      }
    }
    running = false;
    if( finished ) {
      Playlist.getInstance().fireFinishedEvent( new FinishedEvent( file ) );
    }
  }

  public void pause() {
    if( isPlaying() ) {
      String msStr = mStream.properties().get( "mp3.position.byte" ).toString();
      int bytePos = Integer.parseInt( msStr );
      resumeBytePos = bytePos;
      running = false;
      Playlist.getInstance().firePausedEvent( new PausedEvent( this.file ) );
    }
  }

  public void resume() {
    if( isPaused() ) {
      play();
    }
  }

  public void stop() {
    Playlist.getInstance().fireStopedEvent( new StopedEvent( this.file ) );
    running = false;
    resumeBytePos = 0;
  }

  public void forward() {
    long max = audioFileFormat.getByteLength();
    String msStr = mStream.properties().get( "mp3.position.byte" ).toString();
    long cur = Long.parseLong( msStr );
    long offset = max / 10;
    if( cur + offset < max ) {
      mStream.skip( offset );
    }
    if( isPaused() ) {
//      resumeTime = cgp;
    }
  }

  public void rewind() {
    // TODO
  }

  public void seek( final float progress ) {
    // TODO
  }

  public boolean isPlaying() {
    return this.running;
  }

  public boolean isPaused() {
    return !this.running && this.resumeBytePos > 0;
  }

  public String getCurrentKBitRate() {
    String result = "-";
    if( mStream != null ) {
      String brStr = mStream.properties().get( "mp3.frame.bitrate" ).toString();
      result = Integer.toString( Integer.valueOf(brStr).intValue() / 1000 );
    }
    return result;
  }

  public String getSampleRate() {
    String result = "-";
    if( mStream != null ) {
      float sampleRate = mStream.getFormat().getFrameRate();
      result = Float.toString( sampleRate / 1000);
    }
    return result;
  }

  public String getChannels() {
    String result = "-";
    if( mStream != null ) {
      int channels = mStream.getFormat().getChannels();
      result = Integer.toString( channels );
    }
    return result;
  }

  public String getTitle() {
    String result = "";
    //TODO
    return result;
  }

  public String getArtist() {
    String result = "";
    //TODO
    return result;
  }

  public String getTime() {
    String result = "-";
    if( mStream != null ) {
      Date date = new Date();
      DateFormat df = new SimpleDateFormat( "H:mm:ss" );
      df.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
      String msStr = mStream.properties().get( "mp3.position.microseconds" ).toString();
      date.setTime( Long.parseLong(msStr) / 1000 );
      result = df.format( date );
    }
    return result;
  }

  public int getProgress() {
    int result = 0;
    if( mStream != null ) {
      long max = audioFileFormat.getByteLength();
      String msStr = mStream.properties().get( "mp3.position.byte" ).toString();
      long cgp = Long.parseLong( msStr );
      long progress = ( cgp * 100 / max  );
      result = Math.round(progress);
    }
    return result;
  }
}
