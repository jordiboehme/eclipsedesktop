//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views.playlistview;

import java.io.File;
import org.eclipse.jface.viewers.LabelProvider;

/** <p>TODO</p>
 * 
 * @author Jordi B�hme L�pez (mail@jordi-boehme.de)
 */
public class PlaylistLabelProvider extends LabelProvider {

  public String getText( final Object obj ) {
    return ( ( File )obj ).getName();
  }
}