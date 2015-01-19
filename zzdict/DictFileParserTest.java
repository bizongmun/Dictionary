package zzdict;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import zzdict.IndexFileParser;

public class DictFileParserTest {

	private DictFileParser dictFileParser;
	private InfoFileParser infoFileParser;
	private StarDictInfo dictInfo;
	
	public void setUp() throws Exception {
		dictFileParser = new DictFileParser(
			"/Users/dluan/Dictionary/zzdict/data/stardict1.3.dict.dz");
		infoFileParser = new InfoFileParser(
			"/Users/dluan/Dictionary/zzdict/data/stardict1.3.ifo");
		dictInfo = infoFileParser.parseStarDictInfo();
	}

	public void testGetWordDatas() {
		String word;
		DictDataInfo dataInfo;
		
		List<WordDataItem> list;
		
		word = "tepidity";
		dataInfo = new DictDataInfo(1352867, 18);
		try {
			list = dictFileParser.getWordDatas(word, dataInfo, dictInfo);
			System.out.println(list.size());
			System.out.println(list.get(0).type);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void testGetWordDatas2(){
		String word;
		DictDataInfo dataInfo;
		
		List<WordDataItem> list;
		
		word = "unbiassed";
		dataInfo = new DictDataInfo(1419824, 14);
		try {
			list = dictFileParser.getWordDatas(word, dataInfo, dictInfo);
			System.out.println(list.size());
			System.out.println(list.get(0).type);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public static void main(String[] args) {
        DictFileParserTest dict = new DictFileParserTest();  
        dict.testGetWordDatas();
    }
}
