package om_mp3.mega.com.onlinemusic.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import om_mp3.mega.com.onlinemusic.R;
import om_mp3.mega.com.onlinemusic.fragment.FragmentController;
import om_mp3.mega.com.onlinemusic.model.Song;

public class SimpleSongAdapter extends RecyclerView.Adapter<SimpleSongAdapter.ViewHolder> {


    private final FragmentController activity;

    private ArrayList<Song> songList = new ArrayList<>();
    private Song playingSong;
    private boolean isShuffleMode;


    public SimpleSongAdapter(FragmentController context) {
        this.activity = context;

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout_id = R.layout.song_item_layout;

        if (viewType == 0)
            layout_id = R.layout.song_header_layout;

        return new ViewHolder(LayoutInflater.from(activity.getActivity()).inflate(layout_id, parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Song song = songList.get(position);
        holder.title_tv.setText(song.getTitle());
        holder.artist_tv.setText(song.getArtist());

        song.getIsPlaying().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                holder.equalizer_iv.setVisibility(song.isPlaying() ? View.VISIBLE : View.GONE);
                playingSong = song;
            }
        });

        Glide.with(activity)
                .load(R.drawable.equalizer)
                .apply(new RequestOptions().centerCrop())
                .into(holder.equalizer_iv);

        holder.download_btn.setVisibility(song.isDownloaded()
                ? View.GONE : View.VISIBLE);

        /* ****************************Download Song**************************************/
        holder.download_btn.setOnClickListener(view -> {

            if (!activity.checkIfPermissionGranted()) {
                activity.requestPermission();
            } else{
                DownloadManager manager = (DownloadManager) activity.getContext().getSystemService(Context.DOWNLOAD_SERVICE);

//                manager.down
            }
//                new DownloadSong(activity).execute(song);
        });

        if (song.getAlbum() != null) {
            try {
                if (!song.getAlbum()
                        .getAlbumThumbnails().isEmpty()) {
                    String url = song.getAlbum()
                            .getAlbumThumbnails().get(song.getAlbum()
                                    .getAlbumThumbnails().size() - 1).getUrl();


                    Glide.with(activity)
                            .load(url)
                            .into(holder.thumbnail_iv);
                }
//                RequestOptions options = new RequestOptions()
//                        .centerCrop()
//                        .placeholder(R.drawable.ic_music_note)
//                        .error(R.mipmap.ic_launcher_round);


//                Glide.with(activity).load(song.getAlbum()
//                        .getAlbumThumbnails().get(0).getUrl()).apply(options).into(holder.thumbnail_iv);

//                holder.thumbnail_iv.setImageBitmap(BitmapFactory.decodeStream(new URL(song.getAlbum()
//                        .getAlbumThumbnails().get(0).getUrl())
//                .openConnection().getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setShuffleMode(boolean shuffleMode) {
        isShuffleMode = shuffleMode;

        if (isShuffleMode)
            playNext();
    }

    public boolean isShuffleMode() {
        return isShuffleMode;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setList(@NonNull List<Song> songs) {
        this.songList.clear();
        this.songList.addAll(songs);
        notifyDataSetChanged();
    }

    public void playNext() {

        if (playingSong != null)
            playingSong.setPlaying(false);

        if (isShuffleMode)
            activity.loadSong(songList.get(Math.abs(new Random().nextInt(songList.size() - 1))));
        else {
            if (playingSong != null) {
                int index = songList.indexOf(playingSong);

                if (index == songList.size())
                    index = 0;

                activity.loadSong(songList.get(index));
            }
        }

    }

    public ArrayList<Song> getList() {
        return songList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_tv;
        TextView artist_tv;
        ImageView thumbnail_iv;
        ImageButton download_btn;
        ImageView equalizer_iv;

        public ViewHolder(View itemView) {
            super(itemView);

            title_tv = itemView.findViewById(R.id.title_tv);
            artist_tv = itemView.findViewById(R.id.artist_tv);
            thumbnail_iv = itemView.findViewById(R.id.thumbnail_iv);

            download_btn = itemView.findViewById(R.id.download_btn);
            equalizer_iv = itemView.findViewById(R.id.equalizer_iv);


            itemView.setOnClickListener(view -> {

//                MediaController controller = new MediaController(activity);
//                controller.setMediaPlayer(player);

//                if (playingSong != null) {
//                    playingSong.setPlaying(false);
//                }

                activity.loadSong(getAdapterPosition());


            });

        }
    }


}
