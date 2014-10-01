package lavaGlobe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel implements MouseListener,
		MouseMotionListener, KeyListener {
	// Clicking mouse draws the screen.

	int width = 600;
	int height = 600;

	Image[] imageAr;

	Thread thread;
	Image image;
	Graphics g;

	int lastX = 0;
	int lastY = 0;

	// Vars for gLoop Above

	public Panel() {
		super();

		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		this.setSize(new Dimension(width, height));

		pStart();
	}

	/**
	 * Methods go below here.
	 * 
	 */

	Model m = new Model();

	public void pStart() {
		imageInit();
		m.createTorus(40, 20, 100, 50);
		// m.createCube();
	}

	void vectPlaneIntersection() {
		float[] a = { 4, -1, 2 };
		float[] b = { 1, 1, 2 };
		float[] c = { 3, -1, 3 };
		float[] d = { 2, 0, 0 };
		float[] e = { 0, 2, 0 };
		float v = .25f;
		float u1 = ((v * (e[1] * b[0] - e[0] * b[1]) + c[1] * b[0] - a[1]
				* b[0] - c[0] * b[1] + a[0] * b[1]) / (d[0] * b[1] - d[1]
				* b[0]));
		float u2 = ((v * (e[2] * b[0] - e[0] * b[2]) + c[2] * b[0] - a[2]
				* b[0] - c[0] * b[2] + a[0] * b[2]) / (d[0] * b[2] - d[2]
				* b[0]));

		float u3 = ((v
				* ((e[1] * b[0] - e[0] * b[1]) * (d[0] * b[2] - d[2] * b[0]))
				+ c[1] * b[0] * (d[0] * b[2] - d[2] * b[0]) - a[1] * b[0]
				* (d[0] * b[2] - d[2] * b[0]) - c[0] * b[1]
				* (d[0] * b[2] - d[2] * b[0]) + a[0] * b[1]
				* (d[0] * b[2] - d[2] * b[0])));
		float u4 = ((v
				* ((e[2] * b[0] - e[0] * b[2]) * (d[0] * b[1] - d[1] * b[0]))
				+ c[2] * b[0] * (d[0] * b[1] - d[1] * b[0]) - a[2] * b[0]
				* (d[0] * b[1] - d[1] * b[0]) - c[0] * b[2]
				* (d[0] * b[1] - d[1] * b[0]) + a[0] * b[2]
				* (d[0] * b[1] - d[1] * b[0])));

		v = (c[2] * b[0] * (d[0] * b[1] - d[1] * b[0]) - a[2] * b[0]
				* (d[0] * b[1] - d[1] * b[0]) - c[0] * b[2]
				* (d[0] * b[1] - d[1] * b[0]) + a[0] * b[2]
				* (d[0] * b[1] - d[1] * b[0]) - c[1] * b[0]
				* (d[0] * b[2] - d[2] * b[0]) + a[1] * b[0]
				* (d[0] * b[2] - d[2] * b[0]) + c[0] * b[1]
				* (d[0] * b[2] - d[2] * b[0]) - a[0] * b[1]
				* (d[0] * b[2] - d[2] * b[0]))
				/ ((e[1] * b[0] - e[0] * b[1]) * (d[0] * b[2] - d[2] * b[0]) - (e[2]
						* b[0] - e[0] * b[2])
						* (d[0] * b[1] - d[1] * b[0]));
		System.out.println("v: " + v);
		float t = -(c[2] * e[0] * (d[0] * e[1] - d[1] * e[0]) - a[2] * e[0]
				* (d[0] * e[1] - d[1] * e[0]) - c[0] * e[2]
				* (d[0] * e[1] - d[1] * e[0]) + a[0] * e[2]
				* (d[0] * e[1] - d[1] * e[0]) - c[1] * e[0]
				* (d[0] * e[2] - d[2] * e[0]) + a[1] * e[0]
				* (d[0] * e[2] - d[2] * e[0]) + c[0] * e[1]
				* (d[0] * e[2] - d[2] * e[0]) - a[0] * e[1]
				* (d[0] * e[2] - d[2] * e[0]))
				/ ((-b[1] * e[0] + b[0] * e[1]) * (d[0] * e[2] - d[2] * e[0]) + (b[2]
						* e[0] + b[0] * e[2])
						* (d[0] * e[1] - d[1] * e[0]));
		System.out.println("t: " + t);
		float u = (c[2] * d[0] * (b[0] * d[1] - b[1] * d[0]) - a[2] * d[0]
				* (b[0] * d[1] - b[1] * d[0]) - c[0] * d[2]
				* (b[0] * d[1] - b[1] * d[0]) + a[0] * d[2]
				* (b[0] * d[1] - b[1] * d[0]) - c[1] * d[0]
				* (b[0] * d[2] - b[2] * d[0]) + a[1] * d[0]
				* (b[0] * d[2] - b[2] * d[0]) + c[0] * d[1]
				* (b[0] * d[2] - b[2] * d[0]) - a[0] * d[1]
				* (b[0] * d[2] - b[2] * d[0]))
				/ ((e[1] * d[0] - e[0] * d[1]) * (b[0] * d[2] - b[2] * d[0]) - (e[2]
						* d[0] - e[0] * d[2])
						* (b[0] * d[1] - b[1] * d[0]));
		System.out.println("u: " + u);

		System.out.println("u1: " + u1 + "  \tu2: " + u2);
		System.out.println("u3: " + u3 + "  \tu4: " + u4);

		t = .5f;
		v = .25f;
		v = ((-b[1] * t - a[1] - c[1]) * b[0] - (-b[0] * t - a[0] - c[0])
				* b[1])
				/ (e[0] * b[1] - e[1] * b[0]);
		System.out.println("v: " + v);

		u = (e[1] * v - b[1] * t - a[1] + c[1]) / -d[1];
		u = (e[0] * v - b[0] * t - a[0] + c[0]) / -d[0];
		System.out.println("u: " + u);

		// if any axis of b, d, or e is 0 then i can use that as a shortcut.
		// favor a b of 0 because that shorcuts to t.

		// v shorctut when d[1] is 0.s
		v = -(-b[1] * t - a[1] + c[1]) * (-d[0]) / (e[1] * (-d[0]));
		System.out.println("v: " + v);

		v = .25f;
		t = .5f;
		u = .75f;

		t = ((-a[2] + c[2]) * (-d[0]) * (e[1] * (-d[0])) - (-a[1] + c[1])
				* (-d[0]) * (e[2] * (-d[0])))
				/ (-b[1] * (-d[0]) * (e[2] * (-d[0]) + b[2] * (-d[0])
						* (e[1] * (-d[0]))));
		System.out.println("t: " + t);
		t = .5f;
		System.out
				.println(((-a[2] + c[2]) * (-d[0]) * (e[1] * (-d[0])) - (-a[1] + c[1])
						* (-d[0]) * (e[2] * (-d[0]))));
		System.out.println((-b[1] * (-d[0]) * (e[2] * (-d[0])) + (b[2]
				* (-d[0]) * (e[1] * (-d[0])))));
		System.out.println(-b[1] * -d[0] * e[2] * -d[0]);
		System.out.println(b[2] * -d[0] * e[1] * -d[0]);
		t = ((-a[2] + c[2]) * (-d[0]) * (e[1] * (-d[0])) - (-a[1] + c[1])
				* (-d[0]) * (e[2] * (-d[0])))
				/ (-b[1] * -d[0] * e[2] * -d[0] + b[2] * -d[0] * e[1] * -d[0]);
		System.out.println("t: " + t);
		float[] part1 = Vect3d.vectMultScalar(t, b);
		Vect3d.sayVect("part1", part1);
		Vect3d.sayVect("a", a);
		float[] intersect = Vect3d.vectAdd(a, part1);
		Vect3d.sayVect("intersect", intersect);
	}

	void test1() {
		float[] a = { 4, -1, 2 };
		float[] b = { 1, 1, 2 };
		float[] c = { 3, -1, 3 };
		float[] d = { 2, 0, 0 };
		float[] e = { 0, 2, 0 };
		float v = .25f;
		float t = .5f;
		float u = .75f;
		// rune throught b and find a zero.

		// if b[1] = 0;
		t = ((-a[2] + c[2]) * (-d[0]) * (e[1] * (-d[0])) - (-a[1] + c[1])
				* (-d[0]) * (e[2] * (-d[0])))
				/ (-b[1] * -d[0] * e[2] * -d[0] + b[2] * -d[0] * e[1] * -d[0]);
		System.out.println("t: " + t);
		float[] part1 = Vect3d.vectMultScalar(t, b);
		Vect3d.sayVect("part1", part1);
		Vect3d.sayVect("a", a);
		float[] intersect = Vect3d.vectAdd(a, part1);
		Vect3d.sayVect("intersect", intersect);
	}

	void abcdefghijklmnopqrstuvwxyz() {
		// abcdefghijklmnopqrstuvwxyz<>{}[]()|?_-+=,.`~/\
	}

	float[][] objects = createTorus(40, 20, 100, 50);

	float[][] createTorus(float r1, int n1, float r2, int n2) {
		// Create a torus along the y-axis, centered on (0,0)
		// The cross-rections has radius r1 and n1 vertices,
		// The torus has radius r2 and n2 cross-sections.

		float[] c;
		float dx, dy;
		int j, i;
		float theta = 360 / n1;
		float[][] nodes = {};
		float[] node;

		for (j = 0; j < n2; j++) {
			c = new float[] { (float) (r2 * Math.cos(theta * j)), (float) 0,
					(float) (r2 * Math.sin(theta * j)) };

			for (i = 0; i < n1; i++) {
				dx = r1 * (float) Math.cos(theta * i);
				dy = r1 * (float) Math.sin(theta * i);
				node = new float[] { c[0] + dx * (float) Math.cos(theta * j),
						c[1] + dy, c[2] + dx * (float) Math.sin(theta * j) };
				nodes = appendFloatArAr(nodes, node);
			}
		}

		int[][] faces = {};
		for (j = 0; j < n2 - 1; j++) {
			int s = j * n1;
			for (i = 0; i < n1; i++) {
				if (i < n1 - 1) {
					int[] app = { s + i + 1, s + i, s + i + n1, s + i + n1 + 1 };

					faces = appendIntArAr(faces, app);
				} else {
					int[] app = { s + 0, s + i, s + i + n1, s + n1 };
					faces = appendIntArAr(faces, app);
				}
			}
		}
		// return new float[][] { nodes, faces };
		return nodes;
	}

	void draw(Model m1) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		// Model mod = new Model();
		// mod.setModel(m1);
		// mod.translateXY(200, 200);

		float scalar = m.getScalar();

		float i;
		float o;
		// float f;
		int[] face;
		// int[] faces = {};
		float[][] nodes;
		float node1;
		float node2;

		// Faces faces = new Faces();
		Face[] faces = new Face[0];

		Color background = new Color(255, 255, 255);

		// take in a model
		// run though faces

		nodes = new float[m1.getNodes().length][];
		for (int n = 0; n < nodes.length; n++) {
			// nodes[n] = Vect3d.vectAdd(m1.getNodes()[n], m1.getTranslation());
			nodes[n] = Vect3d.vectMultScalar(scalar, m1.getNodes()[n]);
			nodes[n] = Vect3d.vectAdd(nodes[n], m1.getTranslation());
			// nodes[n] = Vect3d.vectMultScalar(scalar, nodes[n]);
			// Vect3d.sayVect("nodes[n]", nodes[n]);
		}
		for (int f = 0; f < m1.getFaces().length; f++) {
			face = m1.getFaces()[f];
			float[] fnorm = normalOfPlane(face, nodes);

			if (fnorm[2] < 0) {
				// Find order in which to draw faces
				// by finding where it intersects the z-axis
				float pos = Vect3d.dot(fnorm, nodes[face[0]]);

				float[][] fnodes;
				if (face.length == 3) {
					fnodes = new float[][] { nodes[face[0]], nodes[face[1]],
							nodes[face[2]] };
				} else {
					fnodes = new float[][] { nodes[face[0]], nodes[face[1]],
							nodes[face[2]], nodes[face[3]] };
				}

				// faces.push([pos / fnorm[2], fnorm, fnodes]);
				// faces.addFace(pos / fnorm[2], fnorm, fnodes);
				Face fay = new Face(pos / fnorm[2], fnorm, fnodes);
				faces = appendFaceAr(faces, fay);
			}
		}

		// sort faces.a from low to high.
		// faces.sortLowToHigh();
		faces = sortFaces(faces);

		Face newFace;
		float[] lightVector = Vect3d.normalise(new float[] { 1, 0, -2 });
		float[] faceColor = { 20, 255, 40 };

		// System.out.println("1 here");
		for (int f = 0; f < faces.length; f++) {
			// System.out.println("2 here");
			newFace = faces[f];
			nodes = newFace.c;
			float l = Vect3d.dot(lightVector, Vect3d.normalise(newFace.b));
			// l += .5f;
			// l /= 1.6;

			// System.out.println("l: " + l);
			// fill(l * faceColor[0], l * faceColor[1], l * faceColor[2]);

			if (newFace.c.length == 3) {
				System.out.println("3");
				// triangle(nodes[0][0], nodes[0][1], nodes[1][0], nodes[1][1],
				// nodes[2][0], nodes[2][1]);
			} else {
				// quad(nodes[0][0], nodes[0][1], nodes[1][0], nodes[1][1],
				// nodes[2][0], nodes[2][1], nodes[3][0], nodes[3][1]);
				int[] xPoints = { (int) nodes[0][0], (int) nodes[1][0],
						(int) nodes[2][0], (int) nodes[3][0] };
				int[] yPoints = { (int) nodes[0][1], (int) nodes[1][1],
						(int) nodes[2][1], (int) nodes[3][1] };
				if (l > 0) {
					g.setColor(new Color((int) (l * faceColor[0]),
							(int) (l * faceColor[1]), (int) (l * faceColor[2])));
					g.fillPolygon(xPoints, yPoints, 4);
				}
			}
		}
	}

	Face[] sortFaces(Face[] faces) {
		// run through and find the lowest a's
		//
		// [0] = a
		// [1] = o
		// old number should be unimportant. No it is needed in order to shape
		// the other variable into order too.
		float[][] order = { { faces[0].a, 0 } };
		for (int o = 1; o < faces.length; o++) {
			boolean stuckIn = false;
			bloop: for (int l = 0; l < order.length; l++) {
				// if the next one is less than this one then stick it in before
				if (faces[o].a < order[l][0]) {
					// stick in before and kill loop
					order = JaMa.injectFloatArAr(order, new float[] {
							faces[o].a, o }, l);
					stuckIn = true;
					break bloop;
				} else {
					// check the next
				}
			}
			if (stuckIn == false) {
				order = JaMa.appendFloatArAr(order,
						new float[] { faces[o].a, o });
			}
		}
		Face[] temp = new Face[faces.length];
		for (int o = 0; o < order.length; o++) {
			temp[o] = faces[(int) order[o][1]];
		}
		return temp;
	}

	float[] normalOfPlane(int[] face, float[][] nodes) {
		float[] n1 = nodes[face[0]];
		float[] n2 = nodes[face[1]];
		float[] n3 = nodes[face[2]];

		float[] v1 = Vect3d.vectSub(n1, n2);
		float[] v2 = Vect3d.vectSub(n1, n3);

		float[] v3 = { (v1[1] * v2[2] - v1[2] * v2[1]),
				(v1[2] * v2[0] - v1[0] * v2[2]),
				(v1[0] * v2[1] - v1[1] * v2[0]) };

		return v3;
	}

	static Face[] appendFaceAr(Face[] st, Face appendage) {
		Face[] temp = new Face[st.length + 1];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	void rotateY3D(float theta) {
		float ct = (float) Math.cos(theta);
		float st = (float) Math.sin(theta);
		float x, y, z;

		for (int i = 0; i < m.nodes.length; i++) {
			x = m.nodes[i][0];
			y = m.nodes[i][1];
			z = m.nodes[i][2];
			// System.out.println("intake (" + m.nodes[i][0] + ", "
			// + m.nodes[i][1] + ", " + m.nodes[i][2] + ")");
			// System.out.println("return (" + (ct * x + st * z) + ", " + (y)
			// + ", " + (-st * x + ct * z) + ")");
			m.nodes[i] = new float[] { ct * x + st * z, y, -st * x + ct * z };
		}
	}

	void rotateX3D(float theta) {
		float ct = (float) Math.cos(theta);
		float st = (float) Math.sin(theta);
		float x, y, z;

		for (int i = 0; i < m.nodes.length; i += 1) {
			x = m.nodes[i][0];
			y = m.nodes[i][1];
			z = m.nodes[i][2];
			m.nodes[i] = new float[] { x, ct * y - st * z, st * y + ct * z };
		}
	}

	/**
	 * Methods go above here.
	 * 
	 */

	int[][] appendIntArAr(int[][] st, int[] appendage) {
		int[][] temp = new int[st.length + 1][];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

	float[][] appendFloatArAr(float[][] st, float[] appendage) {
		float[][] temp = new float[st.length + 1][];
		for (int a = 0; a < st.length; a++) {
			temp[a] = st[a];
		}
		temp[temp.length - 1] = appendage;
		return temp;
	}

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
	public void mousePressed(MouseEvent me) {
		// vectPlaneIntersection();
		lastX = me.getX();
		lastY = me.getY();

		test1();
		draw(m);
		drwGm();
	}

	@Override
	public void mouseClicked(MouseEvent me) {

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
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent ke) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent me) {
		System.out.println("drag");
		int thisX = me.getX();
		int thisY = me.getY();
		float deltaX = thisX - lastX;
		float deltaY = thisY - lastY;
		deltaX /= 80;
		deltaY /= 80;
		System.out.println("deltaX: " + deltaX);
		System.out.println("deltaY: " + deltaY);
		rotateX3D(deltaY);
		rotateY3D(deltaX);
		draw(m);
		drwGm();
		lastX = thisX;
		lastY = thisY;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
