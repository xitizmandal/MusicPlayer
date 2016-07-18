package np.com.xitiz.music;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by xitiz on 7/18/16.
 */
public class CustomLayoutManager extends LinearLayoutManager {
    private Context mContext;
    private static final float MILLISECOND_PER_INCH = 50f;

    public CustomLayoutManager(Context context){
        super(context);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return CustomLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECOND_PER_INCH/displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

//        super.smoothScrollToPosition(recyclerView, state, position);
    }
}
