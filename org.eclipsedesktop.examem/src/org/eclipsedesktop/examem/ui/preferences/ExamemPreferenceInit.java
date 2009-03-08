// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.examem.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipsedesktop.examem.ExamemPlugin;
import org.eclipsedesktop.examem.ui.IExamemConstants;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class ExamemPreferenceInit extends AbstractPreferenceInitializer
                                                   implements IExamemConstants {

  public void initializeDefaultPreferences() {
    ExamemPlugin plugin = ExamemPlugin.getDefault();
    IPreferenceStore prefStore = plugin.getPreferenceStore();
    prefStore.setDefault( REFRESH_INTERVAL_KEY, REFRESH_INTERVAL_DEFAULT );
    prefStore.setDefault( MIN_FREE_MEM_KEY, MIN_FREE_MEM_DEFAULT );
    prefStore.setDefault( AUTO_MEM_CLEANUP_KEY, AUTO_MEM_CLEANUP_DEFAULT );
  }
}