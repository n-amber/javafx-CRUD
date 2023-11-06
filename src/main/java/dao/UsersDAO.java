package dao;

import data.ConnectDB;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {

    public static UsersDAO getInstance(){
        return new UsersDAO();
    }

    public User checkUser (String name) throws SQLException {

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String username =null;
        String password = null;

        try {
            conn = ConnectDB.getConnection();
            String query = "select * from users where username = ? ";

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,name);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                username = rs.getString("username");
                password = rs.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null) preparedStatement.close();
            if (conn != null) conn.close();
        }

        return new User(username,password);
    }

    public boolean save(User user) throws SQLException {
        int result = 0;
        Connection  conn= null;
        PreparedStatement preparedStatement = null;

        try {
            conn = ConnectDB.getConnection();
            String query = "INSERT INTO users(username, password,email) VALUES (?,?,?)";
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }
        return result >0;


    }

    




}
