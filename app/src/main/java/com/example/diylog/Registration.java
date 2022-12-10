package com.example.diylog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;

public class Registration extends AppCompatActivity {
//    FirebaseFirestore firestore;
    TextView signIn;

    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            finish();
            return;
        }
        
        Button btnRegister = findViewById(R.id.registration);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogin();
            }
        });



    }



    private void registerUser() {
        EditText efirstName = findViewById(R.id.firstName_signup);
        EditText elastName = findViewById(R.id.lastName_signup);
        EditText eemail = findViewById(R.id.email_signup);
        EditText epassword = findViewById(R.id.password_signup);


        String firstName = efirstName.getText().toString();
        String lastName = elastName.getText().toString();
        String email = eemail.getText().toString();
        String password = epassword.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty()  || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Blank is not allowed", Toast.LENGTH_LONG).show();
            return;
        }

        if(email.isEmpty())
        {
            eemail.setError("Email is empty");
            eemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            eemail.setError("Enter the valid email");
            eemail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            epassword.setError("Password is empty");
            epassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            epassword.setError("Length of password is more than 6");
            epassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(firstName, lastName, email);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            directToMainActivity();
                                        }
                                    });
;
                        } else {
//                            Toast.makeText(Registration.this, "Authentication failed",
//                                    Toast.LENGTH_LONG).show();
                            Toast.makeText(Registration.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }


                });
    }

    private void directToMainActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void switchToLogin() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }

}









