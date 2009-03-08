// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.core.playlist;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.widgets.Display;
import org.eclipsedesktop.sonicplayer.core.listener.FinishedEvent;
import org.eclipsedesktop.sonicplayer.core.listener.IFinishedListener;
import org.eclipsedesktop.sonicplayer.core.listener.IPausedListener;
import org.eclipsedesktop.sonicplayer.core.listener.IPlayingListener;
import org.eclipsedesktop.sonicplayer.core.listener.IStopedListener;
import org.eclipsedesktop.sonicplayer.core.listener.PausedEvent;
import org.eclipsedesktop.sonicplayer.core.listener.PlayingEvent;
import org.eclipsedesktop.sonicplayer.core.listener.StopedEvent;

/** <p>Data Object containing the playlist as a Singleton with methods for 
  * adding and sorting.</p>
  * 
  * @author Jordi Böhme López (mail@jordi-boehme.de)
  */
public class Playlist {

  private static final int PREVIOUS_ELEMENT_OFFSET = -1;
  private static final int NEXT_ELEMENT_OFFSET = 1;
  private static Playlist _instance;
  private static final int STEP_DOWN = 1;
  private static final int STEP_UP = PREVIOUS_ELEMENT_OFFSET;
  private Object[] elements = new Object[ 0 ];

  private ListenerList playingListeners = new ListenerList();
  private ListenerList pausedListeners = new ListenerList();
  private ListenerList finishedListeners = new ListenerList();
  private ListenerList stopedListeners = new ListenerList();

  private Playlist() {
  }

  public static Playlist getInstance() {
    if( _instance == null ) {
      _instance = new Playlist();
    }
    return _instance;
  }

  public Object[] getElements() {
    return elements;
  }

  public int getElementIndex( final Object obj ) {
    int result = -1;
    for( int i = 0; i < elements.length && result < 0; i++ ) {
      if( obj == elements[ i ] ) {
        result = i;
      }
    }
    return result;
  }
  
  public void setElements( final Object[] elements ) {
    this.elements = elements;
  }

  public void addElement( final Object element ) {
    Object[] newElements = new Object[ elements.length + 1 ];
    newElements[ elements.length ] = element;
    for( int i = 0; i < elements.length; i++ ) {
      newElements[ i ] = elements[ i ];
    }
    this.elements = newElements; 
  }

  public void moveUp( final Object obj ) {
    move( obj, STEP_UP );
  }

  public void moveDown( final Object obj ) {
    move( obj, STEP_DOWN );
  }

  public Object getNext( final Object obj ) {
    return getRelativeObject( obj, NEXT_ELEMENT_OFFSET );
  }

  public Object getPrevious( final Object obj ) {
    return getRelativeObject( obj, PREVIOUS_ELEMENT_OFFSET );
  }

  public void remove( final Object obj ) {
    int objIndex = -1;
    for( int i = 0; i < elements.length; i++ ) {
      if( obj == elements[ i ] ) {
        objIndex = i;
      }
    }
    if( objIndex >= 0 ) {
      Object[] newElements = new Object[ elements.length - 1 ];
      int index = 0;
      for( int i = 0; i < elements.length; i++ ) {
        if( i != objIndex ) {
          newElements[ index ] = elements[ i ];
          index++;
        }
      }
      this.elements = newElements;
    }
  }

  // playing Listener
  ///////////////////
  
  public void addPlayingListener( final IPlayingListener listener) {
    playingListeners.add( listener );
  }

  public void removePlayingListener( final IPlayingListener listener) {
    playingListeners.remove( listener );
  }

  public void firePlayingEvent( final PlayingEvent event ) {
    Object[] listeners = playingListeners.getListeners();
    for (int i = 0; i < listeners.length; ++i) {
      final IPlayingListener l = ( IPlayingListener ) listeners[ i ];
      Runnable runnable = new Runnable() {
        public void run() {
          l.playingEvent( event );
        }
      };
      Display.getDefault().syncExec( runnable );
    }
  }
  
  // paused Listener
  //////////////////
  
  public void addPausedListener( final IPausedListener listener) {
    pausedListeners.add( listener );
  }

  public void removePausedListener( final IPausedListener listener) {
    pausedListeners.remove( listener );
  }

  public void firePausedEvent( final PausedEvent event ) {
    Object[] listeners = pausedListeners.getListeners();
    for (int i = 0; i < listeners.length; ++i) {
      final IPausedListener l = ( IPausedListener ) listeners[ i ];
      Runnable runnable = new Runnable() {
        public void run() {
          l.pausedEvent( event );
        }
      };
      Display.getDefault().syncExec( runnable );
    }
  }
  
  // finished Listener
  ////////////////////
  
  public void addFinishedListener( final IFinishedListener listener) {
    finishedListeners.add( listener );
  }

  public void removeFinishedListener( final IFinishedListener listener) {
    finishedListeners.remove( listener );
  }

  public void fireFinishedEvent( final FinishedEvent event ) {
    Object[] listeners = finishedListeners.getListeners();
    for (int i = 0; i < listeners.length; ++i) {
      final IFinishedListener l = ( IFinishedListener ) listeners[ i ];
      Runnable runnable = new Runnable() {
        public void run() {
          l.finishedEvent( event );
        }
      };
      Display.getDefault().syncExec( runnable );
    }
  }
  
  // stoped Listener
  //////////////////
  
  public void addStopedListener( final IStopedListener listener) {
    stopedListeners.add( listener );
  }

  public void removeStopedListener( final IStopedListener listener) {
    stopedListeners.remove( listener );
  }

  public void fireStopedEvent( final StopedEvent event ) {
    Object[] listeners = stopedListeners.getListeners();
    for (int i = 0; i < listeners.length; ++i) {
      final IStopedListener l = ( IStopedListener ) listeners[ i ];
      Runnable runnable = new Runnable() {
        public void run() {
          l.stopedEvent( event );
        }
      };
      Display.getDefault().syncExec( runnable );
    }
  }
  
  // helping methods
  //////////////////
  
  private void move( final Object obj, final int offset ) {
    int objIndex = 0;
    for( int i = 0; i < elements.length; i++ ) {
      if( obj == elements[ i ] ) {
        objIndex = i;
      }
    }
    int newIndex = objIndex + offset;
    if( newIndex >= 0 && newIndex < elements.length ) {
      elements[ objIndex ] = elements[ newIndex ];
      elements[ newIndex ] = obj;
    }
  }
  
  private Object getRelativeObject( final Object obj, final int offset ) {
    Object result = null;
    int objIndex = PREVIOUS_ELEMENT_OFFSET;
    for( int i = 0; i < elements.length; i++ ) {
      if( obj == elements[ i ] ) {
        objIndex = i;
      }
    }
    int maxIndex = elements.length -1;
    if( !( objIndex + offset < 0 ) || !( objIndex + offset > maxIndex ) ) {
      result = elements[ objIndex + offset ];
    }
    return result;
  }
}