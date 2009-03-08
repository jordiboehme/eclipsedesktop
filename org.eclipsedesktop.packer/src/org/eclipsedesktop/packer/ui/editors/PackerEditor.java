 package org.eclipsedesktop.packer.ui.editors;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.packer.PackerPlugin;
import org.eclipsedesktop.packer.core.IPackerEngine;
import org.eclipsedesktop.packer.core.PackerItem;
import org.eclipsedesktop.packer.ui.contentprovider.PackerContentProvider;
import org.eclipsedesktop.packer.ui.contentprovider.PackerTableLabelProvider;
import org.eclipsedesktop.packer.ui.contentprovider.PackerTableSorter;
import org.eclipsedesktop.packer.ui.dnd.PackerDragListener;

public class PackerEditor extends EditorPart {

  private TableViewer viewer;
  private String[] columnHeaders = {
    "Name",
    "Modified",
    "Size",
    "Ratio",
    "Packed",
    "Path"
  };
  private int[] columnAlign = {
    SWT.LEFT,
    SWT.RIGHT,
    SWT.RIGHT,
    SWT.RIGHT,
    SWT.RIGHT,
    SWT.LEFT
  };
  private PackerTableSorter sorter = new PackerTableSorter();

  public void doSave( final IProgressMonitor monitor ) {
  }

  public void doSaveAs() {
  }

  public void init( final IEditorSite site, 
                    final IEditorInput input ) throws PartInitException {
    this.setSite( site );
    this.setInput( input );
    if( input instanceof IStorageEditorInput ) {
      this.setPartName( input.getName() );
    }
  }

  public boolean isDirty() {
    return false;
  }

  public boolean isSaveAsAllowed() {
    return false;
  }

  public void createPartControl( final Composite parent ) {
    GridLayout gridLayoutComp = Util.getGridLayout( 1, false, 1 );
    parent.setLayout( gridLayoutComp );
    parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    createTableView( parent );
  }

  public void setFocus() {
  }

  // helping methods
  //////////////////
  
  private void createTableView( final Composite parent ) {
    int style = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.VIRTUAL;
    viewer = new TableViewer( parent, style );
    final Table table = viewer.getTable();
    SelectionListener headerListener = new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        int column = table.indexOf( ( TableColumn )e.widget );
        if( column == sorter.getTopPriority() ) {
          sorter.reverseTopPriority();
        } else {
          sorter.setTopPriority( column );
        }
        viewer.refresh();
      }
    };

    for( int i = 0; i < columnHeaders.length; i++ ) {
      TableColumn tc = new TableColumn(table, SWT.NONE, i);
      tc.setText( columnHeaders[ i ] );
      tc.setResizable( true );
      tc.addSelectionListener( headerListener );
      tc.setAlignment( columnAlign[ i ] );
    }
    table.setHeaderVisible( true );
    table.setLinesVisible( true );

    Transfer[] types = new Transfer[] { FileTransfer.getInstance() };
    
    viewer.addDragSupport( DND.DROP_COPY,
                           types,
                           new PackerDragListener( viewer ) );

    viewer.setUseHashlookup( true );
    viewer.getControl().setLayoutData( new GridData( GridData.FILL_BOTH ) );
    viewer.setContentProvider( new PackerContentProvider() );
    viewer.setInput( getEditorInput() );
    viewer.setLabelProvider( new PackerTableLabelProvider() );
    viewer.setSorter( sorter );
    TableColumn[] columns = table.getColumns();
    for( int i = 0; i < columns.length; i++ ) {
      columns[ i ].pack();
    }
    viewer.addOpenListener( getOpenListener() );
  }

  private IOpenListener getOpenListener() {
    return new IOpenListener(){
      public void open( final OpenEvent event ) {
        IStructuredSelection ssel = ( ( IStructuredSelection )event.getSelection() );
        Object[] items = ssel.toArray();
        for (int i = 0; i < items.length; i++) {
          final PackerItem packerItem = ( PackerItem )items[ i ];
          final IStorageEditorInput input = ( IStorageEditorInput )event.getViewer().getInput();

          IStorageEditorInput editorInput = new IStorageEditorInput() {
            public IStorage getStorage() throws CoreException {
              IStorage result = null;
              IPackerEngine engine = PackerPlugin.getDefault().getPackerEngine( input );
              if( engine != null ) {
                result = engine.getEntry( input, packerItem );
              }
              return result;
            }
            public boolean exists() {
              return false;
            }
            public ImageDescriptor getImageDescriptor() {
              return ImageDescriptor.createFromImage( packerItem.getImage() );
            }
            public String getName() {
              return packerItem.getFileName();
            }
            public IPersistableElement getPersistable() {
              return null;
            }
            public String getToolTipText() {
              return input.getToolTipText() + ":" + getName();
            }
            public Object getAdapter( Class adapter ) {
              return null;
            }
          };
          
          IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          try {
            IWorkbench workbench = PlatformUI.getWorkbench();
            IEditorRegistry editorRegistry = workbench.getEditorRegistry();
            IEditorDescriptor desc
              = editorRegistry.getDefaultEditor( packerItem.getFileName() );
            if( desc != null ) {
              page.openEditor( editorInput, desc.getId() );
            }
          } catch( PartInitException e ) {
            PackerPlugin.getDefault().getLog().log( e.getStatus() );
          }
        }
      }
    };
  }
}
