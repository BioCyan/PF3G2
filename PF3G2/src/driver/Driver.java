package driver;

import javax.swing.*;

import gameInterface.GameInterface;

import java.awt.*;

public class Driver implements GameInterface{

	public static void main(String[] args) {
		//System.out.println(new Maze(32, 32));
		
		JFrame frame = new JFrame("3D Test");
		
		frame.setSize(screenSize);
		Game window = new Game(screenSize);
		frame.add(window);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
