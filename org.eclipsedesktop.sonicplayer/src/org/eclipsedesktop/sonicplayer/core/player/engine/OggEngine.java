// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.player.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipsedesktop.sonicplayer.core.listener.FinishedEvent;
import org.eclipsedesktop.sonicplayer.core.listener.PausedEvent;
import org.eclipsedesktop.sonicplayer.core.listener.PlayingEvent;
import org.eclipsedesktop.sonicplayer.core.listener.StopedEvent;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;
import org.eclipsedesktop.sonicplayer.core.player.audiosystem.AudioUtils;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;
import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.ogg.FileStream;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.ogg.OggFormatException;
import de.jarnbjo.vorbis.VorbisStream;

/**
  * <p>TODO</p>
  * 
  * @author Jordi Böhme López (mail@jordi-boehme.de)
  */
public class OggEngine implements ISonicPlayer {

  private File file;
  private VorbisStream vStream;
  private LogicalOggStream loStream;
  private boolean running = false;
  private boolean finished = false;
  private RandomAccessFile oggFile;
  private long resumeTime = 0;

  
  public OggEngine( final File file ) {
    this.file = file;
  }
  
  public File getNowPlaying() {
    return this.file;
  }

  public void play() {
    try {
      this.oggFile = new RandomAccessFile( file, "rwd" );
    } catch( FileNotFoundException exc ) {
      exc.printStackTrace();
    }
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

  public void stop() {
    Playlist.getInstance().fireStopedEvent( new StopedEvent( this.file ) );
    running = false;
    resumeTime = 0;
    try {
      loStream.setTime( 0 );
    } catch( IOException exc ) {
      exc.printStackTrace();
    }
  }

  public void pause() {
    if( isPlaying() ) {
      running = false;
      Playlist.getInstance().firePausedEvent( new PausedEvent( this.file ) );
      resumeTime = loStream.getTime();
    }
  }

  public void resume() {
    if( isPaused() ) {
      play();
    }
  }

  public boolean isPlaying() {
    return this.running;
  }
  
  public boolean isPaused() {
    return !this.running && this.resumeTime > 0;
  }
  
  public void startEngine() {
    final FileStream fs;
    final LogicalOggStream los;
    final VorbisStream vs;
    try {
      fs = new FileStream( oggFile );
      los = ( LogicalOggStream )fs.getLogicalStreams().iterator().next();
      
      vs = new VorbisStream( los );
      vStream = vs;
      loStream = los;

      
      int channels = vs.getIdentificationHeader().getChannels();
      int sRate = vs.getIdentificationHeader().getSampleRate();
      AudioFormat audioFormat = new AudioFormat( sRate,
                                                 16,
                                                 channels,
                                                 true,
                                                 true );
      SourceDataLine sourceDataLine = AudioUtils.openLine( audioFormat );
      
      VorbisInputStream vis = new VorbisInputStream( vs );
      AudioInputStream ais = new AudioInputStream( vis, audioFormat, -1 );
      byte[] buffer = new byte[ 4096 ];
      int cnt = 0, offset = 0;
      int total = 0;
      long sampleRate = vs.getIdentificationHeader().getSampleRate();
      
      while( running ) {
        if( resumeTime > 0 ) {
          loStream.setTime( resumeTime );
          resumeTime = 0;
        }
        offset = 0;
        while( offset < buffer.length
               && ( cnt = ais.read( buffer, offset, buffer.length - offset ) ) > 0 )
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
          long lt = los.getTime();
          lt *= 1000L;
          lt /= sampleRate;
        }
        offset = 0;
        cnt = 0;
      }
      AudioUtils.closeDataLine( sourceDataLine, ais );
    } catch( OggFormatException exc ) {
      exc.printStackTrace();
    } catch( MalformedURLException exc ) {
      exc.printStackTrace();
    } catch( IOException exc ) {
      exc.printStackTrace();
    } catch( LineUnavailableException exc ) {
      exc.printStackTrace();
    }
    running = false;
    if( finished ) {
      Playlist.getInstance().fireFinishedEvent( new FinishedEvent( file ) );
    }
  }

  public String getCurrentKBitRate() {
    String result = "-";
    if( vStream != null ) {
      result = Integer.toString( vStream.getCurrentBitRate() / 1000 );
    }
    return result;
  }

  public String getSampleRate() {
    String result = "-";
    if( vStream != null ) {
      int sampleRate = vStream.getIdentificationHeader().getSampleRate();
      result = Integer.toString( sampleRate / 1000, 1);
    }
    return result;
  }

  public String getChannels() {
    String result = "-";
    if( vStream != null ) {
      int channels = vStream.getIdentificationHeader().getChannels();
      result = Integer.toString( channels );
    }
    return result;
  }

  public String getTitle() {
    String result = "-";
    if( vStream != null && vStream.getCommentHeader().getTitle() != null ) {
      result = vStream.getCommentHeader().getTitle();
    }
    return result;
  }

  public String getArtist() {
    String result = "-";
    if( vStream != null && vStream.getCommentHeader().getArtist() != null ) {
      result = vStream.getCommentHeader().getArtist();
    }
    return result;
  }

  public String getTime() {
    String result = "-";
    if( vStream != null && loStream != null ) {
      Date date = new Date();
      DateFormat df = new SimpleDateFormat( "H:mm:ss" );
      df.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
      long lt = loStream.getTime();
      lt *= 1000L;
      lt /= vStream.getIdentificationHeader().getSampleRate();
      date.setTime( lt );
      result = df.format( date );
    }
    return result;
  }

  public int getProgress() {
    int result = 0;
    if( loStream != null ) {
      long cgp = loStream.getTime();
      long max = loStream.getMaximumGranulePosition();
      long progress = ( cgp * 100 / max  );
      result = Math.round(progress);
    }
    return result;
  }

  public void seek( final float progress ) {
    long seekTime = ( long )( loStream.getMaximumGranulePosition() * ( progress / 100 ) ); 
    try {
      loStream.setTime( seekTime );
    } catch( IOException exc ) {
      exc.printStackTrace();
    }
  }

  public void forward() {
    long cgp = loStream.getTime();
    long maximumGranulePosition = loStream.getMaximumGranulePosition();
    cgp += maximumGranulePosition / 10;
    if( cgp > maximumGranulePosition ) {
      cgp = maximumGranulePosition - ( maximumGranulePosition / 100 );
    }
    try {
      loStream.setTime( cgp );
    } catch( IOException exc ) {
      exc.printStackTrace();
    }
    if( isPaused() ) {
      resumeTime = cgp;
    }
  }

  public void rewind() {
    long cgp = loStream.getTime();
    cgp -= loStream.getMaximumGranulePosition() / 10;
    if( cgp < 0L ) {
      cgp = 0L;
    }
    try {
      loStream.setTime( cgp );
    } catch( IOException exc ) {
      exc.printStackTrace();
    }
    if( isPaused() ) {
      resumeTime = cgp;
    }
  }

  public static class VorbisInputStream extends InputStream {

    private VorbisStream source;

    public VorbisInputStream( final VorbisStream source ) {
      this.source = source;
    }

    public int read() throws IOException {
      return 0;
    }

    public int read( final byte[] buffer ) throws IOException {
      return read( buffer, 0, buffer.length );
    }

    public int read( final byte[] buffer, final int offset, final int length )
      throws IOException
    {
      int result = -1;
      try {
        result = source.readPcm( buffer, offset, length );
      } catch( EndOfOggStreamException e ) {
        result = -1;
      }
      return result;
    }
  }
}