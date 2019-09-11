/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class CircularSuffixArray {

    private final String s;
    private final int[] index;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("null input");
        this.s = s;
        this.index = new int[s.length()];
        CircularSuffix[] suffixes = new CircularSuffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            suffixes[i] = new CircularSuffix(this.s, i);
        }
        Arrays.sort(suffixes);
        for (int i = 0; i < suffixes.length; i++) {
            index[i] = suffixes[i].first;
        }
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        public final char[] s;
        public final int first;

        public CircularSuffix(String s, int index) {
            this.s = s.toCharArray();
            this.first = index;
        }

        public int compareTo(CircularSuffix o) {
            if (this.s[first] > o.s[o.first]) return 1;
            else if (this.s[this.first] < o.s[o.first]) return -1;
                // work through suffixes until a different
            else {
                int thisIndex = this.first;
                int thatIndex = o.first;
                for (int i = 0; i < s.length; i++) {
                    thisIndex++;
                    if (thisIndex == s.length) thisIndex = 0;
                    thatIndex++;
                    if (thatIndex == s.length) thatIndex = 0;
                    if (s[thisIndex] > s[thatIndex]) return 1;
                    else if (s[thisIndex] < s[thatIndex]) return -1;
                }
            }
            // if two suffixes are identical
            return 0;
        }
    }

    public int length() {
        return this.s.length();
    }

    public int index(int i) {
        if (i < 0 || i >= this.s.length()) throw new IllegalArgumentException("index out of range");
        return this.index[i];
    }

    public static void main(String[] args) {
        String foo = "ABRACADABRA!";
        CircularSuffixArray bar = new CircularSuffixArray(foo);
        for (int i = 0; i < foo.length(); i++) {
            System.out.println(bar.index[i]);
        }
        System.out.println("test length " + bar.length());
        System.out.println("test index 6: " + bar.index(6));
    }
}
