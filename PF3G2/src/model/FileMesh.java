package model;

import java.util.Random;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import math.Poly;
import math.Vector;



public class FileMesh {
	public static List<Poly> loadMesh(String fileName, Vector pos, float scale) {
		Random rand = new Random();
		
		List<Poly> mesh = new ArrayList<Poly>();
		Scanner fileScan;
		try {
			fileScan = new Scanner(new File(fileName));	
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return mesh;
		}
		
		List<Vector> vertices = new ArrayList<Vector>();
		while (fileScan.hasNext()) {
			String data = fileScan.nextLine();
			Scanner dataScan = new Scanner(data);
			if (dataScan.hasNext("v")) {
				dataScan.skip("v");
				float x = dataScan.nextFloat();
				float y = dataScan.nextFloat();
				float z = dataScan.nextFloat();
				
				vertices.add(new Vector(x, y, z).times(scale).plus(pos));
			} else if (dataScan.hasNext("f")) {
				dataScan.skip("f");
				List<Vector> polyVerts = new ArrayList<Vector>();
				dataScan.useDelimiter("[\\s/]");
				while (dataScan.hasNextInt()) {
					System.out.println("vert");
					int index = dataScan.nextInt() - 1;
					polyVerts.add(vertices.get(index));
					dataScan.next("");
					dataScan.nextInt();
				}
				
				Color color = new Color(0, 128 + rand.nextInt(128), 0);
				
				Vector[] vertArray = new Vector[polyVerts.size()];
				int i = 0;
				for (Vector vert : polyVerts) {
					vertArray[i] = vert;
					i++;
				}
				
				mesh.add(new Poly(vertArray, color));
			}
			dataScan.close();
		}
		
		fileScan.close();
		return mesh;
	}
}
