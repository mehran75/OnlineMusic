package om_mp3.mega.com.onlinemusic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.fragment.AlbumFragment;
import om_mp3.mega.com.onlinemusic.model.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private AlbumFragment fragment;
    private ArrayList<Album> albumArrayList = new ArrayList<>();

    public AlbumAdapter(AlbumFragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(fragment.getContext())
                .inflate(R.layout.album_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {

        holder.initialize(albumArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }


    public ArrayList<Album> getAlbumArrayList() {
        return albumArrayList;
    }

    public void setAlbumArrayList(ArrayList<Album> albumArrayList) {
        this.albumArrayList.clear();
        this.albumArrayList.addAll(albumArrayList);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail_iv;
        TextView title_tv;
        TextView artist_tv;
        TextView song_number_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail_iv = (ImageView) findViewById(R.id.thumbnail_iv);
            title_tv = (TextView) findViewById(R.id.title_tv);
            artist_tv = (TextView) findViewById(R.id.artist_tv);
            song_number_tv = (TextView) findViewById(R.id.song_number_tv);


            itemView.setOnClickListener(view -> {
                // TODO: 9/20/18
            });
        }

        private View findViewById(int id) {
            return itemView.findViewById(id);
        }

        public void initialize(Album album) {
            Glide.with(fragment.getContext())
                    .load(album.getAlbumThumbnails().get(0).getUrl())
                    .into(thumbnail_iv);

            title_tv.setText(album.getTitle());
            artist_tv.setVisibility(View.GONE);
//            song_number_tv.setText(album.getSongs().size() + " Songs");
//            artist_tv.setText(album.get);
        }
    }
}
