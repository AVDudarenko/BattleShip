#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;       // Объявляем varying переменные

uniform sampler2D u_texture;    // Объявляем uniform переменные
uniform sampler2D u_maskTexture;

void main() {
    vec4 maskColor = texture2D(u_maskTexture, v_texCoords);
    if (maskColor.a < 0.5) {
        discard; // Отбрасываем фрагмент, если прозрачность маски меньше 0.5
    } else {
        gl_FragColor = texture2D(u_texture, v_texCoords);
    }
}
