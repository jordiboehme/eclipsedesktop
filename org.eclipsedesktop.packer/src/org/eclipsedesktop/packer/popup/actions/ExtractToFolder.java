package org.eclipsedesktop.packer.popup.actions;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;

public class ExtractToFolder extends Extract {

  @Override
  protected IContainer getOutRoot( final IFile archiveFile ) {
    String name = new Path( archiveFile.getName() ).removeFileExtension().lastSegment();
    return archiveFile.getParent().getFolder( new Path( name ) );
  }

}
