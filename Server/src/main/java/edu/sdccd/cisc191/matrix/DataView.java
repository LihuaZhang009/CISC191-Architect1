package edu.sdccd.cisc191.matrix;

/**
 * DataView is an abstract class that holds a reference to a Double[] data array.
 * How to interpret the 1D flattened data is up to the subclass. The subclass should
 * implement the relevant methods to access the data in the desired way.
 */
public abstract class DataView {

    protected Double[] data;

    public abstract Integer[] getShape();

    public abstract Double get(Integer... args);

    public abstract void set(Double value, Integer... args);

    public abstract String toString();
    
    protected void checkIndex(Integer... args) {
        Integer[] shape = getShape();
        if (args.length != shape.length) {
            throw new IllegalArgumentException("Incorrect number of indices");
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] < 0 || args[i] >= shape[i]) {
                throw new IllegalArgumentException("Index out of bounds");
            }
        }
    }
    
}
