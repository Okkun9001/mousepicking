package com.okkun.utls;

import com.okkun.TriangleTile;

public class MathHelper {
 

    // CLIPPING
    public static TriangleTile[] clipTriangleAgainstPlane(Vector3D plane_p, Vector3D plane_n, TriangleTile tri){
        Vector3D planeN = plane_n.normalize();

        Vector3D[] insidePoints = new Vector3D[3];
        Vector3D[] outsidePoints = new Vector3D[3];
        int nInsidePointCount = 0;
        int nOutsidePointCount = 0;

        double d0 = Vector3D.DistanceFromPlane(planeN, plane_p, tri.v1);
        double d1 = Vector3D.DistanceFromPlane(planeN, plane_p, tri.v2);
        double d2 = Vector3D.DistanceFromPlane(planeN, plane_p, tri.v3);

        if(d0 >= 0) {   insidePoints[nInsidePointCount++] = tri.v1; }
            else {  outsidePoints[nOutsidePointCount++] = tri.v1; }
        if(d1 >= 0) {   insidePoints[nInsidePointCount++] = tri.v2; }
            else {  outsidePoints[nOutsidePointCount++] = tri.v2; }
        if(d2 >= 0) {   insidePoints[nInsidePointCount++] = tri.v3; }
            else {  outsidePoints[nOutsidePointCount++] = tri.v3; }


        if(nInsidePointCount == 0){
            return null;
        }
        if(nInsidePointCount == 3){
            return new TriangleTile[]{tri};
        }
        if(nInsidePointCount == 1 && nOutsidePointCount == 2){
            TriangleTile newTri = new TriangleTile( insidePoints[0],
                                                    intersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]),
                                                    intersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[1]), tri.getFillColor(), tri.getStrokeColor());
            return new TriangleTile[]{newTri};
        }
        if(nInsidePointCount == 2 && nOutsidePointCount == 1){
            TriangleTile newTri1 = new TriangleTile( insidePoints[0],
                                                    insidePoints[1],
                                                    intersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]), tri.getFillColor(), tri.getStrokeColor());
            TriangleTile newTri2 = new TriangleTile( insidePoints[1],
                                                    newTri1.v3,
                                                    intersectPlane(plane_p, plane_n, insidePoints[1], outsidePoints[0]), tri.getFillColor(), tri.getStrokeColor());

            return new TriangleTile[]{newTri1, newTri2};
        }
        throw new RuntimeException("Should not be here");

    }

     /**
     * Clip against a plane
     * @param plane_p point on the plane
     * @param plane_n normal of the plane
     * @param p point to clip
     * @return true if the point is in the frustum
     */
    public static boolean clipAgainstPlane(Vector3D plane_p, Vector3D plane_n, Vector3D p){
        Vector3D planeN = plane_n.normalize();

        double d = Vector3D.DistanceFromPlane(planeN, plane_p, p);
        if(d >= 0) { return true; }
        return false;
    }

    //MATH

    /**
     * Find the intersection point of a line and a plane
     * @param plane_p point on the plane
     * @param plane_n normal vector of the plane
     * @param lineStart start point of the line
     * @param lineEnd end point of the line
     * @return intersection point
     */
    public static Vector3D intersectPlane(Vector3D plane_p, Vector3D plane_n, Vector3D lineStart, Vector3D lineEnd) {
        
        plane_n = plane_n.normalize();
        double plane_d = -plane_n.dot(plane_p);
        double ad = lineStart.dot(plane_n);
        double bd = lineEnd.dot(plane_n);
        double t = (-plane_d - ad) / (bd - ad);
        Vector3D lineStartToEnd = lineEnd.subtract(lineStart);
        Vector3D lineToIntersect = lineStartToEnd.multiply(t);
        return lineStart.add(lineToIntersect);
    }

    public static boolean isInTriangle(TriangleTile transformed, Vector3D I) {
        
        Vector3D normI = I.normalize();
        Vector3D[] vertices = transformed.getVertices();
        int count = 0;
        for(int j = 0; j < 3; j++){
            Vector3D v0 = vertices[j];
            Vector3D v1 = vertices[(j+1)%3];
            Vector3D v2 = vertices[(j+2)%3];

            Vector3D AB = v1.subtract(v0).normalize();
            Vector3D AC = v2.subtract(v0).normalize();
            Vector3D AM = AB.multiply(AC.dot(AB));
            Vector3D MI = normI.subtract(AM).normalize();
            Vector3D AI = normI.subtract(v0).normalize();
            double dot = MI.dot(AI);
            // System.out.println(dot);
            if(dot > 0) count--;
            else count++;
        }
        return count == 3 || count == -3;
        
    }
}
