package com.example.imageshow;

import com.example.imageshow.picker.BaseTreeFragment;
import com.example.imageshow.picker.DirectoryTreeItem;
import com.example.imageshow.picker.VkTreeItemRoot;
import com.example.imageshow.picker.BaseTreeFragment.SetSourceListener;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class SourceActivity extends FragmentActivity implements SetSourceListener {
    private SharedPreferences preferences;
    public static final String SOURCE_PATH = "source_path";
    public static final String SOURCE_KEY ="source";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source_layout);
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        // work here to change Activity fragments (add, remove, etc.). Example here of adding.
        fragmentTransaction.add(
                R.id.fragment,
                BaseTreeFragment.newInstance(this
                        , new DirectoryTreeItem(Environment.getExternalStorageDirectory(), getResources().getString(R.string.sd)),
                        new DirectoryTreeItem(Environment.getDataDirectory(), getResources().getString(R.string.inner))
                        , new VkTreeItemRoot(getResources().getString(R.string.vk), this)));

        fragmentTransaction.commit();
    }

    @Override
    public String setPath(SourceType type, String path) {
        // TODO Auto-generated method stub
        preferences.edit().putInt(SOURCE_KEY, type.getId()).commit();
        preferences.edit().putString(SOURCE_PATH, path).commit();
        finish();
        return null;
    }

    public enum SourceType{
        INNER(0), OUTER(1), VK(2), OTHER(-1);
        private int id;
        
        SourceType(int id){
            this.id=id;
        }
        public int  getId(){
            return id;
        }
       
        public static SourceType type(int n) {
            for (SourceType c : values()) {
              if (c.id == n) {
                return c;
              }
            }
            // either throw the IAE or return null, your choice.
            throw new IllegalArgumentException(String.valueOf(n));
          }
    }
}
