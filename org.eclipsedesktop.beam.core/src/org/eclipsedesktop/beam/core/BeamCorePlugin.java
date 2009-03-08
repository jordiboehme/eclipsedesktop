package org.eclipsedesktop.beam.core;

import java.util.ArrayList;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class BeamCorePlugin extends Plugin {
    public static final int PORT = 8006;
	private static BeamCorePlugin plugin;
    private ArrayList<IRecievedListener> recievedListeners
      = new ArrayList<IRecievedListener>();
    
	/**
	 * The constructor.
	 */
	public BeamCorePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception {
		super.start( context );
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception {
		super.stop( context );
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static BeamCorePlugin getDefault() {
		return plugin;
	}

    // recieved Listener
    ////////////////////
    
    public void addRecievedListener( final IRecievedListener listener) {
      recievedListeners.add( listener );
    }

    public void removeRecievedListener( final IRecievedListener listener) {
      recievedListeners.remove( listener );
    }

    public void fireRecievedEvent( final RecievedEvent event ) {
      Object[] listeners = recievedListeners.toArray();
      for (int i = 0; i < listeners.length; ++i) {
        final IRecievedListener l = ( IRecievedListener ) listeners[ i ];
        Runnable runnable = new Runnable() {
          public void run() {
            l.recievedEvent( event );
          }
        };
        Display.getDefault().syncExec( runnable );
      }
    }
      
    
}
