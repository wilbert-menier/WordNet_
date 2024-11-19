import dsa.*;
import stdlib.In;
import stdlib.StdOut;

public class WordNet {
    private RedBlackBinarySearchTreeST<String, Set<Integer>> st;
    private RedBlackBinarySearchTreeST<Integer, String> rst;
    private ShortestCommonAncestor sca;

    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        st = new RedBlackBinarySearchTreeST<>();
        rst = new RedBlackBinarySearchTreeST<>();
        In in = new In(synsets);
        StringBuilder b = new StringBuilder();
        StringBuilder c = new StringBuilder();
        int i = 0, j = 1;

        while(!in.isEmpty()){
            char a = in.readChar();
            if (b.isEmpty() && j == 1){
                if (a == ',')
                    j = 0;
            } else if(j == 0) {
                if(a != ',')
                    c.append(a);
                if (a != ' ' && a != ',') {
                    b.append(a);
                } else if (!st.contains(b.toString()) && (!b.isEmpty())) {
                    st.put(b.toString(), new Set<>());
                    Set<Integer> t = st.get(b.toString());
                    t.add(i);
                    b = new StringBuilder();
                } else if (!b.isEmpty()) {
                    Set<Integer> s = st.get(b.toString());
                    s.add(i);
                    b = new StringBuilder();
                }
                if (a == ','){
                    rst.put(i, c.toString());
                    c = new StringBuilder();
                    in.readLine();
                    i++;
                    j = 1;

                }
            }

        }

        in = new In(hypernyms);
        DiGraph G = new DiGraph(rst.size());
        while(!in.isEmpty()){
            String[] a = in.readLine().split(",");
            for(i = 1; i < a.length; i++){
                G.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i]));
            }
        }

        sca = new ShortestCommonAncestor(G);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    public boolean isNoun(String word) {
        return st.contains(word);
    }

    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        int a = sca.ancestor(st.get(noun1), st.get(noun2));
        return rst.get(a);
    }

    // Returns the length of the shortest ancestral path between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        return sca.length(st.get(noun1), st.get(noun2));
    }

    // Unit tests the data type.
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}
