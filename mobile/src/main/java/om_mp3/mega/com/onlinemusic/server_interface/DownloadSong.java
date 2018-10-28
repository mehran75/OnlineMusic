package om_mp3.mega.com.onlinemusic.server_interface;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.constants.Constant;
import om_mp3.mega.com.onlinemusic.fragment.FragmentController;
import om_mp3.mega.com.onlinemusic.model.Song;

public class DownloadSong extends AsyncTask<Song, Integer, Song> {


    private String save_path;
    private FragmentController activity;

    private NotificationManager manager;

    private boolean isSilentModeEnabled = false;

    public DownloadSong(String absolutePath) {
        isSilentModeEnabled = true;
        this.save_path = absolutePath;
    }

    public DownloadSong(FragmentController activity) {
        this.activity = activity;
        manager = (NotificationManager) activity.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Song doInBackground(Song... songs) {

        try {

            if (save_path == null)
                save_path = isSilentModeEnabled
                        ? Constant.CACHE_PATH
                        : Constant.SongFolder;

            File folder = new File(save_path);
            if (!folder.exists())
                folder.mkdir();

            FileOutputStream fos = new FileOutputStream(save_path
                    + File.separator
                    + songs[0].getArtist() + "-" + songs[0].getTitle() + ".mp3");

            URL url = new URL(songs[0].getDownload_url());

            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();
            System.out.println("lenghtOfFile = " + lenghtOfFile);
            // download the file
            InputStream input = new DataInputStream(url.openStream());

            // Output stream
//            OutputStream output = new FileOutputStream(Environment
//                    .getExternalStorageDirectory().toString()
//                    + "/2011.kml");

            byte data[] = new byte[1024 * 1024];

            long total = 0;

            int count;


            while ((count = input.read(data)) != -1) {
                System.out.println("count = " + count);
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress((int) ((total * 100) / lenghtOfFile));

                // writing data to file
                fos.write(data, 0, count);
            }

            // flushing output
            fos.flush();

            // closing streams
            fos.close();
            input.close();

            return songs[0];

//            DataInputStream dis = new DataInputStream(.openConnection()
//                    .getInputStream());
//
//
//
//            int fileSize = dis.available();
//            if (fileSize == 0)
//                return null;
//
//            int chunkSize = 1024 * 1024 * 2;
//            int chunk = fileSize / chunkSize;
//            int remains = fileSize - (chunk * chunkSize);
//            double percentage = (double) 100 / chunk;
//
//
//            if (chunk > 0) {
//                for (int i = 0; i < chunk; i++) {
////                    if (cancelProperty.get()) {
////                        dis.close();
////                        return;
////                    }
//                    byte[] buffer = new byte[chunkSize];
//                    dis.readFully(buffer);
//                    fos.write(buffer);
//                    buffer = null;
//                    onProgressUpdate((int) Math.min(100, i * percentage));
//                }
//                if (remains != 0) {
//                    byte[] buffer = new byte[remains];
//                    dis.readFully(buffer);
//                    fos.write(buffer);
//                    buffer = null;
//                    onProgressUpdate(98);
//                }
//            } else {
////                if (cancelProperty.get()) {
////                    dis.close();
////                    return;
////                }
//                byte buffer[] = new byte[fileSize];
//                dis.readFully(buffer);
//                fos.write(buffer);
//                buffer = null;
//                onProgressUpdate(100);
//            }
//
//            fos.close();
//            fos = null;
//
////
//            byte[] bytes = new byte[dis.available()];
//
//            int chunk = bytes.length / chunkSize;
//
//            int remain = bytes.length - (chunk * chunkSize);
//
//            float percentage = 1;
//            try {
//                percentage = 100 / chunk;
//            } catch (ArithmeticException ignored) {
//            }
//
//            for (int i = 0; i < chunk; i++) {
////                is.read(bytes, i * chunk, chunkSize);
//                onProgressUpdate((int) (i * percentage));
//            }
//
//            if (remain != 0)
//                is.read(bytes);
//
//            onProgressUpdate(100);
//
//
//            fos.write(bytes);
//
//            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        if (isSilentModeEnabled)
            return;

        manager.notify(0, new Notification.Builder(activity.getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("OM mP3")
                .setProgress(100, values[0], false)
                .setContentText("Downloading..")
                .build());
    }

    @Override
    protected void onPostExecute(Song song) {

        if (isSilentModeEnabled)
            if (song != null) {
                song.setFilePath(Constant.SongFolder + File.separator
                        + song.getArtist() + "-" + song.getTitle() + ".mp3");
                song.setDownloaded(true);
                return;
            }

        if (song != null)
            song.save();

        Toast.makeText(activity.getContext(), "Downloded", Toast.LENGTH_SHORT).show();
        manager.cancel(0);
    }
}
