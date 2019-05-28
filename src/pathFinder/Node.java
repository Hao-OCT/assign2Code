package pathFinder;

import map.Coordinate;

import java.util.Objects;

public class Node {
    private Node origin;
    private Coordinate coordinate;
    private int cost;

    public Node(Node origin, Coordinate coordinate, int cost) {
        this.origin = origin;
        this.coordinate = coordinate;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.coordinate.getRow(), this.coordinate.getColumn());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj != null) {
            if (this.coordinate.equals(((Node) obj).getCoordinate()))
                return true;
        }
        return false;
    }
}