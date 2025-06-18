package gui.program;

import commands.ScriptsHandler;
import commands.exceptions.ReccursionFoundException;
import gui.AbstractWindow;
import gui.main.AuthorizationWindow;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import utils.CaptchaGenerator;
import utils.console.ConsoleHandler;
import utils.files.FileReader;
import utils.kernel.Kernel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteScriptWindowCreate extends AbstractWindow {


    protected Kernel kernel;
    protected Stage stage = new Stage();
    private File file;
    protected User user;
    public ExecuteScriptWindowCreate() {
        super();
    }

    public void show(User user) {

        this.user = user;
        Button chooseScript = new Button(bundle.getString("openScript"));
        Button saveScript = new Button(bundle.getString("writeScript"));

        TextArea textArea = new TextArea();

        textArea.setEditable(true);
        textArea.setWrapText(true);

        chooseScript.getStyleClass().add("signIn");
        chooseScript.setPrefWidth(150);

        saveScript.getStyleClass().add("signIn");
        saveScript.setPrefWidth(150);
        saveScript.setOnMouseEntered(event -> saveScript.setCursor(Cursor.HAND));


        chooseScript.setOnMouseEntered(event -> chooseScript.setCursor(Cursor.HAND));
        chooseScript.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(bundle.getString("openScript"));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(bundle.getString("txtFilter"), "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null) {
                try {
                    List<String> lines = Files.readAllLines(selectedFile.toPath());
                    Platform.runLater(() -> {
                        textArea.clear();
                        lines.forEach(line -> textArea.appendText(line + "\n"));
                    });
                    file = selectedFile;
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR,
                                "Не удалось прочитать файл:\n" + ex.getMessage())
                                .showAndWait();
                    });
                }
            }
        });
        saveScript.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(bundle.getString("writeScript"));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(bundle.getString("txtFilter"), "*.txt"));
            if(file != null) {
                fileChooser.setInitialFileName(file.getName());
            } else {
                fileChooser.setInitialFileName("script.txt");
            }
            File selectedFile = fileChooser.showSaveDialog(stage);
            if(selectedFile != null) {
                String content = textArea.getText();
                try {
                    Files.write(selectedFile.toPath(), content.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                file = selectedFile;
            }
        });

        Button execute = new Button(bundle.getString("execute"));
        execute.setOnMouseEntered(event -> execute.setCursor(Cursor.HAND));
        execute.getStyleClass().add("signIn");
        execute.setPrefWidth(150);
        execute.setOnAction(event -> {
            List<String> commandsList= Arrays.asList(textArea.getText().split("\n"));
            if (ScriptsHandler.getScripts().contains(file.getName())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(bundle.getString("getProductError"));
                alert.setContentText(bundle.getString("getProductErrorText"));
                alert.getButtonTypes().setAll(
                        ButtonType.OK
                );
                alert.showAndWait();
            }
            String input = String.join("\n", commandsList);
            InputStream scriptInput = new ByteArrayInputStream(input.getBytes());
            InputStream originalInput = System.in;
            this.kernel = new Kernel(this.user);
            System.setIn(scriptInput);
            this.kernel.consoleManager = new ConsoleHandler();
            this.kernel.setCommands();
            this.kernel.runProgram();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(bundle.getString("scriptSuccess"));
            alert.setContentText(bundle.getString("scriptText"));
            alert.getButtonTypes().setAll(
                    ButtonType.OK
            );
            alert.showAndWait();
        });
        HBox buttons = new HBox(5, chooseScript, saveScript, execute);
        BorderPane infoPanel = new BorderPane();
        infoPanel.setBottom(buttons);
        VBox mainGroup = new VBox(
                15, textArea, infoPanel
        );
        VBox.setVgrow(textArea, Priority.ALWAYS);
        mainGroup.setPadding(new Insets(20, 20, 10, 20));
        Scene scene = new Scene(mainGroup,500, 800);
        scene.getStylesheets().add(AuthorizationWindow.class.getResource("/styles/style.css").toExternalForm());
        this.stage.setScene(scene);
        stage.setTitle(bundle.getString("executeScript"));
        stage.show();
    }
}
