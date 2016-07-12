package np.com.xitiz.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xitiz on 7/12/16.
 */
public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songsList;
    private LayoutInflater songInf;

    public SongAdapter(Context context, ArrayList<Song> songsList){
        this.songsList = songsList;
        songInf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, parent, false);
        TextView songTitleView = (TextView) songLayout.findViewById(R.id.song_title);
        TextView songArtistView = (TextView) songLayout.findViewById(R.id.song_artist);

        Song currentSong = songsList.get(position);
        songTitleView.setText(currentSong.getSongTitle());
        songArtistView.setText(currentSong.getSongArtist());

        songLayout.setTag(position);
        return songLayout;
    }
}
