package projectionBucket;

import javax.swing.JFrame;

public class ProjectionBucket {
	// extends JFrame
	public ProjectionBucket() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new PanelSlow());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("ProjectionBucket");
	}

	public static void main(String[] args) {
		new ProjectionBucket();
	}
}
