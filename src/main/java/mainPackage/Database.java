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
                uInfo.setEmail(rs.getString(1));
                uInfo.setPassword(rs.getString(2));
                uInfo.setName(rs.getString(3));

            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("User was not verified");
        }
        System.out.println(uInfo.getEmail()+" " +
                uInfo.getPassword() + " " +
                uInfo.getName());

        return uInfo;
    }
}

