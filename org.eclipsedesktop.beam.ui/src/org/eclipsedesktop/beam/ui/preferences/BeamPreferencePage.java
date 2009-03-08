package org.eclipsedesktop.beam.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Jordi Boehme
 */
public class BeamPreferencePage extends FieldEditorPreferencePage
  implements IWorkbenchPreferencePage
{

  private StringFieldEditor stringFieldPeerName;
  private BooleanFieldEditor boolFieldAutostart;

  public BeamPreferencePage() {
    super( GRID );
  }

  public void init( final IWorkbench workbench ) {
    // no implementation needed
  }

  public IPreferenceStore getPreferenceStore() {
    return BeamPreferences.getPreferenceStore();
  }

  protected void createFieldEditors() {
    createIntrotext();
    stringFieldPeerName = new StringFieldEditor( BeamPreferences.PEERNAME,
                                                 "Peer name",
                                                 getFieldEditorParent() );

    boolFieldAutostart = new BooleanFieldEditor( BeamPreferences.AUTOSTART_RECIEVER,
                                                 "Autostart reciever",
                                                 getFieldEditorParent() );
    addField( stringFieldPeerName );
    addField(boolFieldAutostart);
  }

  private void createIntrotext() {
    Composite parent = getCompositeEditorParent();
    Label intro = new Label( parent, SWT.NONE );
    String introText = "";
    intro.setText( introText );
  }

  private Composite getCompositeEditorParent() {
    Composite parent = new Composite( getFieldEditorParent(), SWT.NULL );
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    parent.setLayout( layout );
    GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
    gridData.horizontalSpan = 2;
    parent.setLayoutData( gridData );
    parent.setFont( parent.getFont() );
    return parent;
  }


}
