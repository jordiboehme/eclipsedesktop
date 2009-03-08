package org.eclipsedesktop.packer.tar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipsedesktop.packer.core.*;
import org.osgi.framework.Bundle;

public class PackerEngine implements IPackerEngine {

  public boolean isExtensionSupported( final String ext ) {
    return "tar".equalsIgnoreCase( ext );
  }

  public void extract( final IStorageEditorInput input,
                       final PackerItem[] items,
                       final IPath path,
                       final boolean withSubDirs,
                       final IProgressMonitor monitor )
    throws CoreException
  {
    monitor.beginTask( "Extracting " + input.getStorage().getName(), items.length ); 
    TarInputStream tis = new TarInputStream( input.getStorage().getContents() );
    try {
      TarEntry entry = tis.getNextEntry();
      while( entry != null ) {
        boolean written = false;
        for( int i = 0; !written && i < items.length; i++ ) {
          PackerItem packerItem = items[ i ];
          monitor.subTask( packerItem.getFileName() );
          if( entry.getName().equals( packerItem.getFullPath() ) ) {
            IPath outLocation = ( withSubDirs ) ? path.append( packerItem.getFullPath() )
                                                : path.append( packerItem.getFileName() );
            if( packerItem.isDirectory() ) {
              IOUtil.createDirectory( outLocation );
              monitor.worked( 1 );
            } else {
              IOUtil.createFile( IOUtil.readPartialStream( tis ),
                                 outLocation,
                                 new SubProgressMonitor( monitor, 1 ) );
            }
            written = true;
          }
        }
        entry = tis.getNextEntry();
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
        tis.close();
      } catch( IOException ioe ) {
        //ignore
      } finally {
        monitor.done();
      }
    }
  }

  public PackerItem[] getPackerItems( final IStorageEditorInput input ) {
    PackerItem[] result = new PackerItem[0];
    TarInputStream tis = null;
    try {
      tis = new TarInputStream(input.getStorage().getContents());
      List<PackerItem> foundEntries = new ArrayList<PackerItem>();
      TarEntry tarEntry = tis.getNextEntry();
      while( tarEntry != null ) {
        foundEntries.add( new PackerItem( tarEntry.isDirectory(),
                                          tarEntry.getName(),
                                          0,
                                          tarEntry.getSize(),
                                          tarEntry.getModTime().getTime() ) );
        tarEntry = tis.getNextEntry();
      }
      result = new PackerItem[foundEntries.size()];
      foundEntries.toArray(result);
    } catch (IOException e) {
      String msg = e.getMessage();
      IStatus status = new Status( IStatus.ERROR,
                                   Activator.getDefault().getBundle().getSymbolicName(),
                                   IStatus.ERROR,
                                   msg,
                                   e );
      Activator.getDefault().getLog().log( status );
    } catch (final CoreException ce) {
      Activator.getDefault().getLog().log( ce.getStatus() );
    } finally {
      if( tis != null ) {
        try {
          tis.close();
        } catch( IOException e ) {
        }
        tis = null;
      }

    }
    return result;
  }

  public IStorage getEntry( final IStorageEditorInput input,
                            final PackerItem packerItem) {
    return new IStorage() {
      public InputStream getContents() throws CoreException {
        InputStream result = null;
        try {
          TarInputStream tis = new TarInputStream( input.getStorage().getContents() );
          TarEntry entry = tis.getNextEntry();
          while( entry != null ) {
            if( entry.getName().equals( packerItem.getFullPath() ) ) {
              return tis;
            }
            entry = tis.getNextEntry();
          }
          tis.close();
        } catch ( IOException e ) {
          String msg = e.getMessage();
          IStatus status = new Status(IStatus.ERROR, Activator.getDefault()
              .getBundle().getSymbolicName(), IStatus.ERROR, msg, e);
          Activator.getDefault().getLog().log(status);
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

      public Object getAdapter( Class adapter ) {
        return null;
      }
    };
  }

}
