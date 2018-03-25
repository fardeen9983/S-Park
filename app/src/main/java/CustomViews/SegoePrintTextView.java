package CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by ADITYA on 3/24/2018.
 */

public class SegoePrintTextView extends AppCompatTextView {

    public SegoePrintTextView(Context context) {
        super(context);
        init1();
    }

    public SegoePrintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init1();
    }

    public SegoePrintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init1();
    }
    private void init1(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"Fonts/911Fonts.com_SegoePrint__-_911fonts.com_fonts_oki4.ttf");
        setTypeface(tf);
    }
}
