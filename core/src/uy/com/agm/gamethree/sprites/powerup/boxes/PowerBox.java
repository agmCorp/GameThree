package uy.com.agm.gamethree.sprites.powerup.boxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.GameThreeActorDef;
import uy.com.agm.gamethree.sprites.powerup.Items.PowerOne;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/17/2017.
 */

public class PowerBox extends Sprite {
    private static final String TAG = PowerBox.class.getName();

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    TextureRegion powerBoxStand;
    TextureRegion powerBoxDamagedLittle;
    TextureRegion powerBoxDamagedMedium;
    TextureRegion powerBoxDamagedHard;

    protected enum State {WAITING, OPENED, FINISHED};
    protected State currentState;
    protected MapObject object;

    private int damage;

    public PowerBox(PlayScreen screen, MapObject object) {
        this.object = object;
        this.world = screen.getWorld();
        this.screen = screen;

        // Obtengo el rectangulo que dibuje en tiled
        Rectangle rect = ((RectangleMapObject) object).getRectangle();

        // Seteo punto (x,y) en la parte inferior izquierda de ese rectangulo. Ese punto lo usa definePowerBox como centro de su circulo
        setPosition(rect.getX() / Constants.PPM, rect.getY() / Constants.PPM);
        definePowerBox();
        b2body.setActive(false);

        powerBoxStand = Assets.instance.powerBox.powerBoxStand;
        powerBoxDamagedLittle = Assets.instance.powerBox.powerBoxDamagedLittle;
        powerBoxDamagedMedium = Assets.instance.powerBox.powerBoxDamagedMedium;
        powerBoxDamagedHard = Assets.instance.powerBox.powerBoxDamagedHard;

        // setbounds es el que determina el tamano del dibujito del enemigo en pantalla.
        // Es un rectangulo y recibe un punto x, y que es el vertice inferior izquierdo de ese rectangulo
        float resize = 0.7f; // TODO ARREGLAR ESTO
        setBounds(0, 0, powerBoxStand.getRegionWidth() * resize / Constants.PPM, powerBoxStand.getRegionHeight() * resize / Constants.PPM);

        // Ahora posicionamos ese rectangulo en el centro del circulo. Esta hecho en dos pasos porque es setBound quien define width y height
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);


        currentState = State.WAITING;
        damage = 0;
    }

    public boolean isDestroyed() {
        return currentState != State.WAITING;
    }

    protected void getItemOnHit() {
        float MARGEN = 32; // TODO ARREGLAR ESTO
        if (object.getProperties().containsKey("powerOne")) {
            screen.creator.createGameThreeActor(new GameThreeActorDef(new Vector2(b2body.getPosition().x, b2body.getPosition().y + MARGEN / Constants.PPM), PowerOne.class));
        }
    }

    protected void definePowerBox() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(29 / Constants.PPM);
        fdef.filter.categoryBits = Constants.POWERBOX_BIT; // Indica que es
        fdef.filter.maskBits = Constants.ENEMY_BIT |
                Constants.ITEM_BIT |
                Constants.WEAPON_BIT |
                Constants.HERO_BIT; // Con que puede colisionar

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt) {
        switch (currentState) {
            case WAITING:
                switch (damage) {
                    case 0:
                        setRegion(powerBoxStand);
                        break;
                    case 1:
                        setRegion(powerBoxDamagedLittle);
                        break;
                    case 2:
                        setRegion(powerBoxDamagedMedium);
                        break;
                    case 3:
                        setRegion(powerBoxDamagedHard);
                        break;
                    default:
                        break;
                }
                break;
            case OPENED:
                world.destroyBody(b2body);
                currentState = State.FINISHED;
                AudioManager.instance.play(Assets.instance.sounds.openPowerBox, 1, MathUtils.random(1.0f, 1.1f));
                break;
            case FINISHED:
                break;
            default:
                break;
        }
        /* When a Power Box is on camara, it activates (it can colide).
        * You have to be very careful because if the power box is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            if (getY() - getWidth() <= screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2) {
                b2body.setActive(true);
            }
            if (getY() + getWidth() <= screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2) {
                b2body.setActive(false);
            }
        }
    }

    public void onHit() {
        /*crc:
        Debemos remove sus b2boxbody asi no tiene mas colisiones con nadie.
        Esto no se puede hacer aca porque esta siendo llamado desde el WorldContactListener que
        es invocado desde el PlayScreen/update/world.step(1 / 60f, 6, 2);
        No se puede borrar ningun tipo de b2boxbody cuando la simulacion esta ocurriendo.
         */

        int strength = object.getProperties().get("strength", 0, Integer.class);
        Gdx.app.debug(TAG, "strength, damage " + strength + " " + damage);
        if (damage >= strength - 1){
            getItemOnHit();
            currentState = State.OPENED;
        } else {
            damage++;
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        // TODO IF CONSTANTES.DEBUG
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
        Gdx.app.debug(TAG, "** TAMANO RENDERDEBUG X, Y, WIDTH, EIGHT" + getBoundingRectangle().x + " " + getBoundingRectangle().y + " " + getBoundingRectangle().width + " " + getBoundingRectangle().height);
    }

    public void draw(Batch batch) {
        if (currentState == State.WAITING) {
            super.draw(batch);
        }
    }
}
