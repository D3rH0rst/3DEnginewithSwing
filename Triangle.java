package swing.java3d.max;

public class Triangle {
	private Vertex v1, v2, v3;
	private float x1, y1, z1, x2, y2, z2, x3, y3, z3;
	Vertex[] vertices;
	
	private float color;
	public Triangle(Vertex v1, Vertex v2, Vertex v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		
		x1 = v1.getX();
		y1 = v1.getY();
		z1 = v1.getZ();
		x2 = v2.getX();
		y2 = v2.getY();
		z2 = v2.getZ();
		x3 = v3.getX();
		y3 = v3.getY();
		z3 = v3.getZ();
		
		vertices = new Vertex[] {v1, v2, v3};
	}
	
	public Triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.x3 = x3;
		this.y3 = y3;
		this.z3 = z3;
		
		v1 = new Vertex(x1, y1, z1);
		v2 = new Vertex(x2, y2, z2);
		v3 = new Vertex(x3, y3, z3);
		
		vertices = new Vertex[] {v1, v2, v3};
	}
	
	public Triangle() {
		this.x1 = 0;
		this.y1 = 0;
		this.z1 = 0;
		this.x2 = 0;
		this.y2 = 0;
		this.z2 = 0;
		this.x3 = 0;
		this.y3 = 0;
		this.z3 = 0;
		
		v1 = new Vertex(x1, y1, z1);
		v2 = new Vertex(x2, y2, z2);
		v3 = new Vertex(x3, y3, z3);
		
		vertices = new Vertex[] {v1, v2, v3};
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public Vertex getVertex1() {
		return v1;
	}
	public Vertex getVertex2() {
		return v2;
	}
	public Vertex getVertex3() {
		return v3;
	}
	
	public void setVertex1(Vertex v) {
		v1 = v;
		vertices[0] = v1;
	}
	
	public void setVertex2(Vertex v) {
		v2 = v;
		vertices[1] = v2;
	}
	
	public void setVertex3(Vertex v) {
		v3 = v;
		vertices[2] = v3;
	}
	public void setVertices(Vertex[] v) {
		v1 = v[0];
		v2 = v[1];
		v3 = v[2];
		
		vertices = new Vertex[] {v1, v2, v3};
		
	}
	
	public void setColor(float col) {
		color = col;
	}
	
	public float getColor() {
		return color;
	}
	
	public float zAverage() {
		return (v1.getZ() + v2.getZ() + v3.getZ()) / 3f;
	}
	
	
	
	
	
}
