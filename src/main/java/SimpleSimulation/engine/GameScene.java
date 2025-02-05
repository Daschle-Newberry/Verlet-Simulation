package SimpleSimulation.engine;

import SimpleSimulation.engine.Camera;
import SimpleSimulation.engine.input.MouseListener;
import SimpleSimulation.engine.postprocessing.ScreenBuffer;
import SimpleSimulation.engine.simulation.BallSimulation;
import SimpleSimulation.engine.simulation.BoundingBox;
import SimpleSimulation.renderer.BoundingBoxRenderer;
import SimpleSimulation.renderer.Shader;
import SimpleSimulation.renderer.Shaders;
import SimpleSimulation.util.Data;
import SimpleSimulation.util.Time;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class GameScene extends Scene{

    private Camera camera;

    private InstancedModel circle;

    private BallSimulation simulation;
    private ScreenBuffer screenBuffer;
    private BoundingBoxRenderer boundingBoxRenderer;


    @Override
    public void init() {
        Shaders.loadShaders();
        int particleCount = 6000;
        float particleRadius = .002f;
        circle = new InstancedModel("/assets/models/circle.obj",particleCount,particleRadius);
        simulation =  new BallSimulation(particleCount,particleRadius,2f);

        boundingBoxRenderer =  new BoundingBoxRenderer(BoundingBox.getBoundingBoxes());

        camera = new Camera();
        screenBuffer = new ScreenBuffer();
    }

    @Override
    public void framebufferSizeCallback(long window, int width, int height) {
        screenBuffer.framebufferSizeCallback(window,width,height);
    }

    @Override
    public void update(double dt) {

        if(MouseListener.mouseButtonDown(0)){
            Vector2f mouseWSP =  convertToWSP(MouseListener.getX(),MouseListener.getY());
            MouseListener.setWorldX(mouseWSP.x);
            MouseListener.setWorldY(mouseWSP.y);
        }
        float[] positions = simulation.update();

        MouseListener.processButtons();

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
        Shaders.gridShader.uploadMat4f("viewMatrix",camera.getViewMatrix());
        Shaders.gridShader.uploadMat4f("projectionMatrix",camera.getProjectionMatrix());
        boundingBoxRenderer.render();

        //Render to screen
        screenBuffer.render();
    }


    private Vector2f convertToWSP(float x, float y){
      float ndcX = (2 * x / Window.get().width) - 1;
      float ndcY = 1 - (2 * y / Window.get().height );
      Matrix4f PV =  new Matrix4f();
      camera.getProjectionMatrix().mul(camera.getViewMatrix(),PV);
      PV.invert();

      Vector4f clipCoords =  new Vector4f(ndcX, ndcY,0.0f,1.0f);

      Vector4f WSP =  new Vector4f();

      clipCoords.mul(PV,WSP);

      return new Vector2f(WSP.x,WSP.y);
    }


}
