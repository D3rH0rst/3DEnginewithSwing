package swing.java3d.max;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class MyFrame extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	private MyPanel panel;

	public MyFrame() {
		panel = new MyPanel();
		this.add(panel);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.addKeyListener(this);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			if (!panel.isGoForward()) {
				panel.setGoForward();
			}
			break;
		case 's':
			if (!panel.isGoBackward()) {
				panel.setGoBackward();
			}
			break;
		case 'a':
			if (!panel.isLeft()) {
				panel.setLeft();
			}
			break;
		case 'd':
			if (!panel.isRight()) {
				panel.setRight();
			}
			break;
		}
		
		

		
		

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if (keycode == KeyEvent.VK_SPACE) {
			if (!panel.isUp()) {
				panel.setUp();
			}
		} else if (keycode == KeyEvent.VK_CONTROL) {
			if (!panel.isDown()) {
				panel.setDown();
			}
		} else if (keycode == KeyEvent.VK_LEFT) {
			if (!panel.isYawLeft()) {
				panel.setYawLeft();
			}
		} else if (keycode == KeyEvent.VK_RIGHT) {
			if (!panel.isYawRight()) {
				panel.setYawRight();
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			panel.unSetGoForward();
			break;
		case 's':
			panel.unSetGoBackward();
			break;
		case 'a':
			panel.unSetLeft();
			break;
		case 'd':
			panel.unSetRight();
			break;
		case 'r':
			panel.toggleRotate();
			break;
		case 'f':
			panel.resetRotate();
			break;
		case 'm':
			panel.toggleMesh();
			break;
		}

		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			panel.unSetUp();
			break;
		case KeyEvent.VK_CONTROL:
			panel.unSetDown();
			break;
		case KeyEvent.VK_LEFT:
			panel.unSetYawLeft();
			break;
		case KeyEvent.VK_RIGHT:
			panel.unSetYawRight();
			break;

		}

	}


}
