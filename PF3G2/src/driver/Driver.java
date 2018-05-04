package driver;

import javax.swing.*;

//Additional comments have been added
//but the code has been left the same as when we presented
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
