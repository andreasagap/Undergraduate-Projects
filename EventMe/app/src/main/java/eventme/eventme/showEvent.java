package eventme.eventme;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;


public class showEvent extends AppCompatActivity implements OnMapReadyCallback {

    private Button date,time,EventName;
    private TextView description;
    private ImageView image;
    private String temp, location;
    private TextView text;
    private ScrollView mainScrollView;
    private MapFragment mapFragment;
    private ImageView transparentImageView;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Intent mIntent = getIntent();

        description=(TextView) findViewById(R.id.Description);

        date = (Button) findViewById(R.id.date);

        // location = (Button) findViewById(R.id.location);

        EventName = (Button) findViewById(R.id.ename);
        EventName.setText(mIntent.getStringExtra("Name"));

        date.setText(mIntent.getStringExtra("Date")+"  "+ mIntent.getStringExtra("Time"));

        location=mIntent.getStringExtra("Location");

        description.setMovementMethod(new ScrollingMovementMethod());   //description scrolls down (shows 4lines)

        description.setText(mIntent.getStringExtra("Description"));


        text = (TextView) findViewById(R.id.name);
        temp =mIntent.getStringExtra("Email");
        takeUserName(); //sets username in textview under image

        image = (ImageView) findViewById(R.id.imageView);


        String tempForImageRetreival=mIntent.getStringExtra("Date");

        retrieveImage(temp,tempForImageRetreival);
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);


        //for the map
        geocoder=new Geocoder(this);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mainScrollView = (ScrollView) findViewById(R.id.scroll);
        transparentImageView = (ImageView) findViewById(R.id.transparent_image);
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {super.onDestroy();}

    private void retrieveImage(String email,String date)
    {
        StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = mStorageRef.child(email+date.replace("/","")+".jpg");

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(islandRef)
                .into(image);

    }

    public void goToShop(View v)
    {
        Intent intent = new Intent(showEvent.this, Profile.class);
        intent.putExtra("email",temp);
        intent.putExtra("yourprofile",false);
        startActivity(intent);
    }

    private void takeUserName(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User a= ds.getValue(User.class);
                    if(a.getEmail().equals(temp.replace(".",",")))
                    {
                        text.setText(a.getUsername());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    @Override
    public void onMapReady(GoogleMap map)  {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        List<Address> list= null;

        try {
            list = geocoder.getFromLocationName(location,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address=list.get(0);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(address.getLatitude(),address.getLongitude()))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
        map.addMarker(new MarkerOptions()
                .position(new LatLng(address.getLatitude(),address.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));

        map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

    }
}
