package eventme.eventme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;


public class Homepage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private StorageReference mStorageRef;
    private ListView list;
    private ArrayList<Event> events=new ArrayList<>();
    private ArrayList<StorageReference> imageUrl=new ArrayList<>();
    private SharedPreferences preferences;
    private SwipeRefreshLayout nswipe;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        if(isOnline())
        {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            email = preferences.getString("email", "");

            nswipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh); //refresh while scrolling down
            nswipe.setOnRefreshListener(this);

            mStorageRef = FirebaseStorage.getInstance().getReference();
            mDrawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
            mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
            mDrawerLayout.addDrawerListener(mToggle);
            mToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(MenuItem item)
                {

                    mDrawerLayout.closeDrawers();
                    if (item.getItemId() == R.id.nav_login)
                    {
                        Intent intent = new Intent(Homepage.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else if(item.getItemId() == R.id.nav_logout)
                    {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email","");
                        editor.apply();
                        navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
                        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);

                    }
                    else if(item.getItemId()==R.id.nav_profile)
                    {
                        Intent intent = new Intent(Homepage.this, Profile.class);
                        intent.putExtra("email", email);
                        intent.putExtra("yourprofile",true);
                        startActivity(intent);
                    }
                    return true;
                }
            });
            if(!email.equals(""))
            {
                navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
            }
            updateList();

            list=(ListView)findViewById(R.id.list);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent intent = new Intent(Homepage.this, showEvent.class);
                    intent.putExtra("Name", events.get(position).getName());
                    intent.putExtra("Date", events.get(position).getDate());
                    intent.putExtra("Time", events.get(position).getTime());
                    intent.putExtra("Location", events.get(position).getLocation());
                    intent.putExtra("Description", events.get(position).getDescription());
                    intent.putExtra("Email", events.get(position).getemail());
                    startActivity(intent);
                }

            });
        }
        else
        {
            checkNetworkConnection();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database


    }
    private void retrieveData(DataSnapshot dataSnapshot)
    {
        events.clear();
        imageUrl.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Event e=ds.getValue(Event.class);
            events.add(e);


        }

        events = sortListView(events);

        for(int i=0;i<events.size();i++)
        {

            imageUrl.add(mStorageRef.child(events.get(i).getemail()+events.get(i).getDate().replace("/","")+".jpg"));
        }
        CustomList adapter = new CustomList(Homepage.this,events,imageUrl);
        list.setAdapter(adapter);
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){

            return false;
        }
        return true;
    }
    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override           //method for refresh
    public void onRefresh() {
        updateList();
        nswipe.setRefreshing(false);
    }

    private void updateList(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference().child("Event");

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

                currentdate1 = (e1xronia+"-"+e1minas+"-"+e1imera).replace(" ","");

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

                currentdate2 = (e2xronia+"-"+e2minas+"-"+e2imera).replace(" ","") ;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d1=new Date(),d2=new Date();
                try {
                    d1 = sdf.parse(currentdate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    d2 = sdf.parse(currentdate2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (d1.after(d2)) {
                    return 1;
                }

                if (d1.before(d2)) {
                    return -1;
                }

                if (d1.equals(d2)) {
                    return 0;
                }
                return  0;

            }

        });

        return list ;

    }


}

