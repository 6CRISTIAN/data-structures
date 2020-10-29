package com.zentagroup.datastructures.graphs;

import com.zentagroup.datastructures.avltree.AVLTreeSet;

import java.util.*;

public class StructuredGraph {

    private HashMap<String, HashSet<Vertex>> vertices;
    private int numVertices;
    private int numEdges;

    /**
     * Constructor that instantiates an empty Graph.
     */
    public StructuredGraph() {
        vertices = new HashMap();
        numVertices = 0;
        numEdges = 0;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    /**
     * Adds a Vertex to the graph based on a Hashmap with the data and its tag which
     * defines the type of the data.
     * If the type isn't already in the graph's hashmap it creates a new Key set with it.
     *
     * @param data Hashmap with fields as keys and data as value.
     * @param tag  type of data.
     * @return Vertex created. If there's an exception it returns null.
     */
    public Vertex addVertex(HashMap<String, Object> data, String tag) {
        Vertex newVertex = new Vertex(data, tag);
        try {
            if (!vertices.containsKey(newVertex.tag)) {
                vertices.put(newVertex.tag, new HashSet<Vertex>());
            }
            vertices.get(newVertex.tag).add(newVertex);
            numVertices++;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return newVertex;
    }

    /**
     * Adds an Edge to the affected vertex based on origin and destination data and types, a relationship,
     * and a boolean indicating whether the relationship is bidirectional or not. It automatically sets the
     * weight to 1. It uses the addEdge method.
     *
     * @param origin          Hashmap with fields as keys and data as value of the origin.
     * @param tagOrigin       data type of the origin.
     * @param destination     Hashmap with fields as keys and data as value of the destination.
     * @param tagDestination  data type of the destination.
     * @param relationship    relationship for the edges.
     * @param isBidirectional indicates if the relationship goes both ways.
     * @param <T>             type of the relationship.
     */
    public <T extends Comparable> void addEdge(HashMap<String, Object> origin, String tagOrigin,
                                               HashMap<String, Object> destination, String tagDestination,
                                               T relationship, boolean isBidirectional) {
        addEdge(origin, tagOrigin, destination, tagDestination, relationship, isBidirectional, 1.0);
    }

    /**
     * Adds an Edge to the affected vertex based on origin and destination data and types, a relationship,
     * the weight or numeric value of it, and a boolean indicating whether the relationship is bidirectional
     * or not. It uses the Vertex addRelationship method.
     *
     * @param origin          Hashmap with fields as keys and data as value of the origin.
     * @param tagOrigin       data type of the origin.
     * @param destination     Hashmap with fields as keys and data as value of the destination.
     * @param tagDestination  data type of the destination.
     * @param relationship    relationship for the edges.
     * @param isBidirectional indicates if the relationship goes both ways.
     * @param weight          Double with numeric value of relationship.
     * @param <T>             type of the relationship.
     */
    public <T extends Comparable> void addEdge(HashMap<String, Object> origin, String tagOrigin,
                                               HashMap<String, Object> destination, String tagDestination,
                                               T relationship, boolean isBidirectional, Double weight) {
        Vertex d = findVertex(destination, tagDestination);
        if (null == d)
            d = addVertex(destination, tagDestination);
        Vertex o = findVertex(origin, tagOrigin);
        if (null == o)
            o = addVertex(origin, tagOrigin);
        o.addRelationship(d, relationship, isBidirectional, weight);
    }

    /**
     * Deletes a Vertex from the graph based on its data and type. It reduces the total number of
     * vertices by one. It deletes also all the edges containing the vertex using the deleteEdge method.
     *
     * @param vertexData Hashmap with fields as keys and data as value.
     * @param vertexTag  type of data.
     * @return false if the vertex doesn't exist. If it exists and is deleted it returns true.
     */
    public boolean deleteVertex(HashMap<String, Object> vertexData, String vertexTag) {
        Vertex v = findVertex(vertexData, vertexTag);
        if (null != v) {
            Set<Vertex> egressIngressVertex = findEgressAndIngress(vertexData, vertexTag);
            for (Vertex vertex : egressIngressVertex) {
                deleteEdge(v.data, v.tag, vertex.data, vertex.tag);
            }
            vertices.get(vertexTag).remove(v);
            numVertices--;
            return true;
        }
        return false;
    }

    /**
     * Deletes all the Edges representing relationships between two vertex based on their data and type.
     * It uses the deleteEdge private method twice with the egress and twice with the ingress with one of
     * the vertices as origin each time.
     *
     * @param origin      Hashmap with fields as keys and data as value of the origin.
     * @param oTag        data type of the origin.
     * @param destination Hashmap with fields as keys and data as value of the destination.
     * @param dTag        data type of the destination.
     * @return true if both vertices exist and edges are deleted. Otherwise false.
     */
    public boolean deleteEdge(HashMap<String, Object> origin, String oTag, HashMap<String, Object> destination, String dTag) {
        Vertex o = findVertex(origin, oTag);
        Vertex d = findVertex(destination, dTag);
        if (null != o || null != d) {
            deleteEdge(d, dTag, o.getEgress());
            deleteEdge(o, oTag, d.getIngress());
            deleteEdge(d, dTag, o.getIngress());
            deleteEdge(o, oTag, d.getEgress());
            return true;
        }
        return false;
    }

    /**
     * Implements the deletion of edges based on an egress or ingress origin map and a destination vertex.
     * It deletes the edges with the destination vertex from the map of the origin.
     * If after the deletion the value of the map (AVLTreeSet) is empty (no other edges
     * of the type exist) it deletes the key - value pair from the map.
     *
     * @param d    destination Vertex
     * @param dTag data type of the destination
     * @param type egress or ingress HashMap
     * @return true if the edges were deleted. Otherwise false.
     */
    private boolean deleteEdge(Vertex d, String dTag, Map<String, AVLTreeSet<Edge>> type) {
        for (Map.Entry<String, AVLTreeSet<Edge>> e : type.entrySet()) {
            if (e.getKey().equals(dTag)) {
                for (Edge edge : e.getValue()) {
                    if (edge.vertex.compareTo(d) == 0) {
                        e.getValue().remove(edge);
                        numEdges--;
                    }
                }
                if (e.getValue().isEmpty())
                    type.keySet().removeIf(key -> key == dTag);
            }
        }
        return true;
    }

    /**
     * Finds a list of vertices in the graph based on attributes and the data type.
     * It compares each attribute by field and value with the attributes of the maps vertices
     * of the same type. It adds the vertices with matching values to the list.
     *
     * @param attributes hashmap with a variable quantity of attributes from Vertex.
     * @param tag Vertex data type.
     * @return list of vertices with matching values.
     */
    public List<Vertex> findVertexByAttributes(HashMap<String, Object> attributes, String tag) {
        List<Vertex> v = new LinkedList();
        if (vertices.containsKey(tag)) {
            for (Vertex vertex : vertices.get(tag)) {
                boolean matchesAttributes = true;
                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                    if (vertex.data.get(entry.getKey()) != entry.getValue()) {
                        matchesAttributes = false;
                        break;
                    }
                }
                if (matchesAttributes)
                    v.add(vertex);
            }
        }
        return v;
    }

    /**
     * Finds a Vertex in the graph and returns it. Uses the findVertexByAttribute method.
     * If the list has one more than a vertex it returns null, else returns vertex.
     *
     * @param data Hashmap with fields as keys and data as value.
     * @param tag  data type.
     * @return Vertex or null.
     */
    public Vertex findVertex(HashMap<String, Object> data, String tag) {
        List<Vertex> v = findVertexByAttributes(data, tag);
        if (v.size() == 1) {
            return v.get(0);
        }
        return null;
    }

    /**
     * Finds all the types of egress relationships based on the data of an origin Vertex.
     *
     * @param origin Hashmap with fields as keys and data as value.
     * @param tag    data type.
     * @return List with strings representing the different types of data related to the vertex.
     */
    public List<String> findTypeOfRelationships(HashMap origin, String tag) {
        Vertex o = findVertex(origin, tag);
        if (o != null) {
            List<String> types = new ArrayList<>();
            for (Map.Entry<String, AVLTreeSet<Edge>> e : o.getEgress().entrySet())
                types.add(e.getKey());
            return types;
        }
        return null;
    }

    /**
     * Gets all vertices related to a given origin Vertex both with egress and ingress relationships.
     * Uses the getNeighbors and getEntrantVertices methods and only returns the unique values.
     *
     * @param vertexData Hashmap with fields as keys and data as value.
     * @param tag        data type.
     * @return Set with vertices related to the origin.
     */
    public Set<Vertex> findEgressAndIngress(HashMap<String, Object> vertexData, String tag) {
        Set<Vertex> allVertex = new HashSet();
        allVertex.addAll(findNeighbors(vertexData, tag));
        allVertex.addAll(findEntrantVertices(vertexData, tag));
        return allVertex;
    }

    /**
     * Finds the vertices in the egress list of an origin Vertex (vertices that have an entrant
     * relationship from the origin). Uses findRelatedVertices method and sets the relationship to null.
     *
     * @param origin Vertex in which the search will be based.
     * @param tag    data type.
     * @return list of vertices with entrant relationships of the origin Vertex or null if the vertex
     * doesn't exist
     */
    public List<Vertex> findNeighbors(HashMap origin, String tag) {
        Vertex o = findVertex(origin, tag);
        List<Vertex> neighbors = new LinkedList();
        for (Map.Entry<String, AVLTreeSet<Edge>> entry : o.getEgress().entrySet()) {
            neighbors.addAll(findRelatedVertices(o.getEgress(), entry.getKey(), null));
        }
        return neighbors;
    }

    /**
     * Finds the vertices related to a given origin vertex that belong to a specific data type
     * and relationship, or only the data type if the relationship is null. Uses findRelatedVertices method.
     *
     * @param origin       Hashmap with fields as keys and data as value.
     * @param oTag         origin data type.
     * @param dTag         destination vertices data type.
     * @param relationship between the two vertices.
     * @param <T>          relationship type.
     * @return list of vertices.
     */
    public <T extends Comparable> List<Vertex> findNeighbors(HashMap origin, String oTag, String dTag, T relationship) {
        Vertex o = findVertex(origin, oTag);
        if (o != null) {
            return findRelatedVertices(o.getEgress(), dTag, relationship);
        }
        return null;
    }

    /**
     * Finds the vertices in the ingress list of an origin Vertex (vertices the origin has
     * as entrant relationships). Uses findRelatedVertices method and sets the relationship to null.
     *
     * @param origin Vertex in which the search will be based.
     * @param tag    data type.
     * @return list of vertices with outgoing relationships to the origin vertex or null if the vertex
     * doesn't exist.
     */
    public List<Vertex> findEntrantVertices(HashMap origin, String tag) {
        Vertex o = findVertex(origin, tag);
        List<Vertex> neighbors = new LinkedList();
        for (Map.Entry<String, AVLTreeSet<Edge>> entry : o.getIngress().entrySet()) {
            neighbors.addAll(findRelatedVertices(o.getIngress(), entry.getKey(), null));
        }
        return neighbors;
    }

    /**
     * Finds the ingress vertices related to a given origin vertex that belong to a specific data type
     * and relationship, or only the data type if the relationship is null. Uses findRelatedVertices method.
     *
     * @param origin       Hashmap with fields as keys and data as value.
     * @param oTag         origin data type.
     * @param dTag         destination vertices data type.
     * @param relationship between the two vertices.
     * @param <T>          relationship type.
     * @return list of vertices or null if vertex doesn't exist.
     */
    public <T extends Comparable> List<Vertex> findEntrantVertices(HashMap origin, String oTag, String dTag, T relationship) {
        Vertex o = findVertex(origin, oTag);
        if (o != null) {
            return findRelatedVertices(o.getIngress(), dTag, relationship);
        }
        return null;
    }

    /**
     * Retrieve all vertex of the map, if relationship is null returns all vertices of the given tag,
     * else returns only the ones with the same relationship and tag.
     *
     * @param relationshipsByType ingress or egress vertex map
     * @param dTag                destination vertices data type.
     * @param relationship        between the two vertices.
     * @param <T>                 relationship type.
     * @return list of vertices.
     */
    private <T extends Comparable> List<Vertex> findRelatedVertices(
            Map<String, AVLTreeSet<Edge>> relationshipsByType, String dTag, T relationship) {
        List<Vertex> neighbors = new LinkedList();
        for (Map.Entry<String, AVLTreeSet<Edge>> e : relationshipsByType.entrySet()) {
            if (e.getKey().equals(dTag)) {
                e.getValue().forEachInorder(x -> {
                    Edge edge = (Edge) x;
                    if (null != relationship) {
                        if (edge.relationship.compareTo(relationship) == 0)
                            neighbors.add(edge.vertex);
                    } else
                        neighbors.add(edge.vertex);
                });
            }
        }
        return neighbors;
    }

    /**
     * Finds a list of edges in the egress list of an origin Vertex that has the given destination data and
     * relationship.
     *
     * @param origin       Hashmap with fields as keys and data as value of the origin.
     * @param oTag         origin data type.
     * @param destination  Hashmap with fields as keys and data as value of the destination.
     * @param dTag         destination data type.
     * @param relationship between the two vertices.
     * @param <T>          relationship type.
     * @return found edge or null if it doesn't exist.
     */
    public <T extends Comparable> List<Edge> findEdge(HashMap origin, String oTag,
                                                      HashMap destination, String dTag, T relationship) {
        List<Edge> edges = new LinkedList();
        Vertex o = findVertex(origin, oTag);
        Vertex d = findVertex(destination, dTag);
        if (null != o || null != d) {
            for (Map.Entry<String, AVLTreeSet<Edge>> e : o.egress.entrySet()) {
                if (e.getKey().equals(dTag)) {
                    for (Edge edge : e.getValue()) {
                        if (null != relationship) {
                            if (edge.relationship.compareTo(relationship) == 0 && edge.vertex.compareTo(d) == 0)
                                edges.add(edge);
                        } else if (edge.vertex.compareTo(d) == 0)
                            edges.add(edge);
                    }
                }
            }
            return edges;
        }
        return null;
    }

    /**
     * Finds all edges between two Vertices based on their data and type regardless of the relationship.
     *
     * @param origin      Hashmap with fields as keys and data as value of the origin.
     * @param oTag        origin data type.
     * @param destination Hashmap with fields as keys and data as value of the destination.
     * @param dTag        destination type.
     * @return list of edges or null if one of the vertices doesn't exist.
     */
    public List<Edge> findEdgesBetweenTwoObjects(HashMap origin, String oTag, HashMap destination,
                                                 String dTag) {
        return findEdge(origin, oTag, destination, dTag, null);
    }

    /**
     * Finds the edge with the minor wight based on a list of edges.
     *
     * @param edges list of edges.
     * @return Edge with minor weight or null if the list is null.
     */
    public Edge findMinorEdge(List<Edge> edges) {
        if (edges != null || !edges.isEmpty()) {
            Edge edge = edges.get(0);
            for (Edge e : edges) {
                if (e.weight < edge.weight)
                    edge = e;
            }
            return edge;
        }
        return null;
    }

    /**
     * Calculates the total weight of a path based on a list of vertices where each vertex
     * is related to the next one.
     *
     * @param path list of related vertices.
     * @return int weight of path, if a node isn't related to the next one (it isn't a path) or
     * the list has a size of 1 it returns 0;
     */
    public int calculateWeight(List<Vertex> path) {
        int weight = 0;
        if (path.size() > 1) {
            for (int i = 0; i < (path.size() - 1); i++) {
                Vertex o = path.get(i);
                Vertex d = path.get(i + 1);
                Edge edge = findMinorEdge(findEdgesBetweenTwoObjects(o.data, o.tag, d.data, d.tag));
                if (edge == null) return 0;
                weight += edge.weight;
            }
        }
        return weight;
    }

    /**
     * Finds the shortest path (path with minor total weight) between two Vertices based on attributes
     * and type of the data.
     *
     * @param origin      origin Vertex.
     * @param oTag        origins data type.
     * @param destination destination Vertex.
     * @param dTag        destinations type.
     * @return list of vertices in order representing the path from origin to destination.
     */
    public List<Vertex> findShortestPath(HashMap origin, String oTag, HashMap destination, String dTag) {
        Vertex o = findVertex(origin, oTag);
        Vertex d = findVertex(destination, dTag);
        if (d != null && o != null) {
            HashMap<Vertex, Double> vertexW = new HashMap();
            LinkedList<Vertex> queue = new LinkedList();
            List<Vertex> path = new LinkedList();
            vertexW.put(o, 0.0);
            Double lastPathWeight = Double.POSITIVE_INFINITY;
            path = findShortestPathRec(o, d, dTag, queue, vertexW, lastPathWeight, path, 0.0);
            return path;
        }
        return null;
    }

    /**
     * Recursively searches for the destination node in the egress list of a given origin vertex starting
     * from the vertex with minor weight of the list. Once the destination is found it compares the current
     * paths weight to the last one saved, if its minor it replaces it and continues with the cycle until
     * all paths were followed. The resulting path will have the smallest weight.
     *
     * @param origin         origin Vertex.
     * @param destination    destination Vertex.
     * @param dTag           destination type.
     * @param queue          list of the current path.
     * @param vertexW        map with the smallest weights found for each vertex.
     * @param lastPathWeight last smallest weight found to the destination.
     * @param path           last shortest path found.
     * @param weight         current path weight.
     * @return list of vertices.
     */
    private List<Vertex> findShortestPathRec(Vertex origin, Vertex destination, String dTag, LinkedList<Vertex> queue,
                                             HashMap<Vertex, Double> vertexW, Double lastPathWeight,
                                             List<Vertex> path, Double weight) {
        boolean foundDestination = false;
        queue.add(origin);
        HashMap<Vertex, Double> currentVertexW = new HashMap();
        for (Map.Entry<String, AVLTreeSet<Edge>> stringAVLTreeSetEntry : origin.egress.entrySet()) {
            Map.Entry<String, AVLTreeSet<Edge>> e = stringAVLTreeSetEntry;
            if (e.getKey().equals(dTag)) {
                for (Edge y : e.getValue()) {
                    Double currentWeight = weight + y.weight;
                    updateCurrentVertexWeight(y, vertexW, currentVertexW, currentWeight);
                    if (y.vertex == destination && currentWeight < lastPathWeight) {
                        path.clear();
                        path.addAll(queue);
                        path.add(y.vertex);
                        lastPathWeight = currentWeight;
                        foundDestination = true;
                    }
                }
                break;
            }
        }
        for (Map.Entry<Vertex, Double> entry : currentVertexW.entrySet()) {
            if (entry.getValue() <= vertexW.get(entry.getKey()) && entry.getKey() != destination) {
                findShortestPathRec(entry.getKey(), destination, dTag, queue, vertexW,
                        lastPathWeight, path, entry.getValue());
                if (!foundDestination) queue.removeLast();
            }
        }
        return path;
    }

    /**
     * Helper method for findShortestPathRec that adds the current weight of a vertex to
     * the list of currentVertexW and updates the current weight of a vertex in the vertexW
     * map if the current weight is minor to the one already registered.
     *
     * @param currentEdge    edge which's weight will be added / updated in maps
     * @param vertexW        map containing the minor weights of the vertices.
     * @param currentVertexW map containing all the weights of the current path.
     * @param currentWeight  weight of current edge.
     */
    private void updateCurrentVertexWeight(Edge currentEdge, HashMap<Vertex, Double> vertexW, HashMap<Vertex, Double> currentVertexW, Double currentWeight) {
        if (!currentVertexW.containsKey(currentEdge.vertex)) {
            currentVertexW.put(currentEdge.vertex, currentWeight);
        } else {
            if (currentVertexW.get(currentEdge.vertex) > currentWeight)
                currentVertexW.put(currentEdge.vertex, currentWeight);
        }
        if (vertexW.containsKey(currentEdge.vertex)) {
            if (currentWeight < vertexW.get(currentEdge.vertex)) {
                vertexW.put(currentEdge.vertex, currentWeight);
            }
        } else {
            vertexW.put(currentEdge.vertex, currentWeight);
        }
    }

    /**
     * Creates a list with a BFS (Breadth First Search) traversal from a given data and its type.
     *
     * @param data source of the search.
     * @param tag  type of the source data.
     * @return list of vertices.
     */
    public List<Vertex> BFS(HashMap<String, Object> data, String tag) {
        Queue queue = new LinkedList();
        List<Vertex> visited = new LinkedList();
        Vertex v = findVertex(data, tag);
        if (null != v) {
            visited.add(v);
            queue.add(v);
            while (!queue.isEmpty()) {
                v = (Vertex) queue.poll();
                for (Map.Entry<String, AVLTreeSet<Edge>> e : v.egress.entrySet()) {
                    e.getValue().forEachInorder(n -> {
                        Edge edge = (Edge) n;
                        if (!visited.contains(edge.vertex)) {
                            visited.add(edge.vertex);
                            queue.add(edge.vertex);
                        }
                    });
                }
            }
            return visited;
        }
        return null;
    }

    /**
     * Creates a list with a DFS (Depth First Search) traversal from a given data and its type.
     *
     * @param data source of the search. It uses de DFSRec for traversing it recursively.
     * @param tag  type of the source data.
     * @return list of vertices.
     */
    public List<Vertex> DFS(HashMap<String, Object> data, String tag) {
        List<Vertex> visited = new LinkedList<>();
        Vertex origin = findVertex(data, tag);
        if (origin != null) {
            visited = DFSRec(origin, visited);
            return visited;
        }
        return null;
    }

    /**
     * Recursively traverses the graph based on a current Vertex which is marked as visited
     * by going to its egress related vertices if they haven't been visited yet.
     *
     * @param current Vertex.
     * @param visited list of visited vertices.
     * @return list of all visited vertices.
     */
    private List<Vertex> DFSRec(Vertex current, List visited) {
        visited.add(current);
        for (Map.Entry<String, AVLTreeSet<Edge>> e : current.egress.entrySet()) {
            e.getValue().forEachInorder(x -> {
                Edge edge = (Edge) x;
                if (!visited.contains(edge.vertex)) {
                    DFSRec(edge.vertex, visited);
                }
            });
        }
        return visited;
    }

    public class Vertex implements Comparable {
        String tag;
        HashMap<String, Object> data;

        Map<String, AVLTreeSet<Edge>> ingress;
        Map<String, AVLTreeSet<Edge>> egress;

        public Map<String, AVLTreeSet<Edge>> getIngress() {
            return ingress;
        }

        public Map<String, AVLTreeSet<Edge>> getEgress() {
            return egress;
        }

        /**
         * Constructor that creates a vertex with a given data and type
         * creating empty ingress and egress maps.
         *
         * @param data Hashmap with fields as keys and data as value.
         * @param tag  data type.
         */
        public Vertex(HashMap data, String tag) {
            this.tag = tag;
            this.data = data;
            ingress = new HashMap();
            egress = new HashMap();
        }

        @Override
        public String toString() {
            return "VERTEX \n" +
                    " - Data: " + data.toString();
        }

        public void printVertex() {
            System.out.println(
                    "\nVERTEX \n" +
                            " - Data: " + data.toString()
            );
            System.out.println(" ----- Ingress ----- ");
            Iterator<Map.Entry<String, AVLTreeSet<Edge>>> it = ingress.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, AVLTreeSet<Edge>> e = it.next();
                e.getValue().forEachInorder(System.out::println);
            }
            System.out.println(" ----- Egress ----- ");
            Iterator<Map.Entry<String, AVLTreeSet<Edge>>> it2 = egress.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, AVLTreeSet<Edge>> e = it2.next();
                e.getValue().forEachInorder(System.out::println);
            }

        }

