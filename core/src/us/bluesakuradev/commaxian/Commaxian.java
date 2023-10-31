package us.bluesakuradev.commaxian;
/**
 * Name: Jordan Petersen
 * Date: December 11, 2021
 * Assignment: Term Project
 * Class: CSC 240 C40 - Java Programming
 * Purpose: Entertain a bored person
 */
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class Commaxian extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	// An enum to help keep track of where we are in the game.
	// This also helps keep track of what we need to render
	private enum gameState{
		MENU, PLAY, PAUSE, END
	}
	private gameState currentGameState = gameState.MENU;

	// Variables to hold all loaded assets
	Texture playerTex, enemyTex, laserTex, bgTex;
	Sound shootFX, menuSelectFX, explodeFX;
	Music bgMusic;
	Label scoreTxt, attributionTxt, gameOverTxt, titleTxt, instructionsTxt, pauseTxt;

	// Fonts
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	FreeTypeFontGenerator instructionFontGenerator;
	FreeTypeFontGenerator menuFontGenerator;
	BitmapFont menuFont, instructionFont, smallMenuFont, scoreFont, largeInstructionFont;

	// Actors (entities in the game)
	private Player player;
	private ArrayList<Enemy> enemies;

	// Different stages for different Game States
	private Stage menuStage;
	private Stage gameStage;
	private Stage pauseStage;
	private Stage endStage;

	// GLOBAL DEBOUNCE VARIABLES
	boolean qPressed = false, spacePressed = false, yPressed = false, nPressed = false, enterPressed = false;

	// Variables to help control the rate of movement of the enemies. Framerate based.
	int numSteps = 0, stepsToWait = 5;

	// Rectangle to define where the game will end if enemies get hit by it
	Rectangle killZone = new Rectangle(0, 0, 640, 50);

	// Rectangle to define where a laser will despawn.
	Rectangle laserDeathZone = new Rectangle(0, 450, 640, 30);

	// Temporary Laser Object
	Laser tempLaser = null; // If null, a new laser can be created.

	// The player's score.
	static int score = 0;

	// The scroll position of the background
	static int bgY = 0;

	@Override
	public void create () {
		// Create the camera to show a constantly sized scene
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		// Create the four main stages for the game
		menuStage = new Stage(new ScreenViewport());
		gameStage = new Stage(new ScreenViewport());
		pauseStage = new Stage(new ScreenViewport());
		endStage = new Stage(new ScreenViewport());

		// Create a new SpriteBatch to render all necessary sprites.
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined); // Set the sprite batch to render using the OrthographicCamera

		loadTextures();        // Load in all textures used by the game.
		loadSounds();          // Load in all audio used in the game
		generateFonts();       // Generate all font related stuff for the labels and UI.
		createLabelsAndText(); // Create all labels and text for the user interface.


		// Create the Player
		player = new Player(new Vector2(100, 32),  playerTex);

		// Create the enemies
		enemies = new ArrayList<>();
		for(int i = 0; i < 10; i++){
			Enemy e = new Enemy(gridToScreen(i + 2, 12), enemyTex);
			e.getSprite().setColor(Color.RED);
			e.getSprite().setRotation(180.0f);
			e.setMoveDirection(32.0f);
			enemies.add(e);
		}
		for(int i = 0; i < 10; i++){
			Enemy e = new Enemy(gridToScreen(i + 2, 10), enemyTex);
			e.getSprite().setColor(Color.RED);
			e.getSprite().setRotation(180.0f);
			e.setMoveDirection(-32.0f);
			enemies.add(e);
		}
	}

	public void loadTextures(){
		// Load in all textures
		playerTex = new Texture("textures/player.png");
		laserTex = new Texture("textures/laser.png");
		enemyTex = new Texture("textures/player.png");

		bgTex = new Texture("textures/bg.png");
	}

	public void loadSounds(){
		// Load in all sound effects
		shootFX = Gdx.audio.newSound(Gdx.files.internal("sfx/shoot.wav"));
		menuSelectFX = Gdx.audio.newSound(Gdx.files.internal("sfx/select.wav"));
		explodeFX = Gdx.audio.newSound(Gdx.files.internal("sfx/explode.wav"));

		// Load in all background music
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/a.ogg"));
		bgMusic.setLooping(true); // Make the background music loop.
	}

	public void generateFonts(){
		// Load in all necessary fonts
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		instructionFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-BoldItalic.ttf"));
		menuFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SharpRetro.ttf"));

		// Generate more specific fonts
		fontParameter.size = 100;
		menuFont = menuFontGenerator.generateFont(fontParameter);

		fontParameter.size = 20;
		smallMenuFont = menuFontGenerator.generateFont(fontParameter);

		fontParameter.size = 30;
		scoreFont = menuFontGenerator.generateFont(fontParameter);

		fontParameter.size = 25;
		instructionFont = instructionFontGenerator.generateFont(fontParameter);

		fontParameter.size = 100;
		largeInstructionFont = instructionFontGenerator.generateFont(fontParameter);
	}

	public void createLabelsAndText(){
		// Generate Label Styles
		Label.LabelStyle whiteMenuLabelStyle = new Label.LabelStyle();
		whiteMenuLabelStyle.font = menuFont;
		whiteMenuLabelStyle.fontColor = Color.WHITE;

		Label.LabelStyle smallWhiteMenuLabelStyle = new Label.LabelStyle();
		smallWhiteMenuLabelStyle.font = smallMenuFont;
		smallWhiteMenuLabelStyle.fontColor = Color.WHITE;

		Label.LabelStyle scoreLabelStyle = new Label.LabelStyle();
		scoreLabelStyle.font = scoreFont;
		scoreLabelStyle.fontColor = Color.WHITE;

		Label.LabelStyle whiteInstructionLabelStyle = new Label.LabelStyle();
		whiteInstructionLabelStyle.font = instructionFont;
		whiteInstructionLabelStyle.fontColor = Color.WHITE;

		Label.LabelStyle largeWhiteInstructionLabelStyle = new Label.LabelStyle();
		largeWhiteInstructionLabelStyle.font = largeInstructionFont;
		largeWhiteInstructionLabelStyle.fontColor = Color.WHITE;

		// Create the UI and add things to their respective stages
		//		The score text
		scoreTxt = new Label("Score: 000,000,000", scoreLabelStyle);
		scoreTxt.setSize(128, 64);
		scoreTxt.setPosition(gridToScreen(12), gridToScreen(13));
		gameStage.addActor(scoreTxt);

		//		The attribution text
		attributionTxt = new Label("Font: Sharp Retro by JROB774 on OpenGameArt.org", smallWhiteMenuLabelStyle);
		attributionTxt.setSize(128, 64);
		attributionTxt.setPosition(gridToScreen(1), gridToScreen(1));
		menuStage.addActor(attributionTxt);

		//		The Game Over text
		gameOverTxt = new Label("GAME OVER", whiteMenuLabelStyle);
		gameOverTxt.setSize(128, 64);
		gameOverTxt.setPosition(gridToScreen(4), gridToScreen(8));
		endStage.addActor(gameOverTxt);

		//		The Title text
		titleTxt = new Label("Commaxian", whiteMenuLabelStyle);
		titleTxt.setSize(128, 64);
		titleTxt.setPosition(gridToScreen(3), gridToScreen(10));
		menuStage.addActor(titleTxt);

		//		The Instructions text
		instructionsTxt = new Label("Don't let the enemies reach you.\nLeft and Right Arrows to move.\nSpace to shoot.\nEscape to pause.\nPress Enter to Play!", whiteInstructionLabelStyle);
		instructionsTxt.setSize(128, 64);
		instructionsTxt.setPosition(gridToScreen(3), gridToScreen(5));
		menuStage.addActor(instructionsTxt);

		//		The Pause text
		pauseTxt = new Label("Quit? Y/N", largeWhiteInstructionLabelStyle);
		pauseTxt.setSize(64, 32);
		pauseTxt.setPosition(gridToScreen(3), gridToScreen(10));
		pauseStage.addActor(pauseTxt);
	}

	public int gridToScreen(int gridSpot){
		return gridSpot * 32;
	}
	public Vector2 gridToScreen(int x, int y){
		// Makes sure our item is within the screen
		if (x < 0) // If we are off-screen to the left, put us on-screen
			x = 0;
		if (x > 19)
			x = 19; // If we are off-screen to the right, put us on-screen

		if (y < 0)
			y = 0; // If we are off-screen to the top, put us on screen
		if (y > 14)
			y = 14; // If we are off-screen to the bottom, put us on screen

		return new Vector2(x * 32, y * 32);
	}

	@Override
	public void render () {
		// Get user input
		getInput();
		// Render things
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();

		// Change what input to act on and what to render based on the current game state.
		switch(currentGameState){
			case MENU:
				getMenuInput();
				renderMenuScene();
				break;
			case PLAY:
				// If the BGM is not playing
				if(!bgMusic.isPlaying()){
					// Start playing BGM at 50% volume
					bgMusic.setVolume(0.25f);
					bgMusic.play();
				}
				getGameInput();
				renderGameScene();
				break;
			case PAUSE:
				getPauseInput();
				renderPauseScene();
				break;
			case END:
				getEndInput();
				renderGameOverScene();
				break;
		}

	}

	public void renderGameScene(){
		batch.begin();
		batch.draw(bgTex, 0, bgY--, 640, bgTex.getHeight());
		if(bgY >= bgTex.getHeight()){
			bgY = 0;
		}

		// Move and draw all enemies
		for(Enemy e : enemies){
			if(numSteps >= stepsToWait){
				e.moveOver(32, 500);
			}
			e.getSprite().draw(batch);

			// Check bounds to see if the game should end.
			if(e.getSprite().getBoundingRectangle().overlaps(killZone)){
				currentGameState = gameState.END;
			}

			// Check to see if a laser has hit an enemy
			if(tempLaser != null){
				if(e.getSprite().getBoundingRectangle().overlaps(tempLaser.getSprite().getBoundingRectangle())){
					e.setLocation(new Vector2(40, gridToScreen(12))); // Move the enemy hit into the upper left corner
					tempLaser = null; // Delete the laser.
					score++;
					scoreTxt.setText("Score: " + score);
				}
			}

			// This comes last to make sure the UI is rendered on top of the game
			gameStage.act();
			gameStage.draw();
		}

		// If the laser is not null...
		if(tempLaser != null){
			tempLaser.move(50); // Move up 50 pixels
			tempLaser.getSprite().draw(batch); // Render to the screen

			// Do some bounds checking
			if(tempLaser.getSprite().getBoundingRectangle().overlaps(laserDeathZone)){
				tempLaser = null; // Delete the laser
			}
		}

		// Modify the number of steps to help limit enemy movement
		numSteps++;
		if(numSteps > stepsToWait){
			numSteps = 0;
		}


		player.getSprite().draw(batch);

		batch.end();
	}

	public void renderMenuScene(){
		batch.begin();
		batch.draw(bgTex, 0, bgY, 640, bgTex.getHeight());
		batch.end();

		menuStage.act();
		menuStage.draw();
	}

	public void renderGameOverScene(){
		batch.begin();
		batch.draw(bgTex, 0, bgY, 640, bgTex.getHeight());
		batch.end();

		endStage.act();
		endStage.draw();
	}

	public void renderPauseScene(){
		batch.begin();
		batch.draw(bgTex, 0, bgY, 640, bgTex.getHeight());
		batch.end();

		pauseStage.act();
		pauseStage.draw();
	}
	
	@Override
	public void dispose () {
		// Clean up all disposable objects.
		// This helps to prevent memory leaks
		batch.dispose();
		playerTex.dispose();
		enemyTex.dispose();
		laserTex.dispose();
		bgTex.dispose();
		shootFX.dispose();
		menuSelectFX.dispose();
		explodeFX.dispose();
		bgMusic.dispose();
		instructionFontGenerator.dispose();
		menuFontGenerator.dispose();
		menuFont.dispose();
		instructionFont.dispose();
		menuStage.dispose();
		gameStage.dispose();
		pauseStage.dispose();
		endStage.dispose();
	}

	public void getMenuInput(){
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER) && !enterPressed){
			enterPressed = true;
			menuSelectFX.play(0.5f); // Play a sound effect
			// Switch to the PLAY state
			currentGameState = gameState.PLAY;
		}
	}

	public void getPauseInput(){
		// Q or N will unpause the game
		if(Gdx.input.isKeyPressed(Input.Keys.Q) && !qPressed){
			qPressed = true;
			menuSelectFX.play(0.5f); // Play a sound effect
			currentGameState = gameState.PLAY; // Unpause the game
		}
		// Y will quit the game
		if(Gdx.input.isKeyPressed(Input.Keys.Y) && !yPressed){
			yPressed = true;
			// Play a sound effect
			menuSelectFX.play(0.5f);
			// Now we can change the game state
			currentGameState = gameState.END;
		}
		// N will also unpause the game.
		if(Gdx.input.isKeyPressed(Input.Keys.N) && !nPressed){
			nPressed = true;
			// Play a sound effect
			menuSelectFX.play(0.5f);
			// Now we can change the game state
			currentGameState = gameState.PLAY;
		}
	}

	public void getEndInput(){

	}

	public void getGameInput(){
		// Get the player object to do any work it needs to do
		this.player.handleInput();

		// If Q is pressed, pause the game.
		if(Gdx.input.isKeyPressed(Input.Keys.Q) && !qPressed){
			qPressed = true;
			// Play a sound effect
			menuSelectFX.play(0.5f);
			// Pause the game.
			currentGameState = gameState.PAUSE;
		}

		// If the laser is being fired
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !spacePressed){
			spacePressed = true;

			// If the laser is null...
			if(tempLaser == null){
				// Fire the Laser
				tempLaser = new Laser(player.getPosition(), laserTex);
			}
		}
	}

	// General input methods that apply to the entire game.
	// Mostly logic to make sure that buttons that should only get pressed once don't act like they're held down.
	public void getInput(){
		// Key Release Debounce Events
		if(!Gdx.input.isKeyPressed(Input.Keys.ENTER) && enterPressed){
			enterPressed = false;
		}
		if(!Gdx.input.isKeyPressed(Input.Keys.Q) && qPressed){
			qPressed = false;
		}
		if(!Gdx.input.isKeyPressed(Input.Keys.SPACE) && spacePressed){
			spacePressed = false;
		}
		if(!Gdx.input.isKeyPressed(Input.Keys.Y) && yPressed){
			yPressed = false;
		}
		if(!Gdx.input.isKeyPressed(Input.Keys.N) && nPressed){
			nPressed = false;
		}
	}
}
