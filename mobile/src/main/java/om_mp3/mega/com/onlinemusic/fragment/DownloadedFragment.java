package om_mp3.mega.com.onlinemusic.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.activity.Controller;
import om_mp3.mega.com.onlinemusic.adapter.SimpleSongAdapter;
import om_mp3.mega.com.onlinemusic.constants.Constant;
import om_mp3.mega.com.onlinemusic.model.Song;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadedFragment extends FragmentController {


    private RecyclerView recyclerView;
    private View view;
    private SimpleSongAdapter adapter;

    public DownloadedFragment() {
        // Required empty public constructor

        setTitle();

        adapter = new SimpleSongAdapter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_downloaded, container, false);

        File songFolder = new File(Constant.SongFolder);

        if (!checkIfPermissionGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission();
            }
        }

        if (!songFolder.exists()) {
            songFolder.mkdir();
        }


//        File files[] = songFolder.listFiles();
//
//        ArrayList<Song> songList = new ArrayList<>();
//
//        for (File song : files) {
//            songList.add(new Song(getContext(), song));
//        }

        view = inflater.inflate(R.layout.fragment_all_songs, container, false);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//        adapter.setList;

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL, false));

        adapter.setList(Song.listAll(Song.class));
        adapter.notifyDataSetChanged();


        return view;
    }

    private View findViewById(int id) {
        return view.findViewById(id);
    }

    @Override
    public SimpleSongAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void loadSong(int index) {
        ((Controller)getActivity()).playSong(true,index);
    }

    @Override
    public void loadSong(Song song) {

    }

    @Override
    public void setSongs(ArrayList<Song> songs) {
//        ((Controller)getActivity()).setSongList(songs);
    }

    @Override
    public void setTitle() {
        title = "Downloaded";
    }

    @Override
    public void setOfflineUse(boolean offlineUse) {

    }
}
