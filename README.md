Introducción: 
Dodge es un juego del género Bullet Hell, el cual se basa en esquivar una cantidad abrumadora de proyectiles, a la vez de disparar correctamente al enemigo.
Personaje principal: Bart.
Enemigo: Homer.

Trama:
¡Los Rigelianos (aliens) transformaron a Homer en un cyborg maligno con el objetivo de apoderarse del planeta! Bart pidió ayuda al profesor Frink, y el profesor convirtió a Bart en otro cyborg.

Controles PC:
Flechas - Movimiento
Z - Disparar Duff
Shift - Focus (mitad de velocidad y hurtbox visible)

G - Activar/Desactivar modo debug. Con modo debug puedes hacer todo lo siguiente:
Q - Activar/Desactivar transformación Blue Bart (velocidad movimiento y proyectiles x1.5)
Q - Activar/Desactivar Godmode (no perder vidas
1 - Spawnear vida
2 - Spawnear capa (Powerup Blue Bart)
3 - Spawnear friend (resta mitad de puntos, tapa bastante pantalla)


Desarrollo:
Tenemos 4 pantallas (clases): GameScreen (gameplay), WinMenuScreen (cuando ganas), DeadMenuScreen (cuando pierdes), MainMenuScreen (menú principal).

MainMenuScreen: Aquí empieza el juego, explicando los controles y preguntándote si quieres usar controles móviles (tocando pantalla) o de ordenador (tecleando espacio). 
El texto aparece gracias a los GlyphLayout. Además, agregué dos métodos para centrar el texto cómodamente.
Para que el juego detecte si quieres usar los controles móviles o no, cuando cambie a GameScreen pasará un boolean indicándolo.

GameScreen: Donde está la mayor parte de lógica, ya que es donde está el gameplay principal. Arriba se declaran todas los atributos que se vayan a usar (algunos no les he dado uso aún, pero porque quiero hacerlo a futuro por comodidad), como la puntuación, texturas, sonidos, música, sprites, spriteBatch, viewport, vectores, arrays, timers, cooldowns, rectangles (colisiones), vida de enemigo/personaje, etc... Son bastantes, así que explicaré lo importante primero.

Debajo, en el constructor, se inicializan todos los atributos mencionados anteriormente. Sí, he inicializado algunos arriba, pero lo cambiaré a futuro para hacer más óptimo y legible el código.

Lógica importante:
vidaBart: La cantidad de vidas del personaje (3). Es importante declarar estos valores arriba, por si en un futuro se decide modificar el valor, por eso añadí también maxVidaBart, aunque de momento no lo he usado.
speed: Velocidad del personaje. Es igual a "300+vidaBart*100", para que contra menos vida tengas más lento seas. 300 es la velocidad base.
delta: MUY IMPORTANTE para que los FPS no afecten a la velocidad de los sprites/animaciones. Este valor se multiplicará por la variable deseada (timers, speed…) 
EstadoBart: Enum que define los distintos estados en los que puede estar Bart: "NORMAL, SHIFT, BLUE, BLUE_SHIFT, HURTED, HURTED_SHIFT, BLUE_HURTED, BLUE_HURTED_SHIFT, DEAD". 
En el programa se define luego la lógica para saber cuándo debe estar en cada estado: 
	- Vida menor o igual a 0? -> "., sino:
		- Es azul? -> Vida igual a 1? -> Shiftea? -> BLUE_HURTED_SHIFT sino -> BLUE_HURTED
		- etc...
Movimiento Bart: En el caso de usar controles de ordenador, la lógica del input es bastante sencilla, es seguir y comprender los pasos del ejemplo de Simple Game, pero agregando coordenadas Y, ya que te puedes mover por todo el mapa. En el caso de controles móviles, es más complejo, ya que en Simple Game el cubo se podía teletransportar al tocarlo, y aquí por gameplay no debe ser así, por lo que tuve que agregar condicionales que detectan la posición en la que estás tocando y la de bart, haciendo que siga tu dedo a la velocidad que corresponde.

Conclusiones: Me ha gustado bastante hacer todo esto, seguramente siga en el desarrollo como hobbie.
Las flags booleanas son bastante útiles, tengo pensado hacer más para hacer más legible y óptimo el código, ya que veo cosas que no están donde me gustaría que estuviesen, ya que he ido aprendiendo cosas nuevas mientras desarrollaba todo.




- Lógica: colisiones, movimiento, etc…
- Estructura del juego: clases utilizadas, pantallas
○ Conclusiones: ej. diferencia aprendida entre la representación lógica y la gráfica etc..
