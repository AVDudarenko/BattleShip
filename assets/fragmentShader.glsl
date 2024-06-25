#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_mainTexture;
uniform sampler2D u_maskTexture;
uniform vec2 u_maskPosition;
uniform vec2 u_maskSize;

varying vec2 v_texCoord0;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord0);
    vec4 mainTexColor = texture2D(u_mainTexture, v_texCoord0);

    // Calculate mask coordinates
    vec2 maskCoord = (v_texCoord0 - u_maskPosition) / u_maskSize;
    vec4 maskColor = texture2D(u_maskTexture, maskCoord);

    // Use the mask to blend textures
    vec4 blendedColor = mix(texColor, mainTexColor, maskColor.a);

    gl_FragColor = blendedColor;
}
