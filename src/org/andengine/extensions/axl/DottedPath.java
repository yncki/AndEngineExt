package org.andengine.extensions.axl;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;


public class DottedPath extends SpriteBatch {

	class TPoint {
		float mX, mY;

		public TPoint(float pX, float pY) {
			this.reset(pX, pY);
		}

		public void reset(float pX, float pY) {
			this.mX = pX;
			this.mY = pY;
		}
	}
	
	private TPoint[] mOutputPoints;
	private int mLimitIndexToDraw = 0;
	private PathMeasure mPathMeasure = new PathMeasure();
	private Path mPath = new Path();
	private Matrix mMatrix = new Matrix();

	private final ITextureRegion mTextureRegion;

	protected final int mConfigStepBy;
	protected float mConfigTouchEventDelay = 0.05f;
	
	private float mLastX = 0;

	private float mLastY = 0;
	
	private float mSecondsElapsed,mLastSecondsElapsed = 0f;

	protected final float mDotWidth;
	protected final float mDotHeight;
	
	//TODO , change pConfigTouchEventDelay to ratio per second 
	public DottedPath(float pX, float pY, ITexture pTexture,
			ITextureRegion pTextureRegion, int pCapacity,
			VertexBufferObjectManager pVertexBufferObjectManager,
			final int pConfigStepBy,final float pConfigTouchEventDelay,float pDotWidth,float pDotHeight) {
		super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager);
		mConfigStepBy = pConfigStepBy;
		mConfigTouchEventDelay = pConfigTouchEventDelay;
		mDotWidth = pDotWidth;
		mDotHeight = pDotHeight;
		mTextureRegion = pTextureRegion;
		mOutputPoints = new TPoint[pCapacity];
		for (int i = 0; i < pCapacity; i++) {
			mOutputPoints[i] = new TPoint(0, 0);
		}
	}

	public int getCapacity() {
		return this.mCapacity;
	}

	public float getDotColor(int pRGBAIndex, int pIndex) {
		return 1f;
	}

	protected float getDotHeight(int pIndex) {
		return mDotHeight;
	}

	public float getDotWidth(int pIndex) {
//		if (pIndex % 3 == 0)
//			return 16;
		return mDotWidth;
	}

	public float getDotRotation(final int pIndex) {
		return 0;
	}
	
	public void insertInput(TouchEvent pTouchEvent,final Engine pEngine){
		if (mSecondsElapsed>mConfigTouchEventDelay){
			this.insertInput(pTouchEvent.getX(), pTouchEvent.getY());
			mSecondsElapsed = 0;
		} else {
			mSecondsElapsed+=(pEngine.getSecondsElapsedTotal()-mLastSecondsElapsed);
			mLastSecondsElapsed = pEngine.getSecondsElapsedTotal();
		}
	}
	
	public void insertInput(float pX, float pY) {
		final float pDist = MathUtils.distance(pX, pY, mLastX, mLastY);
		if (pDist >= mConfigStepBy) {
			mPath.lineTo(pX, pY);
			mPathMeasure.setPath(mPath, false);
			final float LENGTH = mPathMeasure.getLength();
			float[] v = new float[9];
			mLimitIndexToDraw = 0;
			for (int t = 0; t < LENGTH; t += mConfigStepBy) { //distance between each point
				if (mLimitIndexToDraw < getCapacity() - 1) {
					mPathMeasure.getMatrix(t, mMatrix, 0x01 | 0x02);
					mMatrix.getValues(v);
					mOutputPoints[mLimitIndexToDraw].reset(v[2], v[5]);
					mLimitIndexToDraw++;
					this.onPushOutputPoint(mOutputPoints[mLimitIndexToDraw],mOutputPoints[mLimitIndexToDraw-1]);
				}
			}
			this.submit();
			mLastX = pX;
			mLastY = pY;
		}
	}

	protected void onPushOutputPoint(TPoint pNewPoint,TPoint pLastPoint) {
		
	}

	public void startInput(final TouchEvent pSceneTouchEvent){
		this.startInput(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
	}
	
	public void endInput(final TouchEvent pSceneTouchEvent, boolean pClear) {
		this.endInput(pSceneTouchEvent.getX(),pSceneTouchEvent.getY(), pClear);
	}
	
	
	public void startInput(final float pX, final float pY) {
		mLimitIndexToDraw = 0;
		mLastX = 0;
		mLastY = 0;
		mPath.reset();
		mPath.moveTo(pX, pY);
		this.mSecondsElapsed = 0f;
		this.mLastSecondsElapsed = 0f;
		this.submit();
	}
	public void endInput(boolean pClear) {
		mLastX = 0;
		mLastY = 0;
		mPath.reset();
		if (pClear) {
			super.submit();
			mLimitIndexToDraw = 0;
		}
	}
	public void endInput(final float pX, final float pY, boolean pClear) {
		if (!pClear) this.insertInput(pX, pY);
		mLastX = 0;
		mLastY = 0;
		mPath.reset();
		if (pClear) {
			super.submit();
			mLimitIndexToDraw = 0;
		}
	}
	
	@Override
	public void submit() {
		int pSize = mLimitIndexToDraw;
		int pDrawn = 0;
		if (mLimitIndexToDraw > this.getCapacity())	pSize = this.getCapacity();
		for (int i = 0; i < pSize; i++) {
			final TPoint P = mOutputPoints[i];
			pDrawn++;
			this.draw(mTextureRegion, P.mX - getDotWidth(i) * 0.5f, P.mY
					- getDotHeight(i) * 0.5f, getDotWidth(i), getDotHeight(i),
					getDotRotation(i), getDotColor(0, i), getDotColor(1, i),
					getDotColor(2, i), getDotColor(3, i));
		}
		if (pDrawn > 0)
			super.submit();
	}
}
