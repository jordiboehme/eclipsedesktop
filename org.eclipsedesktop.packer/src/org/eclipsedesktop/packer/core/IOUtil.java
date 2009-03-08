package org.eclipsedesktop.packer.core;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipsedesktop.packer.PackerPlugin;

public class IOUtil {
  
  static final int BUFFER = 2048;

  public static void extract( final ZipFile zipFile,
                              final IFile outFile,
                              final String entryName ) throws IOException,
                                                              CoreException {
    ZipEntry zipEntry = zipFile.getEntry( entryName );
    InputStream zis = zipFile.getInputStream( zipEntry );
    if( outFile.exists() ) {
      outFile.setContents( zis, IFile.FORCE, new NullProgressMonitor() );
    } else {
      outFile.create( zis, IFile.FORCE, new NullProgressMonitor() );
    }
  }

  public static void extract( final ZipFile zipFile,
                              final File outFile,
                              final String entryName ) throws IOException {
    ZipEntry zipEntry = zipFile.getEntry( entryName );
    InputStream zis = zipFile.getInputStream( zipEntry );
    FileOutputStream fos = new FileOutputStream( outFile );
    write( zis, fos );
    fos.flush();
    fos.close();
  }
  
  public static void write( final InputStream is, final OutputStream os ) throws IOException {
    BufferedInputStream bis = new BufferedInputStream( is );
    BufferedOutputStream out = new BufferedOutputStream( os );
    byte[] data = new byte[ BUFFER ];
    while( bis.available() > 0 ) {
      int read = bis.read( data );
      if( read > 0 ) {
        out.write( data, 0, read );
      }
    }
    out.flush();
    out.close();
    os.close();
  }
  
  public static void createFile( final InputStream is,
                                 final IPath path,
                                 final IProgressMonitor monitor )
    throws CoreException
  {
    if( isInWorkspace( path ) ) {
      IFile outFile = getWSFile( path );
      createFolder( outFile.getParent() );
      if( outFile.exists() ) {
        outFile.setContents( is, IResource.FORCE, monitor );
      } else {
        outFile.create( is, IResource.FORCE, monitor );
      }
    } else {
      monitor.beginTask( "Writing " + path.lastSegment(), 1 );
      createDirectory( path.removeLastSegments( 1 ) );
      try {
        FileOutputStream fos = new FileOutputStream( path.toFile() );
        write( IOUtil.readPartialStream( is ), fos );
      } catch( final IOException ioe ) {
        IStatus status = new Status( IStatus.ERROR,
                                     PackerPlugin.getDefault().getBundle().getSymbolicName(),
                                     IStatus.ERROR,
                                     ioe.getLocalizedMessage(),
                                     ioe );
        new CoreException( status );
      } finally {
        monitor.done();
      }
    }
  }

 /** <p>reads the passed stream, without closing it, and buffers all data in
   * it, then returns these data as a new stream. This operation can be seen
   * as creating a substream of <code>is</code>, but one that is independent. 
   * Clients can call this method and close the returned stream safely, 
   * without <code>is</code> being closed.</p> 
   * 
   * @param is            an input stream
   * @return              an input stream that contains everything that could 
   *                      be read from the passed stream
   * @throws IOException  if a problem occured reading the original stream
   */
 public static InputStream readPartialStream( final InputStream is ) 
   throws CoreException
 {
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   try {
    write( is, baos );
  } catch( IOException ioe ) {
    IStatus status = new Status( IStatus.ERROR,
                                 PackerPlugin.getDefault().getBundle().getSymbolicName(),
                                 IStatus.ERROR,
                                 ioe.getLocalizedMessage(),
                                 ioe );
    throw new CoreException( status ); 
  }
   return new ByteArrayInputStream( baos.toByteArray() );
 }
 
  
  private static IFile getWSFile( final IPath location ) {
    IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
    IPath wsLocation = wsRoot.getLocation();
    int matching = wsLocation.matchingFirstSegments( location );
    IPath fullPath = location.removeFirstSegments( matching );
    return wsRoot.getFile( fullPath );
  }

  private static IFolder getWSFolder( final IPath location ) {
    IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
    IPath wsLocation = wsRoot.getLocation();
    int matching = wsLocation.matchingFirstSegments( location );
    IPath fullPath = location.removeFirstSegments( matching );
    return wsRoot.getFolder( fullPath );
  }
  
  private static boolean isInWorkspace( final IPath location ) {
    IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
    return wsRoot.getLocation().isPrefixOf( location );
  }
  
  private static void createFolder( final IContainer container )
    throws CoreException
  {
    if( !container.exists() ) {
      IContainer parent = container.getParent();
      if( !parent.exists() ) {
        createFolder( parent );
      }
      if( parent instanceof IProject ) {
        IFolder folder = ( ( IProject )parent ).getFolder( container.getName() );
        folder.create( true, true, new NullProgressMonitor() );
      } else if( parent instanceof IFolder ) {
        IFolder folder = ( ( IFolder )parent ).getFolder( container.getName() );
        folder.create( true, true, new NullProgressMonitor() );
      }
    }
  }

  public static void createDirectory( final IPath location )
    throws CoreException
  {
    if( isInWorkspace( location ) ) {
      createFolder( getWSFolder( location ) );
    } else {
      File dir = location.toFile();
      if( !dir.exists() ) {
        dir.mkdirs();
      }
    }
  }

}
