package sandbox;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.tantaman.eats.aop.pub.annotations.debug.concurrent.NotOnThread;

public class Temp {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button = new JButton("do operation");
		JButton otherButton = new JButton("other button");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked");
				new LongRunningTask().run();
			}
		});
		
		otherButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
				JLabel label = new JLabel("popup");
				frame.getContentPane().add(label);
				
				frame.pack();
				frame.validate();
				frame.setVisible(true);
			}
		});
		
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(button);
		frame.getContentPane().add(otherButton);
		frame.pack();
		frame.validate();
		frame.setVisible(true);
	}
	
	private static class LongRunningTask implements Runnable {
		 @NotOnThread("AWT-EventQueue")
		public void run() {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
