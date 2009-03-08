// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsemail.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipsedesktop.eclipsemail.IEclipsemailConstants;
import org.eclipsedesktop.eclipsemail.EclipsemailPlugin;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class EclipsemailPreferencePage extends FieldEditorPreferencePage
                    implements IWorkbenchPreferencePage, IEclipsemailConstants {

  public EclipsemailPreferencePage() {
    super( GRID );
  }

  protected void createFieldEditors() {
    IntegerFieldEditor ife = new IntegerFieldEditor( REFRESH_INTERVAL_KEY,
                                           "Status refresh interval in seconds",
                                           getFieldEditorParent() );
    ife.setEmptyStringAllowed( false );
    ife.setValidRange( 60, Integer.MAX_VALUE );
    addField( ife );
    
    StringFieldEditor host = new StringFieldEditor( POP_HOST_KEY,
                                                    "Host",
                                                    getFieldEditorParent() );
    addField( host );

    IntegerFieldEditor port = new IntegerFieldEditor( POP_PORT_KEY,
                                                      "Port",
                                                      getFieldEditorParent() );
    addField( port );

    StringFieldEditor user = new StringFieldEditor( POP_USER_KEY,
                                                    "User",
                                                    getFieldEditorParent() );
    addField( user );

    StringFieldEditor pswd = new StringFieldEditor( POP_PSWD_KEY,
                                                    "Password",
                                                    getFieldEditorParent() );
    addField( pswd );

  }

  public void init( final IWorkbench workbench ) {
    IPreferenceStore prefStore = EclipsemailPlugin.getDefault()
      .getPreferenceStore();
    setPreferenceStore( prefStore );
  }
}