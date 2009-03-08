// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.listener;

import java.util.EventObject;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class StopedEvent extends EventObject {

  private static final long serialVersionUID = 3907209355777684274L;

  public StopedEvent( final Object source ) {
    super( source );
  }
}