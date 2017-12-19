package uy.com.agm.gamethree.sprites.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.EnergyBall;
import uy.com.agm.gamethree.tools.Assets;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.GameThreeActorDef;

/**
 * Created by AGM on 12/3/2017.
 */

public class Hero extends Sprite {
    private static final String TAG = Hero.class.getName();

    private enum State {
        STANDING, MOVING_UP, MOVING_DOWN, DEAD
    }
    private State currentState;
    private State previousState;

    public World world;
    public PlayScreen screen;
    public Body b2body;

    private TextureRegion heroStand;
    private Animation heroMovingUp;
    private Animation heroMovingDown;
    private float stateTimer;
    private boolean heroIsDead;

    public Hero(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        heroMovingUp = Assets.instance.hero.heroMovingUp;
        heroMovingDown = Assets.instance.hero.heroMovingDown;
        heroStand = Assets.instance.hero.heroStand;

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineHero() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, Constants.HERO_WIDTH_METERS, Constants.HERO_HEIGHT_METERS);
        defineHero();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        heroIsDead = false;
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public void update(float dt) {
        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, Hero may have collided with sth. and therefore it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        // It prevents Hero from going beyond the limits of the game
        float camUpperEdge = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
        float camBottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;
        float heroUpperEdge = getY() + getHeight();
        float heroBottomEdge = getY();

        // Beyond bottom edge
        if (camBottomEdge > heroBottomEdge) {
            // Be carefull, we broke the simulation because Hero is teleporting.
            b2body.setTransform(b2body.getPosition().x, camBottomEdge + getHeight() / 2, b2body.getAngle());
        }

        // Beyond upper edge
        if (camUpperEdge < heroUpperEdge) {
            // Be carefull, we broke the simulation because Hero is teleporting.
            b2body.setTransform(b2body.getPosition().x, camUpperEdge - getHeight() / 2, b2body.getAngle());
        }

        // Update Sprite with the correct frame depending on Hero's current action
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        // Get Hero's current state. ie. SANDING, MOVING_DOWN...
        currentState = getState();
        TextureRegion region;
        // depending on the state, get corresponding animation keyFrame.
        switch (currentState) {
            case STANDING:
                region = heroStand;
                break;
            case MOVING_DOWN:
                region = (TextureRegion) heroMovingDown.getKeyFrame(stateTimer, true);
                break;
            case MOVING_UP:
                region = (TextureRegion) heroMovingUp.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = heroStand;
                AudioManager.instance.play(Assets.instance.sounds.dead, 1);
                heroIsDead = false;
                break;
            default:
                region = heroStand;
                break;
        }

        // if the current state is the same as the previous state increase the state timer.
        // otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        // Update previous state
        previousState = currentState;

        // Return our final adjusted frame
        return region;
    }

    public State getState() {
        State state;
        if (!heroIsDead) {
            // Test to Box2D for velocity on the y-axis.
            // If Hero is going positive in y-axis he is moving up.
            // If Hero is going negative in y-axis he is moving down.
            // Otherwise he is standing.
            float y = b2body.getLinearVelocity().y;
            if (y > 0) {
                state = State.MOVING_UP;
            } else if (y < 0) {
                state = State.MOVING_DOWN;
            } else {
                state = State.STANDING;
            }
        } else {
            state = State.DEAD;
        }
        return state;
    }

    public void defineHero() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.HERO_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.HERO_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                Constants.POWERBOX_BIT |
                Constants.OBSTACLE_BIT |
                Constants.ITEM_BIT |
                Constants.ENEMY_BIT; // Depicts what can this Fixture collide with (see WorldContactListener)

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    // TODO: ANALIZAR ESTO
    public void draw(SpriteBatch batch) {
        // estado normal
        // esto es asi para compensar lo que hace el draw por defecto, que dibuja rotado no se por que.
        //clockwise - If true, the texture coordinates are rotated 90 degrees clockwise. If false, they are rotated 90 degrees counter clockwise.
        boolean clockwise = true;
        float angulo = 90;
        float height = this.getWidth();
        float width = this.getHeight();
        float temp;

        // touch
        float vAngle = this.b2body.getLinearVelocity().angle();

        // anda bien
        if (0 < vAngle && vAngle <= 90) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = vAngle;
            //angulo = 45;
        }

        if (90 < vAngle && vAngle <= 180) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = 270.0f - vAngle;
            //angulo = 135;
        }

        if (180 < vAngle && vAngle <= 270) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = vAngle;
            //angulo = 225;
            clockwise = false;
        }

        if (270 < vAngle && vAngle <= 360) {
            Gdx.app.debug(TAG, "VANGLE " + vAngle);
            angulo = vAngle;
            //angulo = 315;
            clockwise = false;
        }

        // Draws a rectangle with the texture coordinates rotated 90 degrees.
        batch.draw(this, this.b2body.getPosition().x - width / 2, this.b2body.getPosition().y - height / 2,
                width / 2, height / 2, width, height, 1.0f, 1.0f, angulo, clockwise);
    }

    public void openFire() {
        Vector2 position = new Vector2(b2body.getPosition().x, b2body.getPosition().y + Constants.WEAPON_OFFSET / Constants.PPM);
        screen.creator.createGameThreeActor(new GameThreeActorDef(position, EnergyBall.class));
    }

    public void onDead() {
        heroIsDead = true;
        Gdx.app.debug(TAG, "Morí T_T");
    }
}
