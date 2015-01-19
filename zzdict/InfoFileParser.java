package zzdict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * StarDict info file parser
 * 
 * @author zzh
 * 
 */
public class InfoFileParser {

	private String infoFileName;
	private FileReader reader;

	/**
	 * constructor of InfoFileParser
	 * 
	 * @param infoFileName
	 *            full info file's name
	 * @throws FileNotFoundException
	 *             if info file can not be found
	 */
	public InfoFileParser(String infoFileName)
			throws FileNotFoundException {
		this.infoFileName = infoFileName;
		try {
			reader = new FileReader(infoFileName);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Info file: " + infoFileName
					+ " could not be found!");
		}
	}

	/**
	 * parse .ifo file to get StarDict info
	 * 
	 * @return StarDictInfo structure that contains dict infos
	 * @throws IOException
	 *             if IO error occure when reading ifo file
	 * @throws WrongPropertyException
	 *             if some required properties are missing
	 */
	public StarDictInfo parseStarDictInfo() throws IOException,
			WrongPropertyException {
		Properties properties = new Properties();
		properties.load(reader);
		StarDictInfo info = new StarDictInfo();

		// version required
		String version = properties.getProperty("version");
		if (version == null)
			throw new WrongPropertyException(
					"version property is required, but it is missing.");
		else {
			try {
				info.version = new Version(version);
			} catch (IllegalVersionFormatException e) {
				throw new WrongPropertyException("Illegal version format for "
						+ version);
			}
		}

		// bookname required
		info.bookname = properties.getProperty("bookname");
		if (info.bookname == null)
			throw new WrongPropertyException(
					"bookname property is required, but it is missing.");

		// wordcount required int
		String wordcount = properties.getProperty("wordcount");
		if (wordcount == null)
			throw new WrongPropertyException(
					"wordcount property is required, but it is missing.");
		else {
			try {
				info.wordcount = Integer.valueOf(wordcount);
			} catch (NumberFormatException e) {
				throw new WrongPropertyException("wordcount property : "
						+ wordcount + " is not a number.");
			}
		}

		// synwordcount required if .syn file exist
		String synwordcount = properties.getProperty("synwordcount");
		if (synwordcount == null) {
			if (isOnlyOneFileWithSuffixExistInDictDir(".syn")) {
				throw new WrongPropertyException(
						"synwordcount property is missing, but a .syn file is found.");
			}
		} else {
			if (!isOnlyOneFileWithSuffixExistInDictDir(".syn")) {
				throw new WrongPropertyException(
						"synwordcount property exists, but .syn file is missing.");
			}
			try {
				info.synwordcount = Integer.valueOf(synwordcount);
			} catch (NumberFormatException e) {
				throw new WrongPropertyException("synwordcount property : "
						+ synwordcount + " is not a number.");
			}
		}

		// idxfilesize required, match original idx file size
		String idxfilesize = properties.getProperty("idxfilesize");
		if (idxfilesize == null) {
			throw new WrongPropertyException(
					"idxfilesize property is required, it is missing.");
		} else {
			try {
				info.idxfilesize = Integer.valueOf(idxfilesize);
			} catch (NumberFormatException e) {
				throw new WrongPropertyException("idxfilesize property : "
						+ idxfilesize + " is not a number.");
			}

			long originalIdxFileSize = 0;
			if (isOnlyOneFileWithSuffixExistInDictDir(".idx")) {
				originalIdxFileSize = new File(
						getNameOfFileWithSuffixInDictDir(".idx")).length();
			} else if (isOnlyOneFileWithSuffixExistInDictDir(".idx.gz")) {
				/*originalIdxFileSize = GZipFileUtils
						.GetUncompressedGZIPFileSize(new File(
								getNameOfFileWithSuffixInDictDir(".idx.gz")));*/
			} else {
				throw new FileNotFoundException("idx file is not found.");
			}

			if (info.idxfilesize != originalIdxFileSize) {
				throw new WrongPropertyException("idxfilesize property : "
						+ idxfilesize
						+ " don't match the actual idx file size : "
						+ originalIdxFileSize + ".");
			}

		}

		// idxoffsetbits, only parse it when version >= 3.0.0
		if (info.version.compareTo(new Version("3.0.0")) != -1) {
			String idxoffsetbits = properties.getProperty("idxoffsetbits");
			if (idxoffsetbits != null) {
				if (idxoffsetbits.equals("32") || idxoffsetbits.equals("64"))
					info.idxoffsetbits = Integer.valueOf(idxoffsetbits);
				else {
					throw new WrongPropertyException(
							"idxoffsetbits should be 32 or 64, but now it is "
									+ idxoffsetbits + '.');
				}
			}
		}

		info.author = properties.getProperty("author");
		info.email = properties.getProperty("email");
		info.website = properties.getProperty("website");
		info.description = properties.getProperty("description");
		info.date = properties.getProperty("date");
		info.dicttype = properties.getProperty("dicttype");

		// TODO do more validation on sametypesequence property
		info.sametypesequence = properties.getProperty("sametypesequence");
		return info;
	}

	/**
	 * check if only one file with given suffix exists in the dict dir
	 * 
	 * @param suffix
	 *            given suffix
	 * @return true if only one file with given suffix exists, false if not
	 *         exists or more than one file with given suffix exists
	 */
	private boolean isOnlyOneFileWithSuffixExistInDictDir(final String suffix) {
		File infoFile = new File(infoFileName);
		File infoFilePath = infoFile.getParentFile();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(suffix);
			}
		};
		String[] result = infoFilePath.list(filter);
		if (result != null && result.length == 1)
			return true;
		else
			return false;
	}

	/**
	 * get name of file with suffix in dict dir
	 * 
	 * @param suffix
	 *            given suffix
	 * @return first found name of file, null if no one is found
	 */
	private String getNameOfFileWithSuffixInDictDir(final String suffix) {
		String dictPath = new File(infoFileName).getParent();
		return FileUtils.getNameOfFileWithSuffixInDir(dictPath, suffix);
	}

}

