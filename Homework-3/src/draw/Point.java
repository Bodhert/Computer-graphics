/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draw;

/**
 *
 * @author alejo
 */
public class Point 
{
     public int x,y;
    
    public Point(int x, int y)
    {
        this.x = x; this.y = y; 
    }
    
    @Override public String toString()
    {
        return "(" +  x + "," + y + ")"; 
    }
    
    public int getX()
    {
     return this.x;
    }
    
    public int getY()
    {
     return this.y;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
}
