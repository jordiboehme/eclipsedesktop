// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.cpumon.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipsedesktop.cpumon.CpumonPlugin;
import org.eclipsedesktop.cpumon.ui.ICpumonConstants;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class CpumonPreferencePage extends FieldEditorPreferencePage
                         implements IWorkbenchPreferencePage, ICpumonConstants {

  public CpumonPreferencePage() {
    super( GRID );
  }

  protected void createFieldEditors() {
    IntegerFieldEditor ife = new IntegerFieldEditor( REFRESH_INTERVAL_KEY,
                                               "Status refresh interval in seconds",
                                               getFieldEditorParent() );
    ife.setEmptyStringAllowed( false );
    ife.setValidRange( 1, 86400 );
    addField( ife );
  }

  public void init( final IWorkbench workbench ) {
    IPreferenceStore prefStore = CpumonPlugin.getDefault()
      .getPreferenceStore();
    setPreferenceStore( prefStore );
  }
}