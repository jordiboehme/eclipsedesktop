//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsemail.attachments;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class AttachmentContentProvider implements IStructuredContentProvider {

  private Object[] elements;
  
  public Object[] getElements( final Object inputElement ) {
    return elements;
  }

  public void dispose() {
  }

  public void inputChanged( final Viewer viewer, 
                            final Object oldInput, 
                            final Object newInput ) {
    this.elements = ( Object[] )newInput;
  }
  
  // helping methods
  //////////////////
  
}
