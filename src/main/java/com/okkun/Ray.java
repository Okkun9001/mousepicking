package com.okkun;

import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector3D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ray {
    private Vector3D origin; //camera in world coordinates
        public Vector3D getOrigin() { return origin; }
    private Vector3D direction; //ray in world coordinates
        public Vector3D getDirection() { return direction; }
    private Vector3D mousePos; //mouse on scren

    public Ray(Vector3D origin, Vector3D direction, Vector3D mousePos) {
        this.origin = origin;
        this.direction = direction;
        this.mousePos = mousePos;
    }

    public Vector3D getPoint(double t) {
        return origin.add(direction.multiply(t));
    }

    public void draw(GraphicsContext gc, Matrix4x4 projMatrix, Matrix4x4 viewMatrix) {
        if(origin == null || direction == null) return;

        Vector3D end = getPoint(1000);
        end.multiplyByMatrix(viewMatrix);
        
        end.multiplyByMatrix(projMatrix);
        end.divide(end.w);
        end.Translate(Renderer3D.TRANSLATE);
        end.Scale(Renderer3D.scale);

        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeLine(mousePos.x, mousePos.y, end.x, end.y);
    }
}
