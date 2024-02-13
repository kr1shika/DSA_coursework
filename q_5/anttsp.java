package q_5;
import java.util.Arrays;
import java.util.Random;

public class anttsp {
    private double[][] distance; // Distance between cities
    private int numOfCities;
    private int numOfAnts;
    private double[][] pheromones;
    private double alpha; // Pheromone importance
    private double beta; // Distance importance
    private double evaporation;
    private double Q; // Pheromone deposit factor
    private double antFactor; // Proportion of ants to cities
    private double randomFactor; // Randomness in path selection

    public anttsp(int numOfCities) {
        this.numOfCities = numOfCities;
        this.distance = new double[numOfCities][numOfCities];
        this.antFactor = 0.8;
        this.numOfAnts = (int) (numOfCities * antFactor);
        this.pheromones = new double[numOfCities][numOfCities];
        this.alpha = 1;
        this.beta = 5;
        this.evaporation = 0.5;
        this.Q = 500;
        this.randomFactor = 0.01;
    }

    public void solve() {
        setupDistanceMatrix();
        initializePheromones();

        int[] bestSolution = new int[numOfCities];
        double bestDistance = Double.MAX_VALUE;

        for (int i = 0; i < 100; i++) { // Number of iterations
            int[][] solutions = new int[numOfAnts][numOfCities];
            for (int k = 0; k < numOfAnts; k++) {
                solutions[k] = createRoute(k);
            }

            updatePheromones(solutions);

            for (int k = 0; k < numOfAnts; k++) {
                double currentDistance = calculateTotalDistance(solutions[k]);
                if (currentDistance < bestDistance) {
                    bestDistance = currentDistance;
                    bestSolution = solutions[k].clone();
                }
            }
        }

        System.out.println("Best distance: " + bestDistance);
        System.out.println("Best solution: " + Arrays.toString(bestSolution));
    }

    private void setupDistanceMatrix() {
        Random random = new Random();
        for (int i = 0; i < numOfCities; i++) {
            for (int j = i + 1; j < numOfCities; j++) {
                distance[i][j] = 20 + (100 - 20) * random.nextDouble(); // Random distance between 20 and 100 units
                distance[j][i] = distance[i][j]; // Symmetric distance
            }
        }
    }

    private void initializePheromones() {
        for (int i = 0; i < numOfCities; i++) {
            for (int j = 0; j < numOfCities; j++) {
                pheromones[i][j] = 1;
            }
        }
    }

    private int[] createRoute(int antNum) {
        int[] route = new int[numOfCities];
        boolean[] visited = new boolean[numOfCities];
        route[0] = antNum % numOfCities; // Start from different cities for different ants
        visited[route[0]] = true;

        for (int i = 1; i < numOfCities; i++) {
            int cityX = route[i - 1];
            route[i] = selectNextCity(cityX, visited);
            visited[route[i]] = true;
        }
        return route;
    }

    private int selectNextCity(int currentCity, boolean[] visited) {
        double[] probabilities = new double[numOfCities];
        double probabilitySum = 0.0;

        for (int i = 0; i < numOfCities; i++) {
            if (!visited[i]) {
                probabilities[i] = Math.pow(pheromones[currentCity][i], alpha) * Math.pow(1.0 / distance[currentCity][i], beta);
                probabilitySum += probabilities[i];
            }
        }

        // Normalize probabilities
        for (int i = 0; i < numOfCities; i++) {
            if (!visited[i]) {
                probabilities[i] /= probabilitySum;
            }
        }

        // Roulette wheel selection
        double randomValue = Math.random(), cumulativeProbability = 0.0;
        for (int i = 0; i < numOfCities; i++) {
            if (!visited[i]) {
                cumulativeProbability += probabilities[i];
                if (randomValue <= cumulativeProbability) {
                    return i;
                }
            }
        }
        return -1; // Fallback, should not happen
    }

    private void updatePheromones(int[][] solutions) {
        for (int i = 0; i < numOfCities; i++) {
            for (int j = 0; j < numOfCities; j++) {
                pheromones[i][j] *= (1.0 - evaporation);
            }
        }

        for (int[] solution : solutions) {
            double routeLength = calculateTotalDistance(solution);
            double depositAmount = Q / routeLength;
            for (int i = 0; i < solution.length - 1; i++) {
                pheromones[solution[i]][solution[i + 1]] += depositAmount;
                pheromones[solution[i + 1]][solution[i]] += depositAmount; // Assuming symmetric TSP
            }
            // Deposit pheromones for the return path to the starting city
            pheromones[solution[solution.length - 1]][solution[0]] += depositAmount;
            pheromones[solution[0]][solution[solution.length - 1]] += depositAmount;
        }
    }

    private double calculateTotalDistance(int[] solution) {
        double length = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            length += distance[solution[i]][solution[i + 1]];
        }
        length += distance[solution[solution.length - 1]][solution[0]]; // Return to start
        return length;
    }

    public static void main(String[] args) {
        anttsp aco = new anttsp(10); // Example: 10 cities
        aco.solve();
    }
}