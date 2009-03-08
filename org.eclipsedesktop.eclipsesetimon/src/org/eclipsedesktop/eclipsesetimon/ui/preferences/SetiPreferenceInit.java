// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsesetimon.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.eclipsesetimon.EclipseSetiPlugin;
import org.eclipsedesktop.eclipsesetimon.ui.ISetiMonConstants;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class SetiPreferenceInit extends AbstractPreferenceInitializer
                                                  implements ISetiMonConstants {

  public void initializeDefaultPreferences() {
    EclipseSetiPlugin plugin = EclipseSetiPlugin.getDefault();
    IPreferenceStore prefStore = plugin.getPreferenceStore();
    prefStore.setDefault( STATEFILE_KEY, Util.getSystemRoot() );
    prefStore.setDefault( REFRESH_INTERVAL_KEY, REFRESH_INTERVAL_DEFAULT );
  }
}