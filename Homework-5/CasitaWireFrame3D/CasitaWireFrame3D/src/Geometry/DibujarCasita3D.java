package Geometry;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import Math.Matrix4x4;
import Math.RotationX;
import Math.RotationZ;
import Math.RotationY;
import Math.Vector4;
import Math.Projection;
import Math.Scaling;
import Math.Translation;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This example reads the description of an object (a polygon) from a file
 * and draws it on a jPanel
 * 
 * @author htrefftz
 */
public class DibujarCasita3D extends JPanel implements KeyListener {

    /**
     * Original (untransformed) PolygonObject
     */
    PolygonObject po;
    /**
     * Transformed object to be drawn
     */
    PolygonObject transformedObject;
    
    /**
     * Current transformations.
     * This is the accumulation of transformations done to the object
     */
    Matrix4x4 currentTransformation = new Matrix4x4();
    Matrix4x4 pivotPoint = new Matrix4x4(); // construir con los puntos medios
    
    

    public static int FRAME_WIDTH = 600;
    public static int FRAME_HEIGHT = 400;
    
    public static int AXIS_SIZE = 20;

    Dimension size;
    Graphics2D g2d;
    /**
     * Distance to the projection plane.
     */
    int proyectionPlaneDistance;
    
    int xmed , ymed , zmed; 

