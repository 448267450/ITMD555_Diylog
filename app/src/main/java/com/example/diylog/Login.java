package com.example.diylog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText user_name, pass_word;
    private TextView signup;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() !=null){
            finish();
            return;
        }

        Button btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(v -> {
            
            authenticateUser();

        });

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               redirectToRegister();
            }
        });



    }



    private void authenticateUser() {

        user_name =findViewById(R.id.username);
        pass_word=findViewById(R.id.password);

        String username = user_name.getText().toString().trim();
        String password = pass_word.getText().toString().trim();

        if(username.isEmpty())
        {
            user_name.setError("Email is empty");
            user_name.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches())
        {
            user_name.setError("Enter the valid email");
            user_name.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            pass_word.setError("Password is empty");
            pass_word.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            pass_word.setError("Length of password is more than 6");
            pass_word.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                directToMainActivity();
            }
            else
            {
                Toast.makeText(Login.this,
                        "Please Check Your login Credentials",
                        Toast.LENGTH_SHORT).show();
            }

        });



    }

    private void directToMainActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void redirectToRegister() {
        startActivity(new Intent(Login.this,Registration.class ));
        finish();
    }
}
