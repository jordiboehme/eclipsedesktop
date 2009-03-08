//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.examem.ui.monitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.examem.ExamemPlugin;
import org.eclipsedesktop.examem.core.Memory;
import org.eclipsedesktop.examem.ui.IExamemConstants;
import org.eclipsedesktop.examem.ui.MemoryBar;
import org.eclipsedesktop.monitorcenter.core.IMonitorCenterContributor;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López
 */
public class Examem implements IMonitorCenterContributor, IExamemConstants {

  private MemoryBar memoryBar;
  private int refreshInterval = REFRESH_INTERVAL_DEFAULT;
  private int minFreeMem = MIN_FREE_MEM_DEFAULT;
  private long lastMemoryCleanup = 0;
  private boolean autoMemCleanup = AUTO_MEM_CLEANUP_DEFAULT;

  // interface methods of IMonitorContributor
  ///////////////////////////////////////////
  public void setMonitor( final Composite parent ) {
    Composite monitorComposite = new Composite( parent, SWT.NONE );
    GridLayout gridLayout = Util.getGridLayout( 1, false, 1 );
    monitorComposite.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    monitorComposite.setLayoutData( gd );

    memoryBar = new MemoryBar( monitorComposite );
    
    IPropertyChangeListener listener = new IPropertyChangeListener() {
        public void propertyChange( final PropertyChangeEvent event ) {
          setPreferences();
        }
      };
      ExamemPlugin.getDefault()
        .getPreferenceStore()
        .addPropertyChangeListener( listener );
    setPreferences();
    startUpdater();
  }

  public void setActions( final IToolBarManager tbm ) {
    Action gcAction = new Action() {
      public void run() {
        cleanupMemory( 0 );
        updateUI();
      }
    };
    String actionDescription = "Memory Cleanup";
    gcAction.setText( actionDescription );
    ExamemPlugin plugin = ExamemPlugin.getDefault();
    String pluginId = plugin.getBundle().getSymbolicName();
    gcAction.setToolTipText( actionDescription );
    gcAction.setImageDescriptor( AbstractUIPlugin
      .imageDescriptorFromPlugin( pluginId,
                                  "icons/full/eview16/gc.gif" ) );
    tbm.add( gcAction );
  }

  // helping methods
  //////////////////
  private void setPreferences() {
    IPreferenceStore prefStore = getPreferenceStore();
    refreshInterval = prefStore.getInt( REFRESH_INTERVAL_KEY );
    minFreeMem = prefStore.getInt( MIN_FREE_MEM_KEY ) * 1024 * 1024;
    autoMemCleanup = prefStore.getBoolean( AUTO_MEM_CLEANUP_KEY );
  }
  
  public void cleanupMemory( final int delay ) {
    if( lastMemoryCleanup + ( delay * 1000 ) < System.currentTimeMillis() ) {
      Memory.clearMemory();
      lastMemoryCleanup = System.currentTimeMillis();
    }
  }

  private IPreferenceStore getPreferenceStore() {
    return ExamemPlugin.getDefault().getPreferenceStore();
  }
  
  private int convertBytesToMegaBytes( final int bytes ) {
    return bytes / 1024 / 1024;
  }

  private void startUpdater() {
    Job job = new Job( "Examine Memory" ) {
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
    if(    memoryBar != null
        && !memoryBar.isDisposed()
        && memoryBar.isVisible() ) {
      int totalMemory = Memory.getTotalMemory();
      int usedMemory = Memory.getUsedMemory();
      int maxMemory = Memory.getMaxMemory();
      if( Memory.getAvailableMemory() < this.minFreeMem ) {
        memoryBar.setAlert( true );
        if( autoMemCleanup ) {
          cleanupMemory( 10 );
        }
      } else {
        memoryBar.setAlert( false );
      }
      memoryBar.setMaximum( maxMemory );
      memoryBar.setTotalSelection( totalMemory );
      memoryBar.setUsedSelection( usedMemory );
      memoryBar.paint();
      int usedMb = convertBytesToMegaBytes( usedMemory );
      int totalMb = convertBytesToMegaBytes( totalMemory );
      int maxMb = convertBytesToMegaBytes( maxMemory );
      memoryBar.setToolTipText(   "Used: " + usedMb + "MB\n"
                                + "Total: " + totalMb + "MB\n"
                                + "Limit: " + maxMb + "MB" );
    }
  }
}