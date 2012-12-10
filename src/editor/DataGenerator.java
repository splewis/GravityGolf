package editor;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game.*;
import structures.*;

/**
 * @author Sean Lewis
 */
public class DataGenerator extends JFrame {

	ArrayList<Level> levels;
	JTextField field = new JTextField(6);
	JButton button = new JButton("Calculate");

	/**
	 * @throws IOException
	 */
	public DataGenerator() throws IOException {
		DataHandler reader = new DataHandler();
		levels = reader.getLevelData("levels/levels.txt");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String in = field.getText();
					if (in.indexOf("-") == -1) {
						int num = Integer.parseInt(field.getText());
						System.out.println("Start");
						levels.get(num - 1).calculateSolutionSet(300.0);
					} else {
						String ar[] = in.split("-");
						int a = Integer.parseInt(ar[0]);
						int b = Integer.parseInt(ar[1]);
						for (int i = a; i <= b; i++) {
							levels.get(i - 1).calculateSolutionSet(300.0);
							System.out.println("Done with level " + (i) + ".");
						}
					}

					System.out.println("Done");
					field.setText("");
				} catch (Exception e) {
				}
			}
		});

		JPanel p = new JPanel();
		p.add(field);
		p.add(button);
		add(p);
		setSize(200, 80);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * 
	 */
	public void paint(Graphics g) {

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new DataGenerator();
	}

}