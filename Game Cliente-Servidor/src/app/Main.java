package app;

import java.awt.EventQueue;

import frame.Game;

public class Main {
	
	// Main da classe Game EU ADICIONEI ISSO AQUI!!!
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
