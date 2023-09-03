package mainPackage;

public class UserInfo {
    public String fname;
    public String lname;
    public String email;
    public String password;

    public UserInfo(String fname, String lname, String email, String password) {

        this.lname = lname;
        this.fname = fname;
        this.email = email;
        this.password = password;

        // More work need to be done here to adjust user preferences when we create a user.

    }

    public UserInfo() {

    }
}

//