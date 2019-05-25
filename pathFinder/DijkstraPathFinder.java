package pathFinder;

import map.Coordinate;
import map.PathMap;

import java.util.*;

public class DijkstraPathFinder implements PathFinder {
    // TODO: You might need to implement some attributes
private Graph graph = null;
    // represent a map to a graph first
    public DijkstraPathFinder(PathMap map) {
        // TODO :Implement
        // the number of vertex in the graph is the cells of the grid - the cells that are impassable
            int v = map.sizeR * map.sizeC - map.numberOfImpassable();
            graph = new Graph(v);
    } // end of DijkstraPathFinder()


    @Override
    public List<Coordinate> findPath() {
        // You can replace this with your favourite list, but note it must be a
        // list type
        List<Coordinate> path = new ArrayList<Coordinate>();

        //The first origin to the first dest.

        // TODO: Implement

        return path;
    } // end of findPath()


    @Override
    public int coordinatesExplored() {
        // TODO: Implement (optional)

        // placeholder
        return 0;
    } // end of cellsExplored()


} // end of class DijsktraPathFinder
