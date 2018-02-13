package eventme.eventme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class profileEdit extends AppCompatActivity {

    private EditText et_name;
    private EditText et_pass;
    private EditText et_email;
    private ImageView buttonUploadImage;
    private StorageReference mStorageRef;
    private static final int SELECT_PICTURE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        et_name = (EditText) findViewById(R.id.etonoma);
        et_pass = (EditText) findViewById(R.id.etcode);
        et_email = (EditText) findViewById(R.id.etemail);

        buttonUploadImage = (ImageView) findViewById(R.id.userphoto);
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                selectImage();
            }

        });
        buttonUploadImage.setAdjustViewBounds(true);
        buttonUploadImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Intent intent = getIntent();

        String cn = intent.getStringExtra("stringname");

        String ce = intent.getStringExtra("stringemail");

        et_name.setText(cn);
        et_pass.setText(intent.getStringExtra("password"));
        et_email.setText(ce);
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                //Get ImageURi and load with help of picasso

                Picasso.with(profileEdit.this).load(data.getData()).centerInside().fit()
                        .into((ImageView) findViewById(R.id.userphoto));
            }
            //Bitmap bitmap = getPath(data.getData());
            //buttonUploadImage.setImageBitmap(bitmap);

        }
    }

    public void saveChanges(View view) {
        String newname = et_name.getText().toString();
        String newemail = et_email.getText().toString();
        String newpass = et_pass.getText().toString();
        if (newname.length() < 3 || newpass.length() < 6) {
            Toast.makeText(profileEdit.this, getString(R.string.completeallfields),
                    Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase.getInstance().getReference().child("Users").child(newemail.replace(".", ",")).child("username").setValue(newname);
            FirebaseDatabase.getInstance().getReference().child("Users").child(newemail.replace(".", ",")).child("password").setValue(newpass);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!

                }
            });
            buttonUploadImage.setDrawingCacheEnabled(true);
            buttonUploadImage.buildDrawingCache();
            Bitmap bitmap = buttonUploadImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference mref = mStorageRef.child(newemail + ".jpg");
            UploadTask uploadTask = mref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Log.i("storage", "SUCCESS");
                }
            });

            Toast.makeText(profileEdit.this, "Done!",
                    Toast.LENGTH_SHORT).show();
            this.finish();

        }
    }
}
