package CommonRespone;
public class CreateProject extends Respone{
    public int IdProject ; 
    public String Author ; 
    public CreateProject(Type  TypeRespone, int IdProject , String Author)
    {
        super(TypeRespone);
        this.IdProject = IdProject;
        this.Author = Author;
    }
}
