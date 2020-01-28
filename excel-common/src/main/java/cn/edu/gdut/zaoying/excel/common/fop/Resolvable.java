package cn.edu.gdut.zaoying.excel.common.fop;

public interface Resolvable<O> {
    void fulfill(O result);
    void reject(Throwable throwable);
}
