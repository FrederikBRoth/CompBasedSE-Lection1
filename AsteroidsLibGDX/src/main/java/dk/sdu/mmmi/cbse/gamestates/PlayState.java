package dk.sdu.mmmi.cbse.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.entities.Asteroid;
import dk.sdu.mmmi.cbse.entities.Bullet;
import dk.sdu.mmmi.cbse.entities.Enemy;
import dk.sdu.mmmi.cbse.entities.Player;
import dk.sdu.mmmi.cbse.main.Game;
import dk.sdu.mmmi.cbse.managers.GameKeys;
import dk.sdu.mmmi.cbse.managers.GameStateManager;
import java.util.ArrayList;

public class PlayState extends GameState {

    private ShapeRenderer sr;

    private Player player, player2;
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Enemy> enemy;

    private int level;
    private int totalAsteroids;
    private int numAsteroidsLeft;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sr = new ShapeRenderer();
        bullets = new ArrayList<Bullet>();
        player = new Player(bullets);

        enemy = new ArrayList<Enemy>();

        asteroids = new ArrayList<Asteroid>();

        level = 1;
        spawnAsteroids();
        spawnEnemies();

    }

    private void spawnEnemies() {
        enemy.clear();

        int numToSpawn = 4;
        for (int i = 0; i < numToSpawn; i++) {
            float x = MathUtils.random(Game.WIDTH);
            float y = MathUtils.random(Game.HEIGHT);

            float dx = x - player.getx();
            float dy = x - player.gety();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            while (dist < 100) {
                x = MathUtils.random(Game.WIDTH);
                y = MathUtils.random(Game.HEIGHT);
                dx = x - player.getx();
                dy = x - player.gety();
                dist = (float) Math.sqrt(dx * dx + dy * dy);
            }
            System.out.println("HEJ");
            enemy.add(new Enemy(x, y));
        }

    }

    private void spawnAsteroids() {
        asteroids.clear();

        int numToSpawn = 4 + level - 1;
        totalAsteroids = numToSpawn * 7;

        numAsteroidsLeft = totalAsteroids;

        for (int i = 0; i < numToSpawn; i++) {
            float x = MathUtils.random(Game.WIDTH);
            float y = MathUtils.random(Game.HEIGHT);

            float dx = x - player.getx();
            float dy = x - player.gety();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            while (dist < 100) {
                x = MathUtils.random(Game.WIDTH);
                y = MathUtils.random(Game.HEIGHT);
                dx = x - player.getx();
                dy = x - player.gety();
                dist = (float) Math.sqrt(dx * dx + dy * dy);
            }

            asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
        }
    }

    public void update(float dt) {

        handleInput();

        player.update(dt);

        //update bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        //update asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).update(dt);
            if (asteroids.get(i).shouldRemove()) {
                asteroids.remove(i);
                i--;
            }
        }

        //update enemy
        for (int i = 0; i < enemy.size(); i++) {
            enemy.get(i).update(dt);

            if (enemy.get(i).shouldRemove()) {
                enemy.remove(i);
                i--;
            }
        }

        //check Collision
        checkCollisions();
    }

    private void checkCollisions() {
        //enemy bullet collision
        
        for(int i = 0; i < enemy.size(); i++){
            Enemy e = enemy.get(i);
            for(int k = 0; k < e.getEnemyBullets().size(); k++){
                Bullet b = e.getEnemyBullets().get(k);
                if(player.contains(b.getx(), b.gety())){
                    e.getEnemyBullets().remove(k);
                    k--;
                    player.hit();
                    break;
                }
            }
        }
        
        //bullet enemy collision

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int k = 0; k < enemy.size(); k++) {
                Enemy e = enemy.get(k);
                if (e.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;
                    enemy.remove(k);
                    break;

                }

            }
        }

        //player asteroid collision
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid a = asteroids.get(i);
            if (a.intersects(player)) {
                player.hit();
                asteroids.remove(i);
                i--;
                splitAsteroids(a);
                break;
            }
        }

        // bullet astyeroid collision
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int k = 0; k < asteroids.size(); k++) {
                Asteroid a = asteroids.get(k);
                if (a.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;
                    asteroids.remove(k);
                    splitAsteroids(a);
                    break;

                }
            }
        }
    }

    private void splitAsteroids(Asteroid a) {
        numAsteroidsLeft--;
        if (a.getType() == Asteroid.LARGE) {
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
        } else if (a.getType() == Asteroid.MEDIUM) {
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
            asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
        }
    }

    public void draw() {
        player.draw(sr);

        //draw bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sr);
        }

        //draw asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).draw(sr);
        }

        //draw enemies
        for (int i = 0; i < enemy.size(); i++) {
            enemy.get(i).draw(sr);
        }
    }

    public void handleInput() {
        player.setLeft(GameKeys.isDown(GameKeys.LEFT));
        player.setRight(GameKeys.isDown(GameKeys.RIGHT));
        player.setUp(GameKeys.isDown(GameKeys.UP));

        if (GameKeys.isPressed(GameKeys.SPACE)) {
            player.shoot();
        }
    }

    public void dispose() {
    }

}
