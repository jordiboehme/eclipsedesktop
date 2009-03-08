//Copyright (c) 2004 by Eclipsedesktop.org
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsemail.views;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.eclipsemail.EclipsemailPlugin;
import org.eclipsedesktop.eclipsemail.IColumnIndices;
import org.eclipsedesktop.eclipsemail.IEclipsemailConstants;
import org.eclipsedesktop.eclipsemail.InboxContentProvider;
import org.eclipsedesktop.eclipsemail.InboxLabelProvider;
import org.eclipsedesktop.eclipsemail.actions.DeleteAction;
import org.eclipsedesktop.eclipsemail.model.IMailMessage;

/**
 * @author Leif Frenzel, Jordi Böhme López
 *
 */
public class OnlineInboxView extends ViewPart implements IEclipsemailConstants {

  private TableViewer tv;
  private int refreshInterval;

  // interface methods of ViewPart
  ////////////////////////////////
  
  public void createPartControl( final Composite parent ) {
    tv = new TableViewer( parent, SWT.NONE );
    int[] columnHeaders = new int[] {
        IColumnIndices.FROM,
        IColumnIndices.SUBJECT,
        IColumnIndices.DATE,
        IColumnIndices.SIZE
    };
    String[] columnNames = {
      "Subject",
      "From",
      "Date",
      "Size"
    };
    for( int i = 0; i < columnHeaders.length; i++ ) {
      TableColumn tc = new TableColumn( tv.getTable(), SWT.NONE, i );
      tc.setText( columnNames[ i ] );
      switch( i ) {
        case IColumnIndices.SIZE:
          tc.setAlignment( SWT.RIGHT );
        break;
        case IColumnIndices.DATE:
          tc.setAlignment( SWT.RIGHT );
        break;
        default:
          tc.setAlignment( SWT.LEFT );
        break;
      }
      tc.setResizable( true );
    }
    
    tv.getTable().setLinesVisible( true );
    tv.getTable().setHeaderVisible( true );
    
    tv.setLabelProvider( new InboxLabelProvider() );
    tv.setContentProvider( new InboxContentProvider() );
    tv.setInput( new Object() );

    Table table = tv.getTable();
    TableColumn[] columns = table.getColumns();
    for( int i = 0; i < columns.length; i++ ) {
      columns[ i ].pack();
    }
    
    IPropertyChangeListener listener = new IPropertyChangeListener() {
      public void propertyChange( final PropertyChangeEvent event ) {
        refreshInterval();
      }
    };
    EclipsemailPlugin.getDefault()
      .getPreferenceStore()
      .addPropertyChangeListener( listener );
    
    tv.addDoubleClickListener( new IDoubleClickListener() {
      public void doubleClick( final DoubleClickEvent event ) {
        ISelection sel = event.getSelection();
        IStructuredSelection structSelection = ( IStructuredSelection )sel;
        IMailMessage message = ( IMailMessage )structSelection.getFirstElement();
        showMessage( message );
      }
    } );
    
    tv.addSelectionChangedListener( new ISelectionChangedListener() {
      public void selectionChanged( final SelectionChangedEvent event ) {
        ISelection sel = event.getSelection();
        IStructuredSelection structSelection = ( IStructuredSelection )sel;
        IMailMessage message = ( IMailMessage )structSelection.getFirstElement();
        showMessage( message );
      }
    } );
    
    createActions();
    refreshInterval();
    startUpdater();
  }

  public void setFocus() {
    tv.getControl().setFocus();
  }

  // helping methods
  //////////////////

  public void createActions() {
    Action gcAction = new Action() {
      public void run() {
        updateUI();
      }
    };
    IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
    String actionDescription = "Update Inbox View";
    gcAction.setText( actionDescription );
    EclipsemailPlugin plugin = EclipsemailPlugin.getDefault();
    String pluginId = plugin.getBundle().getSymbolicName();
    gcAction.setToolTipText( actionDescription );
    gcAction.setImageDescriptor( AbstractUIPlugin
      .imageDescriptorFromPlugin( pluginId,
                                  "icons/full/eview16/refresh.gif" ) );
    tbm.add( gcAction );
    tbm.add( new DeleteAction( this.tv ) );
  }

  private void refreshInterval() {
    IPreferenceStore preferenceStore = EclipsemailPlugin.getDefault().getPreferenceStore();
    this.refreshInterval = preferenceStore.getInt( REFRESH_INTERVAL_KEY );
  }

  private void startUpdater() {
    Job job = new Job( "Inbox Monitor" ) {
      protected IStatus run( final IProgressMonitor monitor ) {
        Runnable runnable = new Runnable() {
          public void run() {
            updateUI();
          }
        };
        Display.getDefault().syncExec( runnable );
        schedule( refreshInterval * 1000 );
        return Status.OK_STATUS;
      }
    };
    job.setPriority( Job.SHORT );
    job.setSystem( true );
    job.schedule();
  }

  private void updateUI() {
    if( tv != null && !tv.getControl().isDisposed() ) {
      tv.refresh();
    }
  }
  
  private void showMessage( final IMailMessage message ) {
    if( message != null ) {
      String viewId = "org.eclipsedesktop.eclipsemail.messageView";
      try {
        IViewPart vp = Util.getActiveWindow().getActivePage().showView( viewId );
        if( vp != null && vp instanceof MessageView ) {
          ( ( MessageView )vp ).setMessage( message );
        }
      } catch( PartInitException e ) {
        e.printStackTrace();
      }
    }
  }
}
