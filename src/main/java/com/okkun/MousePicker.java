package com.okkun;

import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector2D;
import com.okkun.utls.Vector3D;

public class MousePicker {
    
    private Ray currentRay;    public Ray getCurrentRay() { return currentRay; }

    private Renderer3D renderer;
    private Camera3D camera;
    private Matrix4x4 projectionMatrix;
    private Matrix4x4 viewMatrix;

    public MousePicker(Renderer3D renderer, Camera3D camera) {
        this.renderer = renderer;
        this.camera = camera;
        this.projectionMatrix = camera.getProjMatrix();
        this.viewMatrix = Matrix4x4.identity();
    }

    public void update(Matrix4x4 viewMatrix, Vector2D mousePos) {
        if(mousePos != null){
            this.viewMatrix = viewMatrix;
            currentRay = calculateMouseRay(mousePos);
        }
    }

    private Ray calculateMouseRay(Vector2D mousePos) {
        
        Vector3D origin = camera.getPos();
        Vector3D mouse = new Vector3D(mousePos.x, mousePos.y, 1,0 );
        
        Vector2D normalizedCoords = getNormalizedDeviceCoords(mousePos);
        Vector3D clipCoords = new Vector3D(normalizedCoords.x, normalizedCoords.y, 1, 1);
        Vector3D eyeCoords = toEyeCoords(clipCoords);
        Vector3D worldRay = toWorldCoords(eyeCoords);
        return new Ray(origin, worldRay, mouse);
    }

    private Vector3D toWorldCoords(Vector3D eyeCoords) {
        
        Vector3D rayWorld = eyeCoords.multiplyByMatrix(viewMatrix);
        return rayWorld.normalize();
    }

    private Vector3D toEyeCoords(Vector3D clipCoords){
        Matrix4x4 invertedProjection = Matrix4x4.projectionInverse(projectionMatrix);
        Vector3D eyeCoords = clipCoords.multiplyByMatrix(invertedProjection);
        return new Vector3D(eyeCoords.x, eyeCoords.y, 1, 0);
    }


    private Vector2D getNormalizedDeviceCoords(Vector2D mousePos) {
        
        double x = (2.0 * mousePos.x / renderer.getWIDTH()) - 1;
        double y = (2.0 * mousePos.y / renderer.getHEIGHT()) -1;
        return new Vector2D(x, -y);
    }

    //Intersection with plane using vector math
    public Vector3D intersectPlane(Vector3D p_normal, double p_distance){

        double dot = p_normal.dot(currentRay.getOrigin()) + p_distance;
        double dot2 = p_normal.dot(currentRay.getDirection());
        if(dot2 == 0){System.out.println("No intersection");}
        double t = -dot / dot2;
        return currentRay.getPoint(t);
    }


    //Binary search for intersection
    public Vector3D binarySearch(int count, double start, double finish, Ray ray){
        double half = start + ((finish - start) / 2.0);
        if(count >= 100){
            Vector3D endPoint = ray.getPoint(0).add(ray.getPoint(half));
            return endPoint;
        }
        if(intersectionInRange(start, half, ray)){
            return binarySearch(count + 1, start, half, ray);
        }else{
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(double start, double half, Ray ray) {
        
        Vector3D startPoint = ray.getPoint(start);
        Vector3D endPoint = ray.getPoint(half);
        if(!isUnderGround(startPoint) && isUnderGround(endPoint)){
            return true;
        }else{
            return false;
        }
    }

    private boolean isUnderGround(Vector3D startPoint) {
        
        if(startPoint.y < 5){
            return true;
        }else{
            return false;
        }
    }
}
