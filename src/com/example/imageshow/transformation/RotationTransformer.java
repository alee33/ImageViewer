package com.example.imageshow.transformation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Rotation transformation
 * @author user
 *
 */
public class RotationTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        page.setRotationY(position * -30);

    }

}
