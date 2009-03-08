//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.cpumon.ui.monitor;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.cpumon.CpumonPlugin;
import org.eclipsedesktop.cpumon.core.ICPUInfo;
import org.eclipsedesktop.cpumon.core.unix.UnixCPU;
import org.eclipsedesktop.cpumon.core.win32.Win32CPU;
import org.eclipsedesktop.cpumon.ui.ICpumonConstants;
import org.eclipsedesktop.monitorcenter.core.IMonitorCenterContributor;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López
 */
public class Cpumon implements IMonitorCenterContributor, ICpumonConstants {

  private ProgressBar bar;
  private int refreshInterval;

  // interface methods of IMonitorContributor
  ///////////////////////////////////////////
  public void setMonitor( final Composite parent ) {
    Composite monitorComposite = new Composite( parent, SWT.NONE );
    GridLayout gridLayout = Util.getGridLayout( 1, false, 1 );
    monitorComposite.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    monitorComposite.setLayoutData( gd );

    bar = new ProgressBar( monitorComposite,SWT.NONE );
    bar.setMinimum( 0 );
    bar.setMaximum( 100 );
    bar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    IPropertyChangeListener listener = new IPropertyChangeListener() {
        public void propertyChange( final PropertyChangeEvent event ) {
          refreshInterval();
        }
      };
      CpumonPlugin.getDefault()
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
    IPreferenceStore preferenceStore = CpumonPlugin.getDefault()
    .getPreferenceStore();
    this.refreshInterval = preferenceStore.getInt( REFRESH_INTERVAL_KEY );
  }

  private void startUpdater() {
    final ICPUInfo cpuInfo = getCPUInfoByOS();
    Job job = new Job( "CPU Monitor" ) {
      protected IStatus run( final IProgressMonitor monitor ) {
        Runnable runnable = new Runnable() {
          public void run() {
            updateUI( cpuInfo );
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

  private ICPUInfo getCPUInfoByOS() {
    ICPUInfo result = null;
    if( Platform.getOS().equals( Constants.OS_LINUX ) ) {
      result = new UnixCPU();
    } else if( Platform.getOS().equals( Constants.OS_MACOSX ) ) {
      result = new UnixCPU();
    } else if( Platform.getOS().equals( Constants.OS_WIN32 ) ) {
      result = new Win32CPU();
    } 
    return result;
  }

  private void updateUI( final ICPUInfo cpuInfo ) {
    if( bar != null && !bar.isDisposed() ) {
      bar.setSelection( cpuInfo.getUsage() );
    }
  }
}