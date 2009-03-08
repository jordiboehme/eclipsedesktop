// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.cpumon.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipsedesktop.cpumon.CpumonPlugin;
import org.eclipsedesktop.cpumon.ui.ICpumonConstants;

/**
 * <p>TODO</p>
 * 
 * @author Jordi B�hme L�pez (mail@jordi-boehme.de)
 */
public class CpumonPreferenceInit extends AbstractPreferenceInitializer
                                                   implements ICpumonConstants {

  public void initializeDefaultPreferences() {
    CpumonPlugin plugin = CpumonPlugin.getDefault();
    IPreferenceStore prefStore = plugin.getPreferenceStore();
    prefStore.setDefault( REFRESH_INTERVAL_KEY, REFRESH_INTERVAL_DEFAULT );
  }
}