package com.okkun;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public abstract class editor_window extends Pane{
    
    public final int WIDTH;
    public final int HEIGHT;
    protected Canvas canvas;
    protected GraphicsContext gc; 
    protected AnimationTimer loop;

    public editor_window(int w, int h, String color) {
        this.setOnMouseMoved(e -> requestFocus());
        this.WIDTH = w;
        this.HEIGHT = h;
        setPrefSize(WIDTH, HEIGHT);
        setStyle("-fx-background-color: " + color);
        initializeObjects();
    }

    protected void initializeObjects() {
        
        this.canvas = new Canvas(WIDTH, HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
    }

    public void startLoop() {

        loop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if(lastUpdate == 0) {
                    lastUpdate = now;
                }
                long elapsedNanos = now - lastUpdate;
                update(elapsedNanos*1e-9);
                lastUpdate = now;
                redraw();
            }
        };
        loop.start();
    }
    
    public void pauseLoop(){ loop.stop(); }
    public void resumeLoop() { loop.start(); }
    
    private void redraw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        draw();
    }
    
    protected abstract void update(double elapsedSeconds);
    protected abstract void draw();
    
}
