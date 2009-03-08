package org.eclipsedesktop.beam.core;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.eclipse.core.runtime.IPath;

public class Util {
  public static String createChecksum( final File file ) {
    String result = "";
    InputStream fis = null;
    MessageDigest complete = null;
    try {
      fis = new FileInputStream( file );
      byte[] buffer = new byte[ 1024 ];
      complete = MessageDigest.getInstance( "MD5" );
      int numRead = 0;
      do {
        numRead = fis.read( buffer );
        if( numRead > 0 ) {
          complete.update( buffer, 0, numRead );
        }
      } while( numRead != -1);
    } catch( FileNotFoundException e ) {
      e.printStackTrace();
    } catch( NoSuchAlgorithmException e ) {
      e.printStackTrace();
    } catch( IOException ioe ) {
      ioe.printStackTrace();
    } finally {
      if( fis != null ) {
        try {
          fis.close();
        } catch( final Exception ignore ) {
          // ignore
        }
      }
    }
    if( complete != null ) {
      byte[] digest = complete.digest();
      result = getStringFromBytes( digest );
    }
    return result;
  }
  
  public static File getDepot() {
    IPath stateLocation 
      = BeamCorePlugin.getDefault().getStateLocation();
    File result = stateLocation.append( "depot" ).toFile();
    result.mkdirs();
    return result;
  }
  
  // helping methods
  //////////////////
  
  private static String getStringFromBytes( final byte[] bytes ) {
    StringBuffer result = new StringBuffer();
    for( int i = 0; i < bytes.length; i++ ) {
      String byteString = Integer.toHexString( bytes[ i ] + 128 );
      while( byteString.length() < 2 ) {
        byteString = "0" + byteString;
      }
      result.append( byteString );
    }
    return result.toString();
  }

}
