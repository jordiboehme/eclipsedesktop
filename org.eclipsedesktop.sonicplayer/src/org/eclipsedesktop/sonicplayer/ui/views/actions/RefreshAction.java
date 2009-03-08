// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class RefreshAction extends Action {

  private TreeViewer viewer;

  public RefreshAction( final TreeViewer tv ) {
    this.viewer = tv;
  }
  
  public void run() {
    if( viewer != null && !viewer.getControl().isDisposed() ) {
      viewer.refresh();
    }
  }
}