package DirectedGraph;

//import graph.*;
import java.util.*;

/**
 * Graph represents a mutable collection of nodes and edges and is designed to store a number of nodes
 * and connects two nodes by one-way pointers. This implementation includes any number of edges (0, 1, 2, ...)
 * between a pair of nodes.

 * Example of Graph include [node1 - [edge1(node2), edge2(node3)]], [node2 - [edge3(node3)]].... with Edges edge1
 * and edge2 connecting node pairs [node1, node2] and [node1, node3] and Edge edge3 connecting [node2, node3] .
 *
 * E is the type that represents the node in Graph and T is the type that represents the data that each edge
 * between a parent and a child nodes represent.
 */
public class Graph<E, T>{
    // Representation invariant:
    // nodeGraph is not null and every node and every edge in nodeGraph are not null
    // No duplicate nodes in nodeGraph and no edge must have the same parent node, child node and edge label
    // For each node 'n' in nodeGraph.keySet(), every edge 'e' in nodeGraph.get(n) has
    // e.getParentNode() == n and nodeGraph.containsKey(e.getChildNode()) == true

    /**
     * Maps each parent node to a set of edges connecting them to other children nodes in this Graph.
     */
    private final Map<E, Set<Edge<E, T>>> nodeGraph;

    /**
     * Constant to check if the expensive tests in the Representation Invariant holds
     */
    private static final boolean DEBUG = false;

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        if (DEBUG) {
            assert (nodeGraph != null);

            for (E currentNode : nodeGraph.keySet()) {
                assert (currentNode != null);
                assert (nodeGraph.get(currentNode) != null);

                Set<Edge<E, T>> edges = nodeGraph.get(currentNode);
                for (Edge<E, T> currentEdge : edges) {
                    assert (currentEdge.getParentNode().equals(currentNode));
                    assert (nodeGraph.containsKey(currentEdge.getChildNode()));
                }
            }
        }
    }

    /**
     * Default constructor to construct a new Graph with no nodes or edges.
     */
    public Graph() {
        nodeGraph = new HashMap<>();
        checkRep();
    }

    /**
     * Adds a new node of type E to this nodeGraph. Nodes are not duplicated in this nodeGraph
     */
    public void addNode(E nodeData) {
        checkRep();
        if (nodeData == null) {
            throw new IllegalArgumentException("Argument is a null node");
        }

        // Node is added to nodeGraph if it doesn't already exist in it
        if (!containsNode(nodeData)) {
            nodeGraph.put(nodeData, new HashSet<>());
        }
        checkRep();
    }

    /**
     * Adds a new edge of type Edge to this nodeGraph. Edges are added in all cases except if they do not have
     * identical parent node, child node and edge label. If the edge's parent node or child node does not already
     * exist in this nodeGraph, the missing nodes are added into this nodeGraph
     */
    public void addEdge(Edge<E, T> newEdge) {
        checkRep();
        if (newEdge == null) {
            throw new IllegalArgumentException("Argument is a null edge");
        }

        // Edge is added to nodeGraph if it doesn't already exist in it
        if (!containsEdge(newEdge)) {

            // Parent Node is added to nodeGraph if it doesn't already exist in it
            if (!containsNode(newEdge.getParentNode())) {
                addNode(newEdge.getParentNode());
            }

            // Child Node is added to nodeGraph if it doesn't already exist in it
            if (!containsNode(newEdge.getChildNode())) {
                addNode(newEdge.getChildNode());
            }

            // Edge is added to nodeGraph to create a connection between the parent node and child node
            nodeGraph.get(newEdge.getParentNode()).add(newEdge);
        }
        checkRep();
    }

    /**
     * Returns an output which contains a space-separated set of all the data that represents each node on
     * this graph.
     *
     * @return a set of all the nodes in this graph is returned
     */
    public Set<E> listNodes() {
        checkRep();
        // Returning a set of all nodes in this Graph
        return new HashSet<>(this.nodeGraph.keySet());
    }

    /**
     * Returns an output which contains a Map whose key consists of all the children nodes of the parameter
     * nodeData, which is a node on this graph and also the parent node for each label of the edge displayed,
     * and whose value consists of a set of objects which contain all edge labels that connect the parent
     * node nodeData to the corresponding child node in the key, which maps the edge label set in the value.
     * Multiple edges between two nodes have a separate entry in the set for each edge.
     *
     * @param nodeData the object of type E that represents the node whose children are to be returned from this graph
     * @return a list of all the children nodes mapped with all the edges that it connects with the parent node,
     * represented by the object 'nodeData'.
     * @throws IllegalArgumentException if nodeData == null or if !containsNode(nodeData)
     */
    public Map<E, Set<T>> listChildren(E nodeData) {
        checkRep();
        if (nodeData == null) {
            throw new IllegalArgumentException("Null node");
        } else if (!containsNode(nodeData)) {
            throw new IllegalArgumentException("Node not in graph");
        }

        // Stores set of all edges connected such that nodeData represents their parent node in the Graph
        Set<Edge<E, T>> edges = new HashSet<>(this.nodeGraph.get(nodeData));

        // Stores a list of all the children nodes mapped with all the edges it connects with nodeDara
        Map<E, Set<T>> children = new HashMap<>();

        for (Edge<E, T> currentEdge : edges) {
            E childNode = currentEdge.getChildNode();
            // Adding Child Node of Edge to the Map only if it doesn't already exist in it
            if (!children.containsKey(childNode)) {
                children.put(childNode, new HashSet<>());
            }
            // Adding Edge Label of the Edge connecting parent node represented by nodeData and child node
            // represented by childNode in this Graph
            children.get(childNode).add(currentEdge.getEdgeLabel());
        }

        checkRep();
        return children;
    }

    /**
     * Returns an output which contains a set of all the Edges such that it has the parameter nodeData as the parent
     * node of that Edge, which connects that node represented by nodeData to its children nodes on this graph.
     *
     * @param nodeData the object of type E that represents the node whose Edges connected to it are to be returned
     *                 from this graph
     * @return a set of all the Edges that connect nodeData to its children nodes in this graph is returned
     * @throws IllegalArgumentException if nodeData == null or if containsNode(nodeData) == false
     */
    public Set<Edge<E, T>> listEdges(E nodeData) {
        checkRep();
        if (nodeData == null) {
            throw new IllegalArgumentException("Null node");
        } else if (!containsNode(nodeData)) {
            throw new IllegalArgumentException("Node not in Graph");
        }
        checkRep();
        return new HashSet<>(this.nodeGraph.get(nodeData));
    }

    /**
     * Returns true if this Graph is empty
     *
     * @return true if and only if this is empty with no nodes or edges in this and false otherwise
     */
    public boolean isEmpty() {
        checkRep();
        return this.nodeGraph.isEmpty();
    }

    /**
     * Returns true if the node represented by the object 'nodeData' is found in this graph.
     *
     * @param nodeData the object of type E that represents the node to be checked if it is found in this graph
     * @return true if and only if the node represented by the object 'nodeData' is found in this graph and
     * false otherwise
     * @throws IllegalArgumentException if nodeData == null
     */
    public boolean containsNode(E nodeData) {
        checkRep();
        if (nodeData == null) {
            throw new IllegalArgumentException("Null node");
        }

        checkRep();
        // Returns if the node represented by nodeData is a node on this Graph
        return this.nodeGraph.containsKey(nodeData);
    }

    /**
     * Returns true if the edge represented by the object Edge 'edge' is found in this graph for the specific
     * parent node which 'edge' represents. If the parent node that the outgoing Edge connects doesn't exist
     * in this Graph, it returns false.
     *
     * @param edge the Edge object that represents the edge to be checked if it is found in this graph for the
     *             specific parent node which the edge represents
     * @return true if and only if the edge represented by the object Edge 'edge' is found in this graph for the
     * specific parent node which the edge represents and false otherwise. It also returns false if the parent
     * node that the outgoing edge connects isn't in this graph
     * @throws IllegalArgumentException if edge == null
     */
    public boolean containsEdge(Edge<E, T> edge) {
        checkRep();
        if (edge == null) {
            throw new IllegalArgumentException("Null edge");
        }

        // Checks if the parent node that edge connects isn't in this graph,
        if (!containsNode(edge.getParentNode())) {
            checkRep();
            return false;
        }

        checkRep();
        // Returns if the Edge represented by 'edge' is an edge on this Graph
        return this.nodeGraph.get(edge.getParentNode()).contains(edge);
    }

    /**
     * Edge represents a non-mutable structure in the Graph. This Edge class implementation is designed to store
     * the parent node and the child node that it connects to, as well as stores the data of the object of type T
     * it represents, which aren't unique throughout the graph, but is unique for each pair of parent node and
     * child node it points from and towards respectively. The Edge is also a one-way pointer only.
     *
     * This implementation also includes returning the value of the data of the parent node, child node and
     * the label, which stores the object of type T value of the data this edge represents.
     *
     * Example of Graph include ["Node1", "Node2", "Edge1"]
     */
    public static class Edge<E, T> {
        // Representation invariant:
        // parentNode != null and childNode != null and edgeLabel != null
        //
        // Abstraction function:
        // AF(this) = The Edge is represented as a structure on the Graph representation such that
        //            the edge on the graph is connected from 'parentNode' to 'childNode', where in some
        //            cases 'parentNode' could equal 'childNode', and contains an edge label 'edgeLabel'.
        //            'parentNode' and 'childNode' are nodes that are present in the Graph. This edge does not
        //            have a duplicate in the Graph with 'edgeLabel' being unique between the pair of
        //            'parentNode' and 'childNode'.
        /**
         * Holds the information of this edge by storing its parent node it points from, the child node it points
         * to on the graph and the unique edgeLabel it represents between this pair of parent and child node
         */
        private final E parentNode, childNode;
        private final T edgeLabel;

        /**
         * Throws an exception if the representation invariant is violated.
         */
        private void checkRep() {
            // Asserting that the edge is not null
            assert (this.parentNode != null && this.childNode != null && this.edgeLabel != null);
        }

        /**
         * @param parentNode the parent node of the Edge from which it is pointing from on the graph
         * @param childNode  the child node of the Edge from which it is pointing towards on the graph.
         * @param edgeLabel  the object of type T that represents the value of the label to be stored to this edge
         * @throws IllegalArgumentException if n1 == null or n2 == null or nodeLabel == null
         * @spec.effects Constructs a new Edge in the graph, whose parent node it is pointing from is n1, child
         * node it is pointing towards n2, and whose value of the label to be stored is nodeLabel.
         */
        public Edge(E parentNode, E childNode, T edgeLabel) {
            if (parentNode == null || childNode == null || edgeLabel == null) {
                throw new IllegalArgumentException("Null entry");
            }

            this.parentNode = parentNode;
            this.childNode = childNode;
            this.edgeLabel = edgeLabel;
            checkRep();
        }

        /**
         * Gets and returns the Parent node associated with this edge
         *
         * @return the value of the parent node of this edge.
         */
        public E getParentNode() {
            checkRep();
            return this.parentNode;
        }

        /**
         * Gets and returns the Child node associated with this edge
         *
         * @return the value of the child node of this edge.
         */
        public E getChildNode() {
            checkRep();
            return this.childNode;
        }

        /**
         * Gets and returns the label associated with this edge which is the object of type T of the data it represents.
         *
         * @return the value of the label of this edge.
         */
        public T getEdgeLabel() {
            checkRep();
            return this.edgeLabel;
        }

        /**
         * This function is the standard hashCode function which returns a value specific to all
         * equivalent objects
         *
         * @return an integer that all objects equal to this will also return
         */
        @Override
        public int hashCode() {
            checkRep();
            return this.parentNode.hashCode() ^ this.childNode.hashCode() ^ this.edgeLabel.hashCode();
        }

        /**
         * This function is the standard equality operation which returns true if the objects
         * are instances of the same class and are equal edges and false for any other case.
         *
         * @param obj the object to be compared for equality
         * @return true if and only if 'obj' is an instance of Edge and 'this' and 'obj' represent
         * the same Edge.
         * @throws IllegalArgumentException if obj == null
         */
        @Override
        public boolean equals(Object obj) {
            checkRep();
            if (obj == null) {
                throw new IllegalArgumentException("Null edge");
            }
            if (obj instanceof Edge) {
                // Type casting into Edge class as it is an instance of it
                Edge<?, ?> ed = (Edge<?, ?>) obj;
                checkRep();
                return this.parentNode.equals(ed.getParentNode()) && this.childNode.equals(ed.getChildNode())
                        && this.edgeLabel.equals(ed.getEdgeLabel());
            } else {
                checkRep();
                return false;
            }
        }
    }
}