package math;

import main.Camera;
import main.Display;
import model.Mesh;

public class Matrix {
	
	private Matrix() {
		
	}
	
	// =============== Matrices ===============

	public static final float[][] TRANSLATION(float x, float y, float z) {
		return new float[][] {
			{ 1, 0, 0, x },
			{ 0, 1, 0, y },
			{ 0, 0, 1, z },
			{ 0, 0, 0, 1 }
		};
	}

	public static final float[][] SCALE(float x, float y, float z) {
		return new float[][] {
			{ x, 0, 0, 0 },
			{ 0, y, 0, 0 },
			{ 0, 0, z, 0 },
			{ 0, 0, 0, 1 }
		};
	}

	public static final float[][] ROTATION_X(float a) {
		return new float[][] {
			{ 1, 0, 0, 0 },
			{ 0, (float) Math.cos(-Math.toRadians(a)), (float) -Math.sin(-Math.toRadians(a)), 0 },
			{ 0, (float) Math.sin(-Math.toRadians(a)), (float) Math.cos(-Math.toRadians(a)), 0 },
			{ 0, 0, 0, 1 }
		};
	}

	public static final float[][] ROTATION_Y(float a) {
		return new float[][] {
			{ (float) Math.cos(Math.toRadians(a)), 0, (float) Math.sin(Math.toRadians(a)), 0 },
			{ 0, 1, 0, 0 },
			{ (float) -Math.sin(Math.toRadians(a)), 0, (float) Math.cos(Math.toRadians(a)), 0 },
			{ 0, 0, 0, 1 }
		};
	}

	public static final float[][] ROTATION_Z(float a) {
		return new float[][] {
			{ (float) Math.cos(-Math.toRadians(a)), (float) -Math.sin(-Math.toRadians(a)), 0, 0 },
			{ (float) Math.sin(-Math.toRadians(a)), (float) Math.cos(-Math.toRadians(a)), 0, 0 },
			{ 0, 0, 1, 0 },
			{ 0, 0, 0, 1 }
		};
	}
	
	public static final float[][] ROTATION(float rx, float ry, float rz){
		return mult4x4mat(ROTATION_Z(rz), ROTATION_X(rx), ROTATION_Y(ry));
	}
	
	// ----------------------------------------
	
	public static final float[][] MODEL_TO_WORLD(Mesh m) {
		return mult4x4mat(mult4x4mat(TRANSLATION(m.getPosX(), m.getPosY(), m.getPosZ()), ROTATION(m.getRotX(), m.getRotY(), m.getRotZ())), SCALE(m.getScale(), m.getScale(), m.getScale()));
	}
	
	public static final float[][] WORLD_TO_VIEW(Camera cam) {
		return mult4x4mat(ROTATION(-cam.getRotX(), -cam.getRotY(), -cam.getRotZ()), TRANSLATION(-cam.getPosX(), -cam.getPosY(), -cam.getPosZ()));
	}
	
	public static final float[][] VIEW_TO_PROJECTION(Display d, Camera cam) {
		float aspectRatio = (float) d.height() / d.width();
		float scale = (float) (1 / Math.tan(Math.toRadians(cam.getFov() / 2)));
		float zNear = cam.getFocalLength();
		float zFar = cam.getRenderDistance();

		return new float[][] {
			{ aspectRatio * scale, 0.0f, 0.0f, 0.0f },
			{ 0.0f, scale, 0.0f, 0.0f },
			{ 0.0f, 0.0f, zFar / (zFar - zNear), -(zFar * zNear) / (zFar - zNear) },
			{ 0.0f, 0.0f, 1.0f, 0.0f }
		};
	}
	
	public static void test(Vector3D v) {
		float[][] p = multVec(mult4x4mat(new float[][] {
			{ (float) (1 * (1 / Math.tan(Math.toRadians(90 / 2)))), 0.0f, 0.0f, 0.0f },
			{ 0.0f, (float) (1 / Math.tan(Math.toRadians(90 / 2))), 0.0f, 0.0f },
			{ 0.0f, 0.0f, 250 / (250 - .1f), -(250 * .1f) / (250 - .1f) },
			{ 0.0f, 0.0f, 1.0f, 0.0f }
		}, mult4x4mat(
				mult4x4mat(ROTATION(-0, -0, -0), TRANSLATION(-0, -0, -0)),
				mult4x4mat(mult4x4mat(TRANSLATION(0, 0, 0), ROTATION(0, 0, 0)), SCALE(1, 1, 1)))),
				v);
		float x = p[0][0];
		float y = p[1][0];
		float z = p[2][0];
		float w = p[3][0];
		
		System.out.println(x + " " + y + " " + z + " " + w);
		
		if(w != 0) {
			x/=w;
			y/=w;
			z/=w;
		}
		System.out.println(x + " " + y + " " + z);
	}
	
