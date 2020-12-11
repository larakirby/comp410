package MinBinHeap_A3;

public class MinBinHeap implements Heap_Interface {

	private EntryPair[] array; //load this array
	private int size;
	private static final int arraySize = 10000; //Everything in the array will initially 
	//be null. This is ok! Just build out 
	//from array[1]

	public MinBinHeap() {
		this.array = new EntryPair[arraySize];
		array[0] = new EntryPair(null, -100000); //0th will be unused for simplicity 
		//of child/parent computations...
		//the book/animation page both do this.
	}
	public int size() {
		return size;
	}

	public void insert(EntryPair entry) {
		int hole = ++size;
		if (entry.getPriority() < 0) {
			;
		}
		for (array[0] = entry; array[hole/2].getPriority() > entry.getPriority(); hole /= 2 ) {
			array[hole] = array[hole/2];
		}
		array[hole] = entry;
	}

	public void delMin() {
		array[1] = array[size];
		bubbleDown(1);
		array[size] = null;
		size--;
	}

	public EntryPair getMin() {
		return array[1]; //changed to 1
	}
	public void build(EntryPair[] entries) {
		for (int i = 1; i < entries.length+1; i++) {
			if (entries[i-1].getPriority() < 0){
				;
			}
			array[i] = entries[i-1];
			size++;
		}
		for (int i = size /2; i > 0; i--) {
			bubbleDown(i);
		}
	}

	//Please do not remove or modify this method! Used to test your entire Heap.
	@Override
	public EntryPair[] getHeap() { 
		return this.array;
	}




	public void bubbleDown(int i) {
		while(i*2< size) {
			int smallerChild = i*2;
			if(array[i*2+1].getPriority() < array[i*2].getPriority()) {
				smallerChild++;
			}
			if(array[i].getPriority() < array[smallerChild].getPriority()) {
				; 
			} else {
				EntryPair temp = array[i];
				array[i] = array[smallerChild];
				array[smallerChild] = temp;
			}
			i = smallerChild;
		}
	}


}
