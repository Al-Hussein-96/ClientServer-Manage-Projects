package Different;

import java.util.ArrayList;
import java.util.List;

public class StringsComparator {

    private final List<String> left;
    private final List<String> right;

    private int[][] trace;
    int Delete;
    int Insert;

    public StringsComparator(List<String> left, List<String> right) {
        this.left = left;
        this.right = right;
        this.Delete = this.Insert = 0;
        final int size = left.size() + right.size() + 5;
        trace = new int[size][2 * size];
    }

    public Diff getScript() {
        final Diff script = new Diff();
        buildScript(script);
        return script;
    }

    private void buildScript(final Diff script) {
        trace = shortest_edit();
        List<ArrayList<Integer>> track = backtrack();
//        System.out.println("size = " + track.size());
        for (int i = track.size() - 1; i >= 0; i--) {
            ArrayList<Integer> pair = track.get(i);
            int x = pair.get(0);
            int y = pair.get(1);
            if (i != track.size() - 1) {
                int prev_x = track.get(i + 1).get(0);
                int prev_y = track.get(i + 1).get(1);
                if (x == prev_x + 1 && y == prev_y + 1) {
                    // * No Change
                    script.append(new NoChange(left.get(prev_x)));
                } else if (x == prev_x + 1) {
                    // * Delete
                    script.append(new Delete(left.get(prev_x)));
                } else if (y == prev_y + 1) {
                    // * Insert
                    script.append(new Insert(right.get(prev_y)));
                }
            }
        }
    }

    private List<ArrayList<Integer>> backtrack() {
        List<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> pair = new ArrayList<Integer>();
        int n = left.size();
        int m = right.size();
        int max = n + m;
        int x = n, y = m;
        int D = 0;
        for (int d = 0; d <= max; ++d) {
            boolean bo = false;
            for (int k = -d; k <= d; k += 2) {
                int u = trace[d][k + max];
                int v = x - k;
                if (u == n && v == m) {
                    D = d;
                    bo = true;
                    break;
                }
            }
            if (bo) {
                break;
            }
        }
//        System.out.println("The Differents = " + D + "\nx = " + x + ", y = " + y);
        int prev_k, prev_x, prev_y;
        while (x != trace[0][max] || y != trace[0][max]) {
            pair = new ArrayList<>();
            pair.add(x);
            pair.add(y);
            res.add(pair);
            int k = x - y;
            if (k == -D || k != D && trace[D - 1][k - 1 + max] < trace[D - 1][k + 1 + max]) {
                prev_k = k + 1;
            } else {
                prev_k = k - 1;
            }
            prev_x = trace[D - 1][prev_k + max];
            prev_y = prev_x - prev_k;
            while (x > prev_x && y > prev_y) {

                x--;
                y--;
                pair = new ArrayList<>();
                pair.add(x);
                pair.add(y);
                res.add(pair);
            }
            if (x != prev_x) {
                x--;
            }
            if (y != prev_y) {
                y--;
            }
            D--;
        }
        pair = new ArrayList<>();
        pair.add(x);
        pair.add(y);
        res.add(pair);

        while (x > 0 && y > 0) {
            x--;
            y--;
            pair = new ArrayList<>();
            pair.add(x);
            pair.add(y);
            res.add(pair);
        }
        return res;
    }

    private int[][] shortest_edit() {
        // Myers Algorithm
        // Initialisations
        int n = left.size();
        int m = right.size();
        int max = n + m;

        int[][] tracetemp = new int[max + 5][2 * max + 5];
        int[] v = new int[2 * max + 5];
        v[1 + max] = 0;

        int x = 0, y = 0;
        for (int d = 0; d <= max; ++d) {
            for (int k = -d; k <= d; k += 2) {
                if (k == -d || k != d && v[k - 1 + max] < v[k + 1 + max]) {
                    x = v[k + 1 + max];
                } else {
                    x = v[k - 1 + max] + 1;
                }

                y = x - k;

                while (x < n && y < m && left.get(x).equals(right.get(y))) {
                    ++x;
                    ++y;
                }
                v[k + max] = x;
                tracetemp[d][k + max] = x;
                if (x >= n && y >= m) {
                    return tracetemp;
                }
            }
        }
        return null;
    }

}
