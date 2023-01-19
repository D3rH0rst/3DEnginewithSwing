package swing.java3d.max;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Mesh {
	private ArrayList<Triangle> mesh;

	public Mesh(Triangle[] tris) {
		mesh = new ArrayList<Triangle>(Arrays.asList(tris));
	}

	public Mesh(String filename) {
		mesh = new ArrayList<Triangle>();
		loadFromObjectFile(filename);
	}

	public ArrayList<Triangle> getTriangles() {
		return mesh;
	}

	private void loadFromObjectFile(String filename) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			while (br.ready()) {
				String line = br.readLine();
				String[] ln = line.split(" ");
				if (line.startsWith("v ")) {
					vertices.add(new Vertex(Float.parseFloat(ln[1]), Float.parseFloat(ln[2]), Float.parseFloat(ln[3])));
				} else if (line.startsWith("f ")) {
					mesh.add(new Triangle(vertices.get(Integer.valueOf(ln[1]) - 1), 
										  vertices.get(Integer.valueOf(ln[2]) - 1), 
										  vertices.get(Integer.valueOf(ln[3]) - 1)));
				}

			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

}
