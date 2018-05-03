package driver;

import javax.swing.*;

public class Driver implements GameInterface{

	public static void main(String[] args) {
		JFrame frame = new JFrame("3D Test");
		
		frame.setSize(screenSize);
		Game window = new Game(screenSize);
		frame.add(window);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
