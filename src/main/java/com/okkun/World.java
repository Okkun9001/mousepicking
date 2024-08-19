package com.okkun;

import com.okkun.utls.InputHandler;
import com.okkun.utls.Vector2D;
import com.okkun.utls.Vector3D;

public class World extends editor_window{

    private InputHandler inputHandler;
    private Renderer3D renderer;
    private boolean cameraLock = false;

    //MOUSE INPUT
    Vector2D mousePos;

    //CAMERA SETTINGS
    private double scale = 0.3;
    private int camFOV = 120;
    private double near = 0.1;
    private double far = 1000;
    //update checks
    private boolean projectionUpdate = false;
    private boolean scaleUpdate = false;

    Camera3D camera = new Camera3D(this, camFOV, near, far, null);
    
    public World(int w, int h) {
        super(w, h, "#1a232e");
        inputHandler = new InputHandler(this);
        renderer = new Renderer3D(w, h, camera);
    }

    @Override
    protected void update(double elapsedSeconds) {

        Vector3D vForward = camera.getLookDir();
        Vector3D vLeft = new Vector3D(-vForward.z, vForward.y, vForward.x);

        //CHECK FOR CAMERA UPDATES
        if(projectionUpdate){
            camera.setProjection(camFOV, near, far);
            projectionUpdate = false;
        }
        if(scaleUpdate){
            camera.setScale(scale);
            renderer.rescale();
            scaleUpdate = false;
        }

        if(inputHandler.LMB_Clicked()){

            mousePos = inputHandler.getMousePosition();
            mousePos.x = (mousePos.x - this.WIDTH/2)/this.WIDTH;
            mousePos.y = (mousePos.y-this.HEIGHT/2)/this.HEIGHT;
            inputHandler.setLMB_Clicked(false);

        }

        if(!cameraLock){
            handleCameraMovement(elapsedSeconds, vForward, vLeft);
        }

        camera.update();
        renderer.update(camera, inputHandler.getMousePosition());
    }

    public void handleCameraMovement(double dt, Vector3D vForward, Vector3D vLeft) {

        this.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if(cameraLock == false) camera.move(new Vector3D(0, -1, 0), delta * dt);
        });
        
        //LEFT
        if(inputHandler.A_Pressed()) {
            camera.move(vLeft, -8 * dt);
        }
        //RIGHT
        if(inputHandler.D_Pressed()) {
            camera.move(vLeft,8 * dt);
        }
        //FORWARD
        if(inputHandler.W_Pressed()){
            camera.move(vForward,8 * dt);
        }
        //BACKWARD
        if(inputHandler.S_Pressed()){
            camera.move(vForward,-8 * dt);
        }
        
        //ROTATE LEFT
        if(inputHandler.Q_Pressed()){
            camera.rotateY(2.0 * dt);
        }
        //ROTATE RIGHT
        if(inputHandler.E_Pressed()){
            camera.rotateY(-2.0 * dt);
        }

        if(inputHandler.T_Pressed()){
            camera.pitch(-2.0 * dt);
        }

        if(inputHandler.G_Pressed()){
            camera.pitch(2.0 * dt);
        }
    }

    @Override
    protected void draw() {

        renderer.draw(this.gc); 
    }

    public void lockCamera( boolean cameraLock){ this.cameraLock = cameraLock; }

    public void setFOV(int value) { 
        this.camFOV = value; 
        this.projectionUpdate = true;
    }

    public void setScale(double doubleValue) { 
        this.scale = doubleValue; 
        this.scaleUpdate = true;
    }

    public void resetCamera() {
        camera.reset();
    }

}

