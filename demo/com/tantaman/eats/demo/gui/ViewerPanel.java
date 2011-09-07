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

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.concurrent.TimeUnit;

import com.tantaman.eats.aop.pub.annotations.concurrent.CombineInvocations;
import com.tantaman.eats.aop.pub.annotations.concurrent.PutOnFoldingExecutor;
import com.tantaman.eats.aop.pub.annotations.concurrent.ReScheduleExecution;
import com.tantaman.eats.aop.pub.annotations.concurrent.UniqueConcurrentExecutions;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.ConcurrentAccesses;
import com.tantaman.eats.aop.pub.annotations.debug.concurrent.OnThread;
import com.tantaman.eats.demo.ImageScaler;
import com.tantaman.eats.demo.URLImageLoader;

public class ViewerPanel extends Canvas {
	private static final long serialVersionUID = 1L;
	private Image mImage;
	private BufferStrategy mBuffer;

	public ViewerPanel() {
	}

	public void setScene(Image pImage) {
		mImage = pImage; 
		updateDisplayImage();
	}
	
	private void createBuffer() {
		createBufferStrategy(2);
		mBuffer = getBufferStrategy();
	}
	
	@Override
	public void paint(Graphics g) {
		updateDisplayImage();
	}

	protected void render(Graphics g, Image pDisplayImage, BufferStrategy pBufferStrat) {
			g.fillRect(0, 0, getSize().width, getSize().height);
			int displayImgWidth = pDisplayImage.getWidth(null);
			int xOffset = (getPreferredSize().width - displayImgWidth) / 2;
			g.drawImage(pDisplayImage, xOffset, 0, null);
			pBufferStrat.show();
	}
	
	// FIXME CombineInvocations
	@CombineInvocations
	private void updateDisplayImage() {
		if (mImage != null) {
			Image displayImage = ImageScaler.scaleImage(mImage, getSize(), false, true);
			URLImageLoader.waitForImage(displayImage);
			
			if (mBuffer == null)
				createBuffer();
			Graphics g = mBuffer.getDrawGraphics();
			render(g, displayImage, mBuffer);
			g.dispose();
		}
	}
}
