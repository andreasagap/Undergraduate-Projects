package eventme.eventme;

/**
 * Created by Γιώργος on 30/6/2017.
 */

public class User {
    private String username,email,password;
    public User(){}
    public User(String un,String e,String pass){
        username=un;
        email=e;
        password=pass;

    }
    public String getUsername(){
        return username;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }



}
