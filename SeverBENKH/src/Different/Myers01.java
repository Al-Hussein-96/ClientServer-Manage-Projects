package Different;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sun.security.pkcs11.wrapper.Constants;

public class Myers01 {

    private Diff Differences = new Diff();

    public Myers01() {

    }

    public Myers01(String dirNew, String dirOld) {
        List<String> list1 = read(new File(dirNew));
        List<String> list2 = read(new File(dirOld));
        StringsComparator sc = new StringsComparator(list2, list1);
        Differences = sc.getScript();
//        Differences.visit(new CommandVisitor() {
//
//            boolean nlAdd = true;
//            boolean nlRemove = true;
//
//            @Override
//            public void visitInsertCommand(String object) {
//                if (nlAdd) {
//                    System.out.println();
//                    //  System.out.print("+ ");
//                    nlAdd = false;
//                }
//
//                if ("".equals(object.trim())) {
//                    object = "NEW LINE";
//                }
//
//                System.out.println("Insert : " + object);
//            }
//
//            @Override
//            public void visitKeepCommand(String object) {
//                if (!nlAdd) {
//                    nlAdd = true;
//                }
//                if (!nlRemove) {
//                    nlRemove = true;
//                    System.out.println();
//                }
//                if ("".equals(object.trim())) {
//                    object = "NEW LINE";
//                }
//                System.out.println("NoChange : " + object);
//            }
//
//            @Override
//            public void visitDeleteCommand(String object) {
//                if (nlRemove) {
//                    System.out.println();
//                    nlRemove = false;
//                }
//                if ("".equals(object.trim())) {
//                    object = "NEW LINE";
//                }
//                System.out.println("Delete : " + object);
//            }
//        });
//        System.out.println("\n");
    }

    public static void main(String[] args) {
        //      Myers01 m=new Myers01("1.txt","2.txt");
    }

    public static List<String> read(File file) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) {
                    lines.add(Constants.NEWLINE);
                } //     System.out.println(line);
                else {
                    lines.add(line);
                }
            }
            br.close();
        } catch (IOException ex) {
        }

        return lines;
    }

    public Diff getDiff() {
        return Differences;
    }
}
