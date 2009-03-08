// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal;

import org.eclipse.osgi.util.NLS;

/** <p>provides access to the internationalized UI texts.</p>
  * 
  * @author Leif Frenzel
  */
public final class UITexts {

  // internationalized fields
  ///////////////////////////
  
  public static String clockManager_couldNotRestore;
  public static String clockManager_emptyId;
  public static String clockManager_emptyName;
  public static String clockManager_invalidStyle;

  public static String clockView_grpAlarms;
  public static String clockView_grpTime;
  public static String clockView_mnuClockStyles;

  public static String defaultClock_noClocks;

  
  // inits
  ////////
  
  private static final String BUNDLE_NAME = "uitexts"; //$NON-NLS-1$
  private static final String PACKAGE = UITexts.class.getPackage().getName();
  private static final String BUNDLE 
    = PACKAGE + "." + BUNDLE_NAME; //$NON-NLS-1$
  
  static {
    NLS.initializeMessages( BUNDLE, UITexts.class );
  }
}