package ProjectionArchive.projectionBucket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PanelSlow extends JPanel implements MouseListener, KeyListener {

	// Clicking mouse draws the screen.

	int width = 800;
	int height = 600;

	Image[] imageAr;

	Thread thread;
	Image image;
	Graphics g;

	float time = 1;

	// Vars for gLoop Above
	float cameraX = 0;
	float cameraY = 0;
	float cameraZ = 0;

	float worldRotation = 0;

	float[][] ballPoints = { { 20, 20, 0 }, { 34f, 14f, 0 }, { 40, 0, 0 },
			{ 34f, -14f, 0 }, { 20, -20, 0 }, { 6f, -14f, 0 }, { 0, 0, 0 },
			{ 6f, 14f, 0 } };
	float[][] ballPoints2 = { { 20, 20, 0 }, { 30f, 14f, 10 }, { 34, 0, 14 },
			{ 30f, -14f, 10 }, { 20, -20, 0 }, { 10f, -14f, -10 },
			{ 6, 0, -14 }, { 10f, 14f, -10 } };
	float[][] ballPoints3 = { { 20, 20, 0 }, { 20, 14f, 14f }, { 20, 0, 20 },
			{ 20, -14f, 14f }, { 20, -20, 0 }, { 20, -14f, -14f },
			{ 20, 0, -20 }, { 20, 14f, -14f } };
	float[][] ballPoints4 = { { 20, 20, 0 }, { 30f, 14f, -10 }, { 34, 0, -14 },
			{ 30f, -14f, -10 }, { 20, -20, 0 }, { 10f, -14f, 10 },
			{ 6, 0, 14 }, { 10f, 14f, 10 } };
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

	float[][][] squarePanel;

	// method that makes a ball object,
	// top, ring of 8 first from top, down (center ring), dont, bottom.
	float[][] ball = { { 0, 2, 0 }, { 1.4f, 1.4f, 0 }, { 1, 1.4f, -1 },
			{ 0, 1.4f, -1.4f }, { -1, 1.4f, -1 }, { -1.4f, 1.4f, 0 },
			{ -1, 1.4f, 1 }, { 0, 1.4f, 1.4f }, { 1, 1.4f, 1 }, { 2, 0, 0 },
			{ 1.4f, 0, -1.4f }, { 0, 0, -2 }, { -1.4f, 0, -1.4f },
			{ -2, 0, 0 }, { -1.4f, 0, 1.4f }, { 0, 0, 2 }, { 1.4f, 0, 1.4f },
			{ 1.4f, -1.4f, 0 }, { 1, -1.4f, -1 }, { 0, -1.4f, -1.4f },
			{ -1, -1.4f, -1 }, { -1.4f, -1.4f, 0 }, { -1, -1.4f, 1 },
			{ 0, -1.4f, 1.4f }, { 1, -1.4f, 1 }, { 0, -2, 0 } };

	public PanelSlow() {
		super();

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();

		addKeyListener(this);
		addMouseListener(this);

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.setSize(new Dimension(width, height));

		pStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	public void pStart() {
		imageInit();
	}

	void newFlatBall(float[][] ballPoint) {
		g.setColor(Color.WHITE);
		float[][] tempPt = new float[8][3];
		float[][] flatTemp = new float[8][2];
		for (int i = 0; i < tempPt.length; i++) {
			tempPt[i][0] = ballPoint[i][0] - cameraX;
			tempPt[i][1] = ballPoint[i][1] - cameraY;
			tempPt[i][2] = ballPoint[i][2] - cameraZ;

			float hyp = ((tempPt[i][0] * tempPt[i][0]) * (tempPt[i][2] * tempPt[i][2]));

			float thea = (float) Math.atan(tempPt[i][2] / tempPt[i][0]);
			thea += worldRotation;

			flatTemp[i][0] = (tempPt[i][0] / tempPt[i][2]) * 20;
			flatTemp[i][1] = (tempPt[i][1] / tempPt[i][2]) * 20;
		}

		int[][] drawPoints = new int[8][2];
		for (int i = 0; i < drawPoints.length; i++) {
			drawPoints[i][0] = (int) (flatTemp[i][0] * 10) + 400;
			// negate y because java graphics grid system has higher y values
			// lower
			drawPoints[i][1] = (int) (-flatTemp[i][1] * 10) + 300;
			System.out.println("drawPoint[" + i + "]: " + drawPoints[i][0]
					+ ", " + drawPoints[i][1]);
		}

		for (int i = 0; i < 7; i++) {
			if (tempPt[i][2] > 4 && tempPt[i + 1][2] > 4) {
				g.drawLine(drawPoints[i][0], drawPoints[i][1],
						drawPoints[i + 1][0], drawPoints[i + 1][1]);
			}
		}
		if (tempPt[7][2] > 4 && tempPt[0][2] > 4) {
			g.drawLine(drawPoints[7][0], drawPoints[7][1], drawPoints[0][0],
					drawPoints[0][1]);
		}
	}

	void bigFlatBall(float[][][] allBall) {
		g.setColor(Color.WHITE);
		float[][][] tempPt = new float[4][8][3];
		float[][][] rotPt = new float[4][8][3];
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

				float hyp = (float) Math
						.sqrt((tempPt[a][i][0] * tempPt[a][i][0])
								+ (tempPt[a][i][2] * tempPt[a][i][2]));

				float thea = (float) Math.atan(tempPt[a][i][2]
						/ tempPt[a][i][0]);
				thea += worldRotation;
				System.out.println("thea: " + thea);

				rotPt[a][i] = getXZ(hyp, thea, tempPt[a][i][1]);

				System.out.println("rotPt[" + a + "][" + i + "] ("
						+ rotPt[a][i][0] + ", " + rotPt[a][i][1] + ", "
						+ rotPt[a][i][2] + ")");
				System.out.println("tempPt[" + a + "][" + i + "] ("
						+ tempPt[a][i][0] + ", " + tempPt[a][i][1] + ", "
						+ tempPt[a][i][2] + ")");

				flatTemp[a][i][0] = (rotPt[a][i][0] / rotPt[a][i][2])
						* (width / 20);
				flatTemp[a][i][1] = (rotPt[a][i][1] / rotPt[a][i][2])
						* (width / 20);
			}
			for (int i = 0; i < drawPoints[a].length; i++) {
				drawPoints[a][i][0] = (int) ((flatTemp[a][i][0] * 10) + width / 2);
				// negate y because java graphics grid system has higher y
				// values lower
				drawPoints[a][i][1] = (int) ((-flatTemp[a][i][1] * 10) + height / 2);
				// System.out.println("drawPoint[" + a + "][" + i + "]: " +
				// drawPoints[a][i][0] + ", " + drawPoints[a][i][1]);
			}
			for (int i = 0; i < 7; i++) {
				if (rotPt[a][i][2] > 4 && rotPt[a][i + 1][2] > 4) {
					if (Math.abs(rotPt[a][i][0]) < Math.abs(rotPt[a][i][2])
							|| Math.abs(rotPt[a][i + 1][0]) < Math
									.abs(rotPt[a][i + 1][2])) {
						g.drawLine(drawPoints[a][i][0], drawPoints[a][i][1],
								drawPoints[a][i + 1][0],
								drawPoints[a][i + 1][1]);
					}
				} else {
					// All these elses are Garbage Collecting
					System.out.println("CANT[" + a + "][" + i + "]: "
							+ tempPt[a][i][2] + ", " + tempPt[a][i + 1][2]);
				}
			}
			if (rotPt[a][7][2] > 4 && rotPt[a][0][2] > 4) {
				if (Math.abs(rotPt[a][7][0]) < Math.abs(rotPt[a][7][2])
						|| Math.abs(rotPt[a][0][0]) < Math.abs(rotPt[a][0][2])) {
					g.drawLine(drawPoints[a][7][0], drawPoints[a][7][1],
							drawPoints[a][0][0], drawPoints[a][0][1]);
				}
			} else {
				System.out.println("CANT[a" + a + "][A]: " + tempPt[a][7][2]
						+ ", " + tempPt[a][0][2]);
			}
		}
		// g.setColor(Color.WHITE);
		for (int a = 0; a < 2; a++) {
			for (int i = 0; i < 8; i++) {
				if (rotPt[a][i][2] > 4 && rotPt[a + 1][i][2] > 4) {
					if (Math.abs(rotPt[a][i][0]) < Math.abs(rotPt[a][i][2])
							|| Math.abs(rotPt[a + 1][i][0]) < Math
									.abs(rotPt[a + 1][i][2])) {
						g.drawLine(drawPoints[a][i][0], drawPoints[a][i][1],
								drawPoints[a + 1][i][0],
								drawPoints[a + 1][i][1]);
					}
				} else {
					System.out.println("cant");
				}
			}
		}
		for (int i = 0; i < 8; i++) {
			if (rotPt[3][i][2] > 4 && rotPt[0][i][2] > 4) {
				if (Math.abs(rotPt[3][i][0]) < Math.abs(rotPt[3][i][2])
						|| Math.abs(rotPt[0][i][0]) < Math.abs(rotPt[0][i][2])) {
					g.drawLine(drawPoints[3][i][0], drawPoints[3][i][1],
							drawPoints[0][i][0], drawPoints[0][i][1]);
				}
			} else {
				System.out.println("cant");
			}
		}

		if (rotPt[2][1][2] > 4 && rotPt[3][7][2] > 4) {
			// if one of the points is on screen.
			if (Math.abs(rotPt[2][1][2]) > Math.abs(rotPt[2][1][0])
					|| Math.abs(rotPt[3][7][2]) > Math.abs(rotPt[3][7][0])) {
				g.drawLine(drawPoints[2][1][0], drawPoints[2][1][1],
						drawPoints[3][7][0], drawPoints[3][7][1]);
			}
		}

		if (rotPt[2][2][2] > 4 && rotPt[3][6][2] > 4) {
			if (Math.abs(rotPt[2][2][2]) > Math.abs(rotPt[2][2][0])
					|| Math.abs(rotPt[3][6][2]) > Math.abs(rotPt[3][6][0])) {
				g.drawLine(drawPoints[2][2][0], drawPoints[2][2][1],
						drawPoints[3][6][0], drawPoints[3][6][1]);
			}
		}
		if (rotPt[2][3][2] > 4 && rotPt[3][5][2] > 4) {
			if (Math.abs(rotPt[2][3][2]) > Math.abs(rotPt[2][3][0])
					|| Math.abs(rotPt[3][5][2]) > Math.abs(rotPt[3][5][0])) {
				g.drawLine(drawPoints[2][3][0], drawPoints[2][3][1],
						drawPoints[3][5][0], drawPoints[3][5][1]);
			}
		}
		// nub
		if (rotPt[3][1][2] > 4 && rotPt[2][7][2] > 4) {
			if (Math.abs(rotPt[3][1][2]) > Math.abs(rotPt[3][1][0])
					|| Math.abs(rotPt[2][7][2]) > Math.abs(rotPt[2][7][0])) {
				g.drawLine(drawPoints[3][1][0], drawPoints[3][1][1],
						drawPoints[2][7][0], drawPoints[2][7][1]);
			}
		}
		if (rotPt[3][2][2] > 4 && rotPt[2][6][2] > 4) {
			if (Math.abs(rotPt[3][2][2]) > Math.abs(rotPt[3][2][0])
					|| Math.abs(rotPt[2][6][2]) > Math.abs(rotPt[2][6][0])) {
				g.drawLine(drawPoints[3][2][0], drawPoints[3][2][1],
						drawPoints[2][6][0], drawPoints[2][6][1]);
			}
		}
		if (rotPt[3][3][2] > 4 && rotPt[2][5][2] > 4) {
			if (Math.abs(rotPt[3][3][2]) > Math.abs(rotPt[3][3][0])
					|| Math.abs(rotPt[2][5][2]) > Math.abs(rotPt[2][5][0])) {
				g.drawLine(drawPoints[3][3][0], drawPoints[3][3][1],
						drawPoints[2][5][0], drawPoints[2][5][1]);
			}
		}

		// 1 and 7
		// 2 and 6
		// 3 and 5
	}

	void flattenBall3() {
		float[][] flatBall = new float[8][2];
		float[][] tempPoints = new float[ballPoints3.length][];
		for (int p = 0; p < tempPoints.length; p++) {
			float ox = ballPoints3[p][0];
			float oy = ballPoints3[p][1];
			float oz = ballPoints3[p][2];

			float hyp1 = (float) Math.sqrt(Math.pow(ox, 2) + Math.pow(oz, 2));

			float thea1 = (float) Math.atan(oz / ox);

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

			// rotated around camLoc by worldRot.
			tempPoints[p] = getXZ(hypThis, worldRotAndThisThea,
					(ballPoints3[p][1] - cameraY));

			// get world thea of point and add worldRot then get new rot.

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
			g.setColor(Color.WHITE);

		}
		for (int i = 0; i < 7; i++) {
			g.drawLine((int) (flatBall[i][0] * 10) + (width / 2),
					(int) (flatBall[i][1] * 10) + (height / 2),
					(int) (flatBall[i + 1][0] * 10) + (width / 2),
					(int) (flatBall[i + 1][1] * 10) + (height / 2));
		}
		g.drawLine((int) (flatBall[7][0] * 10) + (width / 2),
				(int) (flatBall[7][1] * 10) + (height / 2),
				(int) (flatBall[0][0] * 10) + (width / 2),
				(int) (flatBall[0][1] * 10) + (height / 2));

		for (int i = 0; i < flatBall.length; i++) {
			System.out.println("flat[" + i + "]" + flatBall[i][0] + ", "
					+ flatBall[i][1]);
		}

	}

	float[] getXZ(float hyp, float thea, float y) {
		float radThea = thea;
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
		return new float[] { x, y, z };
	}

	void rotBallAtPoint() {

	}

	float[] node1 = { 0, 0, 10 };

	void drawFilledBall(float[][] allBall) {
		g.setColor(Color.WHITE);
		int aBlen = allBall.length;
		float[][] tempPt = new float[aBlen][3];
		float[][] rotPt = new float[aBlen][3];
		float[][] flatTemp = new float[aBlen][2];
		int[][] drawPoints = new int[aBlen][2];
		for (int i = 0; i < tempPt.length; i++) {
			tempPt[i][0] = (allBall[i][0] - cameraX + node1[0]);
			tempPt[i][1] = (allBall[i][1] - cameraY + node1[1]);
			tempPt[i][2] = (allBall[i][2] - cameraZ + node1[2]);

			float hyp = (float) Math.sqrt((tempPt[i][0] * tempPt[i][0])
					+ (tempPt[i][2] * tempPt[i][2]));

			float thea = (float) Math.atan(tempPt[i][2] / tempPt[i][0]);
			thea += worldRotation;
			System.out.println("thea: " + thea);

			rotPt[i] = getXZ(hyp, thea, tempPt[i][1]);

			System.out.println("rotPt[" + i + "] (" + rotPt[i][0] + ", "
					+ rotPt[i][1] + ", " + rotPt[i][2] + ")");
			System.out.println("tempPt[" + i + "] (" + tempPt[i][0] + ", "
					+ tempPt[i][1] + ", " + tempPt[i][2] + ")");

			flatTemp[i][0] = (rotPt[i][0] / rotPt[i][2]) * (width / 20);
			flatTemp[i][1] = (rotPt[i][1] / rotPt[i][2]) * (width / 20);
		}
		for (int i = 0; i < drawPoints.length; i++) {
			drawPoints[i][0] = (int) ((flatTemp[i][0] * 10) + width / 2);
			drawPoints[i][1] = (int) ((-flatTemp[i][1] * 10) + height / 2);
		}
		/**
		 * Draws the shape with polygons, tris and quads.
		 */
		for (int i = 1; i < 8; i++) {
			g.setColor(Color.WHITE);
			g.fillPolygon(new int[] { drawPoints[i][0], drawPoints[i + 1][0],
					drawPoints[0][0] }, new int[] { drawPoints[i][1],
					drawPoints[i + 1][1], drawPoints[0][1] }, 3);
		}
		g.setColor(Color.WHITE);
		g.fillPolygon(new int[] { drawPoints[8][0], drawPoints[1][0],
				drawPoints[0][0] }, new int[] { drawPoints[8][1],
				drawPoints[1][1], drawPoints[0][1] }, 3);
		g.fillPolygon(new int[] { drawPoints[1][0], drawPoints[2][0],
				drawPoints[10][0], drawPoints[9][0] }, new int[] {
				drawPoints[1][1], drawPoints[2][1], drawPoints[10][1],
				drawPoints[9][1] }, 4);
		for (int i = 1; i < 8; i++) {
			g.fillPolygon(new int[] { drawPoints[i][0], drawPoints[i + 1][0],
					drawPoints[i + 9][0], drawPoints[i + 8][0] }, new int[] {
					drawPoints[i][1], drawPoints[i + 1][1],
					drawPoints[i + 9][1], drawPoints[i + 8][1] }, 4);
		}
		g.fillPolygon(new int[] { drawPoints[8][0], drawPoints[1][0],
				drawPoints[9][0], drawPoints[16][0] }, new int[] {
				drawPoints[8][1], drawPoints[1][1], drawPoints[9][1],
				drawPoints[16][1] }, 4);
		g.fillPolygon(new int[] { drawPoints[1][0], drawPoints[2][0],
				drawPoints[10][0], drawPoints[9][0] }, new int[] {
				drawPoints[1][1], drawPoints[2][1], drawPoints[10][1],
				drawPoints[9][1] }, 4);
		for (int i = 1; i < 8; i++) {
			g.fillPolygon(new int[] { drawPoints[i + 8][0],
					drawPoints[i + 9][0], drawPoints[i + 17][0],
					drawPoints[i + 16][0] }, new int[] { drawPoints[i + 8][1],
					drawPoints[i + 9][1], drawPoints[i + 17][1],
					drawPoints[i + 16][1] }, 4);
		}
		g.fillPolygon(new int[] { drawPoints[16][0], drawPoints[9][0],
				drawPoints[17][0], drawPoints[24][0] }, new int[] {
				drawPoints[16][1], drawPoints[9][1], drawPoints[17][1],
				drawPoints[24][1] }, 4);
		for (int i = 1; i < 8; i++) {
			g.setColor(Color.WHITE);
			g.fillPolygon(new int[] { drawPoints[i + 16][0],
					drawPoints[i + 17][0], drawPoints[25][0] }, new int[] {
					drawPoints[i + 16][1], drawPoints[i + 17][1],
					drawPoints[25][1] }, 3);
		}
		// outline the shapes edges

		g.setColor(Color.RED);
		for (int i = 0; i < 8; i++) {
			g.drawLine(drawPoints[0][0], drawPoints[0][1],
					drawPoints[i + 1][0], drawPoints[i + 1][1]);
		}
		for (int i = 0; i < 8; i++) {
			g.drawLine(drawPoints[i + 1][0], drawPoints[i + 1][1],
					drawPoints[i + 9][0], drawPoints[i + 9][1]);
		}
		for (int i = 0; i < 8; i++) {
			g.drawLine(drawPoints[i + 9][0], drawPoints[i + 9][1],
					drawPoints[i + 17][0], drawPoints[i + 17][1]);
		}
		for (int i = 0; i < 8; i++) {
			g.drawLine(drawPoints[i + 17][0], drawPoints[i + 17][1],
					drawPoints[25][0], drawPoints[25][1]);
		}
		for (int i = 0; i < 7; i++) {
			g.drawLine(drawPoints[i + 1][0], drawPoints[i + 1][1],
					drawPoints[i + 2][0], drawPoints[i + 2][1]);
		}
		g.drawLine(drawPoints[8][0], drawPoints[8][1], drawPoints[1][0],
				drawPoints[1][1]);
		for (int i = 0; i < 7; i++) {
			g.drawLine(drawPoints[i + 9][0], drawPoints[i + 9][1],
					drawPoints[i + 10][0], drawPoints[i + 10][1]);
		}
		g.drawLine(drawPoints[16][0], drawPoints[16][1], drawPoints[9][0],
				drawPoints[9][1]);
		for (int i = 0; i < 7; i++) {
			g.drawLine(drawPoints[i + 17][0], drawPoints[i + 17][1],
					drawPoints[i + 18][0], drawPoints[i + 18][1]);
		}
		g.drawLine(drawPoints[24][0], drawPoints[24][1], drawPoints[17][0],
				drawPoints[17][1]);
	}

	// 0, 1
	// 0, 2
	// 0, 3
	// 0, 4
	// 0, 5
	// 0, 6
	// 0, 7
	// 0, 8

	// 1, 9
	// 2, 10
	// 3, 11
	// 4, 12
	// 5, 13
	// 6, 14
	// 7, 15
	// 8, 16

	// 9 , 17
	// 10, 18
	// 11, 19
	// 12, 20
	// 13, 21
	// 14, 22
	// 15, 23
	// 16, 24

	// 17, 25
	// 18, 25
	// 19, 25
	// 20, 25
	// 21, 25
	// 22, 25
	// 23, 25
	// 24, 25

	// 1, 2
	// 2, 3
	// 3, 4
	// 4, 5
	// 5, 6
	// 6, 7
	// 7, 8
	// 8, 1

	// 9, 10
	// 10, 11
	// 11, 12
	// 12, 13
	// 13, 14
	// 14, 15
	// 15, 16
	// 16, 9

	// 17, 18
	// 18, 19
	// 19, 20
	// 20, 21
	// 21, 22
	// 22, 23
	// 23, 24
	// 24, 17

	// side
	// 0, 1, 2
	// 0, 2, 3
	// 0, 3, 4
	// 0, 4, 5
	// 0, 5, 6
	// 0, 6, 7
	// 0, 7, 8
	// 0, 8, 1

	// 2, 3, 10, 11

	/**
	 * Methods go above here.
	 * 
	 */

	public void drwGm() {
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
	public void mouseClicked(MouseEvent arg0) {
		drwGm();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	float oneDeg = (float) (Math.PI / 180);

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
			drawFilledBall(ball);
		} else if (ke.getKeyCode() == KeyEvent.VK_Z) {
			// Lover Camera (Increase).
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			cameraY++;
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
			// for (int a = 0; a < allBallP.length; a++) {
			// for (int b = 0; b < allBallP[a].length; b++) {
			// allBallP[a][b][1] += 2;
			// bigFlatBall(allBallP);
			// }
			// }
		} else if (ke.getKeyCode() == KeyEvent.VK_X) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			cameraY--;
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
			// for (int a = 0; a < allBallP.length; a++) {
			// for (int b = 0; b < allBallP[a].length; b++) {
			// allBallP[a][b][1] -= 2;
			// bigFlatBall(allBallP);
			// }
			// }
		} else if (ke.getKeyCode() == KeyEvent.VK_W) {
			cameraZ += 3;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// newFlatBall(ballPoints);
			// newFlatBall(ballPoints2);
			// newFlatBall(ballPoints3);
			// newFlatBall(ballPoints4);
			// flattenBall3();
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
		} else if (ke.getKeyCode() == KeyEvent.VK_S) {
			cameraZ -= 3;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// newFlatBall(ballPoints);
			// newFlatBall(ballPoints2);
			// newFlatBall(ballPoints3);
			// newFlatBall(ballPoints4);
			// flattenBall3();
			// bigFlatBall(allBallPC);
			drawFilledBall(ball);
		} else if (ke.getKeyCode() == KeyEvent.VK_Q) {
			cameraX -= 3;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
		} else if (ke.getKeyCode() == KeyEvent.VK_E) {
			cameraX += 3;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
		} else if (ke.getKeyCode() == KeyEvent.VK_A) {
			worldRotation -= 2f * oneDeg;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
		} else if (ke.getKeyCode() == KeyEvent.VK_D) {
			worldRotation += 2f * oneDeg;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			// bigFlatBall(allBallP);
			drawFilledBall(ball);
		}
		drwGm();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
