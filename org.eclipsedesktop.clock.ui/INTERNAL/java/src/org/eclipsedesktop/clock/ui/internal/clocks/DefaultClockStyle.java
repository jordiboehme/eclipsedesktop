// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.clocks;

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.clock.ui.clocks.IClockStyle;

/** <p>an empty, fail-safe implementation for <code>IClockStyle</code>.</p>
  *
  * @author Leif Frenzel
  */
class DefaultClockStyle implements IClockStyle {

  
  // interface methods of IClockStyle
  ///////////////////////////////////
  
  public String getText( final Date date ) {
    return ""; //$NON-NLS-1$
  }
  
  public Image getPartIcon( final Date date ) {
    // null to indicate that no special icon is to be displayed
    return null;
  }
}
