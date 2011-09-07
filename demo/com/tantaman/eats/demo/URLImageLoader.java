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
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import com.tantaman.commons.ref.EReferenceType;
import com.tantaman.eats.aop.pub.annotations.caching.Cache;
import com.tantaman.eats.tools.cache.ECacheType;

public class URLImageLoader {
	
	// FIXME Cache
	@Cache(cacheType=ECacheType.REF, referenceType=EReferenceType.WEAK)
	public Image loadImage(String pUrl) throws IOException {
		URL imageURL = new URL(pUrl);
		
		BufferedImage image = null;
		image = ImageIO.read(imageURL);

		waitForImage(image);
		
		return image;
	}

	public static void waitForImage(Image pImage) {
		ImageWaiter waiter = new ImageWaiter();
		boolean prepared = pImage.getWidth(waiter) != -1;

		if (prepared) return;

		waiter.await();
	}

	private static class ImageWaiter implements ImageObserver {
		private volatile boolean mComplete = false;
		private ReentrantLock mLock = new ReentrantLock();
		private Condition mLoaded = mLock.newCondition();

		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			mLock.lock();
			try {
				switch (infoflags) {
				case ImageObserver.ABORT:
				case ImageObserver.ERROR:
				case ImageObserver.ALLBITS:
				case ImageObserver.FRAMEBITS:
					mComplete = true;
					mLoaded.signalAll();
					return false;
				case ImageObserver.HEIGHT:
				case ImageObserver.PROPERTIES:
				case ImageObserver.SOMEBITS:
				case ImageObserver.WIDTH:
				default:
					return true;	
				}
			}
			finally {
				mLock.unlock();
			}
		}

		public void await() {
			mLock.lock();
			try {
				while (!mComplete) {
					try {
						mLoaded.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} finally {
				mLock.unlock();
			}
		}
	}
}
