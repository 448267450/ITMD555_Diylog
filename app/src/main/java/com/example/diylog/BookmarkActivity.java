package com.example.diylog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView bookmark_list;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;
    private Button back;

    FirebaseAuth mAuth ;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_recyclerview);

        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        // set title
        actionBar.setTitle("Bookmark List");

        // RecyclerView
        bookmark_list = findViewById(R.id.bookmark_list);
        bookmark_list.setHasFixedSize(true);

        // set layout as RelativeLayout
        bookmark_list.setLayoutManager(new LinearLayoutManager(this));

        // send query to FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance();
   //      databaseReference = mDatabase.getReference("Article");
        databaseReference = mDatabase.getReference("bookmarkList");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookmarkActivity.this, HomeActivity.class));
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Model, BookmarkViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, BookmarkViewHolder>(
                        Model.class,
                        R.layout.bookmark_list_item,
                        BookmarkViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(BookmarkViewHolder bookmarkViewHolder, Model model, int i) {

                        bookmarkViewHolder.setDetails(model.getImage(),model.getAuthor(),model.getAuthor(),model.getDate());

//                        bookmarkViewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(BookmarkActivity.this, LogDetailActivity.class);
//                                intent.putExtra("ArticleKey", getRef(i).getKey());
//                                startActivity(intent);
//
//                            }
//                        });

                    }
                };



        bookmark_list.setAdapter(firebaseRecyclerAdapter);

    }
}
