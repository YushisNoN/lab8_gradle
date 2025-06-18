package gui.program;

import client.Client;
import gui.AbstractWindow;
import gui.TableHelper;
import gui.main.AuthorizationWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.CoordinateWrongValueException;
import models.NullValueException;
import models.Product;
import models.User;
import transfer.Request;
import transfer.Response;
import utils.CaptchaGenerator;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.TreeSet;
import java.util.function.Predicate;

public class MainWindow extends AbstractWindow {

    protected User user;
    protected Stage previousStage;
    protected Stage stage = new Stage();
    protected ObservableList<Product> products = FXCollections.observableArrayList();
    protected FilteredList<Product> filteredList = new FilteredList<>(products, p -> true);
    protected SortedList<Product> sortedList = new SortedList<>(filteredList);
    protected TableView<Product> table = new TableView<>();
    protected VisualizeWindow visualizeWindow = new VisualizeWindow();
    protected Thread secondThread;
    public MainWindow() {
        super();
    }
    public void show(User user, Stage stagePrev, Thread thread) {
        this.secondThread = thread;
        Request request = new Request();
        request.setUser(user);
        request.setCommand("show");
        TreeSet<Product> collection;

        try {
            this.client.open();
            client.sendRequest(request);
            Response response = (Response) client.receiveResponse(5000);
            collection = response.getCollection();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        previousStage = stagePrev;
        previousStage.hide();
        System.out.println(previousStage);
        this.user = user;
        client = new Client("localhost", 2205, false);
        try {
            client.open();
        } catch (IOException e) {
            return;
        }
        ImageView accountImage = new ImageView( new Image(getClass().getResourceAsStream("/img/profile-user.png")));
        accountImage.setFitWidth(30);
        accountImage.setFitHeight(30);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem leaveButton = new MenuItem(bundle.getString("logOut"));

        contextMenu.getItems().add(leaveButton);
        accountImage.setOnMouseEntered(event -> accountImage.setCursor(Cursor.HAND));
        accountImage.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(accountImage, event.getScreenX(), event.getScreenY());
            }
        });

        Label username = new Label(this.user.getUsername());
        Label usernameTip = new Label(bundle.getString("usernameTip"));
        username.getStyleClass().add("username");
        usernameTip.getStyleClass().add("username");
        leaveButton.setOnAction(event -> {
            stage.close();
            this.secondThread.interrupt();
            if (previousStage != null) {
                previousStage.show();
            }
        });
        StackPane accountPane = new StackPane(accountImage);
        VBox userNameBox = new VBox(usernameTip, username);
        HBox account = new HBox(2, accountPane, userNameBox);
        Button add = new Button(bundle.getString("add"));
        add.setPrefWidth(150);
        add.setOnMouseEntered(event -> add.setCursor(Cursor.HAND));
        add.getStyleClass().add("signIn");
        add.setOnAction(event -> {
            AddWindow addWindow = new AddWindow();
            addWindow.show(this.user);
        });

        Button visualize = new Button(bundle.getString("visualizeButton"));
        visualize.setPrefWidth(150);
        visualize.getStyleClass().add("signIn");
        visualize.setOnMouseEntered(event -> visualize.setCursor(Cursor.HAND));
        visualize.setOnAction(event -> {
            visualizeWindow.visualize(collection, user);
        });

        Button clearCollection = new Button(bundle.getString("clear"));
        clearCollection.setPrefWidth(200);
        clearCollection.getStyleClass().add("addButton-false");
        clearCollection.setOnMouseEntered(event -> clearCollection.setCursor(Cursor.HAND));
        clearCollection.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("clear"));
            alert.setHeaderText(bundle.getString("clearHeader"));
            TextField inputField = new TextField();
            inputField.setPromptText(bundle.getString("clearInfo"));
            VBox vbox = new VBox(10,
                    new Label(bundle.getString("clearInfo")  +"\n" + bundle.getString("passwordClearFirst")),
                    inputField
            );
            vbox.setPadding(new Insets(15));
            vbox.setPrefWidth(300);
            alert.getDialogPane().setContent(vbox);
            alert.getButtonTypes().setAll(
                    ButtonType.OK,
                    ButtonType.CANCEL
            );
            Platform.runLater(inputField::requestFocus);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String userInput = inputField.getText();
                if(userInput.equals(bundle.getString("passwordClearFirst"))) {
                    Alert alertSecond= new Alert(Alert.AlertType.ERROR);
                    alertSecond.setTitle(bundle.getString("clear"));
                    alertSecond.setHeaderText(bundle.getString("finalClearHeader"));

                    TextField inputFieldSecond = new TextField();
                    String captcha = CaptchaGenerator.generateCaptcha(new Random().nextInt(4, 8));
                    inputFieldSecond.setPromptText(bundle.getString("clearInfo")); // Подсказка в поле ввода
                    VBox vboxSecond = new VBox(10,
                            new Label(bundle.getString("finalClearInfo").replace("\\", "\n")  + "\n" + captcha),
                            inputFieldSecond
                    );
                    vboxSecond.setPadding(new Insets(15));
                    vboxSecond.setPrefWidth(300);
                    alertSecond.getDialogPane().setContent(vboxSecond);
                    alertSecond.getButtonTypes().setAll(
                            ButtonType.OK,
                            ButtonType.CANCEL
                    );
                    Platform.runLater(inputFieldSecond::requestFocus);
                    Optional<ButtonType> resultSecond = alertSecond.showAndWait();
                    if (resultSecond.isPresent() && resultSecond.get() == ButtonType.OK) {
                        String userInputSecond = inputFieldSecond.getText();
                        if(userInputSecond.equals(captcha)) {
                            try {
                                Request req = new Request();
                                req.setUser(user);
                                req.setCommand("clear");
                                client.sendRequest(req);
                                Response response = (Response) client.receiveResponse(5000);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });
        if(collection != null && !collection.isEmpty())
            products.addAll(collection);
        else {
            products.clear();
        }
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setEditable(true);
        table.getColumns().addAll(new TableHelper(this.client, this.user).createTable());
        table.setItems(sortedList);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Button priceFilter = new Button(bundle.getString("countLessThanPrice"));
        priceFilter.getStyleClass().add("signIn");
        priceFilter.setPrefWidth(150);
        priceFilter.setOnMouseEntered(event -> priceFilter.setCursor(Cursor.HAND));
        priceFilter.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(bundle.getString("filterPrice"));
            dialog.setHeaderText(bundle.getString("enterMaxPrice"));
            dialog.setContentText(bundle.getString("price"));
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(priceStr -> {
                try {
                    int maxPrice = Integer.parseInt(priceStr);
                    filteredList.setPredicate(product -> product.getPrice() < maxPrice);
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, bundle.getString("invalidPriceFormat")).show();
                }
            });
        });

        Button nameFilter = new Button(bundle.getString("filterName"));
        nameFilter.getStyleClass().add("signIn");
        nameFilter.setPrefWidth(150);
        nameFilter.setOnMouseEntered(event -> nameFilter.setCursor(Cursor.HAND));
        nameFilter.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(bundle.getString("filterName"));
            dialog.setHeaderText(bundle.getString("enterName"));
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(substring -> {
                if (!substring.trim().isEmpty()) {
                    filteredList.setPredicate(product ->
                            product.getName().toLowerCase().contains(substring.toLowerCase())
                    );
                } else {
                    filteredList.setPredicate(null);
                }
            });
        });
        Button resetFilter = new Button(bundle.getString("resetFilter"));
        resetFilter.getStyleClass().add("signIn");
        resetFilter.setPrefWidth(200);
        resetFilter.setOnMouseEntered(event -> resetFilter.setCursor(Cursor.HAND));
        resetFilter.setOnAction(event -> {
            filteredList.setPredicate(null);
        });

        Button removeAnyByPrice = new Button(bundle.getString("removeAnyByPrice"));
        removeAnyByPrice.getStyleClass().add("signIn");
        removeAnyByPrice.setPrefWidth(150);
        removeAnyByPrice.setOnMouseEntered(event -> removeAnyByPrice.setCursor(Cursor.HAND));
        removeAnyByPrice.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(bundle.getString("removeAnyByPrice"));
            dialog.setHeaderText(bundle.getString("enterMaxPrice"));
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(priceStr -> {
                try {
                    int maxPrice = Integer.parseInt(priceStr);
                    Request request1 = new Request();
                    request1.setUser(user);
                    request1.setCommand("remove_any_by_price " + maxPrice);
                    client.sendRequest(request1);
                    Response response = (Response) client.receiveResponse(5000);
                    if(response != null && response.getResponse() != null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(bundle.getString("objectRemove"));
                        alert.showAndWait();

                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, bundle.getString("invalidPriceFormat")).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        Button removeById = new Button(bundle.getString("removeAnyById"));
        removeById.getStyleClass().add("signIn");
        removeById.setPrefWidth(150);
        removeById.setOnMouseEntered(event -> removeById.setCursor(Cursor.HAND));
        removeById.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(bundle.getString("removeAnyById"));
            dialog.setHeaderText(bundle.getString("enterId"));
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(priceStr -> {
                try {
                    int id = Integer.parseInt(priceStr);
                    Request request1 = new Request();
                    request1.setUser(user);
                    request1.setCommand("remove_by_id " + id);
                    client.sendRequest(request1);
                    Response response = (Response) client.receiveResponse(5000);
                    if(response != null && response.getResponse().equals("W")) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(bundle.getString("objectRemove"));
                        alert.getButtonTypes().setAll(
                                ButtonType.OK
                        );
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(bundle.getString("getProductError"));
                        alert.setContentText(bundle.getString("getProductErrorText"));
                        alert.getButtonTypes().setAll(
                                ButtonType.OK
                        );
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, bundle.getString("invalidPriceFormat")).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        Button removeGreater = new Button(bundle.getString("removeGreater"));
        removeGreater.setPrefWidth(150);
        removeGreater.setOnMouseEntered(event -> removeGreater.setCursor(Cursor.HAND));
        removeGreater.getStyleClass().add("signIn");
        removeGreater.setOnAction(event -> {
            AddWindow removeGreaterWindow = new AddWindow();
            removeGreaterWindow.defaultAdd.setVisible(false);
            removeGreaterWindow.addIfMax.setVisible(false);
            removeGreaterWindow.addIfMin.setVisible(false);
            removeGreaterWindow.radioButtons.setUserData("remove_greater");
            removeGreaterWindow.stage.setTitle(bundle.getString("removeGreater"));
            removeGreaterWindow.addButton.setText(bundle.getString("removeGreater"));
            removeGreaterWindow.show(user);
        });

        Button removeRow = new Button(bundle.getString("remove"));
        removeRow.getStyleClass().add("delButton-false");
        removeRow.setPrefWidth(150);
        removeRow.setOnMouseEntered(event -> removeRow.setCursor(Cursor.DEFAULT));
        removeRow.setDisable(true);
        removeRow.setOnAction(event -> {
            Product selectedProduct = table.getSelectionModel().getSelectedItem();
            if(selectedProduct != null) {
                try {
                    Request request1 = new Request();
                    request1.setUser(user);
                    request1.setCommand("remove_by_id " + selectedProduct.getId());
                    client.sendRequest(request1);
                    Response response = (Response) client.receiveResponse(5000);
                    if(response != null && "W".equals(response.getResponse())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(bundle.getString("objectRemove"));
                        alert.getButtonTypes().setAll(
                                ButtonType.OK
                        );
                    } else if(!"W".equals(response.getResponse())){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(bundle.getString("getProductError"));
                        alert.setContentText(bundle.getString("getProductErrorText"));
                        alert.getButtonTypes().setAll(
                                ButtonType.OK
                        );
                        alert.showAndWait();
                    }
                    removeRow.getStyleClass().removeAll();
                    removeRow.getStyleClass().add("delButton-false");
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                removeRow.setDisable(false);
                removeRow.getStyleClass().removeAll();
                removeRow.getStyleClass().add("delButton-true");
                removeRow.setOnMouseEntered(event -> removeRow.setCursor(Cursor.HAND));
            } else {
                removeRow.getStyleClass().removeAll();
                removeRow.getStyleClass().add("delButton-false");
                removeRow.setOnMouseEntered(event -> removeRow.setCursor(Cursor.DEFAULT));
            }
        });
        Button executeScript = new Button(bundle.getString("executeScript"));
        executeScript.setOnMouseEntered(event -> executeScript.setCursor(Cursor.HAND));
        executeScript.setPrefWidth(150);
        executeScript.getStyleClass().add("signIn");
        executeScript.setOnAction(event -> {
            ExecuteScriptWindowCreate creator = new ExecuteScriptWindowCreate();
            creator.show(this.user);
        });
        Label info = new Label(bundle.getString("infoLabel") + " " + ((collection != null && !collection.isEmpty()) ? collection.size(): 0) + "\n" +
                bundle.getString("infoTypeLabel") + " TreeSet");
        BorderPane infoPanel = new BorderPane();
        infoPanel.setBottom(info);
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.add(account, 0, 0);
        grid.add(add, 1, 0);
        grid.add(visualize, 2, 0);
        grid.add(priceFilter, 3, 0);
        grid.add(nameFilter, 4, 0);
        grid.add(resetFilter, 5, 0);
        grid.add(removeAnyByPrice, 1, 1);
        grid.add(removeById, 2, 1);
        grid.add(removeGreater, 3, 1);
        grid.add(removeRow, 4, 1);
        grid.add(clearCollection, 5, 1);
        grid.add(executeScript, 6, 1);
        VBox mainGroup = new VBox(
                30, grid, scrollPane, infoPanel
        );
        mainGroup.setPadding(new Insets(20, 20, 0, 20));
        Scene scene = new Scene(mainGroup, WIDTH, HEIGHT);
        scene.getStylesheets().add(AuthorizationWindow.class.getResource("/styles/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("bestTeacher"));
        stage.show();
    }
    public void updateTable() {
        try {
            this.client.open();
            Request request = new Request();
            request.setCommand("show");
            client.sendRequest(request);
            Response response = (Response) client.receiveResponse(5000);
            TreeSet<Product> collection = response.getCollection();
            request.setCommand("getOwners");
            request.setUser(user);
            client.sendRequest(request);
            response = (Response) client.receiveResponse(5000);
            Response finalResponse = response;

            Thread thread = new Thread(() -> {
                visualizeWindow.update(collection, finalResponse.getUserProducts());
            });
            thread.start();
            Predicate<Product> currentFilter = (Predicate<Product>) filteredList.getPredicate();
            Platform.runLater(() -> {
                if(collection == null || collection.isEmpty()) {
                    products.clear();
                    filteredList.setPredicate(currentFilter);
                    return;
                }
                for (Product updatedProduct : collection) {
                    Optional<Product> existingOpt = products.stream()
                            .filter(p -> p.getId() == updatedProduct.getId())
                            .findFirst();

                    if (existingOpt.isPresent()) {
                        Product existing = existingOpt.get();
                        existing.setName(updatedProduct.getName());
                        existing.setPrice(updatedProduct.getPrice());
                        existing.setUnitOfMeasure(updatedProduct.getUnitOfMeasure());

                        try {
                            existing.getCoordinates().setX(updatedProduct.getCoordinates().getX());
                        } catch (NullValueException e) {
                            throw new RuntimeException(e);
                        } catch (CoordinateWrongValueException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            existing.getCoordinates().setY(updatedProduct.getCoordinates().getY());
                        } catch (NullValueException e) {
                            throw new RuntimeException(e);
                        }
                        existing.getOwner().setName(updatedProduct.getOwner().getName());
                        existing.getOwner().setHeight(updatedProduct.getOwner().getHeight());
                        existing.getOwner().setEyeColor(updatedProduct.getOwner().getEyeColor());
                        existing.getOwner().setHairColor(updatedProduct.getOwner().getHairColor());
                        existing.getOwner().setNationality(updatedProduct.getOwner().getNationality());
                        existing.getOwner().getLocation().setX(updatedProduct.getOwner().getLocation().getX());
                        try {
                            existing.getOwner().getLocation().setY(updatedProduct.getOwner().getLocation().getY());
                        } catch (NullValueException e) {
                            throw new RuntimeException(e);
                        }
                        existing.getOwner().getLocation().setZ(updatedProduct.getOwner().getLocation().getZ());
                    } else {
                        products.add(updatedProduct);
                    }
                }
                products.removeIf(p -> collection.stream().noneMatch(up -> up.getId() == p.getId()));

                filteredList.setPredicate(currentFilter);
            });

        } catch (IOException | ClassNotFoundException e) {
            Platform.runLater(() ->
                    new Alert(Alert.AlertType.ERROR, "Ошибка обновления: " + e.getMessage()).show());
        }
    }

}
