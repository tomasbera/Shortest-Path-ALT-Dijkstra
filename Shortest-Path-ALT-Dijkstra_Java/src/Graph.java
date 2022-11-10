import java.io.*;
import java.util.*;

public class Graph {
    int nodeCount;
    int vertexes;
    Node []listOfNodes;
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

    public void makePrio(){
        pq = new PriorityQueue<Node>((a,b) -> (a.d.dist)-(b.d.dist));

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
        stringBuilder.append(preNodes.length);
        stringBuilder.append(" ");
        stringBuilder.append(nodeCount);
        stringBuilder.append("\n");

        for (int i = 0; i < preNodes.length; i++) {
            System.out.println(preNodes[i].nodeNum);
            stringBuilder.append(preNodes[i].nodeNum);
            dijkstraPrePos(preNodes[i]);
            for (int j = 0; j < nodeCount; j++) {
                stringBuilder.append(" ");
                stringBuilder.append(listOfNodes[i].d.dist);
            }
            stringBuilder.append("\n");
        }
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(stringBuilder.toString());
        bw.close();
    }

    public Node[] findThreePOI(){
        Random rnd = new Random();
        Node []POIList = new Node[3];
        POIList[0] = listOfNodes[rnd.nextInt(listOfNodes.length)];
        POIList[1] = listOfNodes[rnd.nextInt(listOfNodes.length)];
        POIList[2] = listOfNodes[rnd.nextInt(listOfNodes.length)];
        return POIList;
    }
}
