package com.example.hp.mycloudmusic.adapter.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;

import java.util.List;

public class PopupAdapter extends ArrayAdapter<PopupItem> {
    private int resourceId;

    public PopupAdapter(Context context, int resource, List<PopupItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PopupItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imageView = view.findViewById(R.id.pop_item_image);
        TextView title = view.findViewById(R.id.pop_item_text);
        imageView.setImageResource(item.imageId);
        title.setText(item.title);
        return view;
    }
}
