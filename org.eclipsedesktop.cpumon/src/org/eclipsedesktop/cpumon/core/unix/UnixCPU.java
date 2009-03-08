//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.cpumon.core.unix;

import java.io.File;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.cpumon.core.ICPUInfo;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (jboehme@innoopract.de)
 */
public class UnixCPU implements ICPUInfo {
  
  private File statFile;

  private float pre_total = 0; 
  private float pre_used = 0; 
  
  public UnixCPU() {
    statFile = new File( "/proc/stat" );
  }
  
  public int getUsage() {
    return ( int )calculateUsage( parseCPUValues() );
  }
  
  // helping functions
  ////////////////////
  private String[] parseCPUValues() {
    int valstart = 0;
    int valend = 0;
    String cpustring = "";
    String stat = Util.getFileContent( statFile );

    valstart = stat.indexOf( "cpu " );
    if (valstart != -1) {
      valend = stat.indexOf('\n', valstart);
      valstart += 4;
      cpustring = stat.substring( valstart, valend ).trim();
    }

    String[] info = new String[ 4 ];
    for( int i = 0; i < 4; i++ ) {
      valend = cpustring.indexOf( " " );
      if (valend != -1) {
        valend = ( valend == -1 ) ? cpustring.length() : valend + 1;
        info[ i ] = cpustring.substring( 0, valend ).trim();
        cpustring = cpustring.substring( valend, cpustring.length() ).trim();
      }
    }
    
    return info;
  }

  private float calculateUsage( final String[] info ) {
    float usage = 0;
    float cpu = Float.parseFloat( info[ 0 ] );
    float nice = Float.parseFloat( info[ 1 ] );
    float system = Float.parseFloat( info[ 2 ] );
    float idle = Float.parseFloat( info[ 3 ] );
    float used = cpu + nice + system;
    float total = cpu + nice + system + idle;
    
    if( ( pre_total == 0 ) || !( total - pre_total > 0)) {
      usage = 0;
    } else {
      usage = (100 * ( used - pre_used )) / (total - pre_total);
    }
    this.pre_total = total;
    this.pre_used = used;
    return usage;
  }

}
