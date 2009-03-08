package org.eclipsedesktop.eclipsemail;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipsedesktop.eclipsemail.core.Inbox;

public class InboxContentProvider implements IStructuredContentProvider {

  
  // interface methods of IStructuredContentProvider
  //////////////////////////////////////////////////
  
  public Object[] getElements( final Object inputElement ) {
    return Inbox.getInstance().getMessages();
  }

  public void dispose() {
    // unused
  }

  public void inputChanged( final Viewer viewer, 
                            final Object oldInput, 
                            final Object newInput) {
    // unused
  }
}
