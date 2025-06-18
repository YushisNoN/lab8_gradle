package gui.program;
import color.Color;
import country.Country;
import gui.AbstractWindow;
import gui.main.AuthorizationWindow;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.*;
import transfer.Request;
import transfer.Response;
import uom.UnitOfMeasure;
import java.io.IOException;
import java.util.ResourceBundle;

public class AddWindow extends AbstractWindow {


    protected Stage previousStage;
    protected Stage stage = new Stage();
    protected TextField productNameField = new TextField();
    protected TextField productPriceField = new TextField();
    protected Label productNameError;
    protected Label productPriceError;
    protected Label unitOfMeasureSeparatorLabel;

    protected Label personNameError;
    protected TextField personNameField = new TextField();
    protected TextField personHeightField = new TextField();
    protected Label personHeightError;
    protected Label countySeparatorLabel;
    protected Label eyeColorSeparatorLabel;
    protected Label hairColorSeparatorLabel;
    protected TextField locationWidthField = new TextField();
    protected Label locationWidthError;
    protected TextField locationHeightField = new TextField();
    protected Label locationHeightError;
    protected TextField locationDepthField = new TextField();
    protected Label locationDepthError;
    protected TextField coordinatesXField = new TextField();
    protected Label coordinatesXError;
    protected TextField coordinatesYField = new TextField();
    protected Label coordinatesYError;
    protected Button addButton;
    protected ComboBox<UnitOfMeasure> unitOfMeasureComboBox = new ComboBox<>();
    protected ComboBox<Country> nationalityComboBox = new ComboBox<>();
    protected ComboBox<Color> eyeColorComboBox = new ComboBox<>();
    protected ComboBox<Color> hairColorComboBox = new ComboBox<>();
    protected VBox mainGroup;
    protected ToggleGroup radioButtons = new ToggleGroup();
    protected RadioButton defaultAdd = new RadioButton(bundle.getString("radioAddDefault"));
    protected RadioButton addIfMin = new RadioButton(bundle.getString("radioAddIfMin"));
    protected RadioButton addIfMax = new RadioButton(bundle.getString("radioAddIfMax"));

    public AddWindow() {
        super();
        productNameError = new Label(bundle.getString("fieldEmpty"));
        productPriceError = new Label(bundle.getString("fieldEmpty"));
        personNameError = new Label(bundle.getString("fieldEmpty"));
        personHeightError = new Label(bundle.getString("fieldEmpty"));
        locationWidthError = new Label(bundle.getString("fieldEmpty"));
        locationHeightError = new Label(bundle.getString("fieldEmpty"));
        coordinatesXError = new Label(bundle.getString("fieldEmpty"));
        coordinatesYError = new Label(bundle.getString("fieldEmpty"));
        locationDepthError = new Label(bundle.getString("fieldEmpty"));
        addButton = new Button(bundle.getString("productAdd"));
        mainGroup = new VBox();
    }

