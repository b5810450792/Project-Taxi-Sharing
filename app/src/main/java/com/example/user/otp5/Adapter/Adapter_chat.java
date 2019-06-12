package com.example.user.otp5.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.otp5.Model.Account;
import com.example.user.otp5.Model.Chat;
import com.example.user.otp5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_chat extends RecyclerView.Adapter<Adapter_chat.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private ArrayList<Chat> mChat;
    private String imageurl;
    ArrayList<Account> accounts;
    FirebaseUser fuser;
    private OnItemClickListener mListener;
    DatabaseReference reference;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public Adapter_chat(Context mContext, ArrayList<Chat> mChat) {
        this.mContext = mContext;
        this.mChat = mChat;

    }

    @NonNull
    @Override
    public Adapter_chat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {

        if(viewtype == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new Adapter_chat.ViewHolder(view,mListener);
        }else {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new Adapter_chat.ViewHolder(view,mListener);
        }
    }



    @Override
    public void onBindViewHolder(final Adapter_chat.ViewHolder viewHolder, int position) {
        Chat chat = mChat.get(position);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

                viewHolder.show_message.setText(chat.getMessage());
                //viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
                if (chat.getImage().equals("default")) {
                    //default picture
                } else {
                    Glide.with(mContext).load(chat.getImage()).into(viewHolder.profile_image);
                }
                Log.i("nameee",chat.getName_user());
                viewHolder.name.setText(chat.getName_user()+"")
                ;

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public TextView name;
        public ImageView profile_image;
        public CircleImageView myphoto;

        public ViewHolder(final View itemView, final OnItemClickListener listener) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name_id);
            myphoto = itemView.findViewById(R.id.profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                    mBuilder.setCancelable(false);

                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())
                ){

//            Log.i("name right",mChat.get(position).getName_user());
//            Log.i("sender right",mChat.get(position).getSender());
//            Log.i("message right",mChat.get(position).getMessage());
            return MSG_TYPE_RIGHT;
        }
        else {
//            Log.i("name left",mChat.get(position).getName_user());
//            Log.i("key2 left",mChat.get(position).getReceiver2());
//            Log.i("key3 left",mChat.get(position).getReceiver3());
//            Log.i("key4 left",mChat.get(position).getReceiver4());
//            Log.i("key5 left",mChat.get(position).getReceiver5());
//            Log.i("message left",mChat.get(position).getMessage());
            return MSG_TYPE_LEFT;
        }
    }
}
