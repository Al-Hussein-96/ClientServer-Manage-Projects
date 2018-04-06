package CommonRespone;

import CommonClass.NameAndDirectory;

public class SendFile extends Respone {
    
    public byte[] DataFile;
    boolean EndOfFile;
    NameAndDirectory My ; 
   
    public SendFile(byte source[], boolean EndOfFile , NameAndDirectory My) {
        super(ResponeType.DONE);
        this.My = My;
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
