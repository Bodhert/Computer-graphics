/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

/**
 *
 * @author alejo
 */
public class Point2 
{
    protected double x,y,w;
    
    public Point2(double x, double y, double w)
    {
        this.x = x; this.y = y; this.w = w;
    }
    
    @Override public String toString()
    {
        return "(" +  x + "," + y + "," + w + ")";
    }
    
    public double getX()
    {
     return this.x;
    }
    
    public double getY()
    {
     return this.y;
    }
    
    public double getW()
    {
     return this.w;
    }
    
    public void setX(double x)
    {
        this.x = x;
    }
    
    public void setY(double y)
    {
        this.y = y;
    }
    
    public void setW(double w)
    {
        this.w = w;
    }
}
