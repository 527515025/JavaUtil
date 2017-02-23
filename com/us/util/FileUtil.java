
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.us.cmbms.model.Attachment;

/**
 * 文件处理工具类.
 *
 * @author abel<br>
 * @version 3.0<br>
 * @taskId <br>
 * @CreateDate 2016-05-6 <br>
 */
public class FileUtil {

	/** The log. */
	private static Logger log = Logger.getLogger(FileUtil.class);

	/**
	 *  The Constant pathConfig.
	 *
	 * @param request the request
	 * @return the string
	 * @throws FileUploadException the file upload exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/**
	 * 文件上传.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 * @throws FileUploadException
	 *             the file upload exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public static String upload(HttpServletRequest request)
			throws FileUploadException, IOException {
		String path = "";
		// 文件存放的目录
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		// 创建磁盘文件工厂
		DiskFileItemFactory fac = new DiskFileItemFactory();
		// 创建servlet文件上传组件
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		// 文件列表
		List<FileItem> fileList = null;
		// 解析request从而得到前台传过来的文件
		fileList = upload.parseRequest(request);

		// 保存后的文件名
		String fileName = null;
		// 便利从前台得到的文件列表
		Iterator<FileItem> it = fileList.iterator();

		while (it.hasNext()) {
			FileItem item = it.next();
			// 如果不是普通表单域，当做文件域来处理
			if (!item.isFormField()) {
				String fileType = item.getName().substring(item.getName().lastIndexOf("."));
				fileName = System.currentTimeMillis() + fileType;
				BufferedInputStream in = new BufferedInputStream(item.getInputStream());
				path = path + fileName;
				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File(path)));
				Streams.copy(in, bout, true);
				in.close();
				bout.close();
			}
		}
		return fileName;
	}

	/**
	 * abel 文件上传.
	 *
	 * @param mr the mr
	 * @param filePath the file path
	 * @return the attachment
	 * @throws FileUploadException the file upload exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException the file not found exception
	 */
	public static Attachment upload_attachment(MultipartHttpServletRequest mr, String filePath)
			throws FileUploadException, IOException, FileNotFoundException {
		Attachment attachment = null;

		// 文件存放的目录
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		// 获得file类型的input的name
		Iterator<String> nameList = mr.getFileNames();
		String fileName = nameList.next();
		// 获得到文件

		MultipartFile fm = mr.getFile(fileName);
		double Size = fm.getSize();

		String fileSize = Size + "B";
		if (Size / (1024 * 1024) >= 0.01) {
			fileSize = (double) (Math.round(Size / (1024 * 1024) * 100) / 100.0) + "MB";
		} else if (Size / 1024 >= 0.01) {
			fileSize = (double) (Math.round(Size / 1024 * 100) / 100.0) + "KB";
		}

		// 获得原始文件名字
		String oldFileName = fm.getOriginalFilename();
		// 文件的后缀名
		String filetype ="null";
		if(oldFileName.contains(".")){
			filetype = oldFileName.substring(oldFileName.lastIndexOf('.'));
		}
		String newFileName = UUIDBuild.getUUID() + filetype;
		String realPath = filePath + newFileName;
		BufferedInputStream in = new BufferedInputStream(fm.getInputStream());
		BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File(realPath)));
		Streams.copy(in, bout, true);
		in.close();
		bout.close();
		attachment = new Attachment();
		attachment.setFileName(oldFileName);
		attachment.setFilePath(newFileName);
		attachment.setFileType(filetype);
		attachment.setFlag(1);
		attachment.setFileSize(fileSize);
		attachment.setUploadTime(new Date());

		return attachment;
	}

	/**
	 * 文件下载 abel.
	 *
	 * @param response the response
	 * @param attachment            需要下载的文件
	 * @param filePath the file path
	 */
	public static void downloadFile(HttpServletResponse response, Attachment attachment, String filePath) {
		try {
			filePath = filePath + attachment.getFilePath();
			File file = new File(filePath);
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			response.addHeader("Content-disposition",
					"attachment;filename=\"" + new String(attachment.getFileName().getBytes("gb2312"), "ISO8859-1"));

			response.addHeader("Content-Length", "" + file.length());

			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			os.write(buffer);
			os.flush();
			os.close();
		} catch (IOException ex) {
			log.error("下载文件异常[" + ex.getMessage() + "]");
		}
	}

	/**
	 * 返回页面图片预览.
	 *
	 * @param response the response
	 * @param path the path
	 * @return the object
	 * @throws Exception the exception
	 */
	public static void getPhoto(HttpServletResponse response,String path) throws Exception {
		File file = new File(path);
		FileInputStream fis;
		fis = new FileInputStream(file);

		long size = file.length();
		byte[] temp = new byte[(int) size];
		fis.read(temp, 0, (int) size);
		fis.close();
		byte[] data = temp;
		response.setContentType("image/png");
		OutputStream out = response.getOutputStream();
		out.write(data);
		out.flush();
		out.close();

	}

	/**
	 * abel 删除文件.
	 *
	 * @param sPath            被删除文件路径
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean deleteAttachment(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			flag =file.delete();
		}
		return flag;
	}
	
	/**
	 * 将获取到的文件流转换成文件暂存.
	 *
	 * @param ins the ins
	 * @param file the file
	 */
	public static  void inputstreamtofile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
