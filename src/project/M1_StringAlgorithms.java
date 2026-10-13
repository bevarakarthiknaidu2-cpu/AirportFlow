package project;
import java.util.*;

public class M1_StringAlgorithms {

    // ---------------- NAIVE STRING MATCHING ----------------
    public static List<Integer> naiveSearch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j = 0;

            while (j < m &&
                   text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            if (j == m) {
                result.add(i);
            }
        }

        return result;
    }

    // ---------------- KMP ----------------
    private static int[] buildLPS(String pattern) {
        int[] lps = new int[pattern.length()];

        int len = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i] = ++len;
                i++;
            }
            else if (len > 0) {
                len = lps[len - 1];
            }
            else {
                lps[i] = 0;
                i++;
            }
        }

        return lps;
    }

    public static List<Integer> kmpSearch(String text, String pattern) {

        List<Integer> result = new ArrayList<>();

        if (pattern.length() == 0) {
            return result;
        }

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    result.add(i - j);
                    j = lps[j - 1];
                }
            }
            else if (j > 0) {
                j = lps[j - 1];
            }
            else {
                i++;
            }
        }

        return result;
    }

    // ---------------- Z FUNCTION ----------------
    public static List<Integer> zSearch(String text, String pattern) {

        List<Integer> result = new ArrayList<>();

        String combined = pattern + "$" + text;

        int n = combined.length();
        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] = Math.min(right - i + 1,
                                z[i - left]);
            }

            while (i + z[i] < n &&
                   combined.charAt(z[i]) ==
                   combined.charAt(i + z[i])) {

                z[i]++;
            }

            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }

            if (z[i] == pattern.length()) {
                result.add(i - pattern.length() - 1);
            }
        }

        return result;
    }

    // ---------------- RABIN KARP ----------------
    public static List<Integer> rabinKarp(String text,
                                           String pattern) {

        List<Integer> result = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return result;
        }

        long base = 256;
        long mod = 1000000007;

        long patternHash = 0;
        long textHash = 0;
        long highPower = 1;

        for (int i = 0; i < m - 1; i++) {
            highPower =
                    (highPower * base) % mod;
        }

        for (int i = 0; i < m; i++) {

            patternHash =
                    (patternHash * base +
                     pattern.charAt(i)) % mod;

            textHash =
                    (textHash * base +
                     text.charAt(i)) % mod;
        }

        for (int i = 0; i <= n - m; i++) {

            if (patternHash == textHash) {

                boolean match = true;

                for (int j = 0; j < m; j++) {

                    if (text.charAt(i + j) !=
                        pattern.charAt(j)) {

                        match = false;
                        break;
                    }
                }

                if (match) {
                    result.add(i);
                }
            }

            if (i < n - m) {

                textHash =
                    (base *
                    (textHash -
                    text.charAt(i) * highPower)
                    + text.charAt(i + m)) % mod;

                if (textHash < 0) {
                    textHash += mod;
                }
            }
        }

        return result;
    }

    // ---------------- AHO CORASICK ----------------
    static class AhoCorasick {

        static class Node {

            int[] next = new int[128];

            int fail = 0;

            List<String> output =
                    new ArrayList<>();

            Node() {
                Arrays.fill(next, -1);
            }
        }

        List<Node> trie =
                new ArrayList<>();

        AhoCorasick(List<String> patterns) {

            trie.add(new Node());

            for (String pattern : patterns) {

                int current = 0;

                for (char ch : pattern.toCharArray()) {

                    int c = ch;

                    if (trie.get(current).next[c] == -1) {

                        trie.get(current).next[c] =
                                trie.size();

                        trie.add(new Node());
                    }

                    current =
                            trie.get(current).next[c];
                }

                trie.get(current).output.add(pattern);
            }

            buildFailureLinks();
        }

        private void buildFailureLinks() {

            Queue<Integer> queue =
                    new LinkedList<>();

            for (int c = 0; c < 128; c++) {

                int child =
                        trie.get(0).next[c];

                if (child != -1) {

                    trie.get(child).fail = 0;

                    queue.add(child);
                }
                else {
                    trie.get(0).next[c] = 0;
                }
            }

            while (!queue.isEmpty()) {

                int current =
                        queue.poll();

                for (int c = 0; c < 128; c++) {

                    int child =
                            trie.get(current).next[c];

                    if (child != -1) {

                        trie.get(child).fail =
                                trie.get(
                                    trie.get(current).fail
                                ).next[c];

                        trie.get(child).output.addAll(
                            trie.get(
                                trie.get(child).fail
                            ).output
                        );

                        queue.add(child);
                    }
                    else {

                        trie.get(current).next[c] =
                                trie.get(
                                    trie.get(current).fail
                                ).next[c];
                    }
                }
            }
        }

        public List<String> search(String text) {

            List<String> found =
                    new ArrayList<>();

            int state = 0;

            for (char ch : text.toCharArray()) {

                if (ch >= 128) {
                    state = 0;
                    continue;
                }

                state =
                    trie.get(state).next[ch];

                if (!trie.get(state).output.isEmpty()) {

                    found.addAll(
                        trie.get(state).output
                    );
                }
            }

            return found;
        }
    }

    // ---------------- DEMO ----------------
    public static void runDemo(Scanner sc) {

        System.out.println(
            "\n========== M1: STRING ALGORITHMS =========="
        );

        String text =
            "Flight AI202 is delayed at Hyderabad Airport";

        String pattern = "AI202";

        System.out.println("Text    : " + text);
        System.out.println("Pattern : " + pattern);

        System.out.println(
            "\nNaive      -> " +
            naiveSearch(text, pattern)
        );

        System.out.println(
            "KMP        -> " +
            kmpSearch(text, pattern)
        );

        System.out.println(
            "Z-Function -> " +
            zSearch(text, pattern)
        );

        System.out.println(
            "Rabin-Karp -> " +
            rabinKarp(text, pattern)
        );

        List<String> alerts =
            Arrays.asList(
                "delayed",
                "cancelled",
                "boarding",
                "diverted"
            );

        AhoCorasick ac =
            new AhoCorasick(alerts);

        String alertText =
            "Flight AI202 is delayed. " +
            "Passengers are now boarding.";

        System.out.println(
            "\nAho-Corasick Alert Detection:"
        );

        System.out.println(
            "Detected: " +
            ac.search(alertText)
        );

        System.out.print(
            "\nEnter flight number/pattern: "
        );

        String input = sc.nextLine();

        System.out.println(
            "KMP Result: " +
            kmpSearch(text, input)
        );
    }
}