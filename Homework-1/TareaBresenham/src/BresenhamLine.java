/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 * Project 1
 * Students have to replace method drawBresenhamLine with an implementation
 * of the Bresenham algorithm.
 * @author htrefftz
 */
public class BresenhamLine extends JPanel {

    public static final int STEP = 45;
    public static final int STEPAxis = 90;
    public static final int R = 200; // change it for prefered size

    public static int width;
    public static int height;
    public static int pixelSize = 1;

    /**
     * Draw the lines. This is called by Java whenever it is necessary to draw
     * (or redraw) the panel
     * @param g Graphics context.
     */
    @Override
    public void paintComponent(Graphics g) {
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
        
//        drawPlane(g2d);
//        drawOctants(g2d);   
//        drawLineBeetweenPoints(g2d);
        drawConectivityGraph(g2d, 12); 
    }
    
    
    // given a n, it gives you an list of points with uniform distribution  in the plane 
    ArrayList<MyPoint> calculateDistribution(int n)
    {
        ArrayList<MyPoint> uniformPoints = new ArrayList<>(); 
        int distribution = 360/n;
        for(int angle = 0; angle < 360; angle += distribution)
        {
            double angleD = angle * Math.PI / 180d;
             MyPoint currPoint = new MyPoint(
                    (int) (R * Math.cos(angleD)),
                    (int) (R * Math.sin(angleD))
            );
             uniformPoints.add(currPoint);
        }
        
        return uniformPoints;
    }
    
    
    //given the nodes, it conects every single node in the plane 
    void drawConectivityGraph(Graphics2D g2d,int nodes)
    {
        
        ArrayList<MyPoint> uniformPoints = new ArrayList<>(); 
        uniformPoints = calculateDistribution(nodes);
        for(int i = 0 ; i < uniformPoints.size(); ++i)
        {
            MyPoint current =  uniformPoints.get(i);
//            System.out.println( current.x + " " + current.y);  
            for(int j = i+1; j < uniformPoints.size(); ++j)
            {
                MyPoint next = uniformPoints.get(j);
                drawLineBeetweenPoints(g2d, current.x,current.y, next.x,next.y);
            }
        }
        
    }
    
    // self describe Method, auxiliar to draw lines, helper
    void drawLineBeetweenPoints(Graphics2D g2d,int x1,int y1, int x2, int y2)
    {
        MyPoint p1 =  new MyPoint(x1,y1);
        MyPoint p2 =  new MyPoint(x2,y2);
        drawBresenhamLine3(g2d, p1, p2);  
    }
    
    
    // simply draws every octant in the plane
    void drawOctants(Graphics2D g2d)
    {
        for (int angle = 22; angle < 360; angle += STEP) 
        {
            double angleD = angle * Math.PI / 180d;
            MyPoint p1 = new MyPoint(0, 0);
            MyPoint p2 = new MyPoint(
                    (int) (R * Math.cos(angleD)),
                    (int) (R * Math.sin(angleD))
            );
            drawBresenhamLine3(g2d, p1, p2);
            
        }
        
    }
  
                      
    // Draws the x and y axis
    void drawPlane(Graphics2D g2d)
    {
           for (int angle = 0; angle < 360; angle += STEPAxis) 
        {
            double angleD = angle * Math.PI / 180d;
            MyPoint p1 = new MyPoint(0, 0);
            MyPoint p2 = new MyPoint(
                    (int) (R * Math.cos(angleD)),
                    (int) (R * Math.sin(angleD))
            );
            
            drawBresenhamLine3(g2d, p1, p2);
            
        }
    }

    /** 
     * This has to be changed to an implementation of the Bresenham line
     * @param g2d graphics context
     * @param p1 beginning point of the line
     * @param p2 end point of the line
     */
    public void drawBresenhamLine3(Graphics2D g2d, MyPoint p1, MyPoint p2) {

        // Transform p1 and p2
        viewportTransf(p1);
        viewportTransf(p2);

        // draw the line
        //g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        drawBresenham(g2d, p1.x, p1.y, p2.x, p2.y);

    }

    /**
     * Transforms a point and then draws it on the panel
     * @param p point to be drawn
     * @param g2d graphics context
     */
//    public void drawPoint(MyPoint p, Graphics2D g2d) {
//        viewportTransf(p);
//        g2d.drawLine(p.x, p.y, p.x, p.y);
//    }

    /**
     * Transform a point to java coordinates: X grows from left to right and
     * Y grows from top to bottom
     * @param p Point to be transformed
     */
    public void viewportTransf(MyPoint p) 
    {
        p.x += width / 2;
        p.y = height / 2 - p.y;
    }
    
    //draws a line beetwen x1,y1 -> x2,y2
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
    /**
     * Main program
     * @param args Not used in this case
     */
    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Bresenham");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new BresenhamLine());
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }

}
