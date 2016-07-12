package np.com.xitiz.music;

/**
 * Created by xitiz on 7/12/16.
 */
public class Song {
    private long songId;
    private String songTitle;
    private String songArtist;

    public Song(long songId, String songTitle, String songArtist){
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }

    public long getSongId(){
        return songId;
    }

    public String getSongTitle(){
        return songTitle;
    }

    public String getSongArtist(){
        return songArtist;
    }

}
