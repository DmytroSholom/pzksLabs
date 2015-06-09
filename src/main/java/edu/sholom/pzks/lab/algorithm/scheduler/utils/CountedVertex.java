package edu.sholom.pzks.lab.algorithm.scheduler.utils;

import edu.sholom.pzks.lab.model.Vertex;

/**
 * Created by Dmytro on 01.06.2015.
 */
public class CountedVertex {

    private Vertex vertex;

    private int maxWToEnd;

    private int maxWToStart;

    private int maxNToEnd;

    private int maxNToStart;

    public CountedVertex(Vertex vertex){
        this.vertex = vertex;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public int getMaxWToEnd() {
        return maxWToEnd;
    }

    public void setMaxWToEnd(int maxWToEnd) {
        this.maxWToEnd = maxWToEnd;
    }

    public int getMaxWToStart() {
        return maxWToStart;
    }

    public void setMaxWToStart(int maxWToStart) {
        this.maxWToStart = maxWToStart;
    }

    public int getMaxNToEnd() {
        return maxNToEnd;
    }

    public void setMaxNToEnd(int maxNToEnd) {
        this.maxNToEnd = maxNToEnd;
    }

    public int getMaxNToStart() {
        return maxNToStart;
    }

    public void setMaxNToStart(int maxNToStart) {
        this.maxNToStart = maxNToStart;
    }

    @Override
    public String toString(){
        return "id: " + this.vertex.getIndex() + " weight: " + this.vertex.getWeight() +
                " maxWToStart: " + maxWToStart + " maxNToStart: " + maxNToStart +
                " maxWToEnd: " + maxWToEnd + " maxNToEnd: " + maxNToEnd;
    }
}
