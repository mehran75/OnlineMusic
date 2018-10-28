package om_mp3.mega.com.onlinemusic.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.model.Song;
import om_mp3.mega.com.onlinemusic.service.OMPlayer;

public abstract class Controller extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private static MediaPlayer player;
    private static ArrayList<Song> songArrayList = new ArrayList<>();
    private static ArrayList<Song> shuffle_list = new ArrayList<>();
    private static int playing_index;
    private static boolean playingModeEnabled = false;

    private View player_container;
    private ImageButton play_pause_btn;
    private ImageButton next_btn;
    private ImageButton previous_btn;
    private ImageView thumbnail_iv;
    private TextView title_tv;
    private ProgressBar loading_progressbar;


    public Controller() {
        if (player == null)
            player = new MediaPlayer();

        player.setOnPreparedListener(this);




    }

    @Override
    public void setContentView(int layoutResID) {


//        LinearLayout screenRootView = new LinearLayout(this);
//        screenRootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
////        screenRootView.setOrientation(RelativeLayout.VERTICAL);
//
//        // Create your top view here

//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//
//        View screenView = inflater.inflate(layoutResID, null);
//
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        screenRootView.addView(screenView, params);
////        screenRootView.addView(player_container);


        super.setContentView(layoutResID);

        if (this instanceof MainActivity)
            initializePlayerContainer();
//

        if (!OMPlayer.isStarted()) {
            Intent intent = new Intent(this, OMPlayer.class);
            intent.putParcelableArrayListExtra(OMPlayer.SONG_LIST, songArrayList);
//        intent.putExtra(OMPlayer.OFFLINE_SONG_LIST,songArrayList);
            //        playerService.

            startService(intent);
        }

    }

    private void initializePlayerContainer() {

        try {
            if (player_container == null) {
                player_container = findViewById(R.id.player_container);

//
//            stop_btn = voice_player_container.findViewById(R.id.stop_btn);
//            stop_btn.setImageResource(R.drawable.ic_stop_black);
//            stop_btn.setOnClickListener(this::stopPlay);
//
                play_pause_btn = player_container.findViewById(R.id.play_pause_btn);
                play_pause_btn.setImageResource(R.drawable.ic_pause_circle_outline_white);
                play_pause_btn.setOnClickListener(this::play_pause);


                next_btn = player_container.findViewById(R.id.next_btn);
                next_btn.setImageResource(R.drawable.ic_skip_next_white);
                next_btn.setOnClickListener(this::play_next);

                previous_btn = player_container.findViewById(R.id.previous_btn);
                previous_btn.setImageResource(R.drawable.ic_skip_previous_white);
                previous_btn.setOnClickListener(this::play_previous);


                thumbnail_iv = player_container.findViewById(R.id.thumbnail_iv);

                title_tv = findViewById(R.id.title_tv);
                title_tv.setOnClickListener(this::showPlayerActivity);
//                title_tv.setAnimation(AnimationUtils.loadAnimation(this, R.anim.long_text_animation));


                loading_progressbar = findViewById(R.id.loading_progressbar);


                //            voice_title_tv = voice_player_container.findViewById(R.id.voice_title_tv);
//            seek_bar = voice_player_container.findViewById(R.id.seek_bar);
//
//
//            seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                    if (mediaPlayer != null) {
//                        mediaPlayer.seekTo(i);
//                        mediaPlayer.isPlaying();
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//
//            voice_player_container.setOnDragListener((view, dragEvent) -> {
//                view.setX(dragEvent.getX());
//                view.setY(dragEvent.getY());
//                return true;
//            });
//
//            music_fab.setOnClickListener(this::toggle_voice_layout);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void play_pause(View view) {


        if (player.isPlaying())
            player.pause();
        else
            player.start();

//        try {
//            Thread.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        if (view instanceof ImageButton)
        ((ImageButton) view).setImageResource(player.isPlaying() ? R.drawable.ic_pause_circle_outline_white :
                R.drawable.ic_play_circle_outline_white);
    }


    public void close_player(@Nullable View view) {
        player.stop();

        hidePlayerContainer();
    }

    public void start_shuffle(@Nullable View view) {
        // TODO: 9/26/18
    }

    public void loop(View view) {
        player.setLooping(player.isLooping());

        // TODO: 9/26/18 change image resource
    }

    public void play_next(View view) {
        if (playing_index != songArrayList.size() - 1) {
            playing_index++;

            playSong(false, playing_index);
        }
    }

    public void play_previous(View view) {
        if (playing_index != 0) {
            playing_index--;

            playSong(false, playing_index);
        }
    }


    public void hidePlayerContainer() {
        try {
            ViewGroup.LayoutParams params = player_container.getLayoutParams();

            params.height = 0;


            player_container.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlayerContainer() {
        try {

            loading_progressbar.setVisibility(View.VISIBLE);
            play_pause_btn.setVisibility(View.INVISIBLE);
            play_pause_btn.setEnabled(false);

            ViewGroup.LayoutParams params = player_container.getLayoutParams();

            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70
                    , getResources().getDisplayMetrics());


            player_container.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showPlayerActivity(View view) {

    }

    public void setSongList(ArrayList<Song> songList) {
        songArrayList.clear();
        songArrayList.addAll(songList);

        OMPlayer.setSongArrayList(songList);
    }

    public boolean isPlayingModeEnabled() {
        return true/*playingModeEnabled*/;
    }

    public void setPlayingModeEnabled(boolean playingModeEnabled) {
        Controller.playingModeEnabled = playingModeEnabled;
    }

    public void playSong(boolean offline_list, int index) {




        setSongContent(songArrayList.get(index));
        playing_index = index;

        showPlayerContainer();


        OMPlayer.setPrepareListener(this);
        OMPlayer.playSong(offline_list, index);

    }

    public void setSongContent(Song song) {
        if (player_container != null) {
            title_tv.setText(String.valueOf(song.getTitle() + "-" + song.getArtist()));

            if (!song.getAlbum()
                    .getAlbumThumbnails().isEmpty()) {
                String url = song.getAlbum()
                        .getAlbumThumbnails().get(song.getAlbum()
                                .getAlbumThumbnails().size() - 1).getUrl();


                Glide.with(this)
                        .load(url)
                        .apply(new RequestOptions().centerCrop())
                        .into(thumbnail_iv);
            }


//            new Thread(() -> {
//
////                if (player != null)
//                if (player.isPlaying())
//                    player.stop();
//
//                try {
//                    player.reset();
//                    player.setDataSource(song.getStream_url());
//                    player.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }).start();

        }

    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

//        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);

//        new OMPlayer()

        OMPlayer.startPlayer();

        play_pause_btn.setImageResource(R.drawable.ic_pause_circle_outline_white);

        loading_progressbar.setVisibility(View.GONE);
        play_pause_btn.setVisibility(View.VISIBLE);
        play_pause_btn.setEnabled(true);
    }
}
