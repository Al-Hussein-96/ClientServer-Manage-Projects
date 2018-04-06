package CommonRespone;

public class SendFile extends Respone {
    
    public byte[] DataFile;
    boolean EndOfFile;

    public SendFile(byte source[], boolean EndOfFile) {
        super(ResponeType.DONE);
        DataFile = copyFullArrayUsingClone(source);
        this.EndOfFile = EndOfFile;

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
}
