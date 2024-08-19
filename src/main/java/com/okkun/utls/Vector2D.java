package com.okkun.utls;

import javafx.scene.canvas.GraphicsContext;

public class Vector2D{
    
    public double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v){
        this.x = v.x;
        this.y = v.y;
    }

    public static Vector2D Null(){ return new Vector2D(0, 0); }

    public Vector2D getNormal(){
        if(y == 0){ return new Vector2D(0, x); }
        return new Vector2D(-y, x);
    }

    public double getMagnitude(){
        return Math.sqrt(x*x + y*y);
    }

    public double magnitudeSquared(){
        return (x*x) + (y*y);
    }

    public Vector2D normalize(){
        double magnitude = getMagnitude();
        if(magnitude == 0){ return new Vector2D(0, 0); }
        return new Vector2D(x/magnitude, y/magnitude);
    }

    public double dotProduct(Vector2D v){
        return (x*v.x) + (y*v.y);
    }

    public double crossProduct(Vector2D v) {
        return (this.x*v.y) - (this.y*v.x);
    }

    public Vector2D multiply(double scalar){
        return new Vector2D(x*scalar, y*scalar);
    }

    public Vector2D add(Vector2D v){
        return new Vector2D(x + v.x, y + v.y);
    }

    public Vector2D subtract(Vector2D v){
        return new Vector2D(x - v.x, y - v.y);
    }

    public double getAngle(Vector2D edgeOpp) {
        return Math.acos(this.dotProduct(edgeOpp)/(this.getMagnitude()*edgeOpp.getMagnitude()));
    }

    public static double Distance(Vector2D v1, Vector2D v2){
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public static double DistanceSquared(Vector2D v1, Vector2D v2){
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        return dx*dx + dy*dy;
    }
    
    @Override
    public String toString() { return "Vector2D{" + "x=" + x + ", y=" + y + '}'; }

    public void draw(GraphicsContext gc, double x, double y) {
        gc.strokeLine(x, y, x + this.x, y + this.y);
    }

    private boolean equals(Vector2D v){ return Math2D.equal(this.x, v.x, Math2D.EPSILON)&& Math2D.equal(this.y, v.y, Math2D.EPSILON); }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { throw new NullPointerException("Object is null"); }
        if (getClass() == obj.getClass()) { return this.equals((Vector2D)obj); }
        return false; 
    }

    public int XYCoordsBigger_Than(Vector2D v){
        if(this.x > v.x && this.y > v.y){ return 1; }
        if(this.x < v.x && this.y < v.y){ return -1; }
        return 0;
    }

}
