package om_mp3.mega.com.onlinemusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.model.AlbumThumbnail;
import om_mp3.mega.com.onlinemusic.model.Song;

public class SongPagerAdapter extends PagerAdapter {


    public static final int ADAPTER_TYPE_TOP = 1;
    public static final int ADAPTER_TYPE_BOTTOM = 2;


    private Context context;
    private ArrayList<Song> listItems = new ArrayList<>();
    private int adapterType;

    public SongPagerAdapter(Context context, ArrayList<Song> listItems, int adapterType) {
        this.context = context;
        this.listItems.addAll(listItems);
        this.adapterType = adapterType;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_cover, container, false);
        try {

            LinearLayout linMain = view.findViewById(R.id.linMain);
            ImageView imageCover = view.findViewById(R.id.imageCover);

//            imageCover.setOnLongClickListener(v -> {
//                // TODO: 9/4/18  MainActivity.sharedPreferences.edit().putInt("chat_bg",listItems[position]).apply();
//
////                    Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
////                    vibrator.vibrate(300);
////                    Toast.makeText(getApplicationContext(), R.string.chat_wallpaper_chosen, Toast.LENGTH_SHORT).show();
//                return false;
//            });
            linMain.setTag(position);

            switch (adapterType) {
                case ADAPTER_TYPE_TOP:
                    linMain.setBackgroundResource(R.drawable.shadow);
                    break;
                case ADAPTER_TYPE_BOTTOM:
                    linMain.setBackgroundResource(0);
                    break;
            }

            Song song = listItems.get(position);

            if (song.getAlbum() != null) {
                try {
                    if (!song.getAlbum()
                            .getAlbumThumbnails().isEmpty()) {
                        String url = song.getAlbum()
                                .getAlbumThumbnails().get(song.getAlbum()
                                        .getAlbumThumbnails().size() - 1).getUrl();


                        Glide.with(context)
                                .load(url)
                                .into(imageCover);
                    }else {
                        Glide.with(context)
                                .load(R.drawable.default_thumbnail)
                                .into(imageCover);
                    }
                } catch (Exception ignored) {
                }
            }

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,
                            @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);
    }

}
