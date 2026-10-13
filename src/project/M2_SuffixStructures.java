package project;

import java.util.*;

public class M2_SuffixStructures {

    // ---------------- SUFFIX ARRAY ----------------
    public static int[] buildSuffixArray(String text) {

        int n = text.length();

        Integer[] suffixes =
                new Integer[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
        }

        Arrays.sort(suffixes, (a, b) -> {

            int i = a;
            int j = b;

            while (i < n && j < n) {

                if (text.charAt(i) !=
                    text.charAt(j)) {

                    return Character.compare(
                        text.charAt(i),
                        text.charAt(j)
                    );
                }

                i++;
                j++;
            }

            return Integer.compare(
                n - a,
                n - b
            );
        });

        int[] sa = new int[n];

        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i];
        }

        return sa;
    }

    // ---------------- KASAI LCP ----------------
    public static int[] buildLCP(
            String text,
            int[] suffixArray) {

        int n = text.length();

        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) {

            rank[suffixArray[i]] = i;
        }

        int k = 0;

        for (int i = 0; i < n; i++) {

            int r = rank[i];

            if (r == n - 1) {

                k = 0;
                continue;
            }

            int j = suffixArray[r + 1];

            while (i + k < n &&
                   j + k < n &&
                   text.charAt(i + k) ==
                   text.charAt(j + k)) {

                k++;
            }

            lcp[r] = k;

            if (k > 0) {
                k--;
            }
        }

        return lcp;
    }

    // ---------------- SUFFIX AUTOMATON ----------------
    static class SuffixAutomaton {

        static class State {

            int len;

            int link = -1;

            Map<Character, Integer> next =
                    new HashMap<>();
        }

        List<State> states =
                new ArrayList<>();

        int last;

        SuffixAutomaton(String text) {

            states.add(new State());

            states.get(0).len = 0;

            last = 0;

            for (char c : text.toCharArray()) {

                extend(c);
            }
        }

        private void extend(char c) {

            int cur = states.size();

            states.add(new State());

            states.get(cur).len =
                    states.get(last).len + 1;

            int p = last;

            while (p != -1 &&
                   !states.get(p).next.containsKey(c)) {

                states.get(p).next.put(c, cur);

                p = states.get(p).link;
            }

            if (p == -1) {

                states.get(cur).link = 0;

            } else {

                int q =
                    states.get(p).next.get(c);

                if (states.get(p).len + 1 ==
                    states.get(q).len) {

                    states.get(cur).link = q;

                } else {

                    int clone = states.size();

                    states.add(new State());

                    states.get(clone).len =
                        states.get(p).len + 1;

                    states.get(clone).next =
                        new HashMap<>(
                            states.get(q).next
                        );

                    states.get(clone).link =
                        states.get(q).link;

                    while (
                        p != -1 &&
                        states.get(p)
                              .next
                              .getOrDefault(c, -1)
                              == q
                    ) {

                        states.get(p)
                                .next
                                .put(c, clone);

                        p = states.get(p).link;
                    }

                    states.get(q).link = clone;

                    states.get(cur).link = clone;
                }
            }

            last = cur;
        }

        public boolean contains(String pattern) {

            int current = 0;

            for (char c :
                 pattern.toCharArray()) {

                Integer next =
                    states.get(current)
                         .next
                         .get(c);

                if (next == null) {
                    return false;
                }

                current = next;
            }

            return true;
        }
    }

    // ---------------- DEMO ----------------
    public static void runDemo(Scanner sc) {

        System.out.println(
            "\n========== M2: SUFFIX STRUCTURES =========="
        );

        String document =
            "airport gate runway passenger baggage gate";

        System.out.println(
            "Document: " + document
        );

        int[] sa =
            buildSuffixArray(document);

        int[] lcp =
            buildLCP(document, sa);

        System.out.println(
            "\nSuffix Array:"
        );

        for (int index : sa) {

            System.out.println(
                index + " -> " +
                document.substring(index)
            );
        }

        System.out.println(
            "\nLCP Array:"
        );

        System.out.println(
            Arrays.toString(lcp)
        );

        SuffixAutomaton sam =
            new SuffixAutomaton(document);

        System.out.println(
            "\nSuffix Automaton Queries:"
        );

        System.out.println(
            "gate   -> " +
            sam.contains("gate")
        );

        System.out.println(
            "runway -> " +
            sam.contains("runway")
        );

        System.out.println(
            "hello  -> " +
            sam.contains("hello")
        );

        System.out.print(
            "\nEnter document keyword: "
        );

        String query = sc.nextLine();

        System.out.println(
            "Result: " +
            (sam.contains(query)
                ? "FOUND"
                : "NOT FOUND")
        );
    }
}