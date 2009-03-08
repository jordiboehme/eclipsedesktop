//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.examem.ui;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López
 */
public class MemoryBar {

  
private static final int WIDGET_HEIGHT = 18;
  private boolean alert;
  private int maximum;
  private int usedSelection;
  private int totalSelection;
  private Label imageLabel;
  
  public MemoryBar( final Composite parent ) {
    imageLabel = new Label( parent, SWT.NONE );
    GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
    gridData.heightHint = WIDGET_HEIGHT;
    imageLabel.setLayoutData( gridData );
    imageLabel.addPaintListener( new PaintListener() {
      public void paintControl( final PaintEvent event ) {
        renderImage( event.gc );
      }
    } );
  }
  
  public boolean isDisposed() {
    return imageLabel.isDisposed();
  }
  
  public boolean isVisible() {
    return imageLabel.isVisible();
  }
  
  public void setToolTipText( final String tooltip ) {
    imageLabel.setToolTipText( tooltip );
  }
  
  public void paint() {
    if( !isDisposed() && isVisible() ) {
      GC gc = new GC( imageLabel );
      renderImage( gc );
      gc.dispose();
    }
  }
  
  public void setMaximum( final int maximum ) {
    this.maximum = maximum;
  }

  public void setAlert( final boolean alert ) {
    this.alert = alert;
  }

  public void setTotalSelection( final int totalSelection ) {
    this.totalSelection = totalSelection;
  }

  public void setUsedSelection( final int usedSelection ) {
    this.usedSelection = usedSelection;
  }
  
  // helping methods
  //////////////////
  
  private void renderImage( final GC gc ) {
    Display display = Display.getCurrent();
    int height = WIDGET_HEIGHT;
    int width = imageLabel.getBounds().width;
    int dataWidth = width - 4;
    if( dataWidth > 0 && isDataAvailable() ) {
      int used = ( int )( dataWidth * ( ( float )usedSelection / ( float )maximum ) );
      int total = ( int )( dataWidth * ( ( float )totalSelection / ( float )maximum ) );
      gc.setForeground( display.getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) ); 
      gc.drawRectangle( 0, 0, width, height );
      
      gc.setBackground( display.getSystemColor( SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW ) ); 
      gc.fillRectangle( 1, 1, width - 1, height - 1 );
      
      gc.setBackground( display.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) ); 
      gc.fillRectangle( 1, 1, width - 2, height - 2 );
      
      Color barColor = null;
      if( this.alert ) {
        barColor = display.getSystemColor( SWT.COLOR_RED );
      } else {
        barColor = display.getSystemColor( SWT.COLOR_LIST_SELECTION );
      }
      gc.setBackground( barColor ); 
      gc.fillRectangle( 2, 2, used, height - 3 );
      
      if( this.alert ) {
        String message = "WARNING";
        FontData df = JFaceResources.getDefaultFont().getFontData()[ 0 ];
        gc.setForeground( display.getSystemColor( SWT.COLOR_WHITE ) ); 
        Font font = new Font( display, df.getName(), height / 2, SWT.BOLD );
        gc.setFont( font );
        int messageStartPos = ( used - gc.stringExtent( message ).x ) / 2;
        gc.drawText( message, messageStartPos, 2 );
        
      }
      
      gc.setForeground( display.getSystemColor( SWT.COLOR_RED ) ); 
      gc.setLineWidth( 1 );
      gc.drawLine( total, 2, total, height - 2 );
    }
    gc.dispose();
  }
    
  private boolean isDataAvailable() {
    return usedSelection > 0 && totalSelection > 0 && maximum > 0;
  }
}
