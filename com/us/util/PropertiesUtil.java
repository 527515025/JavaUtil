
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件工具类.
 *
 * @author abel<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016-01-18 <br>
 */
public class PropertiesUtil {

	/** The prop. */
	private static Properties prop = new Properties();
		
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PropertiesUtil.class);

	static {
		try {
			InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/application.properties"); 
			prop.load(inputStream);
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * Gets the value.
	 *
	 * @param key the key
	 * @return the value
	 */
	public static String getValue(String key){
		return prop.getProperty(key);
	}
    
	/**
	 * Gets the file path.
	 *
	 * @param key the key
	 * @return the file path
	 */
	public static String getFilePath(String key){
		
		String path = prop.getProperty(key);
		if(!path.endsWith("/")||!path.endsWith("\\")) {
			path = path +"/";
		}
		return path;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		System.out.println(prop.getProperty("attachment.root.path"));
	}
}
