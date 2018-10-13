import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <p><b>File name: </b> StartScreen.java
 * @version 1.0
 * @since 21.06.2018
 * <p><b>Last modification date: </b> 07.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>StartScreen.java is part of Panzer Batallion.
 * Panzer Batallion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * <p>You should have received a copy of the GNU General Public License v3
 * along with this program.  If not, see <a href="https://www.gnu.org/licenses/">https://www.gnu.org/licenses/</a> .
 * 
 * <p>A summary of the license can be found here: 
 * <a href="https://choosealicense.com/licenses/gpl-3.0/">https://choosealicense.com/licenses/gpl-3.0/</a> .
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a start screen display for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It displays the current level and 
 * the number of enemy tanks left in this level.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class StartScreen extends Actor
{
	/**The file name of the image on which the number of enemy tanks and the 
	 * level number will be displayed.*/
	private static final String START_TEMPLATE="levelStart.png";
	
	/**
	 * Makes a new start screen actor for the current level of the given world.
	 * @param world The world for which this start screen display will be generated.
	 */
	public StartScreen(TankWorld world)
	{
		/*Make images with text that reads the current level and the number of 
		 * enemy tanks on a transparent background.*/
    	Color background=new Color(0,0,0,0);
    	GreenfootImage levelNumber= new GreenfootImage(""+world.getLevel(),50,Color.WHITE,background);
    	GreenfootImage enemyTanks=new GreenfootImage(""+world.getNrEnemyTanks(),50,Color.WHITE,background);
    	
    	/*Make an image from the default image and then draw the level number and
    	 * number of enemy tanks onto it.*/
    	GreenfootImage newImage=new GreenfootImage(START_TEMPLATE);
    	newImage.drawImage(levelNumber, 480, 70);
    	newImage.drawImage(enemyTanks, 550, 130);
    	
    	//set this image as the image of this actor
    	setImage(newImage);
	}
}
