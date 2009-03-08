// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.clocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipsedesktop.clock.ui.clocks.IClockStyle;
import org.eclipsedesktop.clock.ui.internal.ClockUI;
import org.eclipsedesktop.clock.ui.internal.PreferenceNames;
import org.eclipsedesktop.clock.ui.internal.UITexts;


/** <p>a singleton object that manages clock styles.</p> 
  * 
  * @author Leif Frenzel
  */
public class ClockManager {

  private static final String EXTPT_ID 
    = "org.eclipsedesktop.clock.ui.clocks"; //$NON-NLS-1$

  private static final String ATT_ID    = "id"; //$NON-NLS-1$
  private static final String ATT_NAME  = "name"; //$NON-NLS-1$
  private static final String ATT_STYLE = "style"; //$NON-NLS-1$

  // the singleton instance
  private static ClockManager _instance;
  
  private final IClock defaultClock;
  private final List<IClock> clocks;
  private final List<IClockListener> listeners;
  private IClock currentClock;
  
  // constructor is private to prevent instantiation from outside this class  
  private ClockManager() {
    defaultClock= new DefaultClock();
    clocks = new ArrayList<IClock>();
    listeners = new ArrayList<IClockListener>();
    loadClocks();
  }
  
  /** <p>returns a reference to the singleton instance of 
    * <code>ClockStyleManager</code></p>.
    * 
    *  @return  the singleton instance of <code>ClockStyleManager</code>
    */
  public static synchronized ClockManager getInstance() {
    if( _instance == null ) {
      _instance = new ClockManager();
    }
    return _instance;
  }
  
  /** <p>returns the currently active clock style.</p>
    *
    * <p>This returns never <code>null</code>; if no styles are contributed
    * via extensions, an empty dummy implementation is returned.</p>
    */
  public IClock getCurrent() {
    return currentClock;
  }
  
  public IClock[] getAll() {
    return clocks.toArray( new IClock[ clocks.size() ] );
  }
  
  public void setCurrent( final IClock clock ) {
    if( clock == null || !clocks.contains( clock ) ) {
      String msg = "Unknown IClock implementation"; //$NON-NLS-1$
      throw new IllegalArgumentException( msg );
    }
    currentClock = clock;
    saveToPreferences( clock );
    notifyListeners();
  }

  public void addClockListener( final IClockListener listener ) {
    listeners.add( listener );
  }
  
  public void removeClockListener( final IClockListener listener ) {
    listeners.remove( listener );
  }
  
  
  // helping methods
  //////////////////
  
  private void loadClocks() {
    loadExtensions();
    Collections.sort( clocks, new Comparator<IClock>() {
      public int compare( final IClock clock1, final IClock clock2 ) {
        return clock1.getName().compareTo( clock2.getName() );
      }
    } );
    String clockId = lookupFromPrefs();
    if( isEmpty( clockId ) ) {
      initWithDefault(); 
    } else {
      IClock persistedClock = find( clockId );
      if( persistedClock == null ) {
        logWarning( clockId );
        initWithDefault(); 
      } else {
        setCurrent( persistedClock );
      }
    }
  }

  private void logWarning( final String clockId ) {
    String msg = NLS.bind( UITexts.clockManager_couldNotRestore, clockId );
    String pluginId = ClockUI.getPluginId();
    IStatus status = new Status( IStatus.WARNING, pluginId, 0, msg, null );
    ClockUI.getDefault().getLog().log( status );
  }

  private void initWithDefault() {
    currentClock = defaultClock;
    if( clocks.size() > 0 ) {
      setCurrent( clocks.get( 0 ) );
    }
  }

  private void loadExtensions() {
    IExtensionRegistry reg = Platform.getExtensionRegistry();
    IConfigurationElement[] elems = reg.getConfigurationElementsFor( EXTPT_ID );
    for( IConfigurationElement elem: elems ) {
      try {
        String id = elem.getAttribute( ATT_ID );
        String name = elem.getAttribute( ATT_NAME );
        Object style = elem.createExecutableExtension( ATT_STYLE );
        IStatus status = validate( id, name, style );
        if( status.isOK() ) {
          clocks.add( new Clock( id, name, ( IClockStyle )style ) );
        } else {
          ClockUI.getDefault().getLog().log( status );
        }
      } catch( final CoreException cex ) {
        ClockUI.getDefault().getLog().log( cex.getStatus() );
      }
    }
  }
  
  private IStatus validate( final String id, 
                            final String name, 
                            final Object style ) {
    IStatus result = Status.OK_STATUS;
    if( isEmpty( id ) ) {
      result = createWarning( UITexts.clockManager_emptyId );
    } else {
      if( isEmpty( name ) ) {
        String msg = UITexts.clockManager_emptyName;
        result = createWarning( NLS.bind( msg, id ) );
      }
      if( !(style instanceof IClockStyle ) ) {
        String msg = UITexts.clockManager_invalidStyle;
        result = createWarning( NLS.bind( msg, id ) );
      }
    }
    return result;
  }

  private IStatus createWarning( final String msg ) {
    return new Status( IStatus.WARNING, ClockUI.getPluginId(), 0, msg, null );
  }

  private boolean isEmpty( final String candidate ) {
    return candidate == null || candidate.trim().length() == 0;
  }

  private void notifyListeners() {
    Iterator<IClockListener> it = listeners.iterator();
    while( it.hasNext() ) {
      it.next().clockChanged();
    }
  }
  
  private void saveToPreferences( final IClock clock ) {
    getPrefs().setValue( PreferenceNames.CURRENT_CLOCK, clock.getId() );
    ClockUI.getDefault().savePluginPreferences();
  }
  
  private String lookupFromPrefs() {
    return getPrefs().getString( PreferenceNames.CURRENT_CLOCK );
  }

  private Preferences getPrefs() {
    return ClockUI.getDefault().getPluginPreferences();
  }
  
  private IClock find( final String id ) {
    IClock result = null;
    Iterator<IClock> it = clocks.iterator();
    while( result == null && it.hasNext() ) {
      IClock next = it.next();
      if( next.getId().equals( id ) ) {
        result = next;
      }
    }
    return result;
  }
}
