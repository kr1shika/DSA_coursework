import java.util.PriorityQueue;

public class EngineBuilding {

    public static int minBuildTime(int[] engines, int splitTime) {
        // Create a priority queue to hold the engines, with the natural order (i.e., min-heap)
        PriorityQueue<Integer> engineQueue = new PriorityQueue<>();

        // Add all engines to the priority queue
        for (int engine : engines) {
            engineQueue.offer(engine);
        }

        // Continue combining engines until there's only one engine left
        while (engineQueue.size() > 1) {
            // Remove the smallest two engines from the queue
            int fastestEngine = engineQueue.poll();
            int secondFastestEngine = engineQueue.poll();

            // The two engineers team up to split and build the next engine;
            // add the combined time back to the priority queue.
            // The time is the time of the second engineer plus the split time.
            engineQueue.offer(secondFastestEngine + splitTime);
        }

        // The remaining engine in the priority queue requires the longest time to complete.
        // This will be the total time required to build all the engines.
        return engineQueue.poll();
    }

    public static void main(String[] args) {
        int[] engines = {5, 3, 7, 2, 8};
        int splitTime = 4;

        int totalTime = minBuildTime(engines, splitTime);

        System.out.println("Minimum time to build all engines: " + totalTime);
    }
}
