package SimpleSimulation.engine;

import SimpleSimulation.renderer.Shaders;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstancedModel extends Model{
    private int instances;
    private int vertexCount;
    private int VAO;
    private int instanceVBO;

    private int floatBytes = 4;
    private int vec4Size = 16;

    private Matrix4f scale;
    private FloatBuffer instanceBuffer;
    private float[] tmpBuffer;


    public InstancedModel(String filepath,int amount,float scale){

        this.scale = new Matrix4f().scale(scale);
        instances = amount;
        float[] vertexArray = loadModelFile(filepath);

        vertexCount = (int)vertexArray.length/3;
        //VAO
        VAO  = glGenVertexArrays();
        glBindVertexArray(VAO);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //VBO
        int VBO = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER,VBO);

        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        // XYZ
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,3,GL_FLOAT,false,3 * floatBytes,0);



        glBindBuffer(GL_ARRAY_BUFFER,0);

        //Instances
        instanceVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,instanceVBO);
        instanceBuffer = BufferUtils.createFloatBuffer(19*instances);
        glBufferData(GL_ARRAY_BUFFER,instanceBuffer,GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1,4,GL_FLOAT,false,4 * vec4Size + 3 * floatBytes,0);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2,4,GL_FLOAT,false,4 * vec4Size + 3 * floatBytes, vec4Size);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3,4,GL_FLOAT,false,4 * vec4Size + 3 * floatBytes,2 * vec4Size);
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(4,4,GL_FLOAT,false,4 * vec4Size + 3 * floatBytes,3 * vec4Size);

        glEnableVertexAttribArray(5);
        glVertexAttribPointer(5,3,GL_FLOAT,false,4 * vec4Size + 3 * floatBytes,4 * vec4Size);

        glVertexAttribDivisor(1,1);
        glVertexAttribDivisor(2,1);
        glVertexAttribDivisor(3,1);
        glVertexAttribDivisor(4,1);
        glVertexAttribDivisor(5,1);

        glBindBuffer(GL_ARRAY_BUFFER,0);

        tmpBuffer = new float[19];

    }

    public void setBufferData(float[] positions){
        int index = 0;
        for(int i = 0; i < instances; i++){
            Matrix4f modelMatrix = new Matrix4f().translation(positions[index],positions[index+1],0);
            modelMatrix.mul(this.scale,modelMatrix);

            modelMatrix.get(tmpBuffer);
            tmpBuffer[16] = positions[index+2];
            tmpBuffer[17] = positions[index+3];
            tmpBuffer[18] = positions[index+4];


            instanceBuffer.put(tmpBuffer);


            index += 5;
        }

        glBindBuffer(GL_ARRAY_BUFFER,instanceVBO);
        glBufferData(GL_ARRAY_BUFFER,instanceBuffer.flip(),GL_DYNAMIC_DRAW);
    }



    public void setScale(Vector3f scale){
        this.scale = new Matrix4f().scale(scale);
    }

    @Override
    public void render() {
        glBindVertexArray(VAO);
        glDrawArraysInstanced(GL_TRIANGLES,0,vertexCount,instances);
    }
}
