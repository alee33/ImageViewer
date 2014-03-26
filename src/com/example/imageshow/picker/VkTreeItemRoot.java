package com.example.imageshow.picker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.imageshow.R;
import com.example.imageshow.db.orm.DatabaseManager;
import com.example.imageshow.db.orm.Source;
import com.j256.ormlite.dao.Dao;

import android.content.Context;

/**
 * VK items source
 * contains all created items and new
 * 
 * @author user
 * 
 */
public class VkTreeItemRoot implements TreeItemImplementation {

    private String description;
    private Context context;

    public VkTreeItemRoot(String description, Context context) {
        this.description = description;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<TreeItemImplementation> getInnerItems() {
        Dao<Source, Long> sourceDao;
        List<TreeItemImplementation> listFr = new ArrayList<TreeItemImplementation>();
        try {
            sourceDao = DatabaseManager.getInstance().getHelper().getViolationSourceDao();
            List<Source> list = sourceDao.queryForAll();
            for (int i = 0; i < list.size(); i++) {
                // add all create items from DB
                listFr.add(new VkTreeItem(list.get(i), context));
            }
            // add new item
            listFr.add(new VkTreeItemCreate(context));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listFr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeItemImplementation getParentItem() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getImage() {
        // TODO Auto-generated method stub
        return R.drawable.vk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click(TreeFragmentListener listener) {
        listener.onRefreshSubs(this);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doubleClick(TreeFragmentListener listener) {
        // nothing

    }

}
