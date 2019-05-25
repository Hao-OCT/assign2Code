package pathFinder;

import map.Coordinate;

import java.util.LinkedList;

public class Graph {
    private int v;
    private LinkedList<Coordinate> adjListArray[];
    public Graph(int v){
        this.v=v;
        //the size of array of the number of vertex
        adjListArray = new LinkedList[v];
        for(int i=0; i<v;i++){
            adjListArray[i] = new LinkedList<>();
        }
    }

    public LinkedList<Coordinate>[] getAdjListArray() {
        return adjListArray;
    }
}
