package cn.edu.gdut.zaoying.excel.legacy.usermodel;

import cn.edu.gdut.zaoying.excel.common.constants.CellType;
import cn.edu.gdut.zaoying.excel.common.annotations.ExcelSheet;
import cn.edu.gdut.zaoying.excel.common.annotations.SheetHeader;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.util.*;

public class Sheet<E> {
	private List<String> heads;
	private Map<String,Column> columns;
	private List<List<Cell>> rows;
	private RowTraversal<E> rowTraversal;
	private ColumnTraversal columnTraversal;
	private Map<String,HSSFCellStyle> cellStyleMap;
	private HSSFDataFormat dateFormator;
	private HSSFSheet sheet;
	public Sheet(HSSFSheet sheet) {
		this.sheet=sheet;
        heads=new ArrayList<String>();
        columns=new HashMap<String,Column>();
        rows=new ArrayList<List<Cell>>();
        cellStyleMap=new HashMap<String, HSSFCellStyle>();
        dateFormator =sheet.getWorkbook().createDataFormat();
        if(sheet.getLastRowNum()>0){
		HSSFRow firstRow=sheet.getRow(0);
		LinearTraversal<String> headTraversal=new LinearTraversal<String>(){
			public void each(Cell cell,List<String> rowValue) {
				heads.add(cell.getStringCellValue());
			}
		};
		traversalRow(firstRow,headTraversal,null);

		LinearTraversal<Cell> dataTraversal=new LinearTraversal<Cell>(){
			public void each(Cell cell,List<Cell> rowValue) {
				List<Cell> col=columns.get(heads.get(cell.getColumnIndex()));
				if (col == null) {
					col = new ArrayList<Cell>();
				}
				col.add(cell);
				rowValue.add(cell);
			}
			
		};
		Iterator<Row> iterator=sheet.rowIterator();
		iterator.next();
		while(iterator.hasNext()){
			List<Cell> row=new ArrayList<Cell>();
			traversalRow(iterator.next(),dataTraversal,row);
			rows.add(row);
		}
        }
	}
	public HSSFCellStyle getCellStyle(String styleName){
	    return cellStyleMap.get(styleName);
    }
    public void putCellStyleMap(String styleName,HSSFCellStyle style){
	    cellStyleMap.put(styleName,style);
    }
    public void eachRow(RowTraversal<E> rowTraversal){
        Iterator<Row> rowIterator=sheet.rowIterator();
        while (rowIterator.hasNext()){
            Row row=rowIterator.next();
            rowTraversal.traversalRow(row,sheet.getFirstRowNum());
        }
    }
    public RowTraversal<E> getRowTraversal() {
        return this.rowTraversal;
    }

    public void setRowTraversal(RowTraversal<E> rowTraversal) {
        this.rowTraversal = rowTraversal;
    }

    public ColumnTraversal getColumnTraversal() {
        return this.columnTraversal;
    }

    public void setColumnTraversal(ColumnTraversal columnTraversal) {
        this.columnTraversal = columnTraversal;
    }

    public <T> void traversalRow(Row row, LinearTraversal<T> traversal, List<T> rowValue){
		Iterator<Cell> iterator=row.cellIterator();
		while(iterator.hasNext()){
			Cell cell=iterator.next();
			traversal.each(cell,rowValue);
		}
	}
	
	public <T> void traversalColumn(Column column, LinearTraversal<T> traversal, List<T> colValue){
		Iterator<Cell> iterator=column.iterator();
		while(iterator.hasNext()){
			Cell cell=iterator.next();
			traversal.each(cell,colValue);
		}
	}
	
	public KeyValuePair<String> getKeyValuePair(int col, int row){
		String key=heads.get(col);
		KeyValuePair<String> kvp=new KeyValuePair<String>();
		kvp.setKey(key);
		kvp.setValue(columns.get(key).get(row).getStringCellValue());
		return kvp;
	}

	public Map<String,String> getColumnWithHeads(int colNum){
		HashMap<String,String> map=new HashMap<String,String>();
		for(String key:heads){
			map.put(key, columns.get(key).get(colNum).getStringCellValue());
		}
		return map;
	}
	
	public Column getColumn(String head){
		return columns.get(head);
	}
	
	public Column getColumn(int colNum){
		return columns.get(heads.get(colNum));
	}
	
	public Row getRow(int rowNum){
		return sheet.getRow(rowNum);
	}

	public List<String> getHeads() {
		return heads;
	}

	public void setHeads(List<String> heads) {
		this.heads = heads;
	}

