package project;
import java.util.*;

public class AirportFlow {

    public static void main(String[] args) {

        Scanner sc =
                new Scanner(System.in);

        System.out.println(
            "=============================================="
        );

        System.out.println(
            "       AIRPORTFLOW"
        );
 
        System.out.println(
            "       AIRPORT OPERATIONS &"
        );

        System.out.println(
            "       RESOURCE MANAGEMENT"
        );

        System.out.println(
            "       DSA REVIEW-1 PROTOTYPE"
        );

        System.out.println(
            "=============================================="
        );

        while (true) {

            System.out.println(
                "\n--------------- MAIN MENU ----------------"
            );

            System.out.println(
                "1. M1 - String Algorithms"
            );

            System.out.println(
                "2. M2 - Suffix Structures"
            );

            System.out.println(
                "3. M3 - Advanced Dynamic Programming"
            );

            System.out.println(
                "4. Run Complete Review-1 Demo"
            );

            System.out.println(
                "0. Exit"
            );

            System.out.println(
                "------------------------------------------"
            );

            System.out.print(
                "Enter choice: "
            );

            String input =
                    sc.nextLine();

            int choice;

            try {

                choice =
                    Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid choice."
                );

                continue;
            }

            switch (choice) {

                case 1:

                    M1_StringAlgorithms
                        .runDemo(sc);

                    break;

                case 2:

                    M2_SuffixStructures
                        .runDemo(sc);

                    break;

                case 3:

                    M3_DynamicProgramming
                        .runDemo(sc);

                    break;

                case 4:

                    runCompleteDemo();

                    break;

                case 0:

                    System.out.println(
                        "\nThank you for using AirportFlow!"
                    );

                    sc.close();

                    return;

                default:

                    System.out.println(
                        "Invalid choice."
                    );
            }

            if (choice != 0) {

                System.out.println(
                    "\nPress ENTER to return to menu..."
                );

                sc.nextLine();
            }
        }
    }

    // ---------------- COMPLETE DEMO ----------------
    private static void runCompleteDemo() {

        System.out.println(
            "\n========== COMPLETE REVIEW-1 DEMO =========="
        );

        // ================= M1 =================

        System.out.println(
            "\n[M1] STRING ALGORITHMS"
        );

        String text =
            "Flight AI202 is delayed at Hyderabad Airport";

        String pattern =
            "AI202";

        System.out.println(
            "Text: " + text
        );

        System.out.println(
            "Pattern: " + pattern
        );

        System.out.println(
            "Naive: " +
            M1_StringAlgorithms.naiveSearch(
                text,
                pattern
            )
        );

        System.out.println(
            "KMP: " +
            M1_StringAlgorithms.kmpSearch(
                text,
                pattern
            )
        );

        System.out.println(
            "Z-Function: " +
            M1_StringAlgorithms.zSearch(
                text,
                pattern
            )
        );

        System.out.println(
            "Rabin-Karp: " +
            M1_StringAlgorithms.rabinKarp(
                text,
                pattern
            )
        );

        List<String> alerts =
            Arrays.asList(
                "delayed",
                "cancelled",
                "boarding",
                "diverted"
            );

        M1_StringAlgorithms.AhoCorasick ac =
            new M1_StringAlgorithms.AhoCorasick(
                alerts
            );

        System.out.println(
            "Aho-Corasick: " +
            ac.search(
                "Flight AI202 is delayed. " +
                "Passengers are boarding."
            )
        );

        // ================= M2 =================

        System.out.println(
            "\n[M2] SUFFIX STRUCTURES"
        );

        String document =
            "airport gate runway passenger baggage";

        int[] suffixArray =
            M2_SuffixStructures
                .buildSuffixArray(document);

        int[] lcp =
            M2_SuffixStructures
                .buildLCP(
                    document,
                    suffixArray
                );

        System.out.println(
            "Suffix Array: " +
            Arrays.toString(suffixArray)
        );

        System.out.println(
            "LCP Array: " +
            Arrays.toString(lcp)
        );

        M2_SuffixStructures.SuffixAutomaton sam =
            new M2_SuffixStructures.SuffixAutomaton(
                document
            );

        System.out.println(
            "Query 'gate': " +
            (sam.contains("gate")
                ? "FOUND"
                : "NOT FOUND")
        );

        System.out.println(
            "Query 'runway': " +
            (sam.contains("runway")
                ? "FOUND"
                : "NOT FOUND")
        );

        // ================= M3 =================

        System.out.println(
            "\n[M3] ADVANCED DYNAMIC PROGRAMMING"
        );

        System.out.println(
            "Levenshtein:"
        );

        System.out.println(
            "Hyderbad -> Hyderabad = " +
            M3_DynamicProgramming.levenshtein(
                "Hyderbad",
                "Hyderabad"
            )
        );

        int[][] cost = {

            {4, 2, 7},

            {6, 4, 3},

            {5, 8, 1}
        };

        System.out.println(
            "\nFlight-Gate Assignment:"
        );

        int[] assignment =
            M3_DynamicProgramming
                .minimumAssignment(cost);

        for (int i = 0;
             i < assignment.length;
             i++) {

            System.out.println(
                "Flight F" +
                (i + 1) +
                " -> Gate G" +
                (assignment[i] + 1)
            );
        }

        System.out.println(
            "\nMatrix Chain Cost = " +
            M3_DynamicProgramming
                .matrixChain(
                    new int[]{
                        10, 20, 30, 40
                    }
                )
        );

        System.out.println(
            "Optimal BST Cost = " +
            M3_DynamicProgramming
                .optimalBST(
                    new int[]{
                        34, 8, 50
                    }
                )
        );

        System.out.println(
            "\n========== DEMO COMPLETE =========="
        );
    }
}