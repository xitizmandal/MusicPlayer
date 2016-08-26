package np.com.xitiz.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xitiz on 8/25/16.
 */
public class MusicPlayActivity extends AppCompatActivity{
    private ImageView mAlbumArt;
    private TextView mSongTitleView;
    private TextView mSongArtistView;
    int imgSrc;
    public static final String EXTRA_PARAM_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_play);

        Intent intent = this.getIntent();
        imgSrc = intent.getExtras().getInt(EXTRA_PARAM_IMAGE);
        mAlbumArt = (ImageView) findViewById(R.id.imageViewOne);
        mAlbumArt.setImageResource(imgSrc);

    }
}
