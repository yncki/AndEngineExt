package org.andengine.extensions.axl;

import java.util.ArrayList;

import org.andengine.util.math.MathUtils;

import android.graphics.PointF;
import android.util.FloatMath;

public class MyGameMath {
	
	
	private static final float ATAN2_CF1 = (float) (3.1415927f / 4f); 
    private static final float ATAN2_CF2 = 3f * ATAN2_CF1; 
    
	public static float[] position_on_circle(final float centerX,final float centerY,float pDegree,float pRadius){
//		if (mPositionsOnCircle.contains(object))
		double radian = (pDegree / 180) * Math.PI;
		float x = ((float) (centerX + Math.cos(radian) * pRadius));//-this.getWidth()/2;
		float y = ((float) (centerY - Math.sin(radian) * pRadius));//-this.getHeight()/2; 
//		float angle = (float) Math.atan2(y-centerY, x-centerX);
//		angle = MathUtils.radToDeg(angle);
//		float[] res = {x,y};  
		return new float[]{x,y};
	}
    
    /** 
     * This is 50% faster than Math.atan2 in the emulator, but can have 
an error of up to 4 degrees. 
     */ 
    public static float fastatan2(float y, float x) { 
            float abs_y = Math.abs(y); 
            float angle; 
            if (x >= 0) { 
                    float r = (x - abs_y) / (x + abs_y); 
                    angle = ATAN2_CF1 - ATAN2_CF1 * r; 
            } else { 
                    float r = (x + abs_y) / (abs_y - x); 
                    angle = ATAN2_CF2 - ATAN2_CF1 * r; 
            } 
            return y < 0 ? -angle : angle; 
    } 
	
//	public static void mRotateByPath(final Sprite pSprite,final Path cPath,int pWaypointIndex,int pAngleOffset){
//		
//		if (pWaypointIndex!=0) { 
//			final float aX = cPath.getCoordinatesX()[pWaypointIndex-1]+pSprite.getWidth()/2; 
//			final float aY = cPath.getCoordinatesY()[pWaypointIndex-1]+pSprite.getHeight(); 
//			final float bX = cPath.getCoordinatesX()[pWaypointIndex]+pSprite.getWidth()/2; 
//			final float bY = cPath.getCoordinatesY()[pWaypointIndex]+pSprite.getHeight(); 
//			pSprite.setRotation(angleAxisToVectorDegrees(-aX, aY, -bX, bY)+pAngleOffset);
//		}
//	}

//	public static ArrayList<PointF> GetBezierCurve(float pX1,float pY1,float pX2,float pY2,float pBX,float pBY,float pSkip){
//		ArrayList<PointF> pOutPut = new ArrayList<PointF>();
//		for(float t=0.0f;t<=1;t+=pSkip){  
//		    float x = (float) (  (1-t)*(1-t)*pX1 + 2*(1-t)*t*pBX+t*t*pX2);  
//		    float y = (float) (  (1-t)*(1-t)*pY1 + 2*(1-t)*t*pBY+t*t*pY2);  
//		    pOutPut.add(new PointF(x,y));
//		}  
//		//ostatecznie dodajmy jeszcze ostatni punkt poniewaz przy malej precyzji moze go nie byc
//		pOutPut.add(new PointF(pX2, pY2));
//		return pOutPut;
//	}
    
    public static ArrayList<float[]> GetBezierCurve(final float pX1,final float pY1,final float pX2,final float pY2,final float pX3,final float pY3,final float pX4,final float pY4,final float pSkip) {
    	ArrayList<float[]> pOutPut = new ArrayList<float[]>();
    	for (float t = 0.0f; t <= 1; t += pSkip) {
    		pOutPut.add(bezierFunction4(t, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4));
    	}
    	//ostatecznie dodajmy jeszcze ostatni punkt poniewaz przy malej precyzji moze go nie byc
    	final float[] res = {pX4,pY4};
    	pOutPut.add(res);
    	return pOutPut;
    }
	
