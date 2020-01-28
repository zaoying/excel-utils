package cn.edu.gdut.zaoying.excel.legacy.stream.handlers;

import org.apache.poi.hssf.record.*;

public class CellRecordHandler implements Handler<CellRecord> {

    private Handler<CellRecord> beforeHandler;

    private Handler<BoolErrRecord> boolErrRecordHandler;

    private Handler<RKRecord> rkRecordHandler;

    private Handler<NumberRecord> numberRecordHandler;

    private Handler<LabelSSTRecord> labelSSTRecordHandler;

    private Handler<FormulaRecord> formulaRecordHandler;

    private Handler<CellRecord> afterHandler;

    public CellRecordHandler() {
        beforeHandler = cell -> {};
        boolErrRecordHandler = cell -> {};
        rkRecordHandler = cell -> {};
        numberRecordHandler = cell -> {};
        labelSSTRecordHandler = cell -> {};
        formulaRecordHandler = cell -> {};
        afterHandler = cell -> {};
    }

    @Override
    public void handle(CellRecord cellRecord) {
        if(beforeHandler != null){
            beforeHandler.handle(cellRecord);
        }
        switch (cellRecord.getSid()){
            case BoolErrRecord.sid:
                boolErrRecordHandler.handle((BoolErrRecord) cellRecord);
                break;
            case RKRecord.sid:
                rkRecordHandler.handle((RKRecord) cellRecord);
                break;
            case NumberRecord.sid:
                numberRecordHandler.handle((NumberRecord) cellRecord);
                break;
            case LabelSSTRecord.sid:
                labelSSTRecordHandler.handle((LabelSSTRecord) cellRecord);
                break;
            case FormatRecord.sid:
                formulaRecordHandler.handle((FormulaRecord) cellRecord);
        }
        if(afterHandler != null){
            afterHandler.handle(cellRecord);
        }
    }

    public CellRecordHandler setBeforeHandler(Handler<CellRecord> beforeHandler) {
        this.beforeHandler = beforeHandler;
        return this;
    }

    public CellRecordHandler setBoolErrRecordHandler(Handler<BoolErrRecord> boolErrRecordHandler) {
        this.boolErrRecordHandler = boolErrRecordHandler;
        return this;
    }

    public CellRecordHandler setRkRecordHandler(Handler<RKRecord> rkRecordHandler) {
        this.rkRecordHandler = rkRecordHandler;
        return this;
    }

    public CellRecordHandler setNumberRecordHandler(Handler<NumberRecord> numberRecordHandler) {
        this.numberRecordHandler = numberRecordHandler;
        return this;
    }

    public CellRecordHandler setLabelSSTRecordHandler(Handler<LabelSSTRecord> labelSSTRecordHandler) {
        this.labelSSTRecordHandler = labelSSTRecordHandler;
        return this;
    }

    public CellRecordHandler setFormulaRecordHandler(Handler<FormulaRecord> formulaRecordHandler) {
        this.formulaRecordHandler = formulaRecordHandler;
        return this;
    }

    public CellRecordHandler setAfterHandler(Handler<CellRecord> afterHandler) {
        this.afterHandler = afterHandler;
        return this;
    }

}
