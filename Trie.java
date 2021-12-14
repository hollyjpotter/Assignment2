package Assignment2;

public interface Trie {

    public void insert(String word, String frequency);

    public boolean search(String word);

    public boolean startsWith(String prefix);

}
