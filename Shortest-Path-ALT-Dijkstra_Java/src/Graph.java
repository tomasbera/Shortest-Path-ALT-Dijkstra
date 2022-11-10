import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Graph {
    int vertices;
    int edges;
    Node []listOfNodes;
    Edge []listOfEdges;
    PriorityQueue<Node> pq;

    public Graph (File nodeFile, File edgeFile, File poiFile) throws IOException {
        readNodes(nodeFile);
        readEdges(edgeFile);
        readPointsOfInterest(poiFile);
    }

    public void readNodes(File nodeFile)throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nodeFile));

        StringTokenizer st = new StringTokenizer(br.readLine());
        this.vertices = Integer.parseInt(st.nextToken());
        listOfNodes = new Node[vertices];

        for (int i = 0; i < vertices; i++) {
            listOfNodes[i] = new Node();
        }

        for (int i = 0; i < vertices; i++) {
            st = new StringTokenizer(br.readLine());
            int nodeNumb = Integer.parseInt(st.nextToken());
            double lat = Double.parseDouble(st.nextToken());
            double lon = Double.parseDouble(st.nextToken());
            listOfNodes[nodeNumb].nodeNum= nodeNumb;
            listOfNodes[nodeNumb].latitude = lat;
            listOfNodes[nodeNumb].longitude = lon;
        }
    }

    public void readEdges(File edgeFile)throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(edgeFile));
        StringTokenizer st = new StringTokenizer(br.readLine());
        this.edges = Integer.parseInt(st.nextToken());
        listOfEdges = new Edge[edges];

        for (int i = 0; i < edges; i++) {
            st = new StringTokenizer(br.readLine());
            int fromNode = Integer.parseInt(st.nextToken());
            int toNode = Integer.parseInt(st.nextToken());
            int drivingTime = Integer.parseInt(st.nextToken());
            st.nextToken();
            st.nextToken();
            listOfNodes[fromNode].firstEdge = new Edge(listOfNodes[fromNode].firstEdge, listOfNodes[toNode], drivingTime);
        }
    }

    public void readEdgesRev(File edgeFile)throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(edgeFile));
        StringTokenizer st = new StringTokenizer(br.readLine());
        st.nextToken();

        for (int i = 0; i < edges; i++) {
            st = new StringTokenizer(br.readLine());
            int fromNode = Integer.parseInt(st.nextToken());
            int toNode = Integer.parseInt(st.nextToken());
            int drivingTime = Integer.parseInt(st.nextToken());
            st.nextToken();
            st.nextToken();
            listOfNodes[toNode].firstEdge = new Edge(listOfNodes[toNode].firstEdge, listOfNodes[fromNode], drivingTime);
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
            String placeName = String.valueOf(st.nextToken());
            listOfNodes[nodeNum].setCode(code);
            listOfNodes[nodeNum].setPOI(placeName);
        }
    }

    public ArrayList<Node> dijkstra(Node s, Node e){
        ArrayList<Node> visitedNodes = new ArrayList<>();
        e.visited = true;
        if (s != null){
            initPrev(s);
        }
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
        return visitedNodes;
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
        for (int i = vertices; i-->0;) {
            listOfNodes[i].d = new Prev();
        }
        (s.d).dist = 0;
    }

    public Node[] dijkstraPOI(Node s, int type){
        Node []listOfTypeNods = new Node[8];
        int count = 0;
        if (s != null){
            initPrev(s);
        }
        makePrio();
        pq.add(s);

        while (!this.pq.isEmpty()){
            Node n = pq.poll();
            if (n.code == type) {
                listOfTypeNods[count] = n;
                count++;
            }

            if (count==8)break;

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
}
