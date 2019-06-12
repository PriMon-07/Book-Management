package com.learn.user.dbapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {


    Context mContext;
    Cursor mCursor;

    final private ListItemClickListner mOnClickListner;

    public BookAdapter(Context context,Cursor cursor,ListItemClickListner onClickListner){
        mContext=context;
        mCursor=cursor;
        mOnClickListner=onClickListner;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.detail_view_holder,viewGroup,false);
        BookHolder bookHolder=new BookHolder(view);
        return bookHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder bookHolder, int i) {
        if(!mCursor.moveToPosition(i))
            return;

        int id=mCursor.getInt(0);
        String name=mCursor.getString(1);
        //int count=mCursor.getInt(2);

        bookHolder.bind(id,name);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView idTextView,nameTextView;
        ImageView deleteImageView;

        public BookHolder(@NonNull View itemView) {
            super(itemView);
            idTextView=itemView.findViewById(R.id.tv_book_id);
            nameTextView=itemView.findViewById(R.id.tv_book_name);
            deleteImageView=itemView.findViewById(R.id.iv_delete);
            itemView.setOnClickListener(this);
            deleteImageView.setOnClickListener(this);
        }

        public void bind(int id,String name){
            idTextView.setText(String.valueOf(id));
            nameTextView.setText(name);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            //mOnClickListner.onListItemClick(mCursor.getInt(0),false);

            if(v.getId()==R.id.iv_delete){
                mOnClickListner.onListItemClick(mCursor.getInt(0),true,mCursor.getString(1));
            }else{
                mOnClickListner.onListItemClick(mCursor.getInt(0),false,null);
            }

        }
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



    public interface ListItemClickListner{
        void onListItemClick(int clickItemIndex,boolean delete,String bookName);
    }
}
