package np.com.xitiz.music;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xitiz on 7/13/16.
 */
public class RecycleSongAdapter extends RecyclerView.Adapter<RecycleSongAdapter.MyViewHolder> {
    private ArrayList<Song> songList;
    private Context context;
    public MyClicks myClicks;
    private MainActivity mActivity;
    private int albumArtColor;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView songTitle, songArtist, songDuration, albumName;
        public ImageView albumArt;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view, MyClicks listner){
            super(view);
            context = view.getContext();
            myClicks = listner;
            albumArt = (ImageView) view.findViewById(R.id.image);
            songTitle = (TextView) view.findViewById(R.id.song_title);
            songArtist = (TextView) view.findViewById(R.id.song_artist);
            songDuration = (TextView) view.findViewById(R.id.song_duration);
            albumName = (TextView) view.findViewById(R.id.song_albumName);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClicks.clickOnRow(v, getLayoutPosition());

        }
    }

    public  RecycleSongAdapter (ArrayList<Song> songList, MainActivity activity){
        this.songList = songList;
        this.mActivity = activity;
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

                Song song = songList.get(position);
                final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                final Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, song.getAlbumID());

                Bitmap bitmap = null;
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
                } catch (Exception e){
                    Log.d("BITMAP","Album art at " +position+ " not found");
                    bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.album_white);
                }
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getDarkVibrantColor(context.getResources().getColor(android.R.color.black));
//                       albumArtColor = bgColor;
                        mActivity.backgroundTransistion(bgColor);

                    }
                });
                //Calling the songPicked function in the MainActivity to play1 the song.
                try{
//                    albumArtColor = Color.RED;

                    ((MainActivity) info.getContext()).songPicked(itemView);

                    Log.d("RSA","Working");
                } catch (Exception e){

                }


            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Song song = songList.get(position);
        final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        final Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, song.getAlbumID());

        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
        } catch (Exception e){
            Log.d("BITMAP","Album art at " +position+ " not found");
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.album_white);
        }

        //TODO add contents
//        holder.albumArt.setImageBitmap(bitmap);
        /*if (albumArtUri == null){
            holder.albumArt.setImageResource(R.drawable.album_white);
        } else {
            Log.d("BITMAP","Album art at " +position+ " is " + albumArtUri.toString());
            holder.albumArt.setImageURI(null);
            holder.albumArt.setImageURI(albumArtUri);
        }*/
        holder.albumArt.setImageBitmap(bitmap);
//        Glide.with(context).load(new File(albumArtUri.getPath())).into(holder.albumArt);
        holder.songTitle.setText(song.getSongTitle());
        holder.songArtist.setText(song.getSongArtist());
        holder.songDuration.setText(convertDuration(song.getSongDuration()));
        holder.albumName.setText(song.getAlbumName());

  /*      Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int bgColor = palette.getLightMutedColor(context.getResources().getColor(android.R.color.black));
//                holder.relativeLayout.setBackgroundColor(bgColor);
                albumArtColor = bgColor;
            }
        });*/
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
