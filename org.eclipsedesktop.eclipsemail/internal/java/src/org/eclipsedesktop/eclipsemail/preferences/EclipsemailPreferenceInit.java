// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsemail.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipsedesktop.eclipsemail.IEclipsemailConstants;
import org.eclipsedesktop.eclipsemail.EclipsemailPlugin;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class EclipsemailPreferenceInit extends AbstractPreferenceInitializer
                                              implements IEclipsemailConstants {

  public void initializeDefaultPreferences() {
    EclipsemailPlugin plugin = EclipsemailPlugin.getDefault();
    IPreferenceStore prefStore = plugin.getPreferenceStore();
    prefStore.setDefault( REFRESH_INTERVAL_KEY, REFRESH_INTERVAL_DEFAULT );
    prefStore.setDefault( POP_HOST_KEY, POP_HOST );
    prefStore.setDefault( POP_PORT_KEY, POP_PORT );
    prefStore.setDefault( POP_PSWD_KEY, POP_PSWD );
    prefStore.setDefault( POP_USER_KEY, POP_USER );
  }
}