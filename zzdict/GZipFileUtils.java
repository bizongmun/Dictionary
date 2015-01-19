package zzdict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GZipFileUtils {

	/**
	 * Extract to original file size from the gzip file. This size is stored in
	 * the last 4 bytes of the file. This will only provide the correct value if
	 * the compressed file was smaller than 4 Gb.
	 * 
	 * @param aGZFile
	 *            a gzipped file
	 * @return uncompressed file size
	 * @throws FileNotFoundException
	 *             if aGZFile is not found
	 * @throws IOException
	 *             IO error occur when read the file
	 */
	public static long GetUncompressedGZIPFileSize(File aGZFile)
			throws FileNotFoundException, IOException {
		RandomAccessFile raf = new RandomAccessFile(aGZFile, "r");
		raf.seek(raf.length() - 4);
		int b4 = raf.read();
		int b3 = raf.read();
		int b2 = raf.read();
		int b1 = raf.read();
		long val = (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
		raf.close();

		return val;
	}
}
