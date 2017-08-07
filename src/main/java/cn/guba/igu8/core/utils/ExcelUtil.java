package cn.guba.igu8.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.lang.Mirror;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;

/**
 * @author zongtao liu</br>
 *         2017年4月21日 下午6:08:50</br>
 */
public class ExcelUtil {

	private static Log log = Logs.get();

	private static String getCellValue(Cell cell) throws Exception {
		if (cell == null) {
			return "";
		}
		switch (cell.getCellTypeEnum()) {
		case BLANK:
			return "";
		case STRING:
			return cell.toString();
		case NUMERIC:
			short tmpDataFormatShot = cell.getCellStyle().getDataFormat();
			/***
			 * yyyy年m月d日--- 31 yyyy年m月------- 57 m月d日---------- 58
			 */
			if (HSSFDateUtil.isCellDateFormatted(cell) || tmpDataFormatShot == 31 || tmpDataFormatShot == 57
					|| tmpDataFormatShot == 58) {
				SimpleDateFormat sdf = null;
				Calendar cal = Calendar.getInstance();
				cal.setTime(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
				if (cal.get(Calendar.HOUR) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0) {
					sdf = new SimpleDateFormat("yyyy/MM/dd");
				} else {
					sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				}
				return sdf.format(cal.getTime()).toString();
			}
			return NumberToTextConverter.toText(cell.getNumericCellValue());
		case FORMULA:
			return cell.toString();
		default:
			throw new IllegalAccessException(String.format("cell [sheet:%s][colunm:%d][row:%d] type error",
					cell.getSheet().getSheetName(), cell.getColumnIndex(), cell.getRowIndex()));
		}
	}

	/***
	 * 根据对应类的名字，进行检索。找到对应的对象列表
	 * 
	 * @param filePath
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getExcelObjList(String filePath, Class<T> clazz) {
		return getExcelObjList(filePath, Strings.lowerFirst(clazz.getSimpleName()), clazz);
	}

	/***
	 * 根据 sheet的name获取 对象列表
	 * 
	 * @param filePath
	 * @param sheetName
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getExcelObjList(String filePath, String sheetName, Class<T> clazz) {
		File file = new File(filePath);
		return getExcelObjList(file, sheetName, clazz);
	}

	/***
	 * 根据 sheet的name获取 对象列表
	 * 
	 * @param file
	 * @param sheetName
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getExcelObjList(File file, String sheetName, Class<T> clazz) {
		List<T> rList = new ArrayList<T>();
		if (file == null) {
			log.debug("file is null ;sheetName==" + sheetName);
			return rList;
		}
		log.debug("filePath==" + file.getPath() + ";sheetName==" + sheetName);
		if (file.exists()) {
			Workbook workbook = null;
			InputStream stream = Streams.fileIn(file);
			try {
				if (file.getName().contains(".xlsx")) {
					workbook = new XSSFWorkbook(stream);
				} else {
					workbook = new HSSFWorkbook(stream);
				}
				Sheet sheet = workbook.getSheet(sheetName);
				rList = getObjectFromSheet(sheet, clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Streams.safeClose(stream);
		}
		return rList;
	}

	public static List<String> getSheetNames(File file) {
		List<String> list = new ArrayList<String>();
		if (file == null) {
			log.debug("file is null ;");
			return list;
		}
		log.debug("filePath==" + file.getPath());
		if (file.exists()) {
			Workbook workbook = null;
			InputStream stream = Streams.fileIn(file);
			try {
				if (file.getName().contains(".xlsx")) {
					workbook = new XSSFWorkbook(stream);
				} else {
					workbook = new HSSFWorkbook(stream);
				}
				int sheetNum = workbook.getNumberOfSheets();
				for (int i = 0; i < sheetNum; i++) {
					list.add(workbook.getSheetName(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Streams.safeClose(stream);
		}
		return list;
	}

	/***
	 * 根据 sheet的index获取 对象列表
	 * 
	 * @param filePath
	 * @param sheetIndex
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getExcelObjList(String filePath, int sheetIndex, Class<T> clazz) {
		File file = new File(filePath);
		return getExcelObjList(file, sheetIndex, clazz);
	}

	/***
	 * 根据 sheet的index获取 对象列表
	 * 
	 * @param file
	 * @param sheetIndex
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> getExcelObjList(File file, int sheetIndex, Class<T> clazz) {
		List<T> rList = new ArrayList<T>();
		if (file == null) {
			log.debug("file is null ;sheetIndex==" + sheetIndex);
			return rList;
		}
		log.debug("filePath==" + file.getPath() + ";sheetIndex==" + sheetIndex);
		if (file.exists()) {
			Workbook workbook = null;
			InputStream stream = Streams.fileIn(file);
			try {
				if (file.getName().contains(".xlsx")) {
					workbook = new XSSFWorkbook(stream);
				} else {
					workbook = new HSSFWorkbook(stream);
				}
				Sheet sheet = workbook.getSheetAt(sheetIndex);
				rList = getObjectFromSheet(sheet, clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Streams.safeClose(stream);
		}
		return rList;
	}

	/***
	 * 从sheet中获取对象 列表
	 * 
	 * @param sheet
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private static <T> List<T> getObjectFromSheet(Sheet sheet, Class<T> clazz) throws Exception {
		List<T> rList = new ArrayList<T>();
		if (sheet == null) {
			return rList;
		}
		int rowCount = sheet.getPhysicalNumberOfRows();
		if (rowCount == 0) {
			return rList;
		}
		Row row = sheet.getRow(0);
		if (row.getCell(0) == null || row.getCell(0).getCellTypeEnum() == CellType.BLANK) {
			return rList;
		}
		int colunmCount = row.getLastCellNum();
		String[] nameData = new String[colunmCount];
		for (int i = 0; i < colunmCount; i++) {
			Cell tmpCell = row.getCell(i);
			nameData[i] = getCellValue(tmpCell);
		}
		Field[] declaredFields = clazz.getDeclaredFields();
		for (int i = 1; i < rowCount; i++) {
			T instance = clazz.newInstance();
			Row tmpRow = sheet.getRow(i);
			if (tmpRow == null) {
				continue;
			}
			for (int j = 0; j < colunmCount; j++) {
				Cell tmpCell = tmpRow.getCell(j);
				if (tmpCell == null) {
					continue;
				}
				String tmpCellValue = getCellValue(tmpCell);
				tmpCell.setCellValue(1);
				boolean has = false;
				for (Field declaredField : declaredFields) {
					if (Strings.equals(declaredField.getName(), nameData[j])) {
						has = true;
						break;
					}
				}
				if (has) {
					Mirror.me(clazz).setValue(instance, nameData[j], tmpCellValue);
				}
			}
			rList.add(instance);
		}
		return rList;
	}

	/**
	 * 写入excel中
	 * 
	 * @param filePath
	 * @param sheetName
	 * @param objList
	 */
	public static <T> void writeExcel(String filePath, String sheetName, List<T> objList) {
		File file = new File(filePath);
		if (file.exists()) {
			writeOldExcel(filePath, sheetName, objList);
		}else{
			createExcel(filePath, sheetName, objList);
		}
	}
	
	private static <T> void writeOldExcel(String filePath, String sheetName, List<T> objList) {
		File file = new File(filePath);
		if (!file.exists()) {
			return ;
		}
		Workbook workbook = null;
		InputStream fileIn = Streams.fileIn(file);
		OutputStream fileOut = null;
		try {
			if (file.getName().contains(".xlsx")) {
				workbook = new XSSFWorkbook(fileIn);
			} else {
				workbook = new HSSFWorkbook(fileIn);
			}
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
			}
			writeToSheet(sheet, objList);
			fileOut = Streams.fileOut(file);
			workbook.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			Streams.safeClose(fileIn);
			Streams.safeClose(fileOut);
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static <T> void createExcel(String filePath, String sheetName, List<T> objList) {
		File file = new File(filePath);
		if (file.exists()) {
			return ;
		}
		Workbook workbook = null;
		OutputStream fileOut = null;
		try {
			if (file.getName().contains(".xlsx")) {
				workbook = new XSSFWorkbook();
			} else {
				workbook = new HSSFWorkbook();
			}
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = workbook.createSheet(sheetName);
			}
			writeToSheet(sheet, objList);
			fileOut = Streams.fileOut(file);
			workbook.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			Streams.safeClose(fileOut);
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 列表写入sheet中
	 * 
	 * @param sheet
	 * @param objList
	 * @throws Exception
	 */
	private static <T> void writeToSheet(Sheet sheet, List<T> objList) throws Exception {
		if (objList == null || objList.size() == 0) {
			return;
		}
		int rowCount = sheet.getPhysicalNumberOfRows();
		T t = objList.get(0);
		Class<?> clazz = t.getClass();
		if (rowCount == 0) {
			Row row0 = sheet.createRow(0);
			Field[] declaredFields = clazz.getDeclaredFields();
			int index = 0;
			for (Field field : declaredFields) {
				Cell tmpCell = row0.createCell(index);
				tmpCell.setCellValue(field.getName());
				index++;
			}
			rowCount++;
		}
		for (T obj : objList) {
			Row row = sheet.createRow(rowCount++);
			Field[] declaredFields = clazz.getDeclaredFields();
			int index = 0;
			for (Field field : declaredFields) {
				Cell tmpCell = row.createCell(index);
				tmpCell.setCellValue(String.valueOf(Mirror.me(clazz).getValue(obj, field)));
				index++;
			}
		}
	}

}
