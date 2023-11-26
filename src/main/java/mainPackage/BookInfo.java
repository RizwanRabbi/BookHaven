package mainPackage;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.StringJoiner;

public class BookInfo {
    public String ISBN;
    public String name;
    public String author;
    public String description;
    public String genreBitStr;

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
        System.out.println("Genre: " + genreBitStr);
        System.out.println("Price: " + price);
        System.out.println("Quantity: " + quantity);
        System.out.println("Cart Quantity: " + willingToPurchaseQuantity);
        System.out.println("Image: " + image);
        System.out.println("Publication Date: " + pubDate);
        System.out.println("Language: " + language);
    }

    public String getGenresOfBook()
    {
        System.out.println("Retrieving genres -> ");
        System.out.println("Bit String : " +genreBitStr);

        ArrayList<String> genreList = Database.getGenre(genreBitStr);

        // Using StringJoiner for better string concatenation
        StringJoiner finalStrJoiner = new StringJoiner(", ");

        for (String genre : genreList) {
            finalStrJoiner.add(genre);
        }

        String finalStr = finalStrJoiner.toString();
        System.out.println("Retrieved Genres" +finalStr);

        return finalStr;
    }
}
