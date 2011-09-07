package com.tantaman.eats.demo.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

public class EDTStatusFeedback extends JComponent {
	private final ScheduledExecutorService mExecutor = Executors.newScheduledThreadPool(1);
	private final Random mRandom = new Random();
	public EDTStatusFeedback() {
		start();
	}
	
	// TODO: implement schedule execution nutrient
	private void start() {
		mExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				repaint();	
			}
		}, 0L, 100L, TimeUnit.MILLISECONDS);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Color c = g.getColor();
		
		g.setColor(getRandomColor());
		g.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		g.setColor(c);
	}
	
	private Color getRandomColor() {
		return new Color(mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
	}
}
