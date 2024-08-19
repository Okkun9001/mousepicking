package com.okkun.utls;

import com.okkun.Camera3D;

public class Matrix4x4 {
    
    public double[][] m = new double[4][4];

    private Matrix4x4() {}

    public Matrix4x4(double[][] matrix) {
        this.m = matrix;
    }

    public static Matrix4x4 identity() {
        Matrix4x4 m = new Matrix4x4();
        for(int i = 0; i < 4; i++){
            m.m[i][i] = 1;
        }
        return m;
    }

    public static Matrix4x4 rotateX(double fTheta){
        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {1.0, 0.0, 0.0, 0.0};
        matrix[1] = new double[] {0.0, Math.cos(fTheta), Math.sin(fTheta), 0.0};
        matrix[2] = new double[] {0.0, -Math.sin(fTheta), Math.cos(fTheta), 0.0};
        matrix[3] = new double[] {0.0, 0.0, 0.0, 1.0};

        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 rotateY(double fTheta){
        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {Math.cos(fTheta), 0.0, Math.sin(fTheta), 0.0};
        matrix[1] = new double[] {0.0, 1.0, 0.0, 0.0};
        matrix[2] = new double[] {-Math.sin(fTheta), 0.0, Math.cos(fTheta), 0.0};
        matrix[3] = new double[] {0.0, 0.0, 0.0, 1.0};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 rotateZ(double fTheta) {
        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {Math.cos(fTheta), Math.sin(fTheta), 0.0, 0.0};
        matrix[1] = new double[] {-Math.sin(fTheta), Math.cos(fTheta), 0.0, 0.0};
        matrix[2] = new double[] {0.0, 0.0, 1.0, 0.0};
        matrix[3] = new double[] {0.0, 0.0, 0.0, 1.0};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 intrinsicRot(double a, double b, double g){
        double[][] matrix = new double[4][4];

        matrix[0] = new double[] {  Math.cos(a)*Math.cos(b), 
                                    (Math.cos(a)*Math.sin(b)*Math.sin(g)) - (Math.cos(g)*Math.sin(a)), 
                                    Math.cos(a)*Math.sin(b)*Math.cos(g) + Math.sin(a)*Math.sin(g), 
                                    0.0};
        matrix[1] = new double[] {  Math.sin(a)*Math.cos(b), 
                                    (Math.sin(a)*Math.sin(b)*Math.sin(g)) + (Math.cos(a)*Math.cos(g)), 
                                    Math.sin(a)*Math.sin(b)*Math.cos(g) - Math.cos(a)*Math.sin(g), 
                                    0.0};
        matrix[2] = new double[] { -Math.sin(b),
                                    Math.cos(b)*Math.sin(g),
                                    Math.cos(b)*Math.cos(g),
                                    0.0};
        matrix[3] = new double[] { 0.0, 0.0, 0.0, 1.0};

        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 translation(double x, double y, double z){
        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {1.0, 0.0, 0.0, 0.0};
        matrix[1] = new double[] {0.0, 1.0, 0.0, 0.0};
        matrix[2] = new double[] {0.0, 0.0, 1.0, 0.0};
        matrix[3] = new double[] {x, y, z, 1.0};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 translation(Vector3D v){
        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {1.0, 0.0, 0.0, 0.0};
        matrix[1] = new double[] {0.0, 1.0, 0.0, 0.0};
        matrix[2] = new double[] {0.0, 0.0, 1.0, 0.0};
        matrix[3] = new double[] {v.x, v.y, v.z, 1.0};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 projection(double fFovDegrees, double fAspectRatio, double fNear, double fFar){
        
        double[][] projMatrix = new double[4][4]; //row, column
        double fFOV = 1.0 / Math.tan(fFovDegrees * 0.5);
        
        projMatrix[0][0] = fAspectRatio * fFOV;
        projMatrix[0][1] = 0.0;
        projMatrix[0][2] = 0.0;
        projMatrix[0][3] = 0.0;
        
        projMatrix[1][0] = 0.0;
        projMatrix[1][1] = fFOV;
        projMatrix[1][2] = 0.0;
        projMatrix[1][3] = 0.0;
        
        projMatrix[2][0] = 0.0;
        projMatrix[2][1] = 0.0;
        projMatrix[2][2] = (fFar+fNear) / (fFar - fNear);
        projMatrix[2][3] = 1.0;
        
        projMatrix[3][0] = 0.0;
        projMatrix[3][1] = 0.0;
        projMatrix[3][2] = (2*fFar * fNear) / (fNear-fFar);
        projMatrix[3][3] = 0.0;

        return new Matrix4x4(projMatrix);
    }

    public static Matrix4x4 projectionInverse(Matrix4x4 projMat){
        double[][] invProjMatrix = new double[4][4];

        invProjMatrix[0][0] = 1.0f / projMat.m[0][0];
        invProjMatrix[0][1] = 0.0f;
        invProjMatrix[0][2] = 0.0f;
        invProjMatrix[0][3] = 0.0f;

        invProjMatrix[1][0] = 0.0f;
        invProjMatrix[1][1] = 1.0f / projMat.m[1][1];
        invProjMatrix[1][2] = 0.0f;
        invProjMatrix[1][3] = 0.0f;

        invProjMatrix[2][0] = 0.0f;
        invProjMatrix[2][1] = 0.0f;
        invProjMatrix[2][2] = 0.0f;
        invProjMatrix[2][3] = 1.0f;

        invProjMatrix[3][0] = 0.0f;
        invProjMatrix[3][1] = 0.0f;
        invProjMatrix[3][2] = 1/projMat.m[3][2];
        invProjMatrix[3][3] = 1/projMat.m[2][2];

        return new Matrix4x4(invProjMatrix);
    }

    public static Matrix4x4 pointAt(Camera3D camera){

        Vector3D pos = camera.getPos();
        System.out.println("Camera Position: " + pos);
        Vector3D up = new Vector3D(0, -1, 0);

        Vector3D newForward = camera.getLookDir();
        Vector3D a = newForward.multiply(up.dot(newForward));
        Vector3D newUp = up.subtract(a).normalize();
        Vector3D newRight = newForward.cross(newUp);

        double tX = newRight.dot(pos);
        double tY = newUp.dot(pos);
        double tZ = newForward.dot(pos);

        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {newRight.x, newRight.y, newRight.z, tX};
        matrix[1] = new double[] {newUp.x, newUp.y, newUp.z, tY};
        matrix[2] = new double[] {newForward.x, newForward.y, newForward.z, tZ};
        matrix[3] = new double[] {0, 0, 0, 1.0};

        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 viewMatrix(Matrix4x4 pointAtMatrix){
        return quickInverse(pointAtMatrix);
    }

    private static Matrix4x4 quickInverse(Matrix4x4 PAm){

        double[][] matrix = new double[4][4];
        matrix[0] = new double[] {PAm.m[0][0], PAm.m[1][0], PAm.m[2][0], 0.0};
        matrix[1] = new double[] {PAm.m[0][1], PAm.m[1][1], PAm.m[2][1], 0.0};
        matrix[2] = new double[] {PAm.m[0][2], PAm.m[1][2], PAm.m[2][2], 0.0};
        matrix[3] = new double[] {-PAm.m[0][3], -PAm.m[1][3], -PAm.m[2][3], 1.0};

        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 MultiplyMatrices(Matrix4x4 m1, Matrix4x4 m2){
        double[][] matrix = new double[4][4];
        for(int c = 0; c < 4; c++){
            for(int r = 0; r < 4; r++){
                matrix[r][c] =( m1.m[r][0] * m2.m[0][c]) + (m1.m[r][1] * m2.m[1][c]) + (m1.m[r][2] * m2.m[2][c]) + (m1.m[r][3] * m2.m[3][c]);
            }
        }
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 createWorldTransform(double alpha, double beta, double gamma, Vector3D transformVector) {
        
        Matrix4x4 matWorld = identity();
        if(alpha != .0){
            Matrix4x4 matRotX = rotateX(alpha);
            matWorld = MultiplyMatrices(matWorld, matRotX);
        }
        if(beta != .0){
            Matrix4x4 matRotY = rotateY(beta);
            matWorld = MultiplyMatrices(matWorld, matRotY);
        }
        if(gamma != .0){
            Matrix4x4 matRotZ = rotateZ(gamma);
            matWorld = MultiplyMatrices(matWorld, matRotZ);
        }
        if(transformVector != null && !transformVector.isNullVec()){
            Matrix4x4 matTranslation = translation(transformVector);
            matWorld = MultiplyMatrices(matWorld, matTranslation);
        }

        return matWorld;
    }

    public String toString(){
        String s = "";
        for(int r = 0; r < 4; r++){
            for(int c = 0; c < 4; c++){
                s += m[r][c] + " ";
            }
            s += "\n";
        }
        return s;
    }

}