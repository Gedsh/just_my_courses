package ru.geekbrains.garmatin.lesson7.graph;

public class Graph<E> {
    private final Object[] vertexList;
    private final int[][] adjacencyMatrix;
    private int size;

    public Graph(int MAX_VERTEX) {

        vertexList = new Object[MAX_VERTEX];
        adjacencyMatrix = new int[MAX_VERTEX][MAX_VERTEX];

        for (int i = 0; i < MAX_VERTEX; i++) {
            for (int j = 0; j < MAX_VERTEX; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    public void addVertex(E data) {
        vertexList[size++] = new Vertex<>(data);
    }

    public void addEdge(int start, int end) {
        adjacencyMatrix[start][end] = 1;
        adjacencyMatrix[end][start] = 1;
    }

    public void depthFirstTraversal(int startVertex) {
        depthFirstTraversalRecursive(startVertex);
        resetFlags();
    }

    @SuppressWarnings("unchecked")
    private void depthFirstTraversalRecursive(int startVertex) {
        ((Vertex<E>) vertexList[startVertex]).wasVisited = true;

        for (int i = 0; i < size; i++) {
            int vertex = getUnvisitedVertex(startVertex);
            if (!((Vertex<E>) vertexList[i]).wasVisited && vertex != -1) {
                fullDisplayVertex(startVertex, vertex);
                depthFirstTraversalRecursive(i);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void breadthFirstTraversal(int startVertex) {
        int[] queue = new int[size];
        int queueHead = 0;
        int queueTile = 0;

        ((Vertex<E>) vertexList[0]).wasVisited = true;
        queue[queueTile++] = startVertex;

        displayVertex(0);

        int vertex;
        while (queueHead < queueTile) {
            startVertex = queue[queueHead++];

            for (int i = 0; i < size; i++) {
                vertex = getUnvisitedVertex(startVertex);
                if (!((Vertex<E>) vertexList[i]).wasVisited && vertex != -1) {
                    ((Vertex<E>) vertexList[i]).wasVisited = true;
                    displayVertex(queueTile);
                    queue[queueTile++] = i;
                }

            }
        }

        resetFlags();

        System.out.println();
    }

    @SuppressWarnings("unchecked")
    private void resetFlags() {
        for (Object vertex : vertexList) {
            ((Vertex<E>) vertex).wasVisited = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void displayVertex(int vertex) {
        System.out.printf("%s ", ((Vertex<E>) vertexList[vertex]).data);
    }

    @SuppressWarnings("unchecked")
    private void fullDisplayVertex(int vertexFirst, int vertexSecond) {
        System.out.printf("Vertex %s - %s ",
                ((Vertex<E>) vertexList[vertexFirst]).data,
                ((Vertex<E>) vertexList[vertexSecond]).data);
    }

    @SuppressWarnings("unchecked")
    private int getUnvisitedVertex(int vertex) {
        for (int i = 0; i < size; i++) {
            if (adjacencyMatrix[vertex][i] == 1 && !((Vertex<E>) vertexList[i]).wasVisited) {
                return i;
            }
        }

        return -1;
    }
}
