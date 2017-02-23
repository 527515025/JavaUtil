
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * JSON工具类.
 *
 * @author abel<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016-01-18 <br>
 */
public class JsonUtil {

   /**
    * Gets the string.
    *
    * @param request the request
    * @return the string
    */
   public static String getString(HttpServletRequest request){
       StringBuilder sb = new StringBuilder();
		try {
            BufferedReader br = request.getReader();
            String line = null;
            while((line = br.readLine())!=null){
                sb.append(line);
            }   
		}catch (IOException e){
		    throw new RuntimeException(e);
		}
        return sb.toString();
	}

   /**
    * Gets the json.
    *
    * @param jsonStr the json str
    * @return the json
    */
   public static JSONObject getJson(String jsonStr){
       JSONObject json = JSONObject.fromObject(jsonStr);
       return json;
	}
   
   /**
    * Gets the json.
    *
    * @param request the request
    * @return the json
    */
   public static JSONObject getJson(HttpServletRequest request){
		
		String jsonStr = getString(request);
        JSONObject json = JSONObject.fromObject(jsonStr);
        return json;
	}

    /**
     * Gets the json arr.
     *
     * @param request the request
     * @return the json arr
     */
    public static JSONArray getJsonArr(HttpServletRequest request){

        String jsonStr = getString(request);
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);
        return jsonArr;
    }
    
    /**
     * To array.
     *
     * @param <T> the generic type
     * @param request the request
     * @param clazz the clazz
     * @return the t[]
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] toArray(HttpServletRequest request, Class<T> clazz){
		JSONArray jsonArr = getJsonArr(request);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}));
		T[] t = (T[])JSONArray.toArray(jsonArr, clazz);
		return t;
	}
    
	/**
	 * To bean.
	 *
	 * @param <T> the generic type
	 * @param jsonStr the json str
	 * @param clazz the clazz
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T>T toBean(String jsonStr, Class<T> clazz){
		JSONObject json = JSONObject.fromObject(jsonStr);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}));
		T t = (T)JSONObject.toBean(json,clazz);
		return t;
	}
	
	/**
	 * To bean.
	 *
	 * @param <T> the generic type
	 * @param json the json
	 * @param clazz the clazz
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T>T toBean(JSONObject json, Class<T> clazz){
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}));
		T t = (T)JSONObject.toBean(json,clazz);
		return t;
	}
	
	/**
	 * To bean.
	 *
	 * @param <T> the generic type
	 * @param request the request
	 * @param clazz the clazz
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T>T toBean(HttpServletRequest request, Class<T> clazz){
		JSONObject json = getJson(request);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}));
		T t = (T)JSONObject.toBean(json,clazz);
		return t;
	}
	
	/**
	 * To bean.
	 *
	 * @param <T> the generic type
	 * @param request the request
	 * @param clazz the clazz
	 * @param classMap the class map
	 * @return the t
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T>T toBean(HttpServletRequest request, Class<T> clazz,Map<String, Class> classMap){
		JSONObject json = getJson(request);
		json.remove("createTime");
		json.remove("updateTime");
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}));
		T t = (T)JSONObject.toBean(json,clazz, classMap);
		return t;
	}
	
	/**
	 * To bean.
	 *
	 * @param <T> the generic type
	 * @param jsonStr the json str
	 * @param clazz the clazz
	 * @param classMap the class map
	 * @return the t
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T>T toBean(String jsonStr, Class<T> clazz,Map<String, Class> classMap){
		JSONObject json = JSONObject.fromObject(jsonStr);
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}));
		T t = (T)JSONObject.toBean(json,clazz, classMap);
		return t;
	}
	
	
	
	/**
	 * use this method to deal with json if received message like 'dashboardId=1&cardTypeId=1'.
	 *
	 * @param request the request
	 * @return the json like url
	 * @throws Exception the exception
	 * @ 
	 */
	public static JSONObject getJsonLikeUrl(HttpServletRequest request) throws Exception{
		
		String str = URLDecoder.decode(request.getQueryString(),"utf-8");
		JSONObject json = new JSONObject();
		String[] strArr = str.split("&");
		for(String s : strArr){
			json.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=")+1));
		}
        return json;
	}
	
	/**
	 * 将json 中的key,value更新到domain(实体bean).
	 *
	 * @param <T> the generic type
	 * @param json the json
	 * @param domain the domain
	 * @return the t
	 * @ 
	 */
	@SuppressWarnings("unchecked")
	public static <T>T toBean(JSONObject json, T domain){
		T t = (T)JSONObject.toBean(json, domain, new JsonConfig());
		return t;
	}
	
	/**
	 * To bean.
	 *
	 * @param <T> the generic type
	 * @param request the request
	 * @param domain the domain
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T>T toBean(HttpServletRequest request, T domain){
		JSONObject json = getJson(request);
		T t = (T)JSONObject.toBean(json, domain, new JsonConfig());
		return t;
	}
	
	
	/**
	 * 将一个 JavaBean 对象转化为一个  Map.
	 *
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的  Map 对象
	 * @throws IntrospectionException 如果分析类属性失败
	 * @throws IllegalAccessException 如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	public static Map<String,Object> toMap(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class<? extends Object> type = bean.getClass();
        Map<String,Object> returnMap = new HashMap<String,Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
	
	/**
	 * Gets the json string.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the json string
	 */
	public static String getJsonString(String key, String value){
        return "{\"" + key + "\":\""+value+"\"}";
	}
	
	/**
	 * Gets the json from header.
	 *
	 * @param request the request
	 * @return the json from header
	 * @throws Exception the exception
	 */
	public static JSONObject getJsonFromHeader(HttpServletRequest request) throws Exception{
		
		String str = URLDecoder.decode(getString(request),"utf-8");
		JSONObject json = new JSONObject();
		String[] strArr = str.split("&");
		for(String s : strArr){
			json.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=")+1));
		}
        return json;
	}
}
