import java.io.*;
import java.util.ArrayList;

public class Interface {

    public static void main(String[] args) throws IOException {
        File nodeFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/nodeIsland.txt");
        File edgeFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/kantIsland.txt");
        File poiFile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/intereIsland.txt");
        File outputfile = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/pre.txt");
        File outputfileRev = new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/prerev.txt");

        Graph dGraph = new Graph(nodeFile, edgeFile, poiFile);

        //2375652 fly thon hotel
        //4546048 trd torget
        //rejkavik kitchen 93074
        //rejkavik hotel 1424
        Node start = dGraph.listOfNodes[93074];
        Node end = dGraph.listOfNodes[90235];

        runPreProses(dGraph, outputfile, outputfileRev);
        //ArrayList<Node> nodeCount = dGraph.dijkstra(start, end);
        //printDijkstra(nodeCount);
        //Node[] visitedTypes = dGraph.dijkstraPOI(start, 8);
        //printPOIDijkstra(visitedTypes);
    }

    public static void printDijkstra(ArrayList<Node> nodes){
        System.out.println("The Number Of Nodes Visited was: " + nodes.size());
        System.out.println("The length from start to end is: "+ nodes.get(nodes.size()-1).d.dist);
        for (Node node : nodes) {
            System.out.println(node.toString());
        }
    }

    public static void printPOIDijkstra(Node[] poi){
        for (Node node : poi) {
            System.out.println("Distance to " + node.nodeNum + " was: " + node.d.dist);
        }
    }

    public static void runPreProses(Graph altGraph, File outputfile, File outputfileRev) throws IOException {
        Node []preNodes;
        preNodes = findThreePOI(altGraph);
        altGraph.readEdgesRev(outputfile);
        runDijkstraALT(preNodes, altGraph, outputfile);
        altGraph.readEdges(outputfileRev);
        runDijkstraALT(preNodes, altGraph, outputfileRev);

    }

    public static void runDijkstraALT(Node[] preNodes, Graph altGraph, File outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(preNodes.length);
        stringBuilder.append(" ");
        stringBuilder.append(altGraph.vertices);
        stringBuilder.append("\n");

        for (int i = 0; i < preNodes.length; i++) {
            System.out.println(preNodes[i].nodeNum);
            stringBuilder.append(preNodes[i].nodeNum);
            altGraph.dijkstraPrePos(preNodes[i]);
            for (int j = 0; j < altGraph.vertices; j++) {
                stringBuilder.append(" ");
                stringBuilder.append(altGraph.listOfNodes[i].d.dist);
            }
            stringBuilder.append("\n");
        }
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(stringBuilder.toString());
        bw.close();
    }

    public static Node[] findThreePOI(Graph g){
        Node []POIlist = new Node[3];
        Node node = g.listOfNodes[1];
        for (int i = 0; i < g.listOfNodes.length; i++) {
            if (g.listOfNodes[i].latitude > node.latitude) node = g.listOfNodes[i];
        }
        POIlist[0] = node;
        for (int i = 0; i < g.listOfNodes.length; i++) {
            if (g.listOfNodes[i].longitude < node.longitude) node = g.listOfNodes[i];
        }
        POIlist[1] = node;
        for (int i = 0; i < g.listOfNodes.length; i++) {
            if (g.listOfNodes[i].longitude > node.longitude) node = g.listOfNodes[i];
        }
        POIlist[2] = node;
        return POIlist;
    }
}
