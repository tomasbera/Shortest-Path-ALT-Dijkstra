import java.io.*;
import java.util.ArrayList;

public class Interface {

    public static void main(String[] args) throws IOException {
        File nodeFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/noder.txt");
        File edgeFile = new File("Shortest-Path-ALT-Dijkstra/kanter.txt");
        File poiFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/interessepkt.txt");



        Graph dGraph = new Graph(nodeFile, edgeFile, poiFile);
        //Graph aGraph = new Graph(nodeFile, edgeFile, poiFile);

        //2375652 fly chicken
        //4546048 trd torget

        Node start = dGraph.listOfNodes[4546048];
        Node end = dGraph.listOfNodes[2375652];
        //System.out.println(end.toString());


        ArrayList<Node> nodeCount = dGraph.dijkstra(start, end);
        printDijkstra(nodeCount);

    }

    public static void printDijkstra(ArrayList<Node> nodes){
        System.out.println("The Number Of Nodes Visited was: " + nodes.size());
        for (Node node : nodes) {
            System.out.println(node.toString());
        }
    }
}
