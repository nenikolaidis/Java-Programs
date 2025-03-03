import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class Node {
	public String word;
	public double dist;

	public Node(String word, double dist) { 
		this.word = word;
		this.dist = dist;
	}
}

public class HashedHeaps {
	private Node[][] words;	// the hashtable
	private int m;			// size of hashtable
	private int itemsInHash;// number of hashtable entries 
	private int k;			// size of heap
	private int[] itemsInHeap; // number of items in each Heap

	// sz1: Hashtable size, sz2: Heap size
	public HashedHeaps(int sz1, int sz2) {
		m = sz1;
		k = sz2;
		words = new Node[m][k];
		itemsInHash = 0;
		itemsInHeap = new int[m];
	}

	// Parses the file: sFile line-by-line, and makes 2 calls to method: insert() for each line 
	// The format of each line is: word1,word2,dist
	public void load(String sFile) {
		String sLine = "", w1 = "", w2 = "";
		double dist;
		try {
			BufferedReader br = new BufferedReader(new FileReader(sFile));
			while ((sLine=br.readLine()) != null) {
				sLine = sLine.trim();
				String[] arLine = sLine.split(",");
				w1 = arLine[0];
				w2 = arLine[1];
				dist = Double.parseDouble(arLine[2]);
				insert(w1, w2, dist);
				insert(w2, w1, dist);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	// Return the hash value [0...m) for a given word: w
	private int hashFunc(String w) {
		int key = Math.abs(w.hashCode());
		int hash = key % this.m;
		return hash;
	}
	
	// Inserts the entry: (w1, w2, dist) to the data structure.
	// If: w1 does not exist, insert it in the hashtable and create heap with two nodes: 
	//		(w1, MIN_VALUE) and (w2, dist)
	// If: w1 exists, just add (w2, dist) to the heap
	public void insert(String w1, String w2, double dist) {	
		int hash = hashFunc(w1);

		// Check if w1 already exists in the hashtable
		boolean w1Exists = false;
		int index = -1;
		for (int i = 0; i < itemsInHash && i < words.length; i++) {
			if (words[i][0] != null && words[i][0].word.equals(w1)) {
				w1Exists = true;
				index = i;
				break;
			}
		}
	
		if (w1Exists) {
			// w1 exists in the hashtable, add (w2, dist) to the corresponding heap
			Node[] heap = words[index];
			int heapSize = itemsInHeap[index];
	
			// Ensure there is space in the heap
			if (heapSize < k) {
				heap[heapSize] = new Node(w2, dist);
				itemsInHeap[index]++;
			}
		
		} else {
			// w1 does not exist in the hashtable, insert a new entry
			Node[] newHeap = new Node[k];
			newHeap[0] = new Node(w1, -1);  // Node(w1, -1) at the root
			newHeap[1] = new Node(w2, dist);  // Node(w2, dist)
	
			// Add the new entry to the hashtable using the hash value
			words[hash] = newHeap;
			itemsInHeap[hash] = 2;  // Two nodes in the heap
			itemsInHash++;
		}
	}

	// Retrieves the most similar word to: w
	public String findMostSimilarWord(String w) {
		int hash = hashFunc(w);

		if (hash < 0 || hash >= m) {
			return null; // Invalid hash value
		}
		// Retrieve the heap and its size corresponding to the input word
		Node[] heap = words[hash];
		int heapSize = itemsInHeap[hash];

		if (heapSize == 0) {
			return null; 
		}

		// Find the node with the minimum distance to the input word
		Node minDistNode = heap[0]; 

		for (int i = 1; i < heapSize; i++) {
			if (heap[i].dist < minDistNode.dist) {
				minDistNode = heap[i];
			}
		}

		return minDistNode.word;
	}
	
	// Removes the most similar word to: w
	public String removeMostSimilarWord(String w) {
		int hash = hashFunc(w);

		if (hash < 0 || hash >= m) {
			return null; 
		}

		Node[] heap = words[hash];
		int heapSize = itemsInHeap[hash];

		if (heapSize == 0) {
			return null; 
		}

		// Find the index of the node with the minimum distance to the input word
		int minDistIndex = 0; 

		for (int i = 1; i < heapSize; i++) {
			if (heap[i].dist < heap[minDistIndex].dist) {
				minDistIndex = i;
			}
		}

		// Extract the word with the minimum distance
		String removedWord = heap[minDistIndex].word;

		// Remove the node from the heap by shifting elements
		for (int i = minDistIndex; i < heapSize - 1; i++) {
			heap[i] = heap[i + 1];
		}

		
		itemsInHeap[hash]--;

		return removedWord;
	}

	// Returns true if w1 contains w2 in the top-n words with minimum distance, or if w2 contains w1 in the top-n words with minimum distance
	public boolean haveCommonSimilarWord(String w1, String w2, int n) {
		int hash1 = hashFunc(w1);
		int hash2 = hashFunc(w2);

		if (hash1 < 0 || hash1 >= m || hash2 < 0 || hash2 >= m) {
			return false;
		}

		Node[] heap1 = words[hash1];
		Node[] heap2 = words[hash2];

		int size1 = itemsInHeap[hash1];
		int size2 = itemsInHeap[hash2];

		// Check if one or both heaps are empty
		if (size1 == 0 || size2 == 0) {
			return false;
		}

		// Check if w2 is in the n most similar words for w1
		for (int i = 0; i < Math.min(n, size1); i++) {
			if (heap1[i].word.equals(w2)) {
				return true;
			}
		}

		// Check if w1 is in the n most similar words for w2
		for (int i = 0; i < Math.min(n, size2); i++) {
			if (heap2[i].word.equals(w1)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) {
		// Create an instance of HashedHeaps
        HashedHeaps h = new HashedHeaps(1000, 1000);

        // Load data from the file located in the same directory as the Java source files
        String filePath = HashedHeaps.class.getResource("data.txt").getPath();
        h.load(filePath);

        // Print the total number of loaded entries
        System.out.println("Total Loaded Entries: " + h.itemsInHash);
		
		// Test findMostSimilarWord
		String wordToFindSimilarityFor = "the";
		String mostSimilarWord = h.findMostSimilarWord(wordToFindSimilarityFor);
		System.out.println("Most Similar Word to '" + wordToFindSimilarityFor + "': " + mostSimilarWord);
	
		// Test removeMostSimilarWord
		String wordToRemoveSimilarityFor = "the";
		String removedWord = h.removeMostSimilarWord(wordToRemoveSimilarityFor);
		System.out.println("Removed Most Similar Word to '" + wordToRemoveSimilarityFor + "': " + removedWord);
	
		// Test haveCommonSimilarWord
		String word1 = "of";
		String word2 = "and";
		int n = 5;
		boolean haveCommonSimilarWord = h.haveCommonSimilarWord(word1, word2, n);
		System.out.println("Do '" + word1 + "' and '" + word2 + "' have a common similar word within the top " + n + "? " + haveCommonSimilarWord);
		
	}
	
	

}