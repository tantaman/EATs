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

import java.awt.Dimension;
import java.awt.Image;

public class ImageScaler {
	public static Image scaleImage(Image pSourceImage,
			Dimension pDestDimensions, boolean pPreserveDestWidth,
			boolean pPreserveDestHeight) {
		int sourceWidth = pSourceImage.getWidth(null);
		int sourceHeight = pSourceImage.getHeight(null);
		int destWidth = pDestDimensions.width;
		int destHeight = pDestDimensions.height;
		
		float ratio = 0.0f;
		if (pPreserveDestHeight) {
			ratio = (float)sourceWidth / (float)sourceHeight;
			destWidth = Math.round(destHeight * ratio);
		} else if (pPreserveDestWidth) {
			ratio = (float)sourceHeight / (float)sourceWidth;
			destHeight = Math.round(destWidth * ratio);
		}
		
		return pSourceImage.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
	}
}