        /**
         * Adds a relationship between the current vertex and a given destination vertex by using the
         * addEgressRelationship method once if the relationship is unidirectional with the current vertex as origin
         * and one more time with the destination as origin if it's bidirectional.
         *
         * @param destination     destination vertex of the relationship.
         * @param relationship    data between the two vertices.
         * @param isBidirectional boolean.
         * @param weight          numerical value of the relationship.
         * @param <T>             type of relationship data.
         * @return boolean true if the edges were added or false if the destination or relationship were null.
         */
        public <T extends Comparable> boolean addRelationship(Vertex destination, T relationship, boolean isBidirectional, Double weight) {
            if (destination != null && relationship != null) {
                this.addEgressRelationship(this, destination, relationship, destination.tag, weight);
                if (isBidirectional)
                    this.addEgressRelationship(destination, this, relationship, destination.tag, weight);
                return true;
            }
            return false;
        }

        /**
         * Adds a relationship between the current vertex and a given destination vertex by creating Edges.
         * If the relationship is unidirectional it creates an edge in the egress map with the destination
         * vertex and an edge in the ingress map of the destination vertex with the current vertex.
         * If its bidirectional it does the same the other way around.
         *
         * @param local          origin vertex
         * @param destination    other vertex
         * @param relationship   data between the two vertices.
         * @param destinationTag destination data type.
         * @param weight         int numerical value of the relationship.
         * @param <T>            type of relationship data.
         * @return boolean true if the edges were added.
         */
        private <T extends Comparable> boolean addEgressRelationship(Vertex local, Vertex destination,
                                                                    T relationship, String destinationTag,
                                                                    Double weight) {
            if (!local.egress.containsKey(destinationTag)) {
                local.egress.put(destinationTag, new AVLTreeSet());
            }
            local.egress.get(destinationTag).add(new Edge(relationship, destination, weight));
            numEdges++;
            if (!destination.ingress.containsKey(local.tag)) {
                destination.ingress.put(local.tag, new AVLTreeSet());
            }
            destination.ingress.get(local.tag).add(new Edge(relationship, local, weight));
            numEdges++;
            return true;
        }

