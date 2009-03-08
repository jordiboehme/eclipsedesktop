// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.styles;

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.clock.ui.clocks.IClockStyle;

/** <p>a style for the clock that only fuzzily says what time it is.</p>
  *
  * @author Leif Frenzel
  */
public class FuzzyClockStyle implements IClockStyle {

  
  // interface methods of IClockStyle
  ///////////////////////////////////
  
  public Image getPartIcon( final Date date ) {
    // no special part icon
    return null;
  }

  public String getText( final Date date ) {
    return date.toString();
  }
}
