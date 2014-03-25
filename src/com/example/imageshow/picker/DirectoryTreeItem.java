package com.example.imageshow.picker;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.example.imageshow.R;
import com.example.imageshow.SourceActivity.SourceType;

public class DirectoryTreeItem implements TreeItemImplementation, Comparable<DirectoryTreeItem> {

    private File file;
    private int level;
    private String description;
    
    private static final String TAG = BaseTreeFragment.class.getSimpleName();

    public DirectoryTreeItem(File file, int level) {
        this.file = file;
        this.level = level;
    }

    public DirectoryTreeItem(File file, String description) {
        this.file = file;
        this.level = 0;
        this.description = description;
    }

    @Override
    public String toString() {
        if (description == null)
            return file.getName();
        else
            return description;
    }

    @Override
    public int compareTo(DirectoryTreeItem f) {
        if (f.file.isDirectory() && this.file.isFile()) {
            return -1;
        } else {
            return this.toString().compareTo(f.toString());
        }

    }

    private void debug(String message, Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    private List<TreeItemImplementation> changeDirectory(File dir) {
        if (dir == null) {
            debug("Could not change folder: dir was null");
        } else if (!dir.isDirectory()) {
            debug("Could not change folder: dir is no directory");
        } else {
            File[] contents = dir.listFiles(ImageFilter.isValidImage);
            // if (contents != null) {

            List<TreeItemImplementation> files = new ArrayList<TreeItemImplementation>();
            if (contents != null) {
                for (File f : contents) {
                    if (isShow(f)) {
                        files.add(new DirectoryTreeItem(f, level + 1));
                    }
                }
            }
            // Collections.sort(files);
            debug("Changed directory to %s", dir.getAbsolutePath());
            return files;
        }
        return null;
    }

    private boolean isShow(File f) {
        if (f.isDirectory() && (f.listFiles(ImageFilter.isValidImage) != null)) {
            return true;
        } else if (f.isFile()) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<TreeItemImplementation> getInnerItems() {
        return changeDirectory(file);
    }

    @Override
    public TreeItemImplementation getParentItem() {
        File parent;
        if (level != 0 && (parent = file.getParentFile()) != null) {
            return new DirectoryTreeItem(parent, level - 1);
        }
        return null;
    }

    @Override
    public String getPath() {
        return file.getAbsolutePath();
    }

    @Override
    public int getImage() {
        if (file.isDirectory()) {
            return R.drawable.folder;
        } else {
            return R.drawable.file;
        }
    }

    @Override
    public void click(TreeFragmentListener listener) {
        listener.onRefreshSubs(this);

    }

    @Override
    public void doubleClick(TreeFragmentListener listener) {

        listener.returnValue(this, isExternal() ? SourceType.OUTER : SourceType.INNER);

    }

    private boolean isExternal() {
        return file.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    static class ImageFilter {

        /** The is valid image. */
        public static FileFilter isValidImage = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                final String name = pathname.getName();
                String ext = null;
                int i = name.lastIndexOf('.');
                if (i > 0 && i < name.length() - 1) {
                    ext = name.substring(i + 1).toLowerCase();
                }
                if (pathname.isDirectory()) {
                    return true;
                } else if (ext == null)
                    return false;
                else if (!ext.equals("jpg") && !ext.equals("jpeg") && !ext.equals("png") && !ext.equals("gif"))
                    return false;
                else
                    return true;
            }
        };
    }
}
