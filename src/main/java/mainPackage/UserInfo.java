package mainPackage;

import javafx.scene.image.Image;

public class UserInfo {
    public String fname;
    public String lname;
    public String email;
    public String password;
    public String phoneNo;
    public String address;

    public Image image;

    public UserInfo(String fname, String lname, String email, String password) {

        this.lname = lname;
        this.fname = fname;
        this.email = email;
        this.password = password;

        // More work need to be done here to adjust user preferences when we create a user.

    }

    public UserInfo() {

    }

    void print()
    {
        System.out.println(email);
        System.out.println(fname + " "+ lname);
        System.out.println(phoneNo);
        System.out.println(address);
        if(image != null)
            System.out.println("Image was found");
    }
}

//