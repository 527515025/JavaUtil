
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @author abel
 * excel导出的封装类.
 */
public class ExcelExportUtil {

	/**  定义工作表. */
	private SXSSFWorkbook wb;
	
	/**  定义工作表中的sheet. */
	private Sheet sh;
	
	/**  导出文件行数. */
	private int rownum;
	
	/**  导出文件列数. */
	private int colnum;
	
	/**  文件名. */
	private String fileName;
	
	/**  导出文件全路径. */
	private String fileAllPath;
	
	/**  导出文件列标题. */
	private String[] headers;
	
	/**  导出文件每列代码，用于反射获取对象属性值. */
	private String[] fieldCodes;
	
	/**  设置宽度的list，用于设置每列的宽度. */
	private String[] columnSizes;

	/**
	 * Start.
	 *
	 * @param excelExportSXXSSF            SXXSSF对象,用于生成多个sheet。
	 * @param fileName            文件名
	 * @param headers the headers
	 * @param fieldCodes            model 属性
	 * @param columnSizes the column sizes
	 * @param sheetNum            sheet 的个数
	 * @param datalist the datalist
	 * @param columnAlignLeft the column align left
	 * @return the excel export util
	 * @throws Exception the exception
	 */
	public static ExcelExportUtil start(ExcelExportUtil excelExportSXXSSF, String fileName, String[] headers,
			String[] fieldCodes, String[] columnSizes, int sheetNum, List<?> datalist,
			String columnAlignLeft) throws Exception {

		excelExportSXXSSF.setfileName(fileName);
		excelExportSXXSSF.setHeaders(headers);
		excelExportSXXSSF.setFieldCodes(fieldCodes);
		excelExportSXXSSF.setColumnSizes(columnSizes);
		if (excelExportSXXSSF.getWb() == null) {
			excelExportSXXSSF.setWb(new SXSSFWorkbook(100));// 创建workbook
		}
		SXSSFSheet sheet = excelExportSXXSSF.getWb().createSheet();
		excelExportSXXSSF.getWb().setSheetName(sheetNum, "sheet" + (sheetNum + 1));
		excelExportSXXSSF.setSh(sheet);// 创建sheet
		excelExportSXXSSF.writeTitles(sheetNum);
		excelExportSXXSSF.writeDatasByObject(datalist, columnAlignLeft, sheetNum);
		return excelExportSXXSSF;
	}

	/**
	 * 设置导入文件的标题 开始生成导出excel的标题.
	 *
	 * @param sheetNum the sheet num
	 * @throws Exception the exception
	 */
	private void writeTitles(int sheetNum) throws Exception {
		SXSSFWorkbook workBook = this.getWb();
		SXSSFSheet sheet = workBook.getSheetAt(sheetNum);
		rownum = 0;// 第0行
		colnum = headers.length;// 根据列标题得出列数
		Row row = sheet.createRow(rownum);

		for (int cellnum = 0; cellnum < colnum; cellnum++) {
			Cell cell = row.createCell(cellnum);
			cell.setCellValue(headers[cellnum].toString());

			CellStyle setBorder = workBook.createCellStyle();
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			setBorder.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			Font font = workBook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			setBorder.setFont(font);
			cell.setCellStyle(setBorder);

		}
		for (int cellnum = 0; cellnum < columnSizes.length; cellnum++) {
			sheet.setColumnWidth(cellnum, Integer.parseInt((columnSizes[cellnum].toString())));
		}

	}

	/**
	 * 向导出文件写数据.
	 *
	 * @param datalist            存放Object对象，仅支持单个自定义对象，不支持对象中嵌套自定义对象
	 * @param columnAlignLeft the column align left
	 * @param sheetNum the sheet num
	 * @throws Exception the exception
	 */
	public void writeDatasByObject(List<?> datalist, String columnAlignLeft, int sheetNum) throws Exception {

		SXSSFWorkbook workBook = this.getWb();
		Sheet sheet = workBook.getSheetAt(sheetNum);

		CellStyle alignLeft = workBook.createCellStyle();
		alignLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		alignLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		alignLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		alignLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		alignLeft.setWrapText(true);
		alignLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		alignLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);

