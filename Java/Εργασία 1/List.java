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
		// Creation of a new list
		List result = new List();
		Node currentNode = this.head.next;
	
		while (currentNode != this.tail) {
			Node otherNode = list.head.next;
			boolean found = false;

			// Check if the current node exists in the other list
			while (otherNode != list.tail) {
				if (currentNode.key() == otherNode.key()) {
					found = true;
					break;
				}
				otherNode = otherNode.next;
			}
			// If the element exists in both lists, add it to the result list
			if (found) {
				result.insertFirst(currentNode.key());
			}
	
			currentNode = currentNode.next;
		}
	
		return result;
	}

	public List mergeWith(List list) {
		// Creation of a new list
		List mergedList = new List();
		// Add elements from the current list to the merged list
		Node currentNodeThis = this.head.next;
		while (currentNodeThis != this.tail) {
			mergedList.insertFirst(currentNodeThis.key());
			currentNodeThis = currentNodeThis.next;
		}
	
		// Add elements from the other list to the merged list
		Node currentNodeList = list.head.next;
		while (currentNodeList != list.tail) {
			mergedList.insertFirst(currentNodeList.key());
			currentNodeList = currentNodeList.next;
		}
	
		// Sort the merged list using sort
		insertionSort(mergedList);
	
		return mergedList;
	}

	public List largest(int k) {
		// Creation of a new list
		List newList = new List();
		Node currentNode = this.head.next;
		// Copy elements from the current list
		int x = 0;
		while (currentNode != this.tail) {
			newList.insertFirst(currentNode.key());
			currentNode = currentNode.next;
			x++;
		}
	
		// Sort the new list
		insertionSort(newList);

		// Create a new list containing the first k keys from the sorted list
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
		Node sortedEnd = list.head.next; // End of the sorted part
	
		while (current != list.tail) {
			Node next = current.next; // Store the next node before potentially detaching current
	
			if (current.key() < sortedEnd.key()) {
				// Remove current from the list
				current.prev.next = current.next;
				current.next.prev = current.prev;
	
				// Find the correct position to insert current in the sorted part
				Node prevNode = list.head;
				while (prevNode.next != list.tail && prevNode.next.key() < current.key()) {
					prevNode = prevNode.next;
				}
	
				// Insert current after prevNode
				current.next = prevNode.next;
				current.prev = prevNode;
				prevNode.next.prev = current;
				prevNode.next = current;
			} else {
				// Update the end of the sorted part
				sortedEnd = current;
			}
	
			current = next;
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