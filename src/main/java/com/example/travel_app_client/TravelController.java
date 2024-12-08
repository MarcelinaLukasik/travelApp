package com.example.travel_app_client;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.application.Platform;

public class TravelController {
    @FXML
    private VBox offersVBox;
    @FXML
    private Label statusLabel;


    private SocketClient socketClient;


    public void initialize() {

        socketClient = new SocketClient("localhost", 9876);

        new Thread(() -> {
            List<Offer> offers = socketClient.fetchOffers();


            Platform.runLater(() -> {
                if (offers != null ) {
                    for (Offer offer : offers) {
                        createOffer(offer);
                    }
                    statusLabel.setText("Available offers:");
                } else {
                    statusLabel.setText("No offers available");
                }
            });
        }).start();
    }
    private void createOffer(Offer offer) {

        Label offerLabel = new Label(
                 "Country: " + offer.getCountry() + " - City: " + offer.getCity() +
                    "\nHotel: " + offer.getHotelName() +
                    "\nPrice: $" + offer.getPrice() +
                    "\nStart Date: " + offer.getStartDate() +
                    "\nEnd Date: " + offer.getEndDate()
        );

        ImageView offerImageView = new ImageView();
        String imageUrl = "https://www.europol.com.pl/Uploads/stella/guidebook/country/large/FR.png";

        try {

            Image offerImage = new Image(imageUrl);
            offerImageView.setImage(offerImage);
            offerImageView.setFitWidth(200);
            offerImageView.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
            offerImageView.setImage(new Image("default-image.jpg"));
        }

        Button offerButton = new Button("Book offer");
        offerButton.setStyle(
                "-fx-background-color: #1E3A8A; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        offerButton.setOnAction(e -> handleOfferClick(offer));


        VBox rightVBox = new VBox(10, offerLabel, offerButton);
        rightVBox.setStyle("-fx-padding: 10; -fx-alignment: center-left;");


        HBox offerBox = new HBox(10, offerImageView, rightVBox);
        offerBox.setStyle("-fx-padding: 10; -fx-border-width: 1; -fx-border-color: gray;");

        offersVBox.getChildren().add(offerBox);

    }

    private void handleOfferClick(Offer offer)
    {
        System.out.println("Book offer");

        Stage newWindow = new Stage();
        newWindow.setTitle("New Window");

        VBox secondaryLayout = new VBox(10);
        secondaryLayout.setPadding(new Insets(10));

        Label insuranceLabel = new Label("Insurance Details: " + offer.getInsurance());
        insuranceLabel.setWrapText(true);
        insuranceLabel.setMaxWidth(200);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e ->
                {
                    socketClient.bookOffer(offer.getId());
                    newWindow.close();
                }

        );

        secondaryLayout.getChildren().addAll(insuranceLabel, closeButton);

        Scene secondScene = new Scene(secondaryLayout, 350, 250);
        newWindow.setScene(secondScene);

        newWindow.show();


    }
}


