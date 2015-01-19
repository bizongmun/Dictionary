package zzdict;

import java.util.HashMap;
import java.util.Map;

/**
 * WordDataItem class is the abstract class of all word data item. Every
 * WordDataIteam implement convertWordDataToHtml() method, because we use html
 * engine to display the word explanation.
 * 
 * @author zzh
 * 
 */
public abstract class WordDataItem {
    /**
     * word data item type
     */
    public WordDataType type;

    /**
     * convert word data to html
     * 
     * @return html code of word explanation
     */
    public abstract String convertWordDataToHtml();
    
    /**
     * fill word data with part of data of byte array started with startPos
     * @param byteArray byte array
     * @param startPos start position
     * @return first unused byte position in the byte array
     */
    public abstract int fillDataThroughByteArray(byte[] byteArray, int startPos);
    
    /**
     * create concreted WordDataItem object by given WordDataType
     * @param type given WordDataType
     * @return concreted WordDataItem object, null if type is not supported now.
     */
    public static WordDataItem createWordDataItemByType(WordDataType type){
        switch(type){
        case ANSI_PURE_TEXT:
            return new AnsiPureTextWordDataItem();
        case UTF8_PURE_TEXT:
            return new Utf8PureTextWordDataItem();
        case ENGLISH_PHONETIC_UTF8_TEXT:
            return new EnglishPhoneticUtf8TextWordDataItem();
        default:
            return null;
        }
    }
}

abstract class PureTextWordDataItem extends WordDataItem {
    
    protected String wordExplanation;

    /**
     * concreted convert word data to html implementation We should mark
     * different part of word explanation, like pronunciation, word sentence
     * samples etc Later, we will apply css to this html code
     */
    public String convertWordDataToHtml() {
        return wordExplanation;
    }

    public String getWordExplanation() {
        return wordExplanation;
    }

}

/**
 * UTF8 pure text word data item
 * 
 * @author zzh
 * 
 */
class Utf8PureTextWordDataItem extends PureTextWordDataItem {

    public Utf8PureTextWordDataItem() {
        type = WordDataType.UTF8_PURE_TEXT;
    }

    @Override
    public int fillDataThroughByteArray(byte[] byteArray, int startPos) {
        int nullCharPos = ByteArrayTools.findNullCharPosition(byteArray, startPos);
        wordExplanation = ByteArrayTools.convertPartOfArrayToUtf8String(byteArray,startPos,nullCharPos);
        
        //skip null char
        return nullCharPos+1;
    }

}

/**
 * ANSI pure text word data item
 * 
 * @author zzh
 * 
 */
class AnsiPureTextWordDataItem extends PureTextWordDataItem {
    
    public AnsiPureTextWordDataItem() {
        type = WordDataType.ANSI_PURE_TEXT;
    }

    @Override
    public int fillDataThroughByteArray(byte[] byteArray, int startPos) {
        int nullCharPos = ByteArrayTools.findNullCharPosition(byteArray, startPos);
        wordExplanation = ByteArrayTools.convertPartOfArrayToAnsiString(byteArray,startPos,nullCharPos);
        
        //skip null char
        return nullCharPos+1;
    }

}

class EnglishPhoneticUtf8TextWordDataItem extends WordDataItem{

    private String phonetic;
    
    public EnglishPhoneticUtf8TextWordDataItem(){
        type = WordDataType.ENGLISH_PHONETIC_UTF8_TEXT;
    }
    
    public String getPhonetic() {
        return phonetic;
    }

    @Override
    public String convertWordDataToHtml() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int fillDataThroughByteArray(byte[] byteArray, int startPos) {
        int nullCharPos = ByteArrayTools.findNullCharPosition(byteArray, startPos);
        phonetic = ByteArrayTools.convertPartOfArrayToUtf8String(byteArray,startPos,nullCharPos);
        
        //skip null char
        return nullCharPos+1;
    }
    
}

/**
 * word data type
 * 
 * @author zzh
 * 
 */
