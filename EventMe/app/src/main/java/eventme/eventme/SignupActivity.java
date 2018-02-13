package eventme.eventme;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.Serializable;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private EditText _nameText;
    private EditText _emailText;
    private DatabaseReference database;
    private EditText _passwordText;
    private EditText _reEnterPasswordText;
    private  Button _signupButton;
    private TextView _loginLink;
    private boolean flag;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // get Instance  of Database Adapter
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _nameText=(EditText) findViewById(R.id.name);
        _emailText=(EditText) findViewById(R.id.email);

        _passwordText=(EditText) findViewById(R.id.input_password);
        _reEnterPasswordText=(EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton=(Button) findViewById(R.id.btn_signup);

        _loginLink=(TextView) findViewById(R.id.link_login);
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }else {
            _signupButton.setEnabled(false);
            onSignupSuccess();



        }
    }
    public void onSignupSuccess() {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", _emailText.getText().toString());
        editor.apply();
        flag = false;

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        DatabaseReference ref = database1.getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User a = ds.getValue(User.class);
                    if (a.getEmail().equals(_emailText.getText().toString().replace(".", ","))) {
                        flag = true;

                    }
                }
                if (flag)
                {
                    _signupButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), "ΤΟ EMAIL ΧΡΗΣΙΜΟΠΟΙΗΤΑΙ ΗΔΗ", Toast.LENGTH_LONG).show();

                }
                else {

                    database = FirebaseDatabase.getInstance().getReference();
                    database.child("Users").child(_emailText.getText().toString().replace(".", ",")).setValue(new User(_nameText.getText().toString(), _emailText.getText().toString().replace(".", ","), _passwordText.getText().toString()));
                    Toast.makeText(SignupActivity.this, getString(R.string.accountCreated),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignupActivity.this, Homepage.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(),getString(R.string.loginfailed), Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();

        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getString(R.string.atleastcharacters));
            valid = false;
        } else {
            _nameText.setError(null);
        }



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.atleastcharacters));
            valid = false;
        } else {
            _emailText.setError(null);
        }



        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError(getString(R.string.passwordissmall));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError(getString(R.string.passworddonotmatch));
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}