
/**
 * <p><b>File name: </b> DestroyableWallBlock.java
 * @version 1.0
 * @since 05.09.2018
 * <p><p><b>Last modification date: </b> 07.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class models a destroyable wall block for a Greenfoot recreation of
 * the Wii Tanks game for the Nintendo Wii. It forms part of a wall that 
 * obstructs the path of tanks and shell. They can also be destroyed by land mines.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class DestroyableWallBlock extends WallBlock
{
	/*The land mine only needs to know this is a different type of wall than the
	 *  WallBlock class, and this is achieved by just having a different class. 
	 *  Everything else relating to destroying walls is in the LandMine class, 
	 *  so this class is empty and ust has a differrent image.*/
}
