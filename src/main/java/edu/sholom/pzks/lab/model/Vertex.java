package edu.sholom.pzks.lab.model;

/**
 * Created by Dmytro on 21.05.2015.
 */
public class Vertex {
    private int weight;
    private int index;
    private String prefix;
    private double x;
    private double y;

    public Vertex(String prefix, int index, int weight){
        this.prefix = prefix;
        this.index = index;
        this.weight = weight;
    }

    @Override
    public String toString(){
        return prefix + index + " {W:" + weight + "}";
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (index != vertex.index) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return index;
    }
}
