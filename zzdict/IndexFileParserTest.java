package zzdict;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class IndexFileParserTest {

    public void testParseIndexFile() {
        IndexFileParser ifp;
        Map<String, DictDataInfo> resultMap;

        try {
            // stardict1.3.idx contains 51214 words file size is 899,574 bytes
            ifp = new IndexFileParser(
                    "/Users/dluan/Dictionary/data/JV_Javidic/JV_Javidic.idx");
            resultMap = ifp.parseIndexFile();
            int i = 0;
            for (Entry<String, DictDataInfo> entry : resultMap.entrySet()) {
                System.out.println("word: " + entry.getKey());
                System.out
                        .println("offset: " + entry.getValue().wordDataOffset);
                System.out.println("size: " + entry.getValue().wordDataSize);
                i++;
            }
            System.out.println("word's readed: " + i);
            System.out.println("count of words: " + resultMap.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileFormatErrorException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IndexFileParserTest dict = new IndexFileParserTest();  
        dict.testParseIndexFile();
    }
}