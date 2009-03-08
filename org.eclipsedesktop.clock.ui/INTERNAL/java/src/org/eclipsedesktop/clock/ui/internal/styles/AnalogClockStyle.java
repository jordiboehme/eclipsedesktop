// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.styles;

import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.clock.ui.clocks.IClockStyle;

/** <p>a clock implementation that shows the time in an analog-clock 
  * style.</p> 
  *
  * @author Leif Frenzel
  */
public class AnalogClockStyle implements IClockStyle {

  
  // interface methods of IClockStyle
  ///////////////////////////////////
  
  public Image getPartIcon( final Date date ) {
    // no special icon for the part
    return null;
  }

  public String getText( final Date date ) {
    return date.toString();
  }
}
