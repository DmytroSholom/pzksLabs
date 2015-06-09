package edu.sholom.pzks.lab.model;

/**
 * Created by Dmytro on 31.05.2015.
 */
public class AutoGraphParams {
    private Integer vertixMinWeight;
    private Integer vertixMaxWeight;
    private Integer numOfVertices;
    private Double correlation;

    public Integer getVertixMinWeight() {
        return vertixMinWeight;
    }

    public void setVertixMinWeight(Integer vertixMinWeight) {
        this.vertixMinWeight = vertixMinWeight;
    }

    public Integer getVertixMaxWeight() {
        return vertixMaxWeight;
    }

    public void setVertixMaxWeight(Integer vertixMaxWeight) {
        this.vertixMaxWeight = vertixMaxWeight;
    }

    public Integer getNumOfVertices() {
        return numOfVertices;
    }

    public void setNumOfVertices(Integer numOfVertices) {
        this.numOfVertices = numOfVertices;
    }

    public Double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(Double correlation) {
        this.correlation = correlation;
    }
}
