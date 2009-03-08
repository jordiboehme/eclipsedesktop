// Copyright (c) 2004 by eclipsedesktop.org
// Leif Frenzel (himself@leiffrenzel.de)
// Jordi Boehme Lopez (mail@jordi-boehme.de)
package org.eclipsedesktop.eclipsesetimon.core;

/**
 * @author Jordi Boehme Lopez
 */
public class StatusFile implements IStatusFile {

  int progress;
  String elapsed;
  String estimated;
    
  public String getElapsed() {
    return elapsed;
  }

  public int getProgress() {
    return progress;
  }

  public String getEstimated() {
    return estimated;
  }

  public void setElapsed( final String elapsed ) {
    this.elapsed = elapsed;
  }

  public void setProgress( final int progress ) {
    this.progress = progress;
  }

  public void setEstimated( final String estimated ) {
    this.estimated = estimated;
  }
}