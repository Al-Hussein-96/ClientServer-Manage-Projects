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
