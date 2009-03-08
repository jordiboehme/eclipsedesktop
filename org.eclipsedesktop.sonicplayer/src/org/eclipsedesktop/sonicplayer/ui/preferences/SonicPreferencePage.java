//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.ISonicPlayerConstants;


/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class SonicPreferencePage extends FieldEditorPreferencePage
                    implements IWorkbenchPreferencePage, ISonicPlayerConstants {

  public SonicPreferencePage() {
    super( GRID );
  }

  protected void createFieldEditors() {
    DirectoryFieldEditor dfe = new DirectoryFieldEditor( ROOTDIR_KEY, 
                                                         "Music root directory",
                                                         getFieldEditorParent() 
                                                       );
    dfe.setEmptyStringAllowed( false );
    addField( dfe );
  }

  public void init( final IWorkbench workbench ) {
    IPreferenceStore prefStore = SonicPlayerPlugin.getDefault()
      .getPreferenceStore();
    setPreferenceStore( prefStore );
  }
}
