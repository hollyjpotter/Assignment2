package Assignment2;

import java.util.ArrayList;

public class Tries {

    class TrieNode {
        String word;
        ArrayList<TrieNode> children = null;
        long freq;

        TrieNode(){
            word = null;
            freq = 0;
            children = new	ArrayList<TrieNode>();
            for (int i = 0; i < 26; i++) {
                children.add(null);
            }
        }
    }

    TrieNode root = new TrieNode();

    //inserts
    public void insert(String key, long tokens) {
        TrieNode current = root;

        for (char letter : key.toCharArray()) {
            if (current.children.get(letter - 'a') == null) {
                current.children.set(letter - 'a', new TrieNode());
            }
            current = current.children.get(letter - 'a');
        }
        current.word = key;
        current.freq = tokens;
    }

    //search
    public ArrayList<String> search(String type) {
        ArrayList<String> output = new ArrayList<String>();
        TrieNode current = root;
        for (char letter : type.toCharArray()) {
            if (current.children.get(letter - 'a') != null) { //get to the end of the input. Assuming the whole input exists.
                current = current.children.get(letter - 'a');
            }
        }
        output = DFS(current);
        return output;
    }
  
    private ArrayList<String> DFS(TrieNode node){
        ArrayList<String> output = new ArrayList<String>();
        int pos = 0;
        DFS(node, output, pos);

        return output;
    }
    private void DFS(TrieNode node, ArrayList<String> output, int pos) {
        for (int i = node.children.size()-1; i >=0; i--) {
            if (node.children.get(i) != null) {
                DFS(node.children.get(i), output, pos);
            }
        }
        if(node.word != null) {
            output.add(node.word);
        }
    }

    public TrieNode find(String type) {
        TrieNode current = root;
        for (char letter : type.toCharArray()) {
            if (current.children.get(letter - 'a') != null) {
                current = current.children.get(letter - 'a');
            }
        }
        return current;
    }
}
