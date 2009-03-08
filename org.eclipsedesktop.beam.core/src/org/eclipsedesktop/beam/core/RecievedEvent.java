// Copyright (c) 2005 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.beam.core;

/** <p>TODO</p>
 * 
 * @author Jordi B�hme L�pez (mail@jordi-boehme.de)
 */
public class RecievedEvent {

  private BeamItem beamItem;

  public RecievedEvent( final BeamItem beamItem ) {
    this.beamItem = beamItem;
  }

  public BeamItem getBeamItem() {
    return this.beamItem;
  }
}