/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class SAP {

    private Digraph g;

    public SAP(Digraph G) {

        // I think this makes it immutable?
        this.g = new Digraph(G);
    }

    public int length(int v, int w) {

        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.g, w);

        int shortestDist = Integer.MAX_VALUE;

        for (int i = 0; i < this.g.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int currDist = vBFS.distTo(i) + wBFS.distTo(i);
                if (currDist < shortestDist) shortestDist = currDist;
            }
        }

        if (shortestDist != Integer.MAX_VALUE) {
            return shortestDist;
        }

        else return -1;
    }

    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.g, w);

        int shortestDist = Integer.MAX_VALUE;
        int ancestor = Integer.MAX_VALUE;

        for (int i = 0; i < this.g.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int currDist = vBFS.distTo(i) + wBFS.distTo(i);
                if (currDist < shortestDist) {
                    shortestDist = currDist;
                    ancestor = i;
                }
            }
        }

        if (ancestor != Integer.MAX_VALUE) {
            return ancestor;
        }

        else return -1;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.g, w);

        int shortestDist = Integer.MAX_VALUE;

        for (int i = 0; i < this.g.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int currDist = vBFS.distTo(i) + wBFS.distTo(i);
                if (currDist < shortestDist) shortestDist = currDist;
            }
        }

        if (shortestDist != Integer.MAX_VALUE) {
            return shortestDist;
        }

        else return -1;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths vBFS = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths wBFS = new BreadthFirstDirectedPaths(this.g, w);

        int shortestDist = Integer.MAX_VALUE;
        int ancestor = Integer.MAX_VALUE;

        for (int i = 0; i < this.g.V(); i++) {
            if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
                int currDist = vBFS.distTo(i) + wBFS.distTo(i);
                if (currDist < shortestDist) {
                    shortestDist = currDist;
                    ancestor = i;
                }
            }
        }

        if (ancestor != Integer.MAX_VALUE) {
            return ancestor;
        }

        else return -1;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            System.out.println("length = " + length + " ancestor = " + ancestor);
        }
    }
}
