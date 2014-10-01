package lavaGlobe;

public class Faces {
	// take in an array of (float, float[], float[][])
	// first is dist from camera?
	// second is angle for shadow?
	// third is points on the face.

	float[] a;
	float[][] b;
	float[][][] c;
	int total = 0;

	void addFace(float a, float[] b, float[][] c) {
		this.a = JaMa.appendFloatAr(this.a, a);
		this.b = JaMa.appendFloatArAr(this.b, b);
		this.c = JaMa.appendFloatArArAr(this.c, c);
		total++;
	}

	void sortLowToHigh() {
		// run through and find the lowest a's
		//
		// [0] = a
		// [1] = o
		// old number should be unimportant. No it is needed in order to shape
		// the other variable into order too.
		float[][] order = { { a[0], 0 } };
		for (int o = 1; o < a.length; o++) {
			boolean stuckIn = false;
			bloop: for (int l = 0; l < order.length; l++) {
				if (a[o] < order[l][0]) {
					// stick in before and kill loop
					order = JaMa.injectFloatArAr(order,
							new float[] { a[o], o }, l);
					stuckIn = true;
					break bloop;
				} else {
					// check the next
				}
			}
			if (stuckIn == false) {
				order = JaMa.appendFloatArAr(order, new float[] { a[o], o });
			}
		}
		for (int o = 0; o < order.length; o++) {
			this.a[o] = order[o][0];
		}

		float[][] tempB = new float[b.length][];
		// orders a now order b.
		for (int o = 0; o < order.length; o++) {
			tempB[o] = b[(int) order[o][1]];
		}
		this.b = tempB;
		float[][][] tempC = new float[c.length][][];
		// orders a now order b.
		for (int o = 0; o < order.length; o++) {
			tempC[o] = c[(int) order[o][1]];
		}
		this.c = tempC;
	}
}
