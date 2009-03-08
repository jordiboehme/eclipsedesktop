// Copyright (c) 20046 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.packer.core;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IStorageEditorInput;

/** <p>TODO</p>
 * 
 * @author Jordi Boehme Loepez (jboehme@innoopract.de)
 */
public interface IPackerEngine {

  boolean isExtensionSupported( final String ext );
  
  void extract( final IStorageEditorInput input,
                final PackerItem[] items,
                final IPath path,
                final boolean withSubDirs,
                final IProgressMonitor monitor ) throws CoreException;
  
  PackerItem[] getPackerItems( final IStorageEditorInput input );
  
  IStorage getEntry( final IStorageEditorInput input,
                     final PackerItem packerItem);
}