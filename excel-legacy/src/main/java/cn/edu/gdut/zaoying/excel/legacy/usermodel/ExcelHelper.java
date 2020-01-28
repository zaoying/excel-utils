package cn.edu.gdut.zaoying.excel.legacy.usermodel;

import cn.edu.gdut.zaoying.excel.common.annotations.ExcelSheet;
import cn.edu.gdut.zaoying.excel.common.annotations.SheetHeader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelHelper {

	private HSSFWorkbook book;
	private ExcelHelper(POIFSFileSystem fs) throws IOException {
        book=new HSSFWorkbook(fs);
	}
	
	private ExcelHelper(){
		book=new HSSFWorkbook();
	}

	public static ExcelHelper importExcel(File file) throws IOException{
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		// 打开HSSFWorkbook
        POIFSFileSystem fs = new POIFSFileSystem(in);
		return new ExcelHelper(fs);
	}
	public static ExcelHelper importExcel(InputStream is) throws IOException{
		// 打开HSSFWorkbook
        POIFSFileSystem fs = new POIFSFileSystem(is);
		return new ExcelHelper(fs);
	}
	public void close(){
        try {
            if(book!=null){
                book.close();
            }
        } catch (IOException e) {
            Logger.info("文件流异常");
        }
        finally {
            book=null;
        }
    }
	public <E> Sheet<E> getSupportSheet(int index){
		return new Sheet<E>(book.getSheetAt(index));
	}
	
	public <E> Sheet<E> getSupportSheet(HSSFSheet sheet){
		return new Sheet<E>(sheet);
	}
	
	public static ExcelHelper exportExcel(){
		return new ExcelHelper();
	}
	
	public HSSFSheet createSheet(){
		return book.createSheet(null);
	}
	
	public HSSFSheet createSheet(String sheetName){
		HSSFSheet sheet;
		if(sheetName==null)sheet=book.createSheet();
		else sheet=book.createSheet(sheetName);
		return sheet;
	}
	
	public <T> Sheet<T> createSheet(Class<T> clazz){
		return createSheet(clazz,null);
	}
	
	public <T> Sheet<T> createSheet(Class<T> clazz,String sheetName){
		HSSFSheet sheet=createSheet(sheetName);
		Sheet<T> supportSheet=new Sheet<T>(sheet);
		List<String> heads=supportSheet.getHeads();
		if(clazz.isAnnotationPresent(ExcelSheet.class)){
            ExcelSheet excelSheet=clazz.getAnnotation(ExcelSheet.class);
            if(excelSheet.forceFormulaRecalculation()){
                sheet.setForceFormulaRecalculation(true);
            }
			Field[] fields=clazz.getDeclaredFields();
			int i=0;
			Row row=sheet.createRow(0);
			CellStyle style = sheet.getWorkbook().createCellStyle();
			Font font = sheet.getWorkbook().createFont();
			style.setAlignment(HorizontalAlignment.CENTER);
			//设置边框宽度
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.MEDIUM);
			//设置变宽颜色
			style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.index);
			style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.index);
			style.setBottomBorderColor(IndexedColors.RED.index);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			//设置表头背景色
			style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.index);
			//字体加粗
			font.setBold(true);
			style.setFont(font);
			for(Field field:fields){
				SheetHeader header=field.getAnnotation(SheetHeader.class);
				if(header!=null){
                    String columnName=header.name();
					heads.add(columnName);
					Cell cell=row.createCell(i++);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(columnName);
					//表头设置文字居中
					cell.setCellStyle(style);
					if(header.autoSizeColumn()){
						style.setShrinkToFit(true);
						if("保单号".equals(columnName)){
							//设置4倍列名宽度
							sheet.setColumnWidth(i-1,columnName.length()*2048);
						}else if("批单号".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*2048);
						}else if("保费".equals(columnName)){
							//设置2倍列名宽度
							sheet.setColumnWidth(i-1,columnName.length()*1024);
						}else if("规则ID".equals(columnName)){
							//设置1.5倍列名宽度
							sheet.setColumnWidth(i-1,columnName.length()*1536);
						}else if("机构".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*2048);
						}else if("代理人".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1024);
						}else if("业务员".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1024);
						}else if("出单员".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1024);
						}else if("业务来源".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if("险种".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*3072);
						}else if("修改原因".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1024);
						}else if("核保日期".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if("实收日期".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if("实收核算日期".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if("终保日期".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if("起保日期".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if ("险类".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*2048);
						}else if ("分公司".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1300);
						}else if ("支公司".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1400);
						}else if ("机构名称".equals(columnName)){
							sheet.setColumnWidth(i-1,columnName.length()*1000);
						}else if ("费用类型".equals(columnName)) {
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else if ("险类".equals(columnName)) {
							sheet.setColumnWidth(i-1,columnName.length()*3072);
						}else if ("月份".equals(columnName)) {
							sheet.setColumnWidth(i-1,columnName.length()*2048);
						}else if ("备注".equals(columnName)) {
							sheet.setColumnWidth(i-1,columnName.length()*2048);
						}else if ("录入日期".equals(columnName)) {
							sheet.setColumnWidth(i-1,columnName.length()*1024);
						}else if ("修改日期".equals(columnName)) {
							sheet.setColumnWidth(i-1,columnName.length()*768);
						}else{
							//默认宽度比列名宽度略大
							sheet.setColumnWidth(i-1,columnName.length()*600);
						}
                    }
				}
			}
		}
		else {
			Logger.info("错误：找不到ExcelSheet注解");
		}
		return supportSheet;
	}

    /**
     * 导出到文件流
     * @param out 文件流
     * @param close 导出后是否立即关闭，选否需要手动关闭
     * @throws IOException
     */
	public void exportToStream(OutputStream out,boolean close){
        try {
            book.write(out);
            if(close&&book!=null){
                book.close();
            }
        } catch (IOException e) {
            Logger.info("文件流异常");
        }
        finally {
            book=null;
        }
    }

    /**
     * 导出到文件流立即关闭
     * @param out 文件流
     * @throws IOException
     */
    public void exportToStream(OutputStream out){
	    exportToStream(out,true);
    }
}
