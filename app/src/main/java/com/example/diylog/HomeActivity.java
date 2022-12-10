package com.example.diylog;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class HomeActivity extends AppCompatActivity {

    private ImageView add_file,thumb,delete_item,love,bookmark_2,bookmark,logout;
    AlertDialog dialog;
    RecyclerView quotes_list;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference,fvrtref,fvrt_listRef,bookref,bookmark_listRef;
    FirebaseAuth mAuth ;
    Boolean fvrtChecker =false,bookrefChecker=false;
    ArrayList<Article> list;
    SearchView searchView;




    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_reader);


        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, Login.class));
                finish();
            }
        });


        searchView = findViewById(R.id.search_bar);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        fvrtref = FirebaseDatabase.getInstance().getReference().child("favourites");
        fvrt_listRef = FirebaseDatabase.getInstance().getReference("favoriteList").child(mAuth.getCurrentUser().getUid());

        bookref = FirebaseDatabase.getInstance().getReference().child("bookmarks");
        bookmark_listRef = FirebaseDatabase.getInstance().getReference("bookmarkList");




         //Action Bar
        ActionBar actionBar = getSupportActionBar();
        // set title
        actionBar.setTitle("Article List");

        // RecyclerView
        quotes_list = findViewById(R.id.quotes_list);
        quotes_list.setHasFixedSize(true);

        // set layout as RelativeLayout
        quotes_list.setLayoutManager(new LinearLayoutManager(this));

        // send query to FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference("Article");

        getUserinfo();



        add_file = findViewById(R.id.add_file);
        add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, NewArticle.class);
                startActivity(i);
                finish();
            }
        });

        bookmark = findViewById(R.id.bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BookmarkActivity.class));
                finish();
            }
        });



    }




    private void getUserinfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){


                    if (dataSnapshot.hasChild("image"))
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
//                        Picasso.get().load(image).into(thumb);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    // search data
    private void firebaseSearch(String searchText){
        Query firebaseSearchQuery = databaseReference.orderByChild("header").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.list_item_layout,
                ViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                viewHolder.setDetails(model.getDate(),model.getHeader(),model.getMain(),model.getImage(), model.getAuthor(), model.getKey());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, LogDetailActivity.class);
                        intent.putExtra("ArticleKey", getRef(position).getKey());
                        startActivity(intent);

                    }
                });

            }
        };

        // set adapter to recyclerview
        quotes_list.setAdapter(firebaseRecyclerAdapter);

    }


    //load data into recycler view onStart
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model,ViewHolder>(
                        Model.class,
                        R.layout.list_item_layout,
                        ViewHolder.class,
                        databaseReference
                ){
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {




                        viewHolder.setDetails(model.getDate(),model.getHeader(),model.getMain(),model.getImage(), model.getAuthor(), model.getKey());





                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HomeActivity.this, LogDetailActivity.class);
                                intent.putExtra("ArticleKey", getRef(i).getKey());
                                startActivity(intent);

                            }
                        });


                    }

                    @Override
                    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
                        super.onBindViewHolder(viewHolder, position);

                        String currentUserid = mAuth.getCurrentUser().getUid();
                        final String postkey = getRef(position).getKey();
                        final String date = getItem(position).getDate();
                        String head = getItem(position).getHeader();
                        String main = getItem(position).getMain();
                        String image = getItem(position).getImage();
                        String author = getItem(position).getAuthor();
                        String userid = getItem(position).getKey();
                        viewHolder.favouriteChecker(postkey);
                        viewHolder.bookrefChecker(postkey);


                        love= viewHolder.mView.findViewById(R.id.love);
                        love.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fvrtChecker = true;
                                fvrtref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (fvrtChecker.equals(true) ){
                                            if(snapshot.child(postkey).hasChild(currentUserid)){
                                                fvrtref.child(postkey).child(currentUserid).removeValue();
                                                delete(date);
                                                fvrtChecker = false;
                                            }else{
                                                fvrtref.child(postkey).child(currentUserid).setValue(true);
                                                HashMap<String, Object> ArticleMap = new HashMap<>();
                                                ArticleMap.put("key", userid);
                                                ArticleMap.put("author",author);
                                                ArticleMap.put("head",head);
                                                ArticleMap.put("main", main);
                                                ArticleMap.put("image",image);
                                                ArticleMap.put("date",date);

                                                fvrt_listRef.push().setValue(ArticleMap);
                                                fvrtChecker = false;

                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                        bookmark_2 = viewHolder.mView.findViewById(R.id.bookmark_2);
                        bookmark_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bookrefChecker = true;
                                bookref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (bookrefChecker.equals(true) ){
                                            if(snapshot.child(postkey).hasChild(currentUserid)){
                                                bookref.child(postkey).child(currentUserid).removeValue();
                                                delete_bookmark_date(date);
                                                bookrefChecker = false;
                                            }else{
                                                bookref.child(postkey).child(currentUserid).setValue(true);
                                                HashMap<String, Object> BookMap = new HashMap<>();
                                                BookMap.put("key", userid);
                                                BookMap.put("author",author);
                                                BookMap.put("head",head);
                                                BookMap.put("main", main);
                                                BookMap.put("image",image);
                                                BookMap.put("date",date);

                                                bookmark_listRef.push().setValue(BookMap);
                                                bookrefChecker = false;

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });


                            }

                        });

                        delete_item = viewHolder.mView.findViewById(R.id.delete);
                        delete_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setTitle("Do you want to delete?");
                                builder.setMessage("Cannot undo your manipulation");

                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getRef(position).removeValue();
                                        notifyItemRemoved(position);
                                        delete_bookmark_date(date);




                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();

                            }
                        });


                    }



                };
              // set adapter to recyclerview
        quotes_list.setAdapter(firebaseRecyclerAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it presents
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // handle other action bar item clicks here
        if (id == R.id.action_settings){
            //TODO
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //
//
//    }


    void delete(String date) {

        Query query = fvrt_listRef.orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();

                    Toast.makeText(HomeActivity.this, "Love Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    void delete_bookmark_date(String date) {

        Query query = bookmark_listRef.orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();

                    Toast.makeText(HomeActivity.this, "Bookmark Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // do your stuff
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
}
