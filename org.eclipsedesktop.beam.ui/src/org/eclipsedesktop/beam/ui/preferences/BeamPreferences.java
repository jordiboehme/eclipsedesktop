package org.eclipsedesktop.beam.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipsedesktop.beam.ui.BeamUiPlugin;

/**
 * wrapper around preference store for 'typed' access
 * @author Jordi Boehme, Marco van Meegen
 */
public class BeamPreferences extends AbstractPreferenceInitializer {

  /**
   * Preference store key for the user/peerName
   */
  public static final String PEERNAME = "org.eclipsedesktop.beam.peername";

  public static final String AUTOSTART_RECIEVER 
    = "org.eclipsedesktop.beam.autostart";

  public static String getPeerName() {
    String result = getPreferenceStore().getString( PEERNAME );
    return result;
  }

  public static IPreferenceStore getPreferenceStore() {
    return BeamUiPlugin.getDefault().getPreferenceStore();
  }

  public void initializeDefaultPreferences() {
    getPreferenceStore().setDefault( PEERNAME,
                                     System.getProperty( "user.name" ) );
  }
}
