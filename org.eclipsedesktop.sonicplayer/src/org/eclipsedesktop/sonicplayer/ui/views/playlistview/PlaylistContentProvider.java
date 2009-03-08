// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.playlistview;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class PlaylistContentProvider implements IStructuredContentProvider {

  public Object[] getElements( final Object inputElement ) {
    return Playlist.getInstance().getElements();      
  }

  public void dispose() {
    // TODO
  }

  public void inputChanged( final Viewer viewer,
                            final Object oldInput,
                            final Object newInput ) {
    // TODO
  }
}
