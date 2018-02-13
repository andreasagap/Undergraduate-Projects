package eventme.eventme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Profile extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView name;
    private TextView email;
    private String email2,username,password;
    private ImageView buttonUploadImage ;
    private ListView list;
    private boolean yourprofile;
    private SwipeRefreshLayout nswipe;
    private StorageReference mStorageRef;
    private ArrayList<Event> events=new ArrayList<>();
    private ArrayList<StorageReference> imageUrl=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        buttonUploadImage = (ImageView) findViewById(R.id.photo_magaziou);
        nswipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh); //refresh while scrolling down
        nswipe.setOnRefreshListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Informations");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Informations");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("Events");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Events");
        host.addTab(spec);
        list=(ListView)findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(Profile.this, showEvent.class);
                intent.putExtra("Name", events.get(position).getName());
                intent.putExtra("Date", events.get(position).getDate());
                intent.putExtra("Time", events.get(position).getTime());
                intent.putExtra("Location", events.get(position).getLocation());
                intent.putExtra("Description", events.get(position).getDescription());
                intent.putExtra("Email", events.get(position).getemail());
                startActivity(intent);
            }

        });
        name = (TextView) findViewById(R.id.onoma);
        updateList();
        email = (TextView) findViewById(R.id.email_user);
        email.setText( "Email: " + email2);
    }
    @Override           //method for refresh
    public void onRefresh() {
        updateList();
        nswipe.setRefreshing(false);
    }
    private void retrieveData(DataSnapshot dataSnapshot)
    {
        events.clear();
        imageUrl.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Event e=ds.getValue(Event.class);
            if(e.getemail().equals(email2))
            {
                events.add(e);
            }
        }

        events = sortListView(events);

        for(int i=0;i<events.size();i++)
        {

            imageUrl.add(mStorageRef.child(events.get(i).getemail()+events.get(i).getDate().replace("/","")+".jpg"));

        }
        CustomList adapter = new CustomList(Profile.this,events,imageUrl);
        list.setAdapter(adapter);
    }
    private void updateList(){
        Intent intent=getIntent();
        email2 = intent.getStringExtra("email");
        yourprofile=intent.getBooleanExtra("yourprofile",true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User a= ds.getValue(User.class);
                    if(a.getEmail().equals(email2.replace(".",",")))
                    {
                        username = a.getUsername();
                        password=a.getPassword();
                        name.setText("Username: " + a.getUsername());

                    }
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref = database.getReference().child("Event");

        ref.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retrieveData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();
        Log.i("TAGGGG",email2);
        StorageReference islandRef = mStorageRef.child(email2+".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(islandRef)
                .into(buttonUploadImage);
        buttonUploadImage.setAdjustViewBounds(true);
    }

    private ArrayList<Event> sortListView(ArrayList<Event> list)
    {
        Collections.sort(list, new Comparator<Event>()
        {
            @Override
            public int compare(Event e1, Event e2)
            {
                String date1 = e1.getDate();
                String date2 = e2.getDate();
                String e1imera = null , e1minas = null , e1xronia = null , e2imera = null , e2minas = null , e2xronia = null ;

                String currentdate1 , currentdate2 ;

                int temp1 = 0 ;
                StringTokenizer tokenizer = new StringTokenizer(date1, "/");
                while(tokenizer.hasMoreTokens())
                {
                    if(temp1 == 0)
                    {
                        e1imera = tokenizer.nextToken();
                        temp1 ++ ;
                    }
                    if(temp1 == 1)
                    {
                        e1minas = tokenizer.nextToken();
                        temp1++ ;
                    }
                    else
                    {
                        e1xronia = tokenizer.nextToken();
                    }
                }

                currentdate1 = e1xronia+e1minas+e1imera ;

                int temp2 = 0 ;
                StringTokenizer tokenizer2 = new StringTokenizer(date2, "/");
                while(tokenizer2.hasMoreTokens())
                {
                    if(temp2 == 0)
                    {
                        e2imera = tokenizer2.nextToken();
                        temp2 ++ ;
                    }
                    if(temp2 == 1)
                    {
                        e2minas = tokenizer2.nextToken();
                        temp2++ ;
                    }
                    else
                    {
                        e2xronia = tokenizer2.nextToken();
                    }
                }

                currentdate2 = e2xronia+e2minas+e2imera ;

                return  currentdate1.compareTo(currentdate2);

            }

        });

        return list ;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(yourprofile)
        {
            getMenuInflater().inflate(R.menu.threedots,menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.ab)
        {
            Intent intent = new Intent(Profile.this, NewEvent.class);
            startActivity(intent);
            return true ;
        }

        if(id == R.id.ex)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", "");
            editor.apply();
            Intent intent = new Intent(this,Homepage.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.edp)
        {
            Intent intent = new Intent(Profile.this, profileEdit.class);

            intent.putExtra("stringname",username);
            intent.putExtra("stringemail",email2);
            intent.putExtra("password",password);
            startActivity(intent);
            return true ;
        }

        return true;
    }


}
