package om_mp3.mega.com.onlinemusic.activity;

import android.databinding.Observable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SyncParams;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.adapter.SongPagerAdapter;
import om_mp3.mega.com.onlinemusic.model.AlbumThumbnail;
import om_mp3.mega.com.onlinemusic.model.Song;
import om_mp3.mega.com.onlinemusic.server_interface.DownloadSong;
import om_mp3.mega.com.onlinemusic.tools.CarouselEffectTransformer;
import rm.com.audiowave.AudioWaveView;

import static om_mp3.mega.com.onlinemusic.adapter.SongPagerAdapter.ADAPTER_TYPE_BOTTOM;
import static om_mp3.mega.com.onlinemusic.adapter.SongPagerAdapter.ADAPTER_TYPE_TOP;

public class PlayerActivity extends Controller {

    public static final String SONG_LIST = "SongList";
    public static final String PLAYING_SONG = "playing";
    private MediaPlayer player;
    private SeekBar seek_bar;
    private TextView time_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initializeViews();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

//        getActionBar().setTitle();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getParcelableExtra(PLAYING_SONG) != null) {

            Song song = getIntent().getParcelableExtra(PLAYING_SONG);

            if (player != null)
                player.stop();

            player = new MediaPlayer();

            try {
                player.setDataSource(song.getStream_url());


                player.prepare();


                player.setOnPreparedListener(mediaPlayer -> {
                    player.start();
                    song.setPlaying(true);

//                    new DownloadSong(getCacheDir().getAbsolutePath())
//                            .execute(song);
//
//                    song.getDownloaded().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//                        @Override
//                        public void onPropertyChanged(Observable sender, int propertyId) {
//                            if (song.isDownloaded())
//                                configWaveBar(song);
//                        }
//                    });

                });

                Timer timer = new Timer();
//                System.out.println("player.getDuration() = " + player.getDuration());
                int max = player.getDuration() / 1000;
                seek_bar.setMax(max);

                int minutes = max / 60;
                int seconds = max - (minutes * 60);
                int hours = max / (3600);


                ((ImageView) findViewById(R.id.play_pause_btn)).setImageResource(
                        player.isPlaying() ? R.drawable.ic_pause_circle_outline_white
                        : R.drawable.ic_play_circle_outline_white);

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        if (player == null) {
                            timer.cancel();
                            return;
                        }

                        int current_position = player.getCurrentPosition() / 1000;

                        int current_minutes = current_position / 60;
                        int current_seconds = current_position - (current_minutes * 60);
                        int current_hours = current_position / (3600);


                        String time = (hours == 0 ? "" : addZero(current_hours) + ":") +
                                addZero(current_minutes) + ":" + addZero(current_seconds) + " / "
                                + (hours == 0 ? "" : addZero(hours) + ":")
                                + addZero(minutes) + ":" + addZero(seconds);

                        runOnUiThread(() -> {
                            seek_bar.setProgress(current_position);
                            time_tv.setText(time);
                        });

                    }
                }, 0, 1000);
                player.setOnCompletionListener(mediaPlayer -> {
                    play_next(null);
                });
//            wave_bar.setRawData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private String addZero(int value) {

        return value < 10 ? "0" + value : String.valueOf(value);
    }

    private void initializeViews() {
        seek_bar = findViewById(R.id.seek_bar);

//        seek_bar.setOnTouchListener((view, motionEvent) -> {
//            if (motionEvent.getAction() == MotionEvent.ACTION_BUTTON_RELEASE)
//                view.performClick();
//            else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE)
//                if (player != null)
//                    player.seekTo(seek_bar.getProgress());
//            return false;
//        });
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (player != null && b)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        player.seekTo(i, MediaPlayer.SEEK_CLOSEST);
                    } else
                        player.seekTo(seek_bar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        time_tv = findViewById(R.id.time_tv);


        /* ********************************************************/
        ViewPager viewpagerTop = findViewById(R.id.viewpagerTop);
        final ViewPager viewPagerBackground = findViewById(R.id.viewPagerbackground);

        viewpagerTop.setClipChildren(false);
//        viewpagerTop.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewpagerTop.setOffscreenPageLimit(3);
        viewpagerTop.setPageTransformer(false, new CarouselEffectTransformer(this));

//        set Top Viewpager adapter
        SongPagerAdapter adapter = new SongPagerAdapter(this, getSongList(), ADAPTER_TYPE_TOP);
        viewpagerTop.setAdapter(adapter);

        // Set Background ViewPager Adapter
        SongPagerAdapter adapterBackground = new SongPagerAdapter(this, getSongList(), ADAPTER_TYPE_BOTTOM);
        viewPagerBackground.setAdapter(adapterBackground);


        viewpagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int index = 0;

            @Override
            public void onPageSelected(int position) {
                index = position;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int width = viewPagerBackground.getWidth();
                viewPagerBackground.scrollTo((int) (width * position + width * positionOffset), 0);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    viewPagerBackground.setCurrentItem(index);
                }

                System.out.println(getSongList().get(viewPagerBackground.getCurrentItem()).toString());

            }
        });



    }

    private ArrayList<Song> getSongList() {
        return getIntent().getParcelableArrayListExtra(SONG_LIST);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;

            // TODO: 9/22/18 implement other features

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSongList(ArrayList<Song> songList) {

    }


    public void play_previous(View view) {
    }

    public void play_pause(View view) {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            } else
                player.start();

            ((ImageButton) view).setImageResource(player.isPlaying() ? R.drawable.ic_pause_circle_outline_white
                    : R.drawable.ic_play_circle_outline_white);
        }
    }

    public void play_next(View view) {

    }

    public void start_shuffle(View view) {
    }

    public void repeat(View view) {
        if (player != null)
            player.setLooping(!player.isLooping());

        ((ImageButton)view).setImageResource(player.isLooping()?R.drawable.ic_repeat_white:
                R.drawable.ic_repeat__disable_white);
    }
}
