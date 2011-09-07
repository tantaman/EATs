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

import java.awt.Image;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;

import com.tantaman.eats.demo.SceneController;

public class SceneListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	// TODO: make a button group
	private final ButtonGroup mScenes;
	
	public SceneListPanel() {
		mScenes = new ButtonGroup();
	}
	
	public void addScene(Image pImage, SceneController pController) {
		SceneThumbnailComponent tc = new SceneThumbnailComponent(pImage, pController, mScenes);
		mScenes.add(tc);
		add(tc);
		validate();
	}
	
	// TODO: button group sucks...
	public void selectNextScene() {
		ButtonModel selection = mScenes.getSelection();
		Enumeration<AbstractButton> buttons = mScenes.getElements();
		
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (selection == null) {
				mScenes.setSelected(button.getModel(), true);
				break;
			} else if (button.getModel().equals(selection)) {
				if (buttons.hasMoreElements()) {
					button = buttons.nextElement();
					mScenes.setSelected(button.getModel(), true);
					break;
				} else {
					ButtonModel model = mScenes.getElements().nextElement().getModel();
					mScenes.setSelected(model, true);
					break;
				}
			}
		}
	}

	public void selectPreviousScene() {
		ButtonModel selection = mScenes.getSelection();
		Enumeration<AbstractButton> buttons = mScenes.getElements();
		
		ButtonModel lastButton = null;
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (selection == null) {
				setLastButtonSelected();
				break;
			} else if (button.getModel().equals(selection)) {
				if (lastButton == null) {
					setLastButtonSelected();
					break;
				} else {
					mScenes.setSelected(lastButton, true);
				}
			}
			lastButton = button.getModel();
		}
	}
	
	private void setLastButtonSelected() {
		Enumeration<AbstractButton> buttons = mScenes.getElements();
		AbstractButton button = null;
		while (buttons.hasMoreElements()) {
			button = buttons.nextElement();
		}
		
		if (button != null)
			mScenes.setSelected(button.getModel(), true);
	}
}