/**
 * stardict dict infos
 * 
 * @author zzh
 */
class StarDictInfo {
	/**
	 * dict version, required Note that the current "version" string must be
	 * "2.4.2" or "3.0.0". If it's not, then StarDict will refuse to read the
	 * file. If version is "3.0.0", StarDict will parse the "idxoffsetbits"
	 * option.
	 */
	Version version;

	/**
	 * dict name required
	 */
	String bookname;

	/**
	 * word count required, and must match the word count in idx file
	 */
	int wordcount;

	/**
	 * syn word count, required if ".syn" file exists.
	 */
	int synwordcount;

	/**
	 * idx file size, required , must match the original idx file size, even if
	 * idx file is gzipped
	 */
	long idxfilesize;

	/**
	 * idx offset bits, 64(long) or 32(int), default is 32. if it is 64, then it
	 * can support dict data file large than 4G New in 3.0.0
	 */
	int idxoffsetbits = 32;

	/**
	 * author of this dict
	 */
	String author;

	/**
	 * email address of author
	 */
	String email;

	/**
	 * website
	 */
	String website;

	/**
	 * dict description. You can use \<br\>
	 * for new line.
	 */
	String description;

	/**
	 * create date of this dictionary
	 */
	String date;

	/**
	 * The "sametypesequence" option is described in further detail below.
	 * 
	 * <pre>
	 * sametypesequence
	 * 
	 * You should first familiarize yourself with the .dict file format
	 * described in the next section so that you can understand what effect
	 * this option has on the .dict file.
	 * 
	 * If the sametypesequence option is set, it tells StarDict that each
	 * word's data in the .dict file will have the same sequence of datatypes.
	 * In this case, we expect a .dict file that's been optimized in two
	 * ways: the type identifiers should be omitted, and the size marker for
	 * the last data entry of each word should be omitted.
	 * 
	 * Let's consider some concrete examples of the sametypesequence option.
	 * 
	 * Suppose that a dictionary records many .wav files, and so sets:
	 *         sametypesequence=W
	 * In this case, each word's entry in the .dict file consists solely of a
	 * wav file.  In the .dict file, you would leave out the 'W' character
	 * before each entry, and you would also omit the 32-bits integer at the
	 * front of each .wav entry that would normally give the entry's length.
	 * You can do this since the length is known from the information in the
	 * idx file.
	 * 
	 * As another example, suppose a dictionary contains phonetic information
	 * and a meaning for each word.  The sametypesequence option for this
	 * dictionary would be:
	 *         sametypesequence=tm
	 * Once again, you can omit the 't' and 'm' characters before each data
	 * entry in the .dict file.  In addition, you should omit the terminating
	 * '\0' for the 'm' entry for each word in the .dict file, as the length
	 * of the meaning string can be inferred from the length of the phonetic
	 * string (still indicated by a terminating '\0') and the length of the
	 * entire word entry (listed in the .idx file).
	 * 
	 * So for cases where the last data entry for each word normally requires
	 * a terminating '\0' character, you should omit this character in the
	 * dict file.  And for cases where the last data entry for each word
	 * normally requires an initial 32-bits number giving the length of the
	 * field (such as WAV and PNG entries), you must omit this number in the
	 * dictionary.
	 * 
	 * Every dictionary should try to use the sametypesequence feature to
	 * save disk space.
	 * </pre>
	 */
	String sametypesequence; // very important.

