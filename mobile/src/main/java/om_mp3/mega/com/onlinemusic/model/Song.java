package om_mp3.mega.com.onlinemusic.model;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import om_mp3.mega.com.onlinemusic.constants.Constant;

public class Song extends SugarRecord implements Parcelable {

    /**
     * "id": 456340406,
     * "owner_id": 371745432,
     * "artist": "Ellie Goulding",
     * "title": "Love Me Like You Do",
     * "duration": 250,
     * "date": 1494225694,
     * "url": "https:\/\/cs1-42v4.vkuseraudio.net\/p14\/565c33dc6a860e.mp3?extra=f4Sq9l1KzbWLk0DE-D66MQHo9gF-IY48oo1ExIiMd5-eZvF__58__lPNqOOIp4NyubuzsqP4DunU2EUeHfDRU4vDQiwK4jrxTo3W0d90spgF-NZadPBvcSEQpn3cOMJ0y4akzPWFyaSW",
     * "no_search": 1,
     */

    private long id;
    private long owner_id;
    private String artist;
    private String title;
    private long date;
    private int duration;
    private String url;
    private String stream_url = "https://newtabz.stream/stream/";
    private String download_url = "https://newtabz.stream/";

    private Album album;

    private ObservableBoolean downloaded = new ObservableBoolean(false);
    private String filePath;


    private ObservableBoolean isPlaying = new ObservableBoolean(false);

    private static final char[] map = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'z', '1', '2', '3'};


    public Song(long id, long owner_id) {
        this.id = id;
        this.owner_id = owner_id;
    }

    public Song() {
    }


    public Song(Context context, File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(file.getPath());

        setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        setArtist(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        setDate(Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)));
        setDuration(Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        setAlbum(new Album(context, file));


        List<Song> list = Song.listAll(Song.class);

        if (list.contains(this)) {
            filePath = list.get(list.indexOf(this)).filePath;
            setDownloaded(list.get(list.indexOf(this)).isDownloaded());
        }

    }

    public Song(JSONObject json) throws Exception {

        setId(json.getLong("id"));
        setOwner_id(json.getLong("owner_id"));
        setArtist(json.getString("artist"));
        setTitle(json.getString("title"));
        setDate(json.getLong("date"));
        setDuration(json.getInt("duration"));
        setUrl(json.getString("url"));

        if (json.has("album"))
            setAlbum(new Album(json.getJSONObject("album")));

        String key = encode(owner_id) + ":" + encode(id);
        stream_url += key;
        download_url += key;


    }


    protected Song(Parcel in) {
        id = in.readLong();
        owner_id = in.readLong();
        artist = in.readString();
        title = in.readString();
        date = in.readLong();
        duration = in.readInt();
        url = in.readString();
        stream_url = in.readString();
        download_url = in.readString();
        album = in.readParcelable(Album.class.getClassLoader());
        downloaded.set(in.readByte() != 0);
        filePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(owner_id);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeLong(date);
        dest.writeInt(duration);
        dest.writeString(url);
        dest.writeString(stream_url);
        dest.writeString(download_url);
        dest.writeParcelable(album, flags);
        dest.writeByte((byte) (downloaded.get() ? 1 : 0));
        dest.writeString(filePath);
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public static String encode(long input) {
        StringBuilder encoded = new StringBuilder();

        int length = map.length;

        if (input == 0)
            return String.valueOf(map[0]);
        if (input < 0) {
            input *= -1;
            encoded.append("-");
        }
        int val;
        while (input > 0) {
            val = (int) (input % length);
            input = (input / length);
            encoded.append(map[val]);
        }

        return encoded.toString();
    }


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public ObservableBoolean getIsPlaying() {
        return isPlaying;
    }


    public boolean isPlaying() {
        return isPlaying.get();
    }


    public void setPlaying(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                ", stream_url='" + stream_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public void setDownloaded(boolean downloaded) {
        this.downloaded.set(downloaded);
    }

    public ObservableBoolean getDownloaded() {
        return downloaded;
    }

    public boolean isDownloaded() {
        return downloaded.get();
    }

    /* **********************************************************
     *                           Database
     * ************************************************************/

    @Override
    public void save() {

        super.save();
    }
}
