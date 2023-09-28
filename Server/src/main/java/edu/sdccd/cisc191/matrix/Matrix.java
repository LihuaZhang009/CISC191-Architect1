package edu.sdccd.cisc191.matrix;

import java.util.*;

public class Matrix extends DataView implements Array {

    private final int numRows;
    private final int numCols;
    private final int actualNumCols;

    // The actual data is stored in a 1D array, but can be accessed as a 2D array
    // using the row and column indices.
    private final Integer[] rowIndices;
    private final Integer[] colIndices;


    // Constructors
    public Matrix(Double[][] srcData) {

        if (srcData.length == 0) {
            throw new IllegalArgumentException("Matrix must have at least one row");
        }
        int rowLength = srcData[0].length;
        for (Double[] row : srcData) {
            if (row.length != rowLength) {
                throw new IllegalArgumentException("Matrix must have rows of equal length");
            }
        }

        List<Double> flatData = new ArrayList<>();
        for (Double[] row : srcData) {
            for (Double value : row) {
                if (value == null) {
                    throw new IllegalArgumentException("Matrix cannot contain null values");
                }
                flatData.add(value);
            }
        }
        this.data = flatData.toArray(new Double[0]);
        this.numRows = srcData.length;
        this.numCols = srcData[0].length;
        this.actualNumCols = numCols;
        if (numCols == 0) {
            throw new IllegalArgumentException("Matrix must have at least one row and one column");
        }

        this.rowIndices = new Integer[numRows];
        this.colIndices = new Integer[numCols];
        for (int i = 0; i < numRows; i++) {
            rowIndices[i] = i;
        }
        for (int i = 0; i < numCols; i++) {
            colIndices[i] = i;
        }

    }

    /**
     * Create a Matrix from a 1D array of data, with the specified number of rows and columns.
     * This method is private because it is used internally to create a sub-matrix.
     * @param data The 1D array of data
     * @param numRows The number of rows
     * @param numCols The number of columns
     * @param actualNumCols The actual number of columns in the original matrix
     * @param rowIndices The row indices of the sub-matrix
     * @param colIndices The column indices of the sub-matrix
     */
    private Matrix(Double[] data, int numRows, int numCols, int actualNumCols,
                   Integer[] rowIndices, Integer[] colIndices) {
        this.data = data;
        this.numRows = numRows;
        this.numCols = numCols;
        this.rowIndices = rowIndices;
        this.colIndices = colIndices;
        this.actualNumCols = actualNumCols;
    }

