package SimpleSimulation.engine;

import SimpleSimulation.engine.Camera;
import SimpleSimulation.engine.postprocessing.ScreenBuffer;
import SimpleSimulation.engine.simulation.BallSimulation;
import SimpleSimulation.engine.simulation.BoundingBox;
import SimpleSimulation.renderer.Shader;
import SimpleSimulation.renderer.Shaders;
import SimpleSimulation.util.Data;
import SimpleSimulation.util.Time;
import org.joml.Matrix4f;
import org.joml.Matrix4fKt;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class GameScene extends Scene{

    private Camera camera;

    private InstancedModel circle;

    private BallSimulation simulation;
    private ScreenBuffer screenBuffer;


    @Override
    public void init() {
        Shaders.loadShaders();
        int particleCount = 10000;
        float particleRadius = .002f;
        circle = new InstancedModel("/assets/models/circle.obj",particleCount,particleRadius);
        simulation =  new BallSimulation(particleCount,particleRadius,1f);

        camera = new Camera(new Vector3f(0.0f,0.0f,-1.0f),0);
        screenBuffer = new ScreenBuffer();
    }

    @Override
    public void framebufferSizeCallback(long window, int width, int height) {
        screenBuffer.framebufferSizeCallback(window,width,height);
    }

    @Override
    public void update(double dt) {

        double startTime = Time.getTime();
        float[] positions = simulation.update(dt);
        double endTime = Time.getTime();
        Data.simulationUpdateTime += endTime - startTime;
        Data.simulationUpdates ++;
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

        Shaders.gridShader.use();


        //Render to screen
        screenBuffer.render();


    }
}
