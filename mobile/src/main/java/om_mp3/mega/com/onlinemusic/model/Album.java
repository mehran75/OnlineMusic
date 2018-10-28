package om_mp3.mega.com.onlinemusic.model;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.orm.SugarRecord;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Album extends SugarRecord implements Parcelable{

    /*
    * "album": {
     "id": 16726,
     "owner_id": -2000016726,
     "title": "Love Love Love",
     "access_key": "5d3734cf6529623703",
     "thumb": {
     "photo_34": "https:\/\/sun1-12.userapi.com\/c846219\/v846219680\/62adf\/hmgEQ7DC4L8.jpg",
     "photo_68": "https:\/\/sun1-8.userapi.com\/c846219\/v846219680\/62add\/ShNDtHR8CXw.jpg",
     "photo_135": "https:\/\/sun1-19.userapi.com\/c846219\/v846219680\/62adb\/MNtii35GRoA.jpg",
     "photo_270": "https:\/\/sun1-1.userapi.com\/c846219\/v846219680\/62ad8\/BUjy3THk6i4.jpg",
     "photo_300": "https:\/\/sun1-10.userapi.com\/c846219\/v846219680\/62ad6\/PSIhrBIR6O8.jpg",
     "photo_600": "https:\/\/sun1-8.userapi.com\/c846219\/v846219680\/62ad3\/TtqWEAlCPGQ.jpg",
     "width": 300,
     "height": 300
        }
     }
     */

    private long id;
    private long owner_id;
    private String title;
    private String access_key;
    private ArrayList<AlbumThumbnail> albumThumbnails = new ArrayList<>();

//    private List<Song> songList = new LinkedList<>();


    public Album() {
    }


    protected Album(Parcel in) {
        id = in.readLong();
        owner_id = in.readLong();
        title = in.readString();
        access_key = in.readString();
        albumThumbnails = in.createTypedArrayList(AlbumThumbnail.CREATOR);
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public Uri getArtUriFromMusicFile(Context context, File file) {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {MediaStore.Audio.Media.ALBUM_ID};

        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1 AND " + MediaStore.Audio.Media.DATA + " = '"
                + file.getAbsolutePath() + "'";
        final Cursor cursor = context.getContentResolver().query(uri, cursor_cols, where, null, null);
        Log.d(TAG, "Cursor count:" + cursor.getCount());
        /*
         * If the cusor count is greater than 0 then parse the data and get the art id.
         */
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
            cursor.close();
            return albumArtUri;
        }
        return Uri.EMPTY;
    }

    public Album(Context context, File file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getPath());

        setTitle(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        addAlbumThumbnail(new AlbumThumbnail(getArtUriFromMusicFile(context, file)));
    }

    public Album(JSONObject json) throws Exception {

//        JSONObject json = j.getJSONObject("album");

        setId(json.getLong("id"));
        setOwner_id(json.getLong("owner_id"));
        setTitle(json.getString("title"));
        if (json.has("access_key"))
            setAccess_key(json.getString("access_key"));

        if (json.has("thumb")) {
            JSONObject thumb = json.getJSONObject("thumb");
            Iterator<String> iterator = thumb.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.contains("photo_"))
                    addAlbumThumbnail(new AlbumThumbnail(thumb.getString(key)
                            , Integer.parseInt(key.replace("photo_", ""))));
            }
        }


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public List<AlbumThumbnail> getAlbumThumbnails() {
        return albumThumbnails;
    }

    public void setAlbumThumbnails(@NonNull ArrayList<AlbumThumbnail> albumThumbnails) {
        this.albumThumbnails = albumThumbnails;
    }


    public void addAlbumThumbnail(AlbumThumbnail thumbnail) {
        this.albumThumbnails.add(thumbnail);
    }


//    public List<Song> getSongList() {
//        return songList;
//    }
//
//    public void setSongList(@NonNull List<Song> songList) {
//        this.songList = songList;
//    }
//
//    public void addSong(Song song){
//        this.songList.add(song);
//    }


    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", title='" + title + '\'' +
                ", access_key='" + access_key + '\'' +
                ", albumThumbnails=" + Arrays.toString(albumThumbnails.toArray()) +
//                ", songList=" + songList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(owner_id);
        parcel.writeString(title);
        parcel.writeString(access_key);
        parcel.writeTypedList(albumThumbnails);
    }
}
