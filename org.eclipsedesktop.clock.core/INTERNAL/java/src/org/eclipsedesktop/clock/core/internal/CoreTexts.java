// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal;

import org.eclipse.osgi.util.NLS;

/*** <p>provides access to the internationalized Core texts.</p>
   * 
   * @author Leif Frenzel
   */
public final class CoreTexts {

  
  // internationalized fields
  ///////////////////////////

  public static String timerJob_name;
  
  
  // inits
  ////////

  private static final String BUNDLE_NAME = "coretexts"; //$NON-NLS-1$
  private static final String PCK = CoreTexts.class.getPackage().getName();
  private static final String BUNDLE = PCK + "." + BUNDLE_NAME; //$NON-NLS-1$

  static {
    NLS.initializeMessages( BUNDLE, CoreTexts.class );
  }  
}