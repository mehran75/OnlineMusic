package om_mp3.mega.com.onlinemusic.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import om_mp3.mega.com.onlinemusic.adapter.SimpleSongAdapter;
import om_mp3.mega.com.onlinemusic.model.Song;

public abstract class FragmentController extends Fragment {

    protected CharSequence title;

    public abstract void loadSong(Song song);

    public abstract void setSongs(ArrayList<Song> songs);

    public CharSequence getTitle() {
        return title;
    }


    public abstract void setTitle();

    public abstract void setOfflineUse(boolean offlineUse);

    public boolean checkIfPermissionGranted() {
        return
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        getActivity()
                                .checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        getActivity()
                                .checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public abstract SimpleSongAdapter getAdapter();

    public abstract void loadSong(int index);
}
