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

package com.tantaman.eats.demo.gui.borders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class ColoredBorderDrawer implements IBorderDrawer {
	private final Color mBorderColor;
	private final int mBorderSize;
	public ColoredBorderDrawer(Color pBorderColor, int pBorderSize) {
		mBorderColor = pBorderColor;
		mBorderSize = pBorderSize;
	}
	
	@Override
	public void drawBorder(Graphics g, Dimension preferredSize) {
		Color prevColor = g.getColor();
		g.setColor(mBorderColor);
		
		// Top
		g.fillRect(0, 0, preferredSize.width, mBorderSize);
		
		// Bottom
		g.fillRect(0, preferredSize.height - mBorderSize,
				preferredSize.width, mBorderSize);
		
		// Left
		g.fillRect(0, 0, mBorderSize, preferredSize.height);
		
		// Right
		g.fillRect(preferredSize.width - mBorderSize, 0, mBorderSize, preferredSize.height);
		
		g.setColor(prevColor);
	}
}
