package q_4;

import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
    }
}

public class bst {
    public static List<Integer> closestValues(TreeNode root, double target, int x) {
        List<Integer> result = new ArrayList<>();
        if (root == null || x == 0) return result;

        PriorityQueue<int[]> pq = new PriorityQueue<>(x, (a, b) -> Double.compare(Math.abs(b[0]), Math.abs(a[0])));

        inOrderTraversal(root, target, x, pq);

        while (!pq.isEmpty()) {
            result.add(pq.poll()[1]);
        }
        return result;
    }

    private static void inOrderTraversal(TreeNode node, double target, int x, PriorityQueue<int[]> pq) {
        if (node == null) return;

        inOrderTraversal(node.left, target, x, pq);

        double diff = Math.abs(target - node.val);
        pq.offer(new int[]{(int) diff, node.val});

        if (pq.size() > x) {
            pq.poll();
        }
        inOrderTraversal(node.right, target, x, pq);
    }

    public static void main(String[] args) {
        // Construct the BST
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double k = 3.8;
        int x = 2;
        List<Integer> closest = closestValues(root, k, x);
        System.out.println("Closest values to " + k + " with distance " + x + ": " + closest);
    }
}