	public static ArrayList<float[]> GetBezierCurve3(final float pX1,final float pY1,final float pX2,final float pY2,final float pX3,final float pY3,final float pSkip) {
		ArrayList<float[]> pOutPut = new ArrayList<float[]>();
		for (float t = 0.0f; t <= 1; t += pSkip) {
			pOutPut.add(bezierFunction3(t, pX1, pY1, pX2, pY2, pX3, pY3));
		}
		//ostatecznie dodajmy jeszcze ostatni punkt poniewaz przy malej precyzji moze go nie byc
		pOutPut.add(new float[]{pX3,pY3});
		return pOutPut;
	}
	
//	public static ArrayList<float[]> GetBezierCurve(final float pX1,final float pY1,final float pX2,final float pY2,final float pBX,final float pBY,final float pDX,final float pDY,final float pSkip) {
//		ArrayList<float[]> pOutPut = new ArrayList<float[]>();
//		for (float t = 0.0f; t <= 1; t += pSkip) {
//			pOutPut.add(bezierFunction4(t, new PointF(pX1, pY1), new PointF(
//					pX2, pY2), new PointF(pBX, pBY), new PointF(pDX, pDY)));
//		}
//		//ostatecznie dodajmy jeszcze ostatni punkt poniewaz przy malej precyzji moze go nie byc
//		pOutPut.add(new PointF(pDX, pDY));
//		return pOutPut;
//	}


//	public static  PointF bezierFunction4(float t, final PointF p1, final PointF p2, final PointF p3,
//			final PointF p4) {
//		final float x = (float) (p1.x * (Math.pow(-t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
//				+ 3 * p2.x * t * (Math.pow(t, 2) - 2 * t + 1) + 3 * p3.x
//				* Math.pow(t, 2) * (1 - t) + p4.x * Math.pow(t, 3));
//		final float y = (float) (p1.y * (Math.pow(-t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
//				+ 3 * p2.y * t * (Math.pow(t, 2) - 2 * t + 1) + 3 * p3.y
//				* Math.pow(t, 2) * (1 - t) + p4.y * Math.pow(t, 3));
//		return new PointF(x, y);
//	}
	
	
	public static float[] bezierFunction3(float percentageDone,final float pX1,final float pY1,final float pX2,final float pY2,final float pX3,final float pY3) {
		final float u = 1 - percentageDone;
		final float tt = percentageDone*percentageDone;
		final float uu = u*u;
		final float ut2 = 2 * u * percentageDone;
		/* Formula:
		 * ((1-t)^2 * p1) + (2*(t)*(1-t) * p2) + ((t^2) * p3) */
		final float x = (uu * pX1) + (ut2 * pX2) + (tt * pX3);
		final float y = (uu * pY1) + (ut2 * pY2) + (tt * pY3);
		return new float[]{x,y};//res;
	}
	
	
	public static float[] bezierFunction4(float percentageDone,final float pX1,final float pY1,final float pX2,final float pY2,final float pX3,final float pY3,final float pX4,final float pY4) {
		final float u = 1 - percentageDone;
		final float tt = percentageDone * percentageDone;
		final float uu = u * u;
		final float uuu = uu * u;
		final float ttt = tt * percentageDone;
		
		final float ut3 = 3 * uu * percentageDone;
		final float utt3 = 3 * u * tt;
		
		/*
		 * Formula: ((1-t)^3 * p1) + (3*(t)*(1-t)^2 * p2) + (3*(t^2)*(1-t) * p3) + (t^3 * p4)
		 */
		final float x = (((uuu * pX1) + (ut3 * pX2) + (utt3 * pX3) + (ttt * pX4)));//  -((RectangularShape)pEntity).mWidth/2;
		final float y = (((uuu * pY1) + (ut3 * pY2) + (utt3 * pY3) + (ttt * pY4)));//  -((RectangularShape)pEntity).mHeight/2;
		
		return new float[]{x,y};//res;
		
		
		
		
		
//		
//		final float x = (float) (pX1 * (Math.pow(-t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
//				+ 3 * pX2 * t * (Math.pow(t, 2) - 2 * t + 1) + 3 * pX3
//				* Math.pow(t, 2) * (1 - t) + pX4 * Math.pow(t, 3));
//		final float y = (float) (pY1 * (Math.pow(-t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
//				+ 3 * pY2 * t * (Math.pow(t, 2) - 2 * t + 1) + 3 * pY3
//				* Math.pow(t, 2) * (1 - t) + pY4 * Math.pow(t, 3));
//
//		
//		return new float[]{x,y};//res;
	}
	
