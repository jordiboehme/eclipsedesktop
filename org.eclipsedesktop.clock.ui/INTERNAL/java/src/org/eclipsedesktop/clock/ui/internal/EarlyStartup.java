// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipsedesktop.clock.core.internal.timer.Timer;
import org.eclipsedesktop.clock.ui.internal.views.ClockView;

/** <p>we have to start early so that we have some initializations in place
  * when the first UI elements are displayed for the clock.</p> 
  * 
  * @author Leif Frenzel
  */
public class EarlyStartup implements IStartup {


  // interface methods of IStartup
  ////////////////////////////////
  
  public void earlyStartup() {
    Timer.getInstance();
    PlatformUI.getWorkbench().addWindowListener( new ActivatingListener() );
  }
  
  
  // inner classes
  ////////////////
  
  private final class ActivatingListener implements IWindowListener {
    public void windowActivated( final IWorkbenchWindow window ) {
      IWorkbenchPage activePage = window.getActivePage();
      IViewReference[] viewReferences = activePage.getViewReferences();
      for( IViewReference ref: viewReferences ) {
        if( ClockView.class.getName().equals( ref.getId() ) ) {
          ref.getView( true );
        }
      }
    }
    
    public void windowClosed( final IWorkbenchWindow window ) {
      // unused
    }
    
    public void windowDeactivated( final IWorkbenchWindow window ) {
      // unused
    }
    
    public void windowOpened( final IWorkbenchWindow window ) {
      // unused
    }
  }
}