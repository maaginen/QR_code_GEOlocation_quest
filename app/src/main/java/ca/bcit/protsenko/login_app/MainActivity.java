package ca.bcit.protsenko.login_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference reff;
    User user;
    private FusedLocationProviderClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailMain);
        password = findViewById(R.id.passwordMain);
        btnSignUp = findViewById(R.id.signUpMain);
        tvSignIn = findViewById(R.id.signInMain);
        user = new User();
        reff = FirebaseDatabase.getInstance().getReference().child("User");
        //reff = FirebaseDatabase.getInstance().getReference().child("UserLocation");
        client = LocationServices.getFusedLocationProviderClient(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("server/saving-data/one");

        requestPermission();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                final String pw = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                }
                else if(pw.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pw.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_LONG).show();
                }
                else if(!(email.isEmpty() && pw.isEmpty())){


                    mFirebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                           if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Sign Up Unsuccessful. Please try again.", Toast.LENGTH_LONG).show();
                            }
                            else {



                               //Getting Location
                               if(ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                   return;
                               }

                               client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                                   @Override
                                   public void onSuccess(Location location) {
                                       if(location != null){
                                           user.setLoc(location.toString());
                                           user.setEmail_stored(email);
                                           user.setPw_stored(pw);
                                           reff.push().setValue(user);
                                       }
                                   }
                               });



                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this, "Error. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
