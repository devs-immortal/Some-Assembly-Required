#version 150

uniform sampler2D Sampler0;

in vec2 texCoord0;

void main() {
    if (texture(Sampler0, texCoord0).a < 0.1) {
        discard;
    }

    gl_FragDepth = gl_FragCoord.z + 0.0000019073486328125 / gl_FragCoord.z;
}
