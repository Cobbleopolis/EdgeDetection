#version 330 core
#define M_PI 3.1415926535897932384626433832795

in vec4 worldPos;
in vec3 fragNormal;
out vec4 color;

void main() {
    gl_FragData[0] = vec4(0.8, 0.8, 0.8, 1.0f);
    gl_FragData[1] = vec4(fragNormal, 1.0);
}