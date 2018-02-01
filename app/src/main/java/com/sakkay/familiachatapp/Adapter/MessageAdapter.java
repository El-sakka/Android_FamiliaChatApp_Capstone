package com.sakkay.familiachatapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sakkay.familiachatapp.Model.Message;
import com.sakkay.familiachatapp.R;

import java.util.List;

/**
 * Created by mahmoudelsakka on 31/01/18.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, int resource, List<Message> object){
        super(context,resource,object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_layout,
                    parent,false);
        }

        ImageView mImageView = (ImageView)convertView.findViewById(R.id.iv_photo);
        TextView mMessageTextView = (TextView) convertView.findViewById(R.id.tv_message);
        TextView mAutherTextView = (TextView)convertView.findViewById(R.id.tv_name);

        Message message = getItem(position);

        boolean isImage = message.getPhotoUrl() != null;

        if(isImage){
            mMessageTextView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(mImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(mImageView);
        }
        else{
            mMessageTextView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mMessageTextView.setText(message.getText());
        }
        mAutherTextView.setText(message.getName());

        return convertView;
    }
}
