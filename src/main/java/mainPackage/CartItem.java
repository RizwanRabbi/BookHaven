package mainPackage;

import javafx.beans.binding.ListBinding;

import java.sql.SQLException;

public class CartItem {
    String ISBN;
    int quantity;

    CartItem (String isbn, int Quantity)
    {
        ISBN = isbn;
        quantity = Quantity;
    }

    void addToUserCart(String email) throws SQLException {
        if(!Database.alreadyInCart(email,ISBN))
        {
            Database.addToCart(email, ISBN, quantity);
        }
    }
    void print()
    {
        System.out.println(ISBN+quantity);
    }


}
