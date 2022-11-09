public class Node {
    Edge firstEdge;
    Prev d;
    int nodeNum;
    double latitude;
    double longitude;
    String POI;
    int code = -1;
    boolean visited = false;

    public Node(Edge firstEdge, Prev d, int nodeNum, double latitude, double longitude, String POI) {
        this.firstEdge = firstEdge;
        this.d = d;
        this.nodeNum = nodeNum;
        this.latitude = latitude;
        this.longitude = longitude;
        this.POI = POI;
    }

    public Node(){
        this.firstEdge = null;
        this.d = null;
        this.nodeNum = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.POI = "";
        this.visited = false;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setPOI(String POI) {
        this.POI = POI;
    }

    public void resetFound(){
        this.visited = false;
    }

    public void nodeFound(){
        this.visited = true;
    }

    public int compareTo(Node other){
        Prev prev = this.d;
        Prev newPrev = other.d;

        return prev.dist-newPrev.dist;
    }

    @Override
    public String toString(){
        return latitude + "," + longitude;
    }
}
