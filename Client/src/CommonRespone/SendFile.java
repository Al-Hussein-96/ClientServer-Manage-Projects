package CommonRespone;

import CommonClass.NameAndDirectory;

public class SendFile extends Respone {

    public byte[] DataFile;
    boolean EndOfFile;
    long NumberOfByte;
    NameAndDirectory My;

    public SendFile(byte source[], boolean EndOfFile, NameAndDirectory My,long NumberOfByte) {
        super(ResponeType.DONE);
        this.My = My;
        DataFile = copyFullArrayUsingClone(source);
        this.EndOfFile = EndOfFile;
        this.NumberOfByte = NumberOfByte;

    }

    public void setEndOfFile(boolean EndOfFile) {
        this.EndOfFile = EndOfFile;
    }

    private byte[] copyFullArrayUsingClone(byte[] source) {
        return source.clone();
    }

    public byte[] getDataFile() {
        return DataFile;
    }

    public boolean isEndOfFile() {
        return EndOfFile;
    }

    public long getNumberOfByte() {
        return NumberOfByte;
    }
     
}
