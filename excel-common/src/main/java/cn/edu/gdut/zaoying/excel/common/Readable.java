package cn.edu.gdut.zaoying.excel.common;

import cn.edu.gdut.zaoying.excel.common.handlers.ErrorHandler;

public interface Readable {

    Readable handleError(ErrorHandler errorHandler);

    ErrorHandler errorHandler();

    void execute();
}
