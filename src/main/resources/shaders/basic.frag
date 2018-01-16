#version 330 core
#define M_PI 3.1415926535897932384626433832795

in vec4 worldPos;
in vec3 fragNormal;
out vec4 color;

void main() {
    color = vec4(
        abs(fragNormal),
        0.0f
    );
}