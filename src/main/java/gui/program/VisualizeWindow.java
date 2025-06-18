package gui.program;

import gui.AbstractWindow;
import gui.main.AuthorizationWindow;
import gui.utils.CoordinatesView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Product;
import models.User;
import models.UserProducts;
import transfer.Request;
import transfer.Response;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class VisualizeWindow extends AbstractWindow {
    private static final int DEFAULT_SIZE = 700;
    private static final int CELL_SIZE = 25;
    private double offsetX = 0;
    private double offsetY = 0;
    private double dragStartX = 0;
    private double dragStartY = 0;
    private Stage stage = new Stage();
    private double scale = 1.0;
    private Image productImage = new Image(getClass().getResourceAsStream("/img/box.png"));
    private Map<CoordinatesView, Product> productMap = new HashMap<>();
    private List<UserProducts> userProducts = new ArrayList<>();
    private final Map<CoordinatesView, Double> animationProgress = new HashMap<>();
    private final Map<CoordinatesView, Long> animationStartTime = new HashMap<>();
    private final Set<CoordinatesView> animatedCoordinates = new HashSet<>();

    private User user;
    private Canvas canvas = new Canvas(DEFAULT_SIZE, DEFAULT_SIZE);
    private GraphicsContext graphics = canvas.getGraphicsContext2D();
    private Timeline animationTimeline;
    private final Map<Long, Image> recoloredImages = new HashMap<>();


    public VisualizeWindow() {
        super();
    }

    private void drawGrid(GraphicsContext gc, double width, double height) {
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        double scaledCellSize = CELL_SIZE * scale;
        double startX = -offsetX / scaledCellSize;
        double startY = -offsetY / scaledCellSize;
        int firstCol = (int) Math.floor(startX);
        int firstRow = (int) Math.floor(startY);
        int cols = (int) (width / scaledCellSize) + 2;
        int rows = (int) (height / scaledCellSize) + 2;

        for (int i = firstCol; i <= firstCol + cols; i++) {
            double x = i * scaledCellSize + offsetX;
            if (x >= 0 && x <= width) {
                gc.strokeLine(x, 0, x, height);
            }
        }
        for (int j = firstRow; j <= firstRow + rows; j++) {
            double y = j * scaledCellSize + offsetY;
            if (y >= 0 && y <= height) {
                gc.strokeLine(0, y, width, y);
            }
        }
    }

    private void mouseScroll(Group group, Canvas canvas, GraphicsContext graphics, Scene scene) {
        scene.setOnScroll(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            double worldX = (mouseX - offsetX) / scale;
            double worldY = (mouseY - offsetY) / scale;

            double zoomFactor = event.getDeltaY() > 0 ? 1.1 : 0.9;
            scale *= zoomFactor;
            scale = Math.max(0.1, Math.min(scale, 5.0));

            offsetX = mouseX - worldX * scale;
            offsetY = mouseY - worldY * scale;

            drawProducts(graphics, canvas.getWidth(), canvas.getHeight(), productMap);
        });
    }

    public void drawProducts(GraphicsContext gc, double width, double height, Map<CoordinatesView, Product> coordinatesViewProductMap) {
        gc.clearRect(0, 0, width, height);
        drawGrid(gc, width, height);

        double imgSize = CELL_SIZE * scale * 0.8;
        double imgOffset = CELL_SIZE * scale * 0.1;

        gc.setFill(Color.RED);

        long now = System.currentTimeMillis();

        boolean needRedraw = false;
        Map<CoordinatesView, Product> updatedMap = new HashMap<>();
        Set<Long> seenProductIds = new HashSet<>();
        for (Map.Entry<CoordinatesView, Product> entry : coordinatesViewProductMap.entrySet()) {
            seenProductIds.add(entry.getValue().getId());
            updatedMap.put(entry.getKey(), entry.getValue());
        }
        Iterator<Map.Entry<CoordinatesView, Product>> it = productMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<CoordinatesView, Product> oldEntry = it.next();
            Product oldProduct = oldEntry.getValue();
            if (!seenProductIds.contains(oldProduct.getId())) {
                it.remove();
                animationStartTime.remove(oldEntry.getKey());
                animatedCoordinates.remove(oldEntry.getKey());
            }
        }
        productMap.clear();
        productMap.putAll(updatedMap);
        Map<Long, UserProducts> userProductMap = userProducts.stream()
                .collect(Collectors.toMap(
                        UserProducts::getProductId,
                        up -> up,
                        (existing, replacement) -> existing
                ));
        for (Map.Entry<CoordinatesView, Product> entry : coordinatesViewProductMap.entrySet()) {
            CoordinatesView coord = entry.getKey();
            Product product = entry.getValue();
            UserProducts user = userProductMap.get(product.getId());
            if (user == null) continue;
            if (user.getProductId() == entry.getValue().getId()) {
                double x = coord.getX() * CELL_SIZE * scale + offsetX;
                double y = coord.getY() * CELL_SIZE * scale + offsetY;

                if (x + imgSize + imgOffset > 0 && x < width &&
                        y + imgSize + imgOffset > 0 && y < height) {

                    double opacity = 1.0;
                    if (animationStartTime.containsKey(coord) && !animatedCoordinates.contains(coord)) {
                        double elapsed = (now - animationStartTime.get(coord)) / 1000.0;
                        opacity = Math.min(1.0, elapsed);
                        if (opacity >= 1.0) {
                            animatedCoordinates.add(coord);
                            animationStartTime.remove(coord);
                        } else {
                            needRedraw = true;
                        }
                    }

                    if (opacity < 0.01) continue;

                    gc.save();
                    gc.translate(x + imgOffset, y + imgOffset + imgSize);
                    gc.scale(1, -1);
                    Image colored = recoloredImages.computeIfAbsent(
                            user.getUserId(),
                            uid -> recolorImage(productImage, hexToColor(uid))
                    );

                    gc.setGlobalAlpha(opacity);
                    gc.drawImage(colored, 0, 0, imgSize, imgSize);
                    gc.setGlobalAlpha(1.0);
                    gc.restore();
                }
            }
        }
        if (!needRedraw && animationTimeline != null) {
            animationTimeline.stop();
        }

    }


    public static Color hexToColor(Long userId) {
        long id = Math.abs(userId);
        float hue = (id * 57) % 360;
        float saturation = 0.7f;
        float brightness = 0.9f;
        return Color.hsb(hue, saturation, brightness);
    }
    public static Image recolorImage(Image original, Color newColor) {
        int width = (int) original.getWidth();
        int height = (int) original.getHeight();
        WritableImage coloredImage = new WritableImage(width, height);
        PixelReader reader = original.getPixelReader();
        PixelWriter writer = coloredImage.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = reader.getColor(x, y);
                double alpha = pixel.getOpacity();
                if (alpha > 0.1 && pixel.getBrightness() < 0.7) {
                    writer.setColor(x, y, new Color(
                            newColor.getRed(),
                            newColor.getGreen(),
                            newColor.getBlue(),
                            alpha
                    ));
                } else {
                    writer.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }
        return coloredImage;
    }

    public void visualize(TreeSet<Product> collection, User user) {
        this.user = user;
        try {
            client.open();
            Request request = new Request();
            request.setCommand("getOwners");
            request.setUser(user);
            client.sendRequest(request);
            Response response = (Response) client.receiveResponse(5000);
            this.userProducts = response.getUserProducts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        offsetX = canvas.getWidth() / 2;
        offsetY = canvas.getHeight() / 2;
        canvas.setScaleY(-1);
        if(collection != null && !collection.isEmpty()) {
            for (var el : collection) {
                CoordinatesView coords = new CoordinatesView(
                        el.getCoordinates().getX().doubleValue(),
                        el.getCoordinates().getY().doubleValue());
                if (!productMap.containsKey(coords)) {
                    productMap.put(coords, el);
                    animationStartTime.put(coords, System.currentTimeMillis());
                    animatedCoordinates.remove(coords);
                }
            }
        } else {
            productMap = new HashMap<>();
        }
        Tooltip tooltip = new Tooltip();
        tooltip.setAutoHide(true);
        tooltip.setShowDelay(javafx.util.Duration.millis(100));
        canvas.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            boolean found = false;
            double imgSize = CELL_SIZE * scale * 0.8;
            double imgOffset = CELL_SIZE * scale * 0.1;

            for(Map.Entry<CoordinatesView, Product> entry: productMap.entrySet()) {
                double cellX = entry.getKey().getX() * CELL_SIZE * scale + offsetX;
                double cellY = entry.getKey().getY() * CELL_SIZE * scale + offsetY;
                double imgX = cellX + imgOffset;
                double imgY = cellY + imgOffset;

                if (mouseX >= imgX && mouseX <= imgX + imgSize &&
                        mouseY >= imgY && mouseY <= imgY + imgSize) {
                    tooltip.setText(bundle.getString("tooltipCoordinatesStart") + " (" + entry.getKey().getX() + ", " +
                            entry.getKey().getY() + ")\n" +
                            bundle.getString("tooltipProductSart") + ": " + entry.getValue().getName());
                    Tooltip.install(canvas, tooltip);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Tooltip.uninstall(canvas, tooltip);
            }
        });

        canvas.setOnMousePressed(event -> {
            dragStartX = event.getX();
            dragStartY = event.getY();
        });

        canvas.setOnMouseDragged(event -> {
            double deltaX = event.getX() - dragStartX;
            double deltaY = event.getY() - dragStartY;
            offsetX += deltaX;
            offsetY += deltaY;
            dragStartX = event.getX();
            dragStartY = event.getY();
            drawProducts(graphics, canvas.getWidth(), canvas.getHeight(), productMap);
        });

        canvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            double imgSize = CELL_SIZE * scale * 0.8;
            double imgOffset = CELL_SIZE * scale * 0.1;
            for(Map.Entry<CoordinatesView, Product> entry: productMap.entrySet()) {
                double cellX = entry.getKey().getX() * CELL_SIZE * scale + offsetX;
                double cellY = entry.getKey().getY() * CELL_SIZE * scale + offsetY;
                double imgX = cellX + imgOffset;
                double imgY = cellY + imgOffset;
                if (mouseX >= imgX && mouseX <= imgX + imgSize &&
                        mouseY >= imgY && mouseY <= imgY + imgSize) {
                    try {
                        client.open();
                        Request request = new Request();
                        request.setCommand("getOwner");
                        request.setUser(user);
                        request.setProduct(entry.getValue());
                        client.sendRequest(request);
                        Response response = (Response) client.receiveResponse(5000);
                        if(user.getUsername().equals(response.getResponse())) {
                            UpdateWindow updateWindow = new UpdateWindow(user);
                            updateWindow.setFields(entry.getValue());
                            updateWindow.addButton.setOnAction(event1 -> {
                                try {
                                    long id = entry.getValue().getId();
                                    Product product = updateWindow.setValues();
                                    product.setId(id);
                                    Request request1 = new Request();
                                    request1.setCommand("update_id" + " " + product.getId());
                                    request1.setUser(user);
                                    request1.setProduct(product);
                                    this.client.sendRequest(request1);
                                    Response response1 = (Response) this.client.receiveResponse(5000);
                                    if(response1 != null) {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle(bundle.getString("updateButton"));
                                        alert.setHeaderText(bundle.getString("succesUpdate"));
                                        alert.initOwner(this.stage);
                                        alert.initModality(Modality.APPLICATION_MODAL);
                                        alert.showAndWait();
                                        updateWindow.stage.close();
                                    }
                                } catch (IOException | ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            UpdateWindow showInfo = new UpdateWindow(user);
                            showInfo.addButton.setVisible(false);
                            showInfo.setFields(entry.getValue());
                            showInfo.coordinatesXField.setEditable(false);
                            showInfo.locationDepthField.setEditable(false);
                            showInfo.locationHeightField.setEditable(false);
                            showInfo.locationWidthField.setEditable(false);
                            showInfo.personHeightField.setEditable(false);
                            showInfo.personNameField.setEditable(false);
                            showInfo.productPriceField.setEditable(false);
                            showInfo.productNameField.setEditable(false);
                            showInfo.coordinatesYField.setEditable(false);
                            showInfo.defaultAdd.setDisable(true);
                            showInfo.addIfMax.setDisable(true);
                            showInfo.addIfMin.setDisable(true);
                            showInfo.show(user);
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
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        });

        Group mainGroup = new Group(canvas);
        Scene scene = new Scene(mainGroup, DEFAULT_SIZE, DEFAULT_SIZE);
        mouseScroll(mainGroup, canvas, graphics, scene);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            drawProducts(graphics, canvas.getWidth(), canvas.getHeight(), productMap);
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            drawProducts(graphics, canvas.getWidth(), canvas.getHeight(), productMap);
        });

        scene.getStylesheets().add(AuthorizationWindow.class.getResource("/styles/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(bundle.getString("visualizeButton"));
        stage.show();
        animationTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), e -> drawProducts(graphics, canvas.getWidth(), canvas.getHeight(), productMap))
        );
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();



    }

    public void update(TreeSet<Product> collection, List<UserProducts> userProducts) {
        Set<CoordinatesView> newCoords;
        if(collection != null && !collection.isEmpty()) {
            newCoords = collection.stream()
                    .map(p -> new CoordinatesView(p.getCoordinates().getX().doubleValue(),
                            p.getCoordinates().getY().doubleValue()))
                    .collect(Collectors.toSet());
        } else {
            newCoords = new HashSet<>();
        }
        productMap.keySet().removeIf(coord -> !newCoords.contains(coord));
        if(collection != null && !collection.isEmpty()) {
            for (var el : collection) {
                CoordinatesView coords = new CoordinatesView(
                        el.getCoordinates().getX().doubleValue(),
                        el.getCoordinates().getY().doubleValue());
                if (!productMap.containsKey(coords)) {
                    productMap.put(coords, el);
                    animationStartTime.put(coords, System.currentTimeMillis());
                    animatedCoordinates.remove(coords);
                }
            }
        }
        if(userProducts != null && !userProducts.isEmpty())
            this.userProducts = userProducts;
        else {
            this.userProducts = new ArrayList<>();
        }
        if (!animationStartTime.isEmpty() && (animationTimeline != null && animationTimeline.getStatus() != Animation.Status.RUNNING)) {
            animationTimeline.play();
        }
        drawProducts(graphics, canvas.getWidth(), canvas.getHeight(), productMap);
    }
}