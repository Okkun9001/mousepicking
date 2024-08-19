package com.okkun;

import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector3D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TriangleTile  implements transformable, Comparable<TriangleTile> {

    public Vector3D v1, v2, v3;
    private String fillColor = "#00000000";      public String getFillColor() { return fillColor; }         public void setFillColor(String fillColor) { this.fillColor = fillColor; }
    private String strokeColor = "#FFFFFFFF";    public String getStrokeColor() { return strokeColor; }     public void setStrokeColor(String strokeColor) { this.strokeColor = strokeColor; }

    public TriangleTile(Vector3D v1, Vector3D v2, Vector3D v3) {
        
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public TriangleTile(Vector3D v1, Vector3D v2, Vector3D v3, String fillColor, String strokeColor) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
    }

    public TriangleTile(Vector3D[] vertices) {
        this.v1 = vertices[0];
        this.v2 = vertices[1];
        this.v3 = vertices[2];
    }

    public Vector3D[] getVertices() {
        return new Vector3D[]{v1, v2, v3};
    }

    public TriangleTile copy() {
        return new TriangleTile(new Vector3D(v1), new Vector3D(v2), new Vector3D(v3), fillColor, strokeColor);
    }

    @Override
    public void applyMatrix(Matrix4x4 matrix) {
        v1 = v1.multiplyByMatrix(matrix);
        v2 = v2.multiplyByMatrix(matrix);
        v3 = v3.multiplyByMatrix(matrix);
    }

    @Override
    public void divideW() {
        if(v1.w != 0) v1 = v1.divide(v1.w);
        if(v2.w != 0) v2 = v2.divide(v2.w);
        if(v3.w != 0) v3 = v3.divide(v3.w);
    }

    public Vector3D getCenter() {
        return new Vector3D((v1.x + v2.x + v3.x) / 3, (v1.y + v2.y + v3.y) / 3, (v1.z + v2.z + v3.z) / 3, (v1.w + v2.w + v3.w) / 3);
    }

    @Override
    public void translate(Vector3D v) {
        v1.Translate(v);
        v2.Translate(v);
        v3.Translate(v);
    }

    @Override
    public void scale(Vector3D v) {
        v1.Scale(v);
        v2.Scale(v);
        v3.Scale(v);
    }
    
    @Override
    public String toString() {
        return "TriangleTile{" + "v1=" + v1 + ", v2=" + v2 + ", v3=" + v3 + '}';
    }
    public void draw(GraphicsContext gc, Color color, boolean outline, boolean fill) {

        double[] xPoints = new double[3];
        double[] yPoints = new double[3];
        
        xPoints[0] = v1.x;
        xPoints[1] = v2.x;
        xPoints[2] = v3.x;
        yPoints[0] = v1.y;
        yPoints[1] = v2.y;
        yPoints[2] = v3.y;

        if(fill){
            gc.setFill(Color.web(fillColor));
            gc.fillPolygon(xPoints, yPoints, 3);
        }
        if(outline){
            gc.setStroke(Color.web(strokeColor));
            gc.setLineWidth(0.5);
            gc.strokeLine(v1.x, v1.y, v2.x, v2.y);
            gc.strokeLine(v2.x, v2.y, v3.x, v3.y);
            gc.strokeLine(v3.x, v3.y, v1.x, v1.y);
        }
    }
    @Override
    public int compareTo(TriangleTile o) {
        
        return Double.compare(o.getCenter().z, this.getCenter().z);
    }
}
