package io.github.dodgedn.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.dodgedn.Dodge;

public class DeadMenuScreen implements Screen {
    final Dodge game;
    GlyphLayout titulo;
    GlyphLayout puntuacionLayout;
    GlyphLayout volver;
    long puntuacion;

    public DeadMenuScreen(final Dodge game, long puntuacion) {
        this.game = game;
        this.puntuacion = puntuacion;
        game.font.getData().setScale(2f);
        this.titulo = new GlyphLayout(game.font, "Perdiste...");
        this.puntuacionLayout = new GlyphLayout(game.font, "Puntuación: " + this.puntuacion);
        this.volver = new GlyphLayout(game.font, "Espacio/Tocar - Volver");
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

        game.font.draw(game.batch, titulo, centrarH(worldWidth, titulo), centrarV(worldHeight, titulo) + 30);
        game.font.draw(game.batch, puntuacionLayout, centrarH(worldWidth, puntuacionLayout), centrarV(worldHeight, puntuacionLayout) - 30);
        game.font.draw(game.batch, volver, centrarH(worldWidth,volver), centrarV(worldHeight,volver)-60);

        game.batch.end();

        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private float centrarH(float worldWidth, GlyphLayout texto) {
        return worldWidth / 2 - texto.width / 2;
    }

    private float centrarV(float worldHeight, GlyphLayout texto) {
        return worldHeight / 2 + texto.height / 2;
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
