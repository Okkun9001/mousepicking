package com.okkun.utls;

public class Vector3D {
    
    public double x, y, z, w;
    public double c;    public void setC(double c) { this.c = c; }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }
    public Vector3D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector3D(Vector3D v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public void Translate(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
    }
    public void Translate(Vector3D v){
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }
    
    public void Scale(double x, double y, double z){
        if(x!=1){ this.x *= x; }
        if(y!=1){ this.y *= y; }
        if(z!=1){ this.z *= z; }
    }
    public void Scale(Vector3D v){
        if(v.x!=1){ this.x *= v.x; }
        if(v.y!=1){ this.y *= v.y; }
        if(v.z!=1){ this.z *= v.z; }
    }

    public Vector3D cross(Vector3D v){
        double x = this.y*v.z - this.z*v.y;
        double y = this.z*v.x - this.x*v.z;
        double z = this.x*v.y - this.y*v.x;

        return new Vector3D(x, y, z);
    }

    public Vector3D add(Vector3D v){
        return new Vector3D(this.x+v.x, this.y+v.y, this.z+v.z);
    }

    public Vector3D subtract(Vector3D v){
        return new Vector3D(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public Vector3D normalize(){
        double length = Math.sqrt(x*x + y*y + z*z);
        return new Vector3D(x/length, y/length, z/length);
    }

    public double dot(Vector3D v){
        return this.x*v.x + this.y*v.y + this.z*v.z;
    }
    
    public Vector3D multiply(double scalar){
        return new Vector3D(this.x*scalar, this.y*scalar, this.z*scalar);
    }

    public Vector3D divide(double scalar){
        return new Vector3D(this.x/scalar, this.y/scalar, this.z/scalar);
    }

    public double distance(Vector3D v){
        double dx = this.x - v.x;
        double dy = this.y - v.y;
        double dz = this.z - v.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    public Vector3D  multiplyByMatrix(Matrix4x4 m){
        
        double xx = (this.x * m.m[0][0]) + (this.y * m.m[1][0]) + (this.z * m.m[2][0]) + (this.w * m.m[3][0]);
        double yy = (this.x * m.m[0][1]) + (this.y * m.m[1][1]) + (this.z * m.m[2][1]) + (this.w * m.m[3][1]);
        double zz = (this.x * m.m[0][2]) + (this.y * m.m[1][2]) + (this.z * m.m[2][2]) + (this.w * m.m[3][2]);
        double w = (this.x * m.m[0][3]) + (this.y * m.m[1][3]) + (this.z * m.m[2][3]) + (this.w * m.m[3][3]);

        return new Vector3D(xx, yy, zz, w);
    }

    public Vector3D  multiplyByMatrix(double[][] m){
        
        double xx = (this.x * m[0][0]) + (this.y * m[1][0]) + (this.z * m[2][0]) + this.w * m[3][0];
        double yy = (this.x * m[0][1]) + (this.y * m[1][1]) + (this.z * m[2][1]) + this.w * m[3][1];
        double zz = (this.x * m[0][2]) + (this.y * m[1][2]) + (this.z * m[2][2]) + this.w * m[3][2];
        double w = (this.x * m[0][3]) + (this.y * m[1][3]) + (this.z * m[2][3]) + this.w * m[3][3];

        return new Vector3D(xx, yy, zz, w);
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public boolean isNullVec(){
        return x == .0 && y == .0 && z == .0;
    }

    /**
     * Distantance from a point to a plane
     * @param planeN normal vector of the plane
     * @param plane_p point in the plane
     * @param pT point to calculate distance
     * @return distance
     */
    public static double DistanceFromPlane(Vector3D planeN, Vector3D plane_p, Vector3D pT) {            
        double dist = planeN.dot(pT) - (planeN.dot(plane_p));

        return dist;
    }
}
