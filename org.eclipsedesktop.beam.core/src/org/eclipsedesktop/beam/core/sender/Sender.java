package org.eclipsedesktop.beam.core.sender;

import java.io.*;
import java.net.Socket;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipsedesktop.beam.core.*;
import org.p2psockets.P2PSocket;

public class Sender {

  public static void send( final String peer,
                           final File file,
                           final IProgressMonitor monitor ) {
    try {
      monitor.beginTask( "Beaming...", 100 );
//      Socket sock = new Socket( peer, PORT );
      monitor.subTask( "connecting..." );
      Socket sock = new P2PSocket( peer, BeamCorePlugin.PORT );
      OutputStream sockOut = sock.getOutputStream();
      monitor.worked( 5 );
      sendHeader( file, sockOut, new SubProgressMonitor( monitor, 5 ) );
      sendBreak( sockOut );
      sendBinary( file, sockOut, new SubProgressMonitor( monitor, 90 ) );
      sockOut.flush();
      sockOut.close();
      sock.shutdownOutput();
      sock.close();
      monitor.done();
    } catch( IOException e ) {
      e.printStackTrace(); //TODO
    }
  }
  
  // helping methods
  //////////////////

  private static void sendBreak( final OutputStream sockOut ) {
    try {
      sockOut.write( ( byte )0 );
    } catch( IOException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void sendBinary( final File file,
                                  final OutputStream out,
                                  final IProgressMonitor monitor ) {
    int bufSize = 1024;
    long total = file.length();
    monitor.beginTask( "Beaming binary", ( int )( total / bufSize ) );
    try {
      TransferStatus uploadStatus = new TransferStatus( total );
      FileInputStream is = new FileInputStream( file );
      byte[] buffer = new byte[ bufSize ];
      int numRead = 0;
      do {
        numRead = is.read( buffer );
        if( numRead > 0 ) {
          out.write( buffer, 0, numRead );
          uploadStatus.worked( numRead );
          monitor.subTask( uploadStatus.toString() );
        }
        monitor.worked( 1 );
      } while( numRead != -1 && !monitor.isCanceled() );
      is.close();
      monitor.done();
    } catch( FileNotFoundException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch( IOException e ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void sendHeader( final File file,
                                  final OutputStream out,
                                  final IProgressMonitor monitor )
    throws FileNotFoundException,
           IOException {
    monitor.beginTask( "Beaming header", 100 );
    monitor.subTask( "Beaming header" );
    writeString( Header.computeHeader( file ), out );
    monitor.done();
  }

  private static void writeString( final String string,
                                   final OutputStream out )
    throws IOException 
  {
    out.write( string.getBytes() );
  }
}
