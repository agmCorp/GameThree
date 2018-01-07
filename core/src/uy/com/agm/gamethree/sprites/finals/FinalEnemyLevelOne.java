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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamethree.assets.Assets;
import uy.com.agm.gamethree.screens.PlayScreen;
import uy.com.agm.gamethree.sprites.weapons.EnemyBullet;
import uy.com.agm.gamethree.sprites.weapons.Weapon;
import uy.com.agm.gamethree.tools.AudioManager;
import uy.com.agm.gamethree.tools.Constants;
import uy.com.agm.gamethree.tools.GameThreeActorDef;
import uy.com.agm.gamethree.tools.Vector2Util;

/**
 * Created by AGM on 12/30/2017.
 */

public class FinalEnemyLevelOne extends Sprite {
    private static final String TAG = FinalEnemyLevelOne.class.getName();

    private World world;
    private PlayScreen screen;
    private Body b2body;

    private enum StateFinalEnemy {
        NOTACTIVE, WALKING, IDLE, SHOOTING, INJURED, DYING, EXPLODING, DEAD
    }

    private enum PowerState {
        NORMAL, POWERFUL
    }

    private enum StateWalking {
        CEILING_LEFT, CEILING_RIGHT,
        LEFT_UP, LEFT_DOWN,
        FLOOR_LEFT, FLOOR_RIGHT,
        RIGHT_UP, RIGHT_DOWN,
        SLASH_UP, SLASH_DOWN,
        BACKSLASH_UP, BACKSLASH_DOWN
    }

    private StateFinalEnemy currentStateFinalEnemy;
    private StateWalking currentStateWalking;
    private int damage;
    private float stateFinalEnemyTimer;
    private float timeToChangeTimer;
    private float timeToChange;
    private float openFireTimer;

    private Animation finalEnemyLevelOneWalkAnimation;
    private Animation finalEnemyLevelOneIdleAnimation;
    private Animation finalEnemyLevelOneShootAnimation;
    private Animation finalEnemyLevelOneDyingAnimation;

    private Vector2 velocity;
    private Vector2 tmp; // Temp GC friendly vector

    // Power FX
    private PowerState currentPowerState;
    private Animation powerFXAnimation;
    private float powerFXStateTimer;
    private Sprite powerFXSprite;

    // Explosion FX
    private Animation explosionFXAnimation;
    private float explosionFXStateTimer;
    private Sprite explosionFXSprite;

    public FinalEnemyLevelOne(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;

        /* Set this Sprite's bounds on the lower left vertex of a Rectangle.
        * This point will be used by defineFinalEnemyLevelOne() calling getX(), getY() to center its b2body.
        * SetBounds always receives world coordinates.
        */
        setBounds(x, y, Constants.FINALLEVELONE_WIDTH_METERS, Constants.FINALLEVELONE_HEIGHT_METERS);
        defineFinalEnemyLevelOne();

        // By default this FinalEnemyLevelOne doesn't interact in our world
        b2body.setActive(false);

        // Animations
        finalEnemyLevelOneWalkAnimation = Assets.instance.finalEnemyLevelOne.finalEnemyLevelOneWalkAnimation;
        finalEnemyLevelOneIdleAnimation = Assets.instance.finalEnemyLevelOne.finalEnemyLevelOneIdleAnimation;
        finalEnemyLevelOneShootAnimation = Assets.instance.finalEnemyLevelOne.finalEnemyLevelOneShootAnimation;
        finalEnemyLevelOneDyingAnimation = Assets.instance.finalEnemyLevelOne.finalEnemyLevelOneDeathAnimation;

        // FinalEnemyLevelOne variables initialization
        currentStateFinalEnemy = StateFinalEnemy.NOTACTIVE;
        damage = Constants.FINALLEVELONE_MAX_DAMAGE;
        stateFinalEnemyTimer = 0;
        timeToChangeTimer = 0;
        timeToChange = getNextTimeToChange();
        openFireTimer = Constants.FINALLEVELONE_FIRE_DELAY_SECONDS;

        // Place origin of rotation in the center of the Sprite
        setOriginCenter();

        // Initial movement (left or right)
        int direction = MathUtils.randomSign();
        velocity = new Vector2(direction * Constants.FINALLEVELONE_LINEAR_VELOCITY, 0);
        if (direction < 0) {
            currentStateWalking = StateWalking.CEILING_LEFT;
        } else {
            currentStateWalking = StateWalking.CEILING_RIGHT;
        }

        // Temp GC friendly vector
        tmp = new Vector2();

        // -------------------- PowerFX --------------------

        // PowerFX variables initialization
        currentPowerState = PowerState.NORMAL;
        powerFXAnimation = Assets.instance.finalEnemyLevelOnePower.AssetFinalEnemyLevelOnePowerAnimation;
        powerFXStateTimer = 0;

        // Set the power's texture
        powerFXSprite = new Sprite(Assets.instance.finalEnemyLevelOnePower.AssetFinalEnemyLevelOnePowerStand);

        // Only to set width and height of our spritePower (in powerStatePowerful(...) we set its position)
        powerFXSprite.setBounds(getX(), getY(), Constants.FINALLEVELONE_POWER_WIDTH_METERS, Constants.FINALLEVELONE_POWER_HEIGHT_METERS);

        // Place origin of rotation in the center of the Sprite
        powerFXSprite.setOriginCenter();

        // -------------------- ExplosionFX --------------------

        // ExplosionFX variables initialization
        explosionFXAnimation = Assets.instance.explosionE.explosionEAnimation;
        explosionFXStateTimer = 0;

        // Set the explosion's texture
        Sprite spriteExplosion = new Sprite(Assets.instance.explosionE.explosionEStand);

        // Only to set width and height of our spriteExplosion (in stateExploding(...) we set its position)
        spriteExplosion.setBounds(getX(), getY(), Constants.EXPLOSIONE_WIDTH_METERS, Constants.EXPLOSIONE_HEIGHT_METERS);

        // Explosion FX Sprite
        explosionFXSprite = new Sprite(spriteExplosion);

        // Place origin of rotation in the center of the Sprite
        explosionFXSprite.setOriginCenter();
    }

