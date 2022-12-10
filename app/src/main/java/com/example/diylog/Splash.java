package com.example.diylog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import org.jetbrains.annotations.NotNull;

public class Splash extends AppCompatActivity {

    FirebaseAuth mAuth;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


              mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                  @Override
                  public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                      if(mAuth.getCurrentUser() != null){
                          startActivity(new Intent(Splash.this, HomeActivity.class));
                          finish();
                      } else {
                          startActivity(new Intent(Splash.this, Login.class));
                          finish();
                      }
                  }
              });



            }
        }, 2000);
    }
}
