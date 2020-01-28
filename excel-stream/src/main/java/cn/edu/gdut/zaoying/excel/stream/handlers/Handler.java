package cn.edu.gdut.zaoying.excel.stream.handlers;

public interface Handler<IN,OUT> extends Handlable<IN,OUT,Throwable>{
    OUT handle(IN input);
}
