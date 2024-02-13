package q_2;

import java.util.Scanner;

public class sewing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of dresses in each sewing machine separated by commas (e.g., 2, 1, 3, 0, 2):");
        String inputLine = scanner.nextLine(); // Read the entire line as a single string

        // Split the input string by commas and convert each piece to an integer
        String[] parts = inputLine.split(",");
        int[] machines = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            machines[i] = Integer.parseInt(parts[i].trim()); // Convert string to int and handle any spaces
        }

        int result = calculateMinMoves(machines);
        System.out.println("Minimum number of moves required: " + result);

        scanner.close(); // Close the scanner to prevent resource leak
    }

    private static int calculateMinMoves(int[] machines) {
    int totalDresses = 0;
    for (int dresses : machines) {
        totalDresses += dresses;
    }

    int n = machines.length;
    if (totalDresses % n != 0) {
        return -1; // Equal distribution not possible
    }

    int average = totalDresses / n;
    int moves = 0;
    int balance = 0; // Represents the running total of adjustments needed to equalize to the average

    for (int i = 0; i < n; i++) {
        // The difference between the current machine's dresses and the average
        int diff = machines[i] - average;
        // Update balance to include the current difference
        balance += diff;
        // The absolute value of balance is added to moves; it represents the necessary adjustments (moves) to equalize up to this machine
        moves += Math.abs(balance);
    }

    return moves;
  }
}