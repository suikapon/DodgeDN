package io.github.dodgedn.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dodgedn.Dodge;
import io.github.dodgedn.estados.EstadoBart;

public class GameScreen implements Screen {
    final Dodge game;
    boolean controlesMovil;

    Texture bgTexture;
    Texture bartTexture;
    Texture blueBartTexture;
    Texture deadBart;
    Texture bartHurtedTexture;
    Texture bulletTexture;
    Texture sideBulletTexture;
    Texture diagonalBulletTexture;
    Texture duffTexture;
    Texture homerTexture;
    Texture homerSemiSleepingTexture;
    Texture homerSleepingTexture;
    Texture rosquillaTexture;
    Texture slowBulletTexture;
    Texture bartShiftTexture;
    Texture chipTexture;
    Texture brokenChipTexture;
    Texture bartHurtedShiftTexture;
    Texture friendTexture;
    Texture capaTexture;
    Texture vidaTexture;
    Texture blueChipTexture;
    Texture blueBurtHurtTexture;
    Texture brokenBlueChipTexture;

    Texture buttonTexture;

    Sound piu;
    Sound powerup;
    Sound vida;
    Sound bDoh;
    Sound hDoh;
    Music music;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite bartSprite;
    Sprite chipSprite;
    Sprite homerSprite;
    Sprite buttonDuffSprite;
    Sprite buttonShiftSprite;

    Vector2 touchPos;
    Array<Sprite> bulletSprites;
    Array<Sprite> slowBulletSprites;
    Array<Sprite> rosqSprites;
    Array<Sprite> duffSprites;
    Array<Sprite> sideBulletSprites;
    Array<Boolean> sideBulletsDerecha;
    Array<Sprite> diagonalBulletSprites;
    Array<Boolean> diagonalBulletDerecha;
    Array<Sprite> capasSprites;
    Array<Sprite> friendSprites;
    Array<Sprite> vidaSprites;

    float timerBullets;
    float timerSideBullets;
    float timerDiagonalBullets;
    float timerDisparo;
    float timerVidas;
    float timerPowerups;
    float duracionCapa;
    float timerDuracionPowerup;
    float timerDebug;
    float timerMuerte = 0;

    Rectangle bartRectangle;
    Rectangle homerRectangle;
    Rectangle bulletRectangle;
    Rectangle rosqRectangle;
    Rectangle duffRectangle;
    Rectangle slowBulletRectangle;
    Rectangle sideBulletRectangle;
    Rectangle diagonalBulletRectangle;
    Rectangle capaRectangle;
    Rectangle friendRectangle;
    Rectangle vidaRectangle;

    ShapeRenderer shapeRenderer;
    float maxVidaHomer = 100f;
    float vidaHomer = 100f;

    int vidaBart = 3;
    int maxVidaBart = 3;
    boolean derecha = true;
    // cooldowns
    float cooldownPowerups;
    float cooldownDuff;
    float cooldownBullet;
    float cooldownSideBullet;
    float cooldownDiagonalBullet;
    float cooldownVida;
    float maxEsperaVida = 30f;
    float maxEsperaCapa = 25f;
    float alphaShift = 0f;
    float fadeSpeed = 3f;
    EstadoBart estadoBart = EstadoBart.NORMAL;
    boolean bartHit = false;
    boolean primeraDuffDisparada = false;
    EstadoBart lastEstado = EstadoBart.DEAD;
    boolean modoDebug = false;
    boolean changeScreen = false;
    boolean shifting = false;
    // multiplicadores de bart
    float shiftMultiplier = 0.5f; // mitad
    float blueMultiplier = 1.5f; // x1.5


