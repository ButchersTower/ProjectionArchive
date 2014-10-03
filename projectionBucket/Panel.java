package ProjectionArchive.projectionBucket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable, KeyListener {

	// Easy method for basic shapes

	// draws negative images backwards.

	// when negating a rotation it fucks up when passing over y = 0

	// 'A' 'D' Is rotating around center point not camera
	// location................

	// 'A' 'D' Is inveting when passing over y axis

	// If from the angle it is looking at if the

	// 'D'ing over the y axis negates (fuckin why dawg??)

	// 'W' 'S' varies the flat objects distance from the camera, it should vary
	// the 3d objects z axis.

	// 'G' Doesnt work on objects that have a world rotation.

	/**
	 * When dealing with rotations over the X or Y axis it takes the initail
	 * point and remember where it should be from that, instead of when it is
	 * calculating the flat position from keeping track of it.
	 */

	int width = 800;
	int height = 600;

	float cameraX = 0;
	float cameraY = 0;
	float cameraZ = 0;

	float worldRotation = 0;

	// x, y ,z
	// horizontal, vertical, depth.
	// float[][] point = { { -10, -20, 8 }, { 30, -20, 8 }, { 30, -20, 10 },
	// { -10, -20, 10 }, { -10, 40, 8 }, { 30, 40, 8 }, { 30, 40, 10 },
	// { -10, 40, 10 } };

	// float[][] point = { { 15, 10, 20 }, { 30, 10, 20 }, { 30, 10, 40 },
	// { 15, 10, 40 }, { 15, 30, 20 }, { 30, 30, 20 }, { 30, 30, 40 },
	// { 15, 30, 40 } };

	float[][] point = { { 40, 10, 200 }, { 70, 10, 200 }, { 70, 10, 220 },
			{ 40, 10, 220 }, { 40, 40, 200 }, { 70, 40, 200 }, { 70, 40, 220 },
			{ 40, 40, 220 } };

	// float[][] ballPoints = { { 0, 20, 0 }, { 14f, 14f, 0 }, { 20, 0, 0 }, {
	// 14f, -14f, 0 }, { 0, -20, 0 }, { -14f, -14f, 0 }, { -20, 0, 0 }, { -14f,
	// 14f, 0 } };

	/**
	 * PROBLEMS with when the ball crosses the into the negative z axis (even
	 * when the -z seems to invert the y
	 */

	float[][] ballPoints = { { 20, 20, 0 }, { 34f, 14f, 0 }, { 40, 0, 0 },
			{ 34f, -14f, 0 }, { 20, -20, 0 }, { 6f, -14f, 0 }, { 0, 0, 0 },
			{ 6f, 14f, 0 } };
	float[][] ballPoints2 = { { 20, 20, 0 }, { 30f, 14f, 10 }, { 34, 0, 14 },
			{ 30f, -14f, 10 }, { 20, -20, 0 }, { 10f, 14f, -10 },
			{ 6, 0, -14 }, { 10f, -14f, -10 } };
	float[][] ballPoints3 = { { 20, 20, 0 }, { 20, 14f, 14f }, { 20, 0, 20 },
			{ 20, -14f, 14f }, { 20, -20, 0 }, { 20, -14f, -14f },
			{ 20, 0, -20 }, { 20, 14f, -14f } };

	// float[][] point = { { 10, -5, 20 }, { 20, -5, 20 }, { 20, -5, 40 },
	// { 10, -5, 40 }, { 10, 15, 20 }, { 20, 15, 20 }, { 20, 15, 40 },
	// { 10, 15, 40 } };

	float[] centPoint = { 0, 300 };

	float[][] flatPoints = new float[8][2];

	Image[] imageAr;

	Thread thread;
	Image image;
	Graphics g;

	// Vars for gLoop Below
	public int tps = 20;
	public int milps = 1000 / tps;
	long lastTick = 0;
	int sleepTime = 0;
	long lastSec = 0;
	int ticks = 0;
	long startTime;
	long runTime;
	private long nextTick = 0;
	private boolean running = false;

	// Vars for gLoop Above

	public Panel() {
		super();

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.setSize(new Dimension(width, height));

		addKeyListener(this);

		startTime = System.currentTimeMillis();

		gStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	public void gStart() {
		imageInit();

		for (int i = 0; i < point.length; i++) {
			flatPoints[i][0] = (point[i][0] / point[i][2]) * 4;
			flatPoints[i][1] = -(point[i][1] / point[i][2]) * 4;
			System.out.println("flatP: " + flatPoints[i][0] + ", "
					+ flatPoints[i][1] + ")");
		}

		running = true;
		gLoop2();
	}

	void flatten() {
		float[][] tempPoints = new float[point.length][];
		for (int p = 0; p < tempPoints.length; p++) {
			tempPoints[p] = point[p];
		}
		for (int p = 0; p < tempPoints.length; p++) {

			// When goint over the y axis (x becoming negative of postivie) it
			// inverts teh y of the flat point;

			// System.out.println("tot: " + p + ":  " + point[p][0] + ", "
			// + point[p][1] + ", " + point[p][2]);

			// System.out.println("pop: " + p + ":  "
			// + rotateints(tempPoints[p], worldRotation)[0] + ", "
			// + rotateints(tempPoints[p], worldRotation)[1] + ", "
			// + rotateints(tempPoints[p], worldRotation)[2]);

			float ox = point[0][0];
			float oy = point[0][1];
			float oz = point[0][2];

			float hyp1 = (float) Math.sqrt(Math.pow(ox, 2) + Math.pow(oz, 2));

			System.out.println("hyp1: " + hyp1);

			float thea1 = (float) Math.atan(oz / ox);

			System.out.println("thea1: " + thea1);

			// float[] test1 = getXZ(hyp1, thea1, oy);
			// System.out.println("test1: " + test1[0] + ", " + test1[1] + ", "
			// + test1[2]);
			// System.out.println("poin1: " + ox + ", " + oy + ", " + oz);

			tempPoints[p] = new float[] {
					rotateints(point[p], worldRotation)[0],
					rotateints(point[p], worldRotation)[1],
					rotateints(point[p], worldRotation)[2] };

			// System.out.println("tP's: " + tempPoints[p][0] + ", " +
			// tempPoints[p][1] + ", " + tempPoints[p][2]);
			boolean xIsNeg = false;
			if (point[p][0] < 0) {
				xIsNeg = true;
				System.out.println("xIN");
			}
			boolean zIsNeg = false;
			if (point[p][2] < 0) {
				System.out.println("zIN");
				zIsNeg = true;
			}

			// System.out.println("cX: (" + cameraX + ", " + cameraY + ", "
			// + cameraZ + ")");

			float theaOfThisPoint = (float) Math.atan(Math
					.abs((point[p][2] - cameraZ))
					/ Math.abs((point[p][0] - cameraX)));

			float hypThis = (float) Math.sqrt(Math.pow((point[p][0] - cameraX),
					2) + Math.pow((point[p][2] - cameraZ), 2));

			float worldRotAndThisThea = theaOfThisPoint + worldRotation;

			System.out.println("hypThis: " + hypThis + "   worldRot+Theta: "
					+ worldRotAndThisThea + "    RelativeY: "
					+ (point[p][1] - cameraY));

			// rotated around camLoc by worldRot.
			tempPoints[p] = getXZ(hypThis, worldRotAndThisThea,
					(point[p][1] - cameraY));

			// get world thea of point and add worldRot then get new rot.

			System.out.println("tempP[" + p + "] (" + tempPoints[p][0] + ", "
					+ tempPoints[p][1] + ", " + tempPoints[p][2] + ")");

			if (xIsNeg) {
				flatPoints[p][0] = -((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatPoints[p][0] = ((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			if (zIsNeg) {
				flatPoints[p][1] = ((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatPoints[p][1] = -((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			// System.out.println("flatP: " + flatPoints[i][0] + ", " +
			// flatPoints[i][1] + ")");
			System.out.println("normPoint[" + p + "]: (" + point[p][0] + ", "
					+ point[p][1] + ", " + point[p][2]);
			System.out.println("flatPoints[" + p + "][0]" + flatPoints[p][0]);
			System.out.println("flatPoints[" + p + "][1]" + flatPoints[p][1]);
		}
	}

	void newFlatBall(float[][] ballPoint) {
		float[][] tempPt = new float[8][3];
		float[][] flatTemp = new float[8][2];
		for (int i = 0; i < tempPt.length; i++) {
			tempPt[i][0] = ballPoint[i][0] - cameraX;
			tempPt[i][1] = ballPoint[i][1] - cameraY;
			tempPt[i][2] = ballPoint[i][2] - cameraZ;

			float hyp = ((tempPt[i][0] * tempPt[i][0]) * (tempPt[i][2] * tempPt[i][2]));

			float thea = (float) Math.atan(tempPt[i][2] / tempPt[i][0]);
			thea += worldRotation;

			// tempPt[i] = getXZ(hyp, thea, tempPt[i][1]);

			flatTemp[i][0] = (tempPt[i][0] / tempPt[i][2]) * 40;
			flatTemp[i][1] = (tempPt[i][1] / tempPt[i][2]) * 40;

			System.out.println("flat3: " + flatTemp[i][0] + ", "
					+ flatTemp[i][1]);
		}

		for (int i = 0; i < 7; i++) {
			g.drawLine((int) (flatTemp[i][0] * 20) + 400,
					(int) (flatTemp[i][1] * 20) + 300,
					(int) (flatTemp[i + 1][0] * 20) + 400,
					(int) (flatTemp[i + 1][1] * 20) + 300);
		}
		g.drawLine((int) (flatTemp[7][0]) + 400, (int) (flatTemp[7][1]) + 300,
				(int) (flatTemp[0][0]) + 400, (int) (flatTemp[0][1]) + 300);

	}

	void newFlatBall() {
		float[][] tempPt = new float[8][3];
		float[][] flatTemp = new float[8][2];
		for (int i = 0; i < tempPt.length; i++) {
			tempPt[i][0] = ballPoints3[i][0] - cameraX;
			tempPt[i][1] = ballPoints3[i][1] - cameraY;
			tempPt[i][2] = ballPoints3[i][2] - cameraZ;

			float hyp = ((tempPt[i][0] * tempPt[i][0]) * (tempPt[i][2] * tempPt[i][2]));

			float thea = (float) Math.atan(tempPt[i][2] / tempPt[i][0]);
			thea += worldRotation;

			// tempPt[i] = getXZ(hyp, thea, tempPt[i][1]);

			flatTemp[i][0] = (tempPt[i][0] / tempPt[i][2]) * 40;
			flatTemp[i][1] = (tempPt[i][1] / tempPt[i][2]) * 40;

			System.out.println("flat3: " + flatTemp[i][0] + ", "
					+ flatTemp[i][1]);
		}

		for (int i = 0; i < 7; i++) {
			g.drawLine((int) (flatTemp[i][0] * 20) + 400,
					(int) (flatTemp[i][1] * 20) + 300,
					(int) (flatTemp[i + 1][0] * 20) + 400,
					(int) (flatTemp[i + 1][1] * 20) + 300);
		}
		g.drawLine((int) (flatTemp[7][0]) + 400, (int) (flatTemp[7][1]) + 300,
				(int) (flatTemp[0][0]) + 400, (int) (flatTemp[0][1]) + 300);

	}

	void flattenBall() {
		float[][] flatBall = new float[8][2];
		float[][] tempPoints = new float[ballPoints.length][];
		for (int p = 0; p < tempPoints.length; p++) {
			tempPoints[p] = ballPoints[p];
		}
		for (int p = 0; p < tempPoints.length; p++) {
			float ox = ballPoints[0][0];
			float oy = ballPoints[0][1];
			float oz = ballPoints[0][2];

			float hyp1 = (float) Math.sqrt(Math.pow(ox, 2) + Math.pow(oz, 2));

			System.out.println("hyp1: " + hyp1);

			float thea1 = (float) Math.atan(oz / ox);

			System.out.println("thea1: " + thea1);

			tempPoints[p] = new float[] {
					rotateints(ballPoints[p], worldRotation)[0],
					rotateints(ballPoints[p], worldRotation)[1],
					rotateints(ballPoints[p], worldRotation)[2] };

			// System.out.println("tP's: " + tempPoints[p][0] + ", " +
			// tempPoints[p][1] + ", " + tempPoints[p][2]);
			boolean xIsNeg = false;
			if (ballPoints[p][0] < 0) {
				xIsNeg = true;
				System.out.println("xIN");
			}
			boolean zIsNeg = false;
			if (ballPoints[p][2] < 0) {
				System.out.println("zIN");
				zIsNeg = true;
			}

			// System.out.println("cX: (" + cameraX + ", " + cameraY + ", "
			// + cameraZ + ")");

			float theaOfThisPoint = (float) Math.atan(Math
					.abs((ballPoints[p][2] - cameraZ))
					/ Math.abs((ballPoints[p][0] - cameraX)));

			float hypThis = (float) Math.sqrt(Math.pow(
					(ballPoints[p][0] - cameraX), 2)
					+ Math.pow((ballPoints[p][2] - cameraZ), 2));

			float worldRotAndThisThea = theaOfThisPoint + worldRotation;

			System.out.println("hypThis: " + hypThis + "   worldRot+Theta: "
					+ worldRotAndThisThea + "    RelativeY: "
					+ (ballPoints[p][1] - cameraY));

			// rotated around camLoc by worldRot.
			tempPoints[p] = getXZ(hypThis, worldRotAndThisThea,
					(ballPoints[p][1] - cameraY));

			// get world thea of point and add worldRot then get new rot.

			System.out.println("tempP[" + p + "] (" + tempPoints[p][0] + ", "
					+ tempPoints[p][1] + ", " + tempPoints[p][2] + ")");

			if (xIsNeg) {
				flatBall[p][0] = -((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatBall[p][0] = ((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			if (zIsNeg) {
				flatBall[p][1] = ((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatBall[p][1] = -((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			// System.out.println("flatP: " + flatPoints[i][0] + ", " +
			// flatPoints[i][1] + ")");
			System.out.println("normPoint[" + p + "]: (" + ballPoints[p][0]
					+ ", " + ballPoints[p][1] + ", " + ballPoints[p][2]);
			System.out.println("flatPoints[" + p + "][0]" + flatBall[p][0]);
			System.out.println("flatPoints[" + p + "][1]" + flatBall[p][1]);

			g.setColor(Color.WHITE);

			/*
			 * g.drawLine((int) flatBall[0][0], (int) flatBall[0][1],(int)
			 * flatBall[1][0], (int) flatBall[1][1]); g.drawLine((int)
			 * flatBall[1][0], (int) flatBall[1][1],(int) flatBall[2][0], (int)
			 * flatBall[2][1]); g.drawLine((int) flatBall[2][0], (int)
			 * flatBall[2][1],(int) flatBall[3][0], (int) flatBall[3][1]);
			 * g.drawLine((int) flatBall[3][0], (int) flatBall[3][1],(int)
			 * flatBall[4][0], (int) flatBall[4][1]); g.drawLine((int)
			 * flatBall[4][0], (int) flatBall[4][1],(int) flatBall[5][0], (int)
			 * flatBall[5][1]); g.drawLine((int) flatBall[5][0], (int)
			 * flatBall[5][1],(int) flatBall[6][0], (int) flatBall[6][1]);
			 * g.drawLine((int) flatBall[6][0], (int) flatBall[6][1],(int)
			 * flatBall[7][0], (int) flatBall[7][1]); g.drawLine((int)
			 * flatBall[7][0], (int) flatBall[7][1],(int) flatBall[0][0], (int)
			 * flatBall[0][1]);
			 */
		}
		for (int i = 0; i < 7; i++) {
			g.drawLine((int) (flatBall[i][0] * 10) + 400,
					(int) (flatBall[i][1] * 10) + 300,
					(int) (flatBall[i + 1][0] * 10) + 400,
					(int) (flatBall[i + 1][1] * 10) + 300);
			// System.out.println("drw: ("+ ((int) (flatPoints[i][0] * 10) +
			// 400) + ", "+ ((int) (flatPoints[i][1] * 10) + 300) + "), ("+
			// ((int) (flatPoints[i + 1][0] * 10) + 400) + ", "+ ((int)
			// (flatPoints[i + 1][1] * 10) + 300) + ")");
		}
		g.drawLine((int) (flatBall[7][0] * 10) + 400,
				(int) (flatBall[7][1] * 10) + 300,
				(int) (flatBall[0][0] * 10) + 400,
				(int) (flatBall[0][1] * 10) + 300);

	}

	void flattenBall2() {
		float[][] flatBall = new float[8][2];
		float[][] tempPoints = new float[ballPoints2.length][];
		for (int p = 0; p < tempPoints.length; p++) {
			tempPoints[p] = ballPoints2[p];
		}
		for (int p = 0; p < tempPoints.length; p++) {
			float ox = ballPoints2[p][0];
			float oy = ballPoints2[p][1];
			float oz = ballPoints2[p][2];

			float hyp1 = (float) Math.sqrt(Math.pow(ox, 2) + Math.pow(oz, 2));

			System.out.println("hyp1: " + hyp1);

			float thea1 = (float) Math.atan(oz / ox);

			System.out.println("thea1: " + thea1);

			tempPoints[p] = new float[] {
					rotateints(ballPoints2[p], worldRotation)[0],
					rotateints(ballPoints2[p], worldRotation)[1],
					rotateints(ballPoints2[p], worldRotation)[2] };

			// System.out.println("tP's: " + tempPoints[p][0] + ", " +
			// tempPoints[p][1] + ", " + tempPoints[p][2]);
			boolean xIsNeg = false;
			if (ballPoints2[p][0] < 0) {
				xIsNeg = true;
				System.out.println("xIN");
			}
			boolean zIsNeg = false;
			if (ballPoints2[p][2] < 0) {
				System.out.println("zIN");
				zIsNeg = true;
			}

			// System.out.println("cX: (" + cameraX + ", " + cameraY + ", "
			// + cameraZ + ")");

			float theaOfThisPoint = (float) Math.atan(Math
					.abs((ballPoints2[p][2] - cameraZ))
					/ Math.abs((ballPoints2[p][0] - cameraX)));

			float hypThis = (float) Math.sqrt(Math.pow(
					(ballPoints2[p][0] - cameraX), 2)
					+ Math.pow((ballPoints2[p][2] - cameraZ), 2));

			float worldRotAndThisThea = theaOfThisPoint + worldRotation;

			System.out.println("hypThis: " + hypThis + "   worldRot+Theta: "
					+ worldRotAndThisThea + "    RelativeY: "
					+ (ballPoints2[p][1] - cameraY));

			// rotated around camLoc by worldRot.
			tempPoints[p] = getXZ(hypThis, worldRotAndThisThea,
					(ballPoints2[p][1] - cameraY));

			// get world thea of point and add worldRot then get new rot.

			System.out.println("tempP[" + p + "] (" + tempPoints[p][0] + ", "
					+ tempPoints[p][1] + ", " + tempPoints[p][2] + ")");

			if (xIsNeg) {
				flatBall[p][0] = -((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatBall[p][0] = ((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			if (zIsNeg) {
				flatBall[p][1] = ((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatBall[p][1] = -((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			// System.out.println("flatP: " + flatPoints[i][0] + ", " +
			// flatPoints[i][1] + ")");
			System.out.println("normPoint[" + p + "]: (" + ballPoints2[p][0]
					+ ", " + ballPoints2[p][1] + ", " + ballPoints2[p][2]);
			System.out.println("flatPoints[" + p + "][0]" + flatBall[p][0]);
			System.out.println("flatPoints[" + p + "][1]" + flatBall[p][1]);

			g.setColor(Color.WHITE);

			/*
			 * g.drawLine((int) flatBall[0][0], (int) flatBall[0][1],(int)
			 * flatBall[1][0], (int) flatBall[1][1]); g.drawLine((int)
			 * flatBall[1][0], (int) flatBall[1][1],(int) flatBall[2][0], (int)
			 * flatBall[2][1]); g.drawLine((int) flatBall[2][0], (int)
			 * flatBall[2][1],(int) flatBall[3][0], (int) flatBall[3][1]);
			 * g.drawLine((int) flatBall[3][0], (int) flatBall[3][1],(int)
			 * flatBall[4][0], (int) flatBall[4][1]); g.drawLine((int)
			 * flatBall[4][0], (int) flatBall[4][1],(int) flatBall[5][0], (int)
			 * flatBall[5][1]); g.drawLine((int) flatBall[5][0], (int)
			 * flatBall[5][1],(int) flatBall[6][0], (int) flatBall[6][1]);
			 * g.drawLine((int) flatBall[6][0], (int) flatBall[6][1],(int)
			 * flatBall[7][0], (int) flatBall[7][1]); g.drawLine((int)
			 * flatBall[7][0], (int) flatBall[7][1],(int) flatBall[0][0], (int)
			 * flatBall[0][1]);
			 */
		}
		for (int i = 0; i < 7; i++) {
			g.drawLine((int) (flatBall[i][0] * 10) + 400,
					(int) (flatBall[i][1] * 10) + 300,
					(int) (flatBall[i + 1][0] * 10) + 400,
					(int) (flatBall[i + 1][1] * 10) + 300);
			// System.out.println("drw: ("+ ((int) (flatPoints[i][0] * 10) +
			// 400) + ", "+ ((int) (flatPoints[i][1] * 10) + 300) + "), ("+
			// ((int) (flatPoints[i + 1][0] * 10) + 400) + ", "+ ((int)
			// (flatPoints[i + 1][1] * 10) + 300) + ")");
		}
		g.drawLine((int) (flatBall[7][0] * 10) + 400,
				(int) (flatBall[7][1] * 10) + 300,
				(int) (flatBall[0][0] * 10) + 400,
				(int) (flatBall[0][1] * 10) + 300);

	}

	void flattenBall3() {
		float[][] flatBall = new float[8][2];
		float[][] tempPoints = new float[ballPoints3.length][];
		for (int p = 0; p < tempPoints.length; p++) {
			tempPoints[p] = ballPoints3[p];
		}
		for (int p = 0; p < tempPoints.length; p++) {
			float ox = ballPoints3[p][0];
			float oy = ballPoints3[p][1];
			float oz = ballPoints3[p][2];

			float hyp1 = (float) Math.sqrt(Math.pow(ox, 2) + Math.pow(oz, 2));

			System.out.println("hyp1: " + hyp1);

			float thea1 = (float) Math.atan(oz / ox);

			System.out.println("thea1: " + thea1);

			tempPoints[p] = new float[] {
					rotateints(ballPoints3[p], worldRotation)[0],
					rotateints(ballPoints3[p], worldRotation)[1],
					rotateints(ballPoints3[p], worldRotation)[2] };

			// System.out.println("tP's: " + tempPoints[p][0] + ", " +
			// tempPoints[p][1] + ", " + tempPoints[p][2]);
			boolean xIsNeg = false;
			if (ballPoints3[p][0] < 0) {
				xIsNeg = true;
				System.out.println("xIN");
			}
			boolean zIsNeg = false;
			if (ballPoints3[p][2] < 0) {
				System.out.println("zIN");
				zIsNeg = true;
			}

			// System.out.println("cX: (" + cameraX + ", " + cameraY + ", "
			// + cameraZ + ")");

			/**
			 * WORKING HERE
			 */

			float theaOfThisPoint = (float) Math.atan(Math
					.abs((ballPoints3[p][2] - cameraZ))
					/ Math.abs((ballPoints3[p][0] - cameraX)));

			float hypThis = (float) Math.sqrt(Math.pow(
					(ballPoints3[p][0] - cameraX), 2)
					+ Math.pow((ballPoints3[p][2] - cameraZ), 2));

			float worldRotAndThisThea = theaOfThisPoint + worldRotation;

			System.out.println("hypThis: " + hypThis + "   worldRot+Theta: "
					+ worldRotAndThisThea + "    RelativeY: "
					+ (ballPoints3[p][1] - cameraY));

			// rotated around camLoc by worldRot.
			tempPoints[p] = getXZ(hypThis, worldRotAndThisThea,
					(ballPoints3[p][1] - cameraY));

			// get world thea of point and add worldRot then get new rot.

			System.out.println("tempP[" + p + "] (" + tempPoints[p][0] + ", "
					+ tempPoints[p][1] + ", " + tempPoints[p][2] + ")");

			if (xIsNeg) {
				flatBall[p][0] = -((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatBall[p][0] = ((tempPoints[p][0] - cameraX) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			if (zIsNeg) {
				flatBall[p][1] = ((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			} else {
				flatBall[p][1] = -((tempPoints[p][1] - cameraY) / (tempPoints[p][2] - cameraZ)) * 40;
			}
			// System.out.println("flatP: " + flatPoints[i][0] + ", " +
			// flatPoints[i][1] + ")");
			System.out.println("normPoint[" + p + "]: (" + ballPoints3[p][0]
					+ ", " + ballPoints3[p][1] + ", " + ballPoints3[p][2]);
			System.out.println("flatPoints[" + p + "][0]" + flatBall[p][0]);
			System.out.println("flatPoints[" + p + "][1]" + flatBall[p][1]);

			g.setColor(Color.WHITE);

			/*
			 * g.drawLine((int) flatBall[0][0], (int) flatBall[0][1],(int)
			 * flatBall[1][0], (int) flatBall[1][1]); g.drawLine((int)
			 * flatBall[1][0], (int) flatBall[1][1],(int) flatBall[2][0], (int)
			 * flatBall[2][1]); g.drawLine((int) flatBall[2][0], (int)
			 * flatBall[2][1],(int) flatBall[3][0], (int) flatBall[3][1]);
			 * g.drawLine((int) flatBall[3][0], (int) flatBall[3][1],(int)
			 * flatBall[4][0], (int) flatBall[4][1]); g.drawLine((int)
			 * flatBall[4][0], (int) flatBall[4][1],(int) flatBall[5][0], (int)
			 * flatBall[5][1]); g.drawLine((int) flatBall[5][0], (int)
			 * flatBall[5][1],(int) flatBall[6][0], (int) flatBall[6][1]);
			 * g.drawLine((int) flatBall[6][0], (int) flatBall[6][1],(int)
			 * flatBall[7][0], (int) flatBall[7][1]); g.drawLine((int)
			 * flatBall[7][0], (int) flatBall[7][1],(int) flatBall[0][0], (int)
			 * flatBall[0][1]);
			 */
		}
		for (int i = 0; i < 7; i++) {
			g.drawLine((int) (flatBall[i][0] * 10) + 400,
					(int) (flatBall[i][1] * 10) + 300,
					(int) (flatBall[i + 1][0] * 10) + 400,
					(int) (flatBall[i + 1][1] * 10) + 300);
			// System.out.println("drw: ("+ ((int) (flatPoints[i][0] * 10) +
			// 400) + ", "+ ((int) (flatPoints[i][1] * 10) + 300) + "), ("+
			// ((int) (flatPoints[i + 1][0] * 10) + 400) + ", "+ ((int)
			// (flatPoints[i + 1][1] * 10) + 300) + ")");
		}
		g.drawLine((int) (flatBall[7][0] * 10) + 400,
				(int) (flatBall[7][1] * 10) + 300,
				(int) (flatBall[0][0] * 10) + 400,
				(int) (flatBall[0][1] * 10) + 300);

	}

	float[][][] lines = { { { -40, 0, -40 }, { -40, 0, 40 } },
			{ { -30, 0, -40 }, { -30, 0, 40 } },
			{ { -20, 0, -40 }, { -20, 0, 40 } },
			{ { -10, 0, -40 }, { -10, 0, 40 } },
			{ { 0, 0, -40 }, { 0, 0, 40 } }, { { 10, 0, -40 }, { 10, 0, 40 } },
			{ { 20, 0, -40 }, { 20, 0, 40 } },
			{ { 30, 0, -40 }, { 30, 0, 40 } },
			{ { 40, 0, -40 }, { 40, 0, 40 } } };

	public void gLoop() {
		while (running) {
			// Do the things you want the gLoop to do below here

			// draws the axis lines
			g.setColor(Color.BLUE);
			for (int i = 1; i <= 10; i += 1) {
				g.drawLine(width / 2 + i * 20, 0, width / 2 + i * 20, height);
				g.drawLine(0, height / 2 + i * 20, width, height / 2 + i * 20);

				g.drawLine(width / 2 - i * 20, 0, width / 2 - i * 20, height);
				g.drawLine(0, height / 2 - i * 20, width, height / 2 - i * 20);
			}

			// draws cent point.
			g.setColor(Color.DARK_GRAY);
			g.drawOval((int) (centPoint[0] * 10 * 4 / 9) - 2 + 400,
					(int) (300 - (centPoint[1] * 10 * 4 / 9)) - 2, 4, 4);

			g.setColor(Color.WHITE);

			// draws the lines connecting the flat points
			for (int i = 0; i < 3; i++) {
				g.drawLine((int) (flatPoints[i][0] * 10) + 400,
						(int) (flatPoints[i][1] * 10) + 300,
						(int) (flatPoints[i + 1][0] * 10) + 400,
						(int) (flatPoints[i + 1][1] * 10) + 300);
				// System.out.println("drw: ("+ ((int) (flatPoints[i][0] * 10) +
				// 400) + ", "+ ((int) (flatPoints[i][1] * 10) + 300) + "), ("+
				// ((int) (flatPoints[i + 1][0] * 10) + 400) + ", "+ ((int)
				// (flatPoints[i + 1][1] * 10) + 300) + ")");
			}
			g.drawLine((int) (flatPoints[3][0] * 10) + 400,
					(int) (flatPoints[3][1] * 10) + 300,
					(int) (flatPoints[0][0] * 10) + 400,
					(int) (flatPoints[0][1] * 10) + 300);

			for (int i = 0; i < 3; i++) {
				g.drawLine((int) (flatPoints[i + 4][0] * 10) + 400,
						(int) (flatPoints[i + 4][1] * 10) + 300,
						(int) (flatPoints[i + 5][0] * 10) + 400,
						(int) (flatPoints[i + 5][1] * 10) + 300);
			}
			g.drawLine((int) (flatPoints[7][0] * 10) + 400,
					(int) (flatPoints[7][1] * 10) + 300,
					(int) (flatPoints[4][0] * 10) + 400,
					(int) (flatPoints[4][1] * 10) + 300);
			for (int i = 0; i < 4; i++) {
				g.drawLine((int) (flatPoints[i][0] * 10) + 400,
						(int) (flatPoints[i][1] * 10) + 300,
						(int) (flatPoints[i + 4][0] * 10) + 400,
						(int) (flatPoints[i + 4][1] * 10) + 300);
			}

			// draws the x, y axis
			g.setColor(Color.CYAN);
			g.drawLine(width / 2, 0, width / 2, height);
			g.drawLine(0, height / 2, width, height / 2);

			// g.setColor(Color.WHITE);

			flattenBall();
			flattenBall2();
			// flattenBall3();
			newFlatBall();

			// And above here.
			drwGm(g);

			ticks++;
			// Runs once a second and keeps track of ticks;
			// 1000 ms since last output
			if (timer() - lastSec > 1000) {
				if (ticks < tps - 1 || ticks > tps + 1) {
					if (timer() - startTime < 2000) {
						System.out.println("Ticks this second: " + ticks);
						System.out.println("timer(): " + timer());
						System.out.println("nextTick: " + nextTick);
					}
				}

				ticks = 0;
				lastSec = (System.currentTimeMillis() - startTime);
			}

			// Used to protect the game from falling beind.
			if (nextTick < timer()) {
				nextTick = timer() + milps;
			}

			// Limits the ticks per second
			if (timer() - nextTick < 0) {
				sleepTime = (int) (nextTick - timer());
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}

				nextTick += milps;
			}
		}
	}

	float thea1 = (float) (45 * Math.PI / 180);
	float oneDeg = (float) (Math.PI / 180);

	// want a method where i plug in the point and the angle so it rotates the
	// point by that angle.

	float eX;
	float wY;

	// Method that takes a hyp and a axis
	// i dont know what thios is suppoesed to do.
	// takes points, and rotate it by hyp around the center point that it is
	// told.

	int one = 0;
	int two = 2;

	float[] rotateints(float[] pointd, float degrees) {
		// 0, 2 = floor
		// 1, 2 = side wall
		// 0, 1 = back wall

		float newAng = (float) (degrees * Math.PI / 180);
		// Need a way to find if it is about to go into the x+ quads or x- quads

		// get angle from x axis.
		// get relative x y. Tan(o) = x/y;
		eX = pointd[one] - cameraX;
		wY = pointd[two] - cameraY;
		// System.out.println("camLoc: (" + cameraX + ", " + cameraY + ", "
		// + cameraZ + ")");

		// takes points, finds hypotnus
		float hyp = (float) Math.sqrt((eX * eX) + (wY * wY));
		// System.out.println("Point: (" + point[4][0] + ", " + point[4][1]
		// + ")  Hyp: " + hyp);
		// System.out.println("eX, wY: (" + eX + ", " + wY + ")  Hyp: " + hyp);

		float thea = (float) (Math.atan(wY / eX));
		// System.out.println("thea: " + thea);
		// System.out.println("eX, wY: (" + eX + ", " + wY + ") : thea: "
		// + (thea * 180 / Math.PI));

		thea += newAng;
		// System.out.println("thea+: " + thea);
		float[] points = getXY(hyp, thea, wY);
		// System.out.println("points: " + points[0] + ", " + points[1]);
		if (points[2] == 0) {
			return (new float[] { (cameraX - points[0]), (pointd[1]),
					(cameraY + pointd[2]) });
		} else if (points[2] == 1) {
			return (new float[] { (cameraX + points[0]), (pointd[1]),
					(cameraY + pointd[2]) });
		}
		return new float[] { 0, 0 };
	}

	// should put in int[] (x, y) and return this
	void rotate3d(int pointNum, float degrees) {
		// System.out.println("pointNum: " + pointNum);
		// This currently point thing along the y,z axis.

		// System.out.println("point: " + point[0][2]);

		// 0, 2 = floor
		// 1, 2 = side wall
		// 0, 1 = back wall

		float newAng = (float) (degrees * Math.PI / 180);
		// Need a way to find if it is about to go into the x+ quads or x- quads

		// get angle from x axis.
		// get relative x y. Tan(o) = x/y;
		eX = point[pointNum][one] - centPoint[0];
		wY = point[pointNum][two] - centPoint[1];

		float hyp = (float) Math.sqrt((eX * eX) + (wY * wY));
		// System.out.println("Point: (" + point[4][0] + ", " + point[4][1]
		// + ")  Hyp: " + hyp);
		// System.out.println("eX, wY: (" + eX + ", " + wY + ")  Hyp: " + hyp);

		float thea = (float) (Math.atan(wY / eX));
		// System.out.println("thea: " + thea);
		// System.out.println("eX, wY: (" + eX + ", " + wY + ") : thea: "
		// + (thea * 180 / Math.PI));

		thea += newAng;
		// System.out.println("thea+: " + thea);
		float[] points = getXY(hyp, thea, wY);

		// System.out.println("points: " + points[0] + ", " + points[1]);

		if (points[2] == 0) {
			point[pointNum][one] = centPoint[0] - points[0];
			point[pointNum][two] = centPoint[1] - points[1];
		} else if (points[2] == 1) {
			point[pointNum][one] = centPoint[0] + points[0];
			point[pointNum][two] = centPoint[1] + points[1];
		}

		// System.out.println("POINTS: (" + points[one] + ", " + points[two] +
		// ")");

		// System.out.println("theaA: " + thea);

		// System.out.println("PointAfter: (" + point[4][0] + ", " + point[4][1]
		// + ")  Hyp: " + hyp);

		flatten();

		// System.out.println("rotated");
	}

	void rotateShape(int[] points) {
		// use the center.
		// find the angle ratio of the four point and move them all in ratio to
		// the first point.
		// float x1 = point[points[0]][one] - centPoint[0];
		// float y1 = point[points[0]][two] - centPoint[1];
		// float rat1 = (float) Math.atan(y1 / x1);
		rotate3d(points[0], 2);

		// float x2 = point[points[1]][one] - centPoint[0];
		// float y2 = point[points[1]][two] - centPoint[1];
		// float rat2 = (float) Math.atan(y2 / x2);
		// rotate3d(points[1], (rat2 / rat1));
		rotate3d(points[1], 2);

		// float x3 = point[points[2]][one] - centPoint[0];
		// float y3 = point[points[2]][two] - centPoint[1];
		// float rat3 = (float) Math.atan(y3 / x3);
		// rotate3d(points[2], (rat3 / rat1));
		rotate3d(points[2], 2);

		// float x4 = point[points[3]][one] - centPoint[0];
		// float y4 = point[points[3]][two] - centPoint[1];
		// float rat4 = (float) Math.atan(y4 / x4);
		// rotate3d(points[3], (rat4 / rat1));
		rotate3d(points[3], 2);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}

	void opShape(int[] points) {
		// use the center.
		// find the angle ratio of the four point and move them all in ratio to
		// the first point.

		rotate3d(points[0], -2);
		rotate3d(points[1], -2);
		rotate3d(points[2], -2);
		rotate3d(points[3], -2);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}

	float[] getXY(float hyp, float thea, float wY) {
		// need to make this so it doesnt go down the x axis but the relative x
		// axis, but that is But this is based off of relative. But this is
		// based off of relative
		if (eX < 0) {
			// System.out.println("ITS ON LEFT SIDE");
			// System.out.println("thea: " + Math.tan(thea) + "  hyp: " + hyp);
			// plug in hyp and theta and get x then convert to y also.
			// System.out.println("hyp2: " + (hyp * hyp));
			float x = (float) Math.sqrt((hyp * hyp)
					/ (1 + (Math.pow(Math.tan(thea), 2))));
			float y = (float) (x * Math.tan(thea));
			// System.out.println("x: " + x + ", " + y + ")");
			if (wY < 0 && y < 0) {
				System.out.println("SWITCH");
				return new float[] { x, y, 1 };
			}
			return new float[] { x, y, 0 };
		} else if (eX >= 0) {
			// System.out.println("ITS ON RIGHT SIDE");
			// System.out.println("thea: " + Math.tan(thea) + "  hyp: " + hyp);
			// plug in hyp and theta and get x then convert to y also.
			// System.out.println("hyp2: " + (hyp * hyp));
			float x = (float) Math.sqrt((hyp * hyp)
					/ (1 + (Math.pow(Math.tan(thea), 2))));
			float y = (float) (x * Math.tan(thea));
			// System.out.println("x: " + x + ", " + y + ")");
			if (wY > 0 && y < 0) {
				System.out.println("SWITCH");
				return new float[] { x, y, 0 };
			}
			return new float[] { x, y, 1 };
		}
		return new float[] { 0, 0, 0 };

	}

	// plug in a point,
	// draw a circle at a flatten point in front of the camera.
	// dont want to change the point location just change how it gets flattened
	float[] getXZ(float hyp, float thea, float y) {
		float radThea = thea;
		System.out.println("radThea: " + radThea);
		// plug in hyp and theta and get x then convert to y also.
		boolean negativeTan = false;
		if (Math.tan(radThea) < 0) {
			negativeTan = true;
		}
		float x = (float) Math.sqrt((hyp * hyp)
				/ (1 + (Math.pow(Math.tan(radThea), 2))));

		float z = (float) (x * Math.tan(radThea));
		if (negativeTan) {
			x = -x;
			z = -z;
		}
		System.out.println("returnedXYZ (" + x + ", " + y + ", " + z + ")");
		return new float[] { x, y, z };
	}

	void getLoc(int dist, int ang) {
		float top = (dist * dist);
		float bot = (float) (Math.tan(ang * (Math.PI / 180)) * Math.tan(ang
				* (Math.PI / 180)));
		float x = (float) Math.sqrt(top / (1 + bot));
		// System.out.println("tan: " + Math.tan(70));
		// System.out.println("top: " + top + ", bot: " + bot);
		float y = (float) (Math.tan(ang * (Math.PI / 180)) * x);
		System.out.println("loc: (" + x + ", " + y + ")");
	}

	// make a method that flattens straight lines.
	void line(float[][] points) {

	}

	/**
	 * New Shit
	 */

	float ballX = 10;
	float ballY = -20;

	float screenBottom = 10;
	float ballRadius = 2;
	float velocity = 0;
	float gravity = 9.8f;
	float time = 1;
	float lastTic = 0;

	void gravity() {
		float vi = velocity;
		// System.out.println("vi: " + vi);
		float d = ((vi * time) + ((1f / 2f) * gravity * time * time));
		// ((vPostCol * newTime) + ((1f / 2f) * gravity * newTime * newTime));
		// System.out.println("distance: " + d);
		// System.out.println("d:  " + d + " BallY: " + ballY);
		if (ballY + d <= screenBottom) {
			ballY += d;
			// float vf = (float) Math.sqrt((vi * vi) + 2f * gravity * d);
			// this accounts for it going up
			float vf = vi + gravity * time;
			velocity = vf;
			// System.out.println("vi: " + vi);
			// System.out.println("vf: " + vf);
			if (vi < 0 && vf > 0) {
				// might need to account for the zenith by doing the math for it
				// to hit 0 velocity and for it to then go back down with the
				// remaining time
				// System.out.println("arc: " + (screenBottom - ballY));
				// System.out.println("already switched");
			}
		} else {
			float distToBot = screenBottom - ballY;
			// supposed to find the time it takes to get to the bottom.
			float c1 = (float) ((-vi + Math.sqrt((vi * vi)
					- ((2 * gravity) * (+distToBot)))) / gravity);
			float c2 = (float) ((-vi + Math.sqrt((vi * vi)
					- ((2 * gravity) * (-distToBot)))) / gravity);
			float c3 = ((float) (-vi - Math.sqrt((vi * vi)
					- ((2 * gravity) * (+distToBot)))) / gravity);
			float c4 = ((float) (-vi - Math.sqrt((vi * vi)
					- ((2 * gravity) * (-distToBot)))) / gravity);
			// figure out distance, and vilocity for that second to calculate
			// when the ball would have hit the ground and use that velocity to
			// find that ammount of force it has going back up and bounce in the
			// same tick.

			// know the time it took to hit the ground, use this to find
			// velocity of collision, take like 80% of that and use it to
			// project the ball up.

			// using this equasion i guess i didnt need to know the time just
			// the distance.
			// System.out.println("dTB: " + distToBot + " c2: " + c2);
			float vCollision = (float) Math.sqrt((vi * vi)
					+ (2f * gravity * distToBot));
			ballY += distToBot;
			float vCol = vi + gravity * c2;
			// System.out.println("vCol: " + vCol + ", vCollision: " +
			// vCollision);
			// System.out.println("vCol: " + vCollision);
			float vPostCol = -vCol * .8f;
			float newTime = time - c2;
			// System.out.println("newTime: " + newTime + "  Time: " + time);
			float vf = vPostCol + gravity * (newTime);

			float dUp = ((vPostCol * newTime) + ((1f / 2f) * gravity * newTime * newTime));
			float otherD = ((vPostCol + vf) / 2) * (newTime);
			// System.out.println("vCol: " + vCol + "  vPostCol: " + vPostCol);
			// System.out.println("dUp: " + dUp + ",  otherD: " + otherD);
			// float dUp = vPostCol * time
			// + ((1f / 2f) * gravity * (time * time ));
			// System.out.println("vf: " + vf);

			// System.out.println("ballY: " + ballY);
			// System.out.println("screenBottom: " + screenBottom);
			// System.out.println("balCur: " + (ballY + otherD));
			if ((ballY + otherD) > screenBottom) {
				// if(dUp + ballY< screenBottom - .01){
				// System.out.println("STOP");
				// System.out.println("STOP");
				// }
			}

			ballY += otherD;
			velocity = vf;
		}
		// System.out.println("ballY: " + ballY);
		// System.out.println("vf: " + velocity);
		// move ball;
	}

	void gLoop2() {
		while (running) {

			// Do the things you want the gLoop to do below here
			gravity();
			g.setColor(Color.BLUE);
			g.drawOval((int) (ballX - ballRadius),
					(int) ((ballY * 10) - ballRadius), (int) (ballRadius * 2),
					(int) (ballRadius * 2));

			float[][][] allBallBuffer = new float[4][8][3];
			for (int a = 0; a < allBallP.length; a++) {
				for (int b = 0; b < allBallP[a].length; b++) {
					allBallBuffer[a][b][0] = allBallP[a][b][0];
					allBallBuffer[a][b][1] = -(allBallP[a][b][1] + ballY);
					allBallBuffer[a][b][2] = allBallP[a][b][2];
					// allBallP[a][b][1] += ballY;
				}
			}

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			bigFlatBall(allBallBuffer);

			// And above here.
			drwGm(g);

			// System.out.println("time: " + time);

			lastTic = (System.currentTimeMillis() - startTime);

			ticks++;
			// Runs once a second and keeps track of ticks;
			// 1000 ms since last output
			if (timer() - lastSec > 1000) {
				if (ticks < tps - 1 || ticks > tps + 1) {
					if (timer() - startTime < 2000) {
						System.out.println("Ticks this second: " + ticks);
						System.out.println("timer(): " + timer());
						System.out.println("nextTick: " + nextTick);
					}
				}

				ticks = 0;
				lastSec = (System.currentTimeMillis() - startTime);
			}

			// Used to protect the game from falling beind.
			if (nextTick < timer()) {
				nextTick = timer() + milps;
			}

			// Limits the ticks per second
			if (timer() - nextTick < 0) {
				sleepTime = (int) (nextTick - timer());
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}

				nextTick += milps;
			}
			time = (timer() - lastTic) / 1000;
		}
	}

	float[][][] allBallP = {
			{ { 20, 20, 0 }, { 34f, 14f, 0 }, { 40, 0, 0 }, { 34f, -14f, 0 },
					{ 20, -20, 0 }, { 6f, -14f, 0 }, { 0, 0, 0 },
					{ 6f, 14f, 0 } },
			{ { 20, 20, 0 }, { 30f, 14f, 10 }, { 34, 0, 14 },
					{ 30f, -14f, 10 }, { 20, -20, 0 }, { 10f, -14f, -10 },
					{ 6, 0, -14 }, { 10f, 14f, -10 } },
			{ { 20, 20, 0 }, { 20, 14f, 14f }, { 20, 0, 20 },
					{ 20, -14f, 14f }, { 20, -20, 0 }, { 20, -14f, -14f },
					{ 20, 0, -20 }, { 20, 14f, -14f } },
			{ { 20, 20, 0 }, { 30f, 14f, -10 }, { 34, 0, -14 },
					{ 30f, -14f, -10 }, { 20, -20, 0 }, { 10f, -14f, 10 },
					{ 6, 0, 14 }, { 10f, 14f, 10 } } };

	void bigFlatBall(float[][][] allBall) {
		g.setColor(Color.WHITE);
		float[][][] tempPt = new float[4][8][3];
		float[][][] flatTemp = new float[4][8][2];
		int[][][] drawPoints = new int[4][8][2];
		for (int a = 0; a < allBall.length; a++) {
			if (a == 0) {
				// g.setColor(Color.RED);
			} else if (a == 1) {
				// g.setColor(Color.GREEN);
			} else if (a == 2) {
				// g.setColor(Color.BLUE);
			} else if (a == 3) {
				// g.setColor(Color.WHITE);
			}
			for (int i = 0; i < tempPt[a].length; i++) {
				tempPt[a][i][0] = allBall[a][i][0] - cameraX;
				tempPt[a][i][1] = allBall[a][i][1] - cameraY;
				tempPt[a][i][2] = allBall[a][i][2] - cameraZ;
				System.out.println("tempPt[" + a + "][" + i + "]: "
						+ tempPt[a][i][0] + ", " + tempPt[a][i][1] + ", "
						+ tempPt[a][i][2]);

				flatTemp[a][i][0] = (tempPt[a][i][0] / tempPt[a][i][2]) * 20;
				flatTemp[a][i][1] = (tempPt[a][i][1] / tempPt[a][i][2]) * 20;
			}

			for (int i = 0; i < drawPoints[a].length; i++) {
				drawPoints[a][i][0] = (int) (flatTemp[a][i][0] * 10) + 400;
				// negate y because java graphics grid system has higher y
				// values lower
				drawPoints[a][i][1] = (int) (-flatTemp[a][i][1] * 10) + 300;
				System.out.println("drawPoint[" + a + "][" + i + "]: "
						+ drawPoints[a][i][0] + ", " + drawPoints[a][i][1]);
			}
			for (int i = 0; i < 7; i++) {
				if (tempPt[a][i][2] > 4 && tempPt[a][i + 1][2] > 4) {
					g.drawLine(drawPoints[a][i][0], drawPoints[a][i][1],
							drawPoints[a][i + 1][0], drawPoints[a][i + 1][1]);
				} else {
					// All these elses are Garbage Collecting
					System.out.println("CANT[" + a + "][" + i + "]: "
							+ tempPt[a][i][2] + ", " + tempPt[a][i + 1][2]);
				}
			}
			if (tempPt[a][7][2] > 4 && tempPt[a][0][2] > 4) {
				g.drawLine(drawPoints[a][7][0], drawPoints[a][7][1],
						drawPoints[a][0][0], drawPoints[a][0][1]);
			} else {
				System.out.println("CANT[a" + a + "][A]: " + tempPt[a][7][2]
						+ ", " + tempPt[a][0][2]);
			}
		}
		// g.setColor(Color.WHITE);
		for (int a = 0; a < 2; a++) {
			for (int i = 0; i < 8; i++) {
				if (tempPt[a][i][2] > 4 && tempPt[a + 1][i][2] > 4) {
					g.drawLine(drawPoints[a][i][0], drawPoints[a][i][1],
							drawPoints[a + 1][i][0], drawPoints[a + 1][i][1]);
				} else {
					System.out.println("cant");
				}
			}
		}
		for (int i = 0; i < 8; i++) {
			if (tempPt[3][i][2] > 4 && tempPt[0][i][2] > 4) {
				g.drawLine(drawPoints[3][i][0], drawPoints[3][i][1],
						drawPoints[0][i][0], drawPoints[0][i][1]);
			} else {
				System.out.println("cant");
			}
		}

		if (tempPt[2][1][2] > 4 && tempPt[3][7][2] > 4) {
			g.drawLine(drawPoints[2][1][0], drawPoints[2][1][1],
					drawPoints[3][7][0], drawPoints[3][7][1]);
		}

		if (tempPt[2][2][2] > 4 && tempPt[3][6][2] > 4) {
			g.drawLine(drawPoints[2][2][0], drawPoints[2][2][1],
					drawPoints[3][6][0], drawPoints[3][6][1]);
		}
		if (tempPt[2][3][2] > 4 && tempPt[3][5][2] > 4) {
			g.drawLine(drawPoints[2][3][0], drawPoints[2][3][1],
					drawPoints[3][5][0], drawPoints[3][5][1]);
		}
		// nub
		if (tempPt[3][1][2] > 4 && tempPt[2][7][2] > 4) {
			g.drawLine(drawPoints[3][1][0], drawPoints[3][1][1],
					drawPoints[2][7][0], drawPoints[2][7][1]);
		}
		if (tempPt[3][2][2] > 4 && tempPt[2][6][2] > 4) {
			g.drawLine(drawPoints[3][2][0], drawPoints[3][2][1],
					drawPoints[2][6][0], drawPoints[2][6][1]);
		}
		if (tempPt[3][3][2] > 4 && tempPt[2][5][2] > 4) {
			g.drawLine(drawPoints[3][3][0], drawPoints[3][3][1],
					drawPoints[2][5][0], drawPoints[2][5][1]);
		}

		// 1 and 7
		// 2 and 6
		// 3 and 5
	}

	/**
	 * Methods go above here.
	 * 
	 */

	public long timer() {
		return System.currentTimeMillis() - startTime;

	}

	public void drwGm(Graphics g) {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void imageInit() {
		/**
		 * imageAr = new Image[1]; ImageIcon ie = new
		 * ImageIcon(this.getClass().getResource( "res/image.png")); imageAr[0]
		 * = ie.getImage();
		 */

	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
			// getLoc(2, 15);
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			drwGm(g);
		}
		if (ke.getKeyCode() == KeyEvent.VK_F) {
			rotate3d(4, 1);
			rotate3d(5, 1);
		}
		if (ke.getKeyCode() == KeyEvent.VK_G) {
			rotateShape(new int[] { 4, 5, 0, 1 });
			rotateShape(new int[] { 6, 7, 2, 3 });
		}
		if (ke.getKeyCode() == KeyEvent.VK_H) {
			opShape(new int[] { 4, 5, 0, 1 });
			opShape(new int[] { 6, 7, 2, 3 });

		} else if (ke.getKeyCode() == KeyEvent.VK_W) {
			cameraZ += 3;
			// flatten();
			// g.setColor(Color.BLACK);
			// g.fillRect(0, 0, width, height);
			// drwGm(g);
		} else if (ke.getKeyCode() == KeyEvent.VK_S) {
			cameraZ -= 3;
			// flatten();
			// g.setColor(Color.BLACK);
			// g.fillRect(0, 0, width, height);
			// drwGm(g);
		} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
			cameraX -= 2;
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// drwGm(g);
		} else if (ke.getKeyCode() == KeyEvent.VK_E) {
			cameraX += 2;
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// drwGm(g);
		} else if (ke.getKeyCode() == KeyEvent.VK_A) {
			worldRotation -= 2f * oneDeg;
			System.out.println("wr: " + worldRotation);
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// drwGm(g);
		} else if (ke.getKeyCode() == KeyEvent.VK_D) {
			worldRotation += 2f * oneDeg;
			System.out.println("wr: " + worldRotation);
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// drwGm(g);
		} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
			for (int i = 0; i < point.length; i++) {
				point[i][2] -= 10;
			}
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
		} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
			for (int i = 0; i < point.length; i++) {
				point[i][2] += 10;
			}
			flatten();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
		} else if (ke.getKeyCode() == KeyEvent.VK_P) {
			System.out.println("getXZ: " + getXZ(2, 45, 0)[0] + ", "
					+ getXZ(2, 45, 0)[1] + ", " + getXZ(2, 45, 0)[2]);
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
