// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.styles;

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.clock.ui.clocks.IClockStyle;

/** <p>a clock extension that shows the time in digital style.</p> 
  * 
  * @author Leif Frenzel
  */
public class DigitalClockStyle implements IClockStyle {

  
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
