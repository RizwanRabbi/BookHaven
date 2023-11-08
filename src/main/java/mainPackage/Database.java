package mainPackage;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static mainPackage.ConnectDB.getConnection;

public class Database {

    public static String hash(String s)
    {
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < s.length(); i++)
        {
            ret.append((char)(s.charAt(i) ^ 4321));
        }
        return String.valueOf(ret);
    }

    public static
    UserInfo getUserInfo(String email) throws SQLException {
        UserInfo uInfo = new UserInfo();
        Connection conn = getConnection();

        String q = "select * from userinfo where email = ?";
        try {
            PreparedStatement ptsd = conn.prepareStatement(q) ;
            ptsd.setString(1,email);
            ResultSet rs = ptsd.executeQuery();

            while (rs.next()) {
                uInfo.email = rs.getString(1);
                uInfo.fname = rs.getString(2);
                uInfo.lname = rs.getString(3);
                InputStream is = rs.getBinaryStream(4);
                uInfo.image = null;
                if(is != null) uInfo.image = new Image(is);
                uInfo.phoneNo = rs.getString(5);
                uInfo.address = rs.getString(6);
            }
            q = "select pass from users where email = ?";
            ptsd = conn.prepareStatement(q);
            ptsd.setString(1,email);
            rs = ptsd.executeQuery();
            rs.next();
            uInfo.password = rs.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("User was not verified");
        }
        uInfo.print();
        return uInfo;
    }
    public static void updateUserInfo(UserInfo u) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ptsd = conn.prepareStatement("UPDATE USERINFO SET " +
                "FIRSTNAME = ?, " +
                "LASTNAME = ?, " +
                "PHONE = ?, " +
                "ADDRESS = ? " +
                "WHERE EMAIL = ?");

        ptsd.setString(1, u.fname);
        ptsd.setString(2, u.lname);
        ptsd.setString(3, u.phoneNo);
        ptsd.setString(4, u.address);

        ptsd.setString(5, Main.email);

        ptsd.executeUpdate();

        Main.userInfo = Database.getUserInfo(Main.email);
    }

    public static void updateUserInfo(UserInfo u, File image) throws SQLException, FileNotFoundException {
        Connection conn = getConnection();
        PreparedStatement ptsd = conn.prepareStatement("UPDATE USERINFO SET " +
                "FIRSTNAME = ?, " +
                "LASTNAME = ?, " +
                "PHONE = ?, " +
                "ADDRESS = ?, " +
                "DP = ? " +
                "WHERE EMAIL = ?");

        ptsd.setString(1, u.fname);
        ptsd.setString(2, u.lname);
        ptsd.setString(3, u.phoneNo);
        ptsd.setString(4, u.address);
        FileInputStream fin = null;
        if(image != null)
        {
            fin = new FileInputStream(image);
            ptsd.setBinaryStream(5,fin, (int) image.length());
        }
        else {
            ptsd.setBinaryStream(5, null, 0);
        }
        ptsd.setString(6, Main.email);

        ptsd.executeUpdate();

        Main.userInfo = Database.getUserInfo(Main.email);
    }

    public static boolean accountAlreadyExists(String email) {
        Connection conn = getConnection();

        String q = "select * from users where email = ?";
        try {
            PreparedStatement ptsd = conn.prepareStatement(q) ;
            ptsd.setString(1,email);
            ResultSet rs = ptsd.executeQuery();
            if(rs.next()) return true;
            else return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("User was not verified");
        }
        return false;
    }

    public static void registerUser(String fname, String lname, String email, String pass) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users values (?,?)");
        pstmt.setString(1, email);
        pstmt.setString(2, pass);
        pstmt.executeUpdate();
        pstmt = conn.prepareStatement("INSERT INTO userinfo (email,firstname,lastname) values (?,?,?)");
        pstmt.setString(1, email);
        pstmt.setString(2, fname);
        pstmt.setString(3, lname);
        pstmt.executeUpdate();
    }
    public static int loginValidation(String email,String password)
    {
        Connection conn = getConnection();
        try {
            PreparedStatement preparedStatementQ = conn.prepareStatement("SELECT count(email) from users where email = " +
                    "? and pass = ?");
            preparedStatementQ.setString(1, email);
            preparedStatementQ.setString(2, password);
            ResultSet rs = preparedStatementQ.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1) == 0)
                    return 0;
            }
            PreparedStatement ps = conn.prepareStatement("select * from users where email = ?");
            ps.setString(1,email);
            rs = ps.executeQuery();
            while(rs.next())
            {
                return 1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    public static void updatePassword(String pass) throws SQLException
    {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("update users set pass = ? where email = ?");
        pstmt.setString(1,hash(pass));
        pstmt.setString(2,Main.email);
        pstmt.executeUpdate();
    }

    public static ArrayList<BookInfo> getAllBooks() throws SQLException {
        ArrayList<BookInfo> arr = new ArrayList<>();
        Connection conn = getConnection();
        String query = "SELECT * " +
                "FROM ( " +
                "    SELECT * " +
                "    FROM books " +
                "    ORDER BY DBMS_RANDOM.VALUE " +
                ") " +
                "WHERE ROWNUM <= 30";

        PreparedStatement pstmt = conn.prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.ISBN = rs.getString(1);
            bookInfo.name = rs.getString(2);
            bookInfo.author = rs.getString(3);
            bookInfo.description = rs.getString(4);
            bookInfo.genre = rs.getString(5);
            bookInfo.price = rs.getLong(6);
            bookInfo.quantity = rs.getLong(7);
            InputStream is = rs.getBinaryStream(8);
            bookInfo.image = null;
            if(is != null) bookInfo.image = new Image(is);
            bookInfo.pubDate = rs.getDate(9);
            bookInfo.language = rs.getString(10);
            arr.add(bookInfo);
        }
        return arr;
    }
    public static ArrayList<BookInfo> getBooksGivenCartInfo(ArrayList<CartItem> cart) throws SQLException {
        ArrayList<BookInfo> arr = new ArrayList<>();
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select * from books where isbn = ?");
        for (CartItem i: cart)
        {
            pstmt.setString(1, i.ISBN);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                BookInfo bookInfo = new BookInfo();
                bookInfo.ISBN = rs.getString(1);
                bookInfo.name = rs.getString(2);
                bookInfo.author = rs.getString(3);
                bookInfo.description = rs.getString(4);
                bookInfo.genre = rs.getString(5);
                bookInfo.price = rs.getLong(6);
                bookInfo.quantity = rs.getLong(7);
                InputStream is = rs.getBinaryStream(8);
                bookInfo.image = null;
                if(is != null) bookInfo.image = new Image(is);
                bookInfo.pubDate = rs.getDate(9);
                bookInfo.language = rs.getString(10);
                bookInfo.willingToPurchaseQuantity = i.quantity;
                arr.add(bookInfo);
                bookInfo.print();
            }
        }
        return arr;
    }

    public static boolean alreadyInCart(String email, String isbn) throws SQLException {
        Connection conn= getConnection();
        PreparedStatement ptsd= conn.prepareStatement("select * from cart where email = ? and isbn = ?");
        ptsd.setString(1,email);
        ptsd.setString(2,isbn);
        ResultSet rs = ptsd.executeQuery();
        while(rs.next())
        {
            return true;
        }

        return false;
    }

    public static void addToCart(String email, String isbn, int quantity) throws SQLException {
        Connection conn= getConnection();
        PreparedStatement ptsd= conn.prepareStatement("insert into cart (email, isbn, quantity) values (?,?,?)");
        ptsd.setString(1,email);
        ptsd.setString(2,isbn);
        ptsd.setInt(3,quantity);
        try{
            ptsd.executeUpdate();
        }
        catch (SQLException e)
        {
            String errormessage = e.getMessage();
            System.out.println("Error In Add to Cart :" + errormessage);
            e.printStackTrace();
        }
    }
    public static void removeFromCart(String email, String isbn) throws SQLException {
        Connection conn= getConnection();
        PreparedStatement ptsd= conn.prepareStatement("delete from cart where email = ? and isbn = ?");
        ptsd.setString(1,email);
        ptsd.setString(2,isbn);

        try{
            ptsd.executeUpdate();
        }
        catch (SQLException e)
        {
            String errormessage = e.getMessage();
            System.out.println("\n\nError in Delete From Cart :" + errormessage);
            e.printStackTrace();
        }
    }

    public static void updateCart(String email, String isbn, Long quantity) throws SQLException {
        Connection conn= getConnection();
        PreparedStatement ptsd= conn.prepareStatement("update cart set quantity = ? where email = ? and isbn = ?");
        ptsd.setLong(1,quantity);
        ptsd.setString(2,email);
        ptsd.setString(3,isbn);
        try{
            ptsd.executeUpdate();
        }
        catch (SQLException e)
        {
            String errormessage = e.getMessage();
            System.out.println("Error In Updating Cart :" + errormessage);
            e.printStackTrace();
        }
    }
    public static ArrayList<CartItem> getUserCartInfo(String email) throws SQLException {
        Connection conn = getConnection();
        ArrayList<CartItem> cart = new ArrayList<>();
        PreparedStatement ptsd = conn.prepareStatement("select * from cart where email = ?");
        ptsd.setString(1,email);
        ResultSet rs = ptsd.executeQuery();
        while(rs.next())
        {
            CartItem t = new CartItem(rs.getString(2), rs.getInt(1));
            cart.add(t);
        }

        return cart;
    }

    public static ArrayList<BookInfo> getUserCartBooks(String email) throws SQLException {
        ArrayList<BookInfo> books = new ArrayList<>();

        Connection conn = getConnection();
        PreparedStatement ptsd = conn.prepareStatement("select " +
                "name, " +
                "author, " +
                "price, " +
                "cover, " +
                "books.quantity, " +
                "cart.quantity, " +
                "books.isbn " +
                "from books, cart where books.isbn = cart.isbn and cart.email = ?");
        ptsd.setString(1,email);
        ResultSet rs = ptsd.executeQuery();

        while (rs.next()) {
            books.add(getBookInfoFromRS(rs));
        }

        return books;
    }
    public static int getQuantityFromCart(String email, String isbn) throws SQLException {

        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select quantity from cart where email = ? and isbn = ?");
        pstmt.setString(1,email);
        pstmt.setString(2,isbn);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next())
            return rs.getInt(1);

        return 1;
    }
    public static int numberOfItemsInCart(String email) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("select count(isbn) from cart where email = ?");
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    private static BookInfo getBookInfoFromRS(ResultSet rs) throws SQLException {
        BookInfo bookInfo = new BookInfo();
        bookInfo.ISBN = rs.getString(1);
        bookInfo.name = rs.getString(2);
        bookInfo.author = rs.getString(3);
        bookInfo.description = rs.getString(4);
        bookInfo.genre = rs.getString(5);
        bookInfo.price = rs.getLong(6);
        bookInfo.quantity = rs.getLong(7);
        InputStream is = rs.getBinaryStream(8);
        bookInfo.image = null;
        if(is != null) bookInfo.image = new Image(is);
        bookInfo.pubDate = rs.getDate(9);
        bookInfo.language = rs.getString(10);
        return bookInfo;
    }
    public static ArrayList<BookInfo> searchBooks(ArrayList<String> a) throws SQLException {
        Set<BookInfo> st = new HashSet<>();
        Connection conn = getConnection();
        // Search by name, author, genre
        PreparedStatement pstmt = conn.prepareStatement("select * from books where LOWER(name) like ? or LOWER(author) like ?");
        ResultSet rs;
        for(String word: a) {
            pstmt.setString(1, "%" + word + "%");
            pstmt.setString(2, "%" + word + "%");
//            pstmt.setString(4, "%" + word + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                st.add(getBookInfoFromRS(rs));
            }
        }
        ArrayList<BookInfo> bookInfos = new ArrayList<>();
        HashMap<String, Boolean> mp = new HashMap<String, Boolean>();
        for(BookInfo b: st) {
            if(mp.get(b.ISBN) != null) continue;
            mp.put(b.ISBN, true);
            bookInfos.add(b);
        }
        System.out.println(mp);
        return bookInfos;
    }
    public static ArrayList<BookInfo> searchBooksByWordSequence(ArrayList<String> a) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement("select * from books where LOWER(name) like ? or LOWER(author) like ?");
        String src = "%";
        for(String words: a) {
            src += words + "%";
        }
        preparedStatement.setString(1, src);
        preparedStatement.setString(2, src);
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<BookInfo> bookInfos = new ArrayList<>();
        while (rs.next()) {
            bookInfos.add(getBookInfoFromRS(rs));
        }
        return bookInfos;
    }


    public static boolean orderExists(int x) throws SQLException {
        Connection conn = ConnectDB.getConnection();

        PreparedStatement ptsd = conn.prepareStatement("Select * from orders where order_id = ?");
        ptsd.setInt(1,x);
        ResultSet rs = ptsd.executeQuery();
        while(rs.next())
            return true;

        return  false;
    }

    public static int generateOrderID() throws SQLException {

        Random random = new Random();
        int x = random.nextInt(Integer.MAX_VALUE);

        while(Database.orderExists(x))
        {
            x = random.nextInt(Integer.MAX_VALUE);
        }

        return x;
    }



    public static void insertOrder(OrderInfo orderInfo) {
        try (Connection connection = getConnection())
        {
            String insertQuery = "INSERT INTO orders ( " +
                    "Order_ID, " +
                    "Name, " +
                    "Email, " +
                    "Mobile_No, " +
                    "orderDate, " +
                    "Status, " +
                    "Address, " +
                    "Shipping_costs, " +
                    "Total_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, orderInfo.orderID);
            preparedStatement.setString(2, orderInfo.name);
            preparedStatement.setString(3, orderInfo.email);
            preparedStatement.setString(4, orderInfo.phoneNo);
            Timestamp today = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(5, today);
            preparedStatement.setInt(6, orderInfo.status);
            preparedStatement.setString(7, orderInfo.address);
            preparedStatement.setInt(8, orderInfo.shippingCosts);
            preparedStatement.setInt(9, orderInfo.totalAmount);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new order has been inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<OrderInfo> getOrdersByUserEmail(String userEmail) {
        ArrayList<OrderInfo> orders = new ArrayList<>();
        System.out.println("Fetching orders for "+ userEmail);
        try (Connection connection = getConnection()) {
            // Modify the SQL query to order by date in descending order
            String selectQuery = "SELECT * FROM orders WHERE Email = ? ORDER BY orderDate DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, userEmail);

            ResultSet resultSet = preparedStatement.executeQuery();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            while (resultSet.next()) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.orderID = resultSet.getInt("Order_ID");
                orderInfo.name = resultSet.getString("Name");
                orderInfo.email = resultSet.getString("Email");
                orderInfo.phoneNo = resultSet.getString("Mobile_No");

                // Format the orderDate field to "dd/MM/yyyy"
                java.sql.Date sqlDate = resultSet.getDate("orderDate");
                orderInfo.dateString = dateFormat.format(sqlDate);

                orderInfo.status = resultSet.getInt("Status");
                orderInfo.address = resultSet.getString("Address");
                orderInfo.shippingCosts = resultSet.getInt("Shipping_costs");
                orderInfo.totalAmount = resultSet.getInt("Total_amount");
                System.out.println("Order Found");
                orderInfo.print();
                orders.add(orderInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void insertOrderBooks(ArrayList<BookInfo> books, int orderID) throws SQLException {

        Connection conn = ConnectDB.getConnection();
        PreparedStatement ptsd = conn.prepareStatement("Insert into OrderBooks  (ORDER_ID ,ISBN ,UNIT_PRICE, QUANTITY) values (?,?,?,?)");
        ptsd.setInt(1, orderID);

        for (int i=0; i<books.size();i++ )
        {
            ptsd.setString(2, books.get(i).ISBN);
            ptsd.setInt(3, (int) books.get(i).price);
            ptsd.setInt(4, (int) books.get(i).willingToPurchaseQuantity);
            ptsd.executeUpdate();
        }
    }
    public static ArrayList<BookInfo> getOrderedBooks(int orderID) throws SQLException {
        Connection conn = ConnectDB.getConnection();
        PreparedStatement ptsd = conn.prepareStatement(
                "SELECT " +
                        "books.name, " +
                        "books.author, " +
                        "books.price, " +
                        "books.cover, " +
                        "books.quantity AS book_quantity, " +
                        "orderbooks.quantity AS cart_quantity," +
                        "books.isbn " +
                        "FROM books " +
                        "INNER JOIN orderbooks ON books.isbn = orderbooks.isbn " +
                        "WHERE orderbooks.order_id = ?");
        ptsd.setInt(1, orderID);

        ResultSet rs = ptsd.executeQuery();
        ArrayList<BookInfo> orderedBooks = new ArrayList<>();

        while(rs.next())
        {
            BookInfo b = new BookInfo();
            b.name = rs.getString(1);
            b.author = rs.getString(2);
            b.price = rs.getLong(3);
            InputStream is = rs.getBinaryStream(4);
            b.image = null;
            if(is != null) b.image = new Image(is);
            b.quantity = rs.getLong(5);
            b.willingToPurchaseQuantity = rs.getLong(6);
            b.ISBN = rs.getString(7);

            orderedBooks.add(b);
        }

        return orderedBooks;
    }


    public static void deleteUserCart() throws SQLException {
        Connection conn = ConnectDB.getConnection();
        PreparedStatement ptsd = conn.prepareStatement("DELETE FROM CART WHERE EMAIL = ?");
        ptsd.setString(1, Main.email);
        ptsd.executeUpdate();
    }

    public static OrderInfo searchOrder (int orderID, String phoneNo) throws SQLException {
        OrderInfo o = null;

        Connection c = ConnectDB.getConnection();
        PreparedStatement ptsd = c.prepareStatement("SELECT * FROM ORDERS WHERE ORDER_ID = ? AND MOBILE_NO = ?");
        ptsd.setInt(1, orderID);
        ptsd.setString(2, phoneNo);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ResultSet rs = ptsd.executeQuery();
        while (rs.next())
        {
            o = new OrderInfo();
            o.orderID = rs.getInt("Order_ID");
            o.name = rs.getString("Name");
            o.email = rs.getString("Email");
            o.phoneNo = rs.getString("Mobile_No");

            // Format the orderDate field to "dd/MM/yyyy"
            java.sql.Date sqlDate = rs.getDate("orderDate");
            o.dateString = dateFormat.format(sqlDate);

            o.status = rs.getInt("Status");
            o.address = rs.getString("Address");
            o.shippingCosts = rs.getInt("Shipping_costs");
            o.totalAmount = rs.getInt("Total_amount");
            System.out.println("Order Found");
            o.print();
        }
        return o;
    }

    public static BookInfo getBookGivenISBN(String isbn) throws SQLException {
        Connection conn = ConnectDB.getConnection();
        BookInfo b = null ;
        PreparedStatement ptsd = conn.prepareStatement( "SELECT * FROM books where ISBN = ?");
        ptsd.setString(1, isbn);
        ResultSet rs = ptsd.executeQuery();
        while (rs.next())
            b = getBookInfoFromRS(rs);
        return b;
    }

}

