package om_mp3.mega.com.onlinemusic.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class AlbumThumbnail extends SugarRecord implements Parcelable{

    /*
    * "thumb": {
     "photo_34": "https:\/\/sun1-12.userapi.com\/c846219\/v846219680\/62adf\/hmgEQ7DC4L8.jpg",
     "photo_68": "https:\/\/sun1-8.userapi.com\/c846219\/v846219680\/62add\/ShNDtHR8CXw.jpg",
     "photo_135": "https:\/\/sun1-19.userapi.com\/c846219\/v846219680\/62adb\/MNtii35GRoA.jpg",
     "photo_270": "https:\/\/sun1-1.userapi.com\/c846219\/v846219680\/62ad8\/BUjy3THk6i4.jpg",
     "photo_300": "https:\/\/sun1-10.userapi.com\/c846219\/v846219680\/62ad6\/PSIhrBIR6O8.jpg",
     "photo_600": "https:\/\/sun1-8.userapi.com\/c846219\/v846219680\/62ad3\/TtqWEAlCPGQ.jpg",
     "width": 300,
     "height": 300
     */


    private String url;

    private int size;

    private Uri uri;

    public AlbumThumbnail(String url, int size) {

//        System.out.println("thumbnail ->> " + url);
        this.url = url;
        this.size = size;
    }


    public AlbumThumbnail() {
    }

    public AlbumThumbnail(Uri uri) {
        this.uri = uri;
    }

    protected AlbumThumbnail(Parcel in) {
        url = in.readString();
        size = in.readInt();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<AlbumThumbnail> CREATOR = new Creator<AlbumThumbnail>() {
        @Override
        public AlbumThumbnail createFromParcel(Parcel in) {
            return new AlbumThumbnail(in);
        }

        @Override
        public AlbumThumbnail[] newArray(int size) {
            return new AlbumThumbnail[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeInt(size);
        parcel.writeParcelable(uri, i);
    }
}
