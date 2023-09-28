package edu.sdccd.cisc191.linalg;

import edu.sdccd.cisc191.matrix.*;

public class LinSystem {

    // No solution exception
    public static class NoSolutionException extends Exception {
        public NoSolutionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Infinite solutions exception
    public static class InfiniteSolutionsException extends Exception {
        public InfiniteSolutionsException(String errorMessage) {
            super(errorMessage);
        }
    }

    /**
     * Solves a system of linear equations Ax = b
     * @param A The matrix A
     * @param b The matrix b
     * @return Returns the solution x
     * @throws NoSolutionException When there is no solution
     * @throws InfiniteSolutionsException When there are infinite solutions
     * @throws IllegalArgumentException When the matrix shapes are not compatible
     */
    public static Matrix solveSystem(Matrix A, Matrix b) throws
            NoSolutionException,
            InfiniteSolutionsException,
            IllegalArgumentException {

        // Check shape
        Integer[] shapeA = A.getShape();
        Integer[] shapeB = b.getShape();
        if (!shapeA[0].equals(shapeB[0])) {
            throw new IllegalArgumentException("Matrix shapes are not compatible");
        }

        // Augment A and b
        Matrix Ab = Matrix.newZeros(shapeA[0], shapeA[1] + 1);
        Ab.select(new Slice(A.getNumRows()), new Slice(A.getNumCols())).add(
                A.select(new Slice(), new Slice()));
        Ab.select(new Slice(A.getNumRows()), new Slice(A.getNumCols(), A.getNumCols() + 1)).add(
                b.select(new Slice(), new Slice()));

        // Gaussian elimination
        for (int i = 0; i < shapeA[0]; i++) {
            // Find pivot
            int pivot = findPivot(Ab, i);
            if (pivot == -1) {
                // Check for infinite solutions
                for (int row = i; row < shapeA[0]; row++) {
                    boolean allZeros = true;
                    for (int col = i; col < shapeA[1]; col++) {
                        if (Ab.get(row, col) != 0) {
                            allZeros = false;
                            break;
                        }
                    }
                    if (allZeros && Ab.get(row, shapeA[1]) != 0) {
                        // No solution
                        throw new NoSolutionException("No solution");
                    } else if (allZeros) {
                        throw new InfiniteSolutionsException("Infinite solutions");
                    }
                }
            }

            // Swap rows
            if (pivot != i) {
                swapRows(Ab, i, pivot);
            }
            // Eliminate
            for (int j = i + 1; j < shapeA[0]; j++) {
                double factor = Ab.get(j, i) / Ab.get(i, i);
                for (int k = i; k < shapeA[1] + 1; k++) {
                    Ab.set(Ab.get(j, k) - factor * Ab.get(i, k), j, k);
                }
            }
        }

        // Back substitution
        Matrix x = Matrix.newZeros(shapeA[0], 1);
        for (int i = shapeA[0] - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < shapeA[0]; j++) {
                sum += Ab.get(i, j) * x.get(j, 0);
            }
            x.set((Ab.get(i, shapeA[0]) - sum) / Ab.get(i, i), i, 0);
        }

        return x;

    }

    private static int findPivot(Matrix ab, int i) {
        Integer[] shape = ab.getShape();
        for (int j = i; j < shape[0]; j++) {
            if (ab.get(j, i) != 0) {
                return j;
            }
        }
        return -1;
    }

    private static void swapRows(Matrix ab, int i, int j) {
        Integer[] shape = ab.getShape();
        for (int k = 0; k < shape[1]; k++) {
            double temp = ab.get(i, k);
            ab.set(ab.get(j, k), i, k);
            ab.set(temp, j, k);
        }
    }

}
