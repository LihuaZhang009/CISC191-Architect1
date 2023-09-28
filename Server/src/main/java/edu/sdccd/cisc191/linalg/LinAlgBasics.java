package edu.sdccd.cisc191.linalg;

import edu.sdccd.cisc191.matrix.*;

public class LinAlgBasics {

    /**
     * Returns the transpose of a matrix
     * @param A The matrix to transpose
     * @return Returns the transpose of A
     */
    public static Matrix transpose(Matrix A) {
        Integer[] shape = A.getShape();
        Matrix result = Matrix.newZeros(shape[1], shape[0]);
        for (int i = 0; i < shape[0]; i++) {
            for (int j = 0; j < shape[1]; j++) {
                result.set(A.get(i, j), j, i);
            }
        }
        return result;
    }

    /**
     * Returns the matrix product of two matrices, mat1 * mat2
     * @param mat1 The first matrix
     * @param mat2  The second matrix
     * @return Returns the matrix product of mat1 and mat2
     */
    public static Matrix matrixMultiply(Matrix mat1, Matrix mat2) {

        // Check shape
        Integer[] shape1 = mat1.getShape();
        Integer[] shape2 = mat2.getShape();
        if (!shape1[1].equals(shape2[0])) {
            throw new IllegalArgumentException("Matrix shapes are not compatible");
        }

        // Create new matrix
        Matrix result = Matrix.newZeros(shape1[0], shape2[1]);
        for (int i = 0; i < shape1[0]; i++) {
            for (int j = 0; j < shape2[1]; j++) {
                for (int k = 0; k < shape1[1]; k++) {
                    result.set(result.get(i, j) + mat1.get(i, k) * mat2.get(k, j), i, j);
                }
            }
        }
        return result;

    }

}
