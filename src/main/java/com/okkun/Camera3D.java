package com.okkun;

import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector3D;

public class Camera3D {
    
    //Window Properties
    private int WIDTH;
    private int HEIGHT;
    //Camera Properties
    private Vector3D pos = new Vector3D(0, -5, -32); public Vector3D getPos() { return pos; }
    private Vector3D scale; public Vector3D getScale() { return scale; }    public void setScale(double s) { scale = new Vector3D(s*WIDTH, s*HEIGHT, 1); }
    private double fYaw = 0; public double getfYaw() { return fYaw; }
    private double fPitch = 0; public double getfPitch() { return fPitch; }
    private double fRoll = 0; public double getfRoll() { return fRoll; }
    private Vector3D vTarget = new Vector3D(0, 0, 1);
    private Vector3D vLookDir = new Vector3D(0, 0, 1);  public Vector3D getLookDir() { return vLookDir; }

    //CAM WORLD BOUNDARIES
    private static final Vector3D BOTTOM_BOUNDS = new Vector3D(-250, 50, -250);
    private static final Vector3D TOP_BOUNDS = new Vector3D(250, -50, 250);

    // CLIPPING AGAINST CAMERA PLANE
    public static final Vector3D clipDist = new Vector3D(0, 0, 0.1);    public Vector3D getClipDist() { return clipDist; }
    public static final Vector3D clipPlane = new Vector3D(0, 0, 1);     public Vector3D getClipPlane() { return clipPlane; }

    //MATRICES
    private static Matrix4x4 matView;   public Matrix4x4 getViewMatrix() { return matView; }
    private static Matrix4x4 matInvView;    public Matrix4x4 getInvViewMatrix() { return matInvView; }
    private static Matrix4x4 projMatrix;    public Matrix4x4 getProjMatrix() { return projMatrix; }
    private static Matrix4x4 unprojMat;     public Matrix4x4 getUnprojMatrix() { return unprojMat; }

    //Checks
    private boolean viewUpdate = false;
    private boolean targetUpdate = false;

    public Camera3D(World w, int FOV, double near, double far, Vector3D pos) {
        if(pos !=  null) this.pos = pos;
        this.WIDTH = w.WIDTH;
        this.HEIGHT = w.HEIGHT;
        this.scale = new Vector3D(0.5*WIDTH, 0.5*HEIGHT, 1);
        //Instantiate transformations
        projMatrix = Matrix4x4.projection(FOV, (double)w.HEIGHT/(double)w.WIDTH, near, far);
        if(projMatrix == null) throw new RuntimeException("Projection Matrix is null");
        unprojMat = Matrix4x4.projectionInverse(projMatrix);
        if(unprojMat == null) throw new RuntimeException("Projection Inverse Matrix is null");
        //Create Camera Transform
        matInvView = Matrix4x4.pointAt(this);
        matView = Matrix4x4.viewMatrix(matInvView);
        if(matView == null) throw new RuntimeException("Camera Transform is null");
    }

    public void update(){
        if(targetUpdate){
            Matrix4x4 matRot = Matrix4x4.rotateX(fPitch);
            vLookDir = new Vector3D(0, 0, 1).multiplyByMatrix(matRot);
            Matrix4x4 matRotY = Matrix4x4.rotateY(fYaw);
            vLookDir = vLookDir.multiplyByMatrix(matRotY).normalize();
            vTarget = pos.add(vLookDir);
        }
        
        if(viewUpdate || targetUpdate){
            //Create Camera Transform
            matInvView = Matrix4x4.pointAt(this);
            matView = Matrix4x4.viewMatrix(matInvView);
            if(matView == null) throw new RuntimeException("Camera Transform is null");
        }

        targetUpdate = false;
        viewUpdate = false;
    }

    public void move(Vector3D dir, double dist) {
        Vector3D newPos = pos.add(dir.multiply(dist));
        if(inBounds(newPos)){
            pos = newPos;
            viewUpdate = true;
        }
    }

    public boolean inBounds(Vector3D newPos){
        if(newPos.x < BOTTOM_BOUNDS.x || newPos.x > TOP_BOUNDS.x){
            System.out.println("Out of X bounds: " + newPos.x);
            return false; }
        if(newPos.y > BOTTOM_BOUNDS.y || newPos.y < TOP_BOUNDS.y){ 
            System.out.println("Out of Y bounds: " + newPos.y);
            return false; }
        if(newPos.z < BOTTOM_BOUNDS.z || newPos.z > TOP_BOUNDS.z){ 
            System.out.println("Out of Z bounds: " + newPos.z);
            return false; }
        return true;
    }

    public void rotateY(double d) {
        fYaw += d;
        targetUpdate = true;
    }

    public void pitch(double d) {
        fPitch += d;
        targetUpdate = true;
    }

    public Vector3D getTarget() {

        
        return vTarget;
    }
    
    public void setProjection(int camFOV, double near, double far) {
        projMatrix = Matrix4x4.projection(camFOV, (double)HEIGHT/(double)WIDTH, near, far);
        unprojMat = Matrix4x4.projectionInverse(projMatrix);
    }
    public void reset() {
        this.pos = new Vector3D(0, -5, -32);
        this.vLookDir = new Vector3D(0, 0, 1);
        this.vTarget = new Vector3D(0, 0, 1);
        this.fYaw = 0;
        this.fPitch = 0;

    }

    
}
