package org.eclipsedesktop.packer.bzip2;

import java.io.IOException;
import java.io.InputStream;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipsedesktop.packer.core.*;

public class PackerEngine implements IPackerEngine {

  public boolean isExtensionSupported( final String ext ) {
    return "bz2".equalsIgnoreCase( ext );
  }

  public void extract( final IStorageEditorInput input,
                       final PackerItem[] items,
                       final IPath path,
                       final boolean withSubDirs,
                       final IProgressMonitor monitor )
    throws CoreException
  {
    monitor.beginTask( "Extracting " + input.getStorage().getName(), items.length ); 
    CBZip2InputStream zis = null;
    try {
      InputStream contents = input.getStorage().getContents();
      removeMagicNumbers( contents );
      zis = new CBZip2InputStream( contents );
      boolean written = false;
      for( int i = 0; !written && i < items.length; i++ ) {
        PackerItem packerItem = items[ i ];
        monitor.subTask( packerItem.getFileName() );
        if( new Path( input.getName() ).removeFileExtension().toPortableString().equals( packerItem.getFullPath() ) ) {
          IPath outLocation = ( withSubDirs ) ? path.append( packerItem.getFullPath() )
                                              : path.append( packerItem.getFileName() );
          if( packerItem.isDirectory() ) {
            IOUtil.createDirectory( outLocation );
            monitor.worked( 1 );
          } else {
            IOUtil.createFile( zis,
                               outLocation,
                               new SubProgressMonitor( monitor, 1 ) );
          }
          written = true;
        }
      }
    } finally {
      try {
        if( zis != null ) {
          zis.close();
        }
      } catch( IOException ioe ) {
        //ignore
      } finally {
        monitor.done();
      }
    }
  }

  public PackerItem[] getPackerItems( final IStorageEditorInput input ) {
    PackerItem[] result = new PackerItem[]{
      new PackerItem( false,
                      new Path( input.getName() ).removeFileExtension().toPortableString(),
                      0,
                      0,
                      0 )
    };
    return result;
  }

  public IStorage getEntry(final IStorageEditorInput input, final PackerItem packerItem) {
    return new IStorage(){
      public InputStream getContents() throws CoreException {
        InputStream contents = input.getStorage().getContents();
        removeMagicNumbers( contents );
        return new CBZip2InputStream( contents );
      }
      public IPath getFullPath() {
        return null;
      }
      public String getName() {
        return packerItem.getFileName();
      }
      public boolean isReadOnly() {
        return true;
      }
      public Object getAdapter(Class adapter) {
        return null;
      }
    };
  }

  private static void removeMagicNumbers( final InputStream contents)  {
    byte[] bs = new byte[ 2 ];
    try {
      contents.read( bs, 0, 2 );
    } catch( IOException ioe ) {
      String msg = ioe.getMessage();
      IStatus status = new Status( IStatus.ERROR,
                                   Activator.getDefault().getBundle().getSymbolicName(),
                                   IStatus.ERROR,
                                   msg,
                                   ioe );
      Activator.getDefault().getLog().log( status );

    }
  }

}
