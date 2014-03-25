package com.example.imageshow.transformation;

import android.support.v4.view.ViewPager;


/**
 * Transformations fabrica
 * @author user
 *
 */
public class Transformation {
  
    private static Transformation sInstance;
    
    private Transformation(){
        
    }
    
    public static Transformation getInstance() {
        if (sInstance == null) {
            sInstance = new Transformation();
        }
        return sInstance;
    }
    
    public ViewPager.PageTransformer getTransformer(int id) {
        switch (id) {
        case 0:
            return new FadeTransformer();
        case 1:
            return new RotationTransformer();
        }
        return null;
    }

}