    public GameScreen(final Dodge game, boolean controlesMovil) {
        this.game = game;
        this.controlesMovil = controlesMovil;

        friendTexture = new Texture("friend.png");
        capaTexture = new Texture("capa.png");
        vidaTexture = new Texture("vida.png");
        bgTexture = new Texture("bg2.png");
        bartTexture = new Texture("bartolo.png");
        bartShiftTexture = new Texture("bartoloshift.png");
        bartHurtedShiftTexture = new Texture("bartolohurtshift.png");
        deadBart = new Texture("bartdead.png");
        chipTexture = new Texture("chip.png");
        blueChipTexture = new Texture("bluechip.png");
        brokenBlueChipTexture = new Texture("brokenbluechip.png");
        brokenChipTexture = new Texture("brokenchip.png");
        blueBartTexture = new Texture("bartoloblue.png");
        blueBurtHurtTexture = new Texture("bartolobluehurt.png");
        bartHurtedTexture = new Texture("bartolohurt.png");
        bulletTexture = new Texture("bullet.png");
        slowBulletTexture = new Texture("slowbullet.png");
        sideBulletTexture = new Texture("sideBullet.png");
        diagonalBulletTexture = new Texture("diagonalbullet.png");
        duffTexture = new Texture("duff.png");
        homerTexture = new Texture("homer.png");
        homerSemiSleepingTexture = new Texture("homeralmsleep.png");
        homerSleepingTexture = new Texture("homersleep.png");
        rosquillaTexture = new Texture("rosquilla.png");

        buttonTexture = new Texture("button.png");
        buttonShiftSprite = new Sprite(buttonTexture);
        buttonShiftSprite.setSize(100, 100);
        buttonShiftSprite.setPosition(5, 5);

        buttonDuffSprite = new Sprite(buttonTexture);
        buttonDuffSprite.setSize(100, 100);
        buttonDuffSprite.setPosition(125, 5);

        bDoh = Gdx.audio.newSound(Gdx.files.internal("bart_doh.mp3"));
        hDoh = Gdx.audio.newSound(Gdx.files.internal("homer_doh.mp3"));
        piu = Gdx.audio.newSound(Gdx.files.internal("piu.mp3"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("powerup.mp3"));
        vida = Gdx.audio.newSound(Gdx.files.internal("vida.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        spriteBatch = new SpriteBatch();

        viewport = new FitViewport(600, 800);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        bartSprite = new Sprite(bartTexture);
        bartSprite.setSize(40, 90);
        bartSprite.setPosition(worldWidth / 2 - bartSprite.getWidth(), 0);

        chipSprite = new Sprite(chipTexture); // la hurbox visible
        chipSprite.setSize(bartSprite.getWidth(), bartSprite.getHeight());
        chipSprite.setPosition(bartSprite.getX(), bartSprite.getY());

        homerSprite = new Sprite(homerTexture);
        homerSprite.setSize(60, 110);
        homerSprite.setPosition(worldWidth / 2 - homerSprite.getWidth(), worldHeight - 100 - homerSprite.getHeight());

        touchPos = new Vector2();

        bulletSprites = new Array<>();
        rosqSprites = new Array<>();
        duffSprites = new Array<>();
        slowBulletSprites = new Array<>();
        sideBulletSprites = new Array<>();
        sideBulletsDerecha = new Array<>();
        diagonalBulletSprites = new Array<>();
        diagonalBulletDerecha = new Array<>();
        capasSprites = new Array<>();
        friendSprites = new Array<>();
        vidaSprites = new Array<>();

        bartRectangle = new Rectangle();
        homerRectangle = new Rectangle();
        bulletRectangle = new Rectangle();
        duffRectangle = new Rectangle();
        rosqRectangle = new Rectangle();
        slowBulletRectangle = new Rectangle();
        sideBulletRectangle = new Rectangle();
        diagonalBulletRectangle = new Rectangle();
        capaRectangle = new Rectangle();
        friendRectangle = new Rectangle();
        vidaRectangle = new Rectangle();

        bDoh.setVolume(0, 0.5f);

        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();

        shapeRenderer = new ShapeRenderer();

        cooldownDuff = 0f;
        cooldownBullet = MathUtils.random(0.01f, 2f);
        cooldownSideBullet = MathUtils.random(1f, 2f);
        cooldownDiagonalBullet = MathUtils.random(2f, 4f);
        cooldownVida = -1f;
        cooldownPowerups = MathUtils.random(10f, maxEsperaCapa * vidaBart);
        System.out.println("CAPA EN " + cooldownPowerups + " SEGUNDOS");

        duracionCapa = 15f;
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();

        if (changeScreen) {
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void input() {
        float speed = 300f + (float) vidaBart * 100;
        float delta = Gdx.graphics.getDeltaTime();
        timerDisparo += delta;

        if (!controlesMovil)
            shifting = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        else if (Gdx.input.justTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);

            if (buttonShiftSprite.getBoundingRectangle().contains(touchPos))
                shifting = !shifting;
        }

        // estados de bart (muerto, shifteando, vivo, poca vida...)
        if (vidaBart <= 0) {
            estadoBart = EstadoBart.DEAD;
            speed = 0;
            timerMuerte += delta;
            if (timerMuerte >= 1f)
                changeScreen = true;
        } else {
            if (isBlue()) {
                if (vidaBart == 1)
                    estadoBart = shifting ? EstadoBart.BLUE_HURTED_SHIFT : EstadoBart.BLUE_HURTED;
                else
                    estadoBart = shifting ? EstadoBart.BLUE_SHIFT : EstadoBart.BLUE;
            } else if (vidaBart == 1)
                estadoBart = shifting ? EstadoBart.HURTED_SHIFT : EstadoBart.HURTED;
            else
                estadoBart = shifting ? EstadoBart.SHIFT : EstadoBart.NORMAL;
        }

        // aplicar multiplicadores
        if (isBlue())
            speed *= blueMultiplier;
        if (shifting) {
            speed *= shiftMultiplier;
        }

        // ajustar transparencia para transición. tuve que investigar cómo se hacía. es curioso lo del alpha
        if (shifting) {
            alphaShift += fadeSpeed * delta;
        } else
            alphaShift -= fadeSpeed * delta;
        alphaShift = MathUtils.clamp(alphaShift, 0f, 1f);

        if (!controlesMovil) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                bartSprite.setFlip(true, false);
                bartSprite.translateX(speed * delta);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                bartSprite.translateX(-speed * delta);
                bartSprite.setFlip(false, false);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                bartSprite.translateY(speed * delta);

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                bartSprite.translateY(-speed * delta);

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                if (primeraDuffDisparada) {
                    if (vidaBart == 3)
                        cooldownDuff = 1f;
                    else if (vidaBart == 2)
                        cooldownDuff = 1.5f;
                    else
                        cooldownDuff = 2f;

                    if (timerDisparo > cooldownDuff && estadoBart != EstadoBart.DEAD) {
                        createDuff();
                        timerDisparo = 0f;
                    }
                } else {
                    createDuff();
                    primeraDuffDisparada = true;
                    timerDisparo = 0f;
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.G))
                modoDebug = true;
        } else {

            if (Gdx.input.justTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                game.viewport.unproject(touchPos);

                if (buttonDuffSprite.getBoundingRectangle().contains(touchPos))
                    if (primeraDuffDisparada) {
                        if (vidaBart == 3)
                            cooldownDuff = 1f;
                        else if (vidaBart == 2)
                            cooldownDuff = 1.5f;
                        else
                            cooldownDuff = 2f;

                        if (timerDisparo > cooldownDuff && estadoBart != EstadoBart.DEAD) {
                            createDuff();
                            timerDisparo = 0f;
                        }
                    } else {
                        createDuff();
                        primeraDuffDisparada = true;
                        timerDisparo = 0f;
                    }
            }

            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY());
                game.viewport.unproject(touchPos);

                if (!buttonShiftSprite.getBoundingRectangle().contains(touchPos) &&
                    !buttonDuffSprite.getBoundingRectangle().contains(touchPos)) {
                    if (touchPos.x > bartSprite.getX())
                        bartSprite.translateX(speed * delta);

                    if (touchPos.x < bartSprite.getX())
                        bartSprite.translateX(-speed * delta);

                    if (touchPos.y > bartSprite.getY())
                        bartSprite.translateY(speed * delta);

                    if (touchPos.y < bartSprite.getY())
                        bartSprite.translateY(-speed * delta);
                }
            }
        }

        if (modoDebug) {
            // para debug
            if (estadoBart != lastEstado) {
                timerDebug += delta;
                if (timerDebug > 0.1f) {
                    timerDebug = 0;
                    lastEstado = estadoBart;
                    System.out.println("Velocidad: " + speed + " | Estado: " + estadoBart);
                }
            }
            // Con la Q alternas entre powerup y normal
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q))
                estadoBart = isBlue() ? EstadoBart.NORMAL : EstadoBart.BLUE;
        }

    }

    private void logic() {
        float duffSpeed = 1000f + vidaBart * 200;
        float homerSpeed = vidaHomer * 11;
        float slowBulletSpeed = -100f;
        float sideBulletSpeed = 70f;
        float diagonalBulletSpeed = 60f;
        float bulletSpeed = -500f;
        float powerUpSpeed = -100f;

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float bartWidth = bartSprite.getWidth();
        float bartHeight = bartSprite.getHeight();

        float homerWidth = homerSprite.getWidth();
        float homerHeight = homerSprite.getHeight();

        // evitar que bart se salga del mapa
        bartSprite.setX(MathUtils.clamp(bartSprite.getX(), 0, worldWidth - bartWidth));
        bartSprite.setY(MathUtils.clamp(bartSprite.getY(), 0, worldHeight - bartHeight));

        float delta = Gdx.graphics.getDeltaTime(); // para lo de los fps

        // hurtbox bart, más pequeña que el sprite, como se suele hacer en juegos de esquivar. cálculo buscado porque no sabía centrarla
        float hurtWidth = bartWidth / 3f;
        float hurtHeight = bartHeight / 2.5f;
        float offsetX = (bartWidth - hurtWidth) / 2f;
        float offsetY = (bartHeight - hurtHeight) / 1.5f;
        bartRectangle.set(bartSprite.getX() + offsetX, bartSprite.getY() + offsetY, hurtWidth, hurtHeight);
        homerRectangle.set(homerSprite.getX(), homerSprite.getY(), homerSprite.getWidth(), homerSprite.getHeight());

        // lógica movimiento homer

        if (vidaHomer > 0) {
            if (derecha) {
                homerSprite.translateX(homerSpeed * delta);
                if (homerSprite.getX() >= viewport.getWorldWidth() - homerWidth)
                    derecha = false;
            } else {
                homerSprite.translateX(-homerSpeed * delta);
                if (homerSprite.getX() <= 0) {
                    derecha = true;
                }
            }
        } else {
            homerSprite.setTexture(homerSleepingTexture);
        }

        if (vidaHomer <= 50 && vidaHomer > 0)
            homerSprite.setTexture(homerSemiSleepingTexture);

        // balas rápidas para esquivar. gravedad. se borran al terminar (rojas)
        for (int i = bulletSprites.size - 1; i >= 0; i--) {
            Sprite bulletSprite = bulletSprites.get(i);
            float bulletWidth = bulletSprite.getWidth();
            float bulletHeight = bulletSprite.getHeight();

            if (bulletSprite.getY() >= 500)
                bulletSprite.translateY((bulletSpeed * 4 * delta));
            else
                bulletSprite.translateY((bulletSpeed * delta));


            bulletRectangle.set(bulletSprite.getX(), bulletSprite.getY(), bulletWidth, bulletHeight);

            if (bulletSprite.getY() < -bulletHeight)
                bulletSprites.removeIndex(i);
            else if (bartRectangle.overlaps(bulletRectangle)) {
                bartHit = true;
            }
        }

        // balas lentas (amarillas)
        for (int i = slowBulletSprites.size - 1; i >= 0; i--) {
            Sprite slowBulletSprite = slowBulletSprites.get(i);
            float slowBulletWidth = slowBulletSprite.getWidth();
            float slowBulletHeight = slowBulletSprite.getHeight();

            // empieza rápido, termina lento
            if (slowBulletSprite.getY() >= 500)
                slowBulletSprite.translateY((slowBulletSpeed * 2 * delta));
            else
                slowBulletSprite.translateY((slowBulletSpeed * delta));

            slowBulletRectangle.set(slowBulletSprite.getX(), slowBulletSprite.getY(), slowBulletWidth, slowBulletHeight);

            if (slowBulletSprite.getY() < -slowBulletHeight)
                slowBulletSprites.removeIndex(i);
            else if (bartRectangle.overlaps(slowBulletRectangle)) {
                bartHit = true;
            }
        }

        // balas dirección horizontal (verdes)
        for (int i = sideBulletSprites.size - 1; i >= 0; i--) {
            Sprite sideBulletSprite = sideBulletSprites.get(i);
            boolean derecha = sideBulletsDerecha.get(i);
            float sideBulletWidth = sideBulletSprite.getWidth();
            float sideBulletHeight = sideBulletSprite.getHeight();

            if (derecha)
                sideBulletSprite.translateX((-sideBulletSpeed * delta));
            else
                sideBulletSprite.translateX((sideBulletSpeed * delta));

            sideBulletRectangle.set(sideBulletSprite.getX(), sideBulletSprite.getY(), sideBulletWidth, sideBulletHeight);

            if (sideBulletSprite.getX() < -sideBulletWidth) {
                sideBulletSprites.removeIndex(i);
                sideBulletsDerecha.removeIndex(i);
            } else if (bartRectangle.overlaps(sideBulletRectangle)) {
                bartHit = true;
            }
        }

        // balas dirección diagonal (moradas)
        for (int i = diagonalBulletSprites.size - 1; i >= 0; i--) {
            Sprite bullet = diagonalBulletSprites.get(i);
            boolean derecha = diagonalBulletDerecha.get(i);

            bullet.translateY(-diagonalBulletSpeed * delta);
            if (derecha)
                bullet.translateX(-diagonalBulletSpeed * delta);
            else
                bullet.translateX(diagonalBulletSpeed * delta);

            diagonalBulletRectangle.set(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());

            if (bartRectangle.overlaps(diagonalBulletRectangle)) {
                bartHit = true;
            } else if (bullet.getY() < -bullet.getHeight()) {
                diagonalBulletSprites.removeIndex(i);
                diagonalBulletDerecha.removeIndex(i);
            }
        }

        // vidas (icono bart)
        for (int i = vidaSprites.size - 1; i >= 0; i--) {
            Sprite vidaSprite = vidaSprites.get(i);
            float width = vidaSprite.getWidth();
            float height = vidaSprite.getHeight();

            vidaSprite.translateY((powerUpSpeed * delta));

            vidaRectangle.set(vidaSprite.getX(), vidaSprite.getY(), width, height);

            if (vidaSprite.getY() < -height)
                vidaSprites.removeIndex(i);
            else if (bartRectangle.overlaps(vidaRectangle)) {
                vida.play();
                vidaBart++;
                vidaSprites.removeIndex(i);
            }
        }

        // capas (icono capa), transforma en blue bart
        for (int i = capasSprites.size - 1; i >= 0; i--) {
            Sprite capaSprite = capasSprites.get(i);
            float width = capaSprite.getWidth();
            float height = capaSprite.getHeight();

            capaSprite.translateY((powerUpSpeed * delta));

            capaRectangle.set(capaSprite.getX(), capaSprite.getY(), width, height);

            if (capaSprite.getY() < -height)
                capasSprites.removeIndex(i);
            else if (bartRectangle.overlaps(capaRectangle)) {
                powerup.play();
                estadoBart = EstadoBart.BLUE;
                capasSprites.removeIndex(i);
            }
        }

        // bart
        if (bartHit) {
            takeDamage();
            bartHit = false;
        }
        // estados de bart y transiciones chip
        switch (estadoBart) {
            case NORMAL:
            case SHIFT:
                chipSprite.setTexture(chipTexture);
                chipSprite.setAlpha(alphaShift);
                bartSprite.setTexture(bartTexture);
                break;
            case BLUE:
            case BLUE_SHIFT:
                chipSprite.setTexture(blueChipTexture);
                chipSprite.setAlpha(alphaShift);
                bartSprite.setTexture(blueBartTexture);
                break;
            case HURTED:
            case HURTED_SHIFT:
                chipSprite.setTexture(brokenChipTexture);
                chipSprite.setAlpha(alphaShift);
                bartSprite.setTexture(bartHurtedTexture);
                break;
            case BLUE_HURTED:
            case BLUE_HURTED_SHIFT:
                chipSprite.setTexture(brokenBlueChipTexture);
                chipSprite.setAlpha(alphaShift);
                bartSprite.setTexture(blueBurtHurtTexture);
                break;
            case DEAD:
                chipSprite.setAlpha(alphaShift);
                bartSprite.setTexture(deadBart);
                break;
        }

        // hurtbox (chip)
        chipSprite.setPosition(bartSprite.getX(), bartSprite.getY());
        // duff (disparos)
        for (int i = duffSprites.size - 1; i >= 0; i--) {
            Sprite duffSprite = duffSprites.get(i);
            float duffWidth = duffSprite.getWidth();
            float duffHeight = duffSprite.getHeight();

            if (estadoBart == EstadoBart.BLUE || estadoBart == EstadoBart.BLUE_SHIFT)
                duffSprite.translateY((duffSpeed * blueMultiplier * delta));
            else
                duffSprite.translateY((duffSpeed * delta));
            duffRectangle.set(duffSprite.getX(), duffSprite.getY(), duffWidth, duffHeight);

            if (duffSprite.getY() < -duffHeight)
                duffSprites.removeIndex(i);
            else if (homerRectangle.overlaps(duffRectangle)) {
                hDoh.play();
                vidaHomer -= 5;
                duffSprites.removeIndex(i);
            }
        }

        // cooldown entre cada bala, decide si es lenta (amarilla) o rápida (roja)
        timerBullets += delta;
        if (timerBullets > cooldownBullet) {
            boolean fast = MathUtils.randomBoolean();
            if (fast)
                createBullet();
            else
                createSlowBullet();
            timerBullets = 0;
            cooldownBullet = MathUtils.random(0.25f, vidaHomer / 100f);
        }

        // cooldown entre cada bala horizontal
        timerSideBullets += delta;
        if (timerSideBullets > cooldownSideBullet) {
            createSideBullet();
            timerSideBullets = 0;
            cooldownSideBullet = MathUtils.random(0.5f, 2 * vidaHomer / 100f);
        }

        // cooldown entre cada bala diagonal
        timerDiagonalBullets += delta;
        if (timerDiagonalBullets > cooldownDiagonalBullet) {
            createDiagonalBullet();
            timerDiagonalBullets = 0;
            cooldownDiagonalBullet = MathUtils.random(0.5f, 2 * vidaHomer / 100f);
        }

        /*
        cooldown vidas
        la vida no spawnea si hay más de una en pantalla, si tienes 3 vidas
        o si acabas de empezar la partida. cuando recibas el primer hit podrán salir
         */
        timerVidas += delta;
        if (timerVidas > cooldownVida && cooldownVida != -1f) {
            if (vidaBart < 3 && vidaBart > 0) {
                if (vidaSprites.size == 0)
                    createVida();
            } else
                System.out.println("VIDA NO SPAWNEADA");
            timerVidas = 0;
            cooldownVida = MathUtils.random(20f, maxEsperaVida * vidaBart);
            System.out.println("VIDA EN " + cooldownVida + " SEGUNDOS");
        }

        /*
        cooldown capa
        solo spawnean si no hay en pantalla y si no estás transformado
         */
        timerPowerups += delta;
        if (timerPowerups > cooldownPowerups) {
            if (capasSprites.size == 0 && estadoBart != EstadoBart.BLUE && estadoBart != EstadoBart.BLUE_SHIFT)
                createCapa();
            else
                System.out.println("CAPA NO SPAWNEADA");
            timerPowerups = 0;
            cooldownPowerups = MathUtils.random(10f, maxEsperaCapa * vidaBart);
            System.out.println("CAPA EN " + cooldownPowerups + " SEGUNDOS");
        }

        // duracion capa
        if (isBlue()) {
            timerDuracionPowerup += delta;
            if (timerDuracionPowerup > duracionCapa) {
                estadoBart = EstadoBart.NORMAL;
                timerDuracionPowerup = 0;
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(bgTexture, 0, 0, worldWidth, worldHeight); // fondo
        bartSprite.draw(spriteBatch);
        bartSprite.setAlpha(1f);
        homerSprite.draw(spriteBatch);
        chipSprite.draw(spriteBatch);

        for (Sprite bulletSprite : bulletSprites) {
            bulletSprite.draw(spriteBatch);
        }

        for (Sprite slowBulletSprite : slowBulletSprites) {
            slowBulletSprite.draw(spriteBatch);
        }

        for (Sprite duffSprite : duffSprites) {
            duffSprite.draw(spriteBatch);
        }

        for (Sprite sideBulletSprite : sideBulletSprites) {
            sideBulletSprite.draw(spriteBatch);
        }

        for (Sprite diagonalBulletSprite : diagonalBulletSprites) {
            diagonalBulletSprite.draw(spriteBatch);
        }

        for (Sprite vidaSprite : vidaSprites) {
            vidaSprite.draw(spriteBatch);
        }

        for (Sprite capaSprite : capasSprites) {
            capaSprite.draw(spriteBatch);
        }

        buttonShiftSprite.draw(spriteBatch);
        buttonDuffSprite.draw(spriteBatch);

        spriteBatch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barraVidaAncho = 560f;
        float barraVidaAlto = 20f;

        float porcentajeVida = vidaHomer / maxVidaHomer;
        float anchoActual = barraVidaAncho * porcentajeVida;

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(20, worldHeight - 35, barraVidaAncho, barraVidaAlto);

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(20, worldHeight - 35, anchoActual, barraVidaAlto);

        shapeRenderer.end();
    }

    // devuelve si es azul (powerup capa)
    private boolean isBlue() {
        return estadoBart == EstadoBart.BLUE || estadoBart == EstadoBart.BLUE_SHIFT ||
            estadoBart == EstadoBart.BLUE_HURTED || estadoBart == EstadoBart.BLUE_HURTED_SHIFT;
    }

    private void clearBullets() {
        bulletSprites.clear();
        slowBulletSprites.clear();
        sideBulletSprites.clear();
        sideBulletsDerecha.clear();
        diagonalBulletSprites.clear();
        diagonalBulletDerecha.clear();
    }

    private void takeDamage() {
        bDoh.play();
        vidaBart--;
        timerVidas = 0;
        cooldownVida = MathUtils.random(10f, maxEsperaVida * vidaBart);
        System.out.println("VIDA EN " + cooldownVida + " SEGUNDOS");
        clearBullets();
    }

    private void createVida() {
        float width = 50;
        float height = 50;
        float randomX = MathUtils.random(width, viewport.getWorldWidth() - width);
        Sprite vidaSprite = new Sprite(vidaTexture);
        vidaSprite.setSize(width, height);

        vidaSprite.setPosition(randomX, viewport.getWorldHeight());
        vidaSprites.add(vidaSprite);
        System.out.println("---------------\nVIDA SPAWNEADA\n---------------");
    }

    private void createCapa() {
        float width = 50;
        float height = 50;
        float randomX = MathUtils.random(width, viewport.getWorldWidth() - width);
        Sprite capaSprite = new Sprite(capaTexture);
        capaSprite.setSize(width, height);

        capaSprite.setPosition(randomX, viewport.getWorldHeight());
        capasSprites.add(capaSprite);
        System.out.println("---------------\nCAPA SPAWNEADA\n---------------");
    }

    private void createBullet() {
        piu.play();
        float bulletWidth = 25;
        float bulletHeight = 25;

        Sprite bulletSprite = new Sprite(bulletTexture);
        bulletSprite.setSize(bulletWidth, bulletHeight);

        bulletSprite.setPosition(homerSprite.getX(), homerSprite.getY());
        bulletSprites.add(bulletSprite);
    }

    private void createSlowBullet() {
        piu.play();
        float slowBulletWidth = 30;
        float slowBulletHeight = 30;

        Sprite slowBulletSprite = new Sprite(slowBulletTexture);
        slowBulletSprite.setSize(slowBulletWidth, slowBulletHeight);

        slowBulletSprite.setPosition(homerSprite.getX(), homerSprite.getY());
        slowBulletSprites.add(slowBulletSprite);
    }

    private void createSideBullet() {
        float sideBulletWidth = 20;
        float sideBulletHeight = 20;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        boolean spawnDerecha = MathUtils.randomBoolean();
        float randomY = MathUtils.random(0f, worldHeight - sideBulletHeight);

        Sprite sideBulletSprite = new Sprite(sideBulletTexture);
        sideBulletSprite.setSize(sideBulletWidth, sideBulletHeight);

        // si spawnea a la derecha la creamos en un punto aleatorio de Y
        if (spawnDerecha) {
            sideBulletSprite.setPosition(worldWidth, randomY);
            sideBulletsDerecha.add(true);
        } else {
            sideBulletSprite.setPosition(-sideBulletWidth, randomY);
            sideBulletsDerecha.add(false);
        }
        sideBulletSprites.add(sideBulletSprite);
    }

    private void createDiagonalBullet() {
        float diagonalBulletWidth = 25;
        float diagonalBulletHeight = 25;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite diagonalBulletSprite = new Sprite(diagonalBulletTexture);
        diagonalBulletSprite.setSize(diagonalBulletWidth, diagonalBulletHeight);

        boolean spawnDerecha = MathUtils.randomBoolean();
        float randomY = MathUtils.random(0f, worldHeight - diagonalBulletHeight);

        if (spawnDerecha) {
            diagonalBulletSprite.setPosition(worldWidth, randomY);
            diagonalBulletDerecha.add(true);
        } else {
            diagonalBulletSprite.setPosition(-diagonalBulletWidth, randomY);
            diagonalBulletDerecha.add(false);
        }
        diagonalBulletSprites.add(diagonalBulletSprite);
    }

    private void createRosquilla() {
        float rosqWidth = 25;
        float rosqHeight = 25;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite rosqSprite = new Sprite(rosquillaTexture);
        rosqSprite.setSize(rosqWidth, rosqHeight);

        rosqSprite.setPosition(homerSprite.getX(), homerSprite.getY());
        rosqSprites.add(rosqSprite);
    }

    private void createDuff() {
        float duffWidth = 30;
        float duffHeight = 50;

        Sprite duffSprite = new Sprite(duffTexture);
        duffSprite.setSize(duffWidth, duffHeight);

        duffSprite.setPosition(bartSprite.getX(), bartSprite.getY());
        duffSprites.add(duffSprite);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Texturas
        friendTexture.dispose();
        capaTexture.dispose();
        vidaTexture.dispose();
        bgTexture.dispose();
        bartTexture.dispose();
        bartShiftTexture.dispose();
        bartHurtedShiftTexture.dispose();
        deadBart.dispose();
        chipTexture.dispose();
        blueChipTexture.dispose();
        brokenBlueChipTexture.dispose();
        brokenChipTexture.dispose();
        blueBartTexture.dispose();
        blueBurtHurtTexture.dispose();
        bartHurtedTexture.dispose();
        bulletTexture.dispose();
        slowBulletTexture.dispose();
        sideBulletTexture.dispose();
        diagonalBulletTexture.dispose();
        duffTexture.dispose();
        homerTexture.dispose();
        homerSemiSleepingTexture.dispose();
        homerSleepingTexture.dispose();
        rosquillaTexture.dispose();

        // Sonidos
        bDoh.dispose();
        hDoh.dispose();
        piu.dispose();
        powerup.dispose();
        vida.dispose();

        // Música
        music.dispose();

        // Render
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}


