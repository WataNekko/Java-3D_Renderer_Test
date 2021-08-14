package main;

import math.CameraRotation;
import math.Vector3D;
import model.Model3D;

public class Camera extends Model3D {
	private float fov, focalLength, renderDistance;
	public static final float DEFAULT_FOV = 70.0f;
	public static final float DEFAULT_FOCAL_LENGTH = 0.1f;
	public static final float DEFAULT_RENDER_DISTANCE = 250.0f;
	public static final String DEFAULT_NAME = "Camera";

	public static final float MOVEMENT_SPEED = 2.8f; // unit per second
	public static final float TURN_SPEED = 12f;

	// =============== Constructor ===============

	public Camera(float fov, float focalLength, float renderDistance, String name) {
		this(new CameraRotation(), fov, focalLength, renderDistance, name);
	}

	public Camera(Vector3D pos, float fov, float focalLength, float renderDistance, String name) {
		this(pos, new CameraRotation(), fov, focalLength, renderDistance, name);
	}

	public Camera(CameraRotation rot, float fov, float focalLength, float renderDistance, String name) {
		super(rot, name);
		setFov(fov);
		setFocalLength(focalLength);
		setRenderDistance(renderDistance);
	}

	public Camera(Vector3D pos, CameraRotation rot, float fov, float focalLength, float renderDistance, String name) {
		super(pos, rot, name);
		setFov(fov);
		setFocalLength(focalLength);
		setRenderDistance(renderDistance);
	}

	// ----------

	public Camera(String name) {
		this(DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, name);
	}

	public Camera(Vector3D pos, String name) {
		this(pos, DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, name);
	}

	public Camera(CameraRotation rot, String name) {
		this(rot, DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, name);
	}

	public Camera(Vector3D pos, CameraRotation rot, String name) {
		this(pos, rot, DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, name);
	}

	// -------------------------------------------

	public Camera(float fov, float focalLength, float renderDistance) {
		this(fov, focalLength, renderDistance, DEFAULT_NAME);
	}

	public Camera(Vector3D pos, float fov, float focalLength, float renderDistance) {
		this(pos, fov, focalLength, renderDistance, DEFAULT_NAME);
	}

	public Camera(CameraRotation rot, float fov, float focalLength, float renderDistance) {
		this(rot, fov, focalLength, renderDistance, DEFAULT_NAME);
	}

	public Camera(Vector3D pos, CameraRotation rot, float fov, float focalLength, float renderDistance) {
		this(pos, rot, fov, focalLength, renderDistance, DEFAULT_NAME);
	}

	// ----------

	public Camera() {
		this(DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, DEFAULT_NAME);
	}

	public Camera(Vector3D pos) {
		this(pos, DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, DEFAULT_NAME);
	}

	public Camera(CameraRotation rot) {
		this(rot, DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, DEFAULT_NAME);
	}

	public Camera(Vector3D pos, CameraRotation rot) {
		this(pos, rot, DEFAULT_FOV, DEFAULT_FOCAL_LENGTH, DEFAULT_RENDER_DISTANCE, DEFAULT_NAME);
	}

	// =========================================

	public void reset() {
		super.reset();
		this.fov = DEFAULT_FOV;
		this.focalLength = DEFAULT_FOCAL_LENGTH;
		this.renderDistance = DEFAULT_RENDER_DISTANCE;
	}

	public String toDebugString() {
		String debug = super.toDebugString();

		debug += "\tFOV: " + this.fov + "\n";
		debug += "\tFocal Length: " + this.focalLength + "\n";
		debug += "\tRender Distance: " + this.renderDistance + "\n";

		return debug;
	}

	// ============= Getter Setter =============

	public float getFov() {
		return fov;
	}

	public float getFocalLength() {
		return focalLength;
	}

	public float getRenderDistance() {
		return renderDistance;
	}

	public void setFov(float fov) {
		// 0 <= fov <= 180
		if (fov < 0)
			fov = 0;
		else if (fov > 180)
			fov = 180;
		this.fov = fov;
	}

	public void setFocalLength(float focalLength) {
		this.focalLength = Math.abs(focalLength);
	}

	public void setRenderDistance(float renderDistance) {
		this.renderDistance = Math.abs(renderDistance);
	}
	// =========================================

}
