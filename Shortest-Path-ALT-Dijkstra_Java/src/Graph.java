import java.io.*;
import java.util.*;

public class Graph {
    int nodeCount;
    int vertexes;
    Node []listOfNodes;
    ArrayList<ArrayList<Integer>> fromList;
    ArrayList<ArrayList<Integer>> toList;
    HashMap<String, Integer> listOfLandmarks;
    PriorityQueue<Node> pq;

    public Graph (File nodeFile, File edgeFile, File poiFile) throws IOException {
        this.listOfLandmarks = new HashMap<>();
        readNodes(nodeFile);
        readEdges(edgeFile);
        readPointsOfInterest(poiFile);
    }

    public void readNodes(File nodeFile)throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nodeFile));
        StringTokenizer st = new StringTokenizer(br.readLine());
        this.nodeCount = Integer.parseInt(st.nextToken());
        listOfNodes = new Node[nodeCount];

        for (int i = 0; i < nodeCount; i++) {
            st = new StringTokenizer(br.readLine());
            int nodeNumb = Integer.parseInt(st.nextToken());
            double latitude = Double.parseDouble(st.nextToken());
            double longitude = Double.parseDouble(st.nextToken());
            listOfNodes[nodeNumb] = new Node(nodeNumb, latitude, longitude);
        }
    }

    public void readEdges(File edgeFile)throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(edgeFile));
        StringTokenizer st = new StringTokenizer(br.readLine());
        this.vertexes = Integer.parseInt(st.nextToken());

        for (int i = 0; i < vertexes; i++) {
            st = new StringTokenizer(br.readLine());
            int fromNode = Integer.parseInt(st.nextToken());
            int toNode = Integer.parseInt(st.nextToken());
            int drivingTime = Integer.parseInt(st.nextToken());
            st.nextToken();
            st.nextToken();
            listOfNodes[fromNode].firstEdge  =
                    new Edge(listOfNodes[toNode], listOfNodes[fromNode].firstEdge, drivingTime);
        }
    }

    public void readEdgesRev(File edgeFile)throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(edgeFile));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int revEdges = Integer.parseInt(st.nextToken());

        for (int i = 0; i < revEdges; i++) {
            st = new StringTokenizer(br.readLine());
            int fromNode = Integer.parseInt(st.nextToken());
            int toNode = Integer.parseInt(st.nextToken());
            int drivingTime = Integer.parseInt(st.nextToken());
            st.nextToken();
            st.nextToken();
            listOfNodes[toNode].firstEdge =
                    new Edge(listOfNodes[fromNode], listOfNodes[toNode].firstEdge, drivingTime);
        }
    }

    public void readPointsOfInterest(File poiFile)throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(poiFile));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numOfLocations = Integer.parseInt(st.nextToken());

        for (int i = 0; i < numOfLocations; i++) {
            st = new StringTokenizer(br.readLine());
            int nodeNum = Integer.parseInt(st.nextToken());
            int code = Integer.parseInt(st.nextToken());
            StringBuilder placeName = new StringBuilder(String.valueOf(st.nextToken()));
            while (st.hasMoreTokens()){
                placeName.append(" ").append(st.nextToken());
            }
            listOfNodes[nodeNum].setCode(code);
            listOfNodes[nodeNum].setPOI(placeName.toString());
            this.listOfLandmarks.put(placeName.toString(), nodeNum);
        }
    }

    public void makePrio(){
        pq = new PriorityQueue<Node>((a,b) -> (a.d.dist)-(b.d.dist));
    }

    public ArrayList<Node> dijkstra(Node s, Node e){
        ArrayList<Node> visitedNodes = new ArrayList<>();
        if (s != null){
            initPrev(s);
        }
        e.visited = true;
        makePrio();
        pq.add(s);

        while (!this.pq.isEmpty()){
            Node n = pq.poll();
            visitedNodes.add(n);

            if (n.visited) return visitedNodes;

            for (Edge we = n.firstEdge; we != null; we = we.nextEdge){
                shorten(n, we);
            }
        }
        return null;
    }

    public void initPrev(Node s){
        for (int i = nodeCount; i-->0;) {
            listOfNodes[i].d = new Prev();
        }
        (s.d).dist = 0;
    }

    public static  void runDijkstraPOI(Graph g, int s, int code){
        Node[] arr = g.dijkstraPOI(g.listOfNodes[s], code);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null){
                System.out.println(arr[i].POI + " "
                        + arr[i].nodeNum + " " + arr[i].code);
            }
        }
    }

    public Node[] dijkstraPOI(Node s, int type){
        Node []listOfTypeNods = new Node[8];
        if (s != null){
            initPrev(s);
        }
        makePrio();
        pq.add(s);
        int count = 0;

        while (!this.pq.isEmpty()){
            Node n = pq.poll();
            if ((n.code == type) || n.code == 6) {
                listOfTypeNods[count] = n;
                count++;
                n.visited = true;
            }

            if (count == 8)break;

            for (Edge we = n.firstEdge; we != null; we = we.nextEdge){
                shorten(n, we);
            }
        }
        return listOfTypeNods;
    }

    public void shorten(Node n, Edge we){
        Prev nd = n.d, md = we.to.d;
        if (md.dist > nd.dist + we.weight){
            md.dist = nd.dist + we.weight;
            md.prev = n;
            this.pq.add(we.to);
        }
    }

    public void dijkstraPrePos(Node s){
        if (s != null){
            initPrev(s);
        }
        makePrio();
        pq.add(s);

        while (!this.pq.isEmpty()){
            Node n = pq.poll();
            for (Edge we = n.firstEdge; we != null; we = we.nextEdge){
                shorten(n, we);
            }
        }
    }

    public Node[] findThreePOI(){
        Random rnd = new Random();
        Node []POIList = new Node[3];
        POIList[0] = listOfNodes[rnd.nextInt(listOfNodes.length)];
        POIList[1] = listOfNodes[rnd.nextInt(listOfNodes.length)];
        POIList[2] = listOfNodes[rnd.nextInt(listOfNodes.length)];
        return POIList;
    }

    public void runPreProses(File engFile) throws IOException {
        Node []preNodes;
        preNodes = findThreePOI();
        readEdgesRev(engFile);
        runToFile(preNodes, new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/PreTo.txt"));
        readEdges(engFile);
        runToFile(preNodes, new File("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/PreFrom.txt"));
    }

    public void runToFile(Node[] preNodes, File outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile.getAbsolutePath());
        StringBuilder stringBuilder = new StringBuilder();
        BufferedWriter bw = new BufferedWriter(fileWriter);
        stringBuilder.append(preNodes.length);
        stringBuilder.append(" ");
        stringBuilder.append(nodeCount);
        stringBuilder.append("\n");

        for (Node preNode : preNodes) {
            System.out.println(preNode.nodeNum);
            stringBuilder.append(preNode.nodeNum);
            dijkstraPrePos(preNode);
            for (int j = 0; j < nodeCount; j++) {
                stringBuilder.append(" ");
                stringBuilder.append(listOfNodes[j].d.dist);
            }
            stringBuilder.append("\n");
            bw = new BufferedWriter(fileWriter);
            bw.write(stringBuilder.toString());
        }
        bw.close();
    }

    public ArrayList<Node> runALT(Node s, Node e) throws IOException {
        BufferedReader brf = new BufferedReader(
                new FileReader("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/PreFrom.txt"));
        readFromToFile(brf);
        BufferedReader brt = new BufferedReader(
                new FileReader("Shortest-Path-ALT-Dijkstra/Shortest-Path-ALT-Dijkstra_Java/PreTo.txt"));
        readFromToFile(brt);

        estimatedDistanceLE();
        estimateDistanceNL();

        return ALT(s, e);
    }

    public void makePrioALT(){
        pq = new PriorityQueue<>((a,b) -> a.d.sumDist()-b.d.sumDist());
    }

    public void readFromToFile(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numbOfLandmarks = Integer.parseInt(st.nextToken());
        int numbOfNodes = Integer.parseInt(st.nextToken());
        fromList = new ArrayList<>(numbOfLandmarks);

        for (int i = 0; i < numbOfLandmarks; i++) {
            st = new StringTokenizer(br.readLine());
            ArrayList<Integer> ld = new ArrayList<>();
            ld.add(Integer.parseInt(st.nextToken()));
            for (int j = 0; j < numbOfNodes; j++) {
               ld.add(Integer.parseInt(st.nextToken()));
            }
            fromList.add(ld);
        }
    }

    public ArrayList<Node> ALT(Node s, Node e){
        ArrayList<Node> visitedNodes = new ArrayList<>();
        if (s != null){
            initPrev(s);
        }
        makePrioALT();
        distanceEstimate(s, e);
        e.visited = true;
        pq.add(s);

        while (!this.pq.isEmpty()){
            Node n = pq.poll();

            if (n.visited) return visitedNodes;

            visitedNodes.add(n);

            for (Edge we = n.firstEdge; we != null; we = we.nextEdge){
                shortenALT(n, we, e);
            }
        }
        return null;
    }

    public void shortenALT(Node n, Edge we, Node e){
        Prev nd = n.d, md = we.to.d;

        if (md.dist > nd.dist + we.weight){
            distanceEstimate(we.to, e);
            md.dist = nd.dist + we.weight;
            md.prev = n;
            this.pq.add(we.to);
        }
    }

    public void estimatedDistanceLE(){

    }

    public void estimateDistanceNL(){

    }

    public void distanceEstimate(Node start, Node end){

    }
}
