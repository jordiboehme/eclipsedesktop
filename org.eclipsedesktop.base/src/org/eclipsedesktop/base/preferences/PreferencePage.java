// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.base.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/** <p>TODO</p>
 * 
 * @author Jordi B�hme L�pez (mail@jordi-boehme.de)
 */
public class PreferencePage extends FieldEditorPreferencePage
                                           implements IWorkbenchPreferencePage {

  public PreferencePage() {
    super( GRID );
  }

  protected void createFieldEditors() {
  }

  public void init( final IWorkbench workbench ) {
  }
}
