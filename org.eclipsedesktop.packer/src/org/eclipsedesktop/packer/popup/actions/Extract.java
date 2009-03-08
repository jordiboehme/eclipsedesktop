// Copyright (c) 20042006 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.packer.popup.actions;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipsedesktop.packer.PackerPlugin;
import org.eclipsedesktop.packer.core.IPackerEngine;
import org.eclipsedesktop.packer.core.PackerItem;

public abstract class Extract implements IObjectActionDelegate {

  static final int BUFFER = 2048;

  private IWorkbenchPart activePart;

  private Object[] objects;

  public Extract() {
    super();
  }

  protected abstract IContainer getOutRoot( final IFile archiveFile );

  public void setActivePart(final IAction action, final IWorkbenchPart part) {
    this.activePart = part;
  }

  public void run(final IAction action) {
    for( int i = 0; i < this.objects.length; i++ ) {
      Object object = objects[ i ];
      if( object instanceof IFile ) {
        final IFile file = ( IFile )object;
        String ext = file.getFileExtension();
          if( PackerPlugin.getDefault().isSupported( ext ) ) {
            fireJob( file );
          }
      }
    }
  }

  private void fireJob( final IFile file ) {
    WorkspaceJob job = new WorkspaceJob( "Extract " + file.getName() ) {
      @Override
      public IStatus runInWorkspace( final IProgressMonitor monitor ) {
        IStatus result = Status.OK_STATUS;
        try {
          FileEditorInput input = new FileEditorInput( file );
          IPackerEngine engine = PackerPlugin.getDefault().getPackerEngine( input );
          PackerItem[] items = engine.getPackerItems( input );
          engine.extract( input,
                          items,
                          getOutRoot( file ).getLocation(),
                          true,
                          monitor );
        } catch( final CoreException ce ) {
          result = ce.getStatus();
          ErrorDialog.openError( getShell(),
                                 "Extraction error",
                                 ce.getLocalizedMessage(),
                                 ce.getStatus(),
                                 IStatus.ERROR );
          PackerPlugin.getDefault().getLog().log( ce.getStatus() );
        }
        return result;
      }
    };
    job.setPriority( Job.LONG );
    job.setRule( file.getProject() );
    job.setUser( true );
    job.schedule();
  }

  public void selectionChanged(final IAction action, final ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      this.objects = ((IStructuredSelection) selection).toArray();
    }
  }

  // helping functions
  // //////////////////

  private Shell getShell() {
    return activePart.getSite().getShell();
  }
}