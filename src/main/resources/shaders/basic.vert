#version 330 core

uniform mat4 perspectiveMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec3 position;
in vec3 normal;
out vec4 worldPos;
out vec3 fragNormal;

void main() {
    vec4 pos = vec4(position, 1.0);
    gl_Position = perspectiveMatrix * viewMatrix * modelMatrix * pos;
    worldPos = modelMatrix * pos;
    mat4 rotMat = mat4(modelMatrix);
    rotMat[3] = vec4(0.0);
    fragNormal = (vec4(normal, 1.0) * rotMat).xyz;
}