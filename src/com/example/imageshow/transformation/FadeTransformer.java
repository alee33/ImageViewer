package com.example.imageshow.transformation;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

/**
 * Fadt transformation
 * @author user
 *
 */
public class FadeTransformer implements PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        final float normalizedposition = Math.abs(Math.abs(position) - 1);
        page.setAlpha(normalizedposition);

    }

}
