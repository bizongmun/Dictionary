//import zzdict.*;
package zzdict;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InfoFileParserTest {

	public void testParseStarDictInfo() {
		try {
			InfoFileParser infoFileParser = new InfoFileParser(
					"/Users/dluan/Dictionary/zzdict/data/stardict1.3.ifo");
			StarDictInfo info = infoFileParser.parseStarDictInfo();
			System.out.println(info.wordcount);
			System.out.println(info.bookname);
			System.out.println(info.author);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongPropertyException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
        InfoFileParserTest dict = new InfoFileParserTest();  
        dict.testParseStarDictInfo();
    }
}
