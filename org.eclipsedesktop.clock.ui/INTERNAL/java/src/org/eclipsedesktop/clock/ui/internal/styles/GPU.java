// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.styles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

class GPU {

  static Image getBinaryClockIcon( final int hour, final int minute ) {
    Display display = Display.getCurrent();
    Image result = new Image( display, 16, 16 );

    GC gc = new GC( result );

    drawBackground( display.getSystemColor( SWT.COLOR_BLACK ), gc );
    drawBlock( hour, 1, display.getSystemColor( SWT.COLOR_RED ), gc );
    drawBlock( minute, 9, display.getSystemColor( SWT.COLOR_YELLOW ), gc );

    gc.dispose();
    return result;
  }


  // helping methods
  //////////////////

  private static void drawBackground( final Color color, final GC gc ) {
    gc.setBackground( color );
    gc.fillRectangle( 0, 0, 16, 16 );
  }

  private static void drawBlock( final int intVal,
                                 final int xOffset,
                                 final Color color,
                                 final GC gc ) {
    gc.setBackground( color );
    int newVal = intVal;
    if( newVal > 10 ) {
      drawColumn( newVal / 10, xOffset, gc );
      newVal = newVal % 10;
    }
    if( newVal > 0 ) {
      drawColumn( newVal, xOffset + 4, gc );
    }
  }

  private static void drawColumn( final int intVal,
                                  final int xOffset,
                                  final GC gc ) {
    int i = 1;
    int row = 3;
    while( row >= 0 ) {
      if ( ( intVal & i ) == i ) {
        int y = ( row * 4 ) + 1;
        gc.fillRectangle( new Rectangle( xOffset,
                                         y,
                                         2,
                                         2 ) );
      }
      i *= 2;
      row--;
    }
  }
}