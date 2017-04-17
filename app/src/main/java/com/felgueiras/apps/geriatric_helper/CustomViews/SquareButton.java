package com.felgueiras.apps.geriatric_helper.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by felgueiras on 31/03/2017.
 */

public class SquareButton extends android.support.v7.widget.AppCompatButton {
    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int WidthSpec, int HeightSpec) {
        int newWidthSpec = HeightSpec;

        super.onMeasure(newWidthSpec, HeightSpec);
    }
}
