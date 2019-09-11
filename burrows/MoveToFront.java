/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {

    public static void encode() {
        char[] ascii = new char[256];
        for (int i = 0; i < 256; i++) {
            ascii[i] = (char) i;
        }
        StdOut.print("Starting while loop");

        while (!StdIn.isEmpty()) {
            StdOut.print("In while loop");
            char letter = StdIn.readChar();
            for (int i = 0; i < 256; i++) {
                if (letter == ascii[i]) {
                    StdOut.println(i);
                    char temp = ascii[i];
                    System.arraycopy(ascii, 0, ascii, 1, i);
                    ascii[0] = temp;
                    break;
                }
            }
        }
    }

    public static void decode() {
        char[] ascii = new char[256];
        for (int i = 0; i < 256; i++) {
            ascii[i] = (char) i;
        }
        while (!StdIn.isEmpty()) {
            int index = StdIn.readInt();
            StdOut.println(ascii[index]);
            char temp = ascii[index];
            System.arraycopy(ascii, 0, ascii, 1, index);
            ascii[0] = temp;
        }
    }

    public static void main(String[] args) {
        
        while (!StdIn.isEmpty()) {
            char letter = StdIn.readChar();
            StdOut.print(letter);
        }
        if (args[0].equals("-")) MoveToFront.encode();
        else if (args[0].equals("+")) decode();
    }
}
