package org.eclipsedesktop.packer.ui.contentprovider;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipsedesktop.packer.core.PackerItem;
import org.eclipsedesktop.packer.ui.IPackerConstants;

public class PackerTableSorter extends ViewerSorter implements IPackerConstants {
  
  final static int ASCENDING = 1;
  final static int DEFAULT_DIRECTION = 0;
  final static int DESCENDING = -1;
  final static int[] DEFAULT_PRIORITIES = { COL_PATH, 
                                            COL_NAME, 
                                            COL_DATE, 
                                            COL_SIZE, 
                                            COL_COMPSIZE, 
                                            COL_COMP };
  final static int[] DEFAULT_DIRECTIONS = { ASCENDING,
                                            ASCENDING,
                                            ASCENDING,
                                            ASCENDING,
                                            ASCENDING,
                                            ASCENDING };
  private int[] priorities;
  private int[] directions;

  public PackerTableSorter() {
    resetState();
  }
  
  public int compare( final Viewer viewer, final Object o1, final Object o2 ) {
    PackerItem e1 = ( PackerItem )o1;
    PackerItem e2 = ( PackerItem )o2;
    return compareColumnValue( e1, e2, 0 );
  }

  public void resetState() {
    priorities = new int[ DEFAULT_PRIORITIES.length ];
    System.arraycopy( DEFAULT_PRIORITIES, 0, priorities, 0, priorities.length );
    directions = new int[ DEFAULT_DIRECTIONS.length ];
    System.arraycopy( DEFAULT_DIRECTIONS, 0, directions, 0, directions.length );
  }

  public void setTopPriority( final int priority ) {
    if( priority >= 0 && priority < priorities.length ) {
      int index = -1;
      for( int i = 0; index < 0 && i < priorities.length; i++ ) {
        if( priorities[ i ] == priority ) {
          index = i;
        }
      }

      if( index > -1 ) {
        for( int i = index; i > 0; i-- ) {
          priorities[ i ] = priorities[ i - 1 ];
        }
        priorities[ 0 ] = priority;
        directions[ priority ] = DEFAULT_DIRECTIONS[ priority ];
      }
    }
  }

  public int getTopPriority() {
    return priorities[ 0 ];
  }

  public void reverseTopPriority() {
    directions[ priorities[ 0 ] ] *= -1;
  }

  public void setTopPriorityDirection( final int direction ) {
    if( direction == DEFAULT_DIRECTION ) {
      directions[ priorities[ 0 ] ] = DEFAULT_DIRECTIONS[ priorities[ 0 ] ];
    } else if( direction == ASCENDING || direction == DESCENDING ) {
      directions[ priorities[ 0 ] ] = direction;
    }
  }

  public int getTopPriorityDirection() {
    return directions[ priorities[ 0 ] ];
  }

  private int compareColumnValue( final PackerItem e1,
                                  final PackerItem e2, 
                                  final int depth ) {
    int result = 0;
    if( depth < priorities.length ) {
      int columnNumber = priorities[ depth ];
      int direction = directions[ columnNumber ];
      switch( columnNumber ) {
        case COL_NAME:
          result = collator.compare( e1.getFileName(), e2.getFileName() );
          if( result == 0 ) {
            result = compareColumnValue( e1, e2, depth + 1 );
          } else {
            result = result * direction;
          }
        break;
        case COL_PATH:
          result = collator.compare( e1.getPath(), e2.getPath() );
          if (result == 0) {
            result = compareColumnValue( e1, e2, depth + 1 );
          } else {
            result = result * direction;
          }
        break;
        case COL_COMP:
          result =  0;
          int diff = e1.getCompression() - e2.getCompression(); 
          if( diff > 0 ) {
            result =  1;
          } else if( diff < 0 ) {
            result =  -1;
          }
          if( result == 0 ) {
            result = compareColumnValue( e1, e2, depth + 1 );
          } else {
            result = result * direction;
          }
        break;
        case COL_DATE:
          result =  0;
          long timediff = e1.getTime() - e2.getTime(); 
          if( timediff > 0 ) {
            result =  1;
          } else if( timediff < 0 ) {
            result =  -1;
          }
          if( result == 0 ) {
            result = compareColumnValue( e1, e2, depth + 1 );
          } else {
            result = result * direction;
          }
        break;
        case COL_SIZE:
          result =  0;
          long sizediff = e1.getSize() - e2.getSize(); 
          if( sizediff > 0 ) {
            result =  1;
          } else if( sizediff < 0 ) {
            result =  -1;
          }
          if( result == 0 ) {
            result = compareColumnValue( e1, e2, depth + 1 );
          } else {
            result = result * direction;
          }
        break;
        case COL_COMPSIZE:
          result =  0;
          long compsizediff = e1.getCompressedSize() - e2.getCompressedSize(); 
          if( compsizediff > 0 ) {
            result =  1;
          } else if( compsizediff < 0 ) {
            result =  -1;
          }
          if( result == 0 ) {
            result = compareColumnValue( e1, e2, depth + 1 );
          } else {
            result = result * direction;
          }
        break;
      }
    }
    return result;
  }
}
