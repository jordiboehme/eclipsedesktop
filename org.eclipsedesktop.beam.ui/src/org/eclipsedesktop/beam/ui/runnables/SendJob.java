package org.eclipsedesktop.beam.ui.runnables;

import java.io.File;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipsedesktop.beam.core.sender.Sender;


public class SendJob extends Job {

  private IFile file;
  private String toPeerName;
  
  public SendJob( final String toPeerName,
                  final IFile file ) {
    super( "Beaming file" );
    this.toPeerName = toPeerName;
    this.file = file;
  }
  
  public IStatus run( final IProgressMonitor monitor ) {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IFile fileLocation = root.getFile( this.file.getFullPath() );
    File fileToSend = fileLocation.getLocation().toFile();
    Sender.send( this.toPeerName, fileToSend, monitor );
    return Status.OK_STATUS;
  }
}
