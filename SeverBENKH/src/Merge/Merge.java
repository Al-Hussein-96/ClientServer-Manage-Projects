package Merge;

import Different.Changes;
import Different.Delete;
import Different.Diff;
import Different.Insert;
import Different.Myers01;
import static Different.Myers01.read;
import Different.NoChange;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sun.security.pkcs11.wrapper.Constants;

public class Merge {

    String Name_a = "a", Name_b = "b";
    List<String> MergingList = new ArrayList<>();

    public Merge(String Original, String FirstBranch, String SecondBranch) {
        List<String> listOrg = read(new File(Original));
        List<String> listFr = read(new File(FirstBranch));
        List<String> listSc = read(new File(SecondBranch));
        System.out.println("Original : " + listOrg);
        System.out.println("First : " + listFr);
        System.out.println("Second : " + listSc);

        Myers01 diff1 = new Myers01(FirstBranch, Original);
        Myers01 diff2 = new Myers01(SecondBranch, Original);

        Diff Diff1 = diff1.getDiff();
        Diff Diff2 = diff2.getDiff();

        Different3 tt = new Different3(listOrg, listFr, listSc, Diff1, Diff2, Name_a, Name_b);

        tt.MergingLists();
        MergingList = tt.getMergingList();
        System.out.println(MergingList);
        
   //     write(MergingList, new File("4.txt"));

    }

    public void setName_a(String Name_a) {
        this.Name_a = Name_a;
    }

    public void setName_b(String Name_b) {
        this.Name_b = Name_b;
    }

    public static void main(String[] args) {
   //            Merge m = new Merge("1.txt", "2.txt", "3.txt");
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

    public static void write(List<String> Lines, File file) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for (String line : Lines) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();
        } catch (IOException ex) {
        }
    }

    public List<String> getMergingList() {
        return MergingList;
    }
}
