package com.okkun.utls;

public class Math2D {

    public static final double EPSILON = 1e-5;
    
    public static double Clamp(double value, double min, double max){
        if(min > max){ throw new IllegalArgumentException("Min value greater than max value."); }
        return Math.max(min, Math.min(max, value));
    }

    public static int Clamp(int value, int min, int max){
        if(min > max){ throw new IllegalArgumentException("Min value greater than max value."); }
        return Math.max(min, Math.min(max, value));
    }

    public static double PolygonArea(Vector2D[] vertices, int[][] shapes) {
        
        double areaSum = 0;
        for(int i = 0; i < shapes.length; i++){
            if(shapes[i].length == 3){
                Vector2D p1 = vertices[shapes[i][0]];
                Vector2D p2 = vertices[shapes[i][1]];
                Vector2D p3 = vertices[shapes[i][2]];
                areaSum += TriangleArea(p1, p2, p3);
            }else if(shapes[i].length == 4){
                Vector2D p1 = vertices[shapes[i][0]];
                Vector2D p2 = vertices[shapes[i][1]];
                Vector2D p3 = vertices[shapes[i][2]];
                Vector2D p4 = vertices[shapes[i][3]];
                areaSum += TriangleArea(p1, p2, p3);
                areaSum += TriangleArea(p1, p3, p4);
            }
            else if(shapes[i].length > 4){
                areaSum += ConvexPolygonArea(vertices, shapes[i]);
            }
        }
        return areaSum;
    }

    public static Vector2D FindPolygonCentroid(Vector2D[] vertics, double area){
        double x = 0;
        double y = 0;
        for(int i = 0; i < vertics.length; i++){
            Vector2D p1 = vertics[i];
            Vector2D p2 = vertics[(i+1)%vertics.length];
            double common = p1.x*p2.y - p2.x*p1.y;
            x += (p1.x + p2.x) * common;
            y += (p1.y + p2.y) * common;
        }
        x /= 6.*area;
        y /= 6.*area;
        return new Vector2D(x, y);
    }

    private static double ConvexPolygonArea(Vector2D[] vertices, int[] shape) {
        double area = 0;
        for (int i = 0; i < shape.length; i++) {
            Vector2D current = vertices[shape[i]];
            Vector2D next = vertices[shape[(i + 1) % shape.length]]; // Wrap around to the first vertex
            area += current.x * next.y - next.x * current.y;
        }
        area = Math.abs(area) / 2.0;
        return area;
    }

    public static double TriangleArea(Vector2D p1, Vector2D p2, Vector2D p3) {
        //A = (1/2) |x1(y2 − y3) + x2(y3 − y1) + x3(y1 − y2)|
        return 0.5 * Math.abs(p1.x*(p2.y - p3.y) + p2.x*(p3.y - p1.y) + p3.x*(p1.y - p2.y));
    }

    public static Vector2D FindArithmeticMean(Vector2D[] vertices) {
        double x = 0;
        double y = 0;
        for(int i = 0; i < vertices.length; i++){
            x += vertices[i].x;
            y += vertices[i].y;
        }
        return new Vector2D(x/vertices.length, y/vertices.length);
    }

    public static Vector2D PointSegmentDist(Vector2D p, Vector2D a, Vector2D b){
        Vector2D ab = b.subtract(a);
        Vector2D ap = p.subtract(a);

        double proj = ap.dotProduct(ab);
        double abLength = ab.magnitudeSquared();
        double d = proj/abLength;

        Vector2D contact;
        if(d <= .0){
            contact = a;
        }
        else if(d>=1.){
            contact = b;
        }
        else{
            contact = a.add(ab.multiply(d));
        }
        
        return contact;
    }

    public static boolean equal(double x, double y, double epsilon) { return Math.abs(x - y) < epsilon; }

    public static double PolygonInertia(Vector2D[] vertices,int[][] shapes, double mass) {
        
        double inertia = 0;
        for(int i =0; i<shapes.length;i++){
            if(shapes[i].length == 3){
                Vector2D p1 = vertices[shapes[i][0]];
                Vector2D p2 = vertices[shapes[i][1]];
                Vector2D p3 = vertices[shapes[i][2]];
                inertia += TriangleInertia(p3, p2, p1, mass);
            }else if(shapes[i].length == 4){
                Vector2D p1 = vertices[shapes[i][0]];
                Vector2D p2 = vertices[shapes[i][1]];
                Vector2D p3 = vertices[shapes[i][2]];
                Vector2D p4 = vertices[shapes[i][3]];
                inertia += TriangleInertia(p1, p2, p3, mass);
                inertia += TriangleInertia(p1, p3, p4, mass);
            }
            else if(shapes[i].length > 4){
                return -1;
                //inertia += ConvexPolygonInertia(vertices, shapes[i], mass);
            }
        }
        return inertia;
    }

    public static double TriangleInertia(Vector2D p1, Vector2D p2, Vector2D p3, double mass) {
        Vector2D v1 = p2.subtract(p1);
        Vector2D v2 = p3.subtract(p1);

        //A = (v1 x v2)/2 = wh/2 -> h = 2A/w = (v1 x v2)/w = (v1 x v2)/|v1|
        double crossp = Math.abs(v1.crossProduct(v2));
        // System.out.println("crossp: " + crossp);
        // System.out.println("Area: " + crossp/2.0);
        //density = m/A = 2m/(v1 x v2)
        double density = 2*mass/crossp;

        double w = v1.getMagnitude();
        // System.out.println("'b' aka v1: " + w);
        double h = (crossp/w);
        // System.out.println("h: " +h);
        
        //w1 = p1 + (v1 • v2) * v1
        double projection = v1.dotProduct(v2);
        // System.out.println("projection: " + projection);
        double scalar = projection/v1.getMagnitude();
        // System.out.println("scalar: " + scalar);
        Vector2D p4 = p1.add(v1.normalize().multiply(scalar));
        double w1 = p4.getMagnitude();
        double w2 = w - w1;
        
        //Inertia with respect to P3
        //I = density * ((h*w1^3)/4 + (h^3*w1)/12    +    (h*w2^3)/4 + (h^3*w2)/12)
        double T1 = ((h*Math.pow(w1, 3))/4) + ((Math.pow(h, 3)*w1)/12);
        double T2 = ((h*Math.pow(w2, 3))/4) + ((Math.pow(h, 3)*w2)/12);
        double I = density*( T1 + T2);
        // System.out.println("w1: " + w1);
        // System.out.println("w2: " + w2);
        
        //Parallel axis thorem to find inertia with respect to centroid
        //I = Icm + m*d^2
        Vector2D centroid = FindArithmeticMean(new Vector2D[]{p1, p2, p3});
        double dSq = Vector2D.DistanceSquared(centroid, p3);
        // System.out.println("d: " + d);
        // System.out.println("I: " + I);
        // System.out.println();
        I += dSq*mass;
        
        return I;
    }

    public static Vector2D[] scale(Vector2D[] polygon, double scale) {
        Vector2D[] scaled = new Vector2D[polygon.length];
        for (int i = 0; i < polygon.length; i++) {
            Vector2D vector = polygon[i];
            vector = vector.multiply(scale);
            scaled[i] = vector;
        }
        return scaled;
    }
}

//Dr.Ben Yelverton formula: density*(u x v)/12 * [|u|^2 + |v|^2 + u dot v]
    // Vector2D u = p2.subtract(p1);
    // Vector2D v = p3.subtract(p2);
    // double I2 = density*crossp/12.0 * (u.magnitudeSquared() + v.magnitudeSquared() + u.dotProduct(v));
    // System.out.println("I2: " + I2);
    
