package uy.com.agm.gamethree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.concurrent.LinkedBlockingQueue;

import uy.com.agm.gamethree.game.GameThree;
import uy.com.agm.gamethree.scenes.Hud;
import uy.com.agm.gamethree.sprites.Items.Item;
import uy.com.agm.gamethree.sprites.Items.ItemDef;
import uy.com.agm.gamethree.sprites.Items.PowerOne;
import uy.com.agm.gamethree.sprites.enemies.Enemy;
import uy.com.agm.gamethree.sprites.player.Hero;
import uy.com.agm.gamethree.tools.B2WorldCreator;
import uy.com.agm.gamethree.tools.WorldContactListener;

/**
 * Created by AGM on 12/2/2017.
 */

public class PlayScreen implements Screen {
    private static final String TAG = PlayScreen.class.getName();
    private GameThree game;
    public OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    public Hero player;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToCreate;

    public PlayScreen(GameThree game) {
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(GameThree.V_WIDTH / GameThree.PPM, GameThree.V_HEIGHT / GameThree.PPM, gameCam);
        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("level1/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / GameThree.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Sin gravedad
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Hero(this, 1.0f, 1.0f);

        world.setContactListener(new WorldContactListener());

        items = new Array<Item>();
        itemsToCreate = new LinkedBlockingQueue<ItemDef>();
    }

    public void createItem(ItemDef idef) {
        itemsToCreate.add(idef);
    }

    public void handleCreatingItems() {
        if (!itemsToCreate.isEmpty()) {
            ItemDef idef = itemsToCreate.poll(); // similar to pop but for a queue, removes the element
            if (idef.type == PowerOne.class) {
                items.add(new PowerOne(this, idef.position.x, idef.position.y));
            }
        }
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= 400) {
            //player.b2body.applyLinearImpulse(new Vector2(0, 5f), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, 4.0f));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y >= -400) {
            //player.b2body.applyLinearImpulse(new Vector2(0, -5f), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(player.b2body.getLinearVelocity().x, -4.0f));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 400) {
            //player.b2body.applyLinearImpulse(new Vector2(5f, 0), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(4.0f, player.b2body.getLinearVelocity().y));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -400) {
            //player.b2body.applyLinearImpulse(new Vector2(-5f, 0), player.b2body.getWorldCenter(), true);
            player.b2body.setLinearVelocity(new Vector2(-4.0f, player.b2body.getLinearVelocity().y));
        }
    }

    public void update(float dt) {
        handleInput(dt);
        handleCreatingItems();

        world.step(1 / 60f, 6, 2);

        player.update(dt);

        // Enemies
        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            // Cuando el enemyOne entra en la camara, se activa (se mueve y puede colisionar)
            // Hay que tener mucho cuidado porque si el enemigo esta destruido, el body no existe y da errores aleatorios
            if (!enemy.isDestroyed() && enemy.getY() <= gameCam.position.y + gamePort.getWorldHeight() / 2 ) {
                enemy.b2body.setActive(true);
            }
        }

        // Items
        for(Item item: items) {
            item.update(dt);
        }


        hud.update(dt);
        //gameCam.position.x = player.b2body.getPosition().x;
        //gameCam.position.y += 0.5 * dt;

        // Intento controlar que no se vaya de los limites (este codigo no deberia ir aca, deberia ir en la clase del heroe)
        float width = player.b2body.getFixtureList().get(0).getShape().getRadius();
        if (player.b2body.getPosition().y + width >= gameCam.position.y + gamePort.getWorldHeight() / 2) {
            /*
            // esto esta bien al parecer. Demora, mejor poner un objeto que vaya avanzando y topee.
            float posicionEnDondeQuieroEstar = gameCam.position.y + gamePort.getWorldHeight() / 2 - width;
            float posicionActual = player.b2body.getPosition().y;
            float v = posicionEnDondeQuieroEstar - posicionActual;
            player.b2body.setLinearVelocity(player.b2body.getLinearVelocity().x, v);
            */
            player.b2body.setTransform(player.b2body.getPosition().x, gameCam.position.y + gamePort.getWorldHeight() / 2 - width, player.b2body.getAngle());
        }
        if (player.b2body.getPosition().y - width <= gameCam.position.y - gamePort.getWorldHeight() / 2) {
            player.b2body.setTransform(player.b2body.getPosition().x, gameCam.position.y - gamePort.getWorldHeight() / 2 + width, player.b2body.getAngle());
        }
        gameCam.update();

        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.batch);
        }

        for(Item item: items) {
            item.draw(game.batch);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // debug
        //if (Constants.DEBUG_BOUNDS) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(gameCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        player.renderDebug(shapeRenderer);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.renderDebug(shapeRenderer);
        }
        for(Item item: items) {
            item.renderDebug(shapeRenderer);
        }
        shapeRenderer.end();
        //}
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public Hud getHud() {
        return hud;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
