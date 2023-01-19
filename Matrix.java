package swing.java3d.max;

public class Matrix {
	private int m, n;
	private float[][] matrix;

	public Matrix(int m, int n) {
		this.m = m;
		this.n = n;
		this.matrix = new float[m][n];
		reset();
	}

	public Matrix(float[][] matrix) {
		m = matrix.length;
		n = matrix[0].length;
		this.matrix = matrix;
	}

	public Matrix() {
		this.m = 4;
		this.n = 4;
		this.matrix = new float[m][n];
		reset();
	}

	public Vertex multiplyMatrixVector4x4(Vertex v) {

		Vertex vOut = new Vertex();

		vOut.setX(v.getX() * matrix[0][0] + v.getY() * matrix[1][0] + v.getZ() * matrix[2][0] + matrix[3][0]);
		vOut.setY(v.getX() * matrix[0][1] + v.getY() * matrix[1][1] + v.getZ() * matrix[2][1] + matrix[3][1]);
		vOut.setZ(v.getX() * matrix[0][2] + v.getY() * matrix[1][2] + v.getZ() * matrix[2][2] + matrix[3][2]);
		vOut.setW(v.getX() * matrix[0][3] + v.getY() * matrix[1][3] + v.getZ() * matrix[2][3] + matrix[3][3]);

		return vOut;
	}

	public void setAt(int x, int y, float value) {
		matrix[x][y] = value;
	}

	public float getAt(int x, int y) {
		return matrix[x][y];
	}

	public void makeIdentityMatrix() {
		reset();
		for (int i = 0; i < m; i++) {
			matrix[i][i] = 1;
		}
	}

	public void makeProjectionMatrix(float fNear, float fFar, float fFov, float fAspectRatio, float fFovRad) {
		reset();
		matrix[0][0] = fAspectRatio * fFovRad;
		matrix[1][1] = fFovRad;
		matrix[2][2] = fFar / (fFar - fNear);
		matrix[2][3] = 1;
		matrix[3][2] = (-fFar * fNear) / (fFar - fNear);
	}

	public void makeRotationXMatrix(float theta) {
		reset();
		matrix[0][0] = 1;
		matrix[3][3] = 1;
		matrix[1][1] = (float) Math.cos(theta);
		matrix[1][2] = (float) Math.sin(theta);
		matrix[2][1] = (float) -Math.sin(theta);
		matrix[2][2] = (float) Math.cos(theta);
	}

	public void makeRotationZMatrix(float theta) {
		reset();
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		matrix[0][0] = (float) Math.cos(theta);
		matrix[0][1] = (float) Math.sin(theta);
		matrix[1][0] = (float) -Math.sin(theta);
		matrix[1][1] = (float) Math.cos(theta);
	}
	
	public void makeRotationYMatrix(float theta) {
		reset();
		matrix[0][0] = (float) Math.cos(theta);
		matrix[0][2] = (float) Math.sin(theta);
		matrix[1][1] = 1;
		matrix[2][0] = (float) -Math.sin(theta);
		matrix[2][2] = (float) Math.cos(theta);
		matrix[3][3] = 1;
	}

	public void makeTranslationMatrix(float x, float y, float z) {
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		matrix[3][0] = x;
		matrix[3][1] = y;
		matrix[3][2] = z;
	}
	
	public void makePointAtMatrix(Vertex pos, Vertex target, Vertex up) {
		//Calculate new Forward
		Vertex forward = Vertex.subtractVertex(target, pos);
		forward.setNormalVector();
		
		//Calculate new Up
		Vertex a = Vertex.multiplyVertex(forward, Vertex.dotP(up, forward));
		Vertex newUp = Vertex.subtractVertex(up, a);
		newUp.setNormalVector();
		
		//Calculate new Right
		Vertex right = Vertex.crossP(newUp, forward);
		
		// Makes the 4x4 Matrix all zeros
		reset();
		
		matrix[0][0] = right.getX();
		matrix[0][1] = right.getY();
		matrix[0][2] = right.getZ();
		
		matrix[1][0] = newUp.getX();
		matrix[1][1] = newUp.getY();
		matrix[1][2] = newUp.getZ();
		
		matrix[2][0] = forward.getX();
		matrix[2][1] = forward.getY();
		matrix[2][2] = forward.getZ();
		
		matrix[3][0] = pos.getX();
		matrix[3][1] = pos.getY();
		matrix[3][2] = pos.getZ();
		matrix[3][3] = 1;
	}
	
	
	
	public static Matrix invertMatrix(Matrix m) {
		Matrix result = new Matrix();
		
		result.setAt(0, 0, m.getAt(0, 0));
		result.setAt(0, 1, m.getAt(1, 0));
		result.setAt(0, 2, m.getAt(2, 0));
		
		result.setAt(1, 0, m.getAt(0, 1));
		result.setAt(1, 1, m.getAt(1, 1));
		result.setAt(1, 2, m.getAt(2, 1));
		
		result.setAt(2, 0, m.getAt(0, 2));
		result.setAt(2, 1, m.getAt(1, 2));
		result.setAt(2, 2, m.getAt(2, 2));
		
		result.setAt(3, 0, -(m.getAt(3, 0) * m.getAt(0, 0) + m.getAt(3, 1) * m.getAt(1, 0) + m.getAt(3, 2) * m.getAt(2, 0)));
		result.setAt(3, 1, -(m.getAt(3, 0) * m.getAt(0, 1) + m.getAt(3, 1) * m.getAt(1, 1) + m.getAt(3, 2) * m.getAt(2, 1)));
		result.setAt(3, 2, -(m.getAt(3, 0) * m.getAt(0, 2) + m.getAt(3, 1) * m.getAt(1, 2) + m.getAt(3, 2) * m.getAt(2, 2)));
		result.setAt(3, 3, 1);
		
		return result;
	}

	public static Matrix multiplyMatrix(Matrix m1, Matrix m2) {
		Matrix result = new Matrix();

		for (int c = 0; c < 4; c++) {
			for (int r = 0; r < 4; r++) {
				result.setAt(r, c, m1.getAt(r, 0) * m2.getAt(0, c) + m1.getAt(r, 1) * m2.getAt(1, c)
						+ m1.getAt(r, 2) * m2.getAt(2, c) + m1.getAt(r, 3) * m2.getAt(3, c));
			}
		}

		return result;
	}

	private void reset() {
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				this.matrix[i][j] = 0;
			}
		}
	}

}
