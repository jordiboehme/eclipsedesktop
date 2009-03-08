//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.monitorcenter.core;

import org.eclipse.jface.resource.ImageDescriptor;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López
 */
public class Monitor {

  private IMonitorCenterContributor monitor;
  private String name;
  private ImageDescriptor imageDescr;

  /**
   * @return Returns the imageDescr.
   */
  public ImageDescriptor getImageDescr() {
    return imageDescr;
  }
  /**
   * @param imageDescr The imageDescr to set.
   */
  public void setImageDescr( final ImageDescriptor imageDescr ) {
    this.imageDescr = imageDescr;
  }
  /**
   * @return Returns the monitor.
   */
  public IMonitorCenterContributor getMonitor() {
    return monitor;
  }
  /**
   * @param monitor The monitor to set.
   */
  public void setMonitor( final IMonitorCenterContributor monitor ) {
    this.monitor = monitor;
  }
  /**
   * @return Returns the name.
   */
  public String getName() {
    return name;
  }
  /**
   * @param name The name to set.
   */
  public void setName( final String name ) {
    this.name = name;
  }
}
