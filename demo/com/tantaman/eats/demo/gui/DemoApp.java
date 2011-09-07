/*
*   Copyright 2010 Matthew Crinklaw-Vogt
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*  limitations under the License.
*/

package com.tantaman.eats.demo.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tantaman.eats.demo.SceneController;

public class DemoApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Dimension viewerSize = new Dimension(800, 600);
	private static final Dimension sceneSelectorSize = new Dimension(800, 54);

	public static void main(String[] args) {
		DemoApp app = new DemoApp();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}
	
	public DemoApp() {
		BoxLayout layout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		setLayout(layout);
		
		SceneListPanel scenePanel = new SceneListPanel();
		ViewerPanel viewerPanel = new ViewerPanel();
		viewerPanel.setPreferredSize(viewerSize);
		
		SceneController sceneController = new SceneController(scenePanel, viewerPanel);
		JPanel controlPanel = new ControlPanel(sceneController);
		scenePanel.setPreferredSize(sceneSelectorSize);
		scenePanel.setBackground(Color.black);
		
		add(controlPanel);
		add(viewerPanel);
		add(scenePanel);
		
		pack();
		validate();
	}
}
