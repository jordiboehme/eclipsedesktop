package org.eclipsedesktop.packer.gzip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipsedesktop.packer.core.*;
import org.osgi.framework.Bundle;

public class PackerEngine implements IPackerEngine {

  public boolean isExtensionSupported( final String ext ) {
    return "gz".equalsIgnoreCase( ext );
  }

  public void extract( final IStorageEditorInput input,
                       final PackerItem[] items,
                       final IPath path,
                       final boolean withSubDirs,
                       final IProgressMonitor monitor )
    throws CoreException
  {
    monitor.beginTask( "Extracting " + input.getStorage().getName(),
                       items.length ); 
    GZIPInputStream zis = null;
    try {
      zis = new GZIPInputStream( input.getStorage().getContents() );
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
    } catch( IOException ioe ) {
      Bundle bundle = Activator.getDefault().getBundle();
      IStatus status = new Status( IStatus.ERROR,
                                   bundle.getSymbolicName(),
                                   IStatus.OK,
                                   ioe.getMessage(),
                                   ioe );
      throw new CoreException( status );
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
        InputStream result = null;
        try {
          result = new GZIPInputStream( input.getStorage().getContents() );
        } catch( IOException e ) {
          String msg = e.getMessage();
          IStatus status = new Status( IStatus.ERROR,
                                       Activator.getDefault().getBundle().getSymbolicName(),
                                       IStatus.ERROR,
                                       msg,
                                       e );
          Activator.getDefault().getLog().log( status );
        }
        return result;
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

}