    public DibujarCasita3D() {
        super();
        // El panel, por defecto no es "focusable". 
        // Hay que incluir estas líneas para que el panel pueda
        // agregarse como KeyListsener.
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);          
    }
    
    /**
     * This method draws the object.
     * The graphics context is received in variable Graphics.
     * It is necessary to cast the graphics context into Graphics 2D in order
     * to use Java2D.
     * @param g Graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g2d = (Graphics2D) g;
        // Size of the window.
        size = getSize();
        
        // Draw the X axis
        g2d.setColor(Color.RED);
        drawOneLine(-DibujarCasita3D.AXIS_SIZE, 0, DibujarCasita3D.AXIS_SIZE, 0);

        // Draw the Y axis
        g2d.setColor(Color.GREEN);
        drawOneLine(0, -DibujarCasita3D.AXIS_SIZE, 0, DibujarCasita3D.AXIS_SIZE);

        // Draw the polygon object
        g2d.setColor(Color.BLUE);
        //po.drawObject(this);
        
        // Transform the object
        transformObject();
        
        // Apply UVN matrix
        applyUVN();
        
        // Apply projection
        applyProjection();

        // Draw the object
        transformedObject.drawObject(this);
        
    }
    
    /**
     * Apply the current transformation to the original object.
     * currentTransformation is the accumulation of the transforms that
     * the user has entered.
     */
    private void transformObject() {
        transformedObject = PolygonObject.transformObject(po, currentTransformation);
    }
    
    /**
     * Based on the position and orientation of the camera, create and apply
     * the UVN matrix.
     */
    private void applyUVN() {
        
    }
    
    /**
     * Create and apply the projection matrix
     * First: create the projection matrix.
     * Then 
     * The parameter is the negative value of the distance from the
     * origin to the projection plane (see constant above)
     */
    private void applyProjection() {
        // Create the projection matrix
        // The parameter is the negative value of the
        // distance from the origin to the projection plane
        Projection proj = new Projection(- proyectionPlaneDistance);
        // Apply the projection using method transformObject in PolygonObject
        // The input object is transformedObject
        // The outout object is also transformedObject
        // The transformation to be applyed is the projection matrix
        // just created
        transformedObject = PolygonObject.transformObject(transformedObject, proj);
    }

    /**
     * This function draws one line on this JPanel.
     * A mapping is done in order to:
     * - Have the Y coordinate grow upwards
     * - Have the origin of the coordinate system in the middle of the panel
     *
     * @param x1 Starting x coordinate of the line to be drawn
     * @param y1 Starting y coordinate of the line to be drawn
     * @param x2 Ending x coordinate of the line to be drawn
     * @param y2 Ending x coordinate of the line to be drawn
     */
    public void drawOneLine(int x1, int y1, int x2, int y2) {

        x1 = x1 + size.width / 2;
        x2 = x2 + size.width / 2;

        y1 = size.height / 2 - y1;
        y2 = size.height / 2 - y2;

        g2d.drawLine(x1, y1, x2, y2);
    }

    /**
     * Read the description of the object from the given file
     * @param fileName Name of the file with the object description
     */
    public void readObjectDescription(String fileName) {
        Scanner in;
        po = new PolygonObject();
        
        // variables for find the middle point
        int xmin = (int) Double.POSITIVE_INFINITY;
        int xmax = (int) Double.NEGATIVE_INFINITY;
        
        int ymin = (int) Double.POSITIVE_INFINITY;
        int ymax = (int) Double.NEGATIVE_INFINITY;
        
        int zmin = (int) Double.POSITIVE_INFINITY;
        int zmax = (int) Double.NEGATIVE_INFINITY;
        
        try {
            in = new Scanner(new File(fileName));
            // Read the number of vertices
            int numVertices = in.nextInt();
            Vector4[] vertexArray = new Vector4[numVertices];
            // Read the vertices
            for (int i = 0; i < numVertices; i++) {
                // Read a vertex
                int x = in.nextInt();
                int y = in.nextInt();
                int z = in.nextInt();
                
                xmin = Math.min(xmin, x);
                xmax = Math.max(xmax, x);
                
                ymin = Math.min(ymin, y);
                ymax = Math.max(ymax, y);
                
                zmin = Math.min(zmin, z);
                zmax = Math.max(zmax, z);
                
                vertexArray[i] = new Vector4(x, y, z);
            }
            
            System.out.println("xmin: " + xmin + " xmax:" + xmax);
            System.out.println("ymin: " + ymin + " ymax:" + ymax);
            System.out.println("zmin: " + zmin + " zmax:" + zmax);

            // calculating the med point
            xmed = (xmin + xmax)/2; ymed = (ymin + ymax) /2; zmed = (zmin + zmax)/2;
            
            
            // creating the "pivot matrix"
            
            double pivot[][] = { {1d, 0  , 0  ,-xmed},
                                 {0   ,1d, 0  ,-ymed},
                                 {0   , 0  ,1d,-zmed},
                                 {0   , 0  ,0,  1d  }
                               };
            
            pivotPoint = new Matrix4x4(pivot);
            
            
            // Read the number of edges
            int numEdges = in.nextInt();
            // Read the edges
            for (int i = 0; i < numEdges; i++) {
                // Read an edge
                int start = in.nextInt();
                int end = in.nextInt();
                Edge edge = new Edge(vertexArray[start], vertexArray[end]);
                po.addEdge(edge);
            }
            
         
            
            // Read the Project Plane Distance to the virtual camera
            proyectionPlaneDistance = in.nextInt();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

    }

    @Override
  public void keyReleased(KeyEvent ke) {
      System.out.println("Key Released");      
  }
  
    @Override
  public void keyPressed(KeyEvent ke) {
      System.out.println("Key Pressed");
      if(ke.getKeyCode() == KeyEvent.VK_A) {        // Left
        Translation trans = new Translation(-10, 0, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if(ke.getKeyCode() == KeyEvent.VK_D) { // Right
        Translation trans = new Translation(10, 0, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if(ke.getKeyCode() == KeyEvent.VK_W) { // Up
        Translation trans = new Translation(0, 10, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if(ke.getKeyCode() == KeyEvent.VK_S) { // Down
        Translation trans = new Translation(0, -10, 0);
        currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if(ke.getKeyCode() == KeyEvent.VK_R) { // Reset
        currentTransformation = new Matrix4x4();
      } else if (ke.getKeyCode() == KeyEvent.VK_X) {    // Rotate +X
          System.out.print(currentTransformation.toString());
          RotationX trans = new RotationX( 5d * Math.PI / 180d); // change for 5d * Math.PI / 180d
          currentTransformation = Matrix4x4.times(currentTransformation, trans);
          System.out.print(currentTransformation.toString());
      } else if (ke.getKeyCode() == KeyEvent.VK_C) {    // Rotate -X
          RotationX trans = new RotationX(-5d * Math.PI / 180d);
          currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if (ke.getKeyCode() == KeyEvent.VK_V) {    // Rotate +Z
          RotationZ trans = new RotationZ(5d * Math.PI / 180d);
          currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if (ke.getKeyCode() == KeyEvent.VK_B) {    // Rotate -Z
          RotationZ trans = new RotationZ(-5d * Math.PI / 180d);
          currentTransformation = Matrix4x4.times(currentTransformation, trans);
      } else if (ke.getKeyCode() == KeyEvent.VK_UP) {    // projection distance +10
          proyectionPlaneDistance += 10;
      } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {    // projection distance -10
          proyectionPlaneDistance -= 10;
      } else if(ke.getKeyCode() == KeyEvent.VK_Y){
          RotationY trans = new RotationY(5d * Math.PI / 180d);
          currentTransformation =  Matrix4x4.times(currentTransformation, trans);
      } else if(ke.getKeyCode() == KeyEvent.VK_6) {
          RotationY trans = new RotationY(-5d * Math.PI / 180d);
          currentTransformation =  Matrix4x4.times(currentTransformation, trans);
      } else if(ke.getKeyCode() == KeyEvent.VK_0) {
          Scaling trans = new Scaling(1.1,1.1,1.1);
          currentTransformation =  Matrix4x4.times(currentTransformation, trans);
 
//          just to see if scaling were done
//          Translation trans2 = new Translation(0,0,100);
//          currentTransformation =  Matrix4x4.times(currentTransformation, trans2);
       
      } 
      else if(ke.getKeyCode() == KeyEvent.VK_9)
      {
        Scaling trans = new Scaling(0.5,0.5,0.5);
        currentTransformation =  Matrix4x4.times(currentTransformation, trans);
        
////            just to see if scaling were done  
          Translation trans2 = new Translation(0,0,100);
          currentTransformation =  Matrix4x4.times(currentTransformation, trans2);
      }
      else if(ke.getKeyCode() == KeyEvent.VK_2)
      {
          // i suposse calcule only once an then modify
          
           RotationX transx = new RotationX(5d * Math.PI / 180d);
           Translation trans2 = new Translation(-xmed,-ymed,-zmed);
           currentTransformation = pivotPoint; // this line bring the house to the origin
           currentTransformation = Matrix4x4.times(currentTransformation, transx);
           pivotPoint = currentTransformation;
           currentTransformation = Matrix4x4.times(currentTransformation, trans2);           
   
      }
      else if(ke.getKeyCode() == KeyEvent.VK_8)
      {
          // i suposse calcule only once an then modify
          
           RotationX transx = new RotationX(-5d * Math.PI / 180d);
           Translation trans2 = new Translation(-xmed,-ymed,-zmed);
           currentTransformation = pivotPoint; // this line bring the house to the origin
           currentTransformation = Matrix4x4.times(currentTransformation, transx);
           pivotPoint = currentTransformation;
           currentTransformation = Matrix4x4.times(currentTransformation, trans2);           
   
      }
       else if(ke.getKeyCode() == KeyEvent.VK_4)
      {
          // i suposse calcule only once an then modify
          
           RotationY transy = new RotationY(5d * Math.PI / 180d);
           Translation trans2 = new Translation(-xmed,-ymed,-zmed);
           currentTransformation = pivotPoint; // this line bring the house to the origin
           currentTransformation = Matrix4x4.times(currentTransformation, transy);
           pivotPoint = currentTransformation;
           currentTransformation = Matrix4x4.times(currentTransformation, trans2);           
   
      }
      else if(ke.getKeyCode() == KeyEvent.VK_5)
      {
          // i suposse calcule only once an then modify
          
           RotationY transy = new RotationY(-5d * Math.PI / 180d);
           Translation trans2 = new Translation(-xmed,-ymed,-zmed);
           currentTransformation = pivotPoint; // this line bring the house to the origin
           currentTransformation = Matrix4x4.times(currentTransformation, transy);
           pivotPoint = currentTransformation;
           currentTransformation = Matrix4x4.times(currentTransformation, trans2);           
   
      }
        else if(ke.getKeyCode() == KeyEvent.VK_1)
      {
          // i suposse calcule only once an then modify
          
           RotationZ transz = new RotationZ(5d * Math.PI / 180d);
           Translation trans2 = new Translation(-xmed,-ymed,-zmed);
           currentTransformation = pivotPoint; // this line bring the house to the origin
           currentTransformation = Matrix4x4.times(currentTransformation, transz);
           pivotPoint = currentTransformation;
           currentTransformation = Matrix4x4.times(currentTransformation, trans2);           
   
      }
          else if(ke.getKeyCode() == KeyEvent.VK_3)
      {
          // i suposse calcule only once an then modify
          
           RotationZ transz = new RotationZ(-5d * Math.PI / 180d);
           Translation trans2 = new Translation(-xmed,-ymed,-zmed);
           currentTransformation = pivotPoint; // this line bring the house to the origin
           currentTransformation = Matrix4x4.times(currentTransformation, transz);
           pivotPoint = currentTransformation;
           currentTransformation = Matrix4x4.times(currentTransformation, trans2);           
   
      }
          
          
      repaint();
  } 
  
    @Override
  public void keyTyped(KeyEvent ke) {
      System.out.println("Key Typed");
  }
  
    
    
    public static void main(String[] args) {
        DibujarCasita3D dc = new DibujarCasita3D();

        // Read the file with the object description
        dc.readObjectDescription("objeto3D.txt");

        // Create a new Frame
        JFrame frame = new JFrame("Wire Frame Object");
        // Upon closing the frame, the application ends
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a panel called DibujarCasita3D
        frame.add(dc);
        // Asignarle tamaño
        frame.setSize(DibujarCasita3D.FRAME_WIDTH, DibujarCasita3D.FRAME_HEIGHT);
        // Put the frame in the middle of the window
        frame.setLocationRelativeTo(null);
        // Show the frame
        frame.setVisible(true);
    }
}
