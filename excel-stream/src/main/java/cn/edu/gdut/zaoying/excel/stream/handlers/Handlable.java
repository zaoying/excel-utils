package cn.edu.gdut.zaoying.excel.stream.handlers;

public interface Handlable<IN,OUT,EX extends Throwable>{
    OUT handle(IN input) throws EX;
}
