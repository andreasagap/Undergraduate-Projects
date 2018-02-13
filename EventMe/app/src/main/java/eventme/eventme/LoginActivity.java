package eventme.eventme;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Owner;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private boolean flag;
    private EditText _email;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _passwordText=(EditText) findViewById(R.id.input_password);
        _email= (EditText) findViewById(R.id.email);
        _loginButton=(Button) findViewById(R.id.btn_login);
        _signupLink=(TextView) findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }
        final String email = _email.getText().toString();
        final String password = _passwordText.getText().toString();
        flag=true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User a= ds.getValue(User.class);
                    if(a.getEmail().equals(email.replace(".",",")) && a.getPassword().equals(password))
                    {
                        flag=false;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email", _email.getText().toString());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, Homepage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                }
                if(flag)
                    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

    }

    public boolean validate() {
        boolean valid = true;

        String username = _email.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _email.setError("Enter Valid Username");
            valid = false;
        }
        else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            _passwordText.setError("Small password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database

    }

}

