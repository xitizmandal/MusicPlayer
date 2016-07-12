package np.com.xitiz.music;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by xitiz on 7/12/16.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{

    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songsList;
    private int songPosition;

    private final IBinder MUSIC_BIND = new MusicBinder();

    public void onCreate(){
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songsList){
        this.songsList = songsList;
    }

    /**
     * Class for binding MainActivity and MusicService.
     * */
    public class MusicBinder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return MUSIC_BIND;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void playSong(){
        mediaPlayer.reset();
        Song playSong = songsList.get(songPosition);
        long currentSong = playSong.getSongId();
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong);

        try{
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e){
            Log.e("MUSIC SERVICE","Error setting data source ", e);
        }

        mediaPlayer.prepareAsync();
    }

    public void setSong(int songIndex){
        songPosition = songIndex;
    }

}
