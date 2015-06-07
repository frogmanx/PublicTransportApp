package com.example.adam.pubtrans.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.adam.pubtrans.R;
import com.github.clans.fab.FloatingActionButton;

/**
 * Created by Adam on 7/06/2015.
 */
public class SelectableFloatingActionButton extends FloatingActionButton{
    private boolean mSelected;
    public SelectableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelected(boolean select) {
        super.setSelected(select);
        if(isSelected()) {
            setColorNormal(getResources().getColor(R.color.primary));
            setColorRipple(getResources().getColor(R.color.primaryDark));
        }
        else {
            setColorNormal(getResources().getColor(R.color.secondary));

            setColorRipple(getResources().getColor(R.color.secondary));
        }
    }

}
