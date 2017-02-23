
import java.net.InetAddress;
import java.util.UUID;


public class UUIDBuild {
	
	/** The sep. */
	private String sep = "";

	/** The Constant IP. */
	private static final int IP;

	/** The counter. */
	private static short counter = (short) 0;

	/** The Constant JVM. */
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	/** The uuidgen. */
	private static UUIDBuild uuidgen = new UUIDBuild();

	static {

		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}

	/**
	 * Gets the single instance of UUIDBuild.
	 *
	 * @return single instance of UUIDBuild
	 */
	public static UUIDBuild getInstance() {
		return uuidgen;
	}

	/**
	 * To int.
	 *
	 * @param bytes the bytes
	 * @return the int
	 */
	public static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	/**
	 * Format.
	 *
	 * @param intval the intval
	 * @return the string
	 */
	protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	/**
	 * Format.
	 *
	 * @param shortval the shortval
	 * @return the string
	 */
	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	/**
	 * Get the jvm detail .
	 *
	 * @return the int
	 */
	protected int getJVM() {
		return JVM;
	}

	/**
	 * Get the count detail .
	 *
	 * @return the short
	 */
	protected synchronized short getCount() {
		if (counter < 0) {
			counter = 0;
		}
		return counter++;
	}

	/**
	 * Get the ip detail .
	 *
	 * @return the int
	 */
	protected int getIP() {
		return IP;
	}

	/**
	 * Get the  hi time. detail .
	 *
	 * @return the short
	 */
	protected short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	/**
	 * Get the lo time detail .
	 *
	 * @return the int
	 */
	protected int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	/**
	 * Generate.
	 *
	 * @return the string
	 */
	public String generate() {
		return new StringBuffer(36).append(format(getIP())).append(sep).append(
				format(getJVM())).append(sep).append(format(getHiTime()))
				.append(sep).append(format(getLoTime())).append(sep).append(
						format(getCount())).toString();
	}

	/**
	 * The main method.
	 *
	 * @param str the arguments
	 */
	public static void main(String[] str) {

		// 得到一个序号
		System.out.println(getUUID());

		// 一次得到多个序号
		String[] UUID_s = getUUID(10);
		for (int i = 0; i < UUID_s.length; i++) {
			System.out.println(UUID_s[i]);
		}
	}

	/**
	 * Get the uuid detail .
	 *
	 * @return the String
	 */
	// 得到一个序号
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 一次得到多个序号.
	 *
	 * @param number            
	 * int 需要获得的序号数量
	 * @return String[] 序号数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

}
