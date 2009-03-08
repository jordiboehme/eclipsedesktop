package org.eclipsedesktop.eclipsemail.attachments;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.eclipsemail.Util;

public class AttachmentLabelProvider implements ITableLabelProvider {

  public Image getColumnImage( final Object element, final int columnIndex ) {
    return Util.getImageByFilename( ( String )element );
  }

  public String getColumnText( final Object element, final int columnIndex ) {
    return ( String )element;
  }

  public void addListener( final ILabelProviderListener listener ) {
  }

  public void dispose() {
  }

  public boolean isLabelProperty( final Object element, final String property ) {
    return false;
  }

  public void removeListener( final ILabelProviderListener listener ) {
  }
}
