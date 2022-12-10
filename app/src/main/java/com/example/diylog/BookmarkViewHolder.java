package com.example.diylog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

public class BookmarkViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public BookmarkViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

// portrait
    // recyclerview details
    public void setDetails(String image, String header, String author, String date){
        // Views
        TextView mTitleTv = mView.findViewById(R.id.b_text);
        TextView mAuthor = mView.findViewById(R.id.b_author);
        TextView mDate = mView.findViewById(R.id.b_date);
        ImageView mImage = mView.findViewById(R.id.portrait);

        //set data to views
        mTitleTv.setText(header);
        mAuthor.setText(author);
        mDate.setText(date);

        Picasso.get().load(image).into(mImage);

    }





}
