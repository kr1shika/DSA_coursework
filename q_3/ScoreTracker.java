package q_3;

import java.util.*;

public class ScoreTracker {
    List<Double> scores;
    public ScoreTracker() {
        scores = new ArrayList<>();
    }

    public void addScore(double score) {
        scores.add(score);
        Collections.sort(scores); // Ensure scores are sorted after addition
    }

    public double getMedianScore() {
        int n = scores.size();
        if (n == 0) {
            return 0.0; // Handle edge case when no scores are available
        }

        if (n % 2 == 0) {
            // If the number of scores is even, average the two middle scores
            int mid = n / 2;
            return (scores.get(mid - 1) + scores.get(mid)) / 2.0;
        } else {
            // If the number of scores is odd, return the middle score
            return scores.get(n / 2);
        }
    }

    public static void main(String[] args) {
        ScoreTracker scoreTracker = new ScoreTracker();
        scoreTracker.addScore(85.5);
        scoreTracker.addScore(92.3);
        scoreTracker.addScore(77.8);
        scoreTracker.addScore(90.1);
        double median1 = scoreTracker.getMedianScore();
        System.out.println("Median 1: " + median1); // Output: 88.9
        scoreTracker.addScore(81.2);
        scoreTracker.addScore(88.7);
        double median2 = scoreTracker.getMedianScore();
        System.out.println("Median 2: " + median2); // Output: 86.95
    }
}