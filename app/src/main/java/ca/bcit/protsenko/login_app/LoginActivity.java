package ca.bcit.protsenko.login_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        btnSignIn = findViewById(R.id.signUpLoginBtn);
        tvSignUp = findViewById(R.id.signUpLogin);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this,"You are loggen in", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this,"Login please.", Toast.LENGTH_LONG).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pw = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                }
                else if(pw.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pw.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_LONG).show();
                }
                else if(!(email.isEmpty() && pw.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error. Please try again.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent intHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
