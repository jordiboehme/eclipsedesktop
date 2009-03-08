package org.eclipsedesktop.beam.core.reciever;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipsedesktop.beam.core.*;
import org.p2psockets.*;

public class Reciever extends Job {

  public static final String JOB_NAME = "Beam reciever";
  private static final String APP_NAME = "org.eclipsedesktop.beam";

  public Reciever() {
    super( JOB_NAME );
    this.isRecieverHub = true;
  }

  private Socket connection;
  private boolean isRecieverHub;
  private String peerName;
  
  public void setPeerName( final String peerName ) {
    this.peerName = peerName;
  }
  
  protected void setRecieverHub( final boolean isRecieverHub ) {
    this.isRecieverHub = isRecieverHub;
  }
  
  protected void setSocket( final Socket sock ) {
    this.connection = sock;
  }
  
  protected IStatus run( final IProgressMonitor monitor ) {
    if( this.isRecieverHub ) {
      try {
        runRecieverHub( monitor );
      } catch( IOException e ) {
        e.printStackTrace();
      } catch( Exception e ) {
        e.printStackTrace();
      }
    } else {
      runRecieverWorker( monitor );
    }
    return Status.OK_STATUS;
  }

  private void runRecieverWorker( final IProgressMonitor monitor ) {
    monitor.beginTask( "Recieving beam", 100 );
    try {
      InputStream is = this.connection.getInputStream();
      Header header = new Header();
      String headerData = readHeader( is );
      monitor.worked( 5 );
      Header.parse( header, headerData );
      monitor.worked( 5 );
      File file = readBinary( is,
                              header,
                              new SubProgressMonitor( monitor, 90 ) );
      is.close();
      this.connection.close();
      BeamItem beamItem = new BeamItem();
      beamItem.setFileName( header.getFileName() );
      beamItem.setFileSize( header.getFileSize() );
      beamItem.setFile( file );
      RecievedEvent event = new RecievedEvent( beamItem );
      BeamCorePlugin.getDefault().fireRecievedEvent( event );
    } catch( IOException e ) {
      e.printStackTrace();
    }
    monitor.done();
  }

  private void runRecieverHub( final IProgressMonitor monitor )
    throws Exception
  {
    P2PNetwork.autoSignin( this.peerName, APP_NAME );
//    ServerSocket serverSock = new ServerSocket( this.port );
    ServerSocket serverSock = new P2PServerSocket( this.peerName,
                                                   BeamCorePlugin.PORT );
    while( !monitor.isCanceled() ) {
      Socket connection = serverSock.accept();
      Reciever job = new Reciever();
      job.setSocket( connection );
      job.setPeerName( this.peerName );
      job.setRecieverHub( false );
      job.setPriority( Job.LONG );
      job.setSystem( false );
      job.schedule();
    }
    P2PNetwork.signOff();
    monitor.done();
  }

  // helping methods
  //////////////////
  
  private File getUniqueDepotFile( final Header header ) {
    File fileFolder = new File( Util.getDepot(), header.getChecksum() );
    fileFolder.mkdirs();
    return new File( fileFolder, header.getFileName() );
  }

  private String readHeader( final InputStream is ) throws IOException {
    StringBuffer result = new StringBuffer();
    byte[] buf = new byte[ 1 ];
    int i = is.read( buf );
    while( i != -1 && buf[ 0 ] != ( byte )0 ) {
      result.append( new String( buf ) );
      i = is.read( buf );
    }

    return result.toString();
  }

  private File readBinary( final InputStream is,
                           final Header header,
                           final IProgressMonitor monitor )
    throws IOException
  {
    long total = Long.parseLong( header.getFileSize() );
    int bufSize = 1024;
    monitor.beginTask( "Recieving file", ( int )( total / bufSize ) );
    TransferStatus uploadStatus = new TransferStatus( total );
    
    File result = getUniqueDepotFile( header );
    FileOutputStream fos = new FileOutputStream( result );
    BufferedOutputStream bos = new BufferedOutputStream( fos );

    byte[] buf = new byte[ bufSize ];
    int numRead = is.read( buf );
    long worked = numRead;
    while( !monitor.isCanceled() && worked < total && numRead != -1 ) {
        bos.write( buf, 0, numRead );
        numRead = is.read( buf );
        if( numRead > 0 ) {
  		  worked += numRead;
  		  uploadStatus.worked( numRead );
  		  monitor.worked( 1 );
  		  monitor.subTask( uploadStatus.toString() );
        }
    }
    if( numRead > 0 ) {
      bos.write( buf, 0, numRead );
    }
    bos.close();
    fos.close();
    monitor.done();
    return result;
  }
}
