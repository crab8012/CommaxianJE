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

public class Laser {
    public Texture texture;
    public Sprite sprite;

    private float moveDirection;


    public Laser(){
        this.texture = new Texture("defaultTex.png");
        this.sprite = new Sprite(this.texture);
        this.moveDirection = 2.0f;
    }

    public Laser(Vector2 location, Texture texture){
        this.texture = texture;
        this.sprite = new Sprite(this.texture);
        this.setLocation(location);
        this.moveDirection = 2.0f;
    }

    // Make sure the Sprite represents the rest of the object
    private void updateLaser(){
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
        updateLaser();
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
    public void move(float y){
        this.sprite.translate(0, y);
    }
}
