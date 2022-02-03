#version 150

uniform sampler2D Sampler0;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    fragColor = texture(Sampler0, texCoord0);
    if (fragColor.a < 0.1) {
        discard;
    }

    gl_FragDepth = gl_FragCoord.z + 0.0000019073486328125 / gl_FragCoord.z;
}
