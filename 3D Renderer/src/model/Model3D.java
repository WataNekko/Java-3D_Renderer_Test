package model;

import math.Matrix;
import math.Rotation;
import math.Vector3D;

public class Model3D {
	protected Vector3D pos;
	protected Rotation rot;
	protected String name;
	public static final String DEFAULT_NAME = "Model";

	// =============== Constructor ===============

	public Model3D(String name) {
		this.pos = new Vector3D();
		this.rot = new Rotation();
		this.name = name != null ? name : DEFAULT_NAME;
	}

	public Model3D(Vector3D pos, String name) {
		this.pos = pos;
		this.rot = new Rotation();
		this.name = name != null ? name : DEFAULT_NAME;
	}

	public Model3D(Rotation rot, String name) {
		this.pos = new Vector3D();
		this.rot = rot;
		this.name = name != null ? name : DEFAULT_NAME;
	}

	public Model3D(Vector3D pos, Rotation rot, String name) {
		this.pos = pos;
		this.rot = rot;
		this.name = name != null ? name : DEFAULT_NAME;
	}

	// -------------------------------------------

	public Model3D() {
		this(DEFAULT_NAME);
	}

	public Model3D(Vector3D pos) {
		this(pos, DEFAULT_NAME);
	}

	public Model3D(Rotation rot) {
		this(rot, DEFAULT_NAME);
	}

	public Model3D(Vector3D pos, Rotation rot) {
		this(pos, rot, DEFAULT_NAME);
	}

	// =========================================

	public void reset() {
		this.pos.reset();
		this.rot.reset();
	}

	public String toDebugString() {
		String debug = "";

		debug += this.name + "\n";
		debug += "\tPosition: " + this.pos.toString() + "\n";
		debug += "\tRotation: " + this.rot.toString() + "\n";

		return debug;
	}

	// ============= Getter Setter =============

	// -------------- Name --------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// --------------- Pos ---------------
	public String getPosString() {
		return pos.toString();
	}

	public float getPosX() {
		return pos.x;
	}

	public float getPosY() {
		return pos.y;
	}

	public float getPosZ() {
		return pos.z;
	}

	// -----------------------------

	public void setPosX(float x) {
		this.pos.x = x;
	}

	public void setPosY(float y) {
		this.pos.y = y;
	}

	public void setPosZ(float z) {
		this.pos.z = z;
	}

	public void setPos(float x, float y, float z) {
		this.pos.set(x, y, z);
	}

	public void setPos(Vector3D pos) {
		this.pos.set(pos);
	}

	// --------------- Rot ---------------

	public String getRotString() {
		return rot.toString();
	}

	public float getRotX() {
		return rot.getRx();
	}

	public float getRotY() {
		return rot.getRy();
	}

	public float getRotZ() {
		return rot.getRz();
	}

	// ---------------------------

	public void setRotX(float rx) {
		this.rot.setRx(rx);
	}

	public void setRotY(float ry) {
		this.rot.setRy(ry);
	}

	public void setRotZ(float rz) {
		this.rot.setRz(rz);
	}

	public void setRot(float rx, float ry, float rz) {
		this.rot.set(rx, ry, rz);
	}

	public void setRot(Rotation rot) {
		this.rot.set(rot);
	}

	// =========================================

	// --------------- Pos ---------------

	public void translateX(float x) {
		this.pos.x += x;
	}

	public void translateY(float y) {
		this.pos.y += y;
	}

	public void translateZ(float z) {
		this.pos.z += z;
	}

	public void translate(float x, float y, float z) {
		this.pos.translate(x, y, z);
	}

	public void translate(Vector3D v) {
		this.pos.translate(v);
	}

	// --------------- Rot ---------------

	public void rotateX(float rx) {
		this.rot.rotateX(rx);
	}

	public void rotateY(float ry) {
		this.rot.rotateY(ry);
	}

	public void rotateZ(float rz) {
		this.rot.rotateZ(rz);
	}

	public void rotate(float rx, float ry, float rz) {
		this.rot.rotate(rx, ry, rz);
	}

	public void rotate(Rotation r) {
		this.rot.rotate(r);
	}

	// =========================================

	public void move(float x, float y, float z) {
		translate(Matrix.getRotatedVector(x, y, z, this.rot));
	}
	
	public void move(Vector3D v) {
		translate(Matrix.getRotatedVector(v, this.rot));
	}

	public void moveX(float x) {
		move(x, 0, 0);
	}
	
	public void moveY(float y) {
		move(0, y, 0);
	}

	public void moveZ(float z) {
		move(0, 0, z);
	}

}