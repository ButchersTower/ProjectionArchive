package lavaGlobe;

public class Face {
	float a;
	float[] b;
	float[][] c;

	public Face(float a, float[] b, float[][] c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public Face(Face face) {
		this.a = face.a;
		this.b = face.b;
		this.c = face.c;
	}

	void addFace(float a, float[] b, float[][] c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
}
