package org.eclipsedesktop.eclipsemail.actions;

import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.eclipsemail.EclipsemailPlugin;
import org.eclipsedesktop.eclipsemail.core.Inbox;
import org.eclipsedesktop.eclipsemail.model.IMailMessage;


public class DeleteAction extends Action {
  
  private Viewer viewer;
  
  public DeleteAction( final Viewer viewer ) {
    this.viewer = viewer;
    EclipsemailPlugin plugin = EclipsemailPlugin.getDefault();
    String pluginId = plugin.getBundle().getSymbolicName();
    setText( "Delete" );
    setToolTipText( "Delete" );
    setImageDescriptor( AbstractUIPlugin.imageDescriptorFromPlugin( pluginId,
                                            "icons/full/eview16/delete.gif" ) );
    
  }
  
  public void run() {
    IStructuredSelection sSel = ( IStructuredSelection )viewer.getSelection();
    Iterator sSelIter = sSel.iterator();
    while( sSelIter.hasNext() ) {
      IMailMessage message = ( IMailMessage )sSelIter.next();
      Inbox.getInstance().deleteMessage( message);
      this.viewer.refresh();
    }
  }
}
