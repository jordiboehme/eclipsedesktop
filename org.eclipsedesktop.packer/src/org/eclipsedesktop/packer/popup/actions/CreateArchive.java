// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.packer.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipsedesktop.packer.PackerPlugin;
import org.eclipsedesktop.packer.core.ZipItem;

public class CreateArchive implements IObjectActionDelegate {

  private static long emptyCrc = new CRC32().getValue();
  private IWorkbenchPart activePart;
  private Object[] objects;
  private ZipOutputStream zipOutStream;
  private ArrayList<ZipItem> addQuery = new ArrayList<ZipItem>();
  private File destZipFile;

  public CreateArchive() {
    super();
  }

  public void setActivePart( final IAction action, final IWorkbenchPart part ) {
    this.activePart = part;
  }

  public void run( final IAction action ) {
    resetValues();
    FileDialog fileDialog = new FileDialog( getShell(), SWT.SAVE );
    fileDialog.setFilterExtensions( new String[]{
      "*.zip"
    } );
    fileDialog.setFilterPath( computeParent( this.objects ) );
    fileDialog.open();
    String filterPath = fileDialog.getFilterPath();
    String fileName = fileDialog.getFileName();
    if( !fileName.equals( "" ) ) {
      destZipFile = new File( filterPath, fileName );
    }
    if( destZipFile != null ) {
      try {
        fillAddQuery( "", this.objects );
        ProgressMonitorDialog pmd = new ProgressMonitorDialog( getShell() );
        pmd.run( false, false, new IRunnableWithProgress() {
          public void run( final IProgressMonitor monitor ) {
            try {
              monitor.beginTask( "Creating archive...", addQuery.size() );
              FileOutputStream fos = new FileOutputStream( destZipFile );
              zipOutStream = new ZipOutputStream( fos );
              zipOutStream.setLevel( 9 );
              for( int i = 0; i < addQuery.size(); i++ ) {
                ZipItem item = addQuery.get( i );
                monitor.subTask( item.getFullName() );
                if( item.isDirectory() ) {
                  addDirectory( item );
                } else if( item.isFile() ) {
                  addFile( item );
                }
                monitor.worked( 1 );
              }
              zipOutStream.close();
            } catch( FileNotFoundException e ) {
              e.printStackTrace();
            } catch( IOException e ) {
              e.printStackTrace();
            }
          }
          
        } );
      } catch( FileNotFoundException exc ) {
        exc.printStackTrace();
      } catch( CoreException exc ) {
        exc.printStackTrace();
      } catch( IOException exc ) {
        exc.printStackTrace();
      } catch( InvocationTargetException e ) {
        e.printStackTrace();
      } catch( InterruptedException e ) {
        e.printStackTrace();
      } finally {
        refreshWorkspaceIfPossible( this.destZipFile );
      }
    }
  }

  private void refreshWorkspaceIfPossible( final File file ) {
    IPath path = new Path( file.getAbsolutePath() );
    IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
    IResource[] resources = null;
    if( file.isDirectory() ) {
      resources = wsRoot.findContainersForLocation( path );
    } else if( file.isFile() ) {
      resources = wsRoot.findFilesForLocation( path );
    }
    HashSet< IResource > todo = new HashSet< IResource >();
    for( int i = 0; resources != null && i < resources.length; i++ ) {
      todo.add( resources[ i ].getParent() );
    }
    Iterator<IResource> iter = todo.iterator();
    while( iter.hasNext() ) {
      IResource element = iter.next();
      try {
        element.refreshLocal( IResource.DEPTH_ONE, new NullProgressMonitor() );
      } catch( CoreException ce ) {
        PackerPlugin.getDefault().getLog().log( ce.getStatus() );
      }
    }
  }

  public void selectionChanged( final IAction action, 
                                final ISelection selection ) {
    this.objects = ( ( IStructuredSelection )selection ).toArray();
  }

  // helping methods
  //////////////////

  private void resetValues() {
    destZipFile = null;
    zipOutStream = null;
    addQuery = new ArrayList<ZipItem>();
  }

  private void fillAddQuery( final String path,
                             final Object[] objectlist )
                                             throws CoreException, IOException {
    for( int i = 0; i < objectlist.length; i++ ) {
      Object object = objectlist[ i ];
      if( object != null && object instanceof IFolder ) {
        IFolder iFolder = ( IFolder )object;
        if( !isMemberOf( objectlist, iFolder ) ) {
          if( iFolder.members().length > 0 ) {
            fillAddQuery( mergePath( path, iFolder.getName() ), iFolder.members() );
          } else {
            ZipItem item = new ZipItem();
            item.setFileObject( iFolder.getLocation().toFile() );
            item.setFullName( mergePath( path, iFolder.getName() + "/" ) );
            addQuery.add( item );
          }
        }
      } else if( object != null && object instanceof IFile ) {
        IFile iFile = ( IFile )object;
        if( !isMemberOf( objectlist, iFile ) ) {
          ZipItem item = new ZipItem();
          item.setFileObject( iFile.getLocation().toFile() );
          item.setFullName( mergePath( path, iFile.getName() ) );
          addQuery.add( item );
        }
      }
    }
  }
  
  private String computeParent( final Object[] objectlist ) {
    IPath parent = null;
    for( int i = 0; i < objectlist.length; i++ ) {
      Object object = objectlist[ i ];
      if( object != null && object instanceof IResource ) {
        IResource res = ( IResource )object;
        if( parent == null ) {
          parent = res.getLocation();
        } else {
          int matching = res.getLocation().matchingFirstSegments( parent );
          parent = res.getLocation().uptoSegment( matching );
        }
      }
    }
    return ( parent == null ) ? "" : parent.toOSString();
  }
  
  private boolean isMemberOf( final Object[] objectlist,
                              final IResource resource ) {
    boolean result = false;
    for( int i = 0; i < objectlist.length && !result; i++ ) {
      Object object = objectlist[ i ];
      if( object != null && object != resource ) {
        if( object instanceof IFolder  ) {
          IFolder iFolder = ( IFolder )object;
          result = iFolder.getLocation().isPrefixOf( resource.getLocation() );
        }
      }
    }
    return result;
  }

  private String mergePath( final String path, final String fileName ) {
    return ( path.equals( "" ) ) ? fileName : path + "/" + fileName;
  }
  
  private void addFile( final ZipItem item ) 
                                     throws FileNotFoundException, IOException {
    ZipEntry zipEntry = new ZipEntry( item.getFullName() );
    File file = item.getFileObject();
    FileInputStream fileInputStream = new FileInputStream( file );
    int length = ( int )file.length();
    byte[] buffer = new byte[ length ];
    fileInputStream.read( buffer, 0, length );
    fileInputStream.close();
    zipEntry.setSize( length );
    zipEntry.setMethod( ZipEntry.DEFLATED );
    zipOutStream.putNextEntry( zipEntry );
    zipOutStream.write( buffer, 0, length );
  }

  private void addDirectory( final ZipItem item ) throws IOException {
    ZipEntry zipEntry = new ZipEntry( item.getFullName() );
    zipEntry.setSize( 0 );
    zipEntry.setMethod( ZipEntry.STORED );
    zipEntry.setCrc( emptyCrc );
    zipOutStream.putNextEntry( zipEntry );
  }

  private Shell getShell() {
    return activePart.getSite().getShell();
  }
}