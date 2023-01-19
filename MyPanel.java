package swing.java3d.max;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import javax.swing.JPanel;

public class MyPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;

	private final int HW = WIDTH / 2;
	private final int HH = HEIGHT / 2;

	private float fNear = 0.1f;
	private float fFar = 1000f;
	private float fFov = 90f;
	private float fAspectRatio = (float) HEIGHT / (float) WIDTH;
	private float fFovRad = (float) (1 / Math.tan(fFov * 0.5f / 180f * Math.PI));
	private float yaw;

	private long startTime;
	private long endTime;
	private long FPS;

	public int framesPerSecond;

	Timer timer;

	Mesh meshCube;
	Vertex camera;
	Vertex look_direction;
	Vertex light_direction;
	Matrix matProj, matRotX, matCameraRot, matRotZ, translationMatrix, worldMatrix, matCam, matView;

	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private boolean yawLeft = false;
	private boolean yawRight = false;
	private boolean goForward = false;
	private boolean goBackward = false;
	private boolean rotate = false;
	private boolean drawMesh = false;

	ArrayList<Triangle> trisToDraw;

	float theta = 0;

	public MyPanel() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.black);

		camera = new Vertex();
		look_direction = new Vertex(0, 0, 1);
		yaw = 0;

		light_direction = new Vertex(-0.5f, 0.5f, -1);
		light_direction.setNormalVector();

		meshCube = new Mesh("recource/axis.obj");

		matProj = new Matrix();
		matProj.makeProjectionMatrix(fNear, fFar, fFov, fAspectRatio, fFovRad);

		matRotX = new Matrix();
		matCameraRot = new Matrix();
		matRotZ = new Matrix();
		translationMatrix = new Matrix();
		worldMatrix = new Matrix();
		matCam = new Matrix();
		matView = new Matrix();
		Font font = new Font("Calibri", Font.BOLD, 24);
		this.setFont(font);

		timer = new Timer(1, this);
		timer.start();

	}

	public void paint(Graphics g) {
		startTime = System.nanoTime();

		Graphics2D g2D = (Graphics2D) g;
		super.paint(g);

		// Camera Movement
		if (up) {
			camera.setY(camera.getY() - 0.25f);
		}

		if (down) {
			camera.setY(camera.getY() + 0.25f);
		}

		if (left) {
			camera.setX(camera.getX() - 0.25f);
		}

		if (right) {
			camera.setX(camera.getX() + 0.25f);
		}

		Vertex camForward = Vertex.multiplyVertex(look_direction, 0.25f);

		if (goForward) {
			camera = Vertex.addVertex(camera, camForward);
		}

		if (goBackward) {
			camera = Vertex.subtractVertex(camera, camForward);
		}

		if (yawLeft) {
			yaw += 0.01f;
		}

		if (yawRight) {
			yaw -= 0.01f;
		}

		// Rotation Matrices

		matRotX.makeRotationXMatrix(theta * 0.5f);
		matRotZ.makeRotationZMatrix(theta);

		// Translation Matrix
		translationMatrix.makeTranslationMatrix(0, 0, 5);

		// World Matrix
		worldMatrix.makeIdentityMatrix();
		worldMatrix = Matrix.multiplyMatrix(matRotX, matRotZ);
		worldMatrix = Matrix.multiplyMatrix(worldMatrix, translationMatrix);

		Vertex upVec = new Vertex(0, 1, 0);
		Vertex targetVec = new Vertex(0, 0, 1);

		matCameraRot.makeRotationYMatrix(yaw);
		look_direction = matCameraRot.multiplyMatrixVector4x4(targetVec);
		look_direction.setNormalVector();
		// look_direction = new Vertex(0, 0, 1);
		targetVec = Vertex.addVertex(camera, look_direction);

		matCam.makePointAtMatrix(camera, targetVec, upVec);
		matView = Matrix.invertMatrix(matCam);

		if (rotate) {
			theta += 0.02;
		}

		trisToDraw = new ArrayList<Triangle>();

		for (Triangle tri : meshCube.getTriangles()) {

			Triangle triProj = new Triangle();
			Triangle triTransformed = new Triangle();
			Triangle triViewed = new Triangle();

			triTransformed.setVertex1(worldMatrix.multiplyMatrixVector4x4(tri.getVertex1()));
			triTransformed.setVertex2(worldMatrix.multiplyMatrixVector4x4(tri.getVertex2()));
			triTransformed.setVertex3(worldMatrix.multiplyMatrixVector4x4(tri.getVertex3()));

			Vertex normal = new Vertex();
			Vertex line1 = new Vertex();
			Vertex line2 = new Vertex();

			line1 = Vertex.subtractVertex(triTransformed.getVertex2(), triTransformed.getVertex1());
			line2 = Vertex.subtractVertex(triTransformed.getVertex3(), triTransformed.getVertex1());

			normal = Vertex.crossP(line1, line2);

			normal.setNormalVector();

			Vertex cameraRay = Vertex.subtractVertex(triTransformed.getVertex1(), camera);
			if (Vertex.dotP(normal, cameraRay) < 0) {

				// Lighting
				float lightDP = Vertex.dotP(normal, light_direction);
				float color_value = linearInterpolateColor(lightDP);

				triProj.setColor(color_value);

				triViewed.setVertex1(matView.multiplyMatrixVector4x4(triTransformed.getVertex1()));
				triViewed.setVertex2(matView.multiplyMatrixVector4x4(triTransformed.getVertex2()));
				triViewed.setVertex3(matView.multiplyMatrixVector4x4(triTransformed.getVertex3()));

				// Do the ProjectionMath
				triProj.setVertex1(matProj.multiplyMatrixVector4x4(triViewed.getVertex1()));
				triProj.setVertex2(matProj.multiplyMatrixVector4x4(triViewed.getVertex2()));
				triProj.setVertex3(matProj.multiplyMatrixVector4x4(triViewed.getVertex3()));

				// SCALE INTO VIEW
				triProj.setVertex1(Vertex.divideVertex(triProj.getVertex1(), triProj.getVertex1().getW()));
				triProj.setVertex2(Vertex.divideVertex(triProj.getVertex2(), triProj.getVertex2().getW()));
				triProj.setVertex3(Vertex.divideVertex(triProj.getVertex3(), triProj.getVertex3().getW()));

				Vertex offsetView = new Vertex(1, 1, 0);

				triProj.setVertex1(Vertex.addVertex(triProj.getVertex1(), offsetView));
				triProj.setVertex2(Vertex.addVertex(triProj.getVertex2(), offsetView));
				triProj.setVertex3(Vertex.addVertex(triProj.getVertex3(), offsetView));

				triProj.getVertex1().setX(triProj.getVertex1().getX() * HW);
				triProj.getVertex1().setY(triProj.getVertex1().getY() * HH);
				triProj.getVertex2().setX(triProj.getVertex2().getX() * HW);
				triProj.getVertex2().setY(triProj.getVertex2().getY() * HH);
				triProj.getVertex3().setX(triProj.getVertex3().getX() * HW);
				triProj.getVertex3().setY(triProj.getVertex3().getY() * HH);

				trisToDraw.add(triProj);

			}
		}

		trisToDraw.sort((Triangle t1, Triangle t2) -> Float.compare(t2.zAverage(), t1.zAverage()));

		for (Triangle tri : trisToDraw) {

			Polygon t = new Polygon();
			t.npoints = 3;
			t.xpoints = new int[] { (int) tri.getVertex1().getX(), (int) tri.getVertex2().getX(),
					(int) tri.getVertex3().getX() };
			t.ypoints = new int[] { (int) tri.getVertex1().getY(), (int) tri.getVertex2().getY(),
					(int) tri.getVertex3().getY() };

			// Draw the solid
			g2D.setColor(new Color((int) tri.getColor(), (int) tri.getColor(), (int) tri.getColor()));
			g2D.fillPolygon(t);

			// Draw the mesh
			if (drawMesh) {
				g2D.setColor(Color.black);
				g2D.drawPolygon(t);
			}
		}

		endTime = System.nanoTime();

		// Draw the FPS counter on the screen
		FPS = endTime - startTime;

		framesPerSecond = (int) (1f / (FPS / 1000000000f));

		g2D.setColor(Color.white);
		g2D.drawString("" + framesPerSecond + " FPS", 50, 50);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();
	}

	public float linearInterpolateColor(float val) {
		if (val < 0) {
			return 0;
		}
		return 255 * val;
	}

	public void setLeft() {
		left = true;
	}

	public void unSetLeft() {
		left = false;
	}

	public void setRight() {
		right = true;
	}

	public void unSetRight() {
		right = false;
	}

	public void setUp() {
		up = true;
	}

	public void unSetUp() {
		up = false;
	}

	public void setDown() {
		down = true;
	}

	public void unSetDown() {
		down = false;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isYawLeft() {
		return yawLeft;
	}

	public boolean isYawRight() {
		return yawRight;
	}

	public void setYawLeft() {
		yawLeft = true;
	}

	public void setYawRight() {
		yawRight = true;
	}

	public void unSetYawLeft() {
		yawLeft = false;
	}

	public void unSetYawRight() {
		yawRight = false;
	}

	public boolean isGoForward() {
		return goForward;
	}

	public void setGoForward() {
		goForward = true;
	}

	public void unSetGoForward() {
		goForward = false;
	}

	public boolean isGoBackward() {
		return goBackward;
	}

	public void setGoBackward() {
		goBackward = true;
	}

	public void unSetGoBackward() {
		goBackward = false;
	}

	public void toggleRotate() {
		rotate = !rotate;
	}

	public void resetRotate() {
		theta = 0;
	}

	public void toggleMesh() {
		drawMesh = !drawMesh;
	}
}