	public static float distancePointF(PointF p, PointF q)
	{ float dx   = p.x - q.x;         //horizontal difference 
	  float dy   = p.y - q.y;         //vertical difference 
	  float dist = FloatMath.sqrt( dx*dx + dy*dy ); //distance using Pythagoras theorem
	  return dist;
	}
	
	public static float distance(final float pX,final float pY,final float qX,final float qY){
		float dx   = pX - qX;         //horizontal difference 
		  float dy   = pY - qY;        //vertical difference 
		  float dist = FloatMath.sqrt( dx*dx + dy*dy ); //distance using Pythagoras theorem
		  return dist;
	}
	
	 /**
	+        * Return the angle in radians from the fixed X axis and the vector x1,y1 x2,y2
	+        * The angle is normalized between -PI and PI. 0 is parallel to the fixed Y axis.
	+        * Positive angles from 0 to PI/2 are in the top right quadrant
	+        * Positive angles from PI/2 to PI are in the bottom right quadrant
	+        * Negative angles from 0 to -PI/2 are in the top left quadrant
	+        * Negative angles from -PI/2 to -PI are in the bottom left quadrant
	+        *
	+        * @param pX1 The X coordinate of the first point
	+        * @param pY1 The Y coordinate of the first point
	+        * @param pX2 The X coordinate of the second point
	+        * @param pY2 The Y coordinate of the second point
	+        * @return Angle between the X axis and the vector specified by pX1,pY1 pX2,pY2
	+        */
			public static float angleAxisToVector(final float pX1, final float pY1, final float pX2, final float pY2){
				return (float)MathUtils.atan2(pX2 - pX1, pY2 - pY1);
	        }
	
	      /**
	+        * Return the angle in degrees from the fixed X axis and the vector x1,y1 x2,y2
	+        * The angle is normalized between 0 and 360. 360.00 is not included (360.00 == 0 degrees)
	+        *
	+        * @param pX1 The X coordinate of the first point
	+        * @param pY1 The Y coordinate of the first point
	+        * @param pX2 The X coordinate of the second point
	+        * @param pY2 The Y coordinate of the second point
	+        * @return Angle between the X axis and the vector specified by pX1,pY1 pX2,pY2
	+        */
	       public static float angleAxisToVectorDegreesNormalized(final float pX1, final float pY1, final float pX2, final float pY2) {
	               final float angleRad = MyGameMath.angleAxisToVector(pX1, pY1, pX2, pY2);
//	               if (pNormalize) {
	            	   return normalizeAngleDegrees(MathUtils.radToDeg(angleRad));
//	               } else {
//	            	   return MathUtils.radToDeg(angleRad);
//	               }
	               
	       }
	       
	       public static float angleAxisToVectorDegrees(final float pX1, final float pY1, final float pX2, final float pY2) {
               final float angleRad = (float)MathUtils.atan2(pX2 - pX1, pY2 - pY1);
               return MathUtils.radToDeg(angleRad);
	       }
	       
	/*+        * Normalize an angle in degrees between 0 and 360
	+        * The value 360.00 itself is not included in the output range.
	+        *
	+        * @param pDeg Angle to normalize
	+        * @return Normalized angle between 0 and 360
	+        */
	       public static float normalizeAngleDegrees(final float pDeg)
	       {
	               final float pMod = pDeg % 360;
	               if(pMod >= 0)
	                       return pMod;
	               return pMod + 360;
	       }
}