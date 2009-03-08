// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsesetimon.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipsedesktop.eclipsesetimon.EclipseSetiPlugin;
import org.eclipsedesktop.eclipsesetimon.ui.ISetiMonConstants;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class SetiPreferencePage extends FieldEditorPreferencePage
                        implements IWorkbenchPreferencePage, ISetiMonConstants {

  public SetiPreferencePage() {
    super( GRID );
  }

  protected void createFieldEditors() {
    FileFieldEditor ffe = new FileFieldEditor( STATEFILE_KEY,
                                               "Status File (state.sah)",
                                               getFieldEditorParent() );
    ffe.setEmptyStringAllowed( false );
    addField( ffe );

    IntegerFieldEditor ife = new IntegerFieldEditor( REFRESH_INTERVAL_KEY,
                                                     "Status refresh interval in seconds",
                                                     getFieldEditorParent() );
    ife.setEmptyStringAllowed( false );
    ife.setValidRange( 1, 86400 );
    addField( ife );
  }

  public void init( final IWorkbench workbench ) {
    IPreferenceStore prefStore = EclipseSetiPlugin.getDefault()
      .getPreferenceStore();
    setPreferenceStore( prefStore );
  }
}