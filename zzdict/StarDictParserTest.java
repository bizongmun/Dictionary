package zzdict;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import zzdict.WordDataItem;

public class StarDictParserTest {
    StarDictParser parser;

    public void setUp() throws Exception {
        //parser = new StarDictParser("/Users/dluan/Dictionary/data/NhatViet");
        //parser = new StarDictParser("/Users/dluan/Dictionary/zzdict/data");
        //parser = new StarDictParser("/Users/dluan/Dictionary/data/JV_Javidic");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/VJ_Javidic");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/star_nhatviet");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/star_vietnhat");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/jmdict-ja-en");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/mypedia");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/kanjidic2");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/edict");
        // parser = new StarDictParser("/Users/dluan/Dictionary/data/meikyo_jpn_jpn");
        parser = new StarDictParser("/Users/dluan/Dictionary/data/gen_jpn_eng");
    }
        
    public void testGetWordDatas() {
        List<WordDataItem> list;
        
        try {
            //list = parser.getWordDatas("unbiassed");
            list = parser.getWordDatas("けん");
            System.out.println(list.size());
            System.out.println(list.get(0).type);
            System.out.println(((EnglishPhoneticUtf8TextWordDataItem)list.get(0)).getPhonetic());
            System.out.println(((PureTextWordDataItem)list.get(1)).getWordExplanation());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void testGetWordMean() {
        try {
            String means = parser.getWordMean("跳ね");
            System.out.println("mean: " + means);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testGetAllMean() {
        parser.getAllWordMean();
    }

    public static void main(String[] args) throws Exception {
        StarDictParserTest dict = new StarDictParserTest();  
        dict.setUp();
        dict.testGetAllMean();
    }
}