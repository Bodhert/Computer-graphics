/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 *
 * @author alejo
 */



public class Matrix3x3 
{
    private static int matSize = 3;
    
  
    
    //I'm supossing that both has the apropiate dimencion for the multiplication;
    //this method is too strict , it would be good to do it generic;
    public static Point2 times(double[][] matrix, Point2 point2)
    {
        Point2 result = new Point2(0,0,0); // inicializing a null point that contains nothing for addig up values
        for(int i = 0; i < matSize; ++i)
        {
            double aux = 0;
            for(int j = 0; j < matSize; ++j)
            {
                double currentMat = matrix[i][j];
                if(j == 0) aux += (currentMat*point2.getX());
                else if(j == 1) aux += (currentMat*point2.getY());
                else if( j == 2) aux += (currentMat*point2.getW());
            }
            
            if(i == 0) result.setX(aux);
            else if(i == 1) result.setY(aux);
            else if(i == 2) result.setW(aux);
        }
        return result;
    }
    
    
    public static double[][] times(double matrix1[][], double matrix2[][])
    {
        int row1 = matrix1.length; int col1 = matrix1[0].length;
        int row2 = matrix2.length; int col2 = matrix2[0].length;
      
        if(col1 != row2) return null;
        double [][] result = new double [row1][col2];          
            for(int i = 0; i < row1;i++)
            {
                for(int j = 0; j < col2; j++)
                {

                    for(int k = 0; k < col1; k++)
                    {
                        result[i][j] += matrix1[i][k]*matrix2[k][j];
                    }
                }
            }
        
        
        return result;
        
    }
    
         
    
   public static void toString(double matrix[][])
   {
       String result = "";
       int rows = matrix.length;
       int colums = matrix[0].length;
       for(int i = 0; i < rows; ++i)
       {
           for(int j = 0; j < colums; ++j)
               result += String.format("%11.2f", matrix[i][j]);
        result += '\n';
       }
       
       System.out.println(result); 
   }
   
   public static Point translation(Point point, double traslationStepX,
           double traslationStepY)
   {
       //check if would be better to change 3 for a globla static variable
       double translationMatrix[][] = {
                                       {1,0,traslationStepX},
                                       {0,1,traslationStepY},
                                       {0,0,1}
                                      };
       
       //converting to the 3d point implemetation for calling times
       Point2 point2 = new Point2(point.x, point.y, 1.0);
       Point2 temp = times(translationMatrix,point2);
       
       Point ans = new Point(temp.getX(), temp.getY());
       return ans;
       
   }
   
   public static Point rotate(Point point, double angle)
   {
    double RotationMatrix[][] = {
                                  {Math.cos(angle),-Math.sin(angle),0},
                                  {Math.sin(angle),Math.cos(angle),0},
                                  {0,0,1}
                                };
    //converting to the 3d point implemetation for calling times
    Point2 point2 = new Point2(point.x, point.y, 1.0);
    Point2 temp = times(RotationMatrix,point2);
    
    Point ans = new Point(temp.getX(), temp.getY());
    return ans;
   }
   
   public static Point scale(Point point, double xScaling, double yScaling)
   {
    double scalingMatrix[][] = {
                                  {xScaling,0,0},
                                  {0,yScaling,0},
                                  {0,0,1}
                                };
    //converting to the 3d point implemetation for calling times
    Point2 point2 = new Point2(point.x, point.y, 1.0);
    Point2 temp = times(scalingMatrix,point2);
    
    Point ans = new Point(temp.getX(), temp.getY());
    return ans;
   }
    
    
}