		CellStyle alignCenter = workBook.createCellStyle();
		alignCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		alignCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		alignCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		alignCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		alignCenter.setWrapText(true);
		alignCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		alignCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		for (int j = 0; j < datalist.size(); j++) {
			rownum = rownum + 1;
			Row row = sheet.createRow(rownum);

			for (int cellnum = 0; cellnum < fieldCodes.length; cellnum++) {
				Object owner = datalist.get(j);
				Object value = invokeMethod(owner, fieldCodes[cellnum].toString(), new Object[] {});
				Cell cell = row.createCell(cellnum);
				if (columnAlignLeft.contains(cellnum +"")) {
					cell.setCellStyle(alignLeft);
				} else {
					cell.setCellStyle(alignCenter);
				}
				cell.setCellValue(value != null ? value.toString() : "");
			}
		}
	}

	/**
	 * 导出文件.
	 *
	 * @param response the response
	 * @throws Exception the exception
	 */
	public void exportFile(HttpServletResponse response) throws Exception {

		
		//转换服务器时区为东八区
		TimeZone timeZoneSH = TimeZone.getTimeZone("Asia/Shanghai");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.CHINA);
		outputFormat.setTimeZone(timeZoneSH);
		Date date = new Date(System.currentTimeMillis());
		String time = outputFormat.format(date);
		fileName = fileName + time;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition",
				"attachment;filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1") + ".xlsx");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 反射方法，通过get方法获取对象属性.
	 *
	 * @param owner the owner
	 * @param fieldname the fieldname
	 * @param args the args
	 * @return the object
	 * @throws Exception the exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object invokeMethod(Object owner, String fieldname, Object[] args) throws Exception {

		String methodName = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
		Class ownerClass = owner.getClass();

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(owner, args);
	}

	/**
	 * Gets the wb.
	 *
	 * @return the wb
	 */
	public SXSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * Sets the wb.
	 *
	 * @param wb the new wb
	 */
	public void setWb(SXSSFWorkbook wb) {
		this.wb = wb;
	}

	/**
	 * Gets the sh.
	 *
	 * @return the sh
	 */
	public Sheet getSh() {
		return sh;
	}

	/**
	 * Sets the sh.
	 *
	 * @param sh the new sh
	 */
	public void setSh(Sheet sh) {
		this.sh = sh;
	}

	/**
	 * Gets the headers.
	 *
	 * @return the headers
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * Sets the headers.
	 *
	 * @param headers the new headers
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	/**
	 * Gets the column sizes.
	 *
	 * @return the column sizes
	 */
	public String[] getColumnSizes() {
		return columnSizes;
	}

	/**
	 * Sets the column sizes.
	 *
	 * @param columnSizes the new column sizes
	 */
	public void setColumnSizes(String[] columnSizes) {
		this.columnSizes = columnSizes;
	}

	/**
	 * Gets the field codes.
	 *
	 * @return the field codes
	 */
	public String[] getFieldCodes() {
		return fieldCodes;
	}

	/**
	 * Sets the field codes.
	 *
	 * @param fieldCodes the new field codes
	 */
	public void setFieldCodes(String[] fieldCodes) {
		this.fieldCodes = fieldCodes;
	}

	/**
	 * Gets the rownum.
	 *
	 * @return the rownum
	 */
	public int getRownum() {
		return rownum;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getfileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setfileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the colnum.
	 *
	 * @return the colnum
	 */
	public int getColnum() {
		return colnum;
	}

	/**
	 * Gets the file all path.
	 *
	 * @return the file all path
	 */
	public String getFileAllPath() {
		return fileAllPath;
	}

	/**
	 * Sets the file all path.
	 *
	 * @param fileAllPath the new file all path
	 */
	public void setFileAllPath(String fileAllPath) {
		this.fileAllPath = fileAllPath;
	}

}