    /**
     * Create a Matrix of all zeros with the specified number of rows and columns.
     * @param numRows The number of rows
     * @param numCols The number of columns
     * @return Returns a Matrix of all zeros
     */
    public static Matrix newZeros(int numRows, int numCols) {
        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException("Matrix must have at least one row and one column");
        }
        Double[] data = new Double[numRows * numCols];
        Arrays.fill(data, 0.0);
        Integer[] rowIndices = new Integer[numRows];
        Integer[] colIndices = new Integer[numCols];
        for (int i = 0; i < numRows; i++) {
            rowIndices[i] = i;
        }
        for (int i = 0; i < numCols; i++) {
            colIndices[i] = i;
        }
        return new Matrix(data, numRows, numCols, numCols, rowIndices, colIndices);
    }

    /**
     * Create a Matrix filled with normally distributed random values with the
     * specified number of rows and columns.
     * @param numRows The number of rows
     * @param numCols The number of columns
     * @return Returns a Matrix filled with normally distributed random values
     */
    public static Matrix newRandom(int numRows, int numCols) {

        if (numRows <= 0 || numCols <= 0) {
            throw new IllegalArgumentException("Matrix must have at least one row and one column");
        }
        Double[] data = new Double[numRows * numCols];
        Random rand = new Random();
        for (int i = 0; i < numRows * numCols; i++) {
            data[i] = rand.nextGaussian();
        }
        Integer[] rowIndices = new Integer[numRows];
        Integer[] colIndices = new Integer[numCols];
        for (int i = 0; i < numRows; i++) {
            rowIndices[i] = i;
        }
        for (int i = 0; i < numCols; i++) {
            colIndices[i] = i;
        }
        return new Matrix(data, numRows, numCols, numCols, rowIndices, colIndices);

    }

    // Implementation of methods in Array
    @SafeVarargs
    @Override
    public final <T> Matrix select(T... args) {

        // Check args length
        if (args.length != 2) {
            throw new IllegalArgumentException("Matrix select() requires 2 arguments");
        }

        // Check args types
        for (T arg : args) {
            if (!(arg instanceof Integer) && !(arg instanceof Integer[]) && !(arg instanceof Slice)) {
                throw new IllegalArgumentException("Matrix select() arguments must be Integer, Integer[], or Slice");
            }
        }

        List<Integer> rowIndices = new ArrayList<>();
        List<Integer> colIndices = new ArrayList<>();

        // Get row indices
        T arg0 = args[0];
        if (arg0 instanceof Integer) {
            rowIndices.add(this.rowIndices[(Integer) arg0]);
        } else if (arg0 instanceof Integer[]) {
            for (Integer index : (Integer[]) arg0) {
                rowIndices.add(this.rowIndices[index]);
            }
        } else if (arg0 instanceof Slice) {
            Slice slice = (Slice) arg0;
            int top = slice.getEnd() == -1 ? numRows : slice.getEnd();
            for (int i = slice.getStart(); i < top; i += slice.getStep()) {
                rowIndices.add(this.rowIndices[i]);
            }
        }

        // Get col indices
        T arg1 = args[1];
        if (arg1 instanceof Integer) {
            colIndices.add(this.colIndices[(Integer) arg1]);
        } else if (arg1 instanceof Integer[]) {
            for (Integer index : (Integer[]) arg1) {
                colIndices.add(this.colIndices[index]);
            }
        } else if (arg1 instanceof Slice) {
            Slice slice = (Slice) arg1;
            int top = slice.getEnd() == -1 ? numCols : slice.getEnd();
            for (int i = slice.getStart(); i < top; i += slice.getStep()) {
                colIndices.add(this.colIndices[i]);
            }
        }


        // Otherwise, the resulting array is a 2-dim Matrix
        return new Matrix(
            data,
            rowIndices.size(),
            colIndices.size(),
            actualNumCols,
            rowIndices.toArray(new Integer[0]),
            colIndices.toArray(new Integer[0])
        );
    }

    @Override
    public Matrix copy() {
        Double[] newData = new Double[numRows * numCols];
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                newData[i * numCols + j] = get(i, j);
            }
        }
        Integer[] rowIndices = new Integer[numRows];
        Integer[] colIndices = new Integer[numCols];
        for (int i = 0; i < numRows; i++) {
            rowIndices[i] = i;
        }
        for (int i = 0; i < numCols; i++) {
            colIndices[i] = i;
        }
        return new Matrix(newData, numRows, numCols, numCols, rowIndices,
                colIndices);
    }

    public Double[][] toList() {
        // Convert to 2d array
        Double[][] data = new Double[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            Double[] rowData = new Double[numCols];
            for (int col = 0; col < numCols; col++) {
                rowData[col] = get(row, col);
            }
            data[row] = rowData;
        }
        return data;
    }

    @Override
    public boolean isCompatible(Array other) {
        if (!(other instanceof Matrix)) {
            return false;
        }
        Matrix otherMatrix = (Matrix) other;
        Integer[] shape = otherMatrix.getShape();
        return numRows == shape[0] && numCols == shape[1];
    }

    @Override
    public void add(Array other) {
        if (!isCompatible(other)) {
            throw new IllegalArgumentException("Matrix add() incompatible with other array");
        }
        Matrix otherMatrix = (Matrix) other;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) + otherMatrix.get(i, j), i, j);
            }
        }
    }

    @Override
    public void add(Double scalar) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) + scalar, i, j);
            }
        }
    }

    @Override
    public void subtract(Array other) {
        if (!isCompatible(other)) {
            throw new IllegalArgumentException("Matrix subtract() incompatible with other array");
        }
        Matrix otherMatrix = (Matrix) other;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) - otherMatrix.get(i, j), i, j);
            }
        }
    }

    @Override
    public void subtract(Double scalar) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) - scalar, i, j);
            }
        }
    }

    @Override
    public void multiply(Array other) {
        if (!isCompatible(other)) {
            throw new IllegalArgumentException("Matrix multiply() incompatible with other array");
        }
        Matrix otherMatrix = (Matrix) other;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) * otherMatrix.get(i, j), i, j);
            }
        }
    }

    @Override
    public void multiply(Double scalar) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) * scalar, i, j);
            }
        }
    }

    @Override
    public void divide(Array other) {
        if (!isCompatible(other)) {
            throw new IllegalArgumentException("Matrix divide() incompatible with other array");
        }
        Matrix otherMatrix = (Matrix) other;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) / otherMatrix.get(i, j), i, j);
            }
        }
    }

    @Override
    public void divide(Double scalar) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.set(this.get(i, j) / scalar, i, j);
            }
        }
    }

    // Inherited from DataView
    @Override
    public Double get(Integer... args) {
        checkIndex(args);
        int row = args[0];
        int col = args[1];
        int rowIndex = rowIndices[row];
        int colIndex = colIndices[col];
        return data[rowIndex * actualNumCols + colIndex];
    }

    @Override
    public void set(Double value, Integer... args) {
        checkIndex(args);
        int row = args[0];
        int col = args[1];
        int rowIndex = rowIndices[row];
        int colIndex = colIndices[col];
        data[rowIndex * actualNumCols + colIndex] = value;
    }

    @Override
    public Integer[] getShape() {
        return new Integer[] {numRows, numCols};
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < numRows; row++) {
            sb.append("[");
            for (int col = 0; col < numCols; col++) {
                sb.append(get(row, col));
                if (col < numCols - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (row < numRows - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // Getters
    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

}
