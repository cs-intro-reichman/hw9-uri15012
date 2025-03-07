import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */
	public int malloc(int length) {		
		if (length <= 0) {
			return -1;
		}
	
		ListIterator it = freeList.iterator(); // Create an iterator for the free list.
	
		while (it.hasNext()) {
			MemoryBlock freeBlock = it.next(); // Get the next free memory block.
	
			if (freeBlock.length >= length) {
				int baseAddress = freeBlock.baseAddress; // Store the base address to return.
	
				// Case 1: Exact match, remove the block from the free list.
				if (freeBlock.length == length) {
					freeList.remove(freeBlock); 
				}
				// Case 2: Free block is larger, modify its properties.
				else {
					freeBlock.baseAddress += length; // Move the base address forward.
					freeBlock.length -= length; // Reduce the size of the free block.
				}
	
				// Create a new allocated block and add it to the allocated list.
				MemoryBlock allocatedBlock = new MemoryBlock(baseAddress, length);
				allocatedList.addLast(allocatedBlock);
	
				return baseAddress; // Return the base address of the allocated block.
			}
		}
	
		return -1; // No suitable block found.
	}

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	public void free(int address) {
		ListIterator it = allocatedList.iterator(); // Create an iterator for allocated blocks
		if (it.current == null) {
			throw new IllegalArgumentException( "index must be between 0 and size");
		}
		
	
		while (it.hasNext()) {
			MemoryBlock allocatedBlock = it.next();
	
			// If a block with the given base address is found
			if (allocatedBlock.baseAddress == address) {
				allocatedList.remove(allocatedBlock); // Remove from allocated list
				freeList.addLast(allocatedBlock); // Add to the end of free list
				return; // Exit after freeing the memory block
			}
		}
	}
	
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		if (freeList.getSize() <= 1) {
			return; // No need to defragment if there are 0 or 1 blocks
		}
	
		// Extract and copy all memory blocks into a temporary array for processing
		ListIterator it = freeList.iterator();
		MemoryBlock[] blocks = new MemoryBlock[freeList.getSize()];
		int i = 0;
	
		while (it.hasNext()) {
			MemoryBlock block = it.next();
			blocks[i++] = new MemoryBlock(block.baseAddress, block.length); // Create fresh copies
		}
	
		// Sort the array of blocks by base address
		Arrays.sort(blocks, Comparator.comparingInt(block -> block.baseAddress));
	
		// Clear the original freeList to rebuild it
		freeList = new LinkedList();
	
		// Traverse the sorted blocks and merge adjacent ones
		MemoryBlock current = blocks[0];
		for (i = 1; i < blocks.length; i++) {
			MemoryBlock next = blocks[i];
	
			// If adjacent, merge the blocks
			if (current.baseAddress + current.length == next.baseAddress) {
				current.length += next.length; // Extend the current block
			} else {
				// Add the non-adjacent block to the freeList
				freeList.addLast(current);
				current = next; // Move to the next block
			}
		}
	
		// Add the last merged or standalone block to the freeList
		freeList.addLast(current);
	}

	/**
 * Sorts a LinkedList of MemoryBlock objects by base address.
 * 
 * @param list the list to sort
 */
private void sortListByBaseAddress(LinkedList list) {
    if (list.getSize() <= 1) {
        return; // No need to sort if list has 0 or 1 element
    }

    MemoryBlock[] blocks = new MemoryBlock[list.getSize()];
    ListIterator it = list.iterator();
    int i = 0;

    while (it.hasNext()) {
        blocks[i++] = it.next();
    }

    // Sort the blocks array by base address
    Arrays.sort(blocks, Comparator.comparingInt(block -> block.baseAddress));

    // Clear and rebuild the list in sorted order
    list = new LinkedList(); // Reset the list
    for (MemoryBlock block : blocks) {
        list.addLast(block);
    }
}
}
