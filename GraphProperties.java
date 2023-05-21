import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GraphProperties {
    private int[][] adjacencyMatrix ,incidenceMatrix;
    private int numVertices ,numEdges;

    public GraphProperties(int[][] adjacencyMatrix, int[][] incidenceMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.incidenceMatrix = incidenceMatrix;
        this.numVertices = adjacencyMatrix.length;
        this.numEdges = incidenceMatrix[0].length;
    }

    public boolean isReflexive() {
        for (int i = 0; i < numVertices; i++) {
            if (adjacencyMatrix[i][i] == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isSymmetric() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                    if (adjacencyMatrix[i][j] != adjacencyMatrix[j][i]) {
                        return false;

                    }

            }
        }
        return true;
    }

    public boolean isTransitive() {
        int[][] resultMatrix = multiplyMatrix(adjacencyMatrix, adjacencyMatrix);
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (resultMatrix[i][j] != 0 && adjacencyMatrix[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isAcyclic() {
        boolean[] visited = new boolean[numVertices];
        boolean[] recursionStack = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            if (isCyclicUtil(i, visited, recursionStack)) {
                return false; // Cycle found
            }
        }
        return true; // No cycles found
    }

    private boolean isCyclicUtil(int vertex, boolean[] visited, boolean[] recursionStack) {
        if (recursionStack[vertex]) {
            return true; // Cycle found
        }

        if (visited[vertex]) {
            return false; // Already visited, no cycle
        }

        visited[vertex] = true;
        recursionStack[vertex] = true;

        for (int i = 0; i < numVertices; i++) {
            if (adjacencyMatrix[vertex][i] != 0) {
                if (isCyclicUtil(i, visited, recursionStack)) {
                    return true; // Cycle found
                }
            }
        }

        recursionStack[vertex] = false; // Remove vertex from recursion stack
        return false; // No cycle found
    }


    public boolean isConnected() {
        boolean[] visited = new boolean[numVertices];

        // Find a vertex that hasn't been visited
        int startVertex = -1;
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                startVertex = i;
                break;
            }
        }

        // If no unvisited vertex found, the graph is already connected
        if (startVertex == -1) {
            return true;
        }

        // Perform BFS from the start vertex
        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            for (int i = 0; i < numVertices; i++) {
                if (adjacencyMatrix[vertex][i] != 0 && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }

        // Check if all vertices have been visited
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                return false; // Not all vertices are reachable
            }
        }

        return true; // All vertices are reachable, graph is connected
    }


    public boolean hasEulerianPath() {
        int oddDegreeVertices = 0;
        for (int i = 0; i < numVertices; i++) {
            int degree = 0;
            for (int j = 0; j < numEdges; j++) {
                if (incidenceMatrix[i][j] != 0) {
                    degree++;
                }
            }
            if (degree % 2 != 0) {
                oddDegreeVertices++;
            }
        }
        return oddDegreeVertices == 0 || oddDegreeVertices == 2;
    }

    private int[][] multiplyMatrix(int[][] matrix1, int[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int cols2 = matrix2[0].length;
        int[][] resultMatrix = new int[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return resultMatrix;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of vertices: ");
        int numVertices = scanner.nextInt();
        System.out.print("Enter the number of edges: ");
        int numEdges = scanner.nextInt();

        int[][] adjacencyMatrix = new int[numVertices][numVertices];
        int[][] incidenceMatrix = new int[numVertices][numEdges];

        System.out.println("Enter the adjacency matrix:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                adjacencyMatrix[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Enter the incidence matrix:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numEdges; j++) {
                incidenceMatrix[i][j] = scanner.nextInt();
            }
        }

        GraphProperties graph = new GraphProperties(adjacencyMatrix, incidenceMatrix);

        System.out.println("Reflexive: " + graph.isReflexive());
        System.out.println("Symmetric: " + graph.isSymmetric());
        System.out.println("Transitive: " + graph.isTransitive());
        System.out.println("Acyclic: " + graph.isAcyclic());
        System.out.println("Connected: " + graph.isConnected());
        System.out.println("Has Eulerian Path: " + graph.hasEulerianPath());

        scanner.close();
    }
}
