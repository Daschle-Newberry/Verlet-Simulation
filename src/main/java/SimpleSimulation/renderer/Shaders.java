package SimpleSimulation.renderer;

public class Shaders {
    public static Shader mainShader;
    public static Shader screenShader;
    public static Shader gridShader;


    public static void loadShaders(){

        mainShader = new Shader("/assets/shaders/default_vert.glsl","/assets/shaders/default_frag.glsl");
        mainShader.compile();

        screenShader =  new Shader("/assets/shaders/post_processing_vert.glsl","/assets/shaders/post_processing_frag.glsl");
        screenShader.compile();

        gridShader =  new Shader("/assets/shaders/grid_vert.glsl","/assets/shaders/grid_frag.glsl");
        gridShader.compile();


    }
}
