package om_mp3.mega.com.onlinemusic.constants;

import android.os.Environment;

import java.io.File;

public class Constant {

    public static final String NOTIFICATION_GROUP = "om_mp3.mega.com.onlinemusic";
    public static final String CACHE_PATH = Environment.getDownloadCacheDirectory().getPath();
    public static String SongFolder = Environment.getExternalStorageDirectory().getPath()
             + File.separator + "om_mp3";



}
