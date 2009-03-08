package org.eclipsedesktop.packer.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipsedesktop.packer.core.IOUtil;
import org.eclipsedesktop.packer.core.IPackerEngine;
import org.eclipsedesktop.packer.core.PackerItem;
import org.osgi.framework.Bundle;

public class PackerEngine implements IPackerEngine {

  public boolean isExtensionSupported( final String ext ) {
    return "zip".equalsIgnoreCase( ext ) || "jar".equalsIgnoreCase( ext );
  }

  public void extract( final IStorageEditorInput input,
                       final PackerItem[] items,
                       final IPath path,
                       final boolean withSubDirs,
                       final IProgressMonitor monitor )
    throws CoreException
  {
    monitor.beginTask( "Extracting " + input.getStorage().getName(), items.length ); 
    ZipInputStream zis = new ZipInputStream( input.getStorage().getContents() );
    try {
      ZipEntry entry = zis.getNextEntry();
      while( entry != null ) {
        boolean written = false;
        for( int i = 0; !written && i < items.length; i++ ) {
          PackerItem packerItem = items[ i ];
          monitor.subTask( packerItem.getFileName() );
          if( entry.getName().equals( packerItem.getFullPath() ) ) {
            IPath outLocation = ( withSubDirs ) ? path.append( packerItem.getFullPath() )
                                                : path.append( packerItem.getFileName() );
            if( entry.isDirectory() ) {
              IOUtil.createDirectory( outLocation );
              monitor.worked( 1 );
            } else {
              IOUtil.createFile( IOUtil.readPartialStream( zis ),
                                 outLocation,
                                 new SubProgressMonitor( monitor, 1 ) );
            }
            written = true;
          }
        }
        entry = zis.getNextEntry();
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
        zis.close();
      } catch( IOException ioe ) {
        //ignore
      } finally {
        monitor.done();
      }
    }
  }

  public PackerItem[] getPackerItems( final IStorageEditorInput input ) {
    PackerItem[] result = new PackerItem[0];
    ZipInputStream zis = null;
    try {
      zis = new ZipInputStream(input.getStorage().getContents());
      List<PackerItem> foundEntries = new ArrayList<PackerItem>();
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        if (!zipEntry.isDirectory()) {
          foundEntries.add(new PackerItem(zipEntry));
        }
        zis.closeEntry();
        zipEntry = zis.getNextEntry();
      }
      result = new PackerItem[foundEntries.size()];
      foundEntries.toArray(result);
    } catch (IOException e) {
      String msg = e.getMessage();
      IStatus status = new Status(IStatus.ERROR, Activator.getDefault()
          .getBundle().getSymbolicName(), IStatus.ERROR, msg, e);
      Activator.getDefault().getLog().log(status);
    } catch (final CoreException ce) {
      Activator.getDefault().getLog().log(ce.getStatus());
    } finally {
      if (zis != null) {
        try {
          zis.close();
        } catch (IOException e) {
        }
        zis = null;
      }

    }
    return result;
  }

  public IStorage getEntry( final IStorageEditorInput input,
                            final PackerItem packerItem) {
    return new IStorage() {
      public InputStream getContents() throws CoreException {
        InputStream result = null;
        ZipInputStream zis = null;
        try {
          zis = new ZipInputStream( input.getStorage().getContents() );
          ZipEntry entry = zis.getNextEntry();
          while( result == null && entry != null ) {
            if( entry.getName().equals( packerItem.getFullPath() ) ) {
              result = zis;
            } else {
              zis.closeEntry();
              entry = zis.getNextEntry();
            }
          }
        } catch ( IOException e ) {
          String msg = e.getMessage();
          IStatus status = new Status(IStatus.ERROR, Activator.getDefault()
              .getBundle().getSymbolicName(), IStatus.ERROR, msg, e);
          Activator.getDefault().getLog().log(status);
        }
        if( result == null && zis != null ) {
          try {
            zis.close();
          } catch( IOException e ) {
            //ignore
          }
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
