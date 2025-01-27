package SimpleSimulation.util;

public class VectorMath {

    public static float[] subtractVec2(float[] a, float[] b){
        return new float[]{a[0] - b[0],a[1] - b[1]};
    }

    public static float[] addVec2(float[] a, float[] b){
        return new float[]{a[0] + b[0],a[1] + b[1]};
    }


    public static float[] normalizeVec2(float[] vec){
        float magnitude = (float)Math.sqrt(Math.pow(vec[0],2) + Math.pow(vec[1],2));

        return new float[]{vec[0]/magnitude,vec[1]/magnitude};
    }

    public static float[] scaleVec2(float[] vec, float scale){
        return new float[]{vec[0] * scale,vec[1] * scale};
    }


}
