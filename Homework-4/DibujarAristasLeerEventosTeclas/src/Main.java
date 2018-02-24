
//import Matrix3x3.translation;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author htrefftz
 */
public class Main extends JPanel implements KeyListener {

    public static final int FRAME_WIDTH = 800;
    public static final int FRAME_HEIGHT = 800;
    
    PolygonObject po;
    
    
    int minX;
    int minY;
    int maxX;
    int maxY;
    
    boolean a = false; boolean d = false; boolean s = false;
    boolean w = false; boolean up = false; boolean down = false;
    boolean left = false; boolean right = false;
    
    
    boolean DEBUG = true;

    /**
     * Draw the axis and the object
     * @param g 
     */
    
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // El panel, por defecto no es "focusable". 
        // Hay que incluir estas líneas para que el panel pueda
        // agregarse como KeyListsener.
        this.setFocusable(true);
        this.requestFocusInWindow();


        this.addKeyListener(this);

        Graphics2D g2d = (Graphics2D) g;
        
        drawAxis(g2d);
        drawObject(g2d);
        
        //the increasing and decreasing, theht could be replaced by global values
        // I mean all this values could be global ones, exept for the zeroes
        if(a) translate(g2d, -50, 0);
        else if(d) translate(g2d, 50, 0);
        else if(w) translate(g2d,0,50);
        else if(s) translate(g2d,0,-50);
        else if(left) rotation(g2d, 45);
        else if(right) rotation(g2d, -45);
        else if(up) scaling(g2d,1.5,1.5);
        else if(down) scaling(g2d,0.3,0.3);
        
//        drawClippingArea(g2d);
    }

    /**
     * Draw the X and Y axis
     * @param g2d Graphics2D context
     */
    private void drawAxis(Graphics2D g2d) 
    {
        g2d.setColor(Color.red);
        drawEdge(g2d,new Point(0, -100), new Point(0, 100));
        g2d.setColor(Color.green);
        drawEdge(g2d,new Point(-100, 0), new Point(100, 0));
    }
    
    private void drawClippingArea(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        Point p0 = new Point(minX, minY);
        Point p1 = new Point(maxX, minY);
        Point p2 = new Point(maxX, maxY);
        Point p3 = new Point(minX, maxY);
        drawEdge(g2d, p0, p1);
        drawEdge(g2d, p1, p2);
        drawEdge(g2d, p2, p3);
        drawEdge(g2d, p3, p0);
    }
    
    /**
     * Draw the wire-frame object
     * @param g2d Graphics2D context
     */
    private void drawObject(Graphics2D g2d) 
    {
        g2d.setColor(Color.blue);
        for(Edge e: po.edges) 
        {
            Point p0 = e.p1;
            Point p1 = e.p2;
            drawEdge(g2d, p0, p1);
        }
    }
    
    private void translate(Graphics2D g2d, double x, double y) 
    {
//        System.out.println("me llame");
        g2d.setColor(Color.blue);
        for(Edge e: po.edges) 
        {
            Point p0 = Matrix3x3.translation(e.p1,x,y);
            Point p1 = Matrix3x3.translation(e.p2,x,y);
            
            //modifiying the original points to keep consitency of the draw
            e.p1 = p0;
            e.p2 = p1;
            drawEdge(g2d, p0, p1);
        }
    }
    
    private void rotation(Graphics2D g2d, double theta)
    {
     g2d.setColor(Color.blue);
     for(Edge e: po.edges)
     {
        Point p0 = Matrix3x3.rotate(e.p1, theta);
        Point p1 = Matrix3x3.rotate(e.p2, theta);
         
        e.p1 = p0;
        e.p2 = p1;
        drawEdge(g2d, p0, p1);
     }
    }
    
    private void scaling(Graphics2D g2d, double x, double y)
    {
        System.out.println("llame scaling");
        g2d.setColor(Color.blue);
        for(Edge e: po.edges) 
        {
            Point p0 = Matrix3x3.scale(e.p1,x,y);
            Point p1 = Matrix3x3.scale(e.p2,x,y);
            
            //modifiying the original points to keep consitency of the draw
            e.p1 = p0;
            e.p2 = p1;
            drawEdge(g2d, p0, p1);
        }
    }
    /**
     * Draw an edge from p0 to p1
     * p0 and p1 are in world coordinates, need to be tranformed
     * to the viewpoint.
     * @param g2d Graphics2D context
     * @param p0 first point
     * @param p1 second point
     */
    private void drawEdge(Graphics2D g2d,Point p0, Point p1) {
        double x0 = p0.x + FRAME_WIDTH/2;
        double y0 = FRAME_HEIGHT/2 - p0.y ;
        double x1 = p1.x + FRAME_WIDTH/2;
        double y1 = FRAME_HEIGHT/2 - p1.y;
        g2d.drawLine((int)x0,(int) y0, (int)x1,(int) y1);
    }

    /**
     * Read the object description:
     * n (number of vertices)
     * n vertices
     * m (number of edges)
     * m edges (index of first and second point to be linked)
     * @param fileName Name of the file to read the object description from
     */
    public void readObjectDescription(String fileName) 
    {
        Scanner in;
        po = new PolygonObject();
        try
        {
            in = new Scanner(new File(fileName));
            // Read the number of vertices
            int numVertices = in.nextInt();
            Point[] vertexArray = new Point[numVertices];
            // Read the vertices
            for (int i = 0; i < numVertices; i++) 
            {
                // Read a vertex
                int x = in.nextInt();
                int y = in.nextInt();
                vertexArray[i] = new Point(x, y);
            }
            // Read the number of edges
            int numEdges = in.nextInt();
            // Read the edges
            for (int i = 0; i < numEdges; i++) 
            {
                // Read an edge
                int start = in.nextInt();
                int end = in.nextInt();
                Edge edge = new Edge(vertexArray[start], vertexArray[end]);
                po.add(edge);
            }
            // Read clipping area
//            minX = in.nextInt();
//            minY = in.nextInt();
//            maxX = in.nextInt();
//            maxY = in.nextInt();
        } 
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }

    }
    
    @Override
    public void keyPressed(KeyEvent e) 
    {
        int tecla = e.getKeyCode();
        if(tecla == KeyEvent.VK_D) 
        {
            d = true; repaint();
            System.out.println("El usuario presiona d");
        } 
        else if (tecla == KeyEvent.VK_A) 
        {
            a = true; repaint();
            System.out.println("El usuario presiona a");
        } 
        else if (tecla == KeyEvent.VK_W) 
        {
            w = true; repaint();
            System.out.println("El usuario presiona w");
        } 
        else if (tecla == KeyEvent.VK_S) 
        {
            s = true; repaint();
            System.out.println("El usuario presiona s");
        } 
        else if (tecla == KeyEvent.VK_UP) 
        {
            up = true; repaint();
            System.out.println("El usuario presiona up");
        } 
        else if (tecla == KeyEvent.VK_DOWN) 
        {
            down = true; repaint();
            System.out.println("El usuario presiona down");
        } 
        else if (tecla == KeyEvent.VK_LEFT) 
        {
            left = true; repaint();
            System.out.println("El usuario presiona left");
        } 
        else if (tecla == KeyEvent.VK_RIGHT) 
        {
            right = true; repaint();
            System.out.println("El usuario presiona right");
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();
        if(tecla == KeyEvent.VK_D) 
        {
            d = false; repaint();
            System.out.println("El usuario presiona d");
        } 
        else if (tecla == KeyEvent.VK_A) 
        {
            a = false; repaint();
            System.out.println("El usuario presiona a");
        } 
        else if (tecla == KeyEvent.VK_W) 
        {
            w = false; repaint();
            System.out.println("El usuario presiona w");
        } 
        else if (tecla == KeyEvent.VK_S) 
        {
            s = false; repaint();
            System.out.println("El usuario presiona s");
        } 
        else if (tecla == KeyEvent.VK_UP) 
        {
            up = false; repaint();
            System.out.println("El usuario presiona up");
        } 
        else if (tecla == KeyEvent.VK_DOWN)
        {
            down = false;  repaint();
            System.out.println("El usuario presiona down");
        } 
        else if (tecla == KeyEvent.VK_LEFT) 
        {
            left = false; repaint();
            System.out.println("El usuario presiona left");
        } 
        else if (tecla == KeyEvent.VK_RIGHT) 
        {
            right = false; repaint();
            System.out.println("El usuario presiona right");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    
    
    
    /**
     * Main program
    */
    public static void main(String[] args) {
        Main m = new Main();

        // Read the file with the object description
        m.readObjectDescription("objeto.txt");

        // Create a new Frame
        JFrame frame = new JFrame("Wire Frame Object");
        // Upon closing the frame, the application ends
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a panel called DibujarCasita3D
        frame.add(m);

        // Asignarle tamaño
        frame.setSize(Main.FRAME_WIDTH, Main.FRAME_HEIGHT);
        // Put the frame in the middle of the window
        frame.setLocationRelativeTo(null);
        // Show the frame
        frame.setVisible(true);
    }

}
