package cn.edu.gdut.zaoying.excel.stream.handlers;

import cn.edu.gdut.zaoying.excel.common.fop.Function;
import cn.edu.gdut.zaoying.excel.stream.elements.Element;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;

import java.util.Stack;

public interface ElementHandler {
    void onStart(Element element);
    void onEnd(Element element);

    static EmptyHandler emptyHandler(){
        return new EmptyHandler(null);
    }

    static EmptyHandler onStart(Function.VoidFunction<Element> onStart){
        return new EmptyHandler(onStart, null);
    }

    static EmptyHandler onEnd(Function.VoidFunction<Element> onEnd){
        return new EmptyHandler(null, onEnd);
    }

    static EmptyHandler onBoth(Function.VoidFunction<Element> onBoth){
        return new EmptyHandler(onBoth);
    }

    class EmptyHandler implements ElementHandler {

        private Function.VoidFunction<Element> onStart;

        private Function.VoidFunction<Element> onEnd;

        public EmptyHandler(Function.VoidFunction<Element> onBoth) {
            this.onStart = onBoth;
            this.onEnd = onBoth;
        }

        public EmptyHandler(Function.VoidFunction<Element> onStart, Function.VoidFunction<Element> onEnd) {
            this.onStart = onStart;
            this.onEnd = onEnd;
        }

        @Override
        public void onStart(Element element) {
            if(onStart != null){
                onStart.call(element);
            }
        }

        @Override
        public void onEnd(Element element) {
            if(onEnd != null){
                onEnd.call(element);
            }
        }

        public EmptyHandler setOnStart(Function.VoidFunction<Element> onStart) {
            this.onStart = onStart;
            return this;
        }

        public EmptyHandler setOnEnd(Function.VoidFunction<Element> onEnd) {
            this.onEnd = onEnd;
            return this;
        }
    }

    static Handler<Stack<Element>, String> cellElementHandler(Styles stylesTable, SharedStrings sharedStringsTable, DataFormatter dataFormatter){
        return new CellHandler(stylesTable, sharedStringsTable, dataFormatter);
    }
}
