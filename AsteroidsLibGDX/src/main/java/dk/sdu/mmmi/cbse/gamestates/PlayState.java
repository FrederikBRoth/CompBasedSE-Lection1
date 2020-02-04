package dk.sdu.mmmi.cbse.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.entities.Asteroid;
import dk.sdu.mmmi.cbse.entities.Bullet;
import dk.sdu.mmmi.cbse.entities.Player;
import dk.sdu.mmmi.cbse.managers.GameKeys;
import dk.sdu.mmmi.cbse.managers.GameStateManager;
import java.util.ArrayList;

public class PlayState extends GameState {
	
	private ShapeRenderer sr;
	
	private Player player;
	private ArrayList<Bullet> bullets;
        private ArrayList<Asteroid> asteroids;
        
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
                
                asteroids = new ArrayList<Asteroid>();
                asteroids.add(new Asteroid(100, 100, Asteroid.LARGE));
		
	}
	
	public void update(float dt) {
		
		handleInput();
		
		player.update(dt);
		
                //update bullets
                for(int i = 0; i < bullets.size(); i++){
                    bullets.get(i).update(dt);
                    if(bullets.get(i).shouldRemove()){
                        bullets.remove(i);
                        i--;
                    }
                }
                
                //update asteroids
                
                for(int i = 0; i < asteroids.size(); i++){
                    asteroids.get(i).update(dt);
                    if(asteroids.get(i).shouldRemove()){
                        asteroids.remove(i);
                        i--;
                    }
                }
	}
	
	public void draw() {
		player.draw(sr);
                
                //draw bullets
                for(int i = 0; i < bullets.size(); i++){
                    bullets.get(i).draw(sr);
                }
                
                //draw asteroids
                for(int i = 0; i < asteroids.size(); i++){
                    asteroids.get(i).draw(sr);
                }
	}
	
	public void handleInput() {
		player.setLeft(GameKeys.isDown(GameKeys.LEFT));
		player.setRight(GameKeys.isDown(GameKeys.RIGHT));
		player.setUp(GameKeys.isDown(GameKeys.UP));
                
                if(GameKeys.isPressed(GameKeys.SPACE)) {
                    player.shoot();
                }
	}
	
	public void dispose() {}
	
}









