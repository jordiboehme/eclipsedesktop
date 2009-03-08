package org.eclipsedesktop.beam.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipsedesktop.beam.ui.preferences.BeamPreferences;

public class ContactDialog extends Dialog {

  protected Text peerNameField;
  protected String peerName = "";

  public ContactDialog( Shell parentShell ) {
    super( parentShell );
  }

  protected void configureShell( Shell newShell ) {
    super.configureShell( newShell );
    newShell.setText( "Enter destination peer name" );
  }

  public void create() {
    super.create();
    peerNameField.setFocus();
  }

  protected Control createDialogArea( Composite parent ) {
    Composite top = new Composite( parent, SWT.NONE );
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    top.setLayout( layout );
    top.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Composite imageComposite = new Composite( top, SWT.NONE );
    layout = new GridLayout();
    imageComposite.setLayout( layout );
    imageComposite.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
    Composite main = new Composite( top, SWT.NONE );
    layout = new GridLayout();
    layout.numColumns = 3;
    main.setLayout( layout );
    main.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    createCategoryFields( main );
    Dialog.applyDialogFont( parent );
    return main;
  }

  protected void createCategoryFields( Composite parent ) {
    new Label( parent, SWT.NONE ).setText( "Peer name: " );
    peerNameField = new Text( parent, SWT.BORDER );
    peerNameField.setText( BeamPreferences.getPeerName() );
    GridData data = new GridData( GridData.FILL_HORIZONTAL );
    data.horizontalSpan = 2;
    data.widthHint = convertHorizontalDLUsToPixels( IDialogConstants.ENTRY_FIELD_WIDTH );
    peerNameField.setLayoutData( data );
  }

  public String getPeerName() {
    return peerName;
  }

  /**
   * Notifies that the ok button of this dialog has been pressed.
   * <p>
   * The default implementation of this framework method sets this dialog's
   * return code to <code>Window.OK</code> and closes the dialog. Subclasses
   * may override.
   * </p>
   */
  protected void okPressed() {
    peerName = peerNameField.getText();
    super.okPressed();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.dialogs.Dialog#close()
   */
  public boolean close() {
    return super.close();
  }
}