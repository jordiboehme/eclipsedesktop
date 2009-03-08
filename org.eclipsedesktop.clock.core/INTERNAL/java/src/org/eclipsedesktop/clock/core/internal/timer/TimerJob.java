// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal.timer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipsedesktop.clock.core.internal.CoreTexts;

/** <p>A system job that notifies listeners in regular intervals.</p> 
  * 
  * @author Leif Frenzel
  */
public class TimerJob extends Job {

  public TimerJob() {
    super( CoreTexts.timerJob_name );
    setSystem( true );
  }
  
  
  // interface methods of Job
  ///////////////////////////
  
  @Override
  protected IStatus run( final IProgressMonitor monitor ) {
    Timer.getInstance().notifyListeners();
    schedule( 1024 );
    return Status.OK_STATUS;
  }
}
