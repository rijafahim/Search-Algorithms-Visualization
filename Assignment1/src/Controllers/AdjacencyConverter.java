
package Controllers;

import java.awt.Point;
import java.util.LinkedList;


/****
 * 
 *  This class converts the Grid on the program into an adjacency matrix over which the the algorithms are applied.
 *  00    01	 02	03
    10	  11	 12	13
    20	  21	 22	23
    30	  31	 32	33

    00 - 01, 10
    01 - 00, 11, 02
    02 - 01, 12, 03
    03 - 02, 13
    10
    11
    12
    13
    20
    21
    22
    23
    30
    31
    32
    33
 */
public class AdjacencyConverter 
{
    public int V;   //Total number of nertices
    public LinkedList<Point> adjLists[]; //Adjacency Lists
    public AdjacencyConverter(int [][]gridStatus)
    {
        int vNum = gridStatus.length;
        V = vNum*vNum;
        adjLists = new LinkedList[V];
        int counter = 0;
        for (int i=0; i<vNum; i++)
        {
            for (int j=0; j<vNum; j++)
            {
                adjLists[counter] = new LinkedList();
                if (i-1>=0) //checking for NORTH
                {
                    addEdge(counter, i-1, j);
                }
                if (j+1<vNum) ///Checking RIGHT
                {
                    addEdge(counter, i, j+1);
                }
                if (i+1<vNum) ///Checking DOWN
                {
                    addEdge(counter, i+1, j);
                }
                if (j-1>=0) ///Checking LEFT
                {
                    addEdge(counter, i, j-1);
                }
                counter++;                 
            }
        }

        /*for (int i=0; i<V; i++)
        {
            System.out.print(" - ");
            for (int j=0; j<adjLists[i].size(); j++)
            {
                System.out.print(i +" - " + adjLists[i].get(j).x + "," + adjLists[i].get(j).y + " ");
            }
            System.out.println();
        }*/
    }
    
    // Function to add an edge into the graph
    void addEdge(int VertexNO,int row, int col)
    {
        Point newPoint = new Point(row, col);
        adjLists[VertexNO].add(newPoint);
    }

}
