package gui.program;

import javafx.scene.control.Button;
import models.*;

public class UpdateWindow extends AddWindow{

    private User user;

    public UpdateWindow(User user) {
        super();
        this.user = user;
    }

    public void setFields(Product product) {
        productNameField.setText(product.getName());
        productPriceField.setText(String.valueOf(product.getPrice()));
        unitOfMeasureComboBox.setValue(product.getUnitOfMeasure());
        personNameField.setText(product.getOwner().getName());
        personHeightField.setText(String.valueOf(product.getOwner().getHeight()));
        nationalityComboBox.setValue(product.getOwner().getNationality());
        eyeColorComboBox.setValue(product.getOwner().getEyeColor());
        hairColorComboBox.setValue(product.getOwner().getHairColor());
        locationWidthField.setText(String.valueOf(product.getOwner().getLocation().getX()));
        locationHeightField.setText(String.valueOf(product.getOwner().getLocation().getY()));
        locationDepthField.setText(String.valueOf(product.getOwner().getLocation().getZ()));
        coordinatesXField.setText(String.valueOf(product.getCoordinates().getX()));
        coordinatesYField.setText(String.valueOf(product.getCoordinates().getY()));
        checkField();
        addButton.setText(bundle.getString("updateButton"));
        Button deleteButton = new Button(bundle.getString("deleteButton"));
        deleteButton.setPrefWidth(150);
        stage.setAlwaysOnTop(true);
        show(user);
    }

}
