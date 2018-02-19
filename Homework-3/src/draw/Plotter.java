/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 *
 * @author alejo
 */
public class Plotter extends JPanel
{
    public static int width;
    public static int height;
    public static int pixelSize = 1;    
    
    static ArrayList<Edge> Edges = new ArrayList<>();
    static ArrayList<Point> Points = new ArrayList<>();
    
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        //here we draw every thing we want to show
        Graphics2D g2d = (Graphics2D) g; 

        g2d.setColor(Color.blue);

        // size es el tamaño de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los títulos de la ventana.
        Insets insets = getInsets();

        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;

        width = w;
        height = h;
        
       drawHouse(g2d);

    }
    
    public void drawHouse(Graphics2D g2d)
    {
        for(int i = 0; i < Edges.size();++i)
        {
            int posPoint1 = Edges.get(i).point1;
            int posPoint2 = Edges.get(i).point2;
            
            Point currPoint1 = Points.get(posPoint1);
            Point currPoint2 = Points.get(posPoint2);
            
//            System.out.println(currPoint1.x + " " + currPoint1.y + " " + 
//                    currPoint2.x + " " + currPoint2.y);
            drawLineBeetweenPoints(g2d, currPoint1.x, currPoint1.y, 
                    currPoint2.x, currPoint2.y);
        }
    }
    
    void drawLineBeetweenPoints(Graphics2D g2d,int x1,int y1, int x2, int y2)
    {
        Point p1 =  new Point(x1,y1);
        Point p2 =  new Point(x2,y2);
        drawBresenhamLine3(g2d, p1, p2);  
    }
    
    public void drawBresenhamLine3(Graphics2D g2d, Point p1, Point p2) 
    {
        // Transform p1 and p2
        viewportTransf(p1);
        viewportTransf(p2);
        drawBresenham(g2d, p1.x, p1.y, p2.x, p2.y);
    }
    
    public void viewportTransf(Point p) 
    {
        p.x += width / 2;
        p.y = height / 2 - p.y;
    }
    
    public void drawBresenham(Graphics2D g2d, int x1, int y1, int x2, int y2)
   {
       // delta of exact value and rounded value of the dependent variable
       int d = 0;

       int dx = Math.abs(x2 - x1);
       int dy = Math.abs(y2 - y1);

       int dx2 = 2 * dx; // slope scaling factors to
       int dy2 = 2 * dy; // avoid floating point

       int ix = x1 < x2 ? 1 : -1; // increment direction
       int iy = y1 < y2 ? 1 : -1;

       int x = x1;
       int y = y1;

       if (dx >= dy) 
       {
           while (true) 
           {
               g2d.drawOval(x, y, pixelSize,pixelSize);
               if (x == x2)
                   break;
               x += ix;
               d += dy2;
               if (d > dx) 
               {
                   y += iy;
                   d -= dx2;
               }
           }
       } 
       else 
       {
           while (true) 
           {
               g2d.drawOval(x, y, pixelSize,pixelSize);
               if (y == y2) break;
               y += iy;
               d += dx2;
               if (d > dy) 
               {
                   x += ix;
                   d -= dy2;
               }
           }
       }
    }
    
    public static void main(String[] args) throws FileNotFoundException 
    {
        //read routine
         System.setIn(new FileInputStream("in"));
         Scanner sc = new Scanner(System.in);
         
         int points = sc.nextInt();
         
     
         for(int i = 0; i < points; ++i)
         {
             int x,y;
             x = sc.nextInt(); y = sc.nextInt();
//             System.out.println(x + " " + y);
             Point curr = new Point(x,y);
             Points.add(curr);
         }
         
         int edges = sc.nextInt();
         
         for(int i = 0 ; i < edges; ++i)
         {
             int point1, point2;
             point1 = sc.nextInt(); point2 = sc.nextInt();
             Edge curr = new Edge(point1,point2);
             Edges.add(curr);
//             System.out.println(point1 + " " + point2);
         }
         
            // Crear un nuevo Frame
        JFrame frame = new JFrame("House");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new Plotter());
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
         
         
        
    }
    
}
