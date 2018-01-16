#version 330 core
#define M_PI 3.1415926535897932384626433832795

#define lineThresh 0.95

in vec4 worldPos;
in vec3 fragNormal;

out vec4 color;

const vec3 baseColor = vec3(0.0f, 0.0f, 1.0f);
const vec3 lineColor = vec3(1.0f, 1.0f, 1.0f);

void main() {
    float xCos = cos(2 * M_PI * worldPos.x);
    float zCos = cos(2 * M_PI * worldPos.z);
    if (xCos >= lineThresh || zCos >= lineThresh)
        color = vec4(lineColor, 1.0);
    else
        color = vec4(baseColor, 1.0);
}