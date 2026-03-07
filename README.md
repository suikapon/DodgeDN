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
0 - Activar/Desactivar Godmode (no perder vidas
1 - Spawnear vida
2 - Spawnear capa (Powerup Blue Bart)
3 - Spawnear friend (resta mitad de puntos, tapa bastante pantalla)

Controles Móvil:
Tocar pantalla - Movimiento
Botón 1 - Focus (mitad de velocidad y hurtbox visible)
Botón 2 - Disparar Duff

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

Movimiento Bart: En el caso de usar controles de ordenador, la lógica del input es bastante sencilla, es seguir y comprender los pasos del ejemplo de Simple Game, pero agregando coordenadas Y, ya que te puedes mover por todo el mapa. En el caso de controles móviles, es más complejo, ya que en Simple Game el cubo se podía teletransportar al tocarlo, y aquí por gameplay no debe ser así, por lo que tuve que agregar condicionales que detectan la posición en la que estás tocando y la de bart, haciendo que siga tu dedo a la velocidad que corresponde. También hay una variable shift para saber si estás en modo Focus o no. Se evita que bart se salga de los bordes estableciendo el mínimo y máximo de X e Y

Bart Duffs (disparos): Cada duff es un sprite, con su textura. También existe un array de duffs. Se crea un método createDuff que define su tamaño y posición de inicio, que será las coordenadas de Bart y se añade al Array. En la lógica, se recorre el Array, se define su gravedad, que será positiva en coordenadas Y. Se agregan los multiplicadores (poca vida = más lento, isBlue = más rápido) y se multiplica por delta. Entre disparo hay un cooldown, este se define y gracias a un timer se comprueba si se puede disparar. Duando se dispara, el timer vuelve a 0. En el draw se pinta para que salga en pantalla

Movimiento Homer: Se crea una flag derecha para saber la dirección en la que se moverá. Siempre que su vida sea mayor a 0, se moverá. Empieza a la derecha con X positivo, al tocar el borde, derecha cambia a false, y gracias a un condicional hacemos que si no es derecha se mueva en X negativo hasta tocar el borde. Su velocidad baja contra menos vida tenga

Barra de vida Homer: Está compuesta de dos ShapeRenderer, uno gris (ancho) y otro amarillo (anchoActual) que irá bajando dependiendo de la vida. Se calcula el porcentaje de su vida y el ancho actual, que será el ancho multiplicado por el porcentaje de vida. 

Colisiones: Cada Sprite tiene sus colisiones. Cuando dos colisiones chocan, se puede provocar un evento, como perder vida (bullets y bart), quitar vida (duffs y homer), etc...

Bullets general: Su cooldown será random, pero directamente proporcional a vida de Homer (menos vida = menos cooldown)

Bullets rápidas y lentas: Similares a los disparos de duff, solo que aparecen desde las coordenadas en las que esté Homer y la velocidad es negativa, ya que deben bajar. 

Bullets horizontales: Este es curioso, ya que pueden aparecer tanto en la izquierda como en la derecha, y si creamos un boolean general, éste cambiará en cada bala creada y afectará a todas las horizontales en pantalla, por lo que se bugeará y no se moverán como es debido. Se podría haber solucionado creando una clase SideBullet que tenga un atributo boolean derecha: si empieza en la derecha, speed es negativo. También se podrían haber creado dos sprites: sideBulletDerecha y sideBulletIzquierda, pero esa opción no sería muy buena. En mi caso, decidí crear un nuevo Array boolean que decide si la bala empieza en la derecha o no, esto se decide aleatoriamente. Como el Array de derecha siempre tendrá el mismo tamaño que el Array de sideBullets, no habría problema en usar "i" dentro del bucle for. La bala aparece siempre en un punto random de Y, y se moverá donde corresponda

Bullets diagonales: Similares a las horizontales. Mantienen el Array derecha (ahora llamado spawnDerecha para no interferir) para decidir dónde aparecerán, solo que ahora se mueven en X e Y hacia abajo, al ser diagonales.

Vidas: Si tienes entre 1 y 2 vidas, podrá aparecer aleatoriamente una vida en una posición aleatoria de X y en la máxima de Y, con una velocidad negativa en Y para que baje. El cooldown de spawneo de vida dependerá de la vida que tengas (menos vida = más probabilidad). Lógica similar a una bullet normal (array, sprite, colisión, velocidad...). Si ya hay una vida en pantalla, no podrá aparecer otra. Al detectar colisión Bart, tu vida aumentará en 1, sonido de vida y puntuación + puntuación que tengas*1.1 (siempre positivo)

Capas: Muy similar a vidas, cooldown distinto. Al detectar que Bart entra en colisión, sonido de powerup, estado de Bart a blue

Friend: Como vidas y capas, solo que Friend se debe evitar, ya que resta puntos. Colisión = menos puntos, sonido bDoh

Puntuación: Es un long y un GlyphLayout. Se irá sumando o restando dependiendo de los eventos sucedidos. Con una lógica pensada para su correcto funcionamiento, o al menos la que he visto que funciona bien

Conclusiones: Me ha gustado bastante hacer todo esto, seguramente siga en el desarrollo como hobbie.
Las flags booleanas son bastante útiles, tengo pensado hacer más para hacer más legible y óptimo el código, ya que veo cosas que no están donde me gustaría que estuviesen, ya que he ido aprendiendo cosas nuevas mientras desarrollaba todo.

El juego tiene bastantes líneas de código, eso no sería problema si tuviese distintas clases y métodos. Me he dado cuenta de que a veces es algo enredado buscar alguna línea de código en específico si no tengo todo bien estructurado, sobre todo cuando hay tanto código. Mientras desarrollaba el proyecto, he ido creando distintos métodos y flags para ayudar, pero hay cosas bastante mejorables. Me gustaría solucionarlas con la experiencia que he tenido desarrollando el juego, ahora que entiendo mejor cómo funciona todo.

Planes a futuro: 
- Implementar logros (ganar con x puntos, ganar sin perder vida, ganar sin powerups, etc...)
- Mejorar la estética de los menús
- Agregar más powerups
- Agregar un menú con los sprites de powerups y sus descripciones. Al hacer clic en un sprite, sale su descripción
- Mejorar la música
- Hacer otro personaje jugable con distintas características (más vida pero más lento, por ejemplo)

Datos curiosos: 
- Sprites hechos por mí, exceptuando el botón, dpad (sprite que no usé al final)  y los de Bart y Homer, estos dos últimos son pngs en baja resolución modificados ligeramente
- Música hecha por mí.
