package org.eclipsedesktop.base.core;

import java.io.File;

public class IOUtil {
  /**
   * Removes the given file from the filesystem. If the given file
   * is an directory the whole directory is removed.
   * @param file an absolute path that specifies the file or directory 
    *            that should be removed from the filesystem.
   */
  public static void delete( final File file ) {
    if( file != null && file.exists() ) {
      doDelete( file );
    }
  }
  
  // HELPING METHODS
  //////////////////
  
  private static void doDelete( final File file ) {
    if( file.isDirectory() ) {
      File[] children = file.listFiles();
      for( int i = 0; i < children.length; i++ ) {
        delete( children[ i ] );
      }
    }
    file.delete();
  }

}
