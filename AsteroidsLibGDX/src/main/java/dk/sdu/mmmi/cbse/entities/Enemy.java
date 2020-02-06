/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;

/**
 *
 * @author fredd
 */
public class Enemy extends SpaceObject {

    private static final int MAX_BULLETS = 4;
    //If true clockwise, false counter-clockwise
    private boolean turnDirection = true;
    private float timer;
    private float accelerationTimer;
    private int acceleration;
    private int deceleration;
    private int maxSpeed;
    private boolean remove;
    private ArrayList<Bullet> enemyBullets;

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;

        radians = MathUtils.random(2 * (float) Math.PI);

        rotationSpeed = 3;
        acceleration = 25;
        deceleration = 20;
        maxSpeed = 50;

        enemyBullets = new ArrayList<Bullet>();
        shapex = new float[4];
        shapey = new float[4];
        timer = 0;

    }

    private void setShape() {
        shapex[0] = x + MathUtils.cos(radians) * 8;
        shapey[0] = y + MathUtils.sin(radians) * 8;

        shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
        shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * 8;

        shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
        shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;

        shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
        shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 8;
    }

    public void shoot() {
        if (enemyBullets.size() == MAX_BULLETS) {
            return;
        }
        enemyBullets.add(new Bullet(x, y, radians));
    }

    public void update(float dt) {
        timer += dt;
        dx += MathUtils.cos(radians) * acceleration * dt;
        dy += MathUtils.sin(radians) * acceleration * dt;
        accelerationTimer += dt;
        if (accelerationTimer > 0.1f) {
            accelerationTimer = 0;
        }

        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }

//        if (timer > 3) {
//            if (turnDirection) {
//                radians -= rotationSpeed * dt;
//            } else {
//                radians += rotationSpeed * dt;
//            }
//        }
        if (timer > 3.5f) {
            timer = 0;
//            turnDirection = Math.random() < 0.5 ? true : false;
            shoot();
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).update(dt);
            if (enemyBullets.get(i).shouldRemove()) {
                enemyBullets.remove(i);
                i--;
            }
        }

        x += dx * dt;
        y += dy * dt;
        setShape();
        wrap();
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean shouldRemove() {
        return remove;
    }
    
    
    public ArrayList<Bullet> getEnemyBullets() {
        return enemyBullets;
    }


    public void draw(ShapeRenderer sr) {
        sr.setColor(0.5f, 0.5f, 0, 1);

        sr.begin(ShapeRenderer.ShapeType.Line);

        //draw shio
        for (int i = 0, j = shapex.length - 1;
                i < shapex.length;
                j = i++) {

            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

        }

        sr.end();
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).draw(sr);
        }
    }
}
