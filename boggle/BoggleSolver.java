/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver {

    private final TST<Integer> dict;
    private final SET<String> dictWords;

    public BoggleSolver(String[] dictionary) {
        dictWords = new SET<String>();
        dict = new TST<Integer>();
        int k = 0;
        for (int i = 0; i < dictionary.length; i++) {
            String word = dictionary[i];
            dictWords.add(word);
            char[] letters = word.toCharArray();
            for (int j = 1; j < word.length(); j++) {
                char[] prefixArray = Arrays.copyOf(letters, j);
                String prefix = new String(prefixArray);
                dict.put(prefix, k++);
            }
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> words = new HashSet<String>();

        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                int[] index = new int[2];
                index[0] = i;
                index[1] = j;
                HashSet<String> visited = new HashSet<String>();
                visited.add(Integer.toString(i).concat(Integer.toString(j)));
                char letter = board.getLetter(i, j);
                if (letter == 'Q') {
                    dfs(index, board, "QU", words, visited);
                }
                else {
                    dfs(index, board, Character.toString(letter), words, visited);
                }
            }
        }

        return words;
    }

    private void dfs(int[] index, BoggleBoard board, String prefix, HashSet<String> words,
                     HashSet<String> visited) {

        // top row
        if (index[0] != 0) {
            // top right
            if (index[1] != 0) {
                int row = index[0] - 1;
                int col = index[1] - 1;
                if (!visited.contains(Integer.toString(row).concat(
                        Integer.toString(col)))) {
                    HashSet<String> newVisited = new HashSet<String>(visited);
                    checkSquare(row, col, board.getLetter(row, col), prefix, words,
                                newVisited, board);
                }
            }
            // top center
            int row = index[0] - 1;
            int col = index[1];
            if (!visited.contains(Integer.toString(row).concat(Integer.toString(col)))) {
                HashSet<String> newVisited = new HashSet<String>(visited);
                checkSquare(row, col, board.getLetter(row, col), prefix, words,
                            newVisited, board);
            }

            // top right
            if (index[1] != board.cols() - 1) {
                int r = index[0] - 1;
                int c = index[1] + 1;
                if (!visited.contains(Integer.toString(r).concat(Integer.toString(c)))) {
                    HashSet<String> newVisited = new HashSet<String>(visited);
                    checkSquare(r, c, board.getLetter(r, c), prefix, words,
                                newVisited, board);
                }
            }
        }

        // bottom row
        if (index[0] != board.rows() - 1) {
            // bottom right
            if (index[1] != 0) {
                int row = index[0] + 1;
                int col = index[1] - 1;
                if (!visited.contains(Integer.toString(row).concat(
                        Integer.toString(col)))) {
                    HashSet<String> newVisited = new HashSet<String>(visited);
                    checkSquare(row, col, board.getLetter(row, col), prefix, words,
                                newVisited,
                                board);
                }
            }
            // top center
            int row = index[0] + 1;
            int col = index[1];
            if (!visited.contains(Integer.toString(row).concat(Integer.toString(col)))) {
                HashSet<String> newVisited = new HashSet<String>(visited);
                checkSquare(row, col, board.getLetter(row, col), prefix, words,
                            newVisited, board);
            }

            // top right
            if (index[1] != board.cols() - 1) {
                int r = index[0] + 1;
                int c = index[1] + 1;
                if (!visited.contains(Integer.toString(r).concat(Integer.toString(c)))) {
                    HashSet<String> newVisited = new HashSet<String>(visited);
                    checkSquare(r, c, board.getLetter(r, c), prefix, words,
                                newVisited, board);
                }
            }
        }

        // left sq
        if (index[1] != 0) {
            int row = index[0];
            int col = index[1] - 1;
            if (!visited.contains(Integer.toString(row).concat(Integer.toString(col)))) {
                HashSet<String> newVisited = new HashSet<String>(visited);
                checkSquare(row, col, board.getLetter(row, col), prefix, words,
                            newVisited, board);
            }
        }

        if (index[1] != board.cols() - 1) {
            int row = index[0];
            int col = index[1] + 1;
            if (!visited.contains(Integer.toString(row).concat(Integer.toString(col)))) {
                HashSet<String> newVisited = new HashSet<String>(visited);
                checkSquare(row, col, board.getLetter(row, col), prefix, words,
                            newVisited, board);
            }
        }
    }


    private void checkSquare(int row, int col, char letter, String prefix, HashSet<String> words,
                             HashSet<String> visited, BoggleBoard board) {
        String newWord = "";
        if (letter == 'Q') {
            newWord = prefix.concat("QU");
        }
        else {
            newWord = prefix.concat(Character.toString(letter));
        }

        if (newWord.length() > 2 && this.dictWords.contains(newWord)) {
            words.add(newWord);
        }
        if (this.dict.contains(newWord)) {
            int[] n = new int[2];
            n[0] = row;
            n[1] = col;
            visited.add(Integer.toString(row).concat(Integer.toString(col)));
            dfs(n, board, newWord, words, visited);
        }
    }


    public int scoreOf(String word) {
        if (this.dictWords.contains(word)) {
            switch (word.length()) {
                case 1:
                    return 0;
                case 2:
                    return 0;
                case 3:
                    return 1;
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;

            }
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
