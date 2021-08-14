package model;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

import main.Camera;
import main.Display;
import math.Rotation;
import math.Vector3D;
import model.shape.Triangle3D;

public class Mesh extends Model3D {

	public ArrayList<Triangle3D> triangles;
	protected Color color;
	protected float scale;
	public static final Color DEFAULT_COLOR = Color.white;
	public static final String DEFAULT_NAME = "Mesh";
	public static final float DEFAULT_SCALE = 1.0f;

	private int render;
	public static final int HOLLOW = 0;
	public static final int SOLID = 1;

	// =============== Constructor ===============

	public Mesh(float scale, Color c, int render, String name) {
		super(name != null ? name : DEFAULT_NAME);
		setScale(scale);
		this.color = c;
		setRenderAttribute(render);
	}

	public Mesh(float scale, Vector3D pos, Color c, int render, String name) {
		super(pos, name != null ? name : DEFAULT_NAME);
		setScale(scale);
		this.color = c;
		setRenderAttribute(render);
	}

	public Mesh(float scale, Rotation rot, Color c, int render, String name) {
		super(rot, name != null ? name : DEFAULT_NAME);
		setScale(scale);
		this.color = c;
		setRenderAttribute(render);
	}

	public Mesh(float scale, Vector3D pos, Rotation rot, Color c, int render, String name) {
		super(pos, rot, name != null ? name : DEFAULT_NAME);
		setScale(scale);
		this.color = c;
		setRenderAttribute(render);
	}

	// ----------

	public Mesh(String name) {
		this(DEFAULT_SCALE, DEFAULT_COLOR, HOLLOW, name);
	}

	public Mesh(Vector3D pos, String name) {
		this(DEFAULT_SCALE, pos, DEFAULT_COLOR, HOLLOW, name);
	}

	public Mesh(Rotation rot, String name) {
		this(DEFAULT_SCALE, rot, DEFAULT_COLOR, HOLLOW, name);
	}

	public Mesh(Vector3D pos, Rotation rot, String name) {
		this(DEFAULT_SCALE, pos, rot, DEFAULT_COLOR, HOLLOW, name);
	}

	// -------------------------------------------

	public Mesh(float scale, Color c, int render) {
		this(scale, c, render, DEFAULT_NAME);
	}

	public Mesh(float scale, Vector3D pos, Color c, int render) {
		this(scale, pos, c, render, DEFAULT_NAME);
	}

	public Mesh(float scale, Rotation rot, Color c, int render) {
		this(scale, rot, c, render, DEFAULT_NAME);
	}

	public Mesh(float scale, Vector3D pos, Rotation rot, Color c, int render) {
		this(scale, pos, rot, c, render, DEFAULT_NAME);
	}

	// ----------

