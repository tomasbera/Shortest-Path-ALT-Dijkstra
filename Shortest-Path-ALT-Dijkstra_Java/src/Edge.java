public class Edge {
    Edge nextEdge;
    Node to;
    int weight;

    public Edge(Edge next, Node to, int weight) {
        this.nextEdge = next;
        this.to = to;
        this.weight = weight;
    }
}
