package edu.sdccd.cisc191.matrix;


public interface Array {

    /**
     * Select the sub-array from the array. The sub-array should point to the
     * same data as the original array.
     * @param args The arguments to select the sub-array. The arguments should
     *             be of length 2 for matrices, and length 1 for vectors.
     * @return Returns a sub-array of the array, as an Array.
     * @param <T> Parameter type can be Integer, Integer[], Slice
     */
    public <T> Array select(T... args);

    /**
     * Copy the array. The internal data should be copied.
     * @return Returns a clone of the array.
     */
    public Array copy();

    /**
     * Check if the element-wise operation is compatible with the other array.
     * @param other The other array to check compatibility with.
     * @return Returns true if the operation is compatible, false otherwise.
     */
    public boolean isCompatible(Array other);


    // Array Element-wise Operations
    public void add(Array other);

    public void add(Double scalar);

    public void subtract(Array other);

    public void subtract(Double scalar);

    public void multiply(Array other);

    public void multiply(Double scalar);

    public void divide(Array other);

    public void divide(Double scalar);


}