	public Mesh() {
		this(DEFAULT_SCALE, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	public Mesh(Vector3D pos) {
		this(DEFAULT_SCALE, pos, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	public Mesh(Rotation rot) {
		this(DEFAULT_SCALE, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	public Mesh(Vector3D pos, Rotation rot) {
		this(DEFAULT_SCALE, pos, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	//////////////////////////////////////////////

	public Mesh(float scale, Color c, int render, String name, Triangle3D... triangles) {
		this(scale, c, render, name);
		this.setTriangles(triangles);
	}

	public Mesh(float scale, Vector3D pos, Color c, int render, String name, Triangle3D... triangles) {
		this(scale, pos, c, render, name);
		this.setTriangles(triangles);
	}

	public Mesh(float scale, Rotation rot, Color c, int render, String name, Triangle3D... triangles) {
		this(scale, rot, c, render, name);
		this.setTriangles(triangles);
	}

	public Mesh(float scale, Vector3D pos, Rotation rot, Color c, int render, String name, Triangle3D... triangles) {
		this(scale, pos, rot, c, render, name);
		this.setTriangles(triangles);
	}

	// ----------

	public Mesh(String name, Triangle3D... triangles) {
		this(DEFAULT_SCALE, DEFAULT_COLOR, HOLLOW, name, triangles);
	}

	public Mesh(Vector3D pos, String name, Triangle3D... triangles) {
		this(DEFAULT_SCALE, pos, DEFAULT_COLOR, HOLLOW, name, triangles);
	}

	public Mesh(Rotation rot, String name, Triangle3D... triangles) {
		this(DEFAULT_SCALE, rot, DEFAULT_COLOR, HOLLOW, name, triangles);
	}

	public Mesh(Vector3D pos, Rotation rot, String name, Triangle3D... triangles) {
		this(DEFAULT_SCALE, pos, rot, DEFAULT_COLOR, HOLLOW, name, triangles);
	}

	// -------------------------------------------

	public Mesh(float scale, Color c, int render, Triangle3D... triangles) {
		this(scale, c, render, DEFAULT_NAME, triangles);
	}

	public Mesh(float scale, Vector3D pos, Color c, int render, Triangle3D... triangles) {
		this(scale, pos, c, render, DEFAULT_NAME, triangles);
	}

	public Mesh(float scale, Rotation rot, Color c, int render, Triangle3D... triangles) {
		this(scale, rot, c, render, DEFAULT_NAME, triangles);
	}

	public Mesh(float scale, Vector3D pos, Rotation rot, Color c, int render, Triangle3D... triangles) {
		this(scale, pos, rot, c, render, DEFAULT_NAME, triangles);
	}

	// ----------

	public Mesh(Triangle3D... triangles) {
		this(DEFAULT_SCALE, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME, triangles);
	}

	public Mesh(Vector3D pos, Triangle3D... triangles) {
		this(DEFAULT_SCALE, pos, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME, triangles);
	}

	public Mesh(Rotation rot, Triangle3D... triangles) {
		this(DEFAULT_SCALE, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME, triangles);
	}

	public Mesh(Vector3D pos, Rotation rot, Triangle3D... triangles) {
		this(DEFAULT_SCALE, pos, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME, triangles);
	}

//////////////////////////////////////////////

	public Mesh(String filePath, float scale, Color c, int render, String name) throws IOException {
		this(scale, c, render, name);
		this.loadObjModel(filePath);
	}

	public Mesh(String filePath, float scale, Vector3D pos, Color c, int render, String name) throws IOException {
		this(scale, pos, c, render, name);
		this.loadObjModel(filePath);
	}

	public Mesh(String filePath, float scale, Rotation rot, Color c, int render, String name) throws IOException {
		this(scale, rot, c, render, name);
		this.loadObjModel(filePath);
	}

	public Mesh(String filePath, float scale, Vector3D pos, Rotation rot, Color c, int render, String name) throws IOException {
		this(scale, pos, rot, c, render, name);
		this.loadObjModel(filePath);
	}

// ----------

	public Mesh(String filePath, String name) throws IOException {
		this(filePath, DEFAULT_SCALE, DEFAULT_COLOR, HOLLOW, name);
	}

	public Mesh(String filePath, Vector3D pos, String name) throws IOException {
		this(filePath, DEFAULT_SCALE, pos, DEFAULT_COLOR, HOLLOW, name);
	}

	public Mesh(String filePath, Rotation rot, String name) throws IOException {
		this(filePath, DEFAULT_SCALE, rot, DEFAULT_COLOR, HOLLOW, name);
	}

	public Mesh(String filePath, Vector3D pos, Rotation rot, String name) throws IOException {
		this(filePath, DEFAULT_SCALE, pos, rot, DEFAULT_COLOR, HOLLOW, name);
	}

// -------------------------------------------

	public Mesh(String filePath, float scale, Color c, int render) throws IOException {
		this(filePath, scale, c, render, DEFAULT_NAME);
	}

	public Mesh(String filePath, float scale, Vector3D pos, Color c, int render) throws IOException {
		this(filePath, scale, pos, c, render, DEFAULT_NAME);
	}

	public Mesh(String filePath, float scale, Rotation rot, Color c, int render) throws IOException {
		this(filePath, scale, rot, c, render, DEFAULT_NAME);
	}

	public Mesh(String filePath, float scale, Vector3D pos, Rotation rot, Color c, int render) throws IOException {
		this(filePath, scale, pos, rot, c, render, DEFAULT_NAME);
	}

// ----------

	public Mesh(String filePath, float scale) throws IOException {
		this(filePath, DEFAULT_SCALE, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	public Mesh(String filePath, Vector3D pos) throws IOException {
		this(filePath, DEFAULT_SCALE, pos, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	public Mesh(String filePath, Rotation rot) throws IOException {
		this(filePath, DEFAULT_SCALE, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	public Mesh(String filePath, Vector3D pos, Rotation rot) throws IOException {
		this(filePath, DEFAULT_SCALE, pos, rot, DEFAULT_COLOR, HOLLOW, DEFAULT_NAME);
	}

	// ===========================================

	protected void setTriangles(Triangle3D... triangles) {
		this.triangles = new ArrayList<>(triangles.length);
		for (Triangle3D tri : triangles) {
			this.triangles.add(tri);
		}
	}

	protected void setTriangles(ArrayList<Triangle3D> triangles) {
		this.triangles = triangles;
	}

	public void loadObjModel(String filePath) throws IOException {
		this.triangles = ObjLoader.loadObjModel(filePath);
	}

	public void reset() {
		super.reset();
		this.color = DEFAULT_COLOR;
	}

	public String toDebugString() {
		String debug = super.toDebugString();

		debug += "\tColor: " + this.color.toString() + "\n";
		debug += "\tScale: " + this.scale + "\n";
		debug += "\tRendering Attribute: " + ((this.render == HOLLOW) ? "HOLLOW" : "SOLID") + "\n";

		return debug;
	}
	
	public String toString() {
		return name;
	}

	// ============= Getter Setter =============

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public int getRenderAttribute() {
		return render;
	}

	public void setRenderAttribute(int render) {
		if (render == HOLLOW || render == SOLID)
			this.render = render;
		else {
			System.err.println("Render attribute has to be either Mesh.HOLLOW or Mesh.SOLID");
			this.render = HOLLOW;
		}
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		if (scale <= 0) {
			System.err.println("Scale must be greater than zero");
			scale = DEFAULT_SCALE;
		}
		this.scale = scale;
	}

	// =========================================

	public void render(Display d, Camera cam, Graphics g) {
		for (Triangle3D tri : this.triangles) {
			tri.draw(this, d, cam, g);
		}
	}

}
