package com.example.dflet.scripttanklogindemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewAdapterViewHolder> {
    private ArrayList<TestItem> mItemList;
    public onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }






    //view holder: gets values and assigns them for us
    public static class ViewAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public ViewAdapterViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.editorPicture);
            mTextView1 = itemView.findViewById(R.id.line1text);
            mTextView2 = itemView.findViewById(R.id.line2text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ViewAdapter(ArrayList<TestItem> itemList) {
        mItemList = itemList;

    }

    @NonNull

    @Override
    public ViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.placeholder_item, parent, false);
        ViewAdapterViewHolder vavh = new ViewAdapterViewHolder(v, mListener);
        return vavh;
    }
    //goes through each element and makes a card for each
    @Override
    public void onBindViewHolder(@NonNull ViewAdapterViewHolder holder, int position) {
        TestItem currentItem = mItemList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());



    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}