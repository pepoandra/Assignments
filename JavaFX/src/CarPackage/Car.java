/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CarPackage;

import assignment.ConstantsInterface;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author Alex
 */
public class Car extends Group implements ConstantsInterface {

    protected String DriverName;
    protected String ImagePath;
    protected double Duration_value;
    private int CarNumber;
    public ImageView Car;
    public TranslateTransition bobbing;

    public Car(String DriverName, String ImagePath, int CarNumber) {

        this.DriverName = DriverName;
        this.ImagePath = ImagePath;
        this.CarNumber = CarNumber;

        Car = new ImageView(new Image(getClass().getResourceAsStream(ImagePath)));

        this.getChildren().add(Car);

        TranslateTransition bobbing = new TranslateTransition(Duration.millis(DurationBobbing1), this);
        TranslateTransition bobbing2 = new TranslateTransition(Duration.millis(DurationBobbing2), this);

        bobbing.setFromX(BobbingFromX);
        bobbing.setToX(BobbingToX);
        bobbing.setCycleCount(Timeline.INDEFINITE);
        bobbing.setAutoReverse(true);
        bobbing2.setByY(BobbingByX);
        bobbing2.setCycleCount(Timeline.INDEFINITE);

        bobbing2.setAutoReverse(true);

        bobbing2.play();
        bobbing.play();

    }

    public void StartAnimation(double vDuration) {

        TranslateTransition transition = new TranslateTransition(Duration.millis(vDuration), this);

        transition.setFromX(CarFrom);
        transition.setToX(CarTo);
        transition.setCycleCount(vCycle);

        transition.play();

    }

    /**
     * @return the DriverName
     */
    public String getDriverName() {
        return DriverName;
    }

    /**
     * @param DriverName the DriverName to set
     */
    public void setDriverName(String DriverName) {
        this.DriverName = DriverName;
    }

    /**
     * @return the ImagePath
     */
    public String getImagePath() {
        return ImagePath;
    }

    /**
     * @param ImagePath the ImagePath to set
     */
    public void setImagePath(String ImagePath) {
        this.ImagePath = ImagePath;
    }

    /**
     * @return the Duration
     */
    public double getDuration() {
        return Duration_value;
    }

    /**
     * @param Duration the Duration to set
     */
    public void setDuration(int Duration) {
        this.Duration_value = Duration;
    }

    /**
     * @return the CarNumber
     */
    public int getCarNumber() {
        return CarNumber;
    }

    /**
     * @param CarNumber the CarNumber to set
     */
    public void setCarNumber(int CarNumber) {
        this.CarNumber = CarNumber;
    }

}
