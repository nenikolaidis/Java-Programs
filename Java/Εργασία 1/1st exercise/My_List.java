class Node {
	public Node next;
	public Node prev;
	private int key;

	public Node(int key) {
		this.key = key;
	}

	public int key() {
		return this.key;
	}

	public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

	public void print(String pre, String post) {
		System.out.print(pre + this.key + post);
	}
}

class List {
	private Node head;
	private Node tail;

	public List() {
		head = new Node(0);
		tail = new Node(0);
		head.next = tail;
		tail.prev = head;
	}

	public void insertFirst(int key) {
		Node tmp = new Node(key);

		tmp.prev = head;
		tmp.next = head.next;

		head.next = tmp;
		tmp.next.prev = tmp;
	}

	public List copy() {
		List list = new List();
		Node node = this.tail.prev;
		while(node != this.head) {
			list.insertFirst(node.key());
			node = node.prev;
		}
		return list;
	}

	public void print() {
		Node current = this.head.next;
		System.out.print("[");
		while(current != this.tail) {
		       	current.print(" ", ",");
			current = current.next;
		}
		System.out.println(" ]");
	}

	public List filter(List list) {
		List result = new List();  
		Node currentNode = this.head.next;
	
		while (currentNode != this.tail) {
			Node otherNode = list.head.next;
			boolean found = false;
	
			while (otherNode != list.tail) {
				if (currentNode.key() == otherNode.key()) {
					found = true;
					break;
				}
				otherNode = otherNode.next;
			}
	
			if (found) {
				result.insertFirst(currentNode.key());
			}
	
			currentNode = currentNode.next;
		}
	
		return result;
	}

	public List mergeWith(List list) {
		List mergedList = new List();
	
		Node currentNodeThis = this.head.next;
		while (currentNodeThis != this.tail) {
			mergedList.insertFirst(currentNodeThis.key());
			currentNodeThis = currentNodeThis.next;
		}
	
		// Add the elements of the "list" to the new list
		Node currentNodeList = list.head.next;
		while (currentNodeList != list.tail) {
			mergedList.insertFirst(currentNodeList.key());
			currentNodeList = currentNodeList.next;
		}
	
		// Use insertion sort to sort the merged list in ascending order
		insertionSort(mergedList);
	
		return mergedList;
	}
	
	public List largest(int k) {
		List newList = new List();
		Node currentNode = this.head.next;
		
		int x = 0;
		while (currentNode != this.tail) {
			newList.insertFirst(currentNode.key());
			currentNode = currentNode.next;
			x++;
		}
	
		// Use insertion sort to sort the new list in ascending order
		insertionSort(newList);

		// Create a new list containing the first k keys
		List result = new List();
		Node currentNewList = newList.head.next;
		int count = 0;
		
		while (currentNewList != newList.tail) {
			if (count >= (x - k)) {
				result.insertFirst(currentNewList.key());
				
			}
			currentNewList = currentNewList.next;
			count++;
		}
		
		return result;
	}

	public static void insertionSort(List list) {
		Node current = list.head.next; // Start with the second node
		while (current != list.tail) {
			int keyToInsert = current.getKey();
			Node prevNode = current.prev;
	
			// Move elements of list[0..i-1] that are greater than keyToInsert
			while (prevNode != list.head && prevNode.getKey() > keyToInsert) {
				prevNode.next.setKey(prevNode.getKey()); // Shift the greater key to the right
				prevNode = prevNode.prev;
			}
	
			// Insert the keyToInsert in its correct position
			prevNode.next.setKey(keyToInsert);
	
			current = current.next;
		}
	}
 

	public static void main(String[] args) {

		// Create a sample List
        List list1 = new List();
        list1.insertFirst(5);
        list1.insertFirst(3);
        list1.insertFirst(8);
        list1.insertFirst(1);
        list1.insertFirst(6);
        list1.insertFirst(2);
        
        // Create another List
        List list2 = new List();
        list2.insertFirst(4);
        list2.insertFirst(7);
        list2.insertFirst(2);
        list2.insertFirst(3);
        
        System.out.println("Original List 1:");
        list1.print();
	
        System.out.println("Original List 2:");
        list2.print();
        
        // Test the filter method
        List filteredList = list1.filter(list2);
        System.out.println("Filtered List (common keys):");
        filteredList.print();
        
        // Test the mergeWith method
        List mergedList = list1.mergeWith(list2);
        System.out.println("Merged List:");
        mergedList.print();
        
		// Test the largest method
		int k = 3; // Number of largest keys to retrieve
		List largestList = list1.largest(k);
		System.out.println("Largest " + k + " keys in List 1:");
		largestList.print();

		// Test the sort method
		List listToSort = new List();
		listToSort.insertFirst(5);
		listToSort.insertFirst(3);
		listToSort.insertFirst(8);
		listToSort.insertFirst(1);
		listToSort.insertFirst(6);
		listToSort.insertFirst(2);
		System.out.println("Original List to Sort (unsorted):");
		listToSort.print();
	
		// Sort the list using Insertion Sort
		List.insertionSort(listToSort);
		System.out.println("Sorted List (ascending order):");
		listToSort.print();
	}
}