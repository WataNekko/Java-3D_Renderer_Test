package model.shape;

import java.awt.Color;
import java.util.ArrayList;

import math.Rotation;
import math.Vector3D;
import model.Mesh;

public class Cube extends Mesh {
	public static final String DEFAULT_NAME = "Cube";
	
	// =============== Constructor ===============

	public Cube(float scale, Color c, int render, String name) {
		super(scale, c, render, name != null ? name : DEFAULT_NAME);
		this.triangles = getTriangles(1);
	}
	
	public Cube(float scale, Vector3D pos, Color c, int render, String name) {
		super(scale, pos, c, render, name != null ? name : DEFAULT_NAME);
		this.triangles = getTriangles(1);
	}
	
	public Cube(float scale, Rotation rot, Color c, int render, String name) {
		super(scale, rot, c, render, name != null ? name : DEFAULT_NAME);
		this.triangles = getTriangles(1);
	}
	
	public Cube(float scale, Vector3D pos, Rotation rot, Color c, int render, String name) {
		super(scale, pos, rot, c, render, name != null ? name : DEFAULT_NAME);
		this.triangles = getTriangles(1);
	}
	
	// ----------
	
	public Cube(float scale, String name) {
		this(scale, DEFAULT_COLOR, HOLLOW, name);
	}
	
	public Cube(float scale, Vector3D pos, String name) {
		this(scale, pos, DEFAULT_COLOR, HOLLOW, name);
	}
	
	public Cube(float scale, Rotation rot, String name) {
		this(scale, rot, DEFAULT_COLOR, HOLLOW, name);
	}
	
	public Cube(float scale, Vector3D pos, Rotation rot, String name) {
		this(scale, pos, rot, DEFAULT_COLOR, HOLLOW, name);
	}
	
	// -------------------------------------------
	
	public Cube(float scale, Color c, int render) {
		this(scale, c, render, DEFAULT_NAME);
	}
	
	public Cube(float scale, Vector3D pos, Color c, int render) {
		this(scale, pos, c, render, DEFAULT_NAME);
	}
	
	public Cube(float scale, Rotation rot, Color c, int render) {
		this(scale, rot, c, render, DEFAULT_NAME);
	}
	
	public Cube(float scale, Vector3D pos, Rotation rot, Color c, int render) {
		this(scale, pos, rot, c, render, DEFAULT_NAME);
	}
	
	// ----------
	
	public Cube(float scale) {
		this(scale, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}
	
	public Cube(float scale, Vector3D pos) {
		this(scale, pos, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}
	
	public Cube(float scale, Rotation rot) {
		this(scale, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}
	
	public Cube(float scale, Vector3D pos, Rotation rot) {
		this(scale, pos, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	// ===========================================

	private static ArrayList<Triangle3D> getTriangles(float size) {
		size /= 2;

		Vector3D[] point = new Vector3D[] {
				new Vector3D(-size, -size, -size),
				new Vector3D(size, -size, -size),
				new Vector3D(size, -size, size),
				new Vector3D(-size, -size, size),
				new Vector3D(-size, size, -size),
				new Vector3D(size, size, -size),
				new Vector3D(size, size, size),
				new Vector3D(-size, size, size)
		};
		ArrayList<Triangle3D> triangles = new ArrayList<>(12);
		
		// South (-z)
		triangles.add(new Triangle3D(point[0], point[4], point[5]));
		triangles.add(new Triangle3D(point[0], point[5], point[1]));

		// North (+z)
		triangles.add(new Triangle3D(point[2], point[6], point[7]));
		triangles.add(new Triangle3D(point[2], point[7], point[3]));

		// East (+x)
		triangles.add(new Triangle3D(point[1], point[5], point[6]));
		triangles.add(new Triangle3D(point[1], point[6], point[2]));

		// West (-x)
		triangles.add(new Triangle3D(point[3], point[7], point[4]));
		triangles.add(new Triangle3D(point[3], point[4], point[0]));

		// Top (+y)
		triangles.add(new Triangle3D(point[4], point[7], point[6]));
		triangles.add(new Triangle3D(point[4], point[6], point[5]));

		// Bottom (-y)
		triangles.add(new Triangle3D(point[3], point[0], point[1]));
		triangles.add(new Triangle3D(point[3], point[1], point[2]));

		return triangles;
	}
	
	// ===========================================

}