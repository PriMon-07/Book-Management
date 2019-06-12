package com.learn.user.dbapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.user.dbapplication.Data.PLibraryContracts;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.TitleViewHolder> {

    Context mContext;
    Cursor mCursor;

    final private ListItemClickListner mOnClickListner;

    public GenreAdapter(Context context, Cursor cursor,ListItemClickListner onClickListner){
        mContext=context;
        mCursor=cursor;
        mOnClickListner=onClickListner;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.item_holder,viewGroup,false);
        TitleViewHolder titleViewHolder=new TitleViewHolder(view);
        return titleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder titleViewHolder, int i) {

        if(!mCursor.moveToPosition(i))
            return;

        int id=mCursor.getInt(0);
        String name=mCursor.getString(1);
        int count=mCursor.getInt(2);

        titleViewHolder.bind(name,count);

    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView listItemName,listItemCount;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName=itemView.findViewById(R.id.tv_name);
            listItemCount=itemView.findViewById(R.id.tv_total_number);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        void bind(String genre,int count){
            listItemName.setText(genre);
            listItemCount.setText(String.valueOf(count));
        }

        @Override
        public void onClick(View v) {
            mOnClickListner.onListItemClick(getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnClickListner.onListItemClick(getAdapterPosition(),true);
            return true;
        }
    }

    public interface ListItemClickListner{
        void onListItemClick(int clickItemIndex,boolean onLongClick);
    }
}
