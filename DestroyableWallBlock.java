/**
 * <p><b>File name: </b> DestroyableWallBlock.java
 * @version 1.1
 * @since 28.12.2018
 * <p><b>Last modification date: </b> 07.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>DestroyableWallBlock.java is part of Panzer Batallion.
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
 * <p> This class models a destroyable wall block for a Greenfoot recreation of
 * the Wii Tanks game for the Nintendo Wii. It forms part of a wall that 
 * obstructs the path of tanks and shell. They can also be destroyed by land mines.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>  -1.1 - Added isDestroyable method.
 */

public class DestroyableWallBlock extends WallBlock
{
	/**
     * Says if this type of wall block is destroyable.
     * @return true, since normal wall blocks are not destroyable.
     */
	@Override
    public boolean isDestroyable()
    {
    	return true;
    }
}

