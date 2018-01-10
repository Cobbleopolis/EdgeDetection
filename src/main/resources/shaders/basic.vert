#version 330 core

uniform mat4 perspectiveMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec3 position;
out vec4 worldPos;

void main() {
    vec4 pos = vec4(position, 1.0);
    gl_Position = perspectiveMatrix * viewMatrix * modelMatrix * pos;
    worldPos = modelMatrix * pos;
}