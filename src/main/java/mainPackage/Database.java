package mainPackage;

import java.sql.*;

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
        Connection conn = ConnectDB.getConnection();

        String q = "select * from users where email = ?";
        try {
            PreparedStatement ptsd = conn.prepareStatement(q) ;
            ptsd.setString(1,email);
            ResultSet rs = ptsd.executeQuery();

            while (rs.next()) {
                uInfo.email = rs.getString(1);
                uInfo.fname = rs.getString(2);
                uInfo.lname = rs.getString(2);

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
        return uInfo;
    }

    public static boolean accountAlreadyExists(String email) {
        Connection conn = ConnectDB.getConnection();

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
        Connection conn = ConnectDB.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users values (?,?)");
        pstmt.setString(1, email);
        pstmt.setString(2, pass);
        pstmt.executeUpdate();
        pstmt = conn.prepareStatement("INSERT INTO userinfo values (?,?,?)");
        pstmt.setString(1, email);
        pstmt.setString(2, fname);
        pstmt.setString(3, lname);
        pstmt.executeUpdate();
    }
    public static int loginValidation(String email,String password)
    {
        Connection conn = ConnectDB.getConnection();
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
}

