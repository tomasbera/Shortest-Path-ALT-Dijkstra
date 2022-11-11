import java.io.*;
import java.util.ArrayList;

public class Interface {

    public static void main(String[] args) throws IOException {
        File nodeFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/nodeIsland.txt");
        File edgeFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/kantIsland.txt");
        File poiFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/intereIsland.txt");
        File outputfile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/PreFrom.txt");
        File outputfileRev = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/PreTo.txt");

        Graph dGraph = new Graph(nodeFile, edgeFile, poiFile);

        //2375652 fly thon hotel
        //4546048 trd torget
        //rejkavik kitchen 93074
        //rejkavik hotel 90235
        Node start = dGraph.listOfNodes[93074];
        Node end = dGraph.listOfNodes[90235];

        long s = System.nanoTime();
        ArrayList<Node> nodeCount = dGraph.dijkstra(start, end);
        System.out.println("time used in milli: "+ (System.nanoTime() - s)/1000000);
        printGraph(nodeCount);

        //Node[] visitedTypes = dGraph.dijkstraPOI(start, 8);
        //printPOIDijkstra(visitedTypes);
        //dGraph.runPreProses(edgeFile);


        ArrayList<Node> nodeCountALT = dGraph.runALT(start, end);
        printGraph(nodeCountALT);
    }

    public static void printGraph(ArrayList<Node> nodes){
        Node n = nodes.get(nodes.size()-1);
        while (n != null){
            System.out.println(n.latitude + "," + n.longitude);
            n = n.d.prev;
        }

        System.out.println("The Number Of Nodes Visited was: " + nodes.size());
        System.out.println("The length from start to end is: "+ nodes.get(nodes.size()-1).d.dist);
        System.out.println();
    }

    public static void printPOIDijkstra(Node[] poi){
        for (Node node : poi) {
            System.out.println("Distance to " + node.POI + " was: " + node.d.dist);
        }
        System.out.println();
    }
}
