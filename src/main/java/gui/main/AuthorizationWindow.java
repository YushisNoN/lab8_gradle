package gui.main;

import client.Client;
import gui.AbstractWindow;
import gui.program.MainWindow;
import gui.utils.LanguageSelector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;
import transfer.Request;
import transfer.Response;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthorizationWindow extends Application {
    private ResourceBundle bundle;
    private final static short WIDTH = 800;
    private final static short HEIGHT = 600;
    private Client client;
    private Stage stage = new Stage();
    private Stage prevStage = null;
    private Text welcomeLabel;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Text usernameText;
    private Text passwordText;
    private Label usernameError;
    private Label passwordError;
    private Hyperlink registerLink;
    private Button signIn;
    @Override
    public void start(Stage stage) {
        this.stage=stage;
        Locale.setDefault(new Locale("ru", "RU"));
        this.bundle = ResourceBundle.getBundle("locale");
        client = new Client("localhost", 2205, false);
        try {
            client.open();
        } catch (IOException e) {
            showErrorAlert(bundle.getString("connectionError"), bundle.getString("connectionErrorMsg"));
            return;
        }
        welcomeLabel = new Text(bundle.getString("welcomeMessage"));
        welcomeLabel.getStyleClass().add("welcomeMessage");
        welcomeLabel.setX(WIDTH / 2 - welcomeLabel.getLayoutBounds().getWidth() / 2 - 200);
        welcomeLabel.setY(HEIGHT / 2 - 90);

        usernameField = new TextField();
        usernameField.getStyleClass().add("usernameField");
        usernameField.setPromptText(bundle.getString("enterUsername"));
        usernameField.setFocusTraversable(false);
        usernameField.setMinWidth(180);
        usernameField.setLayoutX(usernameField.getLayoutBounds().getWidth() + 145);
        usernameField.setLayoutY(usernameField.getLayoutBounds().getHeight() + 245);

        passwordField.getStyleClass().add("usernameField");
        passwordField.setPromptText(bundle.getString("enterPassword"));
        passwordField.setFocusTraversable(false);
        passwordField.setMinWidth(180);
        passwordField.setLayoutX(usernameField.getLayoutBounds().getWidth() + 145);
        passwordField.setLayoutY(usernameField.getLayoutBounds().getHeight() + 310);

        usernameText = new Text(bundle.getString("loginText"));
        passwordText = new Text(bundle.getString("passwordText"));
        usernameText.setLayoutX(WIDTH / 2 - usernameText.getLayoutBounds().getWidth() / 2 - 235);
        usernameText.setLayoutY(HEIGHT / 2 - 60);
        passwordText.setLayoutX(WIDTH / 2 - passwordText.getLayoutBounds().getWidth() / 2 - 235);
        passwordText.setLayoutY(HEIGHT / 2 + 5);


        usernameError = new Label(bundle.getString("usernameError"));
        usernameError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");

        passwordError = new Label(bundle.getString("passwordError"));
        passwordError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        usernameError.setVisible(false);
        usernameError.setLayoutX(WIDTH / 2 - usernameError.getLayoutBounds().getWidth() / 2 - 255);
        usernameError.setLayoutY(HEIGHT / 2 - 25);

        passwordError.setVisible(false);
        passwordError.setLayoutX(WIDTH / 2 - passwordError.getLayoutBounds().getWidth() / 2 - 255);
        passwordError.setLayoutY(HEIGHT / 2 + 40);

        registerLink = new Hyperlink(bundle.getString("registerLink"));
        registerLink.setLayoutX(WIDTH / 2 - registerLink.getLayoutBounds().getWidth() / 2 - 235);
        registerLink.setLayoutY(HEIGHT / 2 + 95);

        signIn = new Button(bundle.getString("signIn"));
        signIn.setFocusTraversable(true);
        signIn.setOnMouseEntered(event -> signIn.setCursor(Cursor.HAND));
        signIn.getStyleClass().add("signIn");
        signIn.setMinWidth(180);
        signIn.setLayoutX(signIn.getLayoutBounds().getWidth() + 145);
        signIn.setLayoutY(signIn.getLayoutBounds().getHeight() + 360);
        signIn.setOnAction(event -> handleLogin(usernameField, passwordField, usernameError, passwordError));

        registerLink.setOnAction(event -> {
            RegistrationWindow registrationWindow = new RegistrationWindow(client, stage);
            registrationWindow.show();
        });


        Image image = new Image(getClass().getResourceAsStream("/img/bg_main.jpeg"));
        ImageView imageView = new ImageView(image);
        imageView.setX(WIDTH / 2);

        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH/2);

        BorderPane root = new BorderPane();
        ComboBox<String> languageCombo = LanguageSelector.createLanguageComboBox(this::updateLocalization);
        languageCombo.getStyleClass().add("unitOfMeasure");
        BorderPane.setAlignment(languageCombo, Pos.TOP_LEFT);
        root.setTop(languageCombo);

        Group mainGroup = new Group(
                root,
                welcomeLabel,
                usernameField,
                passwordField,
                usernameText,
                passwordText,
                usernameError,
                passwordError,
                signIn,
                registerLink,
                imageView
        );

        Scene scene = new Scene(mainGroup, WIDTH, HEIGHT);
        scene.getStylesheets().add(AuthorizationWindow.class.getResource("/styles/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("authTitle"));
        stage.show();
    }

    private void updateLocalization() {
        this.bundle = ResourceBundle.getBundle("locale", Locale.getDefault());
        Platform.runLater(() -> {
            welcomeLabel.setText(bundle.getString("welcomeMessage"));
            usernameField.setPromptText(bundle.getString("enterUsername"));
            passwordField.setPromptText(bundle.getString("enterPassword"));
            usernameText.setText(bundle.getString("loginText"));
            passwordText.setText(bundle.getString("passwordText"));
            usernameError.setText(bundle.getString("usernameError"));
            passwordError.setText(bundle.getString("passwordError"));
            registerLink.setText(bundle.getString("registerLink"));
            signIn.setText(bundle.getString("signIn"));
            stage.setTitle(bundle.getString("authTitle"));
        });
    }
    private void handleLogin(TextField usernameField, PasswordField passwordField,
                             Label usernameError, Label passwordError) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        usernameError.setVisible(false);
        passwordError.setVisible(false);

        if (username == null || username.isEmpty()) {
            usernameError.setVisible(true);
            return;
        }
        if (password == null || password.isEmpty()) {
            passwordError.setVisible(true);
            return;
        }

        try {
            Request request = new Request();
            User user = new User();
            user.setUsername(username);
            user.setStatus("login");
            request.setUser(user);

            client.sendRequest(request);
            Response response = (Response) client.receiveResponse(5000);

            if (response == null) {
                showErrorAlert(bundle.getString("error"), bundle.getString("responseTimeOutError"));
                return;
            }

            if ("WRONG".equals(response.getResponse())) {
                usernameError.setVisible(true);
            } else if ("OK".equals(response.getResponse())) {
                user.setPassword(password);
                request.setUser(user);
                client.sendRequest(request);
                response = (Response) client.receiveResponse(5000);
                if (response == null) {
                    passwordError.setVisible(true);
                } else if ("ACCEPT".equals(response.getResponse())) {
                    Thread thread;
                    MainWindow mainWindow = new MainWindow();
                    thread = new Thread(() -> {
                        while(true) {
                            try {
                                mainWindow.updateTable();
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    });
                    mainWindow.show(user, this.stage, thread);
                    thread.setDaemon(true);
                    thread.start();
                    this.stage.hide();
                } else if ("WRONG".equals(response.getResponse())) {
                    passwordError.setVisible(true);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            showErrorAlert(bundle.getString("error"), bundle.getString("responseTimeOutError"));
            e.printStackTrace();
        }
    }
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}