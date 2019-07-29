/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.KosarajuSharirSCC;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private int synNum;
    private Digraph graph;
    private HashMap<Integer, String[]> synList;
    private HashMap<String, HashSet<Integer>> nounList;

    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("null input");
        }

        In synInput = new In(synsets);
        nounList = new HashMap<String, HashSet<Integer>>();
        synList = new HashMap<Integer, String[]>();

        synNum = 0;

        while (!synInput.isEmpty()) {
            String[] fields = synInput.readLine().split(",");
            String[] words = fields[1].split(" ");
            int vertex = Integer.parseInt(fields[0]);
            synList.put(vertex, words);
            for (String word : words) {
                if (nounList.containsKey(word)) {
                    HashSet<Integer> nums = nounList.get(word);
                    nums.add(vertex);
                    nounList.put(word, nums);
                }
                else {
                    HashSet<Integer> nums = new HashSet<Integer>();
                    nums.add(vertex);
                    nounList.put(word, nums);
                }
            }
            synNum++;
        }

        In hyperInput = new In(hypernyms);
        graph = new Digraph(this.synNum);

        while (!hyperInput.isEmpty()) {
            String[] fields = hyperInput.readLine().split(",");
            for (int i = 1; i < fields.length; i++) {
                graph.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
            }
        }

        KosarajuSharirSCC components = new KosarajuSharirSCC(graph);

        if (components.count() != synNum) throw new IllegalArgumentException("Not a DAG");
    }


    public Iterable<String> nouns() {
        Queue<String> nouns = new Queue<String>();

        for (String noun : this.nounList.keySet()) {
            nouns.enqueue(noun);
        }

        return nouns;
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null word");

        return this.nounList.keySet().contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();

        SAP sap = new SAP(this.graph);

        HashSet<Integer> nounAVerts = this.nounList.get(nounA);
        HashSet<Integer> nounBVerts = this.nounList.get(nounB);

        int dist = sap.length(nounAVerts, nounBVerts);

        return dist;
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();

        SAP sap = new SAP(this.graph);

        HashSet<Integer> nounAVerts = this.nounList.get(nounA);
        HashSet<Integer> nounBVerts = this.nounList.get(nounB);

        int vertex = sap.ancestor(nounAVerts, nounBVerts);

        String[] words = this.synList.get(vertex);

        return String.join(" ", words);
    }

    public static void main(String[] args) {

        WordNet foo = new WordNet(args[0], args[1]);
        System.out.println(foo.synNum);
    }
}
