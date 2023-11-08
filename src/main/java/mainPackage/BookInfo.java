package mainPackage;

import javafx.scene.image.Image;

public class BookInfo {
    public String ISBN;
    public String name;
    public String author;
    public String description;
    public String genre;

    public long price;
    public long quantity;
    public Image image;
    public java.sql.Date pubDate;
    public String language;
    public long willingToPurchaseQuantity;

    public BookInfo ()
    {
        willingToPurchaseQuantity = 0;
    }

    void print() {
        System.out.println("ISBN: " + ISBN);
        System.out.println("Name: " + name);
        System.out.println("Author: " + author);
        System.out.println("Description: " + description);
        System.out.println("Genre: " + genre);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("Cart Quantity: " + willingToPurchaseQuantity);
        System.out.println("Image: " + image);
        System.out.println("Publication Date: " + pubDate);
        System.out.println("Language: " + language);
    }

    public String getGenresOfBook()
    {
        // TODO implement a function that returns the string of all the genre
        //  That matches the book

        return "";
    }
}
