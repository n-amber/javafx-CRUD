package dao;

import data.ConnectDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private ObservableList<Customer> list;
    private

    Connection conn;
    PreparedStatement preparedStatement;

    public static CustomerDAO getInstance() {

        return new CustomerDAO();
    }

    public boolean save(Customer customer) throws SQLException {
        int result = 0;

        conn = ConnectDB.getConnection();
        String query = "INSERT INTO customer(fullname,username, email, gender, birthday,id_customer ) VALUES (?,?,?,?,?,?)";

        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, customer.getFullname());
            preparedStatement.setString(2, customer.getUsername());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getGender());
            preparedStatement.setDate(5, customer.getBirthday());
            preparedStatement.setString(6, customer.getId_customer());

            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }

        return result > 0;

    }

    public boolean update(Customer customer) throws SQLException {
        int result = 0;

        try {
            conn = ConnectDB.getConnection();
            String query = "UPDATE customer SET " +
                    "fullname = ?" +
                    ", username= ?" +
                    ", email= ? " +
                    ", gender = ? " +
                    ", birthday = ?" +
                    ", status = ?" +
                    " WHERE id_customer = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, customer.getFullname());
            preparedStatement.setString(2, customer.getUsername());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getGender());
            preparedStatement.setDate(5, customer.getBirthday());
            preparedStatement.setBoolean(6, customer.isStatus());
            preparedStatement.setString(7, customer.getId_customer());

            result = preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }

        return result > 0;
    }

    public boolean updateStatus(String id_cus, boolean isStatus) throws SQLException {
        int result = 0;

        try {
            conn = ConnectDB.getConnection();
            String query = "UPDATE customer SET " +
                    " status = ?" +
                    " WHERE id_customer = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, isStatus);
            preparedStatement.setString(2, id_cus);

            result = preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }

        return result > 0;
    }




//    public List<Customer> customerList() throws SQLException {
//        list = FXCollections.observableArrayList();
//
//        conn = ConnectDB.getConnection();
//        String query = "select * from customer";
//        try {
//            preparedStatement = conn.prepareStatement(query);
//
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                Customer customer = new Customer(rs.getInt("id")
//                        , rs.getString("id_customer")
//                        , rs.getString("fullname")
//                        , rs.getString("username")
//                        , rs.getString("email")
//                        , rs.getString("gender")
//                        , rs.getDate("birthday")
//                        , rs.getBoolean("status"));
//                list.add(customer);
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (preparedStatement != null) preparedStatement.close();
//            if (conn != null) conn.close();
//        }
//
//        return list;
//    }


    public ObservableList<Customer> customerList() throws SQLException {
        list = FXCollections.observableArrayList();

        conn = ConnectDB.getConnection();
        String query = "select * from customer";
        try {
            preparedStatement = conn.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("id")
                        , rs.getString("id_customer")
                        , rs.getString("fullname")
                        , rs.getString("username")
                        , rs.getString("email")
                        , rs.getString("gender")
                        , rs.getDate("birthday")
                        , rs.getBoolean("status"));
                list.add(customer);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }

        return list;
    }


    public ObservableList<Customer> CustomerDeletedList() throws SQLException {
        list = FXCollections.observableArrayList();

        conn = ConnectDB.getConnection();
        String query = "select * from deleted_customer";
        try {
            preparedStatement = conn.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("id")
                        , rs.getString("id_customer")
                        , rs.getString("fullname")
                        , rs.getString("username")
                        , rs.getString("email")
                        , rs.getString("gender")
                        , rs.getDate("birthday")
                        , rs.getBoolean("status"));
                list.add(customer);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (conn != null) conn.close();
        }

        return list;
    }

//    public boolean saveToDeletedList(Customer customer) throws SQLException {
//        int result = 0;
//
//        conn = ConnectDB.getConnection();
//        String query = "INSERT INTO deleted_customer(fullname,username, email, gender, birthday,id_customer ) VALUES (?,?,?,?,?,?)";
//
//        try {
//            preparedStatement = conn.prepareStatement(query);
//            preparedStatement.setString(1, customer.getFullname());
//            preparedStatement.setString(2, customer.getUsername());
//            preparedStatement.setString(3, customer.getEmail());
//            preparedStatement.setString(4, customer.getGender());
//            preparedStatement.setDate(5, customer.getBirthday());
//            preparedStatement.setString(6, customer.getId_customer());
//
//            result = preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (preparedStatement != null) preparedStatement.close();
//            if (conn != null) conn.close();
//        }
//
//        return result > 0;
//
//    }

}
