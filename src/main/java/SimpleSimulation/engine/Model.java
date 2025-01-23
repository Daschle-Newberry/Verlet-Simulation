package SimpleSimulation.engine;

import SimpleSimulation.renderer.Shaders;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public abstract class Model {
    private int floatBytes = 4;

    public Model(){

    }

    public abstract void render();


    public static float[] loadModelFile(String filePath) {
        InputStream modelDataStream = Shaders.class.getResourceAsStream(filePath);

        if(modelDataStream == null){
            throw new RuntimeException("Model file not found");
        }
        //Read files
        File tempFile;
     try {
         tempFile = File.createTempFile("model_", ".obj");
         tempFile.deleteOnExit();  // Ensure the temp file is deleted on exit
     }catch(IOException e){
         throw new RuntimeException("Error creating temp file",e);

     }


        try (OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = modelDataStream.read(buffer)) != -1){
                out.write(buffer,0,bytesRead);
            }
        }catch(IOException e){
            throw new RuntimeException("Error writing to temp file",e);
        }


        AIScene scene = Assimp.aiImportFile(tempFile.getAbsolutePath(),Assimp.aiProcess_Triangulate);


        PointerBuffer buffer = scene.mMeshes();

        ArrayList<Float> vertexAttributesList = new ArrayList<Float>();

        for(int i = 0; i < buffer.limit(); i++){
            AIMesh mesh =AIMesh.create(buffer.get(i));
            processMesh(mesh,vertexAttributesList);
        }
        float[] vertexAttributes = new float[vertexAttributesList.size()];

        for(int i = 0; i < vertexAttributesList.size(); i++){
            vertexAttributes[i] = vertexAttributesList.get(i);
        }
        int index = 0;


        return vertexAttributes;



    }


    private static void processMesh(AIMesh mesh, ArrayList<Float> vertexAttributesList){
        AIVector3D.Buffer positionVectors = mesh.mVertices();

        AIFace.Buffer faces = mesh.mFaces();

        for(int i = 0; i < mesh.mFaces().limit(); i++){
            AIFace face = faces.get(i);

            IntBuffer indices = face.mIndices();

            for(int j = 0; j < indices.limit(); j++){
                int vertexIndex = indices.get(j);
                AIVector3D position = positionVectors.get(vertexIndex);

                vertexAttributesList.add(position.x());
                vertexAttributesList.add(position.y());
                vertexAttributesList.add(position.z());

                vertexAttributesList.add(.5f);
                vertexAttributesList.add(.5f);
                vertexAttributesList.add(1.0f);


            }
        }
    }
}

