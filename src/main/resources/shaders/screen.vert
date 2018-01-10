#version 330 core

// Input vertex data, different for all executions of this shader.
in vec3 position;

// Output data ; will be interpolated for each fragment.
out vec2 uv;

void main(){
	gl_Position =  vec4(position, 1.0);
	uv = (position.xy + vec2(1.0, 1.0)) / 2.0;
}