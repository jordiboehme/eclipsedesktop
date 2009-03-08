// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.monitorcenter.core;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (jboehme@innoopract.de)
 */
public interface IMonitorCenterContributor {

  void setMonitor( Composite parent );
  void setActions( IToolBarManager tbm );
}