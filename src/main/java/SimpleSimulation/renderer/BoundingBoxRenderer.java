package SimpleSimulation.renderer;

import SimpleSimulation.engine.simulation.BoundingBox;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BoundingBoxRenderer {
    private static float wallSize = .002f;
    private static int floatBytes = 4;
    private int[] VAOs;
    public BoundingBoxRenderer(ArrayList<BoundingBox> boundingBoxes){

        this.VAOs = new int[boundingBoxes.size()];
        for(int i = 0; i < boundingBoxes.size(); i++){
            BoundingBox box = boundingBoxes.get(i);
            //left wall
            float[] topLeftOuter = {box.getLeft(),box.getTop()};
            float[] topLeftInner = {box.getLeft() + wallSize,box.getTop()};

            float[] topRightOuter = {box.getRight(),box.getTop()};
            float[] topRightInner = {box.getRight() - wallSize,box.getTop()};

            float[] bottomLeftOuter = {box.getLeft(),box.getBottom()};
            float[] bottomLeftInner = {box.getLeft() + wallSize,box.getBottom()};

            float[] bottomRightOuter = {box.getRight(),box.getBottom()};
            float[] bottomRightInner = {box.getRight() - wallSize,box.getBottom()};

            float[] vertexArray = {
                    box.getLeft() - wallSize, box.getBottom(),
                    box.getLeft(), box.getBottom(),
                    box.getLeft(),box.getTop(),

                    box.getLeft() - wallSize, box.getBottom(),
                    box.getLeft() -wallSize, box.getTop(),
                    box.getLeft(), box.getTop(),

                    box.getRight() +  wallSize, box.getBottom(),
                    box.getRight(), box.getBottom(),
                    box.getRight(),box.getTop(),

                    box.getRight() + wallSize, box.getBottom(),
                    box.getRight() + wallSize, box.getTop(),
                    box.getRight(), box.getTop(),


                    box.getLeft() - wallSize,box.getBottom(),
                    box.getLeft() - wallSize,box.getBottom() - wallSize,
                    box.getRight() + wallSize, box.getBottom(),

                    box.getRight() + wallSize, box.getBottom(),
                    box.getLeft() - wallSize,box.getBottom() - wallSize,
                    box.getRight() + wallSize,box.getBottom() - wallSize,

                    box.getLeft() - wallSize,box.getTop(),
                    box.getLeft() - wallSize,box.getTop() + wallSize,
                    box.getRight() + wallSize, box.getTop(),

                    box.getRight() + wallSize, box.getTop(),
                    box.getLeft() - wallSize,box.getTop() + wallSize,
                    box.getRight() + wallSize,box.getTop() + wallSize,

            };



            int VAO = glGenVertexArrays();
            glBindVertexArray(VAO);

            FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
            vertexBuffer.put(vertexArray).flip();

            //VBO
            int VBO = glGenBuffers();

            glBindBuffer(GL_ARRAY_BUFFER,VBO);

            glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

            // XYZ
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0,2,GL_FLOAT,false,2 * floatBytes,0);
            VAOs[i] = VAO;

            glBindVertexArray(0);
        }
    }

    public void render(){
        for(int VAO : VAOs){
            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES,0,24);
            glBindVertexArray(0);
        }
    }
}
