package ru.geekbrains.garmatin.lesson7;

import ru.geekbrains.garmatin.lesson7.graph.Graph;

public class Task {

    public void graphExample() {
        System.out.println("\nGraph example - social media platform\n");
    }

    public void fillGraph(MyOwnDataType[] array, Graph<MyOwnDataType> graph) {
        System.out.println("Fill graph: " + Utils.checkTime(() ->
                Utils.arrayToGraph(array, graph)) + "\n");
    }

    public void useDepthFirstTraversal(Graph<MyOwnDataType> graph, int startVertex) {
        System.out.println("\nDepthFirstTraversal: " + Utils.checkTime(() ->
                graph.depthFirstTraversal(startVertex)) + "\n");
    }

    public void useBreadthFirstTraversal(Graph<MyOwnDataType> graph, int startVertex) {
        System.out.println("BreadthFirstTraversal: " + Utils.checkTime(() ->
                graph.breadthFirstTraversal(startVertex)) + "\n");
    }
}
