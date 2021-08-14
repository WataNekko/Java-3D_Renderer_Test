package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import math.Vector3D;
import model.shape.Triangle3D;

public class ObjLoader {

	private ObjLoader() {

	}

	public static ArrayList<Triangle3D> loadObjModel(String filePath) throws IOException {
		if (!filePath.endsWith(".obj")) {
			throw new IOException("File is not an obj file");
		}

		FileReader fr = null;

		fr = new FileReader(filePath);

		BufferedReader reader = new BufferedReader(fr);
		String line;
		String[] currentLine;
		ArrayList<Vector3D> vertices = new ArrayList<>();
		ArrayList<Triangle3D> triangles = new ArrayList<>();

		try {

			while ((line = reader.readLine()) != null) {
				line = line.trim().replaceAll(" +", " ");

				if (line.startsWith("v ")) {
					currentLine = line.split(" ");

					vertices.add(new Vector3D(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));

				} else if (line.startsWith("f ")) {
					currentLine = line.split(" ");

					Vector3D v1 = vertices.get(Integer.parseInt(currentLine[1].split("/")[0]) - 1);

					// f 1//2 2//4 3//1 4//3 5//2 6//2...
					for (int i = 2; i < currentLine.length - 1; i++) {
						triangles.add(new Triangle3D(v1, vertices.get(Integer.parseInt(currentLine[i].split("/")[0]) - 1), vertices.get(Integer.parseInt(currentLine[i + 1].split("/")[0]) - 1)));
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return triangles;
	}

}
