public class Prev {
    int dist;
    int estimatedDistEnd;
    Node prev;
    static int inf = 1000000000;

    public Prev(){
        dist = inf;
    }

    public int sumDist(){return dist + estimatedDistEnd;}
}
