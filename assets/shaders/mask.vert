attribute vec4 a_position;   // Атрибут позиции вершины
attribute vec2 a_texCoord0;  // Атрибут координат текстуры

uniform mat4 u_projTrans;

varying vec2 v_texCoords;    // varying переменная для передачи координат текстуры во фрагментный шейдер

void main() {
    v_texCoords = a_texCoord0;  // Передаем координаты текстуры из атрибута в varying переменную
   gl_Position = u_projTrans * a_position;   // Устанавливаем позицию вершины
}
