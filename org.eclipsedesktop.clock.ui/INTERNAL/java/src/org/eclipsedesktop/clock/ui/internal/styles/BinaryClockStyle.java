// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.styles;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.clock.ui.clocks.IClockStyle;


/** <p>a clock extension that shows the time binary-style.</p>
  *
  *  @author Leif Frenzel
  */
public class BinaryClockStyle implements IClockStyle {

  
  // interface methods of IClockStyle
  ///////////////////////////////////
  
  public String getText( final Date date ) {
    // we don't provide the time as text string in the binary clock
    return ""; //$NON-NLS-1$
  }
  
  public Image getPartIcon( final Date date ) {
    return getBinIcon( date );
  }
  
  
  // helping methods
  //////////////////
  
  private Image getBinIcon( final Date date ) {
    SimpleDateFormat sdfH = new SimpleDateFormat( "HH" ); //$NON-NLS-1$
    String hour = sdfH.format( date );
    int iHour = Integer.parseInt( hour );
    
    SimpleDateFormat sdfM = new SimpleDateFormat( "mm" ); //$NON-NLS-1$
    String minutes = sdfM.format( date );
    int iMinutes = Integer.parseInt( minutes );

    return GPU.getBinaryClockIcon( iHour, iMinutes );
  }
}
