package zzdict;
import java.io.File;
import java.io.FilenameFilter;

public class FileUtils {

	/**
	 * get name of file with suffix in dir
	 * 
	 * @param dir
	 * 			dir to search file
	 * @param suffix
	 *            given suffix
	 * @return first found name of file, null if no one is found
	 */
	public static String getNameOfFileWithSuffixInDir(String dir, final String suffix) {
		
		File path = new File(dir);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(suffix);
			}
		};
		String[] result = path.list(filter);

		if (result == null || result.length == 0)
			return null;
		else
			return dir + File.separator + result[0];
	}
}
