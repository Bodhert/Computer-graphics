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
public class Homework3 {

    /**
     * @param args the command line arguments
     */
    
    
    //testing the metods
    public static void main(String[] args) {
        
        double[][] matrixA = new double[][]
        {
            {3.3, 4.6 , 0.0},
            {7.0 , 7.0, 100.6},
            {456.0 , 1.0 , 1.55}
        };
        
        double[][] matrixB = new double[][]
        {
            {3.39, 9.434 , 6.0},
            {1000.6 , -90.8, 0.0009},
            {4.0 , 9.0 , -243.21}
        };
        
        
        Point2 myPoint = new Point2(5, 6, 8);
        
        Vector3 myVec1 = new Vector3(100.0,9,999.32);
        Vector3 myVec2 = new Vector3(6,0.86,1987.3);
        
        //first test Matrix vs Point
        Point2 ans = Matrix3x3.times(matrixA, myPoint);
        System.out.println(ans.toString() + '\n');
        
        //second test: Matrix vs Matrix
        System.out.println("");
        Matrix3x3.toString(Matrix3x3.times(matrixA, matrixB));
        
        //third test : cross Product
        System.out.println(Vector3.crossProduct(myVec1, myVec2).toString() + '\n');
        
        
        //fourth test : dot product
        System.out.println(Vector3.dotProduct(myVec1, myVec2));
        System.out.println("");
        
        //fith test : unitary vector
        myVec1.normalize();
        System.out.println(myVec1.toString());
        
        
                
    }
    
}
