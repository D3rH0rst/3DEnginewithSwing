package swing.java3d.max;

public class Vertex {
	private float x, y, z, w;
	
	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}
	
	public Vertex() {
		x = 0;
		y = 0;
		z = 0;
		w = 1;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public float getW() {
		return w;
	}
	
	public void setW(float w) {
		this.w = w;
	}
	
	public static Vertex crossP(Vertex v1, Vertex v2) {
		Vertex vOut = new Vertex();
		
		vOut.setX(v1.getY() * v2.getZ() - v2.getY() * v1.getZ());
		vOut.setY(v2.getX() * v1.getZ() - v1.getX() * v2.getZ());
		vOut.setZ(v1.getX() * v2.getY() - v2.getX() * v1.getY());
		
		return vOut;
	}
	
	public static float dotP(Vertex v1, Vertex v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
	}
	
	public static Vertex addVertex(Vertex v1, Vertex v2) {
		Vertex vOut = new Vertex();
		
		vOut.setX(v1.getX() + v2.getX());
		vOut.setY(v1.getY() + v2.getY());
		vOut.setZ(v1.getZ() + v2.getZ());
		
		return vOut;
	}
	
	public static Vertex subtractVertex(Vertex v1, Vertex v2) {
		Vertex vOut = new Vertex();
		
		vOut.setX(v1.getX() - v2.getX());
		vOut.setY(v1.getY() - v2.getY());
		vOut.setZ(v1.getZ() - v2.getZ());
		
		return vOut;
	}
	
	public static Vertex multiplyVertex(Vertex v, float f) {
		Vertex vOut = new Vertex();
		
		vOut.setX(v.getX() * f);
		vOut.setY(v.getY() * f);
		vOut.setZ(v.getZ() * f);
		
		return vOut;
		
	}
	
	public static Vertex divideVertex(Vertex v, float f) {
		Vertex vOut = new Vertex();
		
		vOut.setX(v.getX() / f);
		vOut.setY(v.getY() / f);
		vOut.setZ(v.getZ() / f);
		
		return vOut;
		
	}
	
	public void setNormalVector() {
		float len = (float) Math.sqrt(x*x + y*y + z*z);
		
		x /= len;
		y /= len;
		z /= len;
	}
	
	
	
	
	
	
	
}
