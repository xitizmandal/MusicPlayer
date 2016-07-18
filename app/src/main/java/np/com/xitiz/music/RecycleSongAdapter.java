package np.com.xitiz.music;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by xitiz on 7/13/16.
 */
public class RecycleSongAdapter extends RecyclerView.Adapter<RecycleSongAdapter.MyViewHolder> {
    private ArrayList<Song> songList;
    private Context context;
    public MyClicks myClicks;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView songTitle, songArtist, songDuration, albumName;
        public ImageView albumArt;

        public MyViewHolder(View view, MyClicks listner){
            super(view);
            context = view.getContext();
            myClicks = listner;
            albumArt = (ImageView) view.findViewById(R.id.image);
            songTitle = (TextView) view.findViewById(R.id.song_title);
            songArtist = (TextView) view.findViewById(R.id.song_artist);
            songDuration = (TextView) view.findViewById(R.id.song_duration);
            albumName = (TextView) view.findViewById(R.id.song_albumName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClicks.clickOnRow(v, getLayoutPosition());
        }
    }

    public  RecycleSongAdapter (ArrayList<Song> songList){
        this.songList = songList;
    }

    public static interface MyClicks{
        public void clickOnRow(View info, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_layout_2, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView, new RecycleSongAdapter.MyClicks() {

            //Clicks for interaction.
            public void clickOnRow(View info, int position) {
                itemView.setTag(position);

                //Calling the songPicked function in the MainActivity to play the song.
                try{
                    ((MainActivity) info.getContext()).songPicked(itemView);
                } catch (Exception e){

                }


            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = songList.get(position);
        final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, song.getAlbumID());

        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
        } catch (Exception e){
            Log.d("BITMAP","Album art at " +position+ " not found");
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.album);
        }

        //TODO add contents
        holder.albumArt.setImageBitmap(bitmap);
        holder.songTitle.setText(song.getSongTitle());
        holder.songArtist.setText(song.getSongArtist());
        holder.songDuration.setText(convertDuration(song.getSongDuration()));
        holder.albumName.setText(song.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public String convertDuration(int durationInMS){
        int hour;
        int mins;
        int secs;

        hour = durationInMS / 3600000;
        durationInMS = durationInMS - (hour * 3600000);
        mins = durationInMS / 60000;
        durationInMS = durationInMS - (mins * 60000);
        secs = durationInMS / 1000;

        if (hour > 0){
            return (hour + ":" + mins + ":" + secs);
        } else {
            if ((mins/10) == 0) {
                if ((secs/10)==0){
                    return ("0"+ mins + ":0" + secs);
                }
                else {
                    return ("0"+ mins + ":" + secs);
                }
            }
            return (mins + ":" + secs);
        }

    }
}
