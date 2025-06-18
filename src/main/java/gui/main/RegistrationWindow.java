package gui.main;

import client.Client;
import gui.program.MainWindow;
import gui.utils.LanguageSelector;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.User;
import transfer.Request;
import transfer.Response;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RegistrationWindow {
    private final Client client;
    private ResourceBundle bundle;
    private final static short WIDTH = 800;
    private final static short HEIGHT = 600;
    private final Stage previousStage;
    private final Stage stage;
    private Text welcomeLabel;
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Text usernameText;
    private Text passwordText;
    private Label usernameError;
    private Label passwordError;
    private Hyperlink registerLink;
    private PasswordField passwordFieldConfirm = new PasswordField();
    private Button signIn;

    public RegistrationWindow(Client client, Stage previousStage) {
        this.client = client;
        this.previousStage = previousStage;
        this.stage = new Stage();
    }

    public void show() {
        previousStage.hide();
        try {
            client.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.bundle = ResourceBundle.getBundle("locale");

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

        passwordField = new PasswordField();
        passwordField.getStyleClass().add("usernameField");
        passwordField.setPromptText(bundle.getString("enterPassword"));
        passwordField.setFocusTraversable(false);
        passwordField.setMinWidth(180);
        passwordField.setLayoutX(usernameField.getLayoutBounds().getWidth() + 145);
        passwordField.setLayoutY(usernameField.getLayoutBounds().getHeight() + 310);

        passwordFieldConfirm.getStyleClass().add("usernameField");
        passwordFieldConfirm.setPromptText(bundle.getString("checkPassword"));
        passwordFieldConfirm.setFocusTraversable(false);
        passwordFieldConfirm.setMinWidth(180);
        passwordFieldConfirm.setLayoutX(usernameField.getLayoutBounds().getWidth() + 145);
        passwordFieldConfirm.setLayoutY(usernameField.getLayoutBounds().getHeight() + 350);

        usernameText = new Text(bundle.getString("loginText"));
        passwordText = new Text(bundle.getString("passwordText"));
        usernameText.setLayoutX(WIDTH / 2 - usernameText.getLayoutBounds().getWidth() / 2 - 235);
        usernameText.setLayoutY(HEIGHT / 2 - 60);
        passwordText.setLayoutX(WIDTH / 2 - passwordText.getLayoutBounds().getWidth() / 2 - 235);
        passwordText.setLayoutY(HEIGHT / 2 + 5);

        usernameError = new Label(bundle.getString("usernameError"));
        passwordError = new Label(bundle.getString("incorrectPasswords"));
        usernameError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        passwordError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        usernameError.setVisible(false);
        usernameError.setLayoutX(WIDTH / 2 - usernameError.getLayoutBounds().getWidth() / 2 - 255);
        usernameError.setLayoutY(HEIGHT / 2 - 25);

        passwordError.setVisible(false);
        passwordError.setLayoutX(WIDTH / 2 - passwordError.getLayoutBounds().getWidth() / 2 - 255);
        passwordError.setLayoutY(HEIGHT / 2 + 80);

        signIn = new Button(bundle.getString("signUp"));
        signIn.setFocusTraversable(true);
        signIn.setOnMouseEntered(event -> signIn.setCursor(Cursor.HAND));
        signIn.getStyleClass().add("signIn");
        signIn.setMinWidth(180);
        signIn.setLayoutX(signIn.getLayoutBounds().getWidth() + 145);
        signIn.setLayoutY(signIn.getLayoutBounds().getHeight() + 400);
        signIn.setOnAction(event -> handleSignUp(usernameField, passwordField, passwordFieldConfirm, passwordError, usernameError));

        registerLink = new Hyperlink(bundle.getString("signInLink"));
        registerLink.setLayoutX(WIDTH / 2 - registerLink.getLayoutBounds().getWidth() / 2 - 235);
        registerLink.setLayoutY(HEIGHT / 2 + 135);
        registerLink.setOnAction(event -> {
            stage.close();
            previousStage.show();
        });
        Image image = new Image(getClass().getResourceAsStream("/img/bg_main.jpeg"));
        ImageView imageView = new ImageView(image);
        imageView.setX(WIDTH / 2);

        BorderPane root = new BorderPane();
        ComboBox<String> languageCombo = LanguageSelector.createLanguageComboBox(this::updateLocalization);
        languageCombo.getStyleClass().add("unitOfMeasure");
        BorderPane.setAlignment(languageCombo, Pos.TOP_LEFT);
        root.setTop(languageCombo);


        imageView.setFitHeight(HEIGHT);
        imageView.setFitWidth(WIDTH/2);
        Group layout = new Group(
                root,
                welcomeLabel,
                usernameField,
                passwordField,
                passwordError,
                usernameText,
                passwordText,
                usernameError,
                passwordFieldConfirm,
                signIn,
                registerLink,
                imageView
        );

        Scene scene = new Scene(layout, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle(bundle.getString("registerTitle"));
        scene.getStylesheets().add(AuthorizationWindow.class.getResource("/styles/style.css").toExternalForm());
        stage.setOnCloseRequest(e -> previousStage.show());
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
            passwordFieldConfirm.setPromptText(bundle.getString("checkPassword"));
        });
    }

    private void handleSignUp(TextField usernameField, PasswordField passwordField, PasswordField passwordFieldConfirm,
                              Label passwordError, Label usernameError) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String checkPassword = passwordFieldConfirm.getText();

        passwordError.setVisible(false);
        usernameError.setVisible(false);
        if(username == null || username.isEmpty()) {
            usernameError.setText(bundle.getString("usernameError"));
            usernameError.setVisible(true);
            return;
        }
        if((password == null || password.isEmpty()) && (checkPassword.isEmpty() || checkPassword == null)) {
            passwordError.setText(bundle.getString("newPasswordError"));
            passwordError.setVisible(true);
            return;
        } else if(password.equals(checkPassword) == false){
            passwordError.setText(bundle.getString("incorrectPassword"));
            passwordError.setVisible(true);
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus("signup");
        Request request = new Request();
        request.setUser(user);
        try {
            this.client.sendRequest(request);
            System.out.println(request.getUser().getUsername() + " " + request.getUser().getPassword());
            Response response = (Response) this.client.receiveResponse(5000);
            System.out.println(response.getResponse());
            if(response == null) {
                return;
            }
            if("WRONG".equals(response.getResponse())) {
                usernameError.setText(bundle.getString("newLoginError"));
                usernameError.setVisible(true);
                System.out.println("ERROR LOGIN");
            } else if("ACCEPT".equals(response.getResponse())) {
                System.out.println("SUCCESS");
            } else {
                Request req = new Request();
                user.setPassword(password);

                user.setStatus("OK");
                user.setUsername(username);
                req.setUser(user);
                System.out.println(req.getUser().getUsername() + " " + req.getUser().getPassword());
                this.client.sendRequest(req);
                Response response1 = (Response) this.client.receiveResponse(5000);
                System.out.println(response1.getResponse());
                if(response1.getResponse().equals("ACCEPT")) {
                    MainWindow mainWindow = new MainWindow();
                    Thread thread;
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
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}