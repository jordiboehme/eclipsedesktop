/*
 * Created on 17.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipsedesktop.eclipsemail;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.eclipsemail.model.IMailMessage;



/**
 * @author lfrenzel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InboxLabelProvider extends LabelProvider 
                                implements ITableLabelProvider, IColumnIndices {

  public Image getColumnImage( final Object element, 
                               final int columnIndex ) {
    // TODO Auto-generated method stub
    return null;
  }

  public String getColumnText( final Object element, 
                               final int columnIndex ) {
    String result = "";
    IMailMessage message = ( IMailMessage )element;
    try {
      switch( columnIndex ) {
        case FROM:
          result = message.getFrom();
          break;
        case SUBJECT:
          result = message.getSubject();
          break;
        case DATE:
          result = message.getDate().toString();
          break;
        case SIZE:
          result = String.valueOf( message.getSize() );
          break;
        default:
          // TODO
          System.err.println( "What's that?" );
      }
    } catch( Exception ex ) {
      ex.printStackTrace();
    }
    return result;
  }
}
