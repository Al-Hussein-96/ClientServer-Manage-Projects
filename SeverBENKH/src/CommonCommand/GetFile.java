package CommonCommand;

public class GetFile extends Command {

    String DirectoryFile;
    public GetFile(String DirectoryFile) {
        super(CommandType.GETFILE);
        this.DirectoryFile = DirectoryFile;
    }

    public String getDirectoryFile() {
        return DirectoryFile;
    }
    
    

}
