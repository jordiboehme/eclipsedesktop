// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.listener;

import java.util.EventObject;

/** <p>TODO</p>
 * 
 * @author Jordi B�hme L�pez (mail@jordi-boehme.de)
 */
public class PausedEvent extends EventObject {

  private static final long serialVersionUID = 3617579314258654771L;

  public PausedEvent( final Object source ) {
    super( source );
  }
}