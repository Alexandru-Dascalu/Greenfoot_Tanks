
public class RocketShellMk2 extends RocketShell
{
	/**The number of times the shell is allowed to bounce off a wall.The value
     *  is {@value}.*/
    public static final int TIMES_ALLOWED_TO_BOUNCE=2;
    
	/**
     * Makes a new rocket shell MK2 at the given coordinates and with the rotation
     * given.
     * @param rotation The rotation of the new shell, the same as the rotation 
     * of the turret that fired it when it was fired.
     * @param parent The tank whose turret fired this shell.
     * @param x The x coordinate where the new shell will be put.
     * @param y The y coordinate where the new shell will be put.
     */
	public RocketShellMk2(int rotation, Tank parent, int x, int y)
	{
		//just call the superclass constructor
		super(rotation, parent, x ,y);
	}
	
	/**
     * Gets the number of times the shell is allowed to bounce off a wall. We 
     * have this method despite the fact it returns a public constant so that 
     * we can override for different types of shells.
     * @return the number of times the shell is allowed to bounce off a wall.
     */
    public int getBounceLimit()
    {
    	return TIMES_ALLOWED_TO_BOUNCE;
    }
}
