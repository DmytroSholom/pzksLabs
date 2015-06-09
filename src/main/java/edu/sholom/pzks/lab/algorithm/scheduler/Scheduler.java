package edu.sholom.pzks.lab.algorithm.scheduler;

import edu.sholom.pzks.lab.algorithm.scheduler.utils.CountedVertex;
import edu.sholom.pzks.lab.model.Edge;
import edu.sholom.pzks.lab.model.Vertex;
import edu.uci.ics.jung.graph.Graph;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Dmytro on 01.06.2015.
 */
public interface Scheduler {

    public List<ValuedVertex> run(Graph<Vertex, Edge> graph);


    public static class ValuedVertex{

        private Vertex vertex;

        private double value;

        public ValuedVertex(Vertex vertex){
            this.vertex = vertex;
        }

        public Vertex getVertex() {
            return vertex;
        }

        public void setVertex(Vertex vertex) {
            this.vertex = vertex;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public static int compareByValueDesc(ValuedVertex v1, ValuedVertex v2){
            return  v1.getValue() - v2.getValue()>0? -1: v1.getValue() - v2.getValue()<0? 1:0;
        }

        public static int compareByValueAsc(ValuedVertex v1, ValuedVertex v2){
            return  v1.getValue() - v2.getValue()>0? 1: v1.getValue() - v2.getValue()<0? -1:0;
        }
    }
}