	public Map<String, Column> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, Column> columns) {
		this.columns = columns;
	}

	public HSSFSheet getHSSFSheet() {
		return sheet;
	}

	public void setHSSFSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

    public Map<String,Cell> getMapFromRow(Row row){
        Map<String,Cell>  map=new HashMap<String,Cell>();
        for (Cell cell : row) {
            int col=cell.getColumnIndex();
            String head=heads.get(col);
            map.put(head,cell);
        }
        return map;
    }
    public E getRow(int rowNum,E e){
        Row row=sheet.getRow(rowNum);
        Map<String,Cell> map=getMapFromRow(row);
        Field[] fields=e.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            SheetHeader sheetHeader=field.getAnnotation(SheetHeader.class);
            if(null == sheetHeader)continue;
            String head=sheetHeader.name();
            Cell value=map.get(head);
            try {
                field.set(e,value);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
        return e;
    }
    public List<Map<String,Cell>> getRowMaps(){
	    List<Map<String,Cell>> rowMaps=new LinkedList<Map<String, Cell>>();
        Iterator<Row> rowIterator=sheet.rowIterator();
	    while (rowIterator.hasNext()){
            rowMaps.add(getMapFromRow(rowIterator.next()));
        }
        return rowMaps;
    }
//	public List<E> getRows() throws IllegalAccessException, InstantiationException {
//		List<E> rowList=new LinkedList<E>();
//		List<Field> fields=new LinkedList<Field>();
//		for (String head : heads) {
//			Class<E> clazz=Class;
//            try {
//                Field field=clazz.getDeclaredField(head);
//                field.setAccessible(true);
//                fields.add(field);
//            } catch (NoSuchFieldException e1) {
//                e1.printStackTrace();
//            }
//        }
//		return rowList;
//	}

	public void setRows(List<List<Cell>> rows) {
		this.rows = rows;
	}
	
	public void appendRow(E e) throws IllegalArgumentException, IllegalAccessException{
		int lastRowNum = getHSSFSheet().getLastRowNum();
		HSSFRow row= sheet.createRow(lastRowNum+1);
		Class<?> clazz=e.getClass();
		if(clazz.isAnnotationPresent(ExcelSheet.class)){
		    boolean editable=clazz.getAnnotation(ExcelSheet.class).editable();
		    if(!editable)getHSSFSheet().protectSheet("editable");
			Field[] fields=clazz.getDeclaredFields();
			int i=0;
			HSSFCellStyle unLock = this.getCellStyle("unLock");
			if(null == unLock){
			    unLock = sheet.getWorkbook().createCellStyle();
				//lock与editable的逻辑刚好相反
                unLock.setLocked(editable);
				//设置边框宽度
                unLock.setBorderLeft(BorderStyle.THIN);
                unLock.setBorderRight(BorderStyle.THIN);
                unLock.setBorderTop(BorderStyle.THIN);
                unLock.setBorderBottom(BorderStyle.THIN);
				//设置变宽颜色
                unLock.setLeftBorderColor(IndexedColors.SEA_GREEN.index);
                unLock.setRightBorderColor(IndexedColors.SEA_GREEN.index);
                unLock.setTopBorderColor(IndexedColors.SEA_GREEN.index);
                unLock.setBottomBorderColor(IndexedColors.SEA_GREEN.index);
                unLock.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				//设置前景色
                unLock.setFillForegroundColor(IndexedColors.LIME.index);
				//设置文字居中
                unLock.setAlignment(HorizontalAlignment.CENTER);
			    putCellStyleMap("unLock",unLock);
            }
			HSSFCellStyle center = this.getCellStyle("center");
			if(null == center){
                center = sheet.getWorkbook().createCellStyle();
				//设置水平居中
                center.setAlignment(HorizontalAlignment.CENTER);
			    putCellStyleMap("center",center);
            }
			for(Field field:fields){
				SheetHeader header=field.getAnnotation(SheetHeader.class);
				if(header!=null){
					//例外情况
                    boolean except = header.except();
					final HSSFCell cell=row.createCell(i++);
					if(!editable){
						if (except) {
							cell.setCellStyle(unLock);
						}
						else {
							//水平居中
							cell.setCellStyle(center);
						}
					}
					field.setAccessible(true);
					CellType cellType=header.type();
                    Object value=field.get(e);
                    if(value==null){
                    	if(cellType==CellType.FORMULA){
							cell.setCellType(org.apache.poi.ss.usermodel.CellType.FORMULA);
							cell.setCellFormula(header.formula());
						}
						else {
							cell.setCellType(org.apache.poi.ss.usermodel.CellType._NONE);
						}
					}
                    else {
                        if(cellType==CellType.FORMULA){
                        }
						else if(value instanceof Date){
							cellType=CellType.DATE_TIME;
						}
						else if(value instanceof Number){
							cellType=CellType.NUMBER;
						}
                        switch(cellType){
                            case DATE_TIME:
                                cell.setCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC);
                                cell.setCellValue((Date) value);
                                HSSFCellStyle dateFormat=getCellStyle(header.pattern());
                                if(dateFormat==null){
                                    dateFormat=sheet.getWorkbook().createCellStyle();
                                    putCellStyleMap(header.pattern(),dateFormat);
                                }
                                dateFormat.setDataFormat(dateFormator.getFormat(header.pattern()));
                                cell.setCellStyle(dateFormat);
                                break;
                            case NUMBER:
                                Object object=field.get(e);
                                double aDouble=Double.parseDouble(String.valueOf(object));
                                cell.setCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC);
                                cell.setCellValue(aDouble);
                                HSSFCellStyle numberFormat=getCellStyle(header.numberFormat());
                                if(numberFormat==null){
                                    numberFormat=sheet.getWorkbook().createCellStyle();
                                    putCellStyleMap(header.numberFormat(),numberFormat);
                                }
                                numberFormat.setDataFormat(dateFormator.getFormat(header.numberFormat()));
                                cell.setCellStyle(numberFormat);
                                break;
                            case FORMULA:
                                cell.setCellType(org.apache.poi.ss.usermodel.CellType.FORMULA);
                                cell.setCellFormula(header.formula());
                                break;
                            default:
                                cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
                                cell.setCellValue(String.valueOf(value));
                        }
					}
				}
				else {
					Logger.info("警告：找不到SheetHeader注解");
				}
			}
		}
	}
}
