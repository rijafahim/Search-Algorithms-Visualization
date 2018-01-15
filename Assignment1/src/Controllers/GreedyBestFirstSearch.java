package Controllers;


import java.awt.Point;
import java.util.*;
import java.lang.*;

class Node {
    int x;
    int y;
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class GreedyBestFirstSearch
{  
    public static boolean pathExists(int[][] matrix) {
        int N = matrix.length;
        
        List<Node> queue = new ArrayList<Node>();
        for (int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix.length; j++)
            {
                if (matrix[i][j] == 4)
                {
                    queue.add(new Node(i, j));
                }
            }
        }
        
        boolean pathExists = false;
        
        while(!queue.isEmpty()) {
            Node current = queue.remove(0);
            if(matrix[current.x][current.y] == 5) {
                pathExists = true;
                break;
            }
            
            matrix[current.x][current.y] = '0'; // mark as visited
            Point temp = new Point();
            temp.x = current.x;
            temp.y = current.y;
            MainGridController.visitedNodes.add(temp);
            List<Node> neighbors = getNeighbors(matrix, current);
            queue.addAll(neighbors);
        }
        
        return pathExists;
    }
    
    public static List<Node> getNeighbors(int[][] matrix, Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        
        if(isValidPoint(matrix, node.x - 1, node.y)) {
            neighbors.add(new Node(node.x - 1, node.y));
        }
        
        if(isValidPoint(matrix, node.x + 1, node.y)) {
            neighbors.add(new Node(node.x + 1, node.y));
        }
        
        if(isValidPoint(matrix, node.x, node.y - 1)) {
            neighbors.add(new Node(node.x, node.y - 1));
        }
        
        if(isValidPoint(matrix, node.x, node.y + 1)) {
            neighbors.add(new Node(node.x, node.y + 1));
        }
        
        return neighbors;
    }
    
    public static boolean isValidPoint(int[][] matrix, int x, int y) {
        return !(x < 0 || x >= matrix.length || y < 0 || y >= matrix.length) && (matrix[x][y] != '2');
    }
}