package cn.edu.gdut.zaoying.excel.legacy.usermodel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CellUtil{
	public static ThreadLocal<DecimalFormat> decimalFormatThreadLocal=new ThreadLocal<DecimalFormat>() {
        DecimalFormat format;

        @Override
        public DecimalFormat get() {
            if (format == null) {
                format = new DecimalFormat();
            }
            return format;
        }
    };
	public static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal=new ThreadLocal<SimpleDateFormat>(){
        SimpleDateFormat format;
		@Override
		public SimpleDateFormat get() {
		    if(format==null){
		        format=new SimpleDateFormat();
            }
			return format;
		}
	};
	
	public static String getString(Cell cell) {
		String textValue="";
		if(cell==null) {
			return textValue;
		}
		switch(cell.getCellType()){
			case ERROR:
				textValue+=cell.getErrorCellValue();
				break;
			case FORMULA:
				textValue=cell.getCellFormula();
				break;
			case BOOLEAN:
				textValue+=cell.getBooleanCellValue();
				break;
			case NUMERIC:
				if(HSSFDateUtil.isCellDateFormatted(cell)){
				    short format = cell.getCellStyle().getDataFormat();
				    if(format == 14 || format == 31 || format == 57 || format == 58){  
				        //日期
				    	textValue+=printDate(cell.getDateCellValue()); 
				    }else if (format == 20 || format == 32) {  
				        //时间  
				    	textValue+=printTime(cell.getDateCellValue()); 
				    }
				    else {
				        textValue+=cell.getDateCellValue().getTime();
                    }
				}
				else {
					textValue+=printDecimal(cell.getNumericCellValue());
				}
				break;
			case STRING:
				textValue=cell.getStringCellValue();
				break;
			case _NONE:
				break;
				default:
		}
		return textValue;
	}
	
	public static double getDouble(Cell cell){
		double d=0;
		if(cell.getCellType()==CellType.NUMERIC) {
			d=cell.getNumericCellValue();
		} else if(cell.getCellType()==CellType.BLANK) {
			return 0;
		} else if(cell.getCellType()==CellType.STRING){
		    String value=cell.getStringCellValue();
		    if(value==null||value.trim().length()<1) {
				return 0;
			}
		    d=Double.parseDouble(cell.getStringCellValue());
        }
		return d;
	}
	
	public static float getFloat(Cell cell){
		float f=0.f;
		if(cell.getCellType()==CellType.NUMERIC) {
			f=(float) cell.getNumericCellValue();
		} else if(cell.getCellType()==CellType.BLANK) {
			return 0;
		} else if(cell.getCellType()==CellType.STRING){
            String value=cell.getStringCellValue();
            if(value==null||value.trim().length()<1) {
				return 0;
			}
            f=Float.parseFloat(cell.getStringCellValue());
        }
		return f;
	}

	public static Date getDate(Cell cell,String format){
		Date date=null;
		if(cell.getCellType()==CellType.NUMERIC) {
			return cell.getDateCellValue();
		} else if(cell.getCellType()==CellType.BLANK) {
			return null;
		} else if(cell.getCellType()==CellType.STRING){
			simpleDateFormatThreadLocal.get().applyPattern(format);
			try {
				date= simpleDateFormatThreadLocal.get().parse(cell.getStringCellValue());
			} catch (ParseException e) {
				Logger.info("无效日期格式："+cell.getStringCellValue());
			}
		}
		return date;
	}

	public static String printTime(Date value){
        return formatDate(value,"yyyy-MM-dd HH:mm:ss");
	}
	
	public static String printDate(Date value){
        return formatDate(value,"yyyy-MM-dd");
	}
	
	public static String printDateTime(Date value){
        return formatDate(value,"yyyy-MM-dd HH:mm:ss");
	}
	
	public static String printDecimal(double value){
		return formatDecimal(value,"#0.##");
	}

    /**
     * 按照Pattern指定的格式输出数字
     * @param number 数字（双精度浮点Double、长整型Long）
     * @param pattern 格式，例子："#0.##"
     * @return
     */
	public static String formatDecimal(double number,String pattern){
	    DecimalFormat decimalFormat=decimalFormatThreadLocal.get();
	    decimalFormat.applyPattern(pattern);
	    return decimalFormat.format(number);
    }
    /**
     * 根据Pattern指定的格式输出日期
     * @param date 待格式化日期
     * @param pattern 模式
     * @return
     */
	public static String formatDate(Date date, String pattern){
		SimpleDateFormat simpleDateFormat=simpleDateFormatThreadLocal.get();
		simpleDateFormat.applyPattern(pattern);
		return simpleDateFormat.format(date);
	}
    /**
     * 根据Pattern指定的格式输出时间
     * @param time 时间，单位：毫秒
     * @param pattern 模式
     * @return
     */
	public static String formatTime(double time, String pattern){
		SimpleDateFormat simpleDateFormat=simpleDateFormatThreadLocal.get();
		simpleDateFormat.applyPattern(pattern);
		return simpleDateFormat.format(time);
	}
}
