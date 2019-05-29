package pathFinder;

import map.Coordinate;
import map.PathMap;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

/* Source code for Algorithms & Analysis Assignment 2
   RMIT Uni S1 2019,
   @authors: Hao Wang(s3690173), Qiuyu Chen(s3739209)
   implement Dijkstra's Algorithms to find the shortest path.
 */
public class DijkstraPathFinder implements pathFinder.PathFinder {
    private PathMap map;
    private int coordinatesExplored;
    //use outStream to test
    protected static final PrintStream outStream = System.out;

    public DijkstraPathFinder(PathMap map) {
        this.map = map;
        this.coordinatesExplored = 0;
    } // end of DijkstraPathFinder()


    @Override
    public List<Coordinate> findPath() {
        List<Coordinate> path = new ArrayList<Coordinate>();
        List<List<Coordinate>> potentialPath = new ArrayList<List<Coordinate>>();
        List<List<Coordinate>> finalPath = new ArrayList<>();
        if (map.waypointCells.isEmpty()) {
            //Task C : Handling multiple origins and destinations
            //How to test: Don't add -w waypoints1.para in the command line
            for (int i = 0; i < map.originCells.size(); i++) {
                //Multiple origins
                pathFinder.Node current = new pathFinder.Node(null, map.originCells.get(i), 1);
                for (int j = 0; j < map.destCells.size(); j++) {
                    //Multiple destinations
                    Node dest = new Node(null, map.destCells.get(j), 1);
                    path = findAtoB(current, dest);
                    potentialPath.add(path);
                }
            }
            //Sort the potentialPath to make the shortest path at the index of 0
            Collections.sort(potentialPath, new Comparator<List<Coordinate>>() {
                @Override
                public int compare(List<Coordinate> o1, List<Coordinate> o2) {
                    return o1.size() - o2.size();
                }
            });
            return potentialPath.get(0);
        } else {
            //Task D: Handling waypoints
            //To test Task D: You should put waypoints.para into the command line
            for (int j = 0; j < map.originCells.size(); j++) {
                //Multiple origins
                for (int i = 0; i < map.waypointCells.size(); i++) {
                    //Multiple waypoints
                    //Each origin has a shortest path to one of the waypoint
                    path = findAtoB(new Node(null, map.originCells.get(j), 1), new Node(null, map.waypointCells.get(i), 1));
                    potentialPath.add(path);
                }
            }
            Collections.sort(potentialPath, new Comparator<List<Coordinate>>() {
                @Override
                public int compare(List<Coordinate> o1, List<Coordinate> o2) {
                    return o1.size() - o2.size();
                }
            });
            //Add the shortes path of one of the origins and one of the waypoints
            // to the finalPath which would be part of the retrun value
            finalPath.add(potentialPath.get(0));
            //outStream.println(potentialPath.size());
            if (map.waypointCells.size() != 1) {
                //if the number of waypoints is greater than 1
                Node node = new Node(null, potentialPath.get(0).get(potentialPath.get(0).size() - 1), 1);
                potentialPath.clear();
                //Go through every waypoints except the one we picked before,
                //make the one we picked before as a origin and make each of the rest of the
                //waypoints as a destination, and link all the point in the shortest way.
                finalPath.add(throughtWaypoint(node));

                //Now link the destination and the end of waypoints line
                Node lastWaypoint = new Node(null, finalPath.get(1).get(finalPath.get(1).size() - 1), 1);
                for (int i = 0; i < map.destCells.size(); i++) {
                    //Multiple destinations
                    path = findAtoB(lastWaypoint, new Node(null, map.destCells.get(i), 1));
                    potentialPath.add(path);
                }
                Collections.sort(potentialPath, new Comparator<List<Coordinate>>() {
                    @Override
                    public int compare(List<Coordinate> o1, List<Coordinate> o2) {
                        return o1.size() - o2.size();
                    }
                });finalPath.add(potentialPath.get(0));
            }
            //outStream.println(lastWaypoint.getCoordinate());
            else {
                //if the number of waypoints is exactly one
                Node node = new Node(null, potentialPath.get(0).get(potentialPath.get(0).size() - 1), 1);
                potentialPath.clear();
                for (int i = 0; i < map.destCells.size(); i++) {
                    path = findAtoB(node, new Node(null, map.destCells.get(i), 1));
                    potentialPath.add(path);
                }Collections.sort(potentialPath, new Comparator<List<Coordinate>>() {
                    @Override
                    public int compare(List<Coordinate> o1, List<Coordinate> o2) {
                        return o1.size() - o2.size();
                    }
                });finalPath.add(potentialPath.get(0));
            }
            return finalPath.stream().flatMap(List::stream).collect(Collectors.toList());
        }
    } // end of findPath()

    public List<Coordinate> findAtoB(Node A, Node B) {
        //Task B: Handling different terrain
        //How to test: remove the -w waypoints1.para and
        //make sure only one destination and one origin
        //in the map.
        List<Coordinate> path = new ArrayList<Coordinate>();
        Comparator<Node> nodeComparator = (node1, node2) -> node1.getCost() - node2.getCost();
        PriorityQueue<Node> pq = new PriorityQueue<>(nodeComparator);
        List<Node> nodeList = new ArrayList<>();
        Node current = A;
        Node dest = B;
        while (!current.equals(dest)) {
            int r = current.getCoordinate().getRow();
            int c = current.getCoordinate().getColumn();

            //Generate the surrounding coordinate of the current node
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
        return path;
    }

    public List<Coordinate> throughtWaypoint(Node source) {
        // The purpose of this method is to link all the waypoints in the shortest way
        List<Node> waypointList = new ArrayList<Node>();
        List<Coordinate> path = new ArrayList<Coordinate>();
        List<List<Coordinate>> potentialPath = new ArrayList<List<Coordinate>>();
        List<List<Coordinate>> finalPath = new ArrayList<>();
        for (int i = 0; i < map.waypointCells.size(); i++) {
            waypointList.add(new Node(null, map.waypointCells.get(i), 1));
        }
        while (waypointList.size() != 0) {
            waypointList.remove(source);
            for (int i = 0; i < waypointList.size(); i++) {
                path = findAtoB(source, waypointList.get(i));
                potentialPath.add(path);
            }
            Collections.sort(potentialPath, new Comparator<List<Coordinate>>() {
                @Override
                public int compare(List<Coordinate> o1, List<Coordinate> o2) {
                    return o1.size() - o2.size();
                }
            });
            finalPath.add(potentialPath.get(0));
            source = new Node(null, potentialPath.get(0).get(potentialPath.get(0).size() - 1), 1);
            if (waypointList.size() == 1)
                break;
            potentialPath.clear();

        }
        return finalPath.stream().flatMap(List::stream).collect(Collectors.toList());
    }


    public void checkNode(PriorityQueue<Node> pq, List<Node> nodeList, Coordinate coordinate, Node current) {
        Node temp;
        if (map.isPassable(coordinate.getRow(), coordinate.getColumn()) && map.isIn(coordinate)) {
            //Task A
            //How to test: remove -t terrain1.para -w waypoints1.para in the
            // command line and make sure only one origin and one destination
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

        return this.coordinatesExplored;
    } // end of cellsExplored()

} // end of class DijsktraPathFinder