    public void show(User user) {
        this.bundle = ResourceBundle.getBundle("locale");

        // PRODUCT PART

        Separator productSeparator = new Separator();
        Label productLabel = new Label(bundle.getString("productLabel"));
        productSeparator.setMaxWidth(400);
        productSeparator.setStyle("-fx-padding: 40 0 5 0;");
        StackPane stackProduct = new StackPane(productSeparator, productLabel);


        productNameField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 40 && (newText.isEmpty() || newText.matches("^[а-яА-ЯёЁa-zA-Z]+$"))) {
                return change;
            }
            return null;
        }));
        productNameField.getStyleClass().add("usernameField");
        productNameField.setPromptText(bundle.getString("productName"));
        productNameField.setFocusTraversable(false);
        productNameField.setMinWidth(180);
        productNameField.textProperty().addListener((obs, oldVal, newVal) -> checkField());


        productNameError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        productNameError.setPadding(new Insets(0, 0, 0, 12));

        productPriceField.getStyleClass().add("usernameField");
        productPriceField.setPromptText(bundle.getString("productPrice"));
        productPriceField.setFocusTraversable(false);
        productPriceField.setMinWidth(180);
        productPriceField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if(newText.matches("\\d*") && newText.length() <= 9) {
                return change;
            }
            return null;
        }));
        productPriceField.textProperty().addListener((obs, oldVal, newVal) -> checkField());

        productPriceError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        productPriceError.setPadding(new Insets(0, 0, 0, 12));

        unitOfMeasureSeparatorLabel = new Label(bundle.getString("productMeasure"));
        HBox stackUnitOfMeasureSeparator = new HBox(unitOfMeasureSeparatorLabel);



        unitOfMeasureComboBox.setItems(FXCollections.observableArrayList(null, UnitOfMeasure.METERS, UnitOfMeasure.CENTIMETERS, UnitOfMeasure.LITERS, UnitOfMeasure.MILLILITERS, UnitOfMeasure.SQUARE_METERS));
        unitOfMeasureComboBox.getSelectionModel().selectFirst();
        unitOfMeasureComboBox.getStyleClass().add("unitOfMeasure");
        unitOfMeasureComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(UnitOfMeasure unitOfMeasure, boolean empty) {
                super.updateItem(unitOfMeasure, empty);
                if(empty || unitOfMeasure == null) {
                    setText("N/A");
                } else {
                    setText(unitOfMeasure.name());
                }
            }
        });
        unitOfMeasureComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(UnitOfMeasure unitOfMeasure, boolean empty) {
                super.updateItem(unitOfMeasure, empty);
                if (empty || unitOfMeasure == null) {
                    setText("N/A");
                    setStyle("-fx-text-fill: white;");
                } else {
                    setText(unitOfMeasure.name());
                    setStyle("");
                }
            }
        });

        unitOfMeasureComboBox.setPrefWidth(400);

        // PERSON PART

        Separator personSeparator = new Separator();
        Label personLabel = new Label(bundle.getString("personLabel"));
        personSeparator.setMaxWidth(400);
        personSeparator.setStyle("-fx-padding: 40 0 5 0;");
        StackPane stackPerson = new StackPane(personSeparator, personLabel);


        personNameField.getStyleClass().add("usernameField");
        personNameField.setPromptText(bundle.getString("personName"));
        personNameField.setFocusTraversable(false);
        personNameField.setMinWidth(180);
        personNameField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        personNameField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 40 && (newText.isEmpty() || newText.matches("^[а-яА-ЯёЁa-zA-Z]+$"))) {
                return change;
            }
            return null;
        }));
        personNameError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        personNameError.setPadding(new Insets(0, 0, 0, 12));
        personHeightField.getStyleClass().add("usernameField");
        personHeightField.setPromptText(bundle.getString("personHeight"));
        personHeightField.setFocusTraversable(false);
        personHeightField.setMinWidth(180);
        personHeightField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        personHeightField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if(newText.matches("\\d+\\.?\\d*|\\d*\\.\\d+") && newText.length() <= 14) {
                return change;
            }
            return null;
        }));

        personHeightError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        personHeightError.setPadding(new Insets(0, 0, 0, 12));

        countySeparatorLabel = new Label(bundle.getString("personNationality"));
        eyeColorSeparatorLabel = new Label(bundle.getString("personEyeColor"));
        hairColorSeparatorLabel = new Label(bundle.getString("personHairColor"));
        countySeparatorLabel.setMaxWidth(Double.MAX_VALUE);
        eyeColorSeparatorLabel.setMaxWidth(Double.MAX_VALUE);
        hairColorSeparatorLabel.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(countySeparatorLabel, Priority.ALWAYS);
        HBox.setHgrow(eyeColorSeparatorLabel, Priority.ALWAYS);
        HBox.setHgrow(hairColorSeparatorLabel, Priority.ALWAYS);

        HBox personComboBoxSeparator = new HBox(
                countySeparatorLabel,
                eyeColorSeparatorLabel,
                hairColorSeparatorLabel
        );


        nationalityComboBox.setItems(FXCollections.observableArrayList(Country.values()));
        nationalityComboBox.getSelectionModel().selectFirst();
        nationalityComboBox.getStyleClass().add("unitOfMeasure");
        nationalityComboBox.setPrefWidth(400);


        eyeColorComboBox.setItems(FXCollections.observableArrayList(Color.BLUE,Color.BLACK));
        eyeColorComboBox.getSelectionModel().selectFirst();
        eyeColorComboBox.getStyleClass().add("unitOfMeasure");
        eyeColorComboBox.setPrefWidth(400);


        hairColorComboBox.setItems(FXCollections.observableArrayList(Color.RED,Color.GREEN, Color.WHITE, Color.ORANGE));
        hairColorComboBox.getSelectionModel().selectFirst();
        hairColorComboBox.getStyleClass().add("unitOfMeasure");
        hairColorComboBox.setPrefWidth(400);


        // LOCATION PART

        Separator locationSeparator = new Separator();
        Label locationLabel = new Label(bundle.getString("locationLabel"));
        locationSeparator.setMaxWidth(400);
        locationSeparator.setStyle("-fx-padding: 40 0 5 0;");
        StackPane stackLocation = new StackPane(locationSeparator, locationLabel);

        locationWidthField.getStyleClass().add("usernameField");
        locationWidthField.setPromptText(bundle.getString("locationWidth"));
        locationWidthField.setFocusTraversable(false);
        locationWidthField.setMinWidth(180);
        locationWidthField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        locationWidthField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if(newText.matches("\\d*") && newText.length() <= 9) {
                return change;
            }
            return null;
        }));

        locationWidthError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        locationWidthError.setPadding(new Insets(0, 0, 0, 12));




        locationHeightField.setFocusTraversable(false);
        locationHeightField.setPromptText(bundle.getString("locationHeight"));
        locationHeightField.getStyleClass().add("usernameField");
        locationHeightField.setMinWidth(180);
        locationHeightField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        locationHeightField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if(newText.matches("\\d+\\.?\\d*|\\d*\\.\\d+") && newText.length() <= 14) {
                return change;
            }
            return null;
        }));


        locationHeightError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        locationHeightError.setPadding(new Insets(0, 0, 0, 12));


        locationDepthField.getStyleClass().add("usernameField");
        locationDepthField.setPromptText(bundle.getString("locationDepth"));
        locationDepthField.setFocusTraversable(false);
        locationDepthField.setMinWidth(180);
        locationDepthField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        locationDepthField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if(newText.matches("\\d+\\.?\\d*|\\d*\\.\\d+") && newText.length() <= 14) {
                return change;
            }
            return null;
        }));

        locationDepthError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        locationDepthError.setPadding(new Insets(0, 0, 0, 12));

        HBox personComboBoxLayout = new HBox(5,
                nationalityComboBox,
                eyeColorComboBox,
                hairColorComboBox
        );

        // COORDINATES PART

        Separator coordinatesSeparator = new Separator();
        Label coordinatesLabel = new Label(bundle.getString("coordinatesLabel"));
        coordinatesSeparator.setMaxWidth(400);
        coordinatesSeparator.setStyle("-fx-padding: 40 0 5 0;");
        StackPane stackCoordinates = new StackPane(coordinatesSeparator, coordinatesLabel);


        coordinatesXField.getStyleClass().add("usernameField");
        coordinatesXField.setPromptText(bundle.getString("coordinatesX"));
        coordinatesXField.setFocusTraversable(false);
        coordinatesXField.setMinWidth(180);
        coordinatesXField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        coordinatesXField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.equals("-")) {
                return change;
            }
            if (newText.matches("-?\\d{1,9}") && newText.length() <= 17) {
                return change;
            }
            return null;
        }));


        coordinatesXError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        coordinatesXError.setPadding(new Insets(0, 0, 0, 12));

        coordinatesYField.getStyleClass().add("usernameField");
        coordinatesYField.setPromptText(bundle.getString("coordinatesY"));
        coordinatesYField.setFocusTraversable(false);
        coordinatesYField.setMinWidth(180);
        coordinatesYField.textProperty().addListener((obs, oldVal, newVal) -> checkField());
        coordinatesYField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.equals("-")) {
                return change;
            }
            if (newText.matches("-?\\d{1,9}") && newText.length() <= 9) {
                return change;
            }
            return null;
        }));



        coordinatesYError.setStyle("-fx-text-fill: red; -fx-font-family: Bahnschrift; -fx-font-size: 10px;");
        // productNameError.setVisible(false);
        coordinatesYError.setPadding(new Insets(0, 0, 0, 12));


        addButton.setOnAction(event -> {
            if(addButton.getStyleClass().contains("addButton-true")) {
                createProduct(user);
            }
        });
        addButton.setPrefWidth(400);
        addButton.setPrefHeight(50);
        VBox addButtonLayout = new VBox(addButton);
        addButtonLayout.setPadding(new Insets(10, 0, 0, 0));

        // RADIO BUTTON CHECK

        defaultAdd.setToggleGroup(radioButtons);
        addIfMax.setToggleGroup(radioButtons);
        addIfMin.setToggleGroup(radioButtons);
        defaultAdd.setUserData("add");
        addIfMax.setUserData("add_if_max");
        addIfMin.setUserData("add_if_min");
        defaultAdd.setSelected(true);
        addButton.getStyleClass().add("addButton-false");

        VBox radios = new VBox(defaultAdd, addIfMax, addIfMin);

        mainGroup.getChildren().addAll(
                stackProduct,
                productNameField,
                productNameError,
                productPriceField,
                productPriceError,
                stackUnitOfMeasureSeparator,
                unitOfMeasureComboBox,
                stackPerson,
                personNameField,
                personNameError,
                personHeightField,
                personHeightError,
                personComboBoxSeparator,
                personComboBoxLayout,
                stackLocation,
                locationWidthField,
                locationWidthError,
                locationHeightField,
                locationHeightError,
                locationDepthField,
                locationDepthError,
                stackCoordinates,
                coordinatesXField,
                coordinatesXError,
                coordinatesYField,
                coordinatesYError,
                radios,
                addButtonLayout
        );

        mainGroup.setPadding(new Insets(5, 20, 5,20));
        Scene scene = new Scene(mainGroup,400, 800);
        scene.getStylesheets().add(AuthorizationWindow.class.getResource("/styles/style.css").toExternalForm());
        this.stage.setScene(scene);
        stage.setTitle(bundle.getString("add"));
        stage.show();
    }
    protected Product setValues() {
        try {
            Product product = new Product();
            Person person = new Person();
            Location location = new Location();
            Coordinates coordinates = new Coordinates();

            coordinates.setY(Integer.parseInt(coordinatesYField.getText()));
            coordinates.setX(Long.parseLong(coordinatesXField.getText()));

            location.setZ(Double.parseDouble(locationDepthField.getText()));
            location.setX(Long.parseLong(locationWidthField.getText()));
            location.setY(Double.parseDouble(locationHeightField.getText()));

            person.setName(personNameField.getText());
            person.setHeight(Float.parseFloat(personHeightField.getText()));
            person.setNationality(nationalityComboBox.getValue());
            person.setEyeColor(eyeColorComboBox.getValue());
            person.setHairColor(hairColorComboBox.getValue());
            person.setLocation(location);

            product.setName(productNameField.getText());
            product.setPrice(Integer.parseInt(productPriceField.getText()));
            product.setOwner(person);
            product.setCoordinates(coordinates);
            product.setUnitOfMeasure(unitOfMeasureComboBox.getValue());
            return product;
        } catch (CoordinateWrongValueException e) {
            throw new RuntimeException(e);
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
    }
    protected void createProduct(User user) {
        try {
            this.client.open();
            Product product = this.setValues();
            Request request = new Request();
            if(radioButtons.getUserData() != null) {
                request.setCommand("remove_greater");
            }
            else {
                request.setCommand(radioButtons.getSelectedToggle().getUserData().toString());
            }
            request.setProduct(product);
            request.setUser(user);
            System.out.println(request.getProduct().toString() + "\n" + request.getCommand());
            this.client.sendRequest(request);
            Response response = (Response) this.client.receiveResponse(5000);
            if(response.getResponse() != null && radioButtons.getUserData() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Создание объекта");
                alert.setHeaderText(null);
                alert.setContentText(response.getResponse());
                alert.showAndWait();
                this.stage.close();
            } else if(response != null && response.getResponse().equals("W")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(bundle.getString("objectRemove"));
                alert.getButtonTypes().setAll(
                        ButtonType.OK
                );
            } else if(!response.getResponse().equals("W")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(bundle.getString("getProductError"));
                alert.setContentText(bundle.getString("getProductErrorText"));
                alert.getButtonTypes().setAll(
                        ButtonType.OK
                );
                alert.showAndWait();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkField() {
        TextField[] fields = {productNameField, productPriceField, personNameField,
                personHeightField, locationDepthField, locationWidthField,
                locationHeightField, coordinatesYField, coordinatesXField};

        Label[] errors = {productNameError, productPriceError, personNameError,
                personHeightError, locationDepthError, locationWidthError,
                locationHeightError, coordinatesYError, coordinatesXError};
        boolean allValid = true;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getText().isEmpty()) {
                errors[i].setVisible(true);
                addButton.getStyleClass().removeAll("addButton-false", "addButton-true");
                addButton.getStyleClass().add("addButton-false");
                addButton.setOnMouseEntered(event -> addButton.setCursor(Cursor.DEFAULT));
                allValid = false;
            } else {
                errors[i].setVisible(false);
            }
        }

        if (!allValid) {
            addButton.getStyleClass().removeAll("addButton-false", "addButton-true");
            addButton.getStyleClass().add("addButton-false");
            addButton.setOnMouseEntered(event -> addButton.setCursor(Cursor.DEFAULT));
            return;
        }
        addButton.getStyleClass().removeAll("addButton-false", "addButton-true");
        addButton.getStyleClass().add("addButton-true");
        addButton.setOnMouseEntered(event -> addButton.setCursor(Cursor.HAND));

    }
}
