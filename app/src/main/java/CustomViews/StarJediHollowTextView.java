package CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by ADITYA on 3/24/2018.
 */

public class StarJediHollowTextView extends AppCompatTextView {

    public StarJediHollowTextView(Context context) {
        super(context);
        init();
    }

    public StarJediHollowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarJediHollowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"Fonts/Starjhol.ttf");
        setTypeface(tf);
    }
}
