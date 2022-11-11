//En node bør altså ha tre avstandsfelter: ett felt for avstand til start (som endrer seg underveis), et
//estimat for avstand til mål (som bare beregnes én gang for hver node som legges i kø), og en sum
//av de to første som prioritetskøen bruker for å finne beste node. Egentlig trengs ikke sum-feltet,
//man kan ha en aksessmetode som legger sammen ved behov.

public class Node {
    Edge firstEdge;
    Prev d;
    int nodeNum;
    double latitude;
    double longitude;
    String POI;
    int code = -1;
    boolean visited = false;

    public Node(int nodeNum, double latitude, double longitude) {
        this.nodeNum = nodeNum;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public void nodeFound(){
        this.visited = true;
    }

    @Override
    public String toString(){
        return latitude + "," + longitude;
    }
}
