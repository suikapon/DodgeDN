package io.github.dodgedn.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.dodgedn.Dodge;

public class MainMenuScreen implements Screen {
    final Dodge game;
    GlyphLayout titulo;
    GlyphLayout movil;
    GlyphLayout pc;
    BitmapFont font2;
    GlyphLayout controlesPc;
    GlyphLayout controlesAndroid;
    GlyphLayout instrucciones;

    public MainMenuScreen(final Dodge game) {
        this.game = game;
        this.font2 = new BitmapFont();
        font2.setColor(Color.WHITE);
        font2.getData().setScale(1f);
        game.font.getData().setScale(2f);
        this.titulo = new GlyphLayout(game.font, "Dodge!");
        this.movil = new GlyphLayout(game.font, "Tocar pantalla - Usar Móvil");
        this.pc = new GlyphLayout(game.font, "Espacio - Usar Teclado");

        this.controlesPc = new GlyphLayout(game.font, "CONTROLES PC:\nFLECHAS -> MOVIMIENTO\nZ -> DISPARO DUFF\nSHIFT -> FOCUS");
        this.controlesAndroid = new GlyphLayout(game.font, "CONTROLES ANDROID:\nTOCAR -> MOVIMIENTO\nBOTÓN 1 -> FOCUS\nBOTÓN 2 -> DISPARO DUFF");
        this.instrucciones = new GlyphLayout(game.font, "INSTRUCCIONES:\nDISPARA DUFFS A HOMER\nEVITA LAS BALAS\nATRAPA LOS POWERUPS\nLOS GATOS NO SON TUS AMIGOS\nJUST DODGE!");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        game.batch.begin();

        game.font.draw(game.batch, titulo, centrarH(worldWidth,titulo), worldHeight-30);
        game.font.draw(game.batch, movil, centrarH(worldWidth,movil), worldHeight-60);
        game.font.draw(game.batch, pc, centrarH(worldWidth,pc), worldHeight-90);

        game.font.draw(game.batch, controlesPc, centrarH(worldWidth,controlesPc), worldHeight-160);
        game.font.draw(game.batch, controlesAndroid, centrarH(worldWidth,controlesAndroid), worldHeight-350);
        game.font.draw(game.batch, instrucciones, centrarH(worldWidth,instrucciones), worldHeight-530);

        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game, true));
            dispose();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game, false));
            dispose();
        }
    }

    private float centrarH(float worldWidth, GlyphLayout texto)
    {
        return worldWidth/2 - texto.width/2;
    }

    private float centrarV(float worldHeight, GlyphLayout texto)
    {
        return worldHeight/2 + texto.height/2;
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
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

    }

}
