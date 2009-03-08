//Copyright (c) 2004 by eclipsedesktop.org
//Jordi Boehme Lopez (mail@jordi-boehme.de)
package org.eclipsedesktop.eclipsesetimon.ui.monitor;

import java.io.File;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.eclipsesetimon.EclipseSetiPlugin;
import org.eclipsedesktop.eclipsesetimon.core.IStatusFile;
import org.eclipsedesktop.eclipsesetimon.core.StatusFileParser;
import org.eclipsedesktop.eclipsesetimon.ui.ISetiMonConstants;
import org.eclipsedesktop.monitorcenter.core.IMonitorCenterContributor;

/**
* <p>TODO</p>
* 
* @author Jordi Böhme López
*/
public class Setimon implements IMonitorCenterContributor, ISetiMonConstants {

  private ProgressBar bar;
  private Label lblElapsedValue;
  private Label lblEstimatedValue;
  private File file;
  private int refreshInterval;

  // interface methods of IMonitorContributor
  ////////////////////////////////
  public void setMonitor( final Composite parent ) {
    Composite monitorComposite = new Composite( parent, SWT.NONE );
    GridLayout gridLayout = Util.getGridLayout( 2, true, 1 );
    monitorComposite.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    monitorComposite.setLayoutData( gd );

    GridData barGd = new GridData( GridData.FILL_HORIZONTAL );
    barGd.horizontalSpan = 2;

    bar = new ProgressBar( monitorComposite, SWT.SMOOTH );
    bar.setLayoutData( barGd );
    
    Label lblElapsed = new Label( monitorComposite, SWT.NONE );
    lblElapsed.setText( "Elapsed:" );
    lblElapsedValue = new Label( monitorComposite, SWT.NONE );
    lblElapsedValue.setText( "00:00:00" );
    Label lblEstimated = new Label( monitorComposite, SWT.NONE );
    lblEstimated.setText( "Estimated:" );
    lblEstimatedValue = new Label( monitorComposite, SWT.NONE );
    lblEstimatedValue.setText( "00:00:00" );
    IPropertyChangeListener listener = new IPropertyChangeListener() {
  
      public void propertyChange( final PropertyChangeEvent event ) {
        refreshInterval();
        startUpdater();
      }
  
    };
    EclipseSetiPlugin.getDefault()
      .getPreferenceStore()
      .addPropertyChangeListener( listener );
    refreshInterval();
    startUpdater();
  }
  
  public void setActions( final IToolBarManager tbm ) {
    // not needed
  }

  // helping methods
  //////////////////
  private void refreshInterval() {
    IPreferenceStore preferenceStore = EclipseSetiPlugin.getDefault()
    .getPreferenceStore();
    this.refreshInterval = preferenceStore.getInt( REFRESH_INTERVAL_KEY );
  }

  private void startUpdater() {
    IPreferenceStore preferenceStore = EclipseSetiPlugin.getDefault()
    .getPreferenceStore();
    String fName = preferenceStore.getString( STATEFILE_KEY );
    file = new File( fName );
    Job job = new Job( "Seti Monitor" ) {
  
      protected IStatus run( final IProgressMonitor monitor ) {
          if(  file.isFile() ) {
            final IStatusFile status = StatusFileParser.parse( file );
            Runnable runnable = new Runnable() {
              public void run() {
                updateUI( status );
              }
            };
            Display.getDefault().syncExec( runnable );
          }
          schedule( refreshInterval * 1000 );
          return Status.OK_STATUS;
        }
    };
    job.setPriority( Job.SHORT );
    job.setSystem( true );
    job.schedule();
  }
  
  private void updateUI( final IStatusFile status ) {
    if( bar != null && !bar.isDisposed() ) {
      int barSelection = ( bar.getMaximum() * status.getProgress() ) / 100;
      bar.setSelection( barSelection );
      lblElapsedValue.setText( status.getElapsed() );
      lblEstimatedValue.setText( status.getEstimated() );
    }
  }
}