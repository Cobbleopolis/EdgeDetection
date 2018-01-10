#version 330 core
#define M_PI 3.1415926535897932384626433832795

in vec2 uv;

out vec4 color;

uniform sampler2D fbo_texture;
uniform bool disable_color;
uniform vec2 resolution;

const float sensitivity = 0.5;

void make_detection_matrix(inout vec4 n[9], sampler2D tex, vec2 coord) {
	float w = sensitivity / resolution.x;
	float h = sensitivity / resolution.y;

	n[0] = texture2D(tex, coord + vec2( -w, -h));
	n[1] = texture2D(tex, coord + vec2(0.0, -h));
	n[2] = texture2D(tex, coord + vec2(  w, -h));
	n[3] = texture2D(tex, coord + vec2( -w, 0.0));
	n[4] = texture2D(tex, coord);
	n[5] = texture2D(tex, coord + vec2(  w, 0.0));
	n[6] = texture2D(tex, coord + vec2( -w, h));
	n[7] = texture2D(tex, coord + vec2(0.0, h));
	n[8] = texture2D(tex, coord + vec2(  w, h));
}

void main() {
    vec4 n[9];
	make_detection_matrix(n, fbo_texture, uv);

	vec4 sobel_edge_h = n[2] + (2.0 * n[5]) + n[8] - (n[0] + (2.0 * n[3]) + n[6]);
  	vec4 sobel_edge_v = n[0] + (2.0 * n[1]) + n[2] - (n[6] + (2.0 * n[7]) + n[8]);
	vec4 sobel = sqrt((sobel_edge_h * sobel_edge_h) + (sobel_edge_v * sobel_edge_v));
    float avg_sobel = sobel.r + sobel.g + sobel.b / 3.0;

    if (disable_color) {
        color = vec4(vec3(avg_sobel), 1.0);
    } else {
        vec4 diffuse = texture2D(fbo_texture, uv);

        vec3 line_color = vec3(0.0);

        color = diffuse * (1.0 - avg_sobel) + vec4(line_color, 1.0) * avg_sobel;
    }
}