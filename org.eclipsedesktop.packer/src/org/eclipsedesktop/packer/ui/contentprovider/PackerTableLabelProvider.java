package org.eclipsedesktop.packer.ui.contentprovider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipsedesktop.packer.core.PackerItem;
import org.eclipsedesktop.packer.ui.IPackerConstants;

public class PackerTableLabelProvider implements ITableLabelProvider, 
                                                 IPackerConstants {

  public Image getColumnImage( final Object element, final int columnIndex ) {
    Image result = null;
    if( element != null && element instanceof PackerItem ) {
      PackerItem item = ( PackerItem )element;
      switch( columnIndex ) {
        case COL_NAME:
          result = item.getImage();
        break;
      }
    }
    return result;
  }

  public String getColumnText( final Object element, final int columnIndex ) {
    String result = "";
    if( element != null && element instanceof PackerItem ) {
      PackerItem item = ( PackerItem )element;
      switch( columnIndex ) {
        case COL_NAME:
          result = item.getFileName();
        break;
        case COL_DATE:
          result = item.getTimeStr();
        break;
        case COL_SIZE:
          result = item.getSizeStr();
        break;
        case COL_COMP:
          result = item.getCompressionStr();
        break;
        case COL_COMPSIZE:
          result = item.getCompressedSizeStr();
        break;
        case COL_PATH:
          result = item.getPath();
        break;
      }
    }
    return result;
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
