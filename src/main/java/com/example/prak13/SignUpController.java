package com.example.prak13;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField confirm;
    @FXML
    private Button signUp;

    public void actionSignUp(){
        if (!pass.getText().equals(confirm.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password Mismatch");
            alert.setHeaderText(null);
            alert.setContentText("Password and Confirm Password do not match.");
            alert.showAndWait();
            return;
        }

        try (Connection con = Database.getConnection()){
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)");
            ps.setString(1,user.getText());
            ps.setString(2, pass.getText());
            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign Up Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your account has been created!");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) signUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign Up Failed");
            alert.setHeaderText(null);
            alert.setContentText("Username already exists. Please choose another.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
