#version 420 core
layout(location=0) in vec3 aPos;
layout(location=1) in vec3 aColor;
layout(location=2) in mat4 modelMatrix;


uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec3 frag_color;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(aPos,1.0);
}
