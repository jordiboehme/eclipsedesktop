package org.eclipsedesktop.packer.popup.actions;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;

public class ExtractHere extends Extract {

  @Override
  protected IContainer getOutRoot( final IFile archiveFile) {
    return archiveFile.getParent();
  }

}
