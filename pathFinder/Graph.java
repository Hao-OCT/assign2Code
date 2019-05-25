package pathFinder;

import map.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    private int v;
    private LinkedList<Edge> adjListArray[];
    private Coordinate array[];
    public Graph(int v){
        this.v=v;
        //the size of array of the number of vertex
        adjListArray = new LinkedList[v];
        for(int i=0; i<v;i++){
            adjListArray[i] = new LinkedList<>();
        }
    }

    public void setArray(ArrayList<Coordinate> list){
        array = new Coordinate[v];
        for(int i=0;i<list.size();i++){
            array[i]=list.get(i);
        }
    }
    public void setEdge(int index ,ArrayList<Edge> edgeList){
        for(int i=0; i <edgeList.size();i++)
            adjListArray[index].add(edgeList.get(i));
    }

    public Coordinate[] getArray() {
        return array;
    }

    public LinkedList<Edge>[] getAdjListArray() {
        return adjListArray;
    }
}
