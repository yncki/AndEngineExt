package org.andengine.extensions.axl;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;

public class TDottedPath extends SpriteBatch {

	public final static int CFG_DISTANCE_BETWEEN 		= 18;
	
	private 			TItem3d[] 	mActivePoints		= new TItem3d[300];
	private 			int 	  	mIndexToDraw 		= 0;
	private				PathMeasure mPathMeasure 					= new PathMeasure();
	private				Path 		mPath 				= new Path();
	private 			Matrix 		mMatrix 					= new Matrix();
	private				ITextureRegion	mTextureRegion;

	private float mLastX = 0;

	private float mLastY = 0;
	
	public TDottedPath(float pX, float pY, ITexture pTexture,ITextureRegion pTextureRegion, int pCapacity,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager);
		mTextureRegion = pTextureRegion;
		mActivePoints		= new TItem3d[pCapacity];
		for (int i = 0;i<pCapacity;i++){
			mActivePoints[i] = new TItem3d(0, 0);
		}
	}
	
	public void insertInput(float pX,float pY){
		final float pDist = MathUtils.distance(pX, pY, mLastX, mLastY);
		if (pDist>=CFG_DISTANCE_BETWEEN){
			mPath.lineTo(pX, pY);
			//dodajemy punkty 
			mPathMeasure.setPath(mPath, false);
			final float LENGTH = mPathMeasure.getLength();
			float[] v = new float[9];
			mIndexToDraw = 0;
				for (int t = 0;t<LENGTH;t+=CFG_DISTANCE_BETWEEN){  //odleglosc miedzy punktami 
					if (mIndexToDraw<getCapacity()-1){
						mPathMeasure.getMatrix(t, mMatrix , 0x01 | 0x02);
						mMatrix.getValues(v);
						mIndexToDraw++;
						mActivePoints[mIndexToDraw].reset(v[2], v[5]);
					}
				}
			this.submit();
			mLastX = pX;
			mLastY = pY;
		}
		
	}
	
	public int getCapacity(){
		return this.mCapacity;
	}
	
	public float getDotRotation(final int pIndex) {
		return 0;
	}
	
	public void startInput(final float pX,final float pY){
		mLastX = 0;
		mLastY = 0;
		mPath.reset();
		mPath.moveTo(pX, pY);
		this.submit();
	}
	
	public void endInput(final float pX,final float pY,boolean pClear){
		mLastX = 0;
		mLastY = 0;
		mPath.reset();
		if (pClear) {
			mIndexToDraw = 0;
			super.submit();
		}
	}
	
	
	@Override
	public void submit(){
		int pSize = mIndexToDraw;
		int pDrawn = 0;
		if (mIndexToDraw>this.getCapacity()) pSize = this.getCapacity();
		for (int i = 0;i<pSize;i++){
			final TItem3d P = mActivePoints[i];
			pDrawn++;
			this.draw(mTextureRegion,P.mX-getDotWidth(i)*0.5f , P.mY-getDotHeight(i)*0.5f, getDotWidth(i), getDotHeight(i), getDotRotation(i), getDotColor(0, i), getDotColor(1, i), getDotColor(2, i), getDotColor(3, i));
		}
		if (pDrawn>0) super.submit();
	}
	
	public float getDotColor(int pRGBAIndex,int pIndex){
		return 1f;
	}
	
	public float getDotWidth(int pIndex){
		if (pIndex%3==0) return 10;
		return 5;
	}
	
	public float getDotHeight(int pIndex){
		if (pIndex%3==0) return 10;
		return 5;
	}
	
	class TItem3d {
		float mX,mY;
		float mRotation;
		
		public TItem3d(float pX, float pY) {
			this.reset(pX, pY);
		}
		
		public void reset(
				float pX, float pY) {
			this.mX = pX;
			this.mY = pY;
		}
	}
}

