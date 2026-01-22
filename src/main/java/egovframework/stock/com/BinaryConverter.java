package egovframework.stock.com;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

	/**
	 * binary byte를 문자열로 손실 없이 변환 해주는 컨버터
	 *
	 * @author https://sunghs.tistory.com
	 * @see <a href="https://github.com/sunghs/java-utils">source</a>
	 */
	public class BinaryConverter {

	    private final int bitCount;

	    private final StringBuffer bitString;

	    public BinaryConverter() {
	        this(8);
	    }

	    public BinaryConverter(int bitCount) {
	        this.bitCount = bitCount;
	        this.bitString = new StringBuffer(repeat("0" , Math.max(0, bitCount)));
	    }

	    /**
	     * binary byte를 String 문자열로 변환
	     * @param bytes binary bytes
	     * @return String
	     */
	    public String convertBinaryToString(final byte[] bytes) {
	        StringBuilder result = new StringBuilder();
	        for (byte b : bytes) {
	            result.append(byteToBitString(b));
	        }
	        return result.toString();
	    }

	    private String byteToBitString(final byte b) {
	        StringBuilder byteString = new StringBuilder(this.bitString.toString());
	        for (int bit = 0; bit < bitCount; bit++) {
	            if (((b >> bit) & 1) > 0) {
	                byteString.setCharAt((bitCount - 1) - bit, '1');
	            }
	        }
	        return byteString.toString();
	    }

	    /**
	     * 변환 된 String 문자열을 다시 binary byte로 변환
	     * @param byteStrings byteStrings
	     * @return binary bytes
	     */
	    public byte[] convertStringToBinary(final String byteStrings) {
	        int repeatCount = byteStrings.length() / bitCount;
	        byte[] result = new byte[repeatCount];
	        for (int idx = 1; idx < repeatCount; idx++) {
	            int begin = (idx - 1) * bitCount;
	            int end = idx * bitCount;
	            String byteString = byteStrings.substring(begin, end);
	            result[idx - 1] = bitStringToByte(byteString);
	        }
	        return result;
	    }

	    private byte bitStringToByte(final String byteString) {
	        byte b = 0;
	        for (int idx = 0; idx < bitCount; idx++) {
	            byte value = (byte) ((byteString.charAt(bitCount - 1 - idx) == '1') ? (1 << idx) : 0);
	            b = (byte) (value | b);
	        }
	        return b;
	    }
	    
	    public static String repeat(String str, int count) {
	        if (count <= 0) return "";
	        return str + repeat(str, count - 1);
	    }
	    
	    public static void saveFile(byte[] data, String filePath) throws IOException {
	        try (FileOutputStream fos = new FileOutputStream(filePath)) {
	            fos.write(data);
	        }
	        
	    }
}
