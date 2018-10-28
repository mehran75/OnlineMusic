package om_mp3.mega.com.onlinemusic.fragment;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.activity.Controller;
import om_mp3.mega.com.onlinemusic.activity.PlayerActivity;
import om_mp3.mega.com.onlinemusic.adapter.SimpleSongAdapter;
import om_mp3.mega.com.onlinemusic.model.Song;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllSongsFragment extends FragmentController {

    private RecyclerView recyclerView;
    private View view;
    private SimpleSongAdapter adapter;
    private MediaPlayer player;

    public AllSongsFragment() {
        // Required empty public constructor
        adapter = new SimpleSongAdapter(this);

        setTitle();
    }


    @Override
    public void setSongs(ArrayList<Song> songs) {
//        ((Controller)getActivity()).setSongList(songs);
        adapter.setList(songs);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTitle() {
        title = "All Songs";
    }

    @Override
    public void setOfflineUse(boolean offlineUse) {
        // TODO: 9/20/18
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_songs, container, false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//        adapter.setList;

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL, false));

        adapter.notifyDataSetChanged();


        return view;
    }


    public View findViewById(int id) {
        return view.findViewById(id);
    }

    @Override
    public SimpleSongAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void loadSong(int index) {
        ((Controller) getActivity()).playSong(false, index);
    }

    public void loadSong(Song song) {

        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra(PlayerActivity.SONG_LIST, adapter.getList());
        intent.putExtra(PlayerActivity.PLAYING_SONG, song);

        startActivity(intent);

    }
}
