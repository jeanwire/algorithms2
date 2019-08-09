/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class BaseballElimination {

    private String[] t;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    private boolean[] e;
    private Object[] c;

    public BaseballElimination(String filename) {

        In in = new In(filename);
        int numTeams = Integer.parseInt(in.readLine());
        t = new String[numTeams];
        w = new int[numTeams];
        l = new int[numTeams];
        r = new int[numTeams];
        g = new int[numTeams][numTeams];
        e = new boolean[numTeams];
        c = new Object[numTeams];

        for (int i = 0; i < numTeams; i++) {
            String[] line = in.readLine().trim().split(" +");
            t[i] = line[0];
            w[i] = Integer.parseInt(line[1]);
            l[i] = Integer.parseInt(line[2]);
            r[i] = Integer.parseInt(line[3]);
            for (int j = 0; j < numTeams; j++) {
                g[i][j] = Integer.parseInt(line[j + 4]);
            }
        }
        // trivial elimination
        int maxWins = 0;
        String mostWinsTeam = "";
        for (int i = 0; i < numTeams; i++) {
            if (w[i] > maxWins) {
                maxWins = w[i];
                mostWinsTeam = t[i];
            }
        }
        for (int i = 0; i < numTeams; i++) {
            if (w[i] + r[i] < maxWins) {
                e[i] = true;
                Queue<String> cert = new Queue<String>();
                cert.enqueue(mostWinsTeam);
                c[i] = cert;
            }
        }

        // nontrivial elimination -> only check teams that are not yet eliminated
        if (numTeams > 1) {
            for (int i = 0; i < numTeams; i++) {
                if (this.e[i]) continue;

                int numMatchups = (numTeams - 1) * (numTeams - 2) / 2;
                int numVerts = 2 + numTeams - 1 + numMatchups;
                FlowNetwork network = new FlowNetwork(numVerts);
                int vertNum = 1;
                // bug is in matching up game vertex to team vertices
                for (int j = 0; j < numTeams - 1; j++) {
                    for (int k = j + 1; k < numTeams; k++) {
                        if (j == i || k == i) {
                            continue;
                        }
                        FlowEdge numGames = new FlowEdge(0, vertNum, (double) this.g[j][k]);
                        network.addEdge(numGames);
                        vertNum++;
                    }
                }

                vertNum = 1;
                for (int j = 0; j < numTeams - 2; j++) {
                    for (int k = j + 1; k < numTeams - 1; k++) {
                        FlowEdge team1 = new FlowEdge(vertNum, j + numMatchups + 1,
                                                      Double.POSITIVE_INFINITY);
                        network.addEdge(team1);
                        FlowEdge team2 = new FlowEdge(vertNum, k + numMatchups + 1,
                                                      Double.POSITIVE_INFINITY);
                        network.addEdge(team2);
                        vertNum++;
                    }
                }

                // edges from teams to sink
                int potNumWins = w[i] + r[i];
                int m = 1;
                for (int j = 0; j < numTeams; j++) {
                    if (j == i) {
                        m = 0;
                        continue;
                    }
                    if (potNumWins - w[j] >= 0) {
                        FlowEdge edge = new FlowEdge(j + numMatchups + m, numVerts - 1,
                                                     potNumWins - w[j]);
                        network.addEdge(edge);
                    }
                    else {
                        FlowEdge edge = new FlowEdge(j + numMatchups + m, numVerts - 1, 0);
                        network.addEdge(edge);
                    }
                }

                FordFulkerson ff = new FordFulkerson(network, 0, numVerts - 1);

                Iterable<FlowEdge> gameEdges = network.adj(0);
                for (FlowEdge edge : gameEdges) {
                    if (edge.capacity() != edge.flow()) {
                        e[i] = true;
                        break;
                    }
                }
                if (e[i]) {
                    Queue<String> certElim = new Queue<String>();
                    m = 1;
                    for (int j = 0; j < numTeams; j++) {
                        if (j == i) {
                            m = 0;
                            continue;
                        }

                        if (ff.inCut(j + numMatchups + m)) {
                            certElim.enqueue(t[j]);
                        }
                    }
                    c[i] = certElim;
                }
            }
        }
    }

    public int numberOfTeams() {

        return this.t.length;
    }

    public Iterable<String> teams() {

        Queue<String> teams = new Queue<String>();
        for (String team : this.t) {
            teams.enqueue(team);
        }
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

        int index = 0;
        for (int i = 0; i < this.t.length; i++) {
            if (t[i].equals(team)) {
                index = i;
                break;
            }
        }

        return this.e[index];
    }

    public Iterable<String> certificateOfElimination(String team) {

        int index = 0;
        for (int i = 0; i < this.t.length; i++) {
            if (t[i].equals(team)) {
                index = i;
                break;
            }
        }

        return (Queue<String>) this.c[index];
    }

    public static void main(String[] args) {

        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    System.out.print(t + " ");
                }
                System.out.println("}");
            }
            else {
                System.out.println(team + " is not eliminated");
            }
        }
    }
}
