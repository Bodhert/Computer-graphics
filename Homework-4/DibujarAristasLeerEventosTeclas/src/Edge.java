/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Keeps the information of an Edge
 * @author htrefftz
 */
public class Edge {
    Point p1;
    Point p2;

    public Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        return "Edge{" + "p1=" + p1 + ", p2=" + p2 + '}';
    }
    
    
}
