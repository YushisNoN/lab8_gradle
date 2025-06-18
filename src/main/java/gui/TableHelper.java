package gui;

import client.Client;
import color.Color;
import country.Country;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import javafx.scene.control.cell.ComboBoxTableCell;

import models.*;
import transfer.Request;
import transfer.Response;
import uom.UnitOfMeasure;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableHelper extends AbstractWindow {

    private Client client;
    private User user;
    public TableHelper(Client client, User user) {
        super();
        this.client = client;
        this.user = user;
    }
    private java.util.function.Consumer<Product> onProductUpdated;

    public void setOnProductUpdated(java.util.function.Consumer<Product> onProductUpdated) {
        this.onProductUpdated = onProductUpdated;
    }

    public void sendUpdatedProduct(Product product) {
        try {
            Request request = new Request();
            request.setCommand("update_id" + " " + product.getId());
            request.setProduct(product);
            request.setUser(this.user);
            this.client.sendRequest(request);
            Response response = (Response) this.client.receiveResponse(5000);
            System.out.println(" PENIS " + response.getResponse() + " " + response.getCommand());
            if(response.getResponse() != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("updateButton"));
                alert.setHeaderText(bundle.getString("succesUpdate"));
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<TableColumn<Product, ?>> createTable() {
        // ID (не редактируется)
        TableColumn<Product, Long> idColumn = new TableColumn<>(bundle.getString("idColumn"));
        idColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getId()).asObject());

        // Название продукта (редактируемое)
        TableColumn<Product, String> productNameColumn = new TableColumn<>(bundle.getString("productNameColumn"));
        productNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productNameColumn.setOnEditCommit(event -> {
            try{
                Product product = event.getRowValue();
                product.setName(event.getNewValue());
                if (onProductUpdated != null) {
                    onProductUpdated.accept(product);
                }
                sendUpdatedProduct(product);
            } catch (Exception e) {
            }
        });

        TableColumn<Product, Long> coordinatesXColumn = new TableColumn<>(bundle.getString("coordX"));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getCoordinates().getX()).asObject());
        coordinatesXColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        coordinatesXColumn.setOnEditCommit(event -> {
            try {
                Product product = event.getRowValue();
                Coordinates coords = product.getCoordinates();
                coords.setX(event.getNewValue());
                product.setCoordinates(coords);
                sendUpdatedProduct(product);
            } catch (CoordinateWrongValueException e) {
            } catch (NullValueException e) {
            }
        });


        TableColumn<Product, Integer> coordinatesYColumn = new TableColumn<>(bundle.getString("coordY"));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCoordinates().getY()).asObject());
        coordinatesYColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        coordinatesYColumn.setOnEditCommit(event -> {
            try {
                Product product = event.getRowValue();
                Coordinates coords = product.getCoordinates();
                coords.setY(event.getNewValue());
                product.setCoordinates(coords);
                sendUpdatedProduct(product);
            } catch (NullValueException e) {
                throw new RuntimeException(e);
            }
        });

        // Дата создания (не редактируем)
        TableColumn<Product, String> creationDate = new TableColumn<>(bundle.getString("creationDateColumn"));
        creationDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))));


        TableColumn<Product, Integer> productPriceColumn = new TableColumn<>(bundle.getString("priceColumn"));
        productPriceColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPrice()).asObject());
        productPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        productPriceColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setPrice(event.getNewValue());
            sendUpdatedProduct(product);
        });

        TableColumn<Product, UnitOfMeasure> unitOfMeasureColumn = new TableColumn<>(bundle.getString("uomColumn"));
        unitOfMeasureColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getUnitOfMeasure())
        );
        unitOfMeasureColumn.setCellFactory(ComboBoxTableCell.forTableColumn(UnitOfMeasure.values()));

        unitOfMeasureColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setUnitOfMeasure(event.getNewValue());
            sendUpdatedProduct(product);
        });


        // Владелец: имя (не редактируем)
        TableColumn<Product, String> personName = new TableColumn<>(bundle.getString("personNameColumn"));
        personName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOwner().getName()));
        personName.setCellFactory(TextFieldTableCell.forTableColumn());
        personName.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            Person owner = product.getOwner();
            owner.setName(event.getNewValue());
            product.setOwner(owner);
            sendUpdatedProduct(product);
        });

        // Рост владельца (редактируемый)
        TableColumn<Product, Float> personHeight = new TableColumn<>(bundle.getString("personHeightColumn"));
        personHeight.setCellValueFactory(cellData ->
                new SimpleFloatProperty(cellData.getValue().getOwner().getHeight()).asObject());
        personHeight.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        personHeight.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            Person owner = product.getOwner();
            owner.setHeight(event.getNewValue());
            product.setOwner(owner);
            sendUpdatedProduct(product);
        });

        // Цвет глаз (не редактируем)
        TableColumn<Product, Color> personEyeColor = new TableColumn<>(bundle.getString("personEyeColor"));
        personEyeColor.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOwner().getEyeColor())
        );
        personEyeColor.setCellFactory(ComboBoxTableCell.forTableColumn(Color.BLACK, Color.BLUE, Color.ORANGE));
        personEyeColor.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.getOwner().setEyeColor(event.getNewValue());
            sendUpdatedProduct(product);
        });

        // Цвет волос (не редактируем)
        TableColumn<Product, Color> personHairColor = new TableColumn<>(bundle.getString("personHairColor"));
        personHairColor.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOwner().getHairColor()));

        personHairColor.setCellFactory(ComboBoxTableCell.forTableColumn(Color.RED, Color.GREEN, Color.WHITE));
        personHairColor.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.getOwner().setHairColor(event.getNewValue());
            sendUpdatedProduct(product);
        });

        // Национальность (не редактируем)
        TableColumn<Product, Country> personNationality = new TableColumn<>(bundle.getString("personNationality"));
        personNationality.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOwner().getNationality()));
        personNationality.setCellFactory(ComboBoxTableCell.forTableColumn(Country.values()));
        personNationality.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.getOwner().setNationality(event.getNewValue());
            sendUpdatedProduct(product);
        });

        // Координаты локации (не редактируем)
        TableColumn<Product, Long> personLocationWidth = new TableColumn<>(bundle.getString("locationWidthColumn"));
        personLocationWidth.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getOwner().getLocation().getX()).asObject());
        personLocationWidth.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        personLocationWidth.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.getOwner().getLocation().setX(event.getNewValue());
            sendUpdatedProduct(product);
        });

        TableColumn<Product, Double> personLocationHeight = new TableColumn<>(bundle.getString("locationHeightColumn"));
        personLocationHeight.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getOwner().getLocation().getY()).asObject());
        personLocationHeight.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        personLocationHeight.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            try {
                product.getOwner().getLocation().setY(event.getNewValue());
            } catch (NullValueException e) {
                throw new RuntimeException(e);
            }
            sendUpdatedProduct(product);
        });

        TableColumn<Product, Double> personLocationDepth = new TableColumn<>(bundle.getString("locationDepthColumn"));
        personLocationDepth.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getOwner().getLocation().getX()).asObject());
        personLocationDepth.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        personLocationDepth.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.getOwner().getLocation().setZ(event.getNewValue());
            sendUpdatedProduct(product);
        });

        return new ArrayList<>(Arrays.asList(
                idColumn,
                productNameColumn,
                coordinatesXColumn,
                coordinatesYColumn,
                creationDate,
                productPriceColumn,
                unitOfMeasureColumn,
                personName,
                personHeight,
                personEyeColor,
                personHairColor,
                personNationality,
                personLocationWidth,
                personLocationHeight,
                personLocationDepth
        ));
    }
}
