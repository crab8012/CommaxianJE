package us.bluesakuradev.commaxian;
/**
 * Name: Jordan Petersen
 * Date: December 11, 2021
 * Assignment: Term Project
 * Class: CSC 240 C40 - Java Programming
 * Purpose: Entertain a bored person
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    public Texture texture;
    public Sprite sprite;

    private int movementMultiplier = 50;

    public Player(){
        this.texture = new Texture("defaultTex.png");
        this.sprite = new Sprite(this.texture);
        this.sprite.setPosition(0, 0);
    }

    public Player(Vector2 position, Texture texture){
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
        this.sprite.setPosition(position.x, position.y);
    }

    public Texture getTexture(){
        return this.texture;
    }

    public void setTexture(Texture newTexture){
        this.texture = newTexture;
        this.sprite.setTexture(this.texture);
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public Vector2 getPosition(){
        return new Vector2(this.sprite.getX(), this.sprite.getY());
    }

    public void setPosition(Vector2 newPosition){
        this.sprite.setPosition(newPosition.x, newPosition.y);
    }

    public void move(float x, float y){
        this.sprite.translate(x, y);
        //this.sprite.setPosition(this.sprite.getX() + x, this.sprite.getY() + y);
    }

    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){ // If the left arrow is pressed
            if(getPosition().x > 10){ // If the player is within its movement bounds...
                this.move(-0.25f*movementMultiplier, 0.0f); // Move it to the left
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){ // If the right arrow is pressed
            if(getPosition().x < 600){ // If the player is within its movement bounds...
                this.move(0.25f*movementMultiplier, 0.0f); // Move it to the right
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){ // If the space bar is pressed...
            // Fire the laser
        }
    }
}
