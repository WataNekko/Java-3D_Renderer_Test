package math;

import model.Model3D;

public class Vector3D {
	public float x, y, z;

	// =============== Constructor ===============

	public Vector3D() {

	}

	public Vector3D(float x, float y, float z) {
		set(x, y, z);
	}

	public Vector3D(Vector3D v) {
		set(v);
	}

	public Vector3D(Model3D m) {
		set(m);
	}

	// =========================================

	public void reset() {
		set(0.0f, 0.0f, 0.0f);
	}

	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	// ============= Getter Setter =============

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(Vector3D v) {
		set(v.x, v.y, v.z);
	}

	public void set(Model3D m) {
		set(m.getPosX(), m.getPosY(), m.getPosZ());
	}

	// =========================================

	public void translate(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void translate(Vector3D v) {
		translate(v.x, v.y, v.z);
	}

	public void translate(Model3D m) {
		translate(m.getPosX(), m.getPosY(), m.getPosZ());
	}

	public void scale(float x) {
		this.x *= x;
		this.y *= x;
		this.z *= x;
	}

	// =========================================

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public static Vector3D crossProduct(Vector3D a, Vector3D b) {
		return new Vector3D((a.y * b.z - b.y * a.z), (a.z * b.x - b.z * a.x), (a.x * b.y - b.x * a.y));
	}

	public Vector3D crossProduct(Vector3D v) {
		return crossProduct(this, v);
	}

	public static float dotProduct(Vector3D a, Vector3D b) {
		return (a.x * b.x + a.y * b.y + a.z * b.z);
	}

	public float dotProduct(Vector3D v) {
		return dotProduct(this, v);
	}

	public static Vector3D getVector(Vector3D p1, Vector3D p2) {
		return new Vector3D(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
	}

	public Vector3D getVector(Vector3D p) {
		return getVector(this, p);
	}

	public void normalise() {
		float len = this.length();
		if (len != 1) {
			this.scale(1 / len);
		}
	}

	public Vector3D getNormalisedVector() {
		Vector3D unit = new Vector3D(this);
		unit.normalise();
		return unit;
	}

	// =========================================

}
