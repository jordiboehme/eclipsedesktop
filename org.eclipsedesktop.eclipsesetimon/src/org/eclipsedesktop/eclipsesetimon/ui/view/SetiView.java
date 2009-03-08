// Copyright (c) 2004 by eclipsedesktop.org
//Jordi Boehme Lopez (mail@jordi-boehme.de)
package org.eclipsedesktop.eclipsesetimon.ui.view;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.eclipsesetimon.ui.monitor.Setimon;
import org.eclipsedesktop.monitorcenter.core.IMonitorCenterContributor;

/**
 * <p>
 * a view for the information that a running seti client vomits out.
 * </p>
 * 
 * @author Jordi Böhme López
 */
public class SetiView extends ViewPart {

  // interface methods of ViewPart
  ////////////////////////////////
  public void createPartControl( final Composite parent ) {
    GridLayout gridLayout = Util.getGridLayout( 1, false, 1 );
    parent.setLayout( gridLayout );
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    parent.setLayoutData( gd );
    IMonitorCenterContributor monitor = new Setimon();
    monitor.setMonitor( parent );
  }

  public void setFocus() {
    // TODO
  }

}