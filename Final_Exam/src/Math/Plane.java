/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import Scene.Colour;
import Scene.Material;
import Scene.Shader;

/**
 *
 * @author Alejandro Cordoba Bodhert
 * @author Santiago Baena Rivera
 * @author Yorman Aguirre
 */
public class Plane implements Intersectable {
    
    Point p1,p2,p3;
    Material material;
    
    public Plane(Point p1, Point p2, Point p3, Material material){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.material = material;
    }

    @Override
    public Solutions intersect(Ray ray) {
            Vector4 normal = computeNormal(p1); 
            Vector4 vecp1 = new Vector4(p1);
            Vector4 vecp0 = new Vector4(ray.p0);
            Vector4 vecU =  new Vector4(ray.u);
            double d =  Vector4.dotProduct(vecp1, normal);
            
            double s1 = (d + (Vector4.dotProduct(normal, vecp0))) / Vector4.dotProduct(normal, ray.u);
            return  new Solutions(1, s1, 0);
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

    public Vector4 computeNormal(Point p) {
        Vector4 v1 = new Vector4(p1.x,p1.y,p1.z,p2.x,p2.y,p2.z);
        Vector4 v2 = new Vector4(p1.x,p1.y,p1.z,p3.x,p3.y,p3.z);
        Vector4 normal = Vector4.crossProduct(v1, v2);
        normal.normalize();
        return normal;
    }

}


