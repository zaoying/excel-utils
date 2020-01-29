package cn.edu.gdut.zaoying.excel.stream;

import cn.edu.gdut.zaoying.excel.stream.handlers.CellHandler;
import cn.edu.gdut.zaoying.excel.common.utils.Configurable;
import cn.edu.gdut.zaoying.excel.common.utils.ContextMap;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.junit.Test;

public class ConfigurableTest {
    @Test
    public void configCellHandler() throws InstantiationException, IllegalAccessException {
        Configurable.from(new CellHandler(null, null, null))
                .buildContextMap(ContextMap::order)
                .callMethod("setBooleanHandler", Handlers.booleanHandler())
                .config("defaultHandler", Handlers.defaultHandler())
                .config(new DataFormatter())
                .config(contextMap -> contextMap.put("errorHandler", Handlers.errorHandler()))
                .configAll(ContextMap.normal())
                .build();
    }
    @Test
    public void configCellHandlerFromHandlers() throws InstantiationException, IllegalAccessException {
        Configurable.from(new CellHandler(null, null, null))
                .configAll(Handlers.class)
                .build();
    }
}
