#version 420 core
layout(location=0) in vec3 aPos;
layout(location=1) in mat4 modelMatrix;
layout(location=5) in vec3 color;


uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec3 fragColor;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(aPos,1.0);
    fragColor = color;
}
