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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tantaman.eats.demo.SceneController;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int SCENE_URL_COLS = 25;
	private static final String SCENE_URL = "Scene URL:";
	private static final String LOAD_SCENE = "Load";
	private static final String NEXT_SCENE = "Next Scene";
	private static final String PREVIOUS_SCENE = "Previous Scene";
	
	private final JLabel mSceneUrl;
	private final JTextField mSceneToLoad;
	private final JButton mLoadScene;
	private final JButton mNextScene;
	private final JButton mPrevScene;
	
	private final SceneController mSceneController;
	
	public ControlPanel(SceneController pSceneController) {
		mSceneUrl = new JLabel(SCENE_URL);
		mSceneToLoad = new JTextField();
		mSceneToLoad.setColumns(SCENE_URL_COLS);
		mLoadScene = new JButton(LOAD_SCENE);
		mNextScene = new JButton(NEXT_SCENE);
		mPrevScene = new JButton(PREVIOUS_SCENE);
		
		EDTStatusFeedback fb = new EDTStatusFeedback();
		fb.setPreferredSize(new Dimension(50, 50));
		add(fb);
		add(mSceneUrl);
		add(mSceneToLoad);
		add(mLoadScene);
		add(mPrevScene);
		add(mNextScene);
		
		mSceneController = pSceneController;
		
		initListeners();
	}

	private void initListeners() {
		mLoadScene.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mSceneController.loadScene(mSceneToLoad.getText());
			}
		});
		
		mPrevScene.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mSceneController.previousScene();
			}
		});
		
		mNextScene.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mSceneController.nextScene();
				
			}
		});
	}
}
