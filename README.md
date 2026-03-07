Introducción: 
Dodge es un juego del género Bullet Hell, el cual se basa en esquivar una cantidad abrumadora de proyectiles, a la vez de disparar correctamente al enemigo.
Personaje principal: Bart.
Enemigo: Homer.

Trama:
¡Los Rigelianos (aliens) transformaron a Homer en un cyborg maligno con el objetivo de apoderarse del planeta! Bart pidió ayuda al profesor Frink, y el profesor convirtió a Bart en otro cyborg. 

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
delta: MUY IMPORTANTE para que los FPS no afecten a la velocidad de los sprites/animaciones. 





- Lógica: colisiones, movimiento, etc…
- Estructura del juego: clases utilizadas, pantallas
○ Conclusiones: ej. diferencia aprendida entre la representación lógica y la gráfica etc..
