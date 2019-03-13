package com.example.wechat.adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.model.Messages;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Messages> userMessagesList;
    String messageSenderID,receriverID;

    public MessageAdapter(List<Messages> userMessagesList,String messageSenderID,String receriverID){


        this.userMessagesList=userMessagesList;
        this.messageSenderID=messageSenderID;
        this.receriverID=receriverID;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout,parent,false);

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        Messages messages=userMessagesList.get(position);
        String fromID=messages.getFrom();
        if(fromID.equals(messageSenderID)){

            holder.message.setBackgroundResource(R.drawable.message_receiver_bg);
            holder.message.setTextColor(Color.BLACK);
            holder.message.setGravity(Gravity.RIGHT);

        }
        else {
            holder.message.setBackgroundResource(R.drawable.message_text_bg);

            holder.message.setTextColor(Color.WHITE);
            holder.message.setText(userMessagesList.get(position).getMessage());
            holder.message.setGravity(Gravity.RIGHT);

        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView message;
       public CircleImageView userProfile;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.user_message);
            userProfile=itemView.findViewById(R.id.user_image);
        }
    }

}
