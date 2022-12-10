package com.example.diylog;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.widget.Toast.*;


public class NewArticle extends AppCompatActivity  {

    private Button cancel_btn, submit_btn;
    private EditText header_title,mAuthor;
    private TextView idTVCurrent;
    private AppCompatEditText mainBody;
    private ImageView picture,thumb;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth ;
    //    private int trans_id;
    private String myUri = "";
    private StorageTask uploadTask;
    String header,date,main,downloadURL,author;



    // on below line we are creating a variable.
   TextView  currentTV;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_article);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Article");
        storageReference = FirebaseStorage.getInstance().getReference().child("Image");

        thumb = findViewById(R.id.thumb);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        header_title = findViewById(R.id.header_title);
        mainBody = findViewById(R.id.mainBody);
        idTVCurrent = findViewById(R.id.idTVCurrent);
        mAuthor = findViewById(R.id.author);



        // on below line we are initializing our variables.
        TextView currentTV;

        // on below line we are initializing our variables.
        currentTV = findViewById(R.id.idTVCurrent);

        // on below line we are creating and initializing
        // variable for simple date format.
        SimpleDateFormat sdf = new SimpleDateFormat("'Date 'dd-MM-yyyy ");

        // on below line we are creating a variable
        // for current date and time and calling a simple date format in it.
        String currentDateAndTime = sdf.format(new Date());

        // on below line we are setting current
        // date and time to our text view.
        currentTV.setText(currentDateAndTime);

        cancel_btn = findViewById(R.id.cancel_button);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewArticle.this, HomeActivity.class));
            }
        });

        picture = findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();

            }
        });




        submit_btn = findViewById(R.id.submit_button);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                trans_id += trans_id + Math.random()*10000000;
                uploadPicture();


                startActivity(new Intent(NewArticle.this, HomeActivity.class));
                finish();
                
            }
        });





    }




    private void choosePicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
           picture.setImageURI(imageUri);
//          Picasso.with(NewArticle.this).load(imageUri).into(thumb);
        } else {
            Toast.makeText(this, "Error, try again", LENGTH_SHORT).show();
        }
    }

    private void uploadPicture() {

//       final ProgressDialog pd = new ProgressDialog(this);
//        pd.setTitle("Uploading image......");
//        pd.setMessage("please wait, while we're setting your data");
//        pd.show();

        if (imageUri != null){
            final StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getUid()+".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull @NotNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        String header = header_title.getText().toString();
                        String date = idTVCurrent.getText().toString();
                        String main = mainBody.getText().toString();
                        String author = mAuthor.getText().toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);
                        userMap.put("date", date);
                        userMap.put("header", header);
                        userMap.put("main", main);
                        userMap.put("author",author);




                        databaseReference.push().setValue(userMap);
//                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);



                    }
                }
            });

        }
        else{
//            pd.dismiss();
            Toast.makeText(this, "Image not selected.", LENGTH_SHORT).show();
        }





    }



    private Object getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }

    private void insetArticleData() {
        String header = header_title.getText().toString();
        String date = idTVCurrent.getText().toString();
        String main = mainBody.getText().toString();
        String author = mAuthor.getText().toString();
        String key = mAuth.getCurrentUser().getUid();



        Article article = new Article(header, date, main,downloadURL, author, key);
//        String articleId = articleDbRef.push().getKey();
//        articleDbRef.child(articleId).setValue(article);

     databaseReference.push().setValue(article);
        makeText(NewArticle.this, "Article submitted", LENGTH_LONG).show();
    }

}
