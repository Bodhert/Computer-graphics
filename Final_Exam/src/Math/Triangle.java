/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;


import Scene.Material;
import Scene.Colour;
import Scene.Shader;
/**
 *
 * @author alejo
 */
public class Triangle implements Intersectable{
    Point p1,p2,p3;
    Material material;
    
    public Triangle(Point p1, Point p2, Point p3, Material material)
    {
        // the 3 points in the space to form a 3d triangle
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.material = material;
    }
    
    
    @Override
    public Solutions intersect(Ray ray)
    {
        ThreeByThreeSystem equations = new ThreeByThreeSystem
        (
                new double[][] 
                {
                    {ray.u.getX(), p1.x - p2.x, p1.x - p3.x},
                    {ray.u.getY(), p1.y - p2.y, p1.y - p3.y},
                    {ray.u.getZ(), p1.z - p2.z, p1.z - p3.z},      
                },
                new double[]
                {
                    p1.x - ray.p0.getX(),
                    p1.y - ray.p0.getY(),
                    p1.z - ray.p0.getZ(),
                }
        );
        
        double ans[] = equations.computeSystem();
        
        double s = ans[0];
        double alpha = 1 - ans[1] - ans[2];
        double betha = ans[1];
        double gamma = ans[2];
        
        if((alpha > 0 && alpha < 1) && (betha > 0 && betha < 1) && 
                                       (gamma > 0 && gamma < 1))
            return new Solutions(1, s, 0);
        else 
            return  new Solutions(0, 0, 0);
        
    }


    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Colour callShader(Ray ray, double minT) {
        Point point = ray.evaluate(minT);
        Vector4 v1 = new Vector4(p1.x,p1.y,p1.z,p2.x,p2.y,p2.z);
        Vector4 v2 = new Vector4(p1.x,p1.y,p1.z,p3.x,p3.y,p3.z);
        Vector4 normal = Vector4.crossProduct(v1, v2);
        normal.normalize();
        return Shader.computeColor(point, normal, material);
    }

    @Override
    public Vector4 computeNormal(Point p) {
        Vector4 v1 = new Vector4(p1.x,p1.y,p1.z,p2.x,p2.y,p2.z);
        Vector4 v2 = new Vector4(p1.x,p1.y,p1.z,p3.x,p3.y,p3.z);
        Vector4 normal = Vector4.crossProduct(v1, v2);
        normal.normalize();
        return normal;
    }


   
    
}
