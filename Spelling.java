package Assignment2;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Spelling implements Trie{

    class TrieNode {
        
        private TrieNode[] nodes;
        //max number of options
        private final int S = 26;
        //for labelling end of word and frequency
        private boolean isEnd;
        private String frequency;

        public TrieNode() {
            nodes = new TrieNode[S];
        }

        public boolean containsKey(char ch) {
            return nodes[ch -'a'] != null;
        }
        public TrieNode get(char ch) {
            return nodes[ch -'a'];
        }
        public void put(char ch, TrieNode node) {
            nodes[ch -'a'] = node;
        }
        public void setEnd() {
            isEnd = true;
        }
        public boolean isEnd() {
            return isEnd;
        }
        public void setFrequency(String num) {
            frequency = num;
        }
        public String isFrequency() {
            return frequency;
        }
    }

    private TrieNode root;

    public Spelling() {
        root = new TrieNode();
    }

    @Override //inserts the word into the trie
    public void insert(String word, String frequency) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!node.containsKey(currentChar)) {
                node.put(currentChar, new TrieNode());
            }
            node = node.get(currentChar);
        }
        node.setEnd();
        node.setFrequency(frequency);
    }
    //look for prefix
    private TrieNode searchPrefix(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char curLetter = word.charAt(i);
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
            } else {
                return null;
            }
        }
        return node;
    }

    @Override //search word
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    @Override //check if word starts with a given prefix
    public boolean startsWith(String prefix) {
        TrieNode node = searchPrefix(prefix);
        return node != null;
    }
    //returns frequency
    public String getFrequency(String word) {
        TrieNode node = searchPrefix(word);
        return node.frequency;
    }
    //get the prefix
    public List<String> getWordPre(String prefix) {
        TrieNode node = root;
        List<String> res = new ArrayList<String>();
        for (char ch: prefix.toCharArray()) {
            node = node.nodes[ch];
            if (node == null)
                return new ArrayList<String>();
        }
        helper(node, res,  prefix.substring(0, prefix.length()-1));
        return res;
    }

    void helper(TrieNode node, List<String> res, String prefix) {
        if (node == null ) //base condition
            return;
        if (node.isEnd)
            res.add(prefix + node);
        for (TrieNode child: node.nodes)
            helper(child, res, prefix + node);
    }

    private String[] words = new String[333333];
    private long[] frequencies = new long[333333];
    Tries children;
    //for reading and inserting into the trie
    public Tries insertCSV() throws FileNotFoundException {
        Tries trie = new Tries();
        String splitBy = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader("//Users//holly//Downloads//frequencyF.csv"));
            br.readLine();
            String line1;
            while ((line1 = br.readLine()) != null) {
                String[] wordCounts = line1.split(splitBy);
                String word = wordCounts[0];
                Long num = Long.parseLong(wordCounts[1]);
                trie.insert(word, num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trie;
    }
    //The suggest method that returns words that start with the given prefixes
    public List<List<String>> suggest(String token, int count) throws Exception {
        List<List<String>> answer = new ArrayList<List<String>>();
        children = insertCSV();
        String prefix = "";
        List<String> suggested = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            suggested.add(null);
        }
        for (char letter : token.toCharArray()) {
            if (letter < 'a') {
                throw new Exception("Word must be lower case");
            }
            prefix += letter;
            ArrayList<String> p = children.search(prefix);
            String[] s = options(p, count);

            for (int i = 0; i < count; i++) {
                if (s[i] != null) {
                    if (suggested.contains(s[i]) != true) {
                        suggested.set(i, s[i]);
                    }
                }
            }
            List<String> temp = new ArrayList<String>(suggested);
            answer.add(temp);
        }
        return answer;
    }
    //helper for the sorting the word and frequency list
    public void sortWordFreq(String[] words, long[] frequencies) {
        for (int i = 1; i < frequencies.length; i++) {
            long temp = frequencies[i];
            String temp2 = words[i];
            int j = i - 1;
            while (j >= 0 && frequencies[j] < temp) {
                frequencies[j + 1] = frequencies[j];
                words[j + 1] = words[j];
                --j;
            }
            frequencies[j + 1] = temp;
            words[j + 1] = temp2;
        }
    }
    //find the most frequent
    public String[] findMost(int count, String[] wordOptions) {
        String[] mostOften = new String[count];
        if (wordOptions.length >= count) {
            for (int i = 0; i < count; i++) {
                mostOften[i] = wordOptions[i];
            }
        } else {
            for (int i = 0; i < wordOptions.length; i++) {
                mostOften[i] = wordOptions[i];
            }
        }
        return mostOften;
    }
    //provide the options for the most frequent using findMost
    private String[] options(ArrayList<String> options, int count) {
        String[] wordOptions = new String[options.size()];
        options.toArray(wordOptions);
        long[] freqs = new long[wordOptions.length];
        int freq = 0;

        for (String w : wordOptions) {
            Tries.TrieNode theWord = children.find(w);
            freqs[freq++] = theWord.freq;
        }
        sortWordFreq(wordOptions, freqs);
        String[] answer = findMost(count, wordOptions);
        return answer;
    }

    public static void main(String[] args) throws Exception {
        Spelling spell = new Spelling();
        List<List<String>> list = new ArrayList<List<String>>();

        list = spell.suggest("apple", 5);

        for (List<String> l : list) {
            System.out.println(l);
        }
    }
}
