package io.github.dodgedn;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main implements ApplicationListener {
    Texture bgTexture;
    Texture bartTexture;
    Texture blueBartTexture;
    Texture deadBart;
    Texture bartHurtedTexture;
    Texture bulletTexture;
    Texture duffTexture;
    Texture homerTexture;
    Texture homerSemiSleepingTexture;
    Texture homerSleepingTexture;
    Texture rosquillaTexture;
    Texture slowBulletTexture;

    Sound piu;
    Sound bDoh;
    Sound hDoh;
    Music music;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite bartSprite;
    Sprite homerSprite;

    Vector2 touchPos;
    Array<Sprite> bulletSprites;
    Array<Sprite> slowBulletSprites;
    Array<Sprite> rosqSprites;
    Array<Sprite> duffSprites;

    float timer;
    Rectangle bartRectangle;
    Rectangle homerRectangle;
    Rectangle bulletRectangle;
    Rectangle rosqRectangle;
    Rectangle duffRectangle;
    Rectangle slowBulletRectangle;
    float timerDisparo;
    float vidaHomer = 100f;
    int vidaBart = 3;
    boolean derecha = true;

    @Override
    public void create() {
        bgTexture = new Texture("bg2.png");
        bartTexture = new Texture("bartolo.png");
        deadBart = new Texture("bartdead.png");
        blueBartTexture = new Texture("bartoloblue.png");
        bartHurtedTexture = new Texture("bartolohurt.png");
        bulletTexture = new Texture("bullet.png");
        slowBulletTexture = new Texture("slowbullet.png");
        duffTexture = new Texture("duff.png");
        homerTexture = new Texture("homer.png");
        homerSemiSleepingTexture = new Texture("homeralmsleep.png");
        homerSleepingTexture = new Texture("homersleep.png");
        rosquillaTexture = new Texture("rosquilla.png");
        bDoh = Gdx.audio.newSound(Gdx.files.internal("bart_doh.mp3"));
        hDoh = Gdx.audio.newSound(Gdx.files.internal("homer_doh.mp3"));
        piu = Gdx.audio.newSound(Gdx.files.internal("piu.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        spriteBatch = new SpriteBatch();

        viewport = new FitViewport(600, 800);
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        bartSprite = new Sprite(bartTexture);
        bartSprite.setSize(40, 90);
        bartSprite.setPosition(worldWidth / 2 - bartSprite.getWidth(), 0);

        homerSprite = new Sprite(homerTexture);
        homerSprite.setSize(60, 110);
        homerSprite.setPosition(worldWidth / 2 - homerSprite.getWidth(), worldHeight - 100 - homerSprite.getHeight());

        touchPos = new Vector2();

        bulletSprites = new Array<>();
        rosqSprites = new Array<>();
        duffSprites = new Array<>();
        slowBulletSprites = new Array<>();

        bartRectangle = new Rectangle();
        homerRectangle = new Rectangle();
        bulletRectangle = new Rectangle();
        duffRectangle = new Rectangle();
        rosqRectangle = new Rectangle();
        slowBulletRectangle = new Rectangle();

        bDoh.setVolume(0,0.5f);

        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 500f;
        float delta = Gdx.graphics.getDeltaTime();
        timerDisparo += delta;
        float cooldownDisparo;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            speed = speed / 2f;

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
        {
            if (vidaBart==3)
                cooldownDisparo = 1f;
            else if (vidaBart==2)
                cooldownDisparo = 1.5f;
            else
                cooldownDisparo = 2f;

            if (timerDisparo > cooldownDisparo) {
                createDuff();
                timerDisparo = 0f;
            }
        }


    }

    private void logic() {
        float duffSpeed = 1500f;
        float homerSpeed = vidaHomer*10;
        float slowBulletSpeed = -100f;
        float bulletSpeed = -700f;

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

        // hurtbox bart, más pequeña que el sprite, como se suele hacer en juegos de esquivar
        bartRectangle.set(bartSprite.getX(), bartSprite.getY(), (float) (bartWidth/1.3), (float) (bartHeight/1.3));
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
        } else
        {
            homerSprite.setTexture(homerSleepingTexture);
        }

        if (vidaHomer<=50 && vidaHomer>0)
            homerSprite.setTexture(homerSemiSleepingTexture);

        // balas para esquivar. gravedad. se borran al terminar
        for (int i = bulletSprites.size - 1; i >= 0; i--) {
            Sprite bulletSprite = bulletSprites.get(i);
            float bulletWidth = bulletSprite.getWidth();
            float bulletHeight = bulletSprite.getHeight();

            bulletSprite.translateY((bulletSpeed * delta));

            bulletRectangle.set(bulletSprite.getX(), bulletSprite.getY(), bulletWidth, bulletHeight);

            if (bulletSprite.getY() < -bulletHeight)
                bulletSprites.removeIndex(i);
            else if (bartRectangle.overlaps(bulletRectangle)) {
                bDoh.play();
                vidaBart--;
                bulletSprites.removeIndex(i);
            }
        }

        for (int i = slowBulletSprites.size - 1; i >= 0; i--) {
            Sprite slowBulletSprite = slowBulletSprites.get(i);
            float slowBulletWidth = slowBulletSprite.getWidth();
            float slowBulletHeight = slowBulletSprite.getHeight();

            slowBulletSprite.translateY((slowBulletSpeed * delta));

            slowBulletRectangle.set(slowBulletSprite.getX(), slowBulletSprite.getY(), slowBulletWidth, slowBulletHeight);

            if (slowBulletSprite.getY() < -slowBulletHeight)
                slowBulletSprites.removeIndex(i);
            else if (bartRectangle.overlaps(slowBulletRectangle)) {
                bDoh.play();
                vidaBart--;
                slowBulletSprites.removeIndex(i);
            }
        }

        // bart
        if (vidaBart==1)
            bartSprite.setTexture(bartHurtedTexture);
        if (vidaBart<=0)
            bartSprite.setTexture(deadBart);

        // duff (disparos)
        for (int i = duffSprites.size - 1; i >= 0; i--) {
            Sprite duffSprite = duffSprites.get(i);
            float duffWidth = duffSprite.getWidth();
            float duffHeight = duffSprite.getHeight();

            duffSprite.translateY((duffSpeed * delta));
            duffRectangle.set(duffSprite.getX(), duffSprite.getY(), duffWidth, duffHeight);

            if (duffSprite.getY() < -duffHeight)
                duffSprites.removeIndex(i);
            else if (homerRectangle.overlaps(duffRectangle)) {
                hDoh.play();
                vidaHomer-=5;
                duffSprites.removeIndex(i);
            }
        }

        // cooldown entre cada bala, decide si es lenta o rápida
        timer += delta;
        if (timer > MathUtils.random(0.01f, 40f)) {
            boolean fast = MathUtils.randomBoolean();
            if (fast)
                createBullet();
            else
                createSlowBullet();
            timer = 0;
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
        homerSprite.draw(spriteBatch);

        for (Sprite bulletSprite : bulletSprites) {
            bulletSprite.draw(spriteBatch);
        }

        for (Sprite slowBulletSprite : slowBulletSprites) {
            slowBulletSprite.draw(spriteBatch);
        }

        for (Sprite duffSprite : duffSprites) {
            duffSprite.draw(spriteBatch);
        }

        spriteBatch.end();
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

    private void createRosquilla()
    {
        float rosqWidth = 25;
        float rosqHeight = 25;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite rosqSprite = new Sprite(rosquillaTexture);
        rosqSprite.setSize(rosqWidth, rosqHeight);

        rosqSprite.setPosition(homerSprite.getX(), homerSprite.getY());
        rosqSprites.add(rosqSprite);
    }

    private void createDuff()
    {
        float duffWidth = 30;
        float duffHeight = 50;

        Sprite duffSprite = new Sprite(duffTexture);
        duffSprite.setSize(duffWidth, duffHeight);

        duffSprite.setPosition(bartSprite.getX(), bartSprite.getY());
        duffSprites.add(duffSprite);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
