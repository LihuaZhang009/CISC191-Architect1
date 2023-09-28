package edu.sdccd.cisc191.matrix;

public class Slice {

    private int start;
    private int end;
    private int step;

    public Slice(int start, int end, int step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public Slice(int start, int end) {
        this(start, end, 1);
    }

    public Slice(int end) {
        this(0, end, 1);
    }

    public Slice() {
        this(0, -1, 1);
    }

    public int getStart() { return start; }
    public int getEnd() { return end; }
    public int getStep() { return step; }

}
