package math;

import model.Model3D;

public class Rotation {
	protected float rx, ry, rz;

	// =============== Constructor ===============

	public Rotation() {

	}

	public Rotation(float rx, float ry, float rz) {
		set(rx, ry, rz);
	}

	public Rotation(Rotation r) {
		set(r);
	}

	public Rotation(Model3D m) {
		set(m);
	}

	// =========================================

	public void reset() {
		setNoMapping(0.0f, 0.0f, 0.0f);
	}

	public String toString() {
		return "(" + this.rx + ", " + this.ry + ", " + this.rz + ")";
	}

	// ============= Getter Setter =============

	public float getRx() {
		return rx;
	}

	public float getRy() {
		return ry;
	}

	public float getRz() {
		return rz;
	}

	// -----------------------------------------

	public void setRx(float rx) {
		this.rx = map180(rx);
	}

	public void setRy(float ry) {
		this.ry = map360(ry);
	}

	public void setRz(float rz) {
		this.rz = map180(rz);
	}

	public void set(float rx, float ry, float rz) {
		setRx(rx);
		setRy(ry);
		setRz(rz);
	}

	private void setNoMapping(float rx, float ry, float rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}

	public void set(Rotation r) {
		setNoMapping(r.rx, r.ry, r.rz);
	}

	public void set(Model3D m) {
		setNoMapping(m.getRotX(), m.getRotY(), m.getRotZ());
	}

	// =========================================

	public void rotateX(float rx) {
		if (rx % 360 != 0)
			this.rx = map180(this.rx + rx);
	}

	public void rotateY(float ry) {
		if (ry % 360 != 0)
			this.ry = map360(this.ry + ry);
	}

	public void rotateZ(float rz) {
		if (rz % 360 != 0)
			this.rz = map180(this.rz + rz);
	}

	public void rotate(float rx, float ry, float rz) {
		rotateX(rx);
		rotateY(ry);
		rotateZ(rz);
	}

	public void rotate(Rotation r) {
		rotate(r.rx, r.ry, r.rz);
	}

	public void rotate(Model3D m) {
		rotate(m.getRotX(), m.getRotY(), m.getRotZ());
	}

	// =========================================

	private float map360(float r) {
		// 0 <= angle < 360
		if (r <= -360 || 360 <= r)
			r %= 360;
		return (r < 0) ? (r + 360) : r;
	}

	private float map180(float r) {
		// -180 < angle <= 180
		if (r <= -360 || 360 <= r)
			r %= 360;
		if (r <= -180)
			return r + 360;
		else if (r > 180)
			return r - 360;
		return r;
	}

}
