import java.util.ArrayList;

/**
 * base dict class of all type of dict
 * @author zzh
 *
 */
public abstract class BaseDict {
	
	/**
	 * construction method to create a dict from dict file
	 * @param fileName
	 */
	public BaseDict(String fileName){
		init(fileName);
	}
	
	/**
	 * initiation from a dict file
	 * @param fileName dict file name
	 */
	public abstract void init(String fileName);
	
	/**
	 * get dict's name
	 * @return dict's name
	 */
	public abstract String getName();
	
	/**
	 * get dict's description
	 * @return dict's description
	 */
	public abstract String getDescription();
	
	/**
	 * find translation of word in target language 
	 * @param word word to be translated
	 * @param language target language
	 * @return translation, null if the word is not included in this dict 
	 */
	public abstract String findTranslation(String word, String language);

	/**
	 * find similar words, sort by similarity 
	 * @return similar words list, not null
	 */
	public abstract ArrayList<String> findSimilarWords(String word);
	
	/**
	 * find words begin with input word
	 * @param word
	 * @return suggested words, sort by length from shortest to longest
	 */
	public abstract ArrayList<String> genAutoCompleteWords(String word);
	
	/**
	 * get sample sentences of target word
	 * @param word target words
	 * @return sample sentences, not null
	 */
	public abstract ArrayList<Sentence> getSampleSentences(String word);
}
