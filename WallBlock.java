import greenfoot.*; 

/**
 * <p><b>File name: </b> WallBlock.java
 * @version 1.1
 * @since 20.05.2018
 * <p><p><b>Last modification date: </b> 11.07.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a wall block for a Greenfoot recreation of the Wii Tanks 
 * game for the Nintendo Wii. It forms part of a wall that obstructs the path of
 * tanks and shell. They can also be destroyed by land mines.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Added code so that this object can detect what diagonal quadrant
 * of this wall block a point is in, used for when a target search or shell
 * needs to bounce.
 */
public class WallBlock extends Actor
{
    /**The length of the side of the square wall block. It's value is {@value}.*/
    public static final int SIDE=60;
	
    /**
     * Act - do whatever the Wall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * By itself, a wall block does nothing, and collision and bouncing is
     * handled in other classes.
     */
	@Override
    public void act() 
    {
       
    }
    
    /**
    * Calculates what diagonal quadrant of this wall block a point with the
    * given coordinates is in. It also applies to points outside the block
    * (the quadrants start from the centre of this wall block and their 
    * imaginary edges extend up to the world's boundary.)
    * @param x The x coordinate of the point.
    * @param y The y coordinate of the point.
    * @return A string representing the diagonal quadrant the point is in :
    * "top", "bottom", "left or"right".
    */
    public String getQuadrant(int x, int y)
    {
    	/*The wall block is a square, so the geometric slope of it's diagonals
    	 * is either 1 or -1.*/
    	int diagSlope=1;
    	
    	/*Calculate the x and y coordinates of the top left corner of this block.*/
    	int topLeftCornerX=getX()-(SIDE/2);
    	int topLeftCornerY=getY()-(SIDE/2);
    	
    	/*A matrix that holds the names of the 4 quadrants.*/
    	String[][] quadrants= { {"top","right"}, {"left","bottom"} };
    	
    	/*An array that will be set to one of the rows of the matrix, based  on
    	 * if the given point is above or below the diagonal that points to the
    	 * lower left.*/
    	String[] temp;
    	
    	/*The result of this computation. It will be set to one of the values in
    	 * the temp array, based on if the given point is above or below the 
    	 * diagonal that points to the upper right.*/
    	String quadrant;
    	
    	/*We narrow down the possible results by seeing if the given point is 
    	 * above or below the diagonal that points to the lower left. We check
    	 * this using analytical geometry and the formula of y-y' =m*(x-x') .
    	 * Where m is the slope of the diagonal.*/
    	if(y-topLeftCornerY<=diagSlope*(x-topLeftCornerX))
    	{
    		/*If the left side of the equation is smaller or equal to the right
    		 * side, then the point is above the diagonal (not under it, because
    		 * in Greenfoot the y axis is from top to bottom.)*/
    		temp=quadrants[0];
    	}
    	/*If the left side of the equation is greater than the right side, then 
    	 * the point is under the diagonal.*/
    	else
    	{
    		temp=quadrants[1];
    	}
    	
    	/*We find the quadrant by seeing if the given point is above or below 
    	 * the diagonal that points to the upper right. We check this using 
    	 * analytical geometry and the formula of y-y' =m*(x-x') .
    	 * Where m is the slope of the diagonal (which now is negative, unlike
    	 * that of the first diagonal). We take into account this diagonal starts
    	 * lower by the length of the block's side than the first, hence the 
    	 * '-SIDE' in the left part.*/
    	if(y-topLeftCornerY-SIDE<=(-diagSlope)*(x-topLeftCornerX))
    	{
    		/*If the left side of the equation is smaller or equal to the right
    		 * side, then the point is above the diagonal (not under it, because
    		 * in Greenfoot the y axis is from top to bottom.)*/
    		quadrant=temp[0];
    	}
    	/*If the left side of the equation is greater than the right side, then 
    	 * the point is under the diagonal.*/
    	else
    	{
    		quadrant=temp[1];
    	}
    	
    	return quadrant;
    }
}
