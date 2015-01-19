package zzdict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StarDictParser {

	/**
	 * folder that contains dict files
	 */
	private String dictFilesFolder;

	/**
	 * index file info to help access target word data
	 */
	private Map<String, DictDataInfo> indexFileInfo;

	/**
	 * dict infos
	 */
	private StarDictInfo dictInfo;

	private InfoFileParser infoFileParser;
	private IndexFileParser indexFileParser;
	private DictFileParser dictFileParser;


	/**
	 * 
	 * @param dictFilesFolder
	 * @throws WrongPropertyException 
	 * @throws IOException 
	 */
	public StarDictParser(String dictFilesFolder) throws IOException, WrongPropertyException {
		this.dictFilesFolder = dictFilesFolder;
		checkDictFilesFolderValid();
		initStarDictParser();
	}

	private void checkDictFilesFolderValid() throws FileNotFoundException {
		File f = new File(dictFilesFolder);
		if (!(f.exists() && f.isDirectory())) {
			throw new FileNotFoundException("Dict folder " + dictFilesFolder
					+ " could not be found!");
		}
	}

	private void initStarDictParser() throws IOException, WrongPropertyException {
		// init infoFileParser
		String infoFileName = FileUtils.getNameOfFileWithSuffixInDir(dictFilesFolder,".ifo");
		this.infoFileParser = new InfoFileParser(infoFileName);
		this.dictInfo = this.infoFileParser.parseStarDictInfo();

		// init indexFileParser
		String indexFileName = FileUtils.getNameOfFileWithSuffixInDir(dictFilesFolder,".idx");
		if (indexFileName == null){
			indexFileName = FileUtils.getNameOfFileWithSuffixInDir(dictFilesFolder,".idx.gz");
		}
		this.indexFileParser = new IndexFileParser(indexFileName);
		loadDictIndexInfoToMemory();
		
		// init dictFileParser
		String dictFileName = FileUtils.getNameOfFileWithSuffixInDir(dictFilesFolder,".dict");
		if (dictFileName == null){
			dictFileName = FileUtils.getNameOfFileWithSuffixInDir(dictFilesFolder,".dict.dz");
		}		
		this.dictFileParser = new DictFileParser(dictFileName);
	}

	private void loadDictIndexInfoToMemory() {
		try {
			this.indexFileInfo = indexFileParser.parseIndexFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FileFormatErrorException e) {
			e.printStackTrace();
		}
	}

	public void getAllWordMean() {
        try {
            int i = 0;
            for (Entry<String, DictDataInfo> entry : this.indexFileInfo.entrySet()) {
                System.out.println("word: " + entry.getKey());
                System.out
                        .println("offset: " + entry.getValue().wordDataOffset);
                System.out.println("size: " + entry.getValue().wordDataSize);
                String mean = getWordMean(entry.getKey());
                System.out.println("mean: " + mean);
                i++;
            }
            System.out.println("word's readed: " + i);
            System.out.println("count of words: " + this.indexFileInfo.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * get word datas of given word
	 * 
	 * @param word
	 *            given word
	 * @return list of word data
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public List<WordDataItem> getWordDatas(String word) throws FileNotFoundException, IOException {
		if (indexFileInfo.get(word) == null)
			return new ArrayList<WordDataItem>();
		else
			return dictFileParser.getWordDatas(word, indexFileInfo.get(word), dictInfo);
	}

	/**
	 * get word mean of given word
	 * 
	 * @param word
	 *            given word
	 * @return mean of word data
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String getWordMean(String word) throws FileNotFoundException, IOException {
		if (this.indexFileInfo.get(word) == null) {
			return "not found";
		}	
		return dictFileParser.getWordMean(word, this.indexFileInfo.get(word));
	}
}
