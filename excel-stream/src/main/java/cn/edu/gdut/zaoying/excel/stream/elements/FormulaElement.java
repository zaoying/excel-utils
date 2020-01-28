package cn.edu.gdut.zaoying.excel.stream.elements;

public class FormulaElement extends NumberElement{
    private StringBuilder formula;
    private StringBuilder value;

    public FormulaElement() {
    }

    public FormulaElement(StringBuilder formula, StringBuilder value) {
        this.formula = formula;
        this.value = value;
    }

    public FormulaElement setFormula(StringBuilder formula) {
        this.formula = formula;
        return this;
    }

    public FormulaElement setValue(StringBuilder value) {
        this.value = value;
        return this;
    }

    public StringBuilder getFormula() {
        return formula;
    }

    public StringBuilder getValue() {
        return value;
    }

    public void reset(){
        super.reset();
        formula = null;
        value = null;
    }
}
