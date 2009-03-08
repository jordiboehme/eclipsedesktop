package org.eclipsedesktop.beam.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipsedesktop.beam.ui.ContactDialog;
import org.eclipsedesktop.beam.ui.runnables.SendJob;

public class Send implements IObjectActionDelegate {
  private IFile selectedResource;

  public Send() {
    super();
  }
  
  public void setActivePart( final IAction action,
                             final IWorkbenchPart targetPart ) {
  }
  
  public void run( final IAction action ) {
    Shell shell = Display.getCurrent().getActiveShell();
    ContactDialog dialog = new ContactDialog( shell );
    if( dialog.open() == ContactDialog.OK ) {
      String toPeenName = dialog.getPeerName();
      Job sendJob = new SendJob( toPeenName, this.selectedResource );
      sendJob.setUser( true );
      sendJob.schedule();
    }
  }

  public void selectionChanged( final IAction action,
                                final ISelection selection ) {
    if( selection instanceof IStructuredSelection ) {
      Object obj = ( ( IStructuredSelection )selection ).getFirstElement();
      if( obj instanceof IFile ) {
        this.selectedResource = ( IFile )obj;
      }
    }
  }
}
