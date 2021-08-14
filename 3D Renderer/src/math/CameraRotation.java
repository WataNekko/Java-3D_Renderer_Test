package math;

import model.Model3D;

public class CameraRotation extends Rotation {

	public CameraRotation() {
		super();
	}

	public CameraRotation(float rx, float ry, float rz) {
		super(rx, ry, rz);
	}

	public CameraRotation(Rotation r) {
		super(r);
	}

	public CameraRotation(Model3D m) {
		super(m);
	}

	// =========================================

	public void setRx(float rx) {
		this.rx = mapRX(rx);
	}

	public void rotateX(float rx) {
		if (rx != 0)
			this.rx = mapRX(this.rx + rx);
	}

	public void set(Rotation r) {
		if (r instanceof CameraRotation) {
			super.set(r);
		} else {
			setMapRxOnly(r.rx, r.ry, r.rz);
		}
	}

	public void set(Model3D m) {
		setMapRxOnly(m.getRotX(), m.getRotY(), m.getRotZ());
	}
	
	// =========================================

	private void setMapRxOnly(float rx, float ry, float rz) {
		this.setRx(rx);
		this.ry = ry;
		this.rz = rz;
	}

	private float mapRX(float r) {
		// -90 < angle <= 90
		if (r > 90)
			return 90;
		if (r < -90)
			return -90;
		return r;
	}

}
