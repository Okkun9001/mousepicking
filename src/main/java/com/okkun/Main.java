package com.okkun;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main  extends Application {
    public static void main(String[] args) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        World world = new World(1200, 800);
        world.startLoop();
        Scene scene = new Scene(world);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mouse Picking");
        primaryStage.show();

    }
}