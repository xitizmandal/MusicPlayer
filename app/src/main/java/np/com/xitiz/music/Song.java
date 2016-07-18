package np.com.xitiz.music;

/**
 * Created by xitiz on 7/12/16.
 */
public class Song {
    private long songId;
    private String songTitle;
    private String songArtist;
    private int songDuration;
    private long albumID;
    private String albumName;

    public Song(long songId, String songTitle, String songArtist, int songDuration, long albumId, String albumName){
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songDuration = songDuration;
        this.albumID = albumId;
        this.albumName = albumName;
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

    public int getSongDuration() {
        return songDuration;
    }

    public long getAlbumID() {
        return albumID;
    }

    public String getAlbumName(){
        return albumName;
    }

}
