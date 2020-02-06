/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author fredd
 */
public class Enemy extends SpaceObject {

    public Enemy(float x, float y) {
        this.x = x;
        this.y = y;

        rotationSpeed = MathUtils.random(-1, 1);

        radians = MathUtils.random(2 * (float) Math.PI);

        speed = 20;
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        shapex = new float[4];
        shapey = new float[4];

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

    public void update(float dt) {

        x += dx * dt;
        y += dy * dt;
        radians += rotationSpeed * dt;
        setShape();
        wrap();
        // turning
//        if (left) {
//            radians += rotationSpeed * dt;
//        } else if (right) {
//            radians -= rotationSpeed * dt;
//        }
//
//        // accelerating
//        if (up) {
//            dx += MathUtils.cos(radians) * acceleration * dt;
//            dy += MathUtils.sin(radians) * acceleration * dt;
//            acceleratingTimer += dt;
//            if (acceleratingTimer > 0.1f) {
//                acceleratingTimer = 0;
//            }
//        } else {
//            acceleratingTimer = 0;
//        }
//
//        // deceleration
//        float vec = (float) Math.sqrt(dx * dx + dy * dy);
//        if (vec > 0) {
//            dx -= (dx / vec) * deceleration * dt;
//            dy -= (dy / vec) * deceleration * dt;
//        }
//        if (vec > maxSpeed) {
//            dx = (dx / vec) * maxSpeed;
//            dy = (dy / vec) * maxSpeed;
//        }
//
//        // set position
//        x += dx * dt;
//        y += dy * dt;
//
//        // set shape
//        setShape();
//
//        //set flame
//        // screen wrap
//        wrap();
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(255, 1, 1, 1);

        sr.begin(ShapeRenderer.ShapeType.Line);

        //draw shio
        for (int i = 0, j = shapex.length - 1;
                i < shapex.length;
                j = i++) {

            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

        }
        sr.end();
    }
}
