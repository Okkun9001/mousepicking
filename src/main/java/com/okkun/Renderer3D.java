package com.okkun;

import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector2D;
import com.okkun.utls.Vector3D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer3D {

    private int WIDTH, HEIGHT;   public int getWIDTH() { return WIDTH; }     public int getHEIGHT() { return HEIGHT; }

    //GROUND
    private Ground ground;

    //3D canvas
    private MousePicker mousePicker;

    private Vector3D mouseInWorld;

    //PROJECTION
    private static Vector3D vCamera;
    public static Vector3D scale;     private boolean rescale = false;    public void rescale() {rescale = true;}
    public static Vector3D clipDist;
    public static Vector3D clipPlane;
    private Matrix4x4 matView;
    private Matrix4x4 projMatrix;

    //Create final transform and scale into view
    public static final Vector3D TRANSLATE = new Vector3D(1, 1, 0);

    Ray rayInWorld;

    
    public Renderer3D(int w, int h, Camera3D camera) {
        this.WIDTH = w;
        this.HEIGHT = h;
        clipDist = camera.getClipDist();
        clipPlane = camera.getClipPlane();
        scale = camera.getScale();
        
        //Construct mouse picker after projection matrix
        mousePicker = new MousePicker(this, camera);

        //Instantiate world objects
        this.ground = new Ground(500, 0 , 500);
    }

    //UPDATE PHASE
    public void update(Camera3D camera, Vector2D mousePos) {
        
        if(rescale){
            scale = camera.getScale();
            rescale = false;
        }

        //Update Camera
        vCamera = camera.getPos();
        matView = camera.getViewMatrix();
        projMatrix = camera.getProjMatrix();
        
        //Mouse-Ray
        mousePicker.update(camera.getInvViewMatrix(), mousePos);
        rayInWorld = mousePicker.getCurrentRay();

        if(rayInWorld!=null){
            System.out.println();
            System.out.println("Ray in world: " + rayInWorld.getDirection());
            mouseInWorld = mousePicker.intersectPlane(new Vector3D(0, -1, 0), vCamera.distance(new Vector3D(vCamera.x, 0, vCamera.z)));
            System.out.println("Mouse on ground: " + mouseInWorld);
        }
        
        //Update ground
        ground.update(matView, projMatrix, scale, vCamera);
    }
        
    //DRAW PHASE
    public void draw(GraphicsContext gc) {
        
        //Draw ground
        ground.draw(gc);

        //Draw pointer ray
        if(rayInWorld!=null){
            rayInWorld.draw(gc, projMatrix, matView);
        }

        if(mouseInWorld!=null){
            drawmouseInWorld(gc);
        }
        
    }

    private void drawmouseInWorld(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(mouseInWorld.x-5, mouseInWorld.y-5, 10, 10);
    }  
}
