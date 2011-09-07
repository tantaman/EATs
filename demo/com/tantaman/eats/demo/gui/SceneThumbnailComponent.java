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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;

import com.tantaman.eats.demo.ImageScaler;
import com.tantaman.eats.demo.SceneController;
import com.tantaman.eats.demo.URLImageLoader;
import com.tantaman.eats.demo.gui.borders.ColoredBorderDrawer;
import com.tantaman.eats.demo.gui.borders.EmptyBorderDrawer;
import com.tantaman.eats.demo.gui.borders.IBorderDrawer;

public class SceneThumbnailComponent extends AbstractButton {
	private static final long serialVersionUID = 1L;

	private static final int BORDER_WIDTH = 2;
	private static final Color BORDER_COLOR = Color.orange;
	private static final IBorderDrawer NO_BORDER_DRAWER = new EmptyBorderDrawer();
	private static final IBorderDrawer COLORED_BORDER_DRAWER = new ColoredBorderDrawer(BORDER_COLOR, BORDER_WIDTH);
	private static final Dimension SIZE = new Dimension(50,50);
	
	private final Image mImage;
	private final Image mThumbnail;
	private final SceneController mController;
	private final ButtonGroup mButtonGroup;
	
	private IBorderDrawer mBorderDrawer;
	
	public SceneThumbnailComponent(Image pImage, SceneController pController, ButtonGroup pButtonGroup) {
		model = new DefaultButtonModel();
		mImage = pImage;
		mController = pController;
		mThumbnail = ImageScaler.scaleImage(pImage, 
				SIZE, false, true);
		mBorderDrawer = NO_BORDER_DRAWER;
		mButtonGroup = pButtonGroup;
		// create a scaled version of mImage....
		URLImageLoader.waitForImage(mThumbnail);
		setPreferredSize(new Dimension(mThumbnail.getWidth(null) + BORDER_WIDTH * 2,
				mThumbnail.getHeight(null) + BORDER_WIDTH * 2));
		
		initListeners();
	}
	
	private void initListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//setSelected(true);
				mButtonGroup.setSelected(SceneThumbnailComponent.this.getModel(), true);
			}
		});
		
		getModel().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				switch (e.getStateChange()) {
				case ItemEvent.SELECTED:
					selected();
					break;
				default:
					deSelected();
				}
			}
		});
	}
	
	private void deSelected() {
		mBorderDrawer = NO_BORDER_DRAWER;
		repaint();
	}
	
	private void selected() {
		mController.sceneSelected(mImage);
		mBorderDrawer = COLORED_BORDER_DRAWER;
		repaint();
	}
	
	@Override
	public void setSelected(boolean b) {
		super.setSelected(b);
		if (b) {
			selected();
		} else {
			deSelected();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		mBorderDrawer.drawBorder(g, getPreferredSize());
		g.drawImage(mThumbnail, BORDER_WIDTH, BORDER_WIDTH, null);
	}
}
