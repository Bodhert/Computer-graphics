using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;
using System.IO;
using System;

public class Test : MonoBehaviour {

    public Material myMat;
    public GameObject myCamera;
    private GameObject myPlane;
	private GameObject myPlane2;


	// Use this for initialization
	void Start () {
		myPlane = meshCreator.createMesh(100, 100, myMat);
	}
	
	 //Update is called once per frame
	void Update () {
        myCamera.transform.LookAt(myPlane.transform);
	}
}
