using System.Collections;
using System.Collections.Generic;
using System.IO;
using System;
using System.Text;
using System.Linq;
using UnityEngine;

public class meshCreator : MonoBehaviour {
	
	private static List<Vector3> points = new List<Vector3> ();
	private static List<int> triangles = new List<int> ();

	public static void readObjectDescription()
	{
		//reading file
		string path = "Assets/Resources/test1.txt";
		//Read the text from directly from the test.txt file
		StreamReader reader = new StreamReader(path); 
		//		Debug.Log(reader.ReadToEnd());
		int numVertices;
		numVertices = int.Parse(reader.ReadLine());
		Debug.Log(numVertices);

		//assigning the values 
		for (int i = 0; i < numVertices; ++i) 
		{
			string[] tokens = reader.ReadLine().Split();
			int x, y, z;
			int[] numbers = Array.ConvertAll(tokens, int.Parse);
			x = numbers [0]; y = numbers [1]; z = numbers [2];
			points.Add (new Vector3((float)x,(float)y,(float)z));
		}


		// i have this code for a very specific input, going to generalize better
		// for every 4 conections i am reading two triangles
		//		List<int> triangles = new List<int> ();
		int vertsConexions;
		vertsConexions = int.Parse (reader.ReadLine());
		for (int i = 0; i < vertsConexions; ++i) 
		{
			string[] tokens = reader.ReadLine().Split();
			int [] numbers = Array.ConvertAll(tokens, int.Parse);

			//clockwise and anticlockwise for visualitation;
			triangles.Add (numbers [0]);
			triangles.Add (numbers [1]);
			triangles.Add (numbers [2]);

			triangles.Add (numbers [2]);
			triangles.Add (numbers [1]);
			triangles.Add (numbers [0]);


		}

		//Debug.Log(reader.ReadToEnd());

		reader.Close();
	}

	public static void printList()
	{
//		points.ForEach(i => Debug.log("{0}\t", i));
	}

	public static GameObject createMesh(float width, float length, Material mat) {
        GameObject go = new GameObject("myPlane");
        MeshFilter mf = go.AddComponent(typeof(MeshFilter)) as MeshFilter;
        MeshRenderer mr = go.AddComponent(typeof(MeshRenderer)) as MeshRenderer;
        Mesh m = new Mesh();

		readObjectDescription ();

		m.vertices = points.ToArray ();
		m.triangles = triangles.ToArray ();

//        m.uv = new Vector2[] {
//            new Vector2(0,0),
//            new Vector2(0,1),
//            new Vector2(1,1),
//            new Vector2(1,0)
//        };


//		m.triangles = new int[] 
//		{
//			1,0,2,
//			2,0,1,
//
//			0,3,2,
//			2,3,0
//		};
//


        mf.mesh = m;
        mr.material = mat;
        m.RecalculateBounds();
//        m.RecalculateNormals();

        return go;
    }
}
