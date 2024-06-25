attribute vec4 a_position; // Атрибут позиции вершины
attribute vec4 a_color;    // Атрибут цвета вершины
attribute vec2 a_texCoord0; // Атрибут координат текстуры

uniform mat4 u_projTrans;

varying vec4 v_color;      // varying переменная для передачи цвета во фрагментный шейдер
varying vec2 v_texCoords;  // varying переменная для передачи координат текстуры во фрагментный шейдер

void main() {
    v_color = a_color;        // Передаем цвет из атрибута в varying переменную
    v_texCoords = a_texCoord0; // Передаем координаты текстуры из атрибута в varying переменную
    gl_Position = u_projTrans * a_position;  // Устанавливаем позицию вершины
}
