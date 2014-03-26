package com.example.imageshow.picker;

import java.sql.SQLException;

import com.example.imageshow.R;
import com.example.imageshow.db.orm.DatabaseManager;
import com.example.imageshow.db.orm.Source;
import com.example.imageshow.rest.RequestFactory;
import com.j256.ormlite.dao.Dao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

/**
 * New Vk item create
 * 
 * @author user
 * 
 */
public class VkTreeItemCreate extends VkTreeItem {

    public VkTreeItemCreate(Context context) {
        super(new Source(), context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return context.getString(R.string.new_vk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getImage() {
        // TODO Auto-generated method stub
        return R.drawable.ic_set_settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click(final TreeFragmentListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.vk_id));

        // Set up the input for source
        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(source.getText());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().length()==0){
                    return;
                }
                source.setText(input.getText().toString());
                Dao<Source, Long> sourceDao;
                try {
                    sourceDao = DatabaseManager.getInstance().getHelper().getViolationSourceDao();
                    sourceDao.create(source);
                    // perform page detail request
                    execute(RequestFactory.getDetailData(source.getId()), new RequestProgressHandler(listener));

                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doubleClick(TreeFragmentListener listener) {
        // nothing

    }
}
