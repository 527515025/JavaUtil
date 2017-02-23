
import com.us.cmbms.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 基本的工具类.
 *
 * @author abel<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016-01-18 <br>
 */
public class CommonUtil {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(CommonUtil.class);

	/**
	 * 获得当前用户.
	 *
	 * @param request the request
	 * @return the current user
	 */
	public static User getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object ob = session.getAttribute("auth_user");
		User user = (User) ob;
		return user;
	}

	/**
	 * 获得当前用户ID.
	 *
	 * @param request the request
	 * @return the current user id
	 */
	public static Integer getCurrentUserId(HttpServletRequest request) {
		Integer userId = null;
		try {
			User user = getCurrentUser(request);
			if (user != null) {
				userId = user.getId();
			}
		} catch (Exception e) {
			log.error("getCurrentUserId", e);
		}
		return userId;
	}

	/**
	 * 获得当前用户工号.
	 *
	 * @param request the request
	 * @return the current username
	 */
	public static String getCurrentUsername(HttpServletRequest request) {
		String userName = null;
		User user = getCurrentUser(request);
		if (user != null) {
			userName = user.getUsername();
		}
		return userName;
	}

	/**
	 * 从request中获得参数Map，并返回可读的Map.
	 *
	 * @param request
	 *            the request
	 * @return the parameter map
	 */
	public static Map<String, Object> getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map<String, String[]> properties = request.getParameterMap();
		// 返回值Map
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Set<String> keySet = properties.keySet();
		for (String key : keySet) {
			String[] values = properties.get(key);
			String value = "";
			if (values != null && (values.length == 1 && StringUtils.isNotBlank(values[0])) ? true : false) {
				for (int i = 0; i < values.length; i++) {
					if (values[i] != null && !"".equals(values[i])) {
						value += values[i] + ",";
					}
				}
				if (value != null && !"".equals(value)) {
					value = value.substring(0, value.length() - 1);
				}
				if (key.equals("keywords")) {// 关键字特殊查询字符转义
					value = value.replace("_", "\\_").replace("%", "\\%");
				}
				returnMap.put(key, value);
			}
		}
		return returnMap;
	}

	/**
	 * String parmeter to array.
	 * 字符串转化为数组
	 * @param parmeterMap the parmeter map
	 * @param key the key
	 * @return the map
	 */
	public static Map stringParmeterToArray(Map parmeterMap,String key){
		Object obj = parmeterMap.get(key);
		if (obj != null && obj instanceof String) {
			String[] objs = ((String) obj).split(",");
			parmeterMap.put(key,objs);
		}
		return parmeterMap;
	}

	/**
	 * Get the http client info detail .
	 *
	 * @param request the request
	 * @return the String
	 */
	public static String getHttpClientInfo(HttpServletRequest request) {
		String LABEL = "::";
		String SEPERATOR = "\t";
		String REST_URL = "Request URL";
		String REST_METHOD = "Request Method";
		String CLIENT_IP = "Client IP";
		String USER_AGENT = "User Agent";
		String REST_PARAMS = "Request Params";

		String clientIp = null;
		if (request.getRemoteAddr() != null) {
			clientIp = request.getRemoteAddr();
		} else if (request.getRemoteHost() != null) {
			clientIp = request.getRemoteHost();
		} else if (request.getHeader("X-FORWARDED-FOR") != null) {
			clientIp = request.getHeader("X-FORWARDED-FOR");
		}
		String restURL = request.getRequestURL().toString();
		String restMethod = request.getMethod();
		String userAgent = request.getHeader("user-agent");

		StringBuilder sb = new StringBuilder();
		sb.append(REST_URL + LABEL + restURL + SEPERATOR);
		sb.append(REST_METHOD + LABEL + restMethod + SEPERATOR);
		sb.append(CLIENT_IP + LABEL + clientIp + SEPERATOR);
		sb.append(USER_AGENT + LABEL + userAgent + SEPERATOR);
		sb.append(REST_PARAMS + LABEL + CommonUtil.getParameterMap(request) + SEPERATOR);

		return sb.toString();
	}

	/**
	 * 获得当前用户角色.
	 *
	 * @param request the request
	 * @return the current usertype
	 */
	public static Integer getCurrentUsertype(HttpServletRequest request) {
		Integer userType = null;
		User user = getCurrentUser(request);
		if (user != null) {
			userType = user.getUserType();
		}
		return userType;
	}

	/**
	 * 获取client ip地址.
	 *
	 * @param request the request
	 * @return the object
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 根据换行符分割转化为list Description: <br>.
	 *
	 * @author henley<br>
	 * @param str the str
	 * @return <br>
	 * @taskId <br>
	 */
	public static List<String> splitByLineBreak(String str) {
		if (str != null && !"".equals(str.trim())) {
			List<String> strList = Arrays.asList(str.split("\r|\n"));
			return strList;
		}
		return null;
	}

	/**
	 * 获取分页起始条数.
	 *
	 * @param map 传递的查询参数
	 * @return the object
	 */
	public static Map<String, Object> getPageMap(Map<String, Object> map) {
		Integer limit = Integer.valueOf((String) map.get("limit"));
		Integer page = Integer.valueOf((String) map.get("page"));
		Integer pageStart = (page - 1) * limit;
		// 起始开始和每次取的条数以int形式存进map
		map.put("pageStart", pageStart);
		map.put("limit", limit);
		return map;
	}

	/**
	 * 获取上传文件类型.
	 *
	 * @param mr the mr
	 * @return the object
	 */
	public static String getFileType(MultipartHttpServletRequest mr ) {
		String filetype="null";
		Iterator<String> nameList = mr.getFileNames();
		String fileName = nameList.next();
		// 获得到文件
		MultipartFile fm = mr.getFile(fileName);
		String oldFileName=fm.getOriginalFilename();
		if(oldFileName.contains(".")){
			filetype = oldFileName.substring(oldFileName.lastIndexOf('.'));
		}
		return filetype;
	}
}
