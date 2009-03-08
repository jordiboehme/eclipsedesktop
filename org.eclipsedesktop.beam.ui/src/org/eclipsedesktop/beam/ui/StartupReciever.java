package org.eclipsedesktop.beam.ui;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IStartup;
import org.eclipsedesktop.beam.core.reciever.Reciever;
import org.eclipsedesktop.beam.ui.preferences.BeamPreferences;


public class StartupReciever implements IStartup {

  public void earlyStartup() {
    Reciever job = new Reciever();
    job.setPeerName( BeamPreferences.getPeerName() );
    job.setPriority( Job.LONG );
    job.setSystem( true );
    job.schedule();
  }
}
