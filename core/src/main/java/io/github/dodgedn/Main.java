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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture bgTexture;
    Texture bartTexture;
    Texture blueBartTexture;
    Texture bartHurtedTexture;
    Texture bulletTexture;
    Texture duffTexture;
    Texture homerTexture;
    Texture homerSemiSleepingTexture;
    Texture homerSleepingTexture;

    Sound bDoh;
    Sound hDoh;
    Music music;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite bartSprite;
    Sprite homerSprite;

    Vector2 touchPos;
    Array<Sprite> bulletSprites;

    @Override
    public void create() {
        bgTexture = new Texture("bg2.png");
        bartTexture = new Texture("bartolo.png");
        blueBartTexture = new Texture("bartoloblue.png");
        bartHurtedTexture = new Texture("bartolohurt.png");
        bulletTexture = new Texture("bullet.png");
        duffTexture = new Texture("duff.png");
        homerTexture = new Texture("homer.png");
        homerSemiSleepingTexture = new Texture("homeralmsleep.png");
        homerSleepingTexture = new Texture("homersleep.png");
        bDoh = Gdx.audio.newSound(Gdx.files.internal("bart_doh.mp3"));
        hDoh = Gdx.audio.newSound(Gdx.files.internal("homer_doh.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(500,800);

        bartSprite = new Sprite(bartTexture);
        bartSprite.setSize(50,100);

        homerSprite = new Sprite(homerTexture);
        homerSprite.setSize(70,120);

        touchPos = new Vector2();

        bulletSprites = new Array<>();

        createBullet();
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

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            speed = speed / 2f;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bartSprite.translateX(speed * delta);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bartSprite.translateX(-speed * delta);

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            bartSprite.translateY(speed*delta);

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            bartSprite.translateY(-speed*delta);
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float bartWidth = bartSprite.getWidth();
        float bartHeight = bartSprite.getHeight();

        bartSprite.setX(MathUtils.clamp(bartSprite.getX(),0,worldWidth-bartWidth));
        bartSprite.setY(MathUtils.clamp(bartSprite.getY(),0,worldHeight-bartHeight));
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

        spriteBatch.draw(homerTexture, 200, 600, 70, 120); // enemigo

        for (Sprite bulletSprite : bulletSprites)
        {
            bulletSprite.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void createBullet()
    {
        float bulletWidth = 50;
        float bulletHeight = 50;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite bulletSprite = new Sprite(bulletTexture);
        bulletSprite.setSize(bulletWidth,bulletHeight);
        bulletSprite.setX(0);
        bulletSprite.setY(worldHeight);
        bulletSprites.add(bulletSprite);
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
