package com.example.diylog;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewHolder extends RecyclerView.ViewHolder {


    View mView;
    ImageView fvrt_btn, bookmark_btn;
    DatabaseReference favouriteref,bookmarkref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();




    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;


    }





    // set details to recycler view row
    public void setDetails(String date, String header, String main, String image, String mauthor, String key){
        //Views
        TextView mTitleTv = mView.findViewById(R.id.text);
        TextView mAuthor = mView.findViewById(R.id.author);
        TextView mDate = mView.findViewById(R.id.datetime);
        ImageView mImage = mView.findViewById(R.id.thumb);



        //set data to views
        mTitleTv.setText(header);
        mAuthor.setText(mauthor);
        mDate.setText(date);


        Picasso.get().load(image).into(mImage);

    }




    public void favouriteChecker(String postkey) {

        fvrt_btn = itemView.findViewById(R.id.love);
        favouriteref = database.getReference("favourites");


        favouriteref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(uid)){
                    fvrt_btn.setImageResource(R.drawable.heart);
                }else {
                    fvrt_btn.setImageResource(R.drawable.love);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void bookrefChecker(String postkey) {

        bookmark_btn = itemView.findViewById(R.id.bookmark_2);
        bookmarkref = database.getReference("bookmarks");

        bookmarkref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(uid)){
                    bookmark_btn.setImageResource(R.drawable.bookmark_3);
                }else {
                    bookmark_btn.setImageResource(R.drawable.bookmark_2);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


}