enum WordDataType {
    UTF8_PURE_TEXT(
        'm', "Word's pure text meaning. "
                + "The data should be a utf-8 string ending with '\0'. "), 
    ANSI_PURE_TEXT(
        'l', "Word's pure text meaning. "
                + "The data is NOT a utf-8 string, "
                + "but is instead a string in locale encoding, "
                + "ending with '\0'.  "
                + "Sometimes using this type will save disk space, "
                + "but its use is discouraged."), 
    PANGO_MARKUP_UTF8_TEXT(
        'g', "A utf-8 string which is marked up with the Pango text markup language. "
                + "For more information about this markup language, "
                + "See the \"Pango Reference Manual.\" "
                + "You might have it installed locally at: "
                + "file:///usr/share/gtk-doc/html/pango/PangoMarkupFormat.html"), 
    ENGLISH_PHONETIC_UTF8_TEXT(
        't', "English phonetic string. "
                + "The data should be a utf-8 string ending with '\0'. "
                + "Here are some utf-8 phonetic characters: "), 
    XDXF_MARKUP_UTF8_TEXT(
        'x', "A utf-8 string which is marked up with the xdxf language. "
                + "See http://xdxf.sourceforge.net "
                + "StarDict have these extention: "
                + "<rref> can have 'type' attribute, "
                + "it can be 'image', 'sound', 'video' and 'attach'. "
                + "<kref> can have 'k' attribute."), 
    CHINESE_YINBIAO_OR_JAPANESE_KANA(
        'y', "Chinese YinBiao or Japanese KANA. "
                + "The data should be a utf-8 string ending with '\0'."), 
    POWERWORD_UTF8_XML(
        'k', "KingSoft PowerWord's data. The data is a utf-8 string ending with '\0'. "
                + "It is in XML format."), 
    MEDIA_WIKI_MAKRUP_UTF8_TEXT(
        'w', "MediaWiki markup language. "
                + "See http://meta.wikimedia.org/wiki/Help:Editing#The_wiki_markup"), 
    HTML(
        'h', "Html codes."), 
    WORDNET(
        'n', "WordNet data."), 
    RESOURCE_FILES(
        'r', "Resource file list. "
                + "The content can be: "
                + "img:pic/example.jpg  // Image file "
                + "snd:apple.wav        // Sound file "
                + "vdo:film.avi     // Video file "
                + "att:file.bin     // Attachment file "
                + "More than one line is supported as a list of available files. "
                + "StarDict will find the files in the Resource Storage. "
                + "The image will be shown, the sound file will have a play button. "
                + "You can \"save as\" the attachment file and so on."), 
    WAVE_FILE(
        'W', "wav file. "
                + "The data begins with a network byte-ordered guint32 to identify the wav "
                + "file's size, immediately followed by the file's content."), 
    PICTURE_FILE(
            'P', "Picture file. "
                + "The data begins with a network byte-ordered guint32 to identify the picture "
                + "file's size, immediately followed by the file's content."),
    RESERVED(
        'X', "this type identifier is reserved for experimental extensions.");

    /**
     * type char is just the char stored in the beginning of each word data
     * section in dict file
     */
    private char type;

    /**
     * explanation of this type word data item, we store it here just for later
     * usage
     */
    private String comment;

    /**
     * We use a static inside map to store mapping of the char to WordDataType
     * related to this char
     */
    private static Map<Character, WordDataType> map = new HashMap<Character, WordDataType>();

    static {
        for (WordDataType type : WordDataType.values()) {
            map.put(type.type, type);
        }
    }

    WordDataType(char type, String comment) {
        this.type = type;
        this.comment = comment;
    }

    public char type() {
        return type;
    }

    public String comment() {
        return comment;
    }

    public String toString() {
        return "type: " + type + "\n" + "comment: " + comment;
    }

    /**
     * get WordDataType related to given type character
     * 
     * @param type
     *            given type character
     * @return related WordDataType
     */
    public static WordDataType valueOf(char type) {
        return map.get(type);
    }
}