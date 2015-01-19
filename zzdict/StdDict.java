import java.util.ArrayList;

public class StdDict extends BaseDict {
        
        private String indexFileName;
        private boolean isIndexFileZipped;
        
        private String dictFileName;
        private boolean  isDictFileZipped;
        
        public StdDict(String fileName) {
                super(fileName);
                // TODO Auto-generated constructor stub
        }

        @Override
        public String findTranslation(String word, String language) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public ArrayList<String> findSimilarWords(String word) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public String getDescription() {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public String getName() {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public void init(String fileName) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public ArrayList<String> genAutoCompleteWords(String word) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public ArrayList<Sentence> getSampleSentences(String word) {
                // TODO Auto-generated method stub
                return null;
        }
        
}
