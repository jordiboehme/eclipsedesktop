// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.clocks;

import java.util.Date;

import org.eclipse.swt.graphics.Image;

/** <p>implementations of this interface provide a visual 
  * representation of a clock as images and texts that represent time.</p> 
  * 
  * @author Leif Frenzel
  */
public interface IClockStyle {

  /** <p>returns a text representation of the passed date/time.</p> 
    *
    * @param date  the date and time for which a string representation is 
    *              requested
    * @return      a string representation of the time
    */
  String getText( Date date );
  
  /** <p>returns an SWT image that represents the specified date/time.</p>
    * 
    * <p>This method may return <code>null</code> to indicate that the clock
    * part should not represent the time in the icon.</p>
    * 
    * <p>If an image is returned, it must be a proper 16x16 part icon. The
    * image is disposed automatically if it is no longer used by the clock.</p>
    * 
    * @param date  the date and time for which a part icon representation is
    *              requested
    * @return      an icon for the specified time
    */
  Image getPartIcon( Date date );
}
