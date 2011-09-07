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

package com.tantaman.eats.demo;

import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnExecutor;
import com.tantaman.eats.demo.gui.SceneListPanel;
import com.tantaman.eats.demo.gui.ViewerPanel;

public class SceneController {
	private final URLImageLoader mImageLoader;
	private final ViewerPanel mViewer;
	private final SceneListPanel mScenePanel;
	
	public SceneController(SceneListPanel scenePanel,
			ViewerPanel viewerPanel) {
		mImageLoader = new URLImageLoader();
		mViewer = viewerPanel;
		mScenePanel = scenePanel;
		initKeyDispatcher();
	}
	
	private void initKeyDispatcher() {
		KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (e.getID() == KeyEvent.KEY_PRESSED)
						previousScene();
					break;
				case KeyEvent.VK_RIGHT:
					if (e.getID() == KeyEvent.KEY_PRESSED)
						nextScene();
					break;
				}
				
				return false;
			}
		};
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
	}

	// FIXME PutOnExecutor
	@PutOnExecutor
	public void loadScene(String pUrl) {
		Image image;
		try {
			image = mImageLoader.loadImage(pUrl);
			mScenePanel.addScene(image, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void nextScene() {
		mScenePanel.selectNextScene();
	}
	
	public void previousScene() {
		mScenePanel.selectPreviousScene();
	}

	public void sceneSelected(Image mImage) {
		mViewer.setScene(mImage);
	}
	
	public void sceneDeSelected() {
		
	}
}
