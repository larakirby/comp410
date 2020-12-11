package DiGraph_A5;

import java.util.HashMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class DiGraph implements DiGraphInterface {
	private HashMap<String, Node> adjlist; // adjacency list 
	private HashSet<Long> nodeid;	//keeping a list of our nodes but separately
	private HashSet<Long> edgeid;

	private int nsize;
	private int esize;
	//java limit number-- the laragest we can have
	public final static int limit = 2147483647;

	//initializing a comparator to help us decide between nodes!
	class CompareNodeDistance implements Comparator<Node>{
		public int compare(Node node1, Node node2) {
			if (node1.distance < node2.distance) {
				return -1;
			} else if (node1.distance > node2.distance) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	//node class: to hold all our node info and so it's easy to put in the hashmap
	class Node {
		private HashMap<String, Edge> incoming;
		private HashMap<String, Edge> outgoing;
		private String label;
		private long id;
		private long distance;
		boolean wasHere;

		public Node (long id, String label) {
			//hash set of edges, for which the edge object is a parameter
			this.id = id;
			this.label = label;
			this.incoming = new HashMap<String, Edge>();
			this.outgoing = new HashMap<String, Edge>();
		}
	}
	
	class Edge {
		private long idNum;
		private String sLabel;
		private String dLabel;
		private long weight;
		private String eLabel;

		public Edge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
			this.idNum = idNum;
			this.sLabel = sLabel; 
			this.dLabel = dLabel;
			this.weight = weight;
			this.eLabel = eLabel;
		}
		
		public Edge(long idNum, String sLabel, String dLabel, String eLabel) {
			this.idNum = idNum;
			this.sLabel = sLabel;
			this.dLabel = dLabel;
			this.weight = 1;
			this.eLabel = eLabel;
		}
		
		public Edge(long idNum, String sLabel, String dLabel, long weight) {
			this.idNum = idNum;
			this.sLabel = sLabel;
			this.dLabel = dLabel;
			this.weight = weight;
			this.eLabel = null;
		}
		
		public Edge(long idNum, String sLabel, String dLabel) {
			this.idNum = idNum;
			this.sLabel = sLabel;
			this.dLabel = dLabel;
			this.weight = 1;
			this.eLabel = null;
		}
	}

	public DiGraph ( ) { 
		this.esize = 0;
		this.nsize = 0;
		this.adjlist = new HashMap<String, Node>();
		this.nodeid = new HashSet<Long>();
		this.edgeid = new HashSet<Long>();
	}

	@Override
	public boolean addNode(long idNum, String label) {
		if (idNum < 0 || adjlist.containsKey(label) || nodeid.contains(idNum) || label == null) { //???
			return false;
		}
		else {
			Node node = new Node(idNum, label);
			adjlist.put(label, node);
			nodeid.add(idNum);
			nsize ++;
			return true;
		}
	}

	@Override
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		if (edgeid.contains(idNum)) {
			return false;
		}
		if (idNum < 0 || !adjlist.containsKey(sLabel) || !adjlist.containsKey(dLabel) || edgeid.contains(idNum)) { 
			return false;
		}
		if (adjlist.get(sLabel).outgoing.containsKey(dLabel) || adjlist.get(dLabel).incoming.containsKey(sLabel)) {
			return false;
		}
		else {
			//create an edge, isolate slabel & add to out edge, increment
			edgeid.add(idNum);
			Edge edge = new Edge(idNum, sLabel, dLabel, weight, eLabel);
			adjlist.get(sLabel).outgoing.put(dLabel, edge);
			adjlist.get(dLabel).incoming.put(sLabel, edge);
			esize ++; 
			return true;
		}
	}

	public boolean addEdge(long idNum, String sLabel, String dLabel) {
		return addEdge(idNum, sLabel, dLabel, 1, null) ;
	}

	@Override
	public boolean delNode(String label) {
		//counter for how many edges were deleted
		int arrayterator = 0;
		int arrayterator1 = 0;

		if (label == null || !adjlist.containsKey(label)) { // IF NOT IN THE SET
			return false;
		}
		else {
			//go through the edges INCOMING to node and delete them from the
			//other's OUTGOING
			//go through the edges OUTGOING and delete them from the other's 
			//INCOMING
			//count the amount of things we deleted-->
			Iterator outgoingDeletions = adjlist.get(label).outgoing.entrySet().iterator();
			Iterator incomingDeletions = adjlist.get(label).incoming.entrySet().iterator();
			Edge[] removable1 = new Edge[esize];
			Edge[] removable = new Edge[esize];
			while (outgoingDeletions.hasNext()) {
				Edge actualEdge;
				Object o = outgoingDeletions.next();
				actualEdge = (Edge) ((Map.Entry) o ).getValue();
				if (actualEdge != null && esize > 0){
					removable[arrayterator] = actualEdge;
					arrayterator++;
				}
			}
			
			while(incomingDeletions.hasNext()) {
				Edge edge = (Edge) ((Map.Entry) incomingDeletions.next() ).getValue();
				if (edge != null && esize > 0){
					removable1[arrayterator1] = edge;

					arrayterator1++;
				}
			}

			for (int i = 0; i < esize; i++) {
				if (removable[i] != null) {
					if (removable[i].sLabel != null && removable[i].dLabel != null){
						delEdge(removable[i].sLabel, removable[i].dLabel);
					}
				}
				
				if (removable1[i] != null) {
					if (removable1[i].sLabel != null && removable1[i].dLabel != null){
						delEdge(removable1[i].sLabel, removable1[i].dLabel);

					}
				}
			}

			nodeid.remove(adjlist.get(label).id);
			adjlist.get(label).incoming.clear();
			adjlist.get(label).outgoing.clear();
			adjlist.remove(label);
			nsize--;
			return true;
		}
	}

	@Override
	public boolean delEdge(String sLabel, String dLabel) {
		if (!adjlist.containsKey(sLabel) || !adjlist.containsKey(dLabel)) { // IF NOT IN SET
			return false;
		}
		if (!adjlist.get(dLabel).incoming.containsKey(sLabel)) {  //NEED ORACLE BABY!
			return false;
		}
		else {

			edgeid.remove(adjlist.get(sLabel).outgoing.get(dLabel).idNum);
			edgeid.remove(adjlist.get(dLabel).incoming.get(sLabel).idNum);
			adjlist.get(sLabel).outgoing.remove(dLabel);
			adjlist.get(dLabel).incoming.remove(sLabel);
			esize--;
			return true;
		}
	}

	@Override
	public long numNodes() {
		return nsize;
	}

	@Override
	public long numEdges() {
		return esize;
	}

	@Override
	public ShortestPathInfo[] shortestPath(String label) {
		//setting some node qualities
		Iterator entrySetIterator = adjlist.entrySet().iterator();
		while (entrySetIterator.hasNext()) {
			Map.Entry next = (Map.Entry) entrySetIterator.next();
			Node node = (Node) next.getValue();
			node.distance=limit;
			node.wasHere =false;

		}
		//putting together some tools:
		adjlist.get(label).distance = 0;
		CompareNodeDistance comparator = new CompareNodeDistance();
		ShortestPathInfo[] array = new ShortestPathInfo[nsize];
		PriorityQueue<Node> path = new PriorityQueue<Node>(999, comparator);
		HashSet<String> maybe = new HashSet<String>();

		//grab weight for outgoing edges--that's what to add up
		for(String thisLabel: adjlist.get(label).outgoing.keySet()) {
			adjlist.get(label).wasHere = true;
			adjlist.get(thisLabel).distance = adjlist.get(label).outgoing.get(thisLabel).weight;
			path.add(adjlist.get(thisLabel));
			maybe.add(thisLabel);
		}

		//use PRQUEUE to get min path node
		while (!path.isEmpty()) {
			Node min = path.remove();
			adjlist.get(min.label).wasHere = true;
			for (String thisLabel: adjlist.get(min.label).outgoing.keySet()) {
				if (adjlist.get(min.label).distance + adjlist.get(min.label).outgoing.get(thisLabel).weight < adjlist.get(thisLabel).distance) {
					adjlist.get(thisLabel).distance = adjlist.get(min.label).outgoing.get(thisLabel).weight + adjlist.get(min.label).distance;
					maybe.add(thisLabel);
					path.add(adjlist.get(thisLabel));
				}
			}
		}
		int counter = 0;

		Iterator nodeSetIterator = adjlist.entrySet().iterator();
		while (nodeSetIterator.hasNext()) {
			//first get the iterator's next entry, then get the node value from that entry
			Map.Entry next_ = (Map.Entry) nodeSetIterator.next();
			Node node = (Node) next_.getValue();

			if (node.wasHere == true) {
				array[counter] = new ShortestPathInfo(node.label, node.distance);
			} else {
				array[counter] = new ShortestPathInfo(node.label,-1);
				//this says that its not in the path
			}
			counter++;
		}
		return array;
	}

}
