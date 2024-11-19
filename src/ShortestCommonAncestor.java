import dsa.BFSDiPaths;
import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class ShortestCommonAncestor {
    private DiGraph G;

    // Constructs a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        this.G = G;
    }

    // Returns length of the shortest ancestral path between vertices v and w.
    public int length(int v, int w) {
        int a = ancestor(v, w);
        return distFrom(v).get(a) + distFrom(w).get(a);
    }

    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        int a = Integer.MAX_VALUE;
        int b = -1;
        for(int i : distFrom(v).keys()){
            for(int j : distFrom(w).keys()){
                if(j == i){
                    int temp = distFrom(v).get(i) + distFrom(w).get(j);
                    if (a > temp) {
                        a = temp;
                        b = j;
                    }
                }
            }
        }
        return b;
    }

    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        int[] a = triad(A, B);
        int b = a[0];
        return distFrom(a[1]).get(b) + distFrom(a[2]).get(b);
    }

    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        int[] a = triad(A, B);
        return a[0];
    }

    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        BFSDiPaths p = new BFSDiPaths(G, v);
        SeparateChainingHashST<Integer, Integer> st = new SeparateChainingHashST<>();
        for (int i = 0; i < G.V(); i++){
            if (p.hasPathTo(i)){
                st.put(i, (int)p.distTo(i));
            }
        }
        return st;
    }

    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        int[] a = new int[3];
        int l = Integer.MAX_VALUE;
        for(int x : A){
            for(int y : B){
                int len = length(x, y);
                if(len < l){
                    l = len;
                    a[0] = ancestor(x, y);
                    a[1] = x;
                    a[2] = y;
                }

            }
        }
        return a;
    }

    // Unit tests the data type.
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

