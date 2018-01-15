/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Rija Fahim
 */
public class MainGridController implements Initializable 
{
    @FXML private JFXToggleButton hurdleState;
    @FXML private ToggleGroup stateSelection;
    @FXML private Pane gridarea;
    @FXML private JFXComboBox<String> searchOptions;
    @FXML private JFXButton submit;
    @FXML private Label Space;
    @FXML private Label Complete;
    @FXML private Label Strategy;
    @FXML private Label Time;
    String WHITE = "0xffffffff";
    String GREEN = "0x008000ff";
    Rectangle [][] rec;
    int [][] gridStatus; //0 unvisited, 1 for visited, 2 for hurdle, 4 for start state, 5 for end state
    private ObservableList<String> searchOptionsValues = FXCollections.observableArrayList();
     public static ArrayList <Point> visitedNodes = new ArrayList<Point>();
    public static ArrayList<ArrayList <Point>> fringe = new ArrayList<ArrayList<Point>>();
    private ArrayList<Point> fringeNodes = new ArrayList<Point>();
    
    public void Animate(ArrayList <Point> node, ArrayList<ArrayList <Point>> fringe) throws InterruptedException
    {   
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                for (int i=0; i<node.size(); i++)
                {
                    try {
                        Thread.sleep(50); // Wait for 1 sec before updating the color
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int finalI = i;
                    final Point temp= node.get(finalI); 
                    //System.out.print(temp.x + "," + temp.y + "  - ");// Update on JavaFX Application Thread
                    Platform.runLater(new Runnable() {
                            @Override public void run() 
                            { 
                                rec[temp.y][temp.x].setFill(Color.CADETBLUE);
                                for (int j=0; j<fringe.get(finalI).size(); j++)
                                {
                                    final Point temp2 = fringe.get(finalI).get(j);
                                    //System.out.print(temp2.x + "," + temp2.y + " ");// Update on JavaFX Application Thread
                                    rec[temp2.y][temp2.x].setFill(Color.AQUA);
                                  
                               }
                            } 
                    }); 
                }
            }
            }).start();    
    }
    public void Animate2(ArrayList<ArrayList <Point>>  node, ArrayList<ArrayList <Point>> fringe) throws InterruptedException
    {   
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                for (int i=0; i<node.size(); i++)
                {
                    try {
                        Thread.sleep(50); // Wait for 1 sec before updating the color
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
  
                    Platform.runLater(new Runnable() {
                            @Override public void run() 
                            { 
                                for (int i=0; i<node.size(); i++)
                                {
                                    for (int j=0; j<node.get(i).size(); j++)
                                    {
                                        final Point temp = node.get(i).get(j);
                                        rec[temp.y][temp.x].setFill(Color.CADETBLUE);
                                    }
                                }
                                
                                for (int i=0; i<fringe.size(); i++)
                                {
                                    for (int j=0; j<fringe.get(i).size(); j++)
                                    {
                                        final Point temp = fringe.get(i).get(j);
                                        rec[temp.y][temp.x].setFill(Color.AQUA);
                                    }
                                }

                            } 
                    }); 
                }
            }
            }).start();    
    }

    
    
    /* A function to perform a Depth-Limited search from given source cell */
    public boolean DLS(Point startPoint, Point endPoint, int depthLimit, AdjacencyConverter obj)
    {
        boolean flag = true;
        ArrayList<Point> temp = new ArrayList<>();
        if (startPoint.x == endPoint.x && startPoint.y == endPoint.y)
        {
            return true;
        }
        if (depthLimit <= 0)//if reached the max depth, stop
        {
            return false;
        }
        int vertex = getVertexNumber(startPoint.x, startPoint.y, gridStatus.length); // Recur for all the vertices adjacent to source vertex

        if (gridStatus[startPoint.x][startPoint.y] == 0 || gridStatus[startPoint.x][startPoint.y] == 4)
        {
            visitedNodes.add(startPoint);
            gridStatus[startPoint.x][startPoint.y] = 1;
            flag = false;
        }
        
        for (int i = 0 ; i<obj.adjLists[vertex].size(); i++ )
        {     
            if (!flag){
                for (int j=0; j<obj.adjLists[vertex].size(); j++) //checks for unvisited states to add to fringe
                {  
                    Point temp3 = new Point();
                    temp3 = obj.adjLists[vertex].get(j);
                    if (!fringeNodes.contains(temp3) && !(gridStatus[temp3.x][temp3.y] == 1))
                    {
                        fringeNodes.add(temp3);
                    }
                }
            }

            Point n = obj.adjLists[vertex].get(i);              //takes existing node
            if (DLS(n, endPoint, depthLimit-1, obj) == true) //call the DFS function
            {
                return true;
            }
        }
        return false;
    }

 
    // IDDFS to search if target is reachable from v.It uses recursive DFSUtil().
    public boolean IIDFS (int gridStatus[][],  AdjacencyConverter obj) throws InterruptedException
    {
        ArrayList<ArrayList <Point>> visitedNodes_Collective = new ArrayList<ArrayList<Point>>();
        ArrayList<ArrayList <Point>> fringe2 = new ArrayList<ArrayList<Point>>();
        // Repeatedly depth-limit search till the maximum depth.
        Point startPoint = new Point();
        Point endPoint = new Point();
        for ( int i=0; i<gridStatus.length; i++)//The start state will already be considered visited and stored to be passed on the recursive function
        {
            for (int j=0; j<gridStatus.length; j++)
            {
                //System.out.print(gridStatus[i][j]);
                if (gridStatus[i][j] == 4)
                {
                    startPoint.x = i;
                    startPoint.y = j;
                    
                }
                if (gridStatus[i][j] == 5)
                {
                    endPoint.x = i;
                    endPoint.y = j;    
                }
            }
            //System.out.println();
        }
        
        int depth = gridStatus.length*2;
        for (int i = 0; i <= depth; i++)
        {
            //System.out.println(i + " Iteration");
            if (DLS(startPoint, endPoint, i, obj) == true)
            {
                return true;
            }
        }
        return false;
    }

        
    /* A function used by DFS and called by recursion */
    public int DFS_Recursion(Point point, int gridStatus[][], AdjacencyConverter obj, boolean flag)
    {
            gridStatus[point.x][point.y] = 1;  // Mark the current node as visited 
            visitedNodes.add(point);
            System.out.println(point.x + "," + point.y + " "); // print
            ArrayList<Point> temp = new ArrayList<>();
            int vertex = getVertexNumber(point.x, point.y, gridStatus.length);
            Iterator<Point> i = obj.adjLists[vertex].listIterator(); // Recur for all the vertices adjacent to this vertex
            while (i.hasNext())
            {
                Point n = new Point();
                n = i.next();
                for (int j=0; j<obj.adjLists[vertex].size(); j++)
                {
                    Point temp3 = new Point();
                    temp3 = obj.adjLists[vertex].get(j);
                    if ((gridStatus[n.x][n.y] == 0 || gridStatus[n.x][n.y] == 5))
                    {
                        temp.add(n);
                    }
                }
                
                if (gridStatus[n.x][n.y] == 0)
                {
                    fringe.add(temp);
                    DFS_Recursion(n, gridStatus, obj, true);
                }
                else if (gridStatus[n.x][n.y] == 5)
                {
                    flag = false;
                    break;
                    
                }
            }
            if (flag == false)
            {
                return 0;
            }
        return 1;
    }
    
    /* The DFS function. Uses another function that is called recursively. The status of the grid is passed as parameter
       containing visited/non visited nodes. The Grid converted to an adjacency list is also passed.
    */
    public void DFS(int gridStatus[][],  AdjacencyConverter obj)
    {
        Point startPoint = new Point();
        for ( int i=0; i<gridStatus.length; i++)//The start state will already be considered visited and stored to be passed on the recursive function
        {
            for (int j=0; j<gridStatus.length; j++)
            {
                if (gridStatus[i][j] == 4)
                {
                    startPoint.x = i;
                    startPoint.y = j;
                    break;
                }
            }
        }
        int ans = DFS_Recursion(startPoint, gridStatus, obj, true); // Call the recursive helper function to print DFS traversal
        System.out.println(ans);
        if (ans == 0)
        {
            System.out.println("kejgnkng");
            return;
        }
    }

    
    /* This function is called whent the user clicks the Play button to play the animation */
    @FXML
    void submit(ActionEvent event) throws InterruptedException 
    {
        for (int i=0; i<Integer.parseInt(Assignment1.N); i++)
        {
            for  (int j=0; j<Integer.parseInt(Assignment1.N); j++)
            {
                System.out.print(gridStatus[i][j]);
            }
            System.out.println();
        }
        AdjacencyConverter obj = new AdjacencyConverter(gridStatus);
        
        
        if (searchOptions.getValue().equals("DFS"))
        {
            DFS(gridStatus, obj);
            Animate(visitedNodes, fringe);
            printOutput("Depth First Search");
        }
        else if (searchOptions.getValue().equals("A*"))
        {
            System.out.println("In thisss");
            double width = gridarea.getPrefWidth()/Integer.parseInt(Assignment1.N);
            int desX = 0; int desY = 0;
            for (int i= 0; i<Integer.parseInt(Assignment1.N); i++){
                for (int j=0; j<Integer.parseInt(Assignment1.N); j++){
                    if (gridStatus[i][j] == 5){  desX = i; desY = j; System.out.println(desX + " " + desY);}
                }
            }
            for (int i=0; i<Integer.parseInt(Assignment1.N); i++){
                for (int j=0; j<Integer.parseInt(Assignment1.N); j++){
                    if (gridStatus[i][j] == 4)
                    {
                        System.out.println("source found");
                        Astar.test(1,Integer.parseInt(Assignment1.N), Integer.parseInt(Assignment1.N), i, j, desX, desY, gridStatus );
                        break;
                    }
                }
            }  
            Collections.reverse(visitedNodes);
            Animate(visitedNodes, fringe);
            printOutput("A* Search");
        }
        else if (searchOptions.getValue().equals("Greedy Best First"))
        {
            boolean ans = GreedyBestFirstSearch.pathExists(gridStatus);
            System.out.println(ans);
            Animate(visitedNodes,fringe);
            printOutput("Greedy Best First Search");
        }
        else if (searchOptions.getValue().equals("BFS"))
        {
            BFS(gridStatus, obj);
            Animate(visitedNodes, fringe);
            printOutput("Breadth First Search");
        }
        else if (searchOptions.getValue().equals("UCS"))
        {
            BFS(gridStatus, obj);
            Animate(visitedNodes, fringe);
            printOutput("Breadth First Search");
        }
        else if (searchOptions.getValue().equals("IDS"))
        {
            boolean ans = IIDFS(gridStatus, obj);
            if (ans)
            {
                System.out.println("Reachable");
            }
            else 
            {
                System.out.println("Not reachable");
            }
            Animate(visitedNodes, fringe);
            printOutput("Iterative Deepening Search");
        }
    }
    /* 
        We send in an argument of the grid displayed by boolean values. They'll all be marked unvisited 
        We also send in an adjacency matrix of the grid where the BFS will be performed
    */
    public void BFS(int gridStatus[][], AdjacencyConverter obj) throws InterruptedException
    {
        LinkedList<Point> queue = new LinkedList<Point>(); // Create a queue for BFS
        for ( int i=0; i<gridStatus.length; i++)//The start state will already be considered visited and will be enqueued
        {
            for (int j=0; j<gridStatus.length; j++)
            {
                if (gridStatus[i][j] == 4)
                {
                    Point startPoint = new Point(i, j);
                    queue.add(startPoint);
                    break;
                }
            }
        }
        
        Point newPoint = new Point();
        while (queue.size() != 0)
        {            
            newPoint = queue.poll(); // Dequeue a vertex from queue and print it
            int vertex = getVertexNumber(newPoint.x, newPoint.y, gridStatus.length); // Get all adjacent vertices of the dequeued vertex. If a adjacent has not been visited, then mark it visited and enqueue it
            System.out.println(newPoint.x + "," + newPoint.y);
            visitedNodes.add(newPoint); //adds to visited Nodes
            ArrayList<Point> temp = new ArrayList<>();
            /*for (int i=0; i<obj.adjLists[vertex].size(); i++){ 
                System.out.print(obj.adjLists[vertex].get(i).x + "," + obj.adjLists[vertex].get(i).y + " "); //add nodes to the Fringe
                temp.add(obj.adjLists[vertex].get(i));
            }
            System.out.println();
            fringe.add(temp);*/
            int index = 0;
            
            while (index<obj.adjLists[vertex].size())
            {
                Point n = new Point();
                n = obj.adjLists[vertex].get(index);
                if (gridStatus[n.x][n.y] == 0 || gridStatus[n.x][n.y] == 5)
                {
                    temp.add(n);
                    if (gridStatus[n.x][n.y] == 5)
                    {
                        System.out.println("Goal state reached");
                        gridStatus[n.x][n.y] = 1;
                        return;
                    }
                    gridStatus[n.x][n.y] = 1;
                    queue.add(n);
                }
                index++;
            }
            fringe.add(temp);
        }
    }
    
    public void printOutput(String strategy)
    {
        String time = null;
        String space = null;
        double time_temp;
        double space_temp;
        String complete = null;
        if (strategy.equals("Breadth First Search") || strategy.equals("Uniform Cost Search"))
        {
            time_temp = Math.pow(4, gridStatus.length +1);
            time = Double.toString(time_temp ) + " nodes";
            complete = "Yes";
            space_temp = Math.pow(4, gridStatus.length +1);
            space = Double.toString(space_temp) + " nodes";
        }
        else if (strategy.equals("Depth First Search") || strategy.equals("Iterative Deepening Search") )
        {
            complete = "Yes";
            time_temp = Math.pow(4, gridStatus.length);
            time = Double.toString(time_temp ) + " nodes";
            space_temp = 4*gridStatus.length ;
            space = Double.toString(space_temp) + " nodes";
        }
        else if (strategy.equals("A* Search"))
        {
            complete = "Yes";
            space = "Keeps all nodes in memory";
            time = "Exponential";
        }
        else if (strategy.equals("Greedy Best First Search"))
        {
            complete = "Yes";
            time_temp = Math.pow(4, gridStatus.length);
            time = Double.toString(time_temp ) + " nodes";
            space_temp = Math.pow(4, gridStatus.length);
            space = Double.toString(space_temp) + " nodes";   
        }
        Strategy.setText("Strategy: " + strategy);
        Time.setText("Time: " + time);
        Space.setText("Space: " +space);
        Complete.setText("Complete: " + complete);
        Strategy.setTextFill(Color.WHITE);
        Time.setTextFill(Color.WHITE);
        Space.setTextFill(Color.WHITE);
        Complete.setTextFill(Color.WHITE);
        Complete.setFont(Font.font ("Verdana", 12));
        Time.setFont(Font.font ("Verdana", 12));
        Space.setFont(Font.font ("Verdana", 12));
        Strategy.setFont(Font.font ("Verdana", 12));
    }
    /*For a particular row and column of the gird, this returns the vertex number */
    public int getVertexNumber(int row, int col, int size)
    {
        int counter = row*size + col;
        return counter;
    }
    
    /* The initial most funtion called to load the graphics and initialise called member variable */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        double width = gridarea.getPrefWidth()/Integer.parseInt(Assignment1.N);
        rec = new Rectangle [Integer.parseInt(Assignment1.N)][Integer.parseInt(Assignment1.N)];
        gridStatus = new int  [Integer.parseInt(Assignment1.N)][Integer.parseInt(Assignment1.N)];  
        searchOptionsValues.add("DFS");
        searchOptionsValues.add("BFS");
        searchOptionsValues.add("UCS");
        searchOptionsValues.add("IDS");
        searchOptionsValues.add("Greedy Best First");
        searchOptionsValues.add("A*");
        searchOptions.setItems(searchOptionsValues);
        for(int i=0; i<Integer.parseInt(Assignment1.N); i++)
        {
            for(int j=0; j<Integer.parseInt(Assignment1.N); j++)
            {
                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * width);
                rec[i][j].setY(j * width);
                rec[i][j].setWidth(width);
                rec[i][j].setHeight(width);
                rec[i][j].setFill(Color.WHITE);
                rec[i][j].setStroke(Color.BLACK);
                gridarea.getChildren().add(rec[i][j]);
                gridStatus[i][j] = 0;
            }
        }
        
        gridarea.setOnMouseClicked(new EventHandler <MouseEvent> ()
        { 
            @Override 
            public void handle(MouseEvent me)
            {       
                double posX = me.getX();
                double posY = me.getY();
                int colX = (int)(posX / width);
                int colY = (int) (posY / width);
                if (!hurdleState.isSelected())
                {
  
                    if (rec[colX][colY].getFill().toString().equals(WHITE))
                    {
                        System.out.println(colY + " " + colX);
                        rec[colX][colY].setFill(Color.GREEN);
                        gridStatus[colY][colX] = 4;
                    }
                    else if (rec[colX][colY].getFill().toString().equals(GREEN))
                    {
                        System.out.println(colY + " " + colX);
                        rec[colX][colY].setFill(Color.RED);
                        gridStatus[colY][colX] = 5;
                    }

                }
                else
                {
                    rec[colX][colY].setFill(Color.BLACK);
                    gridStatus[colX][colY] = 2;
                }
            } 
        });
        
    
    }
    
}
