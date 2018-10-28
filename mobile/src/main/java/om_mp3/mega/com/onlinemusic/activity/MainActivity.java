package om_mp3.mega.com.onlinemusic.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;


import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.LinkedList;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.adapter.ViewPagerAdapter;
import om_mp3.mega.com.onlinemusic.fragment.AllSongsFragment;
import om_mp3.mega.com.onlinemusic.fragment.DownloadedFragment;
import om_mp3.mega.com.onlinemusic.fragment.FragmentController;
import om_mp3.mega.com.onlinemusic.model.Album;
import om_mp3.mega.com.onlinemusic.model.Song;
import om_mp3.mega.com.onlinemusic.server_interface.PreparePage;

public class MainActivity extends Controller {

    public static final String SONG_LIST = "songList";
    public static final String OFFLINE_USE = "offlineUse";


    private ViewPagerAdapter viewPagerAdapter;


    private ArrayList<Song> songArrayList = new ArrayList<>();
    private ArrayList<Album> albumArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setUpNavigationDrawer();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        setUpFragments();

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPager viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(viewPagerAdapter);


        if (getIntent().getBooleanExtra(OFFLINE_USE, false)) {

            for (FragmentController controller : viewPagerAdapter.getList()) {
                controller.setOfflineUse(true);
            }

        } else {
            songArrayList = getIntent().getParcelableArrayListExtra(SONG_LIST);

            for (FragmentController controller : viewPagerAdapter.getList()) {
                controller.setSongs(songArrayList);
            }

            setSongList(songArrayList);

        }


//        SearchView searchView = findViewById(R.id.searchView);

        MaterialSearchBar searchView = findViewById(R.id.searchView);
//        searchView.setLastSuggestions(new LinkedList<String>());
//        searchView.clearFocus();
//        searchView.setVoiceEnabled(true);

        searchView.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                new PreparePage(MainActivity.this).execute(String.valueOf(text));

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        searchView.setSpeechMode(true);

//        albumArrayList = prepareAlbumList(songArrayList);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.action_bar, menu);

//        MenuItem item = menu.findItem(R.id.action_search);
//        ((MaterialSearchView) findViewById(R.id.searchView)).setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    private void setUpNavigationDrawer() {
//        DrawerLayout drawerLayout = findViewById(R.id.main_layout);
//
//        NavigationView navigationView = findViewById(R.id.navigationView);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
//                , drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
//
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView.setNavigationItemSelectedListener(item -> {
//
//            switch (item.getItemId()) {
//                case R.id.setting_item:
//                    break;
//
//                case R.id.about_me:
//                    break;
//            }
//            return false;
//        });

    }

//    private ArrayList<Album> prepareAlbumList(ArrayList<Song> songArrayList) {
//        ArrayList<Album> list = new ArrayList<>();
//        ArrayList<Album> final_list = new ArrayList<>();
//
//        for (Song song : songArrayList) {
//            if (song.getAlbum() != null) {
//                if (list.contains(song.getAlbum()))
//                    list.get(list.indexOf(song.getAlbum())).addSong(song)
//                list.add(song.getAlbum());
//            }
//        }
//
//        Album album;
//        while (!s.isEmpty()) {
//
//            Album tmp = list.get(0);
//
//        }
//
//
//        return list;
//    }

    private void setUpFragments() {
        AllSongsFragment allSongsFragment = new AllSongsFragment();
//        allSongsFragment.setSongs(songArrayList);
        viewPagerAdapter.getList().add(allSongsFragment);

        DownloadedFragment downloadedFragment = new DownloadedFragment();
//        .setSongs(songArrayList);
        viewPagerAdapter.getList().add(downloadedFragment);


    }

    public void loadSong(Song song) {
        // TODO: 9/20/18
    }

    public void start_playing_shuffle(View view) {
        // TODO: 9/20/18
        viewPagerAdapter.getList().get(0).getAdapter().setShuffleMode(true);
//        addPlayerContainer();
    }

    @Override
    public void setSongList(ArrayList<Song> songList) {
        super.setSongList(songList);
        viewPagerAdapter.getList().get(0)
                .setSongs(songList);
    }
}
