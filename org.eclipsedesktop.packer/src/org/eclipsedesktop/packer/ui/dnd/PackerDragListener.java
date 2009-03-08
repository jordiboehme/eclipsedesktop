package org.eclipsedesktop.packer.ui.dnd;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipsedesktop.packer.PackerPlugin;
import org.eclipsedesktop.packer.core.IPackerEngine;
import org.eclipsedesktop.packer.core.PackerItem;

public class PackerDragListener extends DragSourceAdapter {

  private StructuredViewer viewer;

  public PackerDragListener( final StructuredViewer viewer ) {
    this.viewer = viewer;
  }

  public void dragStart( final DragSourceEvent event ) {
  }

  public void dragFinished( final DragSourceEvent event ) {
  }

  public void dragSetData( final DragSourceEvent event ) {
  	String[] result = new String[ 0 ];

  	try {
      IStorageEditorInput input = ( IStorageEditorInput )this.viewer.getInput();
      IPackerEngine engine = PackerPlugin.getDefault().getPackerEngine( input );
      if( engine != null ) {
        PackerItem[] items = getEntryNames();
        try {
          File tempDir = createTempDir();
          engine.extract( input,
                          items,
                          new Path( tempDir.getAbsolutePath() ),
                          false,
                          new NullProgressMonitor() );
          File[] listFiles = tempDir.listFiles();
          result = new String[ listFiles.length ];
          for( int i = 0; i < listFiles.length; i++ ) {
            result[ i ] = listFiles[ i ].getAbsolutePath();
          }
        } catch( final IOException e ) {
          String msg = e.getMessage();
          IStatus status = new Status( IStatus.ERROR,
                                       PackerPlugin.getDefault().getBundle().getSymbolicName(),
                                       IStatus.ERROR,
                                       msg,
                                       e );
          ErrorDialog.openError( viewer.getControl().getShell(),
                                 "An error occured during file extraction.",
                                 msg,
                                 status );
          PackerPlugin.getDefault().getLog().log( status );
        }
      }
  	} catch( final CoreException ce ) {
      ErrorDialog.openError( viewer.getControl().getShell(),
          "An error occured during file extraction.",
          ce.getMessage(),
          ce.getStatus() );
    }
    event.data = result;
  }

  private File createTempDir() throws IOException {
    File tempDir = File.createTempFile( "packer", "DND" );
    if( tempDir.exists() ) {
      tempDir.delete();
    }
    tempDir.mkdirs();
    tempDir.deleteOnExit();
    return tempDir;
  }

  // helping methods
  //////////////////
  
  private PackerItem[] getEntryNames() {
    PackerItem[] result = new PackerItem[ 0 ];
    List<PackerItem> fileList = new ArrayList<PackerItem>();
    IStructuredSelection sel = ( IStructuredSelection )viewer.getSelection();
    Iterator selIter = sel.iterator();
    while( selIter.hasNext() ) {
      PackerItem item = ( PackerItem )selIter.next();
      fileList.add( item );
    }
    result = new PackerItem[ fileList.size() ];
    fileList.toArray( result );
    return result;
  }

}