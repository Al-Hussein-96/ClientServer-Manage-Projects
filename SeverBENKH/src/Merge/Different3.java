/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Merge;

import Different.Changes;
import Different.Delete;
import Different.Diff;
import Different.Insert;
import Different.NoChange;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Moaz
 */
public class Different3 {

    final int size = 10000;
    int line_a, line_b, line_o;
    List<String> listOrg, list_a, list_b;
    Diff Diff_a, Diff_b;
    int match_a[] = new int[size];
    int match_b[] = new int[size];
    List<String> MergingList = new ArrayList<>();
    String Name_a, Name_b;

    public Different3(List<String> listOrg,
            List<String> listFr, List<String> listSc,
            Diff Diff1, Diff Diff2,
            String Name_a, String Name_b) {

        this.listOrg = listOrg;
        this.list_a = listFr;
        this.list_b = listSc;
        line_a = line_b = line_o = 0;

        this.Diff_a = Diff1;
        this.Diff_b = Diff2;
        this.Name_a = Name_a;
        this.Name_b = Name_b;

    }

    public void MergingLists() {
        match_a = setMatch(Diff_a);
        match_b = setMatch(Diff_b);
//        for (int i = 1; i <= list_a.size(); i++) {
//            System.out.print(match_a[i] + "  ");
//        }
//        System.out.println("");
//        for (int i = 1; i <= list_b.size(); i++) {
//            System.out.print(match_b[i] + "  ");
//        }
//        System.out.println("");
        generate_chunks();
    }

    private int[] setMatch(Diff diff) {
        int match[] = new int[size];

        for (int i = 0; i < size; i++) {
            match[i] = 0;
        }
        int oldRow = 0, newRow = 0;
        List<Changes> list = diff.getChanges();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof Delete) {
                oldRow++;
            } else if (list.get(i) instanceof Insert) {
                newRow++;
            } else if (list.get(i) instanceof NoChange) {
                oldRow++;
                newRow++;
                match[oldRow] = newRow;
            }
        }
        return match;
    }

    private void generate_chunks() {
        while (true) {
            int i = find_next_mismatch();
            if (i == 1) {
                List<Integer> list = find_next_match();
                int o = list.get(0);
                int a = list.get(1);
                int b = list.get(2);
//                System.out.println(o+ " "+a+" "+b);
                if (a != 0 && b != 0) {
                    emit_chunk(o, a, b);
                } else {
                    emit_final_chunk();
                    return;
                }
            } else if (i != -1) {
                emit_chunk(line_o + i, line_a + i, line_b + i);
            } else {
                emit_final_chunk();
                return;
            }
        }
    }

    private int find_next_mismatch() {
        int i = 1;
        while (in_bounds(i) && Match(match_a, line_a, i)
                && Match(match_b, line_b, i)) {
            i += 1;
        }
        if (in_bounds(i)) {
            return i;
        } else {
            return -1;
        }
    }

    private boolean in_bounds(int i) {
        return line_o + i <= listOrg.size()
                || line_a + i <= list_a.size()
                || line_b + i <= list_b.size();
    }

    private boolean Match(int[] matches, int offset, int i) {
        return matches[line_o + i] == offset + i;
    }

    private List<Integer> find_next_match() {
        int o = line_o + 1;

        while (!(o > listOrg.size() || (match_a[o] != 0 && match_b[o] != 0))) {
            o += 1;
        }
        List<Integer> list = new ArrayList<>();
        list.add(o);
        list.add(match_a[o]);
        list.add(match_b[o]);

        return list;
    }

    private void emit_chunk(int o, int a, int b) {
        write_chunk(line_o, o - 1, line_a, a - 1, line_b, b - 1);

        line_o = o - 1;
        line_a = a - 1;
        line_b = b - 1;
    }

    private void emit_final_chunk() {
        write_chunk(line_o, listOrg.size(), line_a, list_a.size(), line_b, list_b.size());
    }

    private void write_chunk(int o_fr, int o_la, int a_fr, int a_la, int b_fr, int b_la) {

//        System.out.println(o_fr + 1 + "  " + o_la);
//        System.out.println(a_fr + 1 + "  " + a_la);
//        System.out.println(b_fr + 1 + "  " + b_la);
//        System.out.println();
        List<String> o = new ArrayList<>();
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();

        for (int i = o_fr; i < o_la; i++) {
            o.add(listOrg.get(i));
        }
        for (int i = a_fr; i < a_la; i++) {
            a.add(list_a.get(i));
        }
        for (int i = b_fr; i < b_la; i++) {
            b.add(list_b.get(i));
        }

//        System.out.println(o);
//        System.out.println(a);
//        System.out.println(b);
        if (o.equals(a) && o.equals(b)) {
//            System.out.println("Equals");
            MergingList.addAll(o);
        } else if ((o.equals(a) && a.size() == 0) || (a.size()==0 && b.size()!=0)) {
//            System.out.println("b");
            MergingList.addAll(b);
        } else if (o.equals(b) && b.size() == 0|| (b.size()==0 && a.size()!=0)) {
//            System.out.println("a");
            MergingList.addAll(a);
        } else if (a.size() != 0 || b.size() != 0) {
//            System.out.println("looool");
            MergingList.add("<<<<<<<" + Name_a);
            MergingList.addAll(a);
            MergingList.add("=======");
            MergingList.addAll(b);
            MergingList.add(">>>>>>>" + Name_b);
        }
    }

    public List<String> getMergingList() {
        return MergingList;
    }
}
