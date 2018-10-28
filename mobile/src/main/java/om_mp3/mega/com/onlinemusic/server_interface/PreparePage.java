package om_mp3.mega.com.onlinemusic.server_interface;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import om_mp3.mega.com.onlinemusic.activity.Controller;
import om_mp3.mega.com.onlinemusic.activity.MainActivity;
import om_mp3.mega.com.onlinemusic.activity.SearchActivity;
import om_mp3.mega.com.onlinemusic.model.Album;
import om_mp3.mega.com.onlinemusic.model.Song;

public class PreparePage extends AsyncTask<String, Void, ArrayList<Song>> {


    private Controller controller;

    public PreparePage(Controller activity) {
        this.controller = activity;
    }
//
//    public PreparePage(MainActivity activity) {
//        mainActivity = activity;
//    }

    @Override
    protected ArrayList<Song> doInBackground(@NonNull String... query) {

        try {

            Connection connection = Jsoup.connect("https://my-free-mp3s.com/api/search.php");

            connection.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");

            connection.data("q", query[0]);
            connection.data("page", "0");

            ArrayList<Song> songList = new ArrayList<>();
            List<Album> albumList = new LinkedList<>();

            Document document = connection.post();

//            System.out.println(document.body().text());

            String text = document.body().text();

            JSONArray response = new JSONObject(text.substring(1, text.length()))
                    .getJSONArray("response");

            for (int i = 1; i < response.length(); i++) {
                JSONObject json = response.getJSONObject(i);

                Song song = new Song(json);
//                song.setAlbum(new Album(json.getJSONObject("album")));

                songList.add(song);
//                albumList.add();

//                songList.get

            }


            return songList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(@NonNull ArrayList<Song> songs) {
//        for (Song song : songs) {
//            System.out.println(song.toString());
//        }

        controller.setSongList(songs);


    }


    /*
     function encode(input) {
     var encoded = "";
     if (input == 0)
     return map[0];
     if (input < 0) {
     input *= -1;
     encoded += "-"
     }
     ;while (input > 0) {
     val = parseInt(input % length);
     input = parseInt(input / length);
     encoded += map[val]
     }
     return encoded
     }
     */

}
