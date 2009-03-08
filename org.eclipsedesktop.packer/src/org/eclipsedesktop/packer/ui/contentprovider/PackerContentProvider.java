//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.packer.ui.contentprovider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipsedesktop.packer.PackerPlugin;
import org.eclipsedesktop.packer.core.IPackerEngine;

public class PackerContentProvider implements IStructuredContentProvider {

  private Object[] elements;
  private IStorageEditorInput editorInput;
  
  public Object[] getElements( final Object inputElement ) {
    if( elements == null ) {
      this.elements = createElements();
    }
    return elements;
  }

  private Object[] createElements() {
    Object[] result = new Object[ 0 ];
    if( editorInput != null ) {
      try{
        IPackerEngine engine = PackerPlugin.getDefault().getPackerEngine( editorInput );
        if( engine != null ) {
          result = engine.getPackerItems( editorInput );
        }
      } catch ( final CoreException ce ) {
        PackerPlugin.getDefault().getLog().log( ce.getStatus() );
      }
    }
    return result;
  }

  public void dispose() {
  }

  public void inputChanged( final Viewer viewer, 
                            final Object oldInput, 
                            final Object newInput ) {
    if( newInput instanceof IStorageEditorInput ) {
      this.editorInput = ( IStorageEditorInput )newInput;
      this.elements = null;
    }
  }
  
  // helping methods
  //////////////////
  
}
