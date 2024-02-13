
import java.util.Arrays;

public class event_manager {

    public static int findMinimumCost(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int venues = costs.length;
        int themes = costs[0].length;

        int[][] memo = new int[venues][themes];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        int[] chosenThemes = new int[venues];
        int[] chosenCosts = new int[venues];

        int minimumCost = recursiveHelper(costs, venues, themes, 0, -1, memo, chosenThemes, chosenCosts);

        System.out.println("Chosen Themes and Costs:");
        for (int i = 0; i < venues; i++) {
            System.out.println("Venue " + i + ": Theme " + chosenThemes[i] + " (Cost: " + chosenCosts[i] + ")");
        }

        return minimumCost;
    }

    private static int recursiveHelper(int[][] costs, int venues, int themes, int venue, int prevTheme, int[][] memo, int[] chosenThemes, int[] chosenCosts) {
        if (venue == venues) {
            return 0;
        }

        if (prevTheme != -1 && memo[venue][prevTheme] != -1) {
            return memo[venue][prevTheme];
        }

        int minCost = Integer.MAX_VALUE;
        int bestTheme = -1;

        for (int currentTheme = 0; currentTheme < themes; currentTheme++) {
            if (currentTheme != prevTheme) {
                int currentCost = costs[venue][currentTheme] +
                        recursiveHelper(costs, venues, themes, venue + 1, currentTheme, memo, chosenThemes, chosenCosts);

                if (currentCost < minCost) {
                    minCost = currentCost;
                    bestTheme = currentTheme;
                }
            }
        }

        if (prevTheme != -1) {
            memo[venue][prevTheme] = minCost;
        }

        chosenThemes[venue] = bestTheme;
        chosenCosts[venue] = costs[venue][bestTheme];

        return minCost;
    }

    public static void main(String[] args) {
        int[][] inputCosts = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        int minimumCost = findMinimumCost(inputCosts);
        System.out.println("Minimum cost to decorate all venues: " + minimumCost);
    }
}
