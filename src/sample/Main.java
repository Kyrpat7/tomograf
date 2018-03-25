package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import tomograf.SinogramUtils;

import java.io.ByteArrayInputStream;

public class Main extends Application {

    ImageView sinogram;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        primaryStage.setTitle("Hello World");
        Group group = new Group();
        Scene scene = new Scene(group);
        scene.setFill(Color.GRAY);
        primaryStage.setScene(scene);

        MatOfByte bytes = new MatOfByte();
        Mat sinogram_mat = SinogramUtils.createSinogram("Kwadraty2.jpg", 1, 120, 250);
        System.out.println(sinogram_mat.dump());
        Highgui.imencode(".bmp", sinogram_mat, bytes);
        //System.out.println(bytes.dump());
        Image sin = new Image(new ByteArrayInputStream(bytes.toArray()), 300, 300, false, false);
        sinogram = new ImageView(sin);
        group.getChildren().add(sinogram);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
