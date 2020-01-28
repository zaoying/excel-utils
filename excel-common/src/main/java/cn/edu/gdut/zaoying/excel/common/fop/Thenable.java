package cn.edu.gdut.zaoying.excel.common.fop;

//一个标记接口，表示实现该接口的操作会非常耗时，会阻塞当前线程
//A symbol of blocking function which makes current thread stuck
public interface Thenable<I,O> extends Function<I,O>{
}
