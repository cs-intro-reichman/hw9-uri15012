/**
 * Represents a list of Nodes. 
 */
public class LinkedList {
	
	private Node first; // pointer to the first element of this list
	private Node last;  // pointer to the last element of this list
	private int size;   // number of elements in this list
	
	/**
	 * Constructs a new list.
	 */ 
	public LinkedList () {
		first = null;
		last = first;
		size = 0;
	}
	
	/**
	 * Gets the first node of the list
	 * @return The first node of the list.
	 */		
	public Node getFirst() {
		return this.first;
	}

	/**
	 * Gets the last node of the list
	 * @return The last node of the list.
	 */		
	public Node getLast() {
		return this.last;
	}
	
	/**
	 * Gets the current size of the list
	 * @return The size of the list.
	 */		
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the node located at the given index in this list. 
	 * 
	 * @param index
	 *        the index of the node to retrieve, between 0 and size
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 * @return the node at the given index
	 */		
	public Node getNode(int index) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}

		Node current = first;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}

		return current;
	}
	
	/**
	 * Creates a new Node object that points to the given memory block, 
	 * and inserts the node at the given index in this list.
	 * <p>
	 * If the given index is 0, the new node becomes the first node in this list.
	 * <p>
	 * If the given index equals the list's size, the new node becomes the last 
	 * node in this list.
     * <p>
	 * The method implementation is optimized, as follows: if the given 
	 * index is either 0 or the list's size, the addition time is O(1). 
	 * 
	 * @param block
	 *        the memory block to be inserted into the list
	 * @param index
	 *        the index before which the memory block should be inserted
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 */
	public void add(int index, MemoryBlock block) {
		if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size (inclusive)");
		}

		Node newNode = new Node(block);

		if (index == 0) {
			newNode.next = first;
			first = newNode;
			if (size == 0) {
				last = newNode; // Update last if the list was previously empty
			}
		} else if (index == size) {
			// Insert at the end of the list
			last.next = newNode;
			last = newNode;
		} else {
			// Insert at the middle of the list
			Node prev = getNode(index - 1); // Get the node just before the insertion point
			newNode.next = prev.next;
			prev.next = newNode;
		}

		size++; // Increment the size of the list
	}

	/**
	 * Creates a new node that points to the given memory block, and adds it
	 * to the end of this list (the node will become the list's last element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addLast(MemoryBlock block) {
		Node newNode = new Node(block);
	
		if (size == 0) {
			// Case 1: List is empty
			first = newNode; // First node becomes the new node
			last = newNode;  // Last node also points to the new node
		} else {
			// Case 2: List is not empty
			last.next = newNode; // Link the current last node to the new node
			last = newNode;      // Update last to point to the new node
		}
	
		size++; // Increment the size of the list
	}
	
	/**
	 * Creates a new node that points to the given memory block, and adds it 
	 * to the beginning of this list (the node will become the list's first element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addFirst(MemoryBlock block) {
		Node newNode = new Node(block);
		newNode.next = first;
			first = newNode;
			if (size == 0) {
				last = newNode; // Update last if the list was previously empty
			}
			size++;
	}

	/**
	 * Gets the memory block located at the given index in this list.
	 * 
	 * @param index
	 *        the index of the retrieved memory block
	 * @return the memory block at the given index
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public MemoryBlock getBlock(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		Node current = first; 
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
	
		return current.block;
	}	

	/**
	 * Gets the index of the node pointing to the given memory block.
	 * 
	 * @param block
	 *        the given memory block
	 * @return the index of the block, or -1 if the block is not in this list
	 */
	public int indexOf(MemoryBlock block) {
		Node current = first; // Start from the head of the list
		int index = 0;
	
		while (current != null) {
			if (current.block.equals(block)) { // Compare memory blocks
				return index; // Return index if found
			}
			current = current.next; // Move to the next node
			index++;
		}
	
		return -1; // Block not found in the list
	}

	/**
	 * Removes the given node from this list.	
	 * 
	 * @param node
	 *        the node that will be removed from this list
	 */
	public void remove(Node node) {
		if (node == null) {
			throw new NullPointerException("ERROR NullPointerException!");
		}
		if (first == null) {
			return; // Nothing to remove if the list is empty
		}
	
		// Case 1: Removing the first node
		if (node == first) {
			first = first.next; // Move head to the next node
			if (first == null) { 
				last = null; // If the list becomes empty, update last
			}
			size--;
			return;
		}
	
		// Traverse the list to find the node and its previous node
		Node current = first;
		Node previous = null;
	
		while (current != null && current != node) {
			previous = current;
			current = current.next;
		}
	
		// If the node is not found in the list, do nothing
		if (current == null) {
			return; // Node to be removed doesn't exist in the list
		}
	
		// If node is found, remove it
		previous.next = current.next; // Skip the node to remove it
		if (current == last) { 
			last = previous; // If last node is removed, update last
		}
		size--; // Decrement size after removal
	}

	/**
	 * Removes from this list the node which is located at the given index.
	 * 
	 * @param index the location of the node that has to be removed.
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
	
		// Case 1: Removing the first node
		if (index == 0) {
			first = first.next; // Move head to the next node
			if (first == null) { 
				last = null; // If the list becomes empty, update last
			}
			size--;
			return;
		}
	
		// Traverse the list to find the node at index
		Node current = first;
		Node previous = null;
	
		for (int i = 0; i < index; i++) {
			previous = current;
			current = current.next;
		}
	
		// Case 2: Removing a middle or last node
		previous.next = current.next; // Skip the node
	
		if (current == last) { 
			last = previous; // If last node is removed, update last
		}
	
		size--;
	}

	/**
	 * Removes from this list the node pointing to the given memory block.
	 * 
	 * @param block the memory block that should be removed from the list
	 * @throws IllegalArgumentException
	 *         if the given memory block is not in this list
	 */
	public void remove(MemoryBlock block) {
		if (first == null || block == null) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
	
		// Case 1: Removing the first node
		if (first.block.equals(block)) {
			first = first.next; // Move head to the next node
			if (first == null) { 
				last = null; // If list becomes empty, update last
			}
			size--;
			return;
		}
	
		// Traverse the list to find the node pointing to the given memory block
		Node current = first;
		Node previous = null;
	
		while (current != null && !current.block.equals(block)) {
			previous = current;
			current = current.next;
		}
	
		// If block was not found, throw an exception
		if (current == null) {
			throw new IllegalArgumentException("index must be between 0 and size");
		}
	
		
		previous.next = current.next; 
		if (current == last) { 
			last = previous; 
		}
	
		size--;
	}

	/**
	 * Returns an iterator over this list, starting with the first element.
	 */
	public ListIterator iterator(){
		return new ListIterator(first);
	}
	
	/**
	 * A textual representation of this list, for debugging.
	 */
	public String toString() {

		String s = "";
		Node current = first;
		while (current != null) {
		s = s + current.block + " ";
		current = current.next;
		}
		return s;
		}
}