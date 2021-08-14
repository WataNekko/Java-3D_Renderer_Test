package model.shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import main.Camera;
import main.Display;
import math.Matrix;
import math.Vector3D;
import model.Mesh;

public class Triangle3D {
	private Vector3D[] p;

	// =============== Constructor ===============

	public Triangle3D(Vector3D p1, Vector3D p2, Vector3D p3) {
		this.p = new Vector3D[3];
		this.p[0] = p1;
		this.p[1] = p2;
		this.p[2] = p3;
	}

	// =========================================

	public void translate(float x, float y, float z) {
		this.p[0].translate(x, y, z);
		this.p[1].translate(x, y, z);
		this.p[2].translate(x, y, z);
	}

	public void translate(Vector3D v) {
		this.p[0].translate(v);
		this.p[1].translate(v);
		this.p[2].translate(v);
	}

	// =========================================

	public Vector3D getNormal() {
		Vector3D normal = Vector3D.crossProduct(p[0].getVector(p[1]), p[0].getVector(p[2]));
		normal.normalise();

		return normal;
	}

	public Vector3D getCentroid() {
		float x = (p[0].x + p[1].x + p[2].x) / 3;
		float y = (p[0].y + p[1].y + p[2].y) / 3;
		float z = (p[0].z + p[1].z + p[2].z) / 3;
		return new Vector3D(x, y, z);
	}

	public void draw(Mesh m, Display d, Camera cam, Graphics g) {
		// Get Model View triangle
		Triangle3D mvTri = new Triangle3D(Matrix.MODEL_VIEW_VECTOR(this.p[0], m, cam), Matrix.MODEL_VIEW_VECTOR(this.p[1], m, cam), Matrix.MODEL_VIEW_VECTOR(this.p[2], m, cam));

		// Check if triangle is visible (SOLID MESH)
		if (m.getRenderAttribute() == Mesh.SOLID) {
			Vector3D normal = mvTri.getNormal();

			if (Vector3D.dotProduct(normal, mvTri.p[0]) >= 0)
				return;
		}

		// Map into frustum
		Vector3D[] projPoints = new Vector3D[3];

		for (int i = 0; i < 3; i++) {
			float[][] v = Matrix.PROJECTED_VECTOR(mvTri.p[i], d, cam);

			float x = v[0][0];
			float y = v[1][0];
			float z = v[2][0];
			float w = v[3][0];

			if (w != 0 && w != 1) {
				x /= w;
				y /= w;
				z /= w;
			}

			projPoints[i] = new Vector3D(x, y, z);
		}

		Triangle3D projTri = new Triangle3D(projPoints[0], projPoints[1], projPoints[2]);

		// Stop rendering if triangle is outside the frustum
		Vector3D cen = projTri.getCentroid();

		if (cen.x < -1 || 1 < cen.x || cen.y < -1 || 1 < cen.y || cen.z < 0 || 1 < cen.z)
			return;

		// Render
		Polygon poly = new Polygon();

		for (int i = 0; i < 3; i++) {
			float x = projTri.p[i].x;
			float y = projTri.p[i].y;

			x *= d.width() / 2f;
			y *= -d.height() / 2f;

			poly.addPoint((int) x, (int) y);
		}

		Graphics2D g2 = (Graphics2D) g;

		float z = (1 - cen.z);
//		System.out.println(z);
		Color c = m.getColor();

//		float z = -Vector3D.getVector(new Vector3D(), Matrix.MODEL_VIEW_VECTOR(this.getCentroid(),m,d)).length() / 20 + 1;
//		if (z < 0)
//			z = 0;
		// Color (Shade base on distance)
//		g2.setColor(new Color((int) (c.getRed() * z), (int) (c.getGreen() * z), (int) (c.getBlue() * z)));
//		g2.fillPolygon(poly);

		// Stroke Width (Decrease base on distance)
		float strokeWidth = 75 * z * d.height() / 600;
		if (strokeWidth < 0)
			strokeWidth = 0;

		g2.setColor(c);
		g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.draw(poly);
	}

}
