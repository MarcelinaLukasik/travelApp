package com.example.travel_app_client;

import javafx.fxml.FXML;
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

            // Update the UI with the offers once they are fetched
            Platform.runLater(() -> {
                if (offers != null ) {
                    for (Offer offer : offers) {
                        createOffer(offer); // Create UI elements for each offer
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

            Image offerImage = new Image(imageUrl);  // Load image directly from the URL
            offerImageView.setImage(offerImage);
            offerImageView.setFitWidth(200);
            offerImageView.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
            offerImageView.setImage(new Image("default-image.jpg"));
        }

        Button offerButton = new Button("View Offer");
        offerButton.setStyle(
                "-fx-background-color: #1E3A8A; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        offerButton.setOnAction(e -> handleOfferClick(offer));

        // Create a VBox for the label and button to be placed vertically on the right
        VBox rightVBox = new VBox(10, offerLabel, offerButton);
        rightVBox.setStyle("-fx-padding: 10; -fx-alignment: center-left;");

        // Create an HBox to arrange the image on the left and the label/button on the right
        HBox offerBox = new HBox(10, offerImageView, rightVBox);
        offerBox.setStyle("-fx-padding: 10; -fx-border-width: 1; -fx-border-color: gray;");

        // Add the individual offer HBox to the main VBox
        offersVBox.getChildren().add(offerBox);
    }

    private void handleOfferClick(Offer offer) {
        System.out.println("Viewing offer");
    }
}


