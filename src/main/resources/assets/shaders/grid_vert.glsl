#version 440 core
layout(location = 0) in vec2 aPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(aPos,0.0f,1.0f);
}