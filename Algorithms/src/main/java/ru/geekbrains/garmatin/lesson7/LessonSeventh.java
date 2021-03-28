package ru.geekbrains.garmatin.lesson7;

import ru.geekbrains.garmatin.lesson7.graph.Graph;

public class LessonSeventh {
    private static final int ARRAY_LENGTH = 400;

    public static void main(String[] args) {
        MyOwnDataType[] array = Utils.generateArray(ARRAY_LENGTH);

        Graph<MyOwnDataType> graph = new Graph<>(array.length);
        int startVertexIndex = array.length * 2 / 3;

        Task task = new Task();

        task.graphExample();

        task.fillGraph(array, graph);

        task.useDepthFirstTraversal(graph, startVertexIndex);

        task.useBreadthFirstTraversal(graph, startVertexIndex);
    }
}