	public static final float[][] MODEL_VIEW_PROJECTION(Mesh m, Display d, Camera cam) {
		return mult4x4mat(VIEW_TO_PROJECTION(d, cam), mult4x4mat(WORLD_TO_VIEW(cam), MODEL_TO_WORLD(m)));
	}
	
	// ----------------------------------------
	
	public static final Vector3D MODEL_VIEW_VECTOR(Vector3D v, Mesh m, Camera cam) {
		float[][] r = multVec(mult4x4mat(WORLD_TO_VIEW(cam), MODEL_TO_WORLD(m)), v);
		return new Vector3D(r[0][0], r[1][0], r[2][0]);
	}
	
	public static final float[][] PROJECTED_VECTOR(Vector3D v, Display d, Camera cam) {
		return multVec(VIEW_TO_PROJECTION(d, cam), v);
	}
	
	// ----------------------------------------
	
	public static Vector3D getRotatedVector(float x, float y, float z, float rx, float ry, float rz) {
		float[][] v = multVec(ROTATION(rx, ry, rz), x, y, z);
		return new Vector3D(v[0][0], v[1][0], v[2][0]);
	}
	
	public static Vector3D getRotatedVector(Vector3D v, Rotation r) {
		return getRotatedVector(v.x, v.y, v.z, r.getRx(), r.getRy(), r.getRz());
	}
	
	public static Vector3D getRotatedVector(Vector3D v, float rx, float ry, float rz) {
		return getRotatedVector(v.x, v.y, v.z, rx, ry, rz);
	}
	
	public static Vector3D getRotatedVector(float x, float y, float z, Rotation r) {
		return getRotatedVector(x, y, z, r.getRx(), r.getRy(), r.getRz());
	}

	// ========================================
	
	private static float[][] mult4x4mat(float[][] a, float[][] b) {
			// m x n * n x p = m x p
			float[][] result = new float[4][4];

			for (int m = 0; m < 4; m++) {
				for (int p = 0; p < 4; p++) {
					for (int n = 0; n < 4; n++) {
						result[m][p] += a[m][n] * b[n][p];
					}
				}
			}

			return result;
	}
	
	private static float[][] mult4x4mat(float[][] a, float[][] b, float[][] c) {
		return mult4x4mat(mult4x4mat(a, b), c);
	}
	
	private static float[][] multVec(float[][] a, float x, float y, float z) {
		return new float[][] {
			{ a[0][0] * x + a[0][1] * y + a[0][2] * z + a[0][3] },
			{ a[1][0] * x + a[1][1] * y + a[1][2] * z + a[1][3] },
			{ a[2][0] * x + a[2][1] * y + a[2][2] * z + a[2][3] },
			{ a[3][0] * x + a[3][1] * y + a[3][2] * z + a[3][3] }
		};
	}
	
	private static float[][] multVec(float[][] a, Vector3D v) {
		return multVec(a, v.x, v.y, v.z);
	}
	
	// ========================================

	public static boolean isMatrix(float[][] a) {

		try {

			if (a.length == 0 || a[0].length == 0) {
				return false;
			} else {
				for (int i = 1; i < a.length; i++) {

					if (a[i].length != a[0].length)
						return false;

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		}

		return true;
	}

	public static boolean isMatrix(float[][]... a) {
		for (float[][] m : a) {
			if (!isMatrix(m))
				return false;
		}
		return true;
	}

	public static float[][] multiply(float[][] a, float[][] b) {
		// Check if possible to multiply
		if (isMatrix(a, b) && (a[0].length == b.length)) {
			// m x n * n x p = m x p
			int M = a.length;
			int N = b.length;
			int P = b[0].length;

			float[][] result = new float[M][P];

			for (int m = 0; m < M; m++) {
				for (int p = 0; p < P; p++) {
					for (int n = 0; n < N; n++) {
						result[m][p] += a[m][n] * b[n][p];
					}
				}
			}

			return result;

		} else {
			throw new ArithmeticException();
		}

	}

	public static String toString(float[][] a) {
		if (isMatrix(a)) {
			String string = "";
			for (float[] row : a) {

				for (float col : row) {
					string += col + "\t";
				}
				string += "\n";

			}

			return string;
		} else {
			throw new ArithmeticException();
		}
	}

}
