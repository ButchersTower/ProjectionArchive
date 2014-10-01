package lavaGlobe;

import javax.swing.JFrame;

public class LavaGlobe {
	// extends JFrame
	public LavaGlobe() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Panel());
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("LavaGlobe");
	}

	public static void main(String[] args) {
		new LavaGlobe();
	}
}
