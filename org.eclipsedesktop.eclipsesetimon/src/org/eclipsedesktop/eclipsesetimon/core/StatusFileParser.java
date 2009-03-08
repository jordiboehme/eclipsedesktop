// Copyright (c) 2004 by eclipsedesktop.org
// Leif Frenzel (himself@leiffrenzel.de)
// Jordi Boehme Lopez (mail@jordi-boehme.de)
package org.eclipsedesktop.eclipsesetimon.core;

import java.io.File;
import org.eclipsedesktop.base.core.Util;

/**
 * @author Jordi Boehme Lopez
 */
public class StatusFileParser {

   public static IStatusFile parse( final File file ) {


    String statusFileContent = Util.getFileContent( file );

    double progress = parseProgress( statusFileContent );
    double cpu = parseCPU( statusFileContent );
    StringBuffer elapsed = parseElapsed( cpu );
    StringBuffer estimated = parseEstimated( progress, cpu );

    StatusFile status = new StatusFile();
    status.setProgress( (int) (progress * 100) );
    status.setElapsed( elapsed.toString() );
    status.setEstimated( estimated.toString() );
    
    return status;
  }



  // Helping Methods
  //////////////////
  
  private static StringBuffer parseEstimated( final double progress,
                                       final double cpu ) {
    StringBuffer estimated = new StringBuffer();
    int seconds = 0;
    int minutes = 0;
    int hours = 0;

    if (progress != 0.0) {
      double cpuleft = (cpu / progress) - cpu;
      seconds = (int) (cpuleft % 60);
      minutes = (int) ((cpuleft / 60) % 60);
      hours = (int) (cpuleft / 3600);
      if (hours < 10)
        estimated.append('0');
      estimated.append(hours);
      estimated.append(":");
      if (minutes < 10)
        estimated.append('0');
      estimated.append(minutes);
      estimated.append(":");
      if (seconds < 10)
        estimated.append('0');
      estimated.append(seconds);
    } else  {
      estimated.append( "none" );
    }
    return estimated;
  }

  private static StringBuffer parseElapsed( final double cpu ) {
    StringBuffer elapsed = new StringBuffer();
    int seconds = (int) (cpu % 60);
    int minutes = (int) ((cpu / 60) % 60);
    int hours = (int) (cpu / 3600);
    if (hours < 10) {
      elapsed.append('0');
    }
    elapsed.append(hours);
    elapsed.append(":");
    if (minutes < 10) {
      elapsed.append('0');
    }
    elapsed.append(minutes);
    elapsed.append(":");
    if (seconds < 10) {
      elapsed.append('0');
    }
    elapsed.append(seconds);
    return elapsed;
  }

  private static double parseCPU( final String statusFileContent ) {
    double cpu = 0.0;
    int valstart;
    int valend = 0;
    valstart = statusFileContent.indexOf("cpu=");
    if (valstart != -1) {
      valend = statusFileContent.indexOf('\n', valstart);
      valstart += 4;
      String cpustring = statusFileContent.substring(valstart, valend);
      try {
        cpu = Double.parseDouble(cpustring);
      } catch (Exception err) {
        cpu = 0.0;
      }
    }
    return cpu;
  }

  private static double parseProgress( final String statusFileContent ) {
    double progress = 0.0;
    int valstart;
    int valend;
    valstart = statusFileContent.indexOf("prog=");
    if (valstart != -1) {
      valend = statusFileContent.indexOf('\n', valstart);
      valstart += 5;
      if (valend == -1)
        valend = valstart;
      String progstring = statusFileContent.substring(valstart, valend);
      try {
        progress = Double.parseDouble(progstring);
      } catch (Exception err) {
        progress = 0.0;
      }
    }
    return progress;
  }
}