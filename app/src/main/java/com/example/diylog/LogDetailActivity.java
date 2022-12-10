package com.example.diylog;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

public class LogDetailActivity extends AppCompatActivity {
    private ImageView image_detail;
    TextView article_detail;

    DatabaseReference ref;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_detail);

        image_detail = findViewById(R.id.image);
        article_detail = findViewById(R.id.quote);

        ref = FirebaseDatabase.getInstance().getReference().child("Article");


        String ArticleKey = getIntent().getStringExtra("ArticleKey");


        ref.child(ArticleKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String articleDetail = dataSnapshot.child("main").getValue().toString();
                    String imageUrl = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(imageUrl).into(image_detail);
                    article_detail.setText(articleDetail);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });









    }
}
