package np.com.xitiz.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xitiz on 7/13/16.
 */
public class RecycleSongAdapter extends RecyclerView.Adapter<RecycleSongAdapter.MyViewHolder> {
    private ArrayList<Song> songList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView songTitle, songArtist, songDuration;

        public MyViewHolder(View view){
            super(view);
            context = view.getContext();
            songTitle = (TextView) view.findViewById(R.id.song_title);
            songArtist = (TextView) view.findViewById(R.id.song_artist);
            songDuration = (TextView) view.findViewById(R.id.song_duration);
        }
    }

    public  RecycleSongAdapter (ArrayList<Song> songList){
        this.songList = songList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songTitle.setText(song.getSongTitle());
        holder.songArtist.setText(song.getSongArtist());
        holder.songDuration.setText(convertDuration(song.getSongDuration()));
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