    private void defineFinalEnemyLevelOne() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth() / 2 , getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        setDefaultFilter();
    }

    private void setDefaultFilter() {
        Filter filter = new Filter();
        filter.categoryBits = Constants.FINAL_ENEMY_LEVEL_ONE_BIT; // Depicts what this fixture is
        filter.maskBits = Constants.BORDERS_BIT |
                Constants.EDGES_BIT |
                Constants.OBSTACLE_BIT |
                Constants.HERO_WEAPON_BIT |
                Constants.SHIELD_BIT |
                Constants.HERO_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    private float getNextTimeToChange() {
        return MathUtils.random(0.0f, Constants.FINALLEVELONE_STATE_MAX_DELAY_SECONDS);
    }

    private StateFinalEnemy getNewRandomState(float dt) {
        boolean blnOption;
        StateFinalEnemy newRandomStateFinalEnemy = currentStateFinalEnemy;

        // Update timer
        timeToChangeTimer += dt;

        // Set a new currentStateFinalEnemy
        if (timeToChangeTimer >= timeToChange) {
            // Reset random state variables
            timeToChangeTimer = 0;
            timeToChange = getNextTimeToChange();

            // Reset variable animation
            stateFinalEnemyTimer = 0;

            // Random option
            blnOption = MathUtils.randomBoolean();

            // Decide which state must return
            switch (currentStateFinalEnemy) {
                case WALKING:
                    if (blnOption) {
                        newRandomStateFinalEnemy = StateFinalEnemy.IDLE;
                    } else {
                        newRandomStateFinalEnemy = StateFinalEnemy.SHOOTING;
                        openFireTimer = Constants.FINALLEVELONE_FIRE_DELAY_SECONDS;
                    }
                    break;
                case IDLE:
                    if (blnOption) {
                        newRandomStateFinalEnemy = StateFinalEnemy.WALKING;
                    } else {
                        newRandomStateFinalEnemy = StateFinalEnemy.SHOOTING;
                        openFireTimer = Constants.FINALLEVELONE_FIRE_DELAY_SECONDS;
                    }
                    break;
                case SHOOTING:
                    if (blnOption) {
                        newRandomStateFinalEnemy = StateFinalEnemy.WALKING;
                    } else {
                        newRandomStateFinalEnemy = StateFinalEnemy.IDLE;
                    }
                    break;
            }
        }
        return newRandomStateFinalEnemy;
    }

    public void update(float dt) {
        if (currentStateFinalEnemy == StateFinalEnemy.NOTACTIVE) {
            // When our final enemy is on camera, it activates
            checkBoundaries();
            if (b2body.isActive()) {
                currentStateFinalEnemy = StateFinalEnemy.WALKING;
                updateLogic(dt);
            }
        } else {
            updateLogic(dt);
        }
    }

    public void updateLogic(float dt) {
        switch (currentStateFinalEnemy) {
            case WALKING:
                stateWalking(dt);
                break;
            case IDLE:
                stateIdle(dt);
                break;
            case SHOOTING:
                stateShooting(dt);
                break;
            case INJURED:
                stateInjured(dt);
                break;
            case DYING:
                stateDying(dt);
                break;
            case EXPLODING:
                stateExploding(dt);
                break;
            case DEAD:
                break;
            default:
                break;
        }

        // FinalEnemyLevelOne could have been destroyed
        if (!isDestroyed()) {
            switchPowerState(dt);
        }
    }

    private void switchPowerState(float dt) {
        switch (currentPowerState) {
            case NORMAL:
                powerStateNormalToPowerful();
                break;
            case POWERFUL:
                powerStatePowerfulToNormal(dt);
                break;
            default:
                break;
        }
    }

    private void stateWalking(float dt) {
        // Set velocity because It could have been changed (see onHitWall)
        b2body.setLinearVelocity(velocity);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalEnemyLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelOneWalkAnimation.getKeyFrame(stateFinalEnemyTimer, true));
        stateFinalEnemyTimer += dt;

        // Depending on the direction, we set the angle and the flip
        switch (currentStateWalking) {
            case CEILING_LEFT:
                setRotation(0);
                setFlip(false, true);
                velocity.set(-Constants.FINALLEVELONE_LINEAR_VELOCITY, 0);
                break;
            case CEILING_RIGHT:
                setRotation(0);
                setFlip(true, true);
                velocity.set(Constants.FINALLEVELONE_LINEAR_VELOCITY, 0);
                break;
            case LEFT_DOWN:
                setRotation(90);
                setFlip(false, true);
                velocity.set(0, -Constants.FINALLEVELONE_LINEAR_VELOCITY);
                break;
            case LEFT_UP:
                setRotation(90);
                setFlip(true, true);
                velocity.set(0, Constants.FINALLEVELONE_LINEAR_VELOCITY);
                break;
            case RIGHT_DOWN:
                setRotation(90);
                setFlip(false, false);
                velocity.set(0, -Constants.FINALLEVELONE_LINEAR_VELOCITY);
                break;
            case RIGHT_UP:
                setRotation(90);
                setFlip(true, false);
                velocity.set(0, Constants.FINALLEVELONE_LINEAR_VELOCITY);
                break;
            case FLOOR_LEFT:
                setRotation(0);
                setFlip(false, false);
                velocity.set(-Constants.FINALLEVELONE_LINEAR_VELOCITY, 0);
                break;
            case FLOOR_RIGHT:
                setRotation(0);
                setFlip(true, false);
                velocity.set(Constants.FINALLEVELONE_LINEAR_VELOCITY, 0);
                break;
            case SLASH_DOWN:
                // It's not exactly 45 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(45);
                setFlip(false, false);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getBottomEdge().getB2body().getPosition().x - Constants.EDGE_WIDTH_METERS / 2 +
                                Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS / 2,
                                screen.getBottomEdge().getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2,
                                Constants.FINALLEVELONE_LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
            case SLASH_UP:
                // It's not exactly 45 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(45);
                setFlip(true, true);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getUpperEdge().getB2body().getPosition().x + Constants.EDGE_WIDTH_METERS / 2 -
                                Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS / 2,
                                screen.getUpperEdge().getB2body().getPosition().y - Constants.EDGE_HEIGHT_METERS / 2,
                                Constants.FINALLEVELONE_LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
            case BACKSLASH_DOWN:
                // It's not exactly 135 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(135);
                setFlip(false, false);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getBottomEdge().getB2body().getPosition().x + Constants.EDGE_WIDTH_METERS / 2 -
                                Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS / 2,
                                screen.getBottomEdge().getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2,
                                Constants.FINALLEVELONE_LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
            case BACKSLASH_UP:
                // It's not exactly 135 degrees because we are walking along the diagonal of a rectangle
                // But it does the trick
                setRotation(135);
                setFlip(true, true);
                tmp.set(b2body.getPosition().x, b2body.getPosition().y);
                Vector2Util.goToTarget(tmp, screen.getUpperEdge().getB2body().getPosition().x - Constants.EDGE_WIDTH_METERS / 2 +
                                Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS / 2,
                                screen.getUpperEdge().getB2body().getPosition().y - Constants.EDGE_HEIGHT_METERS / 2,
                                Constants.FINALLEVELONE_LINEAR_VELOCITY);
                velocity.set(tmp);
                break;
        }

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateIdle(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        // Preserve the flip and rotation state
        boolean isFlipX = isFlipX();
        boolean isFlipY = isFlipY();
        float rotation = getRotation();

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalEnemyLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelOneIdleAnimation.getKeyFrame(stateFinalEnemyTimer, true));
        stateFinalEnemyTimer += dt;

        // Apply previous flip and rotation state
        setFlip(isFlipX, isFlipY);
        setRotation(rotation);

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void stateShooting(float dt) {
        // Stop
        b2body.setLinearVelocity(0.0f, 0.0f);

        /* Update our Sprite to correspond with the position of our Box2D body:
        * Set this Sprite's position on the lower left vertex of a Rectangle determined by its b2body to draw it correctly.
        * At this time, FinalEnemyLevelOne may have collided with sth., and therefore, it has a new position after running the physical simulation.
        * In b2box the origin is at the center of the body, so we must recalculate the new lower left vertex of its bounds.
        * GetWidth and getHeight was established in the constructor of this class (see setBounds).
        * Once its position is established correctly, the Sprite can be drawn at the exact point it should be.
         */
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) finalEnemyLevelOneShootAnimation.getKeyFrame(stateFinalEnemyTimer, true));
        stateFinalEnemyTimer += dt;

        // Calculate shooting angle
        float angle = tmp.set(screen.getPlayer().getB2body().getPosition().x, screen.getPlayer().getB2body().getPosition().y)
                .sub(b2body.getPosition().x, b2body.getPosition().y).angle();
        setRotation(angle);
        setFlip(true, true);

        // If is time to shoot we open fire
        if (openFireTimer >= Constants.FINALLEVELONE_FIRE_DELAY_SECONDS) {
            openFire();
            openFireTimer = 0;
        } else {
            openFireTimer += dt;
        }

        // New random state
        currentStateFinalEnemy = getNewRandomState(dt);
    }

    private void openFire() {
        screen.getCreator().createGameThreeActor(new GameThreeActorDef(b2body.getPosition().x, b2body.getPosition().y, EnemyBullet.class));
    }

    private void stateInjured(float dt) {
        // Destroy box2D body
        world.destroyBody(b2body);

        // Stop music
        AudioManager.instance.stopMusic();

        // Death animation
        stateFinalEnemyTimer = 0;

        // Audio FX // // TODO: 3/1/2018  buscar un audio para cuando comienza a morir
        AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));

        // Set score
        screen.getHud().addScore(Constants.FINALLEVELONE_SCORE);

        // Set the new state
        currentStateFinalEnemy = StateFinalEnemy.DYING;
    }

    private void stateDying(float dt) {
        if (finalEnemyLevelOneDyingAnimation.isAnimationFinished(stateFinalEnemyTimer)) {
            // Exploding animation
            explosionFXStateTimer = 0;

            // Audio FX // // TODO: 3/1/2018  buscar un audio para cuando comienza a explotar
            AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));

            // Set the new state
            currentStateFinalEnemy = StateFinalEnemy.EXPLODING;
        } else {
            // Preserve the flip and rotation state
            boolean isFlipX = isFlipX();
            boolean isFlipY = isFlipY();
            float rotation = getRotation();

            setRegion((TextureRegion) finalEnemyLevelOneDyingAnimation.getKeyFrame(stateFinalEnemyTimer, true));
            stateFinalEnemyTimer += dt;

            // Apply previous flip and rotation state
            setFlip(isFlipX, isFlipY);
            setRotation(rotation);
        }
    }

   private void stateExploding(float dt) {
       if (explosionFXAnimation.isAnimationFinished(explosionFXStateTimer)) {
           // Audio FX // // TODO: 3/1/2018  buscar un audio para cuando termina de explotar, tipo música exitosa.
           AudioManager.instance.play(Assets.instance.sounds.hit, 1, MathUtils.random(1.0f, 1.1f));

           // Set the new state
           currentStateFinalEnemy = StateFinalEnemy.DEAD;
       } else {
           // Animation
           explosionFXSprite.setRegion((TextureRegion) explosionFXAnimation.getKeyFrame(explosionFXStateTimer, true));
           explosionFXStateTimer += dt;

           // Apply rotation and flip of the main character
           explosionFXSprite.setRotation(getRotation());
           explosionFXSprite.setFlip(isFlipX(), isFlipY());

           // Get center of main character bounding rectangle
           getBoundingRectangle().getCenter(tmp);

           // Center the Sprte with respect to the center of FinalEnemyLevelOne
           explosionFXSprite.setPosition( tmp.x - explosionFXSprite.getWidth() / 2, tmp.y - explosionFXSprite.getHeight() / 2);
       }
    }

    private void powerStatePowerfulToNormal(float dt) {
        // If our final enemy is not walking nor shooting, he becomes weak
        if (currentStateFinalEnemy != StateFinalEnemy.WALKING && currentStateFinalEnemy != StateFinalEnemy.SHOOTING) {
            powerFXStateTimer = 0;
            currentPowerState = PowerState.NORMAL;
            AudioManager.instance.play(Assets.instance.sounds.finalLevelOnePowerDown, 1);
        } else {
            // Animation
            powerFXSprite.setRegion((TextureRegion) powerFXAnimation.getKeyFrame(powerFXStateTimer, true));
            powerFXStateTimer += dt;

            // Apply rotation and flip of the main character
            powerFXSprite.setRotation(getRotation());
            powerFXSprite.setFlip(isFlipX(), isFlipY());

            // Update our Sprite to correspond with the position of our finalEnemyLevelOne's Box2D body:
            powerFXSprite.setPosition(b2body.getPosition().x - powerFXSprite.getWidth() / 2, b2body.getPosition().y - powerFXSprite.getHeight() / 2);
        }
    }

    private void powerStateNormalToPowerful() {
        // If our final enemy is walking or shooting, he becomes powerful
        if (currentStateFinalEnemy == StateFinalEnemy.WALKING || currentStateFinalEnemy == StateFinalEnemy.SHOOTING) {
            powerFXStateTimer = 0;
            currentPowerState = PowerState.POWERFUL;
            AudioManager.instance.play(Assets.instance.sounds.finalLevelOnePowerUp, 1);
        }
    }

    public void onHit(Weapon weapon) {
        if (currentStateFinalEnemy == StateFinalEnemy.IDLE) {
            weapon.onTarget();
            damage--;
            AudioManager.instance.play(Assets.instance.sounds.openPowerBox, 1); // todo BUSCAR AUDIO PARA CUANDO SE MORFA UN BALAZO
            if (damage <= 0) {
                currentStateFinalEnemy = StateFinalEnemy.INJURED;
            }
        } else {
            weapon.onBounce();
        }
    }

    public void onHitWall() {
        if (currentStateFinalEnemy == StateFinalEnemy.WALKING) {
            float velY = b2body.getLinearVelocity().y;
            float velX = b2body.getLinearVelocity().x;
            float x = b2body.getPosition().x;
            float y = b2body.getPosition().y;
            int option = MathUtils.random(1, 3);

            if (velY > 0.0f) {
                if (x < screen.gameCam.position.x) {
                    // We are walking UP along the LEFT EDGE
                    switch (option) {
                        case 1: // go back
                            currentStateWalking = StateWalking.LEFT_DOWN;
                            break;
                        case 2:
                            currentStateWalking = StateWalking.CEILING_RIGHT;
                            break;
                        case 3:
                            currentStateWalking = StateWalking.BACKSLASH_DOWN;
                            break;
                    }
                } else {
                    // We are walking UP along the RIGHT EDGE
                    switch (option) {
                        case 1: // go back
                            currentStateWalking = StateWalking.RIGHT_DOWN;
                            break;
                        case 2:
                            currentStateWalking = StateWalking.CEILING_LEFT;
                            break;
                        case 3:
                            currentStateWalking = StateWalking.SLASH_DOWN;
                            break;
                    }
                }
            } else {
                if (velY < 0.0f) {
                    if (x < screen.gameCam.position.x) {
                        // We are walking DOWN along the LEFT EDGE
                        switch (option) {
                            case 1: // go back
                                currentStateWalking = StateWalking.LEFT_UP;
                                break;
                            case 2:
                                currentStateWalking = StateWalking.FLOOR_RIGHT;
                                break;
                            case 3:
                                currentStateWalking = StateWalking.SLASH_UP;
                                break;
                        }
                    } else {
                        // We are walking DOWN along the RIGHT EDGE
                        switch (option) {
                            case 1: // go back
                                currentStateWalking = StateWalking.RIGHT_UP;
                                break;
                            case 2:
                                currentStateWalking = StateWalking.FLOOR_LEFT;
                                break;
                            case 3:
                                currentStateWalking = StateWalking.BACKSLASH_UP;
                                break;
                        }
                    }
                } else {
                    if (velX != 0.0f) { // velY == 0
                        if (velX > 0.0f) {
                            if (y < screen.gameCam.position.y) {
                                // We are walking to the RIGHT along the FLOOR EDGE
                                switch (option) {
                                    case 1: // go back
                                        currentStateWalking = StateWalking.FLOOR_LEFT;
                                        break;
                                    case 2:
                                        currentStateWalking = StateWalking.RIGHT_UP;
                                        break;
                                    case 3:
                                        currentStateWalking = StateWalking.BACKSLASH_UP;
                                        break;
                                }
                            } else {
                                // We are walking to the RIGHT along the CEILING EDGE
                                switch (option) {
                                    case 1: // go back
                                        currentStateWalking = StateWalking.CEILING_LEFT;
                                        break;
                                    case 2:
                                        currentStateWalking = StateWalking.RIGHT_DOWN;
                                        break;
                                    case 3:
                                        currentStateWalking = StateWalking.SLASH_DOWN;
                                        break;
                                }
                            }
                        } else if (velX < 0.0f) {
                            if (y < screen.gameCam.position.y) {
                                // We are walking to the LEFT along the FLOOR EDGE
                                switch (option) {
                                    case 1: // go back
                                        currentStateWalking = StateWalking.FLOOR_RIGHT;
                                        break;
                                    case 2:
                                        currentStateWalking = StateWalking.LEFT_UP;
                                        break;
                                    case 3:
                                        currentStateWalking = StateWalking.SLASH_UP;
                                        break;
                                }
                            } else {
                                // We are walking to the LEFT along the CEILING EDGE
                                switch (option) {
                                    case 1: // go back
                                        currentStateWalking = StateWalking.CEILING_RIGHT;
                                        break;
                                    case 2:
                                        currentStateWalking = StateWalking.LEFT_DOWN;
                                        break;
                                    case 3:
                                        currentStateWalking = StateWalking.BACKSLASH_DOWN;
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isDrawable() {
        return currentStateFinalEnemy != StateFinalEnemy.DEAD &&
                currentStateFinalEnemy != StateFinalEnemy.EXPLODING &&
                currentStateFinalEnemy != StateFinalEnemy.NOTACTIVE;
    }

    private void drawPowers(Batch batch) {
        if (currentPowerState == PowerState.POWERFUL) {
            powerFXSprite.draw(batch);
        }
    }

    private void drawExplosion(Batch batch) {
        if (currentStateFinalEnemy == StateFinalEnemy.EXPLODING) {
            explosionFXSprite.draw(batch);
        }
    }

    public void draw(Batch batch) {
        // We draw FinalEnemyLevelOne in these states: WALKING IDLE SHOOTING INJURED DYING
        if (isDrawable()) {
            super.draw(batch);
            drawPowers(batch);
        } else {
            drawExplosion(batch);
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    // This FinalEnemyLevelOne can be removed from our game
    public boolean isDisposable() {
        return currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    // This FinalEnemyLevelOne doesn't have any b2body
    public boolean isDestroyed() {
        return currentStateFinalEnemy == StateFinalEnemy.DYING ||
                currentStateFinalEnemy == StateFinalEnemy.EXPLODING ||
                currentStateFinalEnemy == StateFinalEnemy.DEAD;
    }

    private void checkBoundaries() {
        /* When a FinalEnemyLevelOne is on camera, it activates (it can collide).
        * You have to be very careful because if the final level One enemy is destroyed, its b2body does not exist and gives
        * random errors if you try to active it.
        */
        float upperEdge = screen.getUpperEdge().getB2body().getPosition().y + Constants.EDGE_HEIGHT_METERS / 2; //  Upper edge of the upperEdge :)

        if (upperEdge > b2body.getPosition().y + Constants.FINALLEVELONE_CIRCLESHAPE_RADIUS_METERS) {
            b2body.setActive(true);
        }
    }
}
