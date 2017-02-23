
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ReflectionUtils;

/**
 * 解析简单的excel文件并返回 Object (with type T) list
 * 
 *
 *
 *
 * @author abel
 * @param <T>
 *            the generic type
 */

public class SimpleExcelReader<T> {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SimpleExcelReader.class);

	/** The clazz. */
	private Class<T> clazz;

	/** The fields. */
	private List<ExcelColumn> fields;

	/** The ingore first N lines. */
	private int ingoreFirstNLines = 0;

	/**
	 * Instantiates a new simple excel reader.
	 *
	 * @param clazz
	 *            the clazz
	 * @param fields
	 *            the fields
	 * @param ingoreFirstNLines
	 *            the ingore first N lines
	 */
	public SimpleExcelReader(Class<T> clazz, List<ExcelColumn> fields, int ingoreFirstNLines) {
		this.clazz = clazz;
		this.fields = fields;
		this.ingoreFirstNLines = ingoreFirstNLines;
	}

	/**
	 * Instantiates a new simple excel reader.
	 *
	 * @param clazz
	 *            the clazz
	 * @param fields
	 *            the fields
	 */
	public SimpleExcelReader(Class<T> clazz, List<ExcelColumn> fields) {
		this(clazz, fields, 1);
	}

	/**
	 * Get the object detail list from excel.
	 *
	 * @param filePath
	 *            the file path
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public List<T> getListFromExcel(String filePath) throws Exception {
		List<T> result = new ArrayList<T>();

		try {

			Workbook wb = readFile(filePath);

			log.debug("Data dump:");

			for (int k = 0; k < wb.getNumberOfSheets(); k++) {
				Sheet sheet = wb.getSheetAt(k);
				int rows = sheet.getPhysicalNumberOfRows();
				log.debug("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows + " row(s).");
				for (int r = 0; r < rows; r++) {

					if (r < ingoreFirstNLines) {
						continue;
					}

					Row row = sheet.getRow(r);
					if (row == null) {
						continue;
					}

					T record = processOneRecord(row);
					result.add(record);
				}
			}

			wb.close();

			return result;
		} catch (Exception e) {
			throw new Exception("failed to read excel to pojo list", e);
		}
	}

	/**
	 * Process one record.
	 *
	 * @param row
	 *            the row
	 * @return the t
	 * @throws Exception
	 *             the exception
	 */
	private T processOneRecord(Row row) throws Exception {
		int cells = row.getPhysicalNumberOfCells();
		log.debug("ROW " + row.getRowNum() + " has " + cells + " cell(s).");

		T target = clazz.newInstance();

		for (int c = 0; c < cells; c++) {
			Cell cell = row.getCell(c);
			String cellValueStr = getCellValueStr(cell);

			ExcelColumn excelColumn = fields.get(c);
			Field field = ReflectionUtils.findField(clazz, excelColumn.getColumnName());
			ReflectionUtils.makeAccessible(field);

			switch (excelColumn.getValueType()) {
			case INTEGER:
				ReflectionUtils.setField(field, target, new Integer(cellValueStr));
				break;
			case DOUBLE:
				ReflectionUtils.setField(field, target, new Double(cellValueStr));
				break;
			case STRING:
				ReflectionUtils.setField(field, target, cellValueStr);
				break;
			case IGNORE:
				// ignore the value
				break;
			default:
				break;
			}
		}
		return target;
	}

	/**
	 * Get the object detail cell value str.
	 *
	 * @param cell
	 *            the cell
	 * @return the object
	 */
	private String getCellValueStr(Cell cell) {
		String value = null;

		switch (cell.getCellType()) {

		case HSSFCell.CELL_TYPE_FORMULA:
			value = "" + cell.getCellFormula();
			break;

		case HSSFCell.CELL_TYPE_NUMERIC:
			value = "" + cell.getNumericCellValue();
			break;

		case HSSFCell.CELL_TYPE_STRING:
			value = "" + cell.getStringCellValue();
			break;
		default:
			break;
		}

		return value;
	}

	/**
	 * Read file.
	 *
	 * @param fileName
	 *            the file name
	 * @return the workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Workbook readFile(String fileName) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
			if (fileName.endsWith(".xlsx")) {
				return new XSSFWorkbook(is);
			} else {
				return new HSSFWorkbook(is);
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * The Class ExcelColumn.
	 *
	 * @Project: kulm
	 * @File_Name: SimpleExcelReader.java
	 * @Package: com.us.cmbms.util
	 * @Author: abel
	 * @Date: 2016
	 * @Version: V1.0
	 */
	public static class ExcelColumn {

		/** The column name. */
		private String columnName;

		/** The value type. */
		private ExcelValueType valueType;

		/**
		 * Instantiates a new excel column.
		 *
		 * @param columnName
		 *            the column name
		 * @param valueType
		 *            the value type
		 */
		public ExcelColumn(String columnName, ExcelValueType valueType) {
			this.columnName = columnName;
			this.valueType = valueType;
		}

		/**
		 * Get the object detail column name.
		 *
		 * @return the object
		 */
		public String getColumnName() {
			return columnName;
		}

		/**
		 * Sets the column name.
		 *
		 * @param columnName
		 *            the new column name
		 */
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		/**
		 * Get the object detail value type.
		 *
		 * @return the object
		 */
		public ExcelValueType getValueType() {
			return valueType;
		}

		/**
		 * Sets the value type.
		 *
		 * @param valueType
		 *            the new value type
		 */
		public void setValueType(ExcelValueType valueType) {
			this.valueType = valueType;
		}

	}

	/**
	 * The Enum ExcelValueType.
	 *
	 * @Project: kulm
	 * @File_Name: SimpleExcelReader.java
	 * @Package: com.us.cmbms.util
	 * @Author: abel
	 * @Date: 2016
	 * @Version: V1.0
	 */
	public enum ExcelValueType {

		/** The integer. */
		INTEGER,
		/** The string. */
		STRING,
		/** The double. */
		DOUBLE,
		/** The ignore. */
		IGNORE;
	}
}
