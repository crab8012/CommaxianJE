package us.bluesakuradev.commaxian;
/**
 * Name: Jordan Petersen
 * Date: December 11, 2021
 * Assignment: Term Project
 * Class: CSC 240 C40 - Java Programming
 * Purpose: Entertain a bored person
 */
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    public Texture texture;
    public Sprite sprite;

    private float moveDirection;


    public Enemy(){
        this.texture = new Texture("defaultTex.png");
        this.sprite = new Sprite(this.texture);
        this.moveDirection = 2.0f;
    }

    public Enemy(Vector2 location, Texture texture){
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
        this.setLocation(location);
        this.moveDirection = 2.0f;
    }

    // Make sure the Sprite represents the rest of the object
    private void updateEnemy(){
        this.sprite.setTexture(this.texture);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getLocation(){
        return new Vector2(this.sprite.getX(), this.sprite.getY());
    }
    public void setLocation(Vector2 newLocation){
        this.sprite.setPosition(newLocation.x, newLocation.y);
    }

    public Texture getTexture(){
        return this.texture;
    }

    public void setTexture(Texture newTexture){
        this.texture = newTexture;
        updateEnemy();
    }

    public float getMoveDirection(){
        return this.moveDirection;
    }

    public void setMoveDirection(float newDirection){
        this.moveDirection = newDirection;
    }

    public void move(float x, float y){
        this.sprite.translate(x, y);
    }

    public void moveOver(int xmin, int xmax){
        // Get the enemy to move how it is supposed to, with bounds checking
        if(this.getLocation().x <= xmin || this.getLocation().x > xmax){
            this.move(0, -32); // Move down a whole position
            this.setMoveDirection(this.getMoveDirection() * -1); // Reverse the movement direction
        }
        // Now we move in our horizontal move direction
        this.move(this.getMoveDirection(), 0);
    }
}
