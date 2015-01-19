package zzdict;

import java.io.UnsupportedEncodingException;

public class ByteArrayTools {

	public static int findNullCharPosition(byte[] byteArray, int startPos) {
		for (int i = startPos; i < byteArray.length; i++) {
			if (byteArray[i] == 0) {
				return i;
			}
		}
		return byteArray.length;
	}

	public static String convertPartOfArrayToUtf8String(byte[] byteArray,
			int startPos, int nullCharPos) {
		return convertPartOfArrayToString(byteArray, startPos, nullCharPos,
				"UTF8");
	}

	private static String convertPartOfArrayToString(byte[] byteArray,
			int startPos, int nullCharPos, String encoding) {
		if (nullCharPos == startPos)
			return "";
		else {
			byte[] tempBuffer = new byte[nullCharPos - startPos];
			System.arraycopy(byteArray, startPos, tempBuffer, 0, nullCharPos
					- startPos);
			try {
				return new String(tempBuffer, encoding);
			} catch (UnsupportedEncodingException e) {
				// TODO log encoding error
				return "";
			}
		}
	}

	public static String convertPartOfArrayToAnsiString(byte[] byteArray,
			int startPos, int nullCharPos) {
		return convertPartOfArrayToString(byteArray, startPos, nullCharPos,
				"ISO8859_1");
	}

}
