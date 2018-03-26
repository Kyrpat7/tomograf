package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    ImageView sinogram;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Tomograph");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();



//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Group group = new Group();
//        Scene scene = new Scene(group);
//        scene.setFill(Color.GRAY);
//        primaryStage.setScene(scene);
//
//        MatOfByte bytes = new MatOfByte();
//        Mat sinogram_mat = SinogramUtils.createSinogram("Shepp_logan.png", 1, 270, 250);
//        System.out.println(sinogram_mat.dump());
//        Highgui.imencode(".bmp", sinogram_mat, bytes);
//        //System.out.println(bytes.dump());
//        Image sin = new Image(new ByteArrayInputStream(bytes.toArray()), 300, 300, false, false);
//        sinogram = new ImageView(sin);
//        group.getChildren().add(sinogram);
//        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
