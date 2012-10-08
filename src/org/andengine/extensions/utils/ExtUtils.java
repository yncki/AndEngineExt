package org.andengine.extensions.utils;

import java.util.ArrayList;

import org.andengine.util.math.MathUtils;

public class ExtUtils {
	
    
	public static float[] position_on_circle(final float centerX,final float centerY,float pDegree,float pRadius){
		double radian = (pDegree / 180) * Math.PI;
		float x = ((float) (centerX + Math.cos(radian) * pRadius));//-this.getWidth()/2;
		float y = ((float) (centerY - Math.sin(radian) * pRadius));//-this.getHeight()/2; 
		return new float[]{x,y};
	}
    

    
    public static ArrayList<float[]> GetBezierCurve(final float pX1,final float pY1,final float pX2,final float pY2,final float pX3,final float pY3,final float pX4,final float pY4,final float pSkip) {
    	ArrayList<float[]> pOutPut = new ArrayList<float[]>();
    	for (float t = 0.0f; t <= 1; t += pSkip) {
    		pOutPut.add(bezierFunction4(t, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4));
    	}
    	final float[] res = {pX4,pY4};
    	pOutPut.add(res);
    	return pOutPut;
    }
	
	public static ArrayList<float[]> GetBezierCurve3(final float pX1,final float pY1,final float pX2,final float pY2,final float pX3,final float pY3,final float pSkip) {
		ArrayList<float[]> pOutPut = new ArrayList<float[]>();
		for (float t = 0.0f; t <= 1; t += pSkip) {
			pOutPut.add(bezierFunction3(t, pX1, pY1, pX2, pY2, pX3, pY3));
		}
		pOutPut.add(new float[]{pX3,pY3});
		return pOutPut;
	}

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
            	   return normalizeAngleDegrees(MathUtils.radToDeg(ExtUtils.angleAxisToVector(pX1, pY1, pX2, pY2)));
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