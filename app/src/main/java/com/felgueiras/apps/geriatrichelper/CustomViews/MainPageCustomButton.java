package com.felgueiras.apps.geriatrichelper.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.R;

/**
 * Created by felgueiras on 30/03/2017.
 */

public class MainPageCustomButton extends LinearLayout {

    private int pageIcon;
    private CharSequence pageName;


    public MainPageCustomButton(Context context) {
        super(context);
        initializeViews(context);
    }

    public MainPageCustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainPageCustomButton);
        pageName = typedArray.getString(R.styleable.MainPageCustomButton_pageName);
        pageIcon = typedArray.getResourceId(R.styleable.MainPageCustomButton_pageIcon,0);
        typedArray.recycle();

        initializeViews(context);
    }

    public MainPageCustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainPageCustomButton);
        pageName = typedArray.getString(R.styleable.MainPageCustomButton_pageName);
        pageIcon = typedArray.getResourceId(R.styleable.MainPageCustomButton_pageIcon,0);

        typedArray.recycle();

        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_button, this);
    }


    @Override
    protected void onFinishInflate() {

        // When the controls in the layout are doing being inflated, set
        // the callbacks for the side arrows.
        super.onFinishInflate();

        // When the previous button is pressed, select the previous value
        // in the list.
        ImageView buttonImage = this.findViewById(R.id.custombutton_image);
        TextView buttonText = this.findViewById(R.id.custombutton_text);

        // set text and image
        buttonImage.setImageResource(pageIcon);
        buttonText.setText(pageName);

    }

    /**
     * Identifier for the state to save the selected index of
     * the side spinner.
     */
    private static String STATE_SELECTED_INDEX = "SelectedIndex";

    /**
     * Identifier for the state of the super class.
     */
    private static String STATE_SUPER_CLASS = "SuperClass";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(STATE_SUPER_CLASS, super.onSaveInstanceState());
//        bundle.putInt(STATE_SELECTED_INDEX, mSelectedIndex);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            super.onRestoreInstanceState(bundle.getParcelable(STATE_SUPER_CLASS));
//            setSelectedIndex(bundle.getInt(STATE_SELECTED_INDEX));
        } else
            super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views in the side
        // spinner are not saved since we handle the state in the
        // onSaveInstanceState.
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        // Makes sure that the state of the child views in the side
        // spinner are not restored since we handle the state in the
        // onSaveInstanceState.
        super.dispatchThawSelfOnly(container);
    }
}