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
 * @author helmuthtrefftz and bodhert
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
    
    static int INSIDE = 0; // 0000
    static int LEFT = 1;   // 0001
    static int RIGHT = 2;  // 0010
    static int BOTTOM = 4; // 0100
    static int TOP = 8;    // 1000
    
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

        drawClippingRectangle(minX, minY, maxX, maxY);
//        drawTransformedLine(0, 0, (int)(radius * Math.cos(theta)), 
//                (int)(radius * Math.sin(theta)));
        
        int xmin = transformX(0); int ymin = transformY(0);
        int xmax = transformX((int)(radius * Math.cos(theta)));
        int ymax = transformY((int)(radius * Math.sin(theta)));
        
        g2d.setColor(Color.green);
        drawBresenham(g2d, xmin, ymin, xmax, ymax);
        
        CohenSutherlandLineClipAndDraw(g2d, xmin, ymin,xmax, ymax);
        
        
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
        
//        System.out.println(" " + x1 + " " + y1 + " " + w1 + " " + h1);
    }
    
    public int ComputeOutCode(double x,double y)
    {
        int code = INSIDE;
        
        if (x < globalXmin)           // to the left of clip window
		code |= LEFT;
	else if (x > globalXmax)      // to the right of clip window
		code |= RIGHT;
	if (y < globalYmin)           // below the clip window
		code |= BOTTOM;
	else if (y > globalYmax)      // above the clip window
		code |= TOP;
        
	return code;
                
    }
    
    void CohenSutherlandLineClipAndDraw(Graphics2D g2d,int x0, int y0, int x1, int y1)
    {
        int outcode0 = ComputeOutCode(x0, y0);
	int outcode1 = ComputeOutCode(x1, y1);
	boolean accept = false;

	while (true) 
        {
		if ((outcode0 | outcode1) == 0) 
                {
			// bitwise OR is 0: both points inside window; trivially accept and exit loop
			accept = true;
			break;
		} 
                else if ((outcode0 & outcode1) != 0) 
                {
			// bitwise AND is not 0: both points share an outside zone (LEFT, RIGHT, TOP,
			// or BOTTOM), so both must be outside window; exit loop (accept is false)
			break;
		} 
                else 
                {
			
			int x = 0, y = 0;
			int outcodeOut = outcode0 != 0 ? outcode0 : outcode1;

			if ((outcodeOut & TOP) != 0) 
                        {           // point is above the clip window
				x = x0 + (x1 - x0) * (globalYmax - y0) / (y1 - y0);
				y = globalYmax;
			} 
                        else if ((outcodeOut & BOTTOM) != 0) 
                        { // point is below the clip window
				x = x0 + (x1 - x0) * (globalYmin - y0) / (y1 - y0);
				y = globalYmin;
			} else if ((outcodeOut & RIGHT) != 0) 
                        {  // point is to the right of clip window
				y = y0 + (y1 - y0) * (globalXmax - x0) / (x1 - x0);
				x = globalXmax;
			} 
                        else if ((outcodeOut & LEFT) != 0) 
                        {   // point is to the left of clip window
				y = y0 + (y1 - y0) * (globalXmin - x0) / (x1 - x0);
				x = globalXmin;
			}
			// Now we move outside point to intersection point to clip
			// and get ready for next pass.
			if (outcodeOut == outcode0) {
				x0 = x;
				y0 = y;
				outcode0 = ComputeOutCode(x0, y0);
			} else {
				x1 = x;
				y1 = y;
				outcode1 = ComputeOutCode(x1, y1);
			}
		}
	}
	if (accept) {
//		DrawRectangle(globalXmin, globalYmin, globalXmax, globalYmax);  
                g2d.setColor(Color.red);
		drawBresenham(g2d,x0, y0, x1, y1);
	}
    }
    
    public void drawTransformedLine(int x1, int y1, int x2, int y2) {
        g2d.setColor(Color.green);

        int x1t = transformX(x1);
        int y1t = transformY(y1);
        int x2t = transformX(x2);
        int y2t = transformY(y2);
        System.out.println(x1t + " " + y1t + " " + x2t + " " + y2t + " ");
//        g2d.drawLine(x1t, y1t, x2t, y2t);
//        drawBresenham(g2d, x1t, y1t, x2t, y2t);
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
