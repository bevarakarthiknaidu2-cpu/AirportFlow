package project;

import java.util.*;

public class M3_DynamicProgramming {

    // ---------------- LEVENSHTEIN ----------------
    public static int levenshtein(
            String a,
            String b) {

        int n = a.length();
        int m = b.length();

        int[][] dp =
                new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost =
                    a.charAt(i - 1) ==
                    b.charAt(j - 1)
                    ? 0 : 1;

                dp[i][j] =
                    Math.min(
                        Math.min(
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                        ),
                        dp[i - 1][j - 1] + cost
                    );
            }
        }

        return dp[n][m];
    }

    // ---------------- DAMERAU-LEVENSHTEIN ----------------
    public static int damerauLevenshtein(
            String a,
            String b) {

        int n = a.length();
        int m = b.length();

        int[][] dp =
                new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost =
                    a.charAt(i - 1) ==
                    b.charAt(j - 1)
                    ? 0 : 1;

                dp[i][j] =
                    Math.min(
                        Math.min(
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                        ),
                        dp[i - 1][j - 1] + cost
                    );

                if (i > 1 && j > 1 &&
                    a.charAt(i - 1) ==
                    b.charAt(j - 2) &&
                    a.charAt(i - 2) ==
                    b.charAt(j - 1)) {

                    dp[i][j] =
                        Math.min(
                            dp[i][j],
                            dp[i - 2][j - 2] + 1
                        );
                }
            }
        }

        return dp[n][m];
    }

    // ---------------- BITMASK DP ----------------
    public static int[] minimumAssignment(
            int[][] cost) {

        int n = cost.length;

        int totalMasks = 1 << n;

        int[] dp =
                new int[totalMasks];

        int[] parent =
                new int[totalMasks];

        Arrays.fill(
            dp,
            Integer.MAX_VALUE / 4
        );

        Arrays.fill(parent, -1);

        dp[0] = 0;

        for (int mask = 0;
             mask < totalMasks;
             mask++) {

            int flight =
                Integer.bitCount(mask);

            if (flight >= n) {
                continue;
            }

            for (int gate = 0;
                 gate < n;
                 gate++) {

                if ((mask &
                    (1 << gate)) == 0) {

                    int newMask =
                        mask | (1 << gate);

                    int newCost =
                        dp[mask] +
                        cost[flight][gate];

                    if (newCost <
                        dp[newMask]) {

                        dp[newMask] =
                            newCost;

                        parent[newMask] =
                            gate;
                    }
                }
            }
        }

        int mask =
            totalMasks - 1;

        int[] assignment =
            new int[n];

        for (int flight = n - 1;
             flight >= 0;
             flight--) {

            int gate =
                parent[mask];

            assignment[flight] =
                gate;

            mask ^=
                (1 << gate);
        }

        System.out.println(
            "Minimum assignment cost = " +
            dp[totalMasks - 1]
        );

        return assignment;
    }

    // ---------------- MATRIX CHAIN ----------------
    public static int matrixChain(
            int[] p) {

        int n = p.length - 1;

        int[][] dp =
                new int[n][n];

        for (int length = 2;
             length <= n;
             length++) {

            for (int i = 0;
                 i <= n - length;
                 i++) {

                int j =
                    i + length - 1;

                dp[i][j] =
                    Integer.MAX_VALUE;

                for (int k = i;
                     k < j;
                     k++) {

                    int cost =
                        dp[i][k] +
                        dp[k + 1][j] +
                        p[i] *
                        p[k + 1] *
                        p[j + 1];

                    dp[i][j] =
                        Math.min(
                            dp[i][j],
                            cost
                        );
                }
            }
        }

        return dp[0][n - 1];
    }

    // ---------------- OPTIMAL BST ----------------
    public static int optimalBST(
            int[] freq) {

        int n = freq.length;

        int[][] dp =
                new int[n][n];

        int[] prefix =
                new int[n + 1];

        for (int i = 0; i < n; i++) {

            prefix[i + 1] =
                prefix[i] + freq[i];

            dp[i][i] =
                freq[i];
        }

        for (int length = 2;
             length <= n;
             length++) {

            for (int i = 0;
                 i <= n - length;
                 i++) {

                int j =
                    i + length - 1;

                int sum =
                    prefix[j + 1] -
                    prefix[i];

                dp[i][j] =
                    Integer.MAX_VALUE;

                for (int root = i;
                     root <= j;
                     root++) {

                    int left =
                        root > i
                        ? dp[i][root - 1]
                        : 0;

                    int right =
                        root < j
                        ? dp[root + 1][j]
                        : 0;

                    dp[i][j] =
                        Math.min(
                            dp[i][j],
                            left + right + sum
                        );
                }
            }
        }

        return dp[0][n - 1];
    }

    // ---------------- DEMO ----------------
    public static void runDemo(Scanner sc) {

        System.out.println(
            "\n========== M3: ADVANCED DP =========="
        );

        // Levenshtein
        String entered =
            "Hyderbad";

        String correct =
            "Hyderabad";

        System.out.println(
            "\n1. Airport Name Correction"
        );

        System.out.println(
            "Entered : " + entered
        );

        System.out.println(
            "Correct : " + correct
        );

        System.out.println(
            "Levenshtein Distance = " +
            levenshtein(
                entered,
                correct
            )
        );

        // Damerau
        System.out.println(
            "\n2. Damerau-Levenshtein"
        );

        System.out.println(
            "CA -> AC = " +
            damerauLevenshtein(
                "CA",
                "AC"
            )
        );

        // Bitmask DP
        System.out.println(
            "\n3. Flight-Gate Assignment"
        );

        int[][] cost = {

            {4, 2, 7},

            {6, 4, 3},

            {5, 8, 1}
        };

        System.out.println(
            "        G1 G2 G3"
        );

        System.out.println(
            "F1      4  2  7"
        );

        System.out.println(
            "F2      6  4  3"
        );

        System.out.println(
            "F3      5  8  1"
        );

        int[] assignment =
            minimumAssignment(cost);

        for (int i = 0;
             i < assignment.length;
             i++) {

            System.out.println(
                "F" + (i + 1) +
                " -> G" +
                (assignment[i] + 1)
            );
        }

        // Matrix Chain
        System.out.println(
            "\n4. Matrix Chain Multiplication"
        );

        int[] matrices =
            {10, 20, 30, 40};

        System.out.println(
            "Minimum multiplication cost = " +
            matrixChain(matrices)
        );

        // Optimal BST
        System.out.println(
            "\n5. Optimal Binary Search Tree"
        );

        int[] frequency =
            {34, 8, 50};

        System.out.println(
            "Minimum search cost = " +
            optimalBST(frequency)
        );

        System.out.print(
            "\nEnter airport/passenger name: "
        );

        String input =
            sc.nextLine();

        System.out.println(
            "Edit distance from Hyderabad = " +
            levenshtein(
                input,
                "Hyderabad"
            )
        );
    }
}