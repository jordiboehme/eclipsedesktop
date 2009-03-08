package org.eclipsedesktop.beam.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipsedesktop.beam.core.*;

public class BeamView extends ViewPart implements IRecievedListener {

  private TreeViewer viewer;
  private Action actionDelete;
//  private Action action2;
  private ArrayList<BeamItem> beamItems;
  
  class BeamViewContentProvider implements ITreeContentProvider {

    public Object[] getElements( Object inputElement ) {
      return getRecievedItems();
    }

    public void dispose() {
    }

    public void inputChanged( final Viewer viewer,
                             final Object oldInput,
                             final Object newInput )
    {
    }

    public Object[] getChildren( final Object parentElement ) {
      return new Object[ 0 ];
    }

    public Object getParent( final Object element ) {
      return null;
    }

    public boolean hasChildren( final Object element ) {
      return false;
    }
  }
  class BeamViewLabelProvider extends LabelProvider {

    public String getText( final Object obj ) {
      String result = obj.toString();
      if( obj instanceof BeamItem ) {
        result = ( ( BeamItem )obj ).getFileName();
      }
      return result;
    }

    public Image getImage( final Object obj ) {
      String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
      if( obj instanceof BeamItem ) {
        imageKey = ISharedImages.IMG_OBJ_FILE;
      }
      return PlatformUI.getWorkbench().getSharedImages().getImage( imageKey );
    }
  }

  public BeamView() {
    this.beamItems = new ArrayList<BeamItem>();
    BeamCorePlugin.getDefault().addRecievedListener( this );
  }

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl( final Composite parent ) {
    viewer = new TreeViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    viewer.setContentProvider( new BeamViewContentProvider() );
    viewer.setLabelProvider( new BeamViewLabelProvider() );
    viewer.setInput( new Object() );

    Transfer[] types = new Transfer[] { FileTransfer.getInstance() };

    viewer.addDragSupport( DND.DROP_COPY,
                           types,
                           new DragSourceListener() {
                              public void dragStart( final DragSourceEvent event ) {
                                // TODO Auto-generated method stub
                              }
                              public void dragSetData( final DragSourceEvent event ) {
                                if( FileTransfer.getInstance().isSupportedType(event.dataType) ) {
                                  ArrayList<String> fileList = new ArrayList<String>();
                                  ISelection selection = viewer.getSelection();
                                  IStructuredSelection ssel = ( IStructuredSelection )selection;
                                  Iterator iter = ssel.iterator();
                                  while( iter.hasNext() ) {
                                    Object obj = iter.next();
                                    if( obj instanceof BeamItem ) {
                                      BeamItem beamItem = ( BeamItem )obj;
                                      String filePath = beamItem.getFile().getAbsolutePath();
                                      fileList.add( filePath );
                                    }
                                  }
                                  String[] files = new String[ fileList.size() ];
                                  fileList.toArray( files );
                                  event.data = files;
                                }
                              }
                              public void dragFinished( final DragSourceEvent event ) {
                                // TODO Auto-generated method stub
                              }
    } );
    makeActions();
    hookContextMenu();
    contributeToActionBars();
  }

  // listener implementation
  // ////////////////////////
  public void recievedEvent( final RecievedEvent event ) {
    this.beamItems.add( event.getBeamItem() );
    this.viewer.refresh();
  }

  // helping methods
  // ////////////////
  private Object[] getRecievedItems() {
    return this.beamItems.toArray();
  }

  private void hookContextMenu() {
    MenuManager menuMgr = new MenuManager( "#PopupMenu" );
    menuMgr.setRemoveAllWhenShown( true );
    menuMgr.addMenuListener( new IMenuListener() {

      public void menuAboutToShow( IMenuManager manager ) {
        BeamView.this.fillContextMenu( manager );
      }
    } );
    Menu menu = menuMgr.createContextMenu( viewer.getControl() );
    viewer.getControl().setMenu( menu );
    getSite().registerContextMenu( menuMgr, viewer );
  }

  private void contributeToActionBars() {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown( bars.getMenuManager() );
    fillLocalToolBar( bars.getToolBarManager() );
  }

  private void fillLocalPullDown( IMenuManager manager ) {
    manager.add( actionDelete );
    manager.add( new Separator() );
//    manager.add( action2 );
  }

  private void fillContextMenu( IMenuManager manager ) {
    manager.add( actionDelete );
//    manager.add( action2 );
    manager.add( new Separator() );
    // Other plug-ins can contribute there actions here
    manager.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
  }

  private void fillLocalToolBar( IToolBarManager manager ) {
    manager.add( actionDelete );
//    manager.add( action2 );
    manager.add( new Separator() );
  }

  private void makeActions() {
    actionDelete = new Action() {
      public void run() {
        ISelection selection = viewer.getSelection();
        IStructuredSelection ssel = ( IStructuredSelection )selection;
        Iterator iter = ssel.iterator();
        while( iter.hasNext() ) {
          Object obj = iter.next();
          if( obj instanceof BeamItem ) {
            BeamItem beamItem = ( BeamItem )obj;
            beamItem.getFile().delete();
            beamItem.getFile().getParentFile().delete();
            beamItems.remove( beamItem );
            viewer.remove( beamItem );
          }
        }
      }
    };
    actionDelete.setText( "Delete" );
    actionDelete.setToolTipText( "Delete selected file" );
    actionDelete.setImageDescriptor( PlatformUI.getWorkbench()
      .getSharedImages()
      .getImageDescriptor( ISharedImages.IMG_TOOL_DELETE ) );

//    action2 = new Action() {
//
//      public void run() {
//        showMessage( "Action 2 executed" );
//      }
//    };
//    action2.setText( "Action 2" );
//    action2.setToolTipText( "Action 2 tooltip" );
//    action2.setImageDescriptor( PlatformUI.getWorkbench()
//      .getSharedImages()
//      .getImageDescriptor( ISharedImages.IMG_OBJS_INFO_TSK ) );
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    viewer.getControl().setFocus();
  }
}