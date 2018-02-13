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
public class Vector3 
{
    protected double x,y,z;
    
    public Vector3(double x,double y, double z)
    {
        this.x = x; this.y = y; this.z = z;
    }
    
    public double magnitude()
    {
        return Math.sqrt((x*x) + (y*y) + (z*z));
    }
    
    
    public static Vector3 crossProduct(Vector3 v1 , Vector3 v2)
    {
        double i = ((v1.getY()*v2.getZ()) - (v2.getY()*v1.getZ()));
        double j = -((v1.getX()*v2.getZ()) - (v2.getX()*v1.getZ()));
        double k = ((v1.getX()*v2.getY())- (v2.getX()*v1.getY()));
        
        Vector3 result = new Vector3(i,j,k);
        
        return result;
    }
    
    public static double dotProduct(Vector3 v1, Vector3 v2)
    {
        return ((v1.getX()*v2.getX()) + (v1.getY()*v2.getY()) + (v1.getZ()*v2.getZ()));
    }
    
    //dont forget to check division by 0
    public void normalize()
    {
         double magnitude = magnitude();
         if(magnitude != 0) {x /= magnitude; y /= magnitude; z/=magnitude;}
         else 
         System.out.println("division by 0 is not allowed");
    }
    
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public double getZ()
    {
        return z;
    }
    
    @ Override public String toString()
    {
        return "<" + x + " , " + y +  " , " + z + ">";
    }
}
