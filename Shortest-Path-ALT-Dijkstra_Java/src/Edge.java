public class Edge {
    Edge nextEdge;
    Node to;
    int weight;

    public Edge(Node to,Edge next, int weight) {
        this.to = to;
        this.nextEdge = next;
        this.weight = weight;
    }
}
