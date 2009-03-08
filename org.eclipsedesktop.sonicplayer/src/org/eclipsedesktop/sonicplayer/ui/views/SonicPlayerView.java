// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer.ui.views;

import java.io.File;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.base.core.Util;
import org.eclipsedesktop.sonicplayer.SonicPlayerPlugin;
import org.eclipsedesktop.sonicplayer.core.ISonicPlayerConstants;
import org.eclipsedesktop.sonicplayer.core.listener.*;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;
import org.eclipsedesktop.sonicplayer.core.player.SonicPlayer;
import org.eclipsedesktop.sonicplayer.core.playlist.Playlist;
import org.eclipsedesktop.sonicplayer.ui.views.actions.*;
import org.eclipsedesktop.sonicplayer.ui.views.playlistview.PlaylistContentProvider;
import org.eclipsedesktop.sonicplayer.ui.views.playlistview.PlaylistLabelProvider;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class SonicPlayerView extends ViewPart implements ISonicPlayerConstants,
                                                         IPausedListener, 
                                                         IPlayingListener,
                                                         IStopedListener,
                                                         IFinishedListener {

  private TableViewer playlist;
  private Label timeInfoLbl;
  private Label bitrateChInfoLbl;
  private ProgressBar bar;
  private PlayAction playActionBtn;
  private StopAction stopActionBtn;
  private ForwardAction forwardActionBtn;
  private RewindAction rewindActionBtn;
  private PrevAction prevActionBtn;
  private NextAction nextActionBtn;
  private Action doubleClickAction;
  private ClearAction clearActionBtn;
  private AddAction addActionBtn;
  private RemoveAction removeActionBtn;
  private UpAction upActionBtn;
  private DownAction downActionBtn;

  public SonicPlayerView() {
  }

  public void createPartControl( final Composite parent ) {
    GridLayout gridLayout = Util.getGridLayout( 1, false, 1 );
    parent.setLayout( gridLayout );

    Composite statusComp = new Composite( parent, SWT.NONE );
    GridLayout gridLayout2 = Util.getGridLayout( 3, false, 1 );
    statusComp.setLayout( gridLayout2 );
    statusComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    Label timeLbl = new Label( statusComp, SWT.NONE );
    timeLbl.setText( "Time:" );

    timeInfoLbl = new Label( statusComp, SWT.NONE );
    timeInfoLbl.setText( " - " );
    timeInfoLbl.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    bitrateChInfoLbl = new Label( statusComp, SWT.NONE );
    bitrateChInfoLbl.setText( " - / - / - " );
    bitrateChInfoLbl.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

    bar = new ProgressBar( statusComp, SWT.BORDER );
    bar.setToolTipText( "click to seek track position" );
    bar.setMaximum( 100 );
    bar.setMinimum( 0 );
    bar.addMouseListener( new BarSliderAction() );
    GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
    gridData.horizontalSpan = 3;
    bar.setLayoutData( gridData );

    createPlaylist( parent );

    createPlaylistToolbar( parent );

    makeActions();
    hookDoubleClickAction();
    contributeToActionBars();
    startUpdater();
    Playlist.getInstance().addPausedListener( this );
    Playlist.getInstance().addStopedListener( this );
    Playlist.getInstance().addPlayingListener( this );
    Playlist.getInstance().addFinishedListener( this );
  }

  public void setFocus() {
    playlist.getControl().setFocus();
  }

  public void pausedEvent( final PausedEvent event ) {
    configActionBtn( playActionBtn, "Resume", ICON_RESUME );
    playActionBtn.setEnabled( true );
    configActionBtn( stopActionBtn, "Stop", ICON_STOP );
    stopActionBtn.setEnabled( true );
    configActionBtn( forwardActionBtn, "Forward", ICON_FORWARD );
    forwardActionBtn.setEnabled( true );
    configActionBtn( rewindActionBtn, "Rewind", ICON_REWIND );
    rewindActionBtn.setEnabled( true );
  }

  public void playingEvent( final PlayingEvent event ) {
    configActionBtn( playActionBtn, "Pause", ICON_PAUSE );
    playActionBtn.setEnabled( true );
    configActionBtn( stopActionBtn, "Stop", ICON_STOP );
    stopActionBtn.setEnabled( true );
    configActionBtn( forwardActionBtn, "Forward", ICON_FORWARD );
    forwardActionBtn.setEnabled( true );
    configActionBtn( rewindActionBtn, "Rewind", ICON_REWIND );
    rewindActionBtn.setEnabled( true );
    selectCurrent( event.getSource() );
  }

  public void stopedEvent( final StopedEvent event ) {
    configActionBtn( playActionBtn, "Play", ICON_PLAY );
    playActionBtn.setEnabled( true );
    configActionBtn( stopActionBtn, "Stop", ICON_STOP );
    stopActionBtn.setEnabled( false );
    configActionBtn( forwardActionBtn, "Forward", ICON_FORWARD );
    forwardActionBtn.setEnabled( false );
    configActionBtn( rewindActionBtn, "Rewind", ICON_REWIND );
    rewindActionBtn.setEnabled( false );
  }

  public void finishedEvent( final FinishedEvent event ) {
    Object obj = Playlist.getInstance().getNext( event.getSource() );
    SonicPlayerPlugin plugin = SonicPlayerPlugin.getDefault();
    ISonicPlayer player = plugin.getActivePlayer();
    if( player != null ) {
      player.stop();
    }
    if( obj instanceof File ) {
      player = new SonicPlayer( (( File )obj) ).getPlayer();
      plugin.setActivePlayer( player );
      if( player != null ) {
        player.play();
      }
    }
  }

 // helping methods
  //////////////////
  private void createPlaylist( final Composite parent ) {
    playlist = new TableViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    playlist.setContentProvider( new PlaylistContentProvider() );
    playlist.setLabelProvider( new PlaylistLabelProvider() );
    playlist.setInput( new Object() );
    playlist.getControl().setLayoutData( new GridData( GridData.FILL_BOTH ) );
  }

  private void createPlaylistToolbar( final Composite parent ) {
    ToolBar tb = new ToolBar( parent, SWT.FLAT | SWT.HORIZONTAL );
    IToolBarManager tbm = new ToolBarManager( tb );
    clearActionBtn = new ClearAction( playlist );
    configActionBtn( clearActionBtn, "clear", ICON_CLEAR );
    addActionBtn = new AddAction( playlist );
    configActionBtn( addActionBtn, "add", ICON_ADD );
    removeActionBtn = new RemoveAction( playlist );
    configActionBtn( removeActionBtn, "remove", ICON_REMOVE );
    upActionBtn = new UpAction( playlist );
    configActionBtn( upActionBtn, "up", ICON_UP );
    downActionBtn = new DownAction( playlist );
    configActionBtn( downActionBtn, "down", ICON_DOWN );
    tbm.add( clearActionBtn );
    tbm.add( addActionBtn );
    tbm.add( removeActionBtn );
    tbm.add( upActionBtn );
    tbm.add( downActionBtn );
    tbm.update( true );
  }

  private void startUpdater() {
    Job job = new Job( "Sonic Player UI" ) {
      
      protected IStatus run( final IProgressMonitor monitor ) {
        if( bar != null && !bar.isDisposed() ) {
          Runnable runnable = new Runnable() {
            public void run() {
              updateUI();
            }
          };
          Display.getDefault().syncExec( runnable );
          schedule( 500 );
        }
        return Status.OK_STATUS;                                                                                          
      }
    };
    job.setPriority( Job.SHORT );
    job.setSystem( true );
    job.schedule();
  }

  private void updateUI() {
    ISonicPlayer player = SonicPlayerPlugin.getDefault().getActivePlayer();
    if( !bar.isDisposed() ) {
      if( player != null ) {
        bar.setSelection( player.getProgress() );
        this.timeInfoLbl.setText( player.getTime() );
        String currentBitRate = player.getCurrentKBitRate();
        String channels = player.getChannels();
        String sampleRate = player.getSampleRate();
        this.bitrateChInfoLbl.setText(   channels 
                                       + "Ch, " 
                                       + sampleRate 
                                       + "kHz, " 
                                       + currentBitRate
                                       + "kbps" );
      } else {
        bar.setSelection( 0 );
        this.timeInfoLbl.setText( "-" );
        this.bitrateChInfoLbl.setText( " - / - / - " );
      }
    }
  }
  
  private void contributeToActionBars() {
    IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
    rewindActionBtn = new RewindAction();
    configActionBtn( rewindActionBtn, "Rewind", ICON_REWIND );
    rewindActionBtn.setEnabled( false );
    forwardActionBtn = new ForwardAction();
    configActionBtn( forwardActionBtn, "Forward", ICON_FORWARD );
    forwardActionBtn.setEnabled( false );
    playActionBtn = new PlayAction( playlist );
    configActionBtn( playActionBtn, "Play", ICON_PLAY );
    stopActionBtn = new StopAction();
    configActionBtn( stopActionBtn, "Stop", ICON_STOP );
    stopActionBtn.setEnabled( false );
    prevActionBtn = new PrevAction();
    configActionBtn( prevActionBtn, "Previous", ICON_PREV );
    nextActionBtn = new NextAction();
    configActionBtn( nextActionBtn, "Next", ICON_NEXT );
    tbm.add( rewindActionBtn );
    tbm.add( forwardActionBtn );
    tbm.add( playActionBtn );
    tbm.add( stopActionBtn );
    tbm.add( prevActionBtn );
    tbm.add( nextActionBtn );
  }

  private void makeActions() {
    doubleClickAction = new Action() {

      public void run() {
        ISelection selection = playlist.getSelection();
        Object obj = ( ( IStructuredSelection )selection ).getFirstElement();
        if( obj instanceof File ) {
          SonicPlayerPlugin plugin = SonicPlayerPlugin.getDefault();
          ISonicPlayer player = plugin.getActivePlayer();
          if( player != null ) {
            player.stop();
          }
          player = new SonicPlayer( (( File )obj) ).getPlayer();
          plugin.setActivePlayer( player );
          if( player != null ) {
            player.play();
          }
        }
      }
    };
  }

  private void hookDoubleClickAction() {
    playlist.addDoubleClickListener( new IDoubleClickListener() {

      public void doubleClick( final DoubleClickEvent event ) {
        doubleClickAction.run();
      }
    } );
  }

  private void configActionBtn( final Action action,
                                    final String text,
                                    final String image ) {
    SonicPlayerPlugin plugin = SonicPlayerPlugin.getDefault();
    String pluginId = plugin.getBundle().getSymbolicName();
    action.setText( text );
    action.setToolTipText( text );
    action.setImageDescriptor( AbstractUIPlugin
                               .imageDescriptorFromPlugin( pluginId, image ) );
  }

  private void selectCurrent( final Object obj ) {
    if( obj != null ) {
      playlist.reveal( obj );
      playlist.getTable().deselectAll();
      Playlist pl = Playlist.getInstance();
      int elementIndex = pl.getElementIndex( obj );
      playlist.getTable().select( elementIndex );
    }
  }
}