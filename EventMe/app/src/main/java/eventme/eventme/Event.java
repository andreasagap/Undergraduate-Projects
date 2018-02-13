package eventme.eventme;




/**
 * Created by andreas agapitos on 11-Jan-17.
 */

public class Event {
    private String name,email,date,time,location,description;
    public Event()
    {}
    public Event(String e,String d, String t, String l, String de,String n) //Constructor
    {

        date=d;
        time=t;
        location=l;
        description=de;
        email=e;
        name=n;
    }

    public String getemail()
    {
        return email;
    }
    public String getDate()
    {
        return date;
    }
    public String getLocation()
    {
        return location;
    }
    public String getDescription()
    {
        return description;
    }
    public String getTime()
    {
        return time;
    }
    public String getName(){return name;}
}

