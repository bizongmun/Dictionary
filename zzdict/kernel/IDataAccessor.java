package zzdict.kernel;

public interface IDataAccessor {
        public byte[] readData(long offset, long len) throws java.io.IOException;
}