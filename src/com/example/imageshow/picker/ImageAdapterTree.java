package com.example.imageshow.picker;

import java.util.ArrayList;
import java.util.List;

import com.example.imageshow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Tree item adapter
 */
public class ImageAdapterTree extends ArrayAdapter<TreeItemImplementation> {
    private List<TreeItemImplementation> data;
    private int layoutResourceId;
    private Context context;

    public ImageAdapterTree(Context context, int layoutResourceId, ArrayList<TreeItemImplementation> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;

    }

    /**
     * {@inheritDoc}
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        TreeItemImplementation item = data.get(position);
        holder.txtTitle.setText(item.toString());
        holder.imageItem.setImageResource(item.getImage());
        return row;

    }
}