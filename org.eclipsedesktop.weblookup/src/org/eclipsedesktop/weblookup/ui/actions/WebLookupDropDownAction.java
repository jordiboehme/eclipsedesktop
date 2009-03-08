// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.weblookup.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate2;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.weblookup.WebLookupPlugin;
import org.eclipsedesktop.weblookup.core.ActionProvider;

/**
 * <p>TODO</p>
 * 
 * @author Jordi Boehme Lopez (jboehme@innoopract.de)
 */
public class WebLookupDropDownAction extends Action 
                    implements IMenuCreator, IWorkbenchWindowPulldownDelegate2 {

  private Menu fMenu;
  
  /**
   * <p>Constructs a new LookupDropDownAction</p>
   * 
   * @param partId Part Id needed to get a selection from.
   */
  public WebLookupDropDownAction() {
    String icon = "icons/weblookup.gif";
    WebLookupPlugin plugin = WebLookupPlugin.getDefault();
    String pluginId = plugin.getBundle().getSymbolicName();
    ImageDescriptor imageDescr 
      = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, icon );
    setImageDescriptor( imageDescr );
    setMenuCreator( this );
    setText( "Web Lookup" );
    setToolTipText( "Web Lookup" );
    setActionDefinitionId( "org.eclipsedesktop.weblookup.search" );
  }

  public void dispose() {
    disposeMenu();
  }

  void disposeMenu() {
    if( fMenu != null )
      fMenu.dispose();
  }

  public Menu getMenu( final Menu parent ) {
    disposeMenu();
    fMenu = parent;
    createMenu();
    return fMenu;
  }

  public Menu getMenu( final Control parent ) {
    disposeMenu();
    fMenu = new Menu( parent );
    createMenu();
    return fMenu;
  }

  public void run() {
    if( ActionProvider.getLastAction() == null ) {
      IWorkbench workbench = PlatformUI.getWorkbench();
      Shell shell = workbench.getActiveWorkbenchWindow().getShell();
      String title = "Eclipsedesktop.org: Sorry";
      String message =   "No previous selected action available.\n"
                       + "Please choose a Web Lookup Service first.";
      MessageDialog.openInformation( shell, title, message );
    } else {
      ActionProvider.getLastAction().run();
    }
  }

  public void init( final IWorkbenchWindow window ) {
  }

  public void run( final IAction action ) {
    run();
  }

  public void selectionChanged( final IAction action, 
                                final ISelection selection ) {
  }
  private void createMenu() {
    Action[] actions = ActionProvider.getActions();
    for( int i = 0; i < actions.length; i++ ) {
      addActionToMenu( fMenu, actions[ i ] );
    }
  }

  protected void addActionToMenu( final Menu parent, final Action action ) {
    ActionContributionItem item = new ActionContributionItem( action );
    item.fill( parent, -1 );
  }

}