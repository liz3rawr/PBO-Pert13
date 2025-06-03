package com.example.prak13;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> colUser;
    @FXML
    private TableColumn<User, String> colPass;
    @FXML
    private TextField user;
    @FXML
    private TextField pass;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    public void initialize(){
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPass.setCellValueFactory(new PropertyValueFactory<>("password"));
        centerAlignColumn(colUser);
        centerAlignColumn(colPass);
        loadData();
    }

    public void loadData(){
        userList.clear();
        try (Connection con = Database.getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")));
            }
            table.setItems(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(){
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)");
            ps.setString(1, user.getText());
            ps.setString(2, pass.getText());
            ps.executeUpdate();
            loadData();
            user.clear();
            pass.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User added successfully.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add user.");
            alert.showAndWait();
        }
    }

    public void update(){
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected!=null) {
            try (Connection con = Database.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE users SET username=?, password=? WHERE id=?");
                ps.setString(1, user.getText());
                ps.setString(2, pass.getText());
                ps.setInt(3, selected.getId());
                ps.executeUpdate();
                loadData();
                user.clear();
                pass.clear();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User updated successfully.");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update user.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to update.");
            alert.showAndWait();
        }
    }

    public void delete() {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected!=null) {
            try (Connection con = Database.getConnection()) {
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM users WHERE id=?");
                ps.setInt(1,selected.getId());
                ps.executeUpdate();
                loadData();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User deleted successfully.");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete user.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }

    private <T> void centerAlignColumn(TableColumn<User, T> column) {
        column.setCellFactory(col -> {
            return new TableCell<User, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            };
        });
    }
}