        @Override
        public int compareTo(Object o) {
            try {
                Vertex v = (Vertex) o;
                int i = data.hashCode() - (v.data.hashCode());
                if (i > 0)
                    return 1;
                if (i < 0)
                    return -1;
            } catch (Exception ex) {
                return 0;
            }
            return 0;
        }
    }

    public class Edge<V extends Comparable, T extends Comparable> implements Comparable {
        T relationship;
        Vertex vertex;
        Double weight;

        /**
         * Constructor receiving all the attributes.
         *
         * @param relationship data that relates both edges.
         * @param vertex       destination vertex
         * @param weight       numerical value of relationship.
         */
        public Edge(T relationship, Vertex vertex, Double weight) {
            this.relationship = relationship;
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return " * EDGE: " + "\n" +
                    " - Relationship: " + relationship + "\n" +
                    " - Vertex: " + vertex.toString() + "\n" +
                    " - Weight: " + weight + "\n";
        }

        @Override
        public int compareTo(Object o) {
            try {
                Edge<V, T> e = (Edge) o;
                int i = this.relationship.compareTo(e.relationship);
                if (i > 0)
                    return 1;
                if (i < 0)
                    return -1;
                int i2 = this.weight.compareTo(e.weight);
                if (i2 > 0)
                    return 1;
                if (i2 < 0)
                    return -1;
                if (this.vertex.compareTo(e.vertex) > 0)
                    return 1;
                if (this.vertex.compareTo(e.vertex) < 0)
                    return -1;
            } catch (Exception ex) {
                return 0;
            }
            return 0;
        }
    }
}