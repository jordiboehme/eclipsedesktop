// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.ISonicPlayerConstants;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class SonicPreferenceInit extends AbstractPreferenceInitializer 
                                              implements ISonicPlayerConstants {

  public void initializeDefaultPreferences() {
    SonicPlayerPlugin plugin = SonicPlayerPlugin.getDefault();
    plugin.getPreferenceStore().setDefault( ROOTDIR_KEY, Util.getSystemRoot() );
  }
}