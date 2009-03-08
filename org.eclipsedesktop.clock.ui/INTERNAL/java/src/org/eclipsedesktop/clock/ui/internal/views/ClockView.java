// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipsedesktop.clock.core.internal.timer.ITimerListener;
import org.eclipsedesktop.clock.core.internal.timer.Timer;
import org.eclipsedesktop.clock.ui.internal.UITexts;
import org.eclipsedesktop.clock.ui.internal.clocks.ClockManager;
import org.eclipsedesktop.clock.ui.internal.clocks.IClock;
import org.eclipsedesktop.clock.ui.internal.clocks.IClockListener;

/** <p>the view part that is responsible for displaying the time to the 
  * user.</p> 
  *
  * @author Leif Frenzel
  */
public class ClockView extends ViewPart {

  private Group grpAlarms;
  private Group grpTime;
  private Label lblTime;

  private Image oldIcon;
  private IMenuManager mmStyles;
  
  // interface methods of ViewPart
  ////////////////////////////////

  @Override
  public void createPartControl( final Composite parent ) {
    parent.setLayout( new GridLayout() );
    
    initTimeGroup( parent );
    initAlarmsGroup( parent );
    initActions();
    
    update();
    initClockListener();
    initTimeListener();
  }

  @Override
  public void setFocus() {
    // unused
  }
  
  @Override
  public Image getTitleImage() {
    IClock clock = ClockManager.getInstance().getCurrent();
    Image newIcon = clock.getClockStyle().getPartIcon( new Date() );
    Image result;
    if( newIcon != null  && !newIcon.isDisposed() ) {
      result = newIcon;
      if( oldIcon != null ) {
        oldIcon.dispose();
      }
      oldIcon = newIcon;
    } else if( oldIcon != null && !oldIcon.isDisposed() ) {
      result = oldIcon;
    } else {
      result = super.getTitleImage();
    }
    return result;
  }

  
  // helping methods
  //////////////////

  private void initTimeGroup( final Composite parent ) {
    grpTime = new Group( parent, SWT.NONE );
    grpTime.setLayout( new GridLayout() );
    grpTime.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    grpTime.setText( UITexts.clockView_grpTime );
    
    lblTime = new Label( grpTime, SWT.NONE );
    lblTime.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
  }

  private void initAlarmsGroup( final Composite parent ) {
    grpAlarms = new Group( parent, SWT.NONE );
    grpAlarms.setLayout( new GridLayout() );
    grpAlarms.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    grpAlarms.setText( UITexts.clockView_grpAlarms );

    Label lbl = new Label( grpAlarms, SWT.NONE );
    lbl.setText( "Currently not supported." );
//    IAlarm[] alarms = AlarmsManager.getInstance().getAll();
//    for( IAlarm alarm: alarms ) {
//      
//    }
  }

  private void initActions() {
    IActionBars actionBars = getViewSite().getActionBars();
    IMenuManager mm = actionBars.getMenuManager();
//    mm.add( new Action( "Show Alarms" ) {
//      // TODO lf show alarm panel
//      // show actions for creating and deleting alarms
//    } );
    
    mm.add( new Separator() );
    
    mmStyles = new MenuManager( UITexts.clockView_mnuClockStyles );
    updateMMStyles();
    mm.add( mmStyles );
  }

  private void updateMMStyles() {
    mmStyles.removeAll();
    IClock[] clocks = ClockManager.getInstance().getAll();
    for( IClock clock: clocks ) {
      Action action = createClockAction( clock );
      mmStyles.add( action );
    }
  }

  private Action createClockAction( final IClock clock ) {
    Action result = new Action( clock.getName() ) {
      @Override
      public void run() {
        ClockManager.getInstance().setCurrent( clock );
      }
    };
    if( ClockManager.getInstance().getCurrent() == clock ) {
      result.setChecked( true );
    }
    return result;
  }

  private void initClockListener() {
    ClockManager.getInstance().addClockListener( new IClockListener() {
      public void clockChanged() {
        updateMMStyles();
        update();
      }
    } );
  }

  private void initTimeListener() {
    Timer.getInstance().addTimeListener( new ITimerListener() {
      public void secondChanged() {
        // not needed here
      }
      public void minuteChanged() {
        Runnable op = new Runnable() {
          public void run() {
            update();
          }
        };
        Display.getDefault().syncExec( op );
      }
    } );
  }
  
  
  // UI updating with new time
  
  private void update() {
    Date date = new Date();
    updateTimeGroup( date );
    updatePartName( date );
    firePropertyChange( IWorkbenchPart.PROP_TITLE );
  }

  private void updateTimeGroup( final Date date ) {
    IClock current = ClockManager.getInstance().getCurrent();
    String text = current.getClockStyle().getText( date );
    if( text == null ) {
      text = ""; //$NON-NLS-1$
    }
    lblTime.setText( text );
  }

  private void updatePartName( final Date date ) {
    SimpleDateFormat sdf = new SimpleDateFormat( "dd.MM.yyyy" ); //$NON-NLS-1$
    String text = sdf.format( date );
    setPartName( text );
  }
}
