/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 * Keeps the information of a wire-frame object.
 * Edges are stored in an ArrayList
 * @author htrefftz
 */
public class PolygonObject {
    ArrayList<Edge> edges;
    
    public PolygonObject() {
        edges = new ArrayList<>();
    }
    
    public void add(Edge edge) {
        edges.add(edge);
    }
    
}
