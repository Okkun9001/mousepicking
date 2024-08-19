package com.okkun;

import com.okkun.utls.Matrix4x4;
import com.okkun.utls.Vector3D;

public interface transformable {
    
    public void applyMatrix(Matrix4x4 matrix);
    public void divideW();
    public void translate(Vector3D v);
    public void scale(Vector3D v);
}
