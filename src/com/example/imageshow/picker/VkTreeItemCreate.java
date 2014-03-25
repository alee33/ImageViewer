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

public class VkTreeItemCreate extends VkTreeItem {

    public VkTreeItemCreate(Context context) {
        super(new Source(), context);
    }



    @Override
    public String getPath() {
        return context.getString(R.string.new_vk);
    }

    @Override
    public String toString() {
        return getPath();
    }

    @Override
    public int getImage() {
        // TODO Auto-generated method stub
        return R.drawable.ic_set_settings;
    }

    @Override
    public void click(final TreeFragmentListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.vk_id));

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(source.getText());
        builder.setView(input);


        
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                source.setText(input.getText().toString());
                Dao<Source, Long> sourceDao;
                try{
                    sourceDao = DatabaseManager.getInstance().getHelper().getViolationSourceDao();
                    sourceDao.create(source);
                    execute(RequestFactory.getDetailData(source.getId()),new RequestProgressHandler(listener));
                    
                }catch (SQLException e) {
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

    @Override
    public void doubleClick(TreeFragmentListener listener) {
        // nothing

    }
}
