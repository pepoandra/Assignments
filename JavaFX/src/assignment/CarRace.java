/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import CarPackage.Car;
import static assignment.ConstantsInterface.MaxDurationCar;
import static assignment.ConstantsInterface.MinDurationCar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Alex
 */
public class CarRace extends Application implements ConstantsInterface {

    @Override
    public void start(final Stage primaryStage) {
        VBox CarHolder = new VBox(10);

        final Random rand = new Random();

        final Car Marx = new Car("Karl Marx", "/assignment/karl_marx_car.png", 1);
        final Car Engels = new Car("Friedrich Engels", "/assignment/engels_car.png", 2);
        final Car Lenin = new Car("Vladimir Lenin", "/assignment/lenin_car.png", 3);
        final Car Walter_Benjamin = new Car("Walter Benjamin", "/assignment/walter_benjamin_car.png", 4);

        HBox ButtonHolder = new HBox(10);

        Button exit = new Button("Back");
        Button btn = new Button("GO");
        btn.setPrefWidth(100);
        exit.setPrefWidth(100);

        ButtonHolder.setAlignment(Pos.CENTER);

        ButtonHolder.getChildren().addAll(btn, exit);

        CarHolder.getChildren().addAll(Marx, Engels, Lenin, Walter_Benjamin);
        CarHolder.setPadding(new Insets(60, 60, 0, 0));

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                int RandomDuration1 = rand.nextInt((MaxDurationCar - MinDurationCar) + 1) + MinDurationCar;
                int RandomDuration2 = rand.nextInt((MaxDurationCar - MinDurationCar) + 1) + MinDurationCar;
                int RandomDuration3 = rand.nextInt((MaxDurationCar - MinDurationCar) + 1) + MinDurationCar;
                int RandomDuration4 = rand.nextInt((MaxDurationCar - MinDurationCar) + 1) + MinDurationCar;

                Marx.StartAnimation(RandomDuration1);
                Lenin.StartAnimation(RandomDuration2);
                Engels.StartAnimation(RandomDuration3);
                Walter_Benjamin.StartAnimation(RandomDuration4);

            }

        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Application menu = new MainMenu();
                Stage anotherStage = new Stage();
                try {
                    menu.start(anotherStage);
                    primaryStage.close();
                } catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        btn.setStyle(style_button);
        exit.setStyle(style_button);

        StackPane root = new StackPane();
        VBox start = new VBox();

        BorderPane root2 = new BorderPane();
        ImageView startLine = new ImageView(new Image(getClass().getResourceAsStream("/assignment/start_line.png")));
        start.getChildren().add(startLine);

        root.getChildren().addAll(start, CarHolder, ButtonHolder);

        root2.setLeft(root);
        root2.setBottom(ButtonHolder);

        root2.setStyle("-fx-background-image: url('assignment/track_field.png');");

        Scene scene = new Scene(root2, 985, 620);

        primaryStage.getIcons().add(new javafx.scene.image.Image(icon_path));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Assignment 4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
