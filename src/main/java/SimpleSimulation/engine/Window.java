package SimpleSimulation.engine;

import SimpleSimulation.engine.input.KeyListener;
import SimpleSimulation.engine.input.MouseListener;
import SimpleSimulation.util.Data;
import SimpleSimulation.util.Time;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
    public int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    private static Scene currentScene;

    private static double lowestFrameRate = -1;
    public static double totalTime;
    private static int totalFrames;

    private static double totalRenderTime = -1;
    private static double highestRenderTime;

    private Window() {
    }

    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();

        }
        return Window.window;
    }

    private static void framebufferSizeCallback(long window, int width, int height){
        Window.get().width = width;
        Window.get().height = height;
        currentScene.framebufferSizeCallback(window,width,height);
    }
    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion());
        init();
        loop();

        System.out.println("AVG collision checks per frame " + Data.collisionChecks/Data.simulationUpdates);
        System.out.println("AVG render time " + Data.totalRenderTime / Data.totalRenders);






        //Free Memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init(){
        //Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure Window
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.width = mode.width();
        this.height = mode.height();
        this.title = "Simulation";

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES,4);

        //Create Window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);

        //Callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallBack);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallBack);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);
        glfwSetFramebufferSizeCallback(Window.get().glfwWindow, Window::framebufferSizeCallback);


        if (glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW Window");

        }

        //Make OpenGl context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable V-SYNC
        glfwSwapInterval(0);

        //Show Window
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_MULTISAMPLE);
        glfwSetWindowSize(glfwWindow,this.width,this.height);
        glViewport(0,0,width,height);


        currentScene = new MainScene();
        currentScene.init();
    }

    public void loop() {
        double beginTime = Time.getTime();
        double endTime;
        double dt = -1.0f;

        double frameTime = 1.0f/144.0;
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            currentScene.update(dt);
            
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;



            if(frameTime - dt > 0){
                try {
                    Thread.sleep((long) (1000 * (frameTime - dt)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            glfwSetWindowTitle(glfwWindow,"FPS " + 1/dt);
            beginTime = endTime;
        }
    }
}