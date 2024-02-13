package q_4;
import java.util.*;

public class mazeGame {
    public List<String> shortestPathAllKeys(String[] grid) {
        int R = grid.length, C = grid[0].length();
        int sr = 0, sc = 0, keys = 0;
        for (int r = 0; r < R; ++r)
            for (int c = 0; c < C; ++c) {
                char ch = grid[r].charAt(c);

                if (ch == '@') {
                    sr = r;
                    sc = c;
                } else if (isLowercase(ch)) {
                    keys++;
                }
            }

        int[][] dirs = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Queue<State> queue = new LinkedList<>();
        Set<String> seen = new HashSet<>();
        State start = new State(sr, sc, new HashSet<>(), "");
        queue.add(start);
        seen.add(start.toString());
        while (!queue.isEmpty()) {
            State state = queue.remove();
            if (state.keys.size() == keys) {
                return Arrays.asList(state.path.split(""));
            }
            for (int[] dir : dirs) {
                int r = state.r + dir[0];
                int c = state.c + dir[1];
                if (r < 0 || c < 0 || r >= R || c >= C) {
                    continue;
                }
                char ch = grid[r].charAt(c);

                Set<Character> keysNeeded = new HashSet<>(state.keys);
                if (isUppercase(ch)) {
                    keysNeeded.add((char) (ch - 'A' + 'a'));
                    if (!state.keys.contains((char) (ch - 'A' + 'a'))) {
                        continue;
                    }
                }
                State next = new State(r, c, keysNeeded, state.path + ch);
                if (!seen.contains(next.toString())) {
                    queue.add(next);
                    seen.add(next.toString());
                }
            }
        }
        System.out.println("No valid path found.");
        return new ArrayList<>();
    }

    private boolean isUppercase(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    private boolean isLowercase(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    class State {
        int r, c;
        Set<Character> keys;
        String path;

        State(int r, int c, Set<Character> keys, String path) {
            this.r = r;
            this.c = c;
            this.keys = keys;
            this.path = path;
        }

        @Override
        public String toString() {
            return r + "," + c + "," + keys.toString() + "," + path;
        }
    }

    public static void main(String[] args) {
        mazeGame solution = new mazeGame();

        // Example grid
        String[] grid = {
            "@.a",
            "###",
            "bA#"
        };

        List<String> result = solution.shortestPathAllKeys(grid);

        System.out.println("HHI");
        if (!result.isEmpty()) {
            System.out.println("Shortest path to collect all keys: " + result.get(0));
        } else {
            System.out.println("No valid path to collect all keys.");
        }

    }
}
