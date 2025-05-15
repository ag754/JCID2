#version 450 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 color;

out vec4 vs_color;

void main(void)
{
	gl_Position = vec4(pos, 1.0);
	vs_color = vec4(color, 1.0);
}