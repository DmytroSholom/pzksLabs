package edu.sholom.pzks.lab.model;

/**
 * Created by Dmytro on 21.05.2015.
 */
public class Edge {
    private double weight;
    private int index;
    private String prefix;

    public Edge(String prefix, int index, double weight){
        this.prefix = prefix;
        this.index = index;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return prefix + index + " {W:" + weight + "}";
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


}
