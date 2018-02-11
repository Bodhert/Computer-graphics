/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author helmuthtrefftz
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JFrame;


/**
 *
 * @author helmuthtrefftz
 */
public class Clipping extends JPanel implements KeyListener  {

    int w = 0;
    int h = 0;
    Graphics2D g2d;
    int radius = 100;
    // Clipping Rectangle
    int minX = 50;
    int minY = -50;
    int maxX = 150;
    int maxY = 50;
    
    int globalXmin ;
    int globalYmin ;
    int globalXmax ;
    int globalYmax ;
    
    double theta = 0;
    double step = 1.0 * Math.PI / 180d;
    
    boolean DEBUG = true;
    
    public static int pixelSize = 1; //size of the "ligth saber"
    
    public Clipping() {
        // El panel, por defecto no es "focusable". 
        // Hay que incluir estas líneas para que el panel pueda
        // agregarse como KeyListsener.
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);        
    }
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g2d = (Graphics2D) g;


        // size es el tamaño de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los títulos de la ventana.
        Insets insets = getInsets();

        w = size.width - insets.left - insets.right;
        h = size.height - insets.top - insets.bottom;


        
        //drawConnectionGraph();
        drawClippingRectangle(minX, minY, maxX, maxY);
        drawTransformedLine(0, 0, (int)(radius * Math.cos(theta)), 
                (int)(radius * Math.sin(theta)));
    }
    
    public void drawClippingRectangle(int minX, int minY, int maxX, int maxY) {
        g2d.setColor(Color.RED);
        int x1 = transformX(minX);
        int y1 = transformY(maxY);
        int w1 = maxX - minX;
        int h1 = maxY - minY;
        g2d.drawRect(x1, y1, w1, h1);
        
        globalXmin = x1;
        globalYmin = y1;
        globalXmax = x1 + w1;
        globalYmax = y1 + h1;
        
        System.out.println(" " + x1 + " " + y1 + " " + w1 + " " + h1);
    }
    
    public void drawTransformedLine(int x1, int y1, int x2, int y2) {
        g2d.setColor(Color.green);

        int x1t = transformX(x1);
        int y1t = transformY(y1);
        int x2t = transformX(x2);
        int y2t = transformY(y2);
//        g2d.drawLine(x1t, y1t, x2t, y2t);
        drawBresenham(g2d, x1t, y1t, x2t, y2t);
    }
    
    public int transformX(int x) {
        return x + w/2;
    }
    
    public int transformY(int y) {
        return h/2 - y;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int tecla = e.getKeyCode();
        if(tecla == KeyEvent.VK_W) {
            theta += step;
        } else if (tecla == KeyEvent.VK_S) {
            theta -= step;
        }
        if(DEBUG) {
            System.out.println(theta + " ");
        }
        repaint();
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
                System.out.println("x:" + transformX(x) + " y:" + transformY(y));
                int tempY = transformY(y);
                System.out.println("..." + globalXmin + " " + globalXmax + " " + globalYmin + " " + globalYmax);
                  if(x >= globalXmin && x <= globalXmax && y >= globalYmin && y <= globalYmax){ 
                    g2d.setColor(Color.red);
                    System.out.println("entro 1");
                }
                else g2d.setColor(Color.green);
                
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
             
                 if(x >= globalXmin && x <= globalXmax && y >= globalYmin && y <= globalYmax){ 
                    g2d.setColor(Color.red);
                    System.out.println("entro 2");
                }
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

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    
    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Punto de partida clipping");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new Clipping());
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }

    
}
