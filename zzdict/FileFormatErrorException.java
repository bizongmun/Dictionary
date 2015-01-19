package zzdict;
/**
 * File Format Error Exception
 * this exception will be thrown, when parsing a dict index file or dict data file
 *
 * @author zzh
 *
 */
public class FileFormatErrorException extends Exception {
        private static final long serialVersionUID = -8681900356376211735L;
        
        private String filename;
        private long pos;
        
        public FileFormatErrorException(String filename, long pos){
                this.filename = filename;
                this.pos = pos;
        }

        /**
         * convert this exception to a readable string that contain filename and pos.<br>
         * pos is estimated, because we use buffered input stream
         * It will help us to find where is wrong.
         */
        public String toString(){
                return "FileFormaErrorException on file :" + filename + " position: "+pos;
        }
}
