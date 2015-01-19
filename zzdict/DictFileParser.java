package zzdict;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import zzdict.kernel.FlatDataAccessor;
import zzdict.kernel.IDataAccessor;
import zzdict.kernel.DictZipDataAccessor;

public class DictFileParser {
	private String dictFileName;

	public DictFileParser(String dictFileName) {
		this.dictFileName = dictFileName;
	}
	
	//public WordDataItem getWordData(String word){
		// TODO parse file to get word data
		/*
		 {7}. The ".dict" file's format.
		The .dict file is a pure data sequence, as the offset and size of each
		word is recorded in the corresponding .idx file.

		If the "sametypesequence" option is not used in the .ifo file, then
		the .dict file has fields in the following order:
		==============
		word_1_data_1_type; // a single char identifying the data type
		word_1_data_1_data; // the data
		word_1_data_2_type;
		word_1_data_2_data;
		...... // the number of data entries for each word is determined by
		       // word_data_size in .idx file
		word_2_data_1_type;
		word_2_data_1_data;
		......
		==============
		It's important to note that each field in each word indicates its
		own length, as described below.  The number of possible fields per
		word is also not fixed, and is determined by simply reading data until
		you've read word_data_size bytes for that word.


		Suppose the "sametypesequence" option is used in the .idx file, and
		the option is set like this:
		sametypesequence=tm
		Then the .dict file will look like this:
		==============
		word_1_data_1_data
		word_1_data_2_data
		word_2_data_1_data
		word_2_data_2_data
		......
		==============
		The first data entry for each word will have a terminating '\0', but
		the second entry will not have a terminating '\0'.  The omissions of
		the type chars and of the last field's size information are the
		optimizations required by the "sametypesequence" option described
		above.

		If "idxoffsetbits=64", the file size of the .dict file will be bigger 
		than 4G. Because we often need to mmap this large file, and there is 
		a 4G maximum virtual memory space limit in a process on the 32 bits 
		computer, which will make we can get error, so "idxoffsetbits=64" 
		dictionary can't be loaded in 32 bits machine in fact, StarDict will 
		simply print a warning in this case when loading. 64-bits computers 
		should haven't this limit.

		Type identifiers
		----------------
		Here are the single-character type identifiers that may be used with
		the "sametypesequence" option in the .idx file, or may appear in the
		dict file itself if the "sametypesequence" option is not used.

		Lower-case characters signify that a field's size is determined by a
		terminating '\0', while upper-case characters indicate that the data
		begins with a network byte-ordered guint32 that gives the length of 
		the following data's size(NOT the whole size which is 4 bytes bigger).

		For the type definition, @see WordDataType

		 */
		//return null;
	//}

	/**
    * query word datas in dict file, each word might relate to different word data, 
    * like explanation, sound, picture etc @see WordDataType
    * 
   	* @param word
    *            the word
    * @param dataInfo
    *            dataInfo that contains wordDateItem type, position and length
    *            information
    * @param dictInfo
    *                      dict infos, most importantly, it contains samesequencetype info
    * @return a list of WordDataItem that contains word's explanation, sentences examples
    *         etc, not null
    * @throws IOException
    *             IO errors occur when reading the dict file
    * @throws FileNotFoundException
    */
    public List<WordDataItem> getWordDatas(String word, DictDataInfo dataInfo, StarDictInfo dictInfo)
                     	throws FileNotFoundException, IOException {
        IDataAccessor accessor;
        if (isDictFileGzipped()) {
            accessor = new DictZipDataAccessor(dictFileName);
        }else{
            accessor = new FlatDataAccessor(dictFileName);
        }
        //read word datas into a byte buffer
        byte[] buf = accessor.readData(dataInfo.wordDataOffset, dataInfo.wordDataSize);
                
        List<WordDataItem> list = new ArrayList<WordDataItem>();
        Position pos = new Position(0);
        WordDataItem item;
                
        if (dictInfo.sametypesequence != null){
            for(char c : dictInfo.sametypesequence.toCharArray()){
                item = getWordData(word,pos.getPos(),buf,WordDataType.valueOf(c),pos);
                list.add(item);
            }
        }else{
            while(pos.getPos() < buf.length){
                item = getWordData(word,pos.getPos(),buf,null,pos);
                list.add(item);
            }
        }
                
        return list;
    }

    public String getWordMean(String word, DictDataInfo dataInfo)
                     	throws FileNotFoundException, IOException {
        IDataAccessor accessor;
        if (isDictFileGzipped()) {
            accessor = new DictZipDataAccessor(dictFileName);
        }else{
            accessor = new FlatDataAccessor(dictFileName);
        }
        //read word datas into a byte buffer
        byte[] buf = accessor.readData(dataInfo.wordDataOffset, dataInfo.wordDataSize);
        
        String mean = new String(buf, "utf-8");
                
        return mean;
    }

    /**
    * get word data since a start position in a byte array
    * 
    * @param word the word
    * @param startPos start position in the byte array
    * @param wordDatasByteArray byte array
    * @param type null if we need to find the type in the array, not null if we have a preset type
    * @param posAfter stored the position after this method for later usage
    * @return a WordDataItem
    */
    private WordDataItem getWordData(String word, int startPos, byte[] wordDatasByteArray, WordDataType type, Position posAfter){
        // TODO get word data from a byte array
        if (type == null){
            type = WordDataType.valueOf((char) wordDatasByteArray[startPos]);
            startPos++;
        }
                
        WordDataItem item;
        item = WordDataItem.createWordDataItemByType(type);
        startPos = item.fillDataThroughByteArray(wordDatasByteArray, startPos);
                
        posAfter.setPos(startPos);
        return item;
    }

    private boolean isDictFileGzipped() {
        return dictFileName.endsWith(".dict.dz");
    }
}

/**
 * A Position class to pass the position info out through parameter
 * @author zzh
 *
 */
class Position {
        
        /**
         * current position
         */
        private int pos;
        
        public Position(int pos){
                this.pos = pos;
        }
        
        public int getPos() {
                return pos;
        }

        public void setPos(int pos) {
                this.pos = pos;
        }       
}