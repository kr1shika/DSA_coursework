package dsatask_7;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import database.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class UserGraph {
    private Set<Edge> edges; // Set of edges representing connections between users
    private Map<String, Set<String>> followersMap; // Map to store followers for each user

    public UserGraph() {
        edges = new HashSet<>();
        followersMap = new HashMap<>();
    }

    // Add a new user to the graph
    public void addUser(String username) {
    followersMap.put(username, new HashSet<>());
} // Add this closing curly brace


public void addConnection(String follower, String followee) {
    Edge edge = new Edge(follower, followee);
    if (edges.contains(edge)) {
        System.out.println("Users are already connected.");
    } else {
        edges.add(edge);
        followersMap.get(followee).add(follower);
        System.out.println(follower + " follows " + followee);
        // Update the 'following' set for the follower
        followersMap.computeIfAbsent(follower, k -> new HashSet<>()).add(followee);
    }
}


    // Get mutual followers for a given user
    public Set<String> getReccomend(String username) {
        Set<String> mutualFollowers = new HashSet<>();

        Set<String> following = followersMap.getOrDefault(username, new HashSet<>());
        System.out.println("Following of " + username + ": " + following);

        for (String followedUser : following) {
            Set<String> followersOfFollowedUser = followersMap.getOrDefault(followedUser, new HashSet<>());
            System.out.println("Followers of the followed user " + followedUser + " are: " + followersOfFollowedUser);

            for (String follower : followersOfFollowedUser) {
                if (!follower.equals(username) && !following.contains(follower) && !mutualFollowers.contains(follower)) {
                    mutualFollowers.add(follower);
                }
            }
        }
        return mutualFollowers;
    }

    // Class to represent an edge between two users
    private static class Edge {
        private String follower;
        private String followee;

        public Edge(String follower, String followee) {
            this.follower = follower;
            this.followee = followee;
        }

        @Override
        public int hashCode() {
            return follower.hashCode() + followee.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Edge)) return false;
            Edge otherEdge = (Edge) obj;
            return this.follower.equals(otherEdge.follower) && this.followee.equals(otherEdge.followee);
        }
    }
    
    // Method to save users and their connections to the database
    public void saveToDatabase(Connection connection) {
        if (connection == null) {
            System.out.println("Database connection is not established.");
            return;
        }

        try {
            // Insert users into the Users table
            String insertUserSql = "INSERT INTO Users (username) VALUES (?)";
            PreparedStatement userStatement = connection.prepareStatement(insertUserSql);

            for (String username : followersMap.keySet()) {
                userStatement.setString(1, username);
                userStatement.executeUpdate();
            }

            // Insert connections into the Connections table
            String insertConnectionSql = "INSERT INTO Connections (follower_username, followee_username) VALUES (?, ?)";
            PreparedStatement connectionStatement = connection.prepareStatement(insertConnectionSql);

            for (String follower : followersMap.keySet()) {
                Set<String> followees = followersMap.get(follower);
                for (String followee : followees) {
                    connectionStatement.setString(1, follower);
                    connectionStatement.setString(2, followee);
                    connectionStatement.executeUpdate();
                }
            }

            System.out.println("Users and connections saved to the database successfully.");

            // Close prepared statements
            userStatement.close();
            connectionStatement.close();
        } catch (Exception e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }
    
    // Method to load data from the database and fill the graph
    public void loadDataFromDatabase(Connection connection) {
        if (connection == null) {
            System.out.println("Database connection is not established.");
            return;
        }

        try {
            // Query to retrieve users and their connections
            String query = "SELECT u.username, c.followee_username\n" +
"FROM Users u\n" +
"JOIN Connections c ON u.username = c.follower_username;";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear the existing graph
            followersMap.clear();

            // Iterate through the result set and populate the graph
            while (resultSet.next()) {
                String follower = resultSet.getString("username");
                String followee = resultSet.getString("followee_username");

                // Add the follower to the graph if not already present
                followersMap.putIfAbsent(follower, new HashSet<>());

                // Add the followee to the follower's connections
                followersMap.get(follower).add(followee);
            }

            System.out.println("Data loaded from the database and graph filled successfully.");

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Error loading data from database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        UserGraph ug = new UserGraph();
//        ug.addUser("user1");
//        ug.addUser("user2");
//        ug.addUser("user3");
//        ug.addUser("user4");
//        ug.addConnection("user1", "user2");
//        ug.addConnection("user3", "user2");
//        ug.addConnection("user4", "user2");
//        ug.addConnection("user1", "user4");
//        System.out.println(ug.getReccomend("user1"));
    }
}
