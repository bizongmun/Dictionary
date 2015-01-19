package zzdict;
import zzdict.WordDataItem;

public class WordDataItemTest {

    public void testWordDataType() {
        System.out.println(WordDataType.valueOf('h'));
    }

    public static void main(String[] args) {
        WordDataItemTest dict = new WordDataItemTest();  
        dict.testWordDataType();
    }
}