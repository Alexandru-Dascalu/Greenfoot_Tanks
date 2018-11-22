import greenfoot.*;

/**
 * <p><b>File name: </b> UIDisplay.java
 * @version 1.0
 * @since 13.06.2018
 * <p><b>Last modification date: </b> 14.06.2018
 * @author Alexandru F. Dascalu
* <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>UIDisplay.java is part of Panzer Batallion.
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
 * <p> This class models a general UI display for a Greenfoot recreation of the 
 * Wii Tanks game for the Nintendo Wii. It should be treated as an abstract class
 * and you should only have objects if this class if they are also instances of
 * a subclass. It models a UI display that has a background image, over which some
 * text is displayed. Part of the text stays the same, another part is updated if
 * the data it displayed has been changed.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created a class for UI displays that update themselves.
 */

public class UIDisplay extends Actor
{
	/**The data displayed in the text that may be updated. Protected so that it
	 * can be accessed by subtypes.*/
	protected int data;
	
	/**The size of the text on UIDisplay objects.It's value is {@value}.*/
	private static final int TEXT_SIZE=25;
	
	/**Makes a new UIDisplay object.*/
	public UIDisplay()
	{
		/*Initialised as -1 because so far all subclasses of UIDisplay work 
		 * with natural numbers, and so the data will always be updated when
		 * the act() method is called the first time.*/
		data=-1;
	}
	
    /**
     * Act - do whatever the LivesMeter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * In this case, it updates the display if it is needed.
     */
	@Override
    public void act() 
    {
		//check if the data has been updated
        if(data!=getNewData())
        {
        	//if it has, update the display
        	update();
        }
    }    
    
	/**
	 * Updates the UI display if the data displayed has been changed.
	 */
    private void update()
    {
    	//we get the updated data, which depends on the subclass of the object.
    	data=getNewData();
    	
    	/*A Color object that is transparent. The Greenfoot API says that if instead
    	 * of a colour we pass null as an argument, then the backgrond is transparent,
    	 * but actually it results in a null pointer exception (which is because they
    	 * changed the code in Greenfoot 3.1.0 and introduced this bug.). Which is 
    	 * why we need to make our own transparent colour.*/
    	Color background=new Color(0,0,0,0);
    	
    	/*we make an image with a transparent background and with the updated text of the display*/
    	GreenfootImage image=new GreenfootImage(getText()+data, TEXT_SIZE,Color.WHITE,background);
    	
    	/*Make a new GreenfootImage made from the background image file of the
    	 * subclass, then draw our updated text image over it, and set this new
    	 * image to be the image of this object.*/
    	GreenfootImage trapezoid=new GreenfootImage(getShapeImage());
    	trapezoid.drawImage(image, getTextXPos(), getTextYPos());
    	setImage(trapezoid);
    }
    
    /**
     * Returns the updated data that should be displayed. Meant to be overridden.
     * @return Just 0, because it is should be overridden and not used on it's own.
     */
    public int getNewData()
    {
    	return 0;
    }
    
    /**
     * Returns the name of the background image of the display. Meant to be overridden.
     * @return Just /'a/', because it is should be overridden and not used on it's own.
     */
    public String getShapeImage()
    {
    	return "a";
    }
    
    /**
     * Returns the text that of the display that remains the same.
     * @return Just /'b/', because it is should be overridden and not used on it's own.
     */
    public String getText()
    {
    	return "b";
    }
    
    /**
     * Returns the correct x coordinate where the updated text should be displayed.
     * @return Just 0, because it is should be overridden and not used on it's own.
     */
    public int getTextXPos()
    {
    	return 0;
    }
    
    /**
     * Returns the correct y coordinate where the updated text should be displayed.
     * @return Just 0, because it is should be overridden and not used on it's own.
     */
    public int getTextYPos()
    {
    	return 0;
    }
}

