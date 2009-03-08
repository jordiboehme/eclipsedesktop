//Copyright (c) 2004 by Eclipsedesktop.org
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsemail.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.eclipsemail.IEclipsemailConstants;
import org.eclipsedesktop.eclipsemail.attachments.AttachmentContentProvider;
import org.eclipsedesktop.eclipsemail.attachments.AttachmentLabelProvider;
import org.eclipsedesktop.eclipsemail.model.IMailMessage;

/**
 * @author Jordi Böhme López
 *
 */
public class MessageView extends ViewPart implements IEclipsemailConstants {

  private IMailMessage message;
  private Text subjectValueLabel;
  private Text fromValueLabel;
  private Text dateValueLabel;
  private Text toValueLabel;
  private Text mailContent;
  private TableViewer attachmentViewer;
  
  // interface methods of ViewPart
  ////////////////////////////////

  public void createPartControl( final Composite parent ) {
    GridLayout gridLayoutComp = Util.getGridLayout( 1, false, 1 );
    parent.setLayout( gridLayoutComp );

    Composite headerComp = new Composite( parent, SWT.NONE );
    GridLayout gridLayoutToolBarComp = Util.getGridLayout( 2, false, 2 );
    gridLayoutToolBarComp.marginHeight = 0;
    gridLayoutToolBarComp.marginWidth = 0;
    headerComp.setLayout( gridLayoutToolBarComp );
    headerComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    Label subjectTextLabel = new Label( headerComp, SWT.NONE );
    subjectTextLabel.setText( "Subject:" );
    subjectValueLabel = new Text( headerComp, SWT.NONE );
    subjectValueLabel.setEditable( false );
    subjectValueLabel.setText("" );
    subjectValueLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    
    Label fromTextLabel = new Label( headerComp, SWT.NONE );
    fromTextLabel.setText( "From:" );
    fromValueLabel = new Text( headerComp, SWT.NONE );
    fromValueLabel.setEditable( false );
    fromValueLabel.setText( "" );
    fromValueLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    
    Label dateTextLabel = new Label( headerComp, SWT.NONE );
    dateTextLabel.setText( "Date:" );
    dateValueLabel = new Text( headerComp, SWT.NONE );
    dateValueLabel.setEditable( false );
    dateValueLabel.setText( "" );
    dateValueLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    
    Label toTextLabel = new Label( headerComp, SWT.NONE );
    toTextLabel.setText( "To:" );
    toValueLabel = new Text( headerComp, SWT.NONE );
    toValueLabel.setEditable( false );
    toValueLabel.setText( "" );
    toValueLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    
    mailContent = new Text( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    mailContent.setEditable( false );
    mailContent.setText( "" );
    mailContent.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    
    Display display = parent.getDisplay();
    mailContent.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
    
    createAttachmentViewer( parent );
  }

  public void setFocus() {
    mailContent.setFocus();
  }

  void setMessage( final IMailMessage message ) {
    this.message = message;
    mailContent.setText(  this.message.getContent() );
    subjectValueLabel.setText( this.message.getSubject() );
    fromValueLabel.setText( this.message.getFrom() );
    dateValueLabel.setText( this.message.getDate().toString() );

    StringBuffer recipients = new StringBuffer();
    String[] recipientList = this.message.getRecipients();
    for( int i = 0; i < recipientList.length; i++ ) {
      if( i > 0 ) {
        recipients.append( ", " );
      }
      recipients.append( recipientList[ i ] );
    }
    attachmentViewer.setInput( this.message.getAttachmentNames() );
    TableColumn[] columns = attachmentViewer.getTable().getColumns();
    for( int i = 0; i < columns.length; i++ ) {
      columns[ i ].pack();
    }
    attachmentViewer.refresh( false );
    toValueLabel.setText( recipients.toString() );
  }

  // helping methods
  //////////////////
  
  private void createAttachmentViewer( final Composite parent ) {
    int style = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
    attachmentViewer = new TableViewer( parent, style );
    GridData gridData = new GridData( SWT.FILL, SWT.DEFAULT, true, false );
    gridData.minimumHeight = 50;
    gridData.heightHint = 50;
    attachmentViewer.getControl().setLayoutData( gridData );
    Table table = attachmentViewer.getTable();
    TableColumn tc = new TableColumn(table, SWT.NONE);
    tc.setText( "Attachments" );
    tc.setResizable( true );
    table.setHeaderVisible( true );
    table.setLinesVisible( false );
    attachmentViewer.setContentProvider( new AttachmentContentProvider() );
    attachmentViewer.setInput( new String[ 0 ] );
    attachmentViewer.setLabelProvider( new AttachmentLabelProvider() );
  }

}