	/**
	 * dict type. dicttype is used by some special dictionary plugins, such as
	 * wordnet. Its value can be "wordnet" presently.
	 */
	String dicttype;
}

class Version implements Comparable<Version> {
	int majorVersion;
	int medianVersion;
	int minorVersion;

	public Version(int majorVersion, int medianVersion, int minorVersion) {
		this.majorVersion = majorVersion;
		this.medianVersion = medianVersion;
		this.minorVersion = minorVersion;
	}

	/**
	 * Constructor of Version from a given version string
	 * 
	 * @param versionString
	 *            given version string
	 * @throws IllegalVersionFormatException
	 *             when version string is not a valid version string. valid
	 *             version string is 3 int connected with '.' like 3.1.2
	 */
	public Version(String versionString) throws IllegalVersionFormatException {
		StringTokenizer st = new StringTokenizer(versionString, ".");
		if (st.countTokens() != 3)
			throw new IllegalVersionFormatException(versionString
					+ " is not a valid version");
		try {
			this.majorVersion = Integer.valueOf(st.nextToken());
			this.medianVersion = Integer.valueOf(st.nextToken());
			this.minorVersion = Integer.valueOf(st.nextToken());
		} catch (NumberFormatException e) {
			throw new IllegalVersionFormatException(versionString
					+ " is not a valid version");
		}
	}

	/**
	 * convert Version to String
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(20);
		sb.append(majorVersion).append('.').append(medianVersion).append('.')
				.append(minorVersion);
		return sb.toString();
	}

	public int compareTo(Version o) {
		if (this.majorVersion > o.majorVersion)
			return 1;
		else if (this.majorVersion < o.majorVersion)
			return -1;
		else {
			if (this.medianVersion > o.medianVersion)
				return 1;
			else if (this.medianVersion < o.medianVersion)
				return -1;
			else {
				if (this.minorVersion > o.minorVersion)
					return 1;
				else if (this.minorVersion < o.minorVersion)
					return -1;
				else
					return 0;
			}
		}
	}

	@Override
	public boolean equals(Object version) {
		if (version instanceof Version) {
			return (this.compareTo((Version) version) == 0);
		} else {
			return false;
		}
	}
}

class IllegalVersionFormatException extends IllegalArgumentException {

	private static final long serialVersionUID = 4625282887934585112L;

	public IllegalVersionFormatException(String message) {
		super(message);
	}

}
