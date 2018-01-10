package com.example.saurav.basicswipegame;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Saurav on 12/15/2017.
 */

public class ArrowTextView extends android.support.v7.widget.AppCompatTextView {

    public final Arrow arrow;


    public ArrowTextView(Context context, Arrow arrow) {
        super(context);
        this.arrow = arrow;
        this.setText(arrow.repChar);

        resizeVextView();
    }

    public ArrowTextView(Context context, @Nullable AttributeSet attrs, Arrow arrow) {
        super(context, attrs);
        this.arrow = arrow;

        resizeVextView();
    }

    public ArrowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, Arrow arrow) {
        super(context, attrs, defStyleAttr);
        this.arrow = arrow;

        resizeVextView();
    }

    private void resizeVextView() {
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
    }
}
