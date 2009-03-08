// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.listener;

import java.util.EventObject;

/** <p>TODO</p>
 * 
 * @author Jordi Bšhme L—pez (mail@jordi-boehme.de)
 */
public class PlayingEvent extends EventObject {

  /**
   * 
   */
  private static final long serialVersionUID = -8833774531107499498L;

  public PlayingEvent( final Object source ) {
    super( source );
  }
}