// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.examem.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipsedesktop.examem.ExamemPlugin;
import org.eclipsedesktop.examem.ui.IExamemConstants;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class ExamemPreferencePage extends FieldEditorPreferencePage
                         implements IWorkbenchPreferencePage, IExamemConstants {

  public ExamemPreferencePage() {
    super( GRID );
  }

  protected void createFieldEditors() {
    IntegerFieldEditor rif = new IntegerFieldEditor( REFRESH_INTERVAL_KEY,
                                               "Status refresh interval in seconds",
                                               getFieldEditorParent() );
    rif.setEmptyStringAllowed( false );
    rif.setValidRange( 1, 86400 );
    addField( rif );

    IntegerFieldEditor oom = new IntegerFieldEditor( MIN_FREE_MEM_KEY,
                                                       "Memory threshold in MB "
                                                     + "that shold be free",
                                                   getFieldEditorParent() );
    oom.setEmptyStringAllowed( false );
    oom.setValidRange( 0, Integer.MAX_VALUE );
    addField( oom );
    
    BooleanFieldEditor agc = new BooleanFieldEditor( AUTO_MEM_CLEANUP_KEY,
                                                       "Run automatic memory "
                                                     + "cleanup when threshold "
                                                     + "reached",
                                                 getFieldEditorParent() );
    addField( agc );
  
  }

  public void init( final IWorkbench workbench ) {
    IPreferenceStore prefStore = ExamemPlugin.getDefault()
      .getPreferenceStore();
    setPreferenceStore( prefStore );
  }
}