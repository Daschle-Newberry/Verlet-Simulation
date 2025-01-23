package SimpleSimulation.engine;

import SimpleSimulation.engine.Camera;
import SimpleSimulation.engine.postprocessing.ScreenBuffer;
import SimpleSimulation.engine.simulation.BallSimulation;
import SimpleSimulation.renderer.Shader;
import SimpleSimulation.renderer.Shaders;
import org.joml.Matrix4f;
import org.joml.Matrix4fKt;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class GameScene extends Scene{

    Camera camera;

    private InstancedModel circle;
    private int particleCount;

    private BallSimulation simulation;

    private ScreenBuffer screenBuffer;

    @Override
    public void init() {
        Shaders.loadShaders();
        particleCount = 100;
        circle = new InstancedModel("/assets/models/circle.obj",particleCount,.05f);
        simulation =  new BallSimulation(particleCount,1,1,1);


        camera = new Camera(new Vector3f(0.0f,0.0f,-1.0f),0);


        screenBuffer = new ScreenBuffer();




    }

    @Override
    public void framebufferSizeCallback(long window, int width, int height) {
        screenBuffer.framebufferSizeCallback(window,width,height);
    }

    @Override
    public void update(double dt) {
        float[] positions = simulation.update(dt,3);
        circle.setBufferData(positions);
        render();
    }

    @Override
    public void render() {
        screenBuffer.bind();
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glClearColor(0f,0f,0f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);

        Shaders.mainShader.use();


        Shaders.mainShader.uploadMat4f("viewMatrix",camera.getViewMatrix());
        Shaders.mainShader.uploadMat4f("projectionMatrix",camera.getProjectionMatrix());

        circle.render();


        Shaders.mainShader.detach();
        glBindVertexArray(0);
        //Render to screen
        screenBuffer.render();


    }
}
