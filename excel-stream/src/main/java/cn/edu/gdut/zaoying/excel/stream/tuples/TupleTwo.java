package cn.edu.gdut.zaoying.excel.stream.tuples;

public class TupleTwo<A,B> {
    private A a;
    private B b;

    public A getA() {
        return a;
    }

    public TupleTwo(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
