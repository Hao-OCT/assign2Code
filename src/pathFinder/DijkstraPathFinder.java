package pathFinder;

import map.Coordinate;
import map.PathMap;

import java.util.*;


public class DijkstraPathFinder implements PathFinder {
    // TODO: You might need to implement some attributes
    private PathMap map;
    private int coordinatesExplored;

    // represent a map to a graph first
    public DijkstraPathFinder(PathMap map) {
        // TODO :Implement
        this.map = map;
        this.coordinatesExplored = 0;
    } // end of DijkstraPathFinder()


    @Override
    public List<Coordinate> findPath() {
        List<Coordinate> path = new ArrayList<Coordinate>();
        List<List<Coordinate>> potentialPath = new ArrayList<List<Coordinate>>();
        //Comparator<Node> nodeComparator = (node1, node2) -> node1.getCost() - node2.getCost();
        //Create a priority queue to store the node in the order of cost.
        //PriorityQueue<Node> pq = new PriorityQueue<>(nodeComparator);
        //List<Node> nodeList = new ArrayList<>();
        int numberOfWayPoint = map.waypointCells.size();
        for (int i = 0; i < map.originCells.size(); i++) {
            Node current = new Node(null, map.originCells.get(i), 1);
            for (int j = 0; j < map.destCells.size(); j++) {
                Node dest = new Node(null, map.destCells.get(j), 1);
                path = findAtoB(current,dest);
                potentialPath.add(path);
            }
        }
		Collections.sort(potentialPath, new Comparator<List<Coordinate>>() {
			@Override
			public int compare(List<Coordinate> o1, List<Coordinate> o2) {
				return o1.size() - o2.size();
			}
		});
        return potentialPath.get(0);
    } // end of findPath()
    
    public List<Coordinate> findAtoB( Node A, Node B) 
    {
    	List<Coordinate> path = new ArrayList<Coordinate>();
    	 Comparator<Node> nodeComparator = (node1, node2) -> node1.getCost() - node2.getCost();
    	PriorityQueue<Node> pq = new PriorityQueue<>(nodeComparator);
        List<Node> nodeList = new ArrayList<>();
    	Node current = A;
    	Node dest = B;
    	while (!current.equals(dest)) {
            int r = current.getCoordinate().getRow();
            int c = current.getCoordinate().getColumn();

            Coordinate up = new Coordinate(r + 1, c);
            Coordinate down = new Coordinate(r - 1, c);
            Coordinate left = new Coordinate(r, c - 1);
            Coordinate right = new Coordinate(r, c + 1);

            checkNode(pq, nodeList, up, current);
            checkNode(pq, nodeList, down, current);
            checkNode(pq, nodeList, left, current);
            checkNode(pq, nodeList, right, current);

            if (!pq.isEmpty()) {
                nodeList.add(current);
                current = pq.remove();
            } else break;
        }
        if (current.equals(dest)) {
            do {
                path.add((current.getCoordinate()));
                current = current.getOrigin();
            } while (current.getOrigin() != null);

            //put the origin into the path
            path.add(A.getCoordinate());
            Collections.reverse(path);
        }
        // TODO: Implement
        return path;
    }

    public void checkNode(PriorityQueue<Node> pq, List<Node> nodeList, Coordinate coordinate, Node current) {
        Node temp;
        if (map.isPassable(coordinate.getRow(), coordinate.getColumn()) && map.isIn(coordinate)) {
            temp = new Node(current, coordinate, current.getCost() + map.cells[coordinate.getRow()][coordinate.getColumn()].getTerrainCost());
            if (!nodeList.contains(temp)) {
                if (!pq.contains(temp)) {
                    pq.add(temp);
                    this.coordinatesExplored++;
                } else {
                    //traversal the priority queue
                    Iterator<Node> iterator = pq.iterator();
                    Node node;
                    do {
                        node = iterator.next();
                    } while (node != null && !node.equals(temp));

                    //choose the one cost less
                    if (node.getCost() > temp.getCost()) {
                        pq.remove(node);
                        node.setCost(temp.getCost());
                        node.setOrigin(current);
                        pq.add(node);
                    }
                }
            }
        }
    }

    @Override
    public int coordinatesExplored() {
        // TODO: Implement (optional)

        // placeholder
        return this.coordinatesExplored;
    } // end of cellsExplored()

} // end of class DijsktraPathFinder

