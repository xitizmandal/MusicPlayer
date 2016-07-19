package np.com.xitiz.music;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xitiz on 7/12/16.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{

    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songsList;
    private int songPosition;

    private final IBinder MUSIC_BIND = new MusicBinder();

    private String songTitle;
    private String songArtist;
    private String songAlbumName;
    private long songAlbumID;
    private static final int NOTIFY_ID = 1;

    private boolean shuffle = false;
    private Random random;

    public void onCreate(){
        super.onCreate();
        songPosition = 0;
        random = new Random();
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

    public boolean setShuffle(){
        if (shuffle){
            shuffle = false;
        } else {
            shuffle =  true;
        }
        return shuffle;
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
        createNotification();


    }

    public void createNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * Notification builder builds the notification that is seen in the notification area.
         * */
        Notification.Builder builder = new Notification.Builder(this);

        final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, songAlbumID);

        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumArtUri);
        } catch (IOException e)
        {
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.album);
        }



        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.album_white)
//                .setLargeIcon(bitmap)
                .setTicker(songTitle)
                .setOngoing(true);
//                .setContentTitle(songTitle)
//                .setContentText(songArtist);

        RemoteViews remoteViews = new RemoteViews(getPackageName() , R.layout.notification);
//        remoteViews.setImageViewResource(R.id.image , R.drawable.album);
        remoteViews.setImageViewUri(R.id.imageViewAlbumArt, albumArtUri);
//        remoteViews.setImageViewBitmap(R.id.imageViewAlbumArt, bitmap);
        remoteViews.setTextViewText(R.id.song_title , songTitle);
        remoteViews.setTextViewText(R.id.song_artist, songArtist);
        builder.setContent(remoteViews);

        Notification notification = builder.build();
        notification.bigContentView = remoteViews;

        startForeground(NOTIFY_ID, notification);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    //TODO add contents
    public void playSong(){
        mediaPlayer.reset();
        Song playSong = songsList.get(songPosition);
        songTitle = playSong.getSongTitle();
        songArtist = playSong.getSongArtist();
        songAlbumName = playSong.getAlbumName();
        songAlbumID = playSong.getAlbumID();
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

    public int getSongPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getSongDuration(){
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }

    public void seek(int position){
        mediaPlayer.seekTo(position);
    }

    public void go(){
        mediaPlayer.start();
    }

    public void playPrev(){
        songPosition--;
        if(songPosition == 0){
            songPosition = songsList.size() - 1;
        }
        playSong();
    }

    public void playNext(){
        if(shuffle){
            int newSong = songPosition;
            while (newSong == songPosition){
                newSong = random.nextInt(songsList.size());
            }
            songPosition = newSong;
        } else {
            songPosition++;
            if (songPosition == songsList.size()) {
                songPosition = 0;
            }
        }
        playSong();

    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}
