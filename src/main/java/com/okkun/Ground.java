package com.okkun;

import java.util.List;

import com.okkun.utls.MathHelper;
import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector3D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Ground {


    private int width;
    private int depth;
    @SuppressWarnings("unused")
    private int y;
    private TriangleTile[] ground;
    private List<TriangleTile> mesh = new ArrayList<>();     public List<TriangleTile> getMesh() { return mesh; }
    
    public Ground(int w, int y, int d){

        this.width = w;
        this.y = y;
        this.depth = d;

        double halfWidth = width / 2;
        double halfDepth = depth / 2;
        this.ground = new TriangleTile[2];
        this.ground[0] = new TriangleTile(new Vector3D(-halfWidth, y, -halfDepth), new Vector3D(-halfWidth, y, halfDepth), new Vector3D(halfWidth, y, halfDepth), "#000000", "#000000");
        this.ground[1] = new TriangleTile(new Vector3D(-halfWidth, y, -halfDepth), new Vector3D(halfWidth, y, halfDepth), new Vector3D(halfWidth, y, -halfDepth), "#000000", "#000000");
    }
    
    public void update(Matrix4x4 matView, Matrix4x4 projMat, Vector3D scale, Vector3D vCam){
        mesh.clear();
        for(int i = 0; i < ground.length; i++){
            TriangleTile transformed = ground[i].copy();
            transformed.applyMatrix(matView);

            //DO THE CLIPPING === Clip against near plane
            TriangleTile[] clipped;
            clipped = MathHelper.clipTriangleAgainstPlane(Renderer3D.clipDist, Renderer3D.clipPlane, transformed);

            if(clipped != null){
                for(TriangleTile clippedTri : clipped){

                    clippedTri.applyMatrix(projMat);
                    clippedTri.divideW();
                    clippedTri.translate(Renderer3D.TRANSLATE);
                    clippedTri.scale(scale);
                    mesh.add(clippedTri);
                }
            }
        }
    }

    public void draw(GraphicsContext gc){

        for(TriangleTile t : mesh){

            double[] xPoints = new double[3];
            double[] yPoints = new double[3];
            xPoints[0] = t.v1.x;
            xPoints[1] = t.v2.x;
            xPoints[2] = t.v3.x;
            yPoints[0] = t.v1.y;
            yPoints[1] = t.v2.y;
            yPoints[2] = t.v3.y;
    
            gc.setFill(Color.rgb(90, 100, 90, 0.8));
            gc.fillPolygon(xPoints, yPoints, 3);
        }
    }
}
