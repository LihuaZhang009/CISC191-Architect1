package edu.sdccd.cisc191.app;

import edu.sdccd.cisc191.matrix.*;

public class Test {

    public static void main(String[] args) {

        Double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Matrix A = new Matrix(data);
        System.out.println(A);

        Matrix subA = A.select(new Slice(0, 2), new Slice(0, 2));
        subA.multiply(2.0);
        System.out.println(A);

    }

}
