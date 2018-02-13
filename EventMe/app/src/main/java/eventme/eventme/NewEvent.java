package eventme.eventme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;


import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class NewEvent extends AppCompatActivity {
    private StorageReference mStorageRef;
    private ImageView buttonUploadImage;
    private  static final int SELECT_PICTURE = 0 ;

    private Button DateButton,TimeButton,LocationButton,SaveButton,EventNameButton;
    private DatabaseReference database;
    private EditText editText;
    private SharedPreferences preferences;
    String email;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        email = preferences.getString("email", "");

        buttonUploadImage = (ImageView) findViewById(R.id.UploadButton);
        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                selectImage();
            }

        });
        //buttonUploadImage.setAdjustViewBounds(true);

        DateButton=(Button) findViewById(R.id.DateButton);
        TimeButton=(Button) findViewById(R.id.TimeButton);
        SaveButton=(Button) findViewById(R.id.SaveBtn);
        LocationButton=(Button) findViewById(R.id.LocationButton);
        EventNameButton=(Button) findViewById(R.id.NameButton);
        editText=(EditText) findViewById(R.id.editText);

        geocoder=new Geocoder(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                //Get ImageURi and load with help of picasso

                Picasso.with(NewEvent.this).load(data.getData()).centerInside().fit().into(buttonUploadImage);
                buttonUploadImage.setAdjustViewBounds(true);
                buttonUploadImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            //Bitmap bitmap = getPath(data.getData());
            //buttonUploadImage.setImageBitmap(bitmap);
        }
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void ClickEventName(View v) // Click the button Event Name
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);
        builder.setTitle("Event Name");
        final EditText input = new EditText(NewEvent.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventNameButton.setText( input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public void ClickDate(View v) // Click the button Date
    {
        calendarDialog();
    }
    public void ClickLocation(View v) // Click the button Location
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEvent.this);
        builder.setTitle("Location");
        final EditText input = new EditText(NewEvent.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String loc=input.getText().toString();
                List<Address> list= null;
                try {
                    list = geocoder.getFromLocationName(loc,1);
                } catch (IOException e) {
                    e.printStackTrace();
                    input.setText("invalid");
                }

                LocationButton.setText(loc);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void ClickTime(View v) // Click the button Time
    {
        timerDialog();
    }
    public void Done(View v) // Click the button for share the event
    {
        database= FirebaseDatabase.getInstance().getReference();
        String datebutton=DateButton.getText().toString();
        String timebutton = TimeButton.getText().toString();
        String locationbutton = LocationButton.getText().toString();
        String eventname=EventNameButton.getText().toString();

        if(datebutton.equals(getResources().getString(R.string.checkDate))
                || timebutton.equals(getResources().getString(R.string.checkhour))
                ||locationbutton.equals(getResources().getString(R.string.checkLocation))
                ||locationbutton.equals("")||eventname.equals(getResources().getString(R.string.checkEventsName))
                ||eventname.equals("") || locationbutton.equals("invalid") )
        {
            Toast.makeText(getBaseContext(),getString(R.string.completeallfields), Toast.LENGTH_LONG).show();
        }
        else
        {
            database.child("Event").push().setValue(new Event(email, DateButton.getText().toString(), TimeButton.getText().toString(), LocationButton.getText().toString(), editText.getText().toString(), EventNameButton.getText().toString()));
            mStorageRef = FirebaseStorage.getInstance().getReference();
            buttonUploadImage.setDrawingCacheEnabled(true);
            buttonUploadImage.buildDrawingCache();
            Bitmap bitmap = buttonUploadImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference mref = mStorageRef.child(email + DateButton.getText().toString().replace("/", "") + ".jpg");
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
            this.finish();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database

    }

    private void calendarDialog(){
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll= (LinearLayout)inflater.inflate(R.layout.calendar, null, false);
        CalendarView cv = (CalendarView) ll.getChildAt(0);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                DateButton.setText(dayOfMonth +" / " + (month+1) + " / " + year);
            }
        });
        new AlertDialog.Builder(NewEvent.this)
                .setTitle("Event Calendar")
                .setMessage("Click to schedule or view events.")
                .setView(ll)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DateButton.setEnabled(true);DateButton.setVisibility(View.VISIBLE);
                        TimeButton.setEnabled(true);TimeButton.setVisibility(View.VISIBLE);
                        LocationButton.setEnabled(true);LocationButton.setVisibility(View.VISIBLE);
                        EventNameButton.setEnabled(true);EventNameButton.setVisibility(View.VISIBLE);
                        SaveButton.setEnabled(true);SaveButton.setVisibility(View.VISIBLE);                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                }
        ).show();
    }
    private void timerDialog(){
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll= (LinearLayout)inflater.inflate(R.layout.time_picker, null, false);
        final TimePicker cv=(TimePicker) ll.getChildAt(0);
        new AlertDialog.Builder(NewEvent.this)
                .setTitle("Event Calendar")
                .setMessage("Click to schedule or view events.")
                .setView(ll)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String a=null;
                        if(cv.getCurrentHour()<10)
                        {
                            a="0"+cv.getCurrentHour().toString();
                        }
                        else
                        {
                            a=cv.getCurrentMinute().toString();
                        }
                        TimeButton.setText(cv.getCurrentHour()+":"+a);
                        DateButton.setEnabled(true);DateButton.setVisibility(View.VISIBLE);
                        TimeButton.setEnabled(true);TimeButton.setVisibility(View.VISIBLE);
                        SaveButton.setEnabled(true);SaveButton.setVisibility(View.VISIBLE);
                        LocationButton.setEnabled(true);LocationButton.setVisibility(View.VISIBLE);
                        EventNameButton.setEnabled(true);EventNameButton.setVisibility(View.VISIBLE);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                }
        ).show();
    }

}
