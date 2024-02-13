package q_2;

import java.util.HashSet;
import java.util.Set;

public class secret {
    public static void main(String[] args) {
        int n = 5; // Total number of individuals
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}}; // Secret-sharing intervals

        Set<Integer> individualsWithSecret = findIndividualsWithSecret(n, intervals);
        System.out.println("Individuals who will know the secret: " + individualsWithSecret);
    }

    private static Set<Integer> findIndividualsWithSecret(int n, int[][] intervals) {
        Set<Integer> knowsSecret = new HashSet<>();
        knowsSecret.add(0); // Person 0 initially knows the secret
        
        boolean[] shared = new boolean[n]; // Track who has shared the secret
        shared[0] = true; // Person 0 has shared the secret
        
        boolean updated = true;
        while (updated) { // Keep iterating until no new individual learns the secret
            updated = false;
            for (int[] interval : intervals) {
                for (int i = interval[0]; i <= interval[1]; i++) {
                    if (knowsSecret.contains(i)) { // If individual knows the secret, they share it
                        for (int j = interval[0]; j <= interval[1]; j++) {
                            if (!knowsSecret.contains(j)) {
                                knowsSecret.add(j);
                                updated = true;
                            }
                        }
                    }
                }
            }
        }
        
        return knowsSecret;
    }
}
