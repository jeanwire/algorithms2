/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class BaseballElimination {

    private String[] t;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;

    public BaseballElimination(String filename) {

        In in = new In(filename);
        int numTeams = Integer.parseInt(in.readLine());
        t = new String[numTeams];
        w = new int[numTeams];
        l = new int[numTeams];
        r = new int[numTeams];
        g = new int[numTeams][numTeams];

        for (int i = 0; i < numTeams; i++) {
            String[] line = in.readLine().split(" +");
            t[i] = line[0];
            w[i] = Integer.parseInt(line[1]);
            l[i] = Integer.parseInt(line[2]);
            r[i] = Integer.parseInt(line[3]);
            for (int j = 0; j < numTeams; j++) {
                g[i][j] = Integer.parseInt(line[j + 4]);
            }
        }
        for (int[] row : g) {
            System.out.println(Arrays.toString(row));
        }
    }

    public int numberOfTeams() {

        return this.t.length;
    }

    public Iterable<String> teams() {

        Queue<String> teams = new Queue<String>();
        return teams;
    }

    public int wins(String team) {

        int index = 0;
        for (int i = 0; i < this.t.length; i++) {
            if (t[i].equals(team)) {
                index = i;
                break;
            }
        }

        return this.w[index];
    }

    public int losses(String team) {

        int index = 0;
        for (int i = 0; i < this.t.length; i++) {
            if (t[i].equals(team)) {
                index = i;
                break;
            }
        }

        return this.l[index];
    }

    public int remaining(String team) {

        int index = 0;
        for (int i = 0; i < this.t.length; i++) {
            if (t[i].equals(team)) {
                index = i;
                break;
            }
        }

        return this.r[index];
    }

    public int against(String team1, String team2) {

        int index1 = 0;
        int index2 = 0;

        for (int i = 0; i < this.t.length; i++) {
            if (t[i].equals(team1)) index1 = i;
            if (t[i].equals(team2)) index2 = i;
        }

        return this.g[index1][index2];
    }

    public boolean isEliminated(String team) {

        return true;
    }

    public Iterable<String> certificateOfElimination(String team) {

        Queue<String> teams = new Queue<String>();
        return teams;
    }

    public static void main(String[] args) {

        BaseballElimination foo = new BaseballElimination("teams4.txt");
    }
}
