package pathFinder;

import map.Coordinate;
import map.PathMap;

import java.util.*;

public class DijkstraPathFinder implements PathFinder {
    // TODO: You might need to implement some attributes
    private Graph graph = null;
    private List<Edge> edgePath = new ArrayList<>();
    private List<Coordinate> origins;
    private List<Coordinate> dests;

    // represent a map to a graph first
    public DijkstraPathFinder(PathMap map) {
        // TODO :Implement
        // the number of vertex in the graph is the cells of the grid - the cells that are impassable
        int v = map.sizeR * map.sizeC - map.numberOfImpassable();
        origins = map.originCells;
        dests=map.destCells;
        graph = new Graph(v);
        // put vertex which is the passable cell in the array
        graph.setArray(map.getPassableCells());
        for (int i = 0; i < v; i++) {
            graph.setEdge(i, map.getSurroundings(graph.getArray()[i]));
        }
    } // end of DijkstraPathFinder()


    @Override
    public List<Coordinate> findPath() {
        // You can replace this with your favourite list, but note it must be a
        // list type
        List<Coordinate> path = new ArrayList<Coordinate>();
        int min=graph.getArray().length;
        int indexOfOrigin=-1;
        int indexOfDest=-1;
        for(int i=0;i<origins.size();i++){
            for(int j=0;j<dests.size();j++){
                List<Coordinate> temp = new ArrayList<>(breathFirstSearch(graph,origins.get(i)));
                int num=temp.indexOf(dests.get(j));
                if(num<min){
                    min = num;
                    indexOfOrigin=i;
                    indexOfDest=j;
                }
            }
        } List<Coordinate> pathList = new ArrayList<>(breathFirstSearch(graph,origins.get(indexOfOrigin)));
        for(int i=0; i<=pathList.indexOf(dests.get(indexOfDest));i++){
            path.add(pathList.get(i));
        }

        // TODO: Implement

        return path;
    } // end of findPath()


    @Override
    public int coordinatesExplored() {
        // TODO: Implement (optional)

        // placeholder
        return 0;
    } // end of cellsExplored()

//    public void recursiveFind(Edge origin, Edge dest) {
//        edgePath.add(origin);
//        int indexOfOrigin = -1;
//        for (int i = 0; i < graph.getArray().length; i++) {
//            if (graph.getArray()[i].equals(origin.getC())) {
//                indexOfOrigin = i;
//                break;
//            }
//        }
//        if (!graph.getAdjListArray()[indexOfOrigin].contains(dest)) {
//            for (int i = 0; i < graph.getAdjListArray()[indexOfOrigin].size(); i++) {
//                recursiveFind(graph.getAdjListArray()[indexOfOrigin].get(i), dest);
//            }
//        } else {
//            edgePath.add(dest);
//        }
//    }

    public Set<Coordinate> breathFirstSearch(Graph graph, Coordinate origin) {
        Set<Coordinate> visited = new LinkedHashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(origin);
        visited.add(origin);
        while (!queue.isEmpty()) {
            Coordinate coordinate = queue.poll();
            int indexOfOrigin = -1;
            for (int i = 0; i < graph.getArray().length; i++) {
                if (graph.getArray()[i].equals(origin)) {
                    indexOfOrigin = i;
                    break;
                }
            }
            for (Edge edge : graph.getAdjListArray()[indexOfOrigin]) {
                if (!visited.contains(edge.getC())) {
                    visited.add(edge.getC());
                    queue.add(edge.getC());
                }
            }
        }
        return visited;
    }
} // end of class DijsktraPathFinder
