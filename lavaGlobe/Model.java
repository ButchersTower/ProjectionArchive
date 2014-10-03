package ProjectionArchive.lavaGlobe;

public class Model {
	int[][] faces;
	float[][] nodes;

	float[] translation = { 200, 200, 0 };

	float[][] cubePoints = { { 1f, 1f, 1f }, { -1f, 1f, 1f }, { -1f, -1f, 1f },
			{ 1f, -1f, 1f }, { 1f, 1f, -1f }, { -1f, 1f, -1f },
			{ -1f, -1f, -1f }, { 1f, -1f, -1f } };
	// float[][] cubePoints = { { 2f, 2f, 2f }, { 0f, 2f, 2f }, { 0f, 0f, 2f },
	// { 2f, 0f, 2f }, { 2f, 2f, 0f }, { 0f, 2f, 0f }, { 0f, 0f, 0f },
	// { 2f, 0f, 0f } };

	float scalar = 1;

	// [2] = { 2, 1, 5, 6 }

	int[][] cubeFaces = { { 3, 2, 1, 0 }, { 4, 7, 3, 0 }, { 1, 2, 6, 5 },
			{ 0, 1, 5, 4 }, { 2, 3, 7, 6 }, { 4, 5, 6, 7 } };

	public Model() {

	}

	void setModel(Model mod) {
		this.faces = mod.faces;
		this.nodes = mod.nodes;
	}

	void createTorus(float r1, int n1, float r2, int n2) {
		// Create a torus along the y-axis, centered on (0,0)
		// The cross-rections has radius r1 and n1 vertices,
		// The torus has radius r2 and n2 cross-sections.

		float[] c;
		float dx, dy;
		int j, i;
		float theta = (float) Math.PI * 2 / n1;
		float[][] nodes = {};
		float[] node;

		for (j = 0; j < n2; j++) {
			c = new float[] { r2 * (float) Math.cos(theta * j), 0,
					r2 * (float) Math.sin(theta * j) };

			for (i = 0; i < n1; i++) {
				dx = r1 * (float) Math.cos(theta * i);
				dy = r1 * (float) Math.sin(theta * i);
				node = new float[] { c[0] + dx * (float) Math.cos(theta * j),
						c[1] + dy, c[2] + dx * (float) Math.sin(theta * j) };
				nodes = JaMa.appendFloatArAr(nodes, node);
			}
		}

		int[][] faces = {};
		for (j = 0; j < n2 - 1; j++) {
			int s = j * n1;
			for (i = 0; i < n1; i++) {
				if (i < n1 - 1) {
					int[] app = { s + i + 1, s + i, s + i + n1, s + i + n1 + 1 };
					faces = JaMa.appendIntArAr(faces, app);
				} else {
					int[] app = { s + 0, s + i, s + i + n1, s + n1 };
					faces = JaMa.appendIntArAr(faces, app);
				}
			}
		}

		for (int f1 = 0; f1 < faces.length; f1++) {
			System.out.print("faces[" + f1 + "] (");
			for (int f2 = 0; f2 < faces[f1].length; f2++) {
				if (f2 + 1 == faces[f1].length) {
					System.out.print(faces[f1][f2] + ")");
				} else {
					System.out.print(faces[f1][f2] + ", ");
				}
			}
			System.out.println();
		}

		// return new float[][] { nodes, faces };
		this.faces = faces;
		this.nodes = nodes;
	}

	void createCube() {
		faces = cubeFaces;
		nodes = cubePoints;
	}

	void translateXY(float tx, float ty) {
		for (int n = 0; n < nodes.length; n++) {
			nodes[n][0] += tx;
			nodes[n][1] += ty;
		}
	}

	float[][] getNodes() {
		return nodes;
	}

	int[][] getFaces() {
		return faces;
	}

	float[] getTranslation() {
		return translation;
	}

	float getScalar() {
		return scalar;
	}

}
