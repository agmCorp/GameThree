package uy.com.agm.gamethree.sprites.finals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.tools.Constants;

/**
 * Created by AGM on 12/30/2017.
 */

public class FinalLevelOne extends Sprite {
    private static final String TAG = FinalLevelOne.class.getName();

    private World world;
    private PlayScreen screen;
    private Body b2body;

    private enum State {
        WALKING, IDLE, SHOOTING, EXPLODING, DEAD
    }

    private State currentState;
    private int damage;
    private float stateTime;

    private TextureRegion finalLevelOneStand;
    private Animation finalLevelOneWalkAnimation;
    private Animation finalLevelOneIdleAnimation;
    private Animation finalLevelOneShootAnimation;
    private Animation finalLevelOneDeathAnimation;

    private Vector2 velocity;

    public FinalLevelOne(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

       /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineFinalLevelOne() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, Constants.FINALLEVELONE_WIDTH_METERS, Constants.FINALLEVELONE_HEIGHT_METERS);
        defineFinalLevelOne();

        // By default this FinalLevelOne doesn't interact in our world
        b2body.setActive(false);

        // Textures
        finalLevelOneStand = Assets.instance.finalLevelOne.finalLevelOneStand;
        finalLevelOneWalkAnimation = Assets.instance.finalLevelOne.finalLevelOneWalkAnimation;
        finalLevelOneIdleAnimation = Assets.instance.finalLevelOne.finalLevelOneIdleAnimation;
        finalLevelOneShootAnimation = Assets.instance.finalLevelOne.finalLevelOneShootAnimation;
        finalLevelOneDeathAnimation = Assets.instance.finalLevelOne.finalLevelOneDeathAnimation;

        currentState = State.WALKING;
        damage = 0;
        stateTime = 0;

        velocity = new Vector2(MathUtils.randomSign() * Constants.FINALLEVELONE_LINEAR_VELOCITY, 0);
    }

    private void defineFinalLevelOne() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth() / 2 , getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS);
        fdef.filter.categoryBits = Constants.FINALLEVELONE_BIT; // Depicts what this fixture is
        fdef.filter.maskBits = Constants.BORDERS_BIT |
                            Constants.OBSTACLE_BIT |
                            Constants.HERO_WEAPON_BIT |
                            Constants.HERO_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt) {
        switch (currentState) {
            case WALKING:
                stateWalking(dt);
                break;
            case IDLE:
                stateIdle();
                break;
            case SHOOTING:
                stateShooting();
                break;
            case EXPLODING:
                stateExploding(dt);
                break;
            case DEAD:
                break;
            default:
                break;
        }
        checkBoundaries();
    }

    private void stateWalking(float dt) {
        b2body.setLinearVelocity(velocity);
        float vy = b2body.getLinearVelocity().y;
        float vx = b2body.getLinearVelocity().x;
        float x = b2body.getPosition().x;
        float y = b2body.getPosition().y;

        if (vy > 0.0f) {
            // Se mueve hacia arriba
            if (x < screen.gameCam.position.x) {
                // borde izquierdo
            } else {
                // borde derecho
            }
        } else if (vy < 0.0f) {
            // Se mueve hacia abajo
            if (x < screen.gameCam.position.x) {
                // borde izquierdo
            } else {
                // borde derecho
            }
        } else {
            if (vx != 0.0f) {
                if (vx > 0.0f) {
                    // Se mueve a la derecha
                    if (y < screen.gameCam.position.y) {
                        // borde inferior

                    } else {
                        // borde superior
                        flip(true, false);
                    }
                } else if (vx < 0.0f) {
                    // Se mueve a la izquierda
                    if (y < screen.gameCam.position.y) {
                        // borde inferior
                    } else {
                        // borde superior
                        flip(true, true);
                    }
                }
            } else {
                // esta quieto // vx == 0 && vy == 0
            }
        }



        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalLevelOneWalkAnimation.getKeyFrame(stateTime, true));
        stateTime += dt;
    }

    private void stateIdle() {

    }

    private void stateShooting() {

    }

    private void stateExploding(float dt) {

    }

    public void onHit() {
        /*
         * We must remove its b2body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.update.world.step(...).
         * No b2body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
//        currentState = State.INJURED;
    }

    public void onWall() {
        if (b2body.getPosition().x < screen.gameCam.position.x) {
            // pared izquerda
            //currentState = WALKING_LEFT;
        } else {
            // pared derecha
            //currentState = WALKING_RIGHT;
        }
    }

    public void draw(Batch batch) {
        if (currentState != State.DEAD) {
            super.draw(batch);
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    // This FinalLevelOne can be removed from our game
    public boolean isDisposable() {
        return currentState == State.DEAD;
    }

    // This FinalLevelOne doesn't have any b2body
    public boolean isDestroyed() {
        return currentState == State.DEAD || currentState == State.EXPLODING;
    }

    private void checkBoundaries() {
        /* When a FinalLevelOne is on camera, it activates (it can collide).
        * You have to be very careful because if the final level One enemy is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        if (!isDestroyed()) {
            float upperEdge = screen.gameCam.position.y + screen.gameViewPort.getWorldHeight() / 2;
            float bottomEdge = screen.gameCam.position.y - screen.gameViewPort.getWorldHeight() / 2;

            if (bottomEdge <= getY() + getHeight() && getY() <= upperEdge) {
                b2body.setActive(true);
            } else {
                if (b2body.isActive()) { // Was on camera...
                    // It's outside bottom edge
                    if (bottomEdge > getY() + getHeight()) {
                        world.destroyBody(b2body);
                        currentState = State.DEAD;
                    }
                }
            }
        }
    }
}
