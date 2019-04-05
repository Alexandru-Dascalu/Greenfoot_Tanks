import greenfoot.*;

/**
 * <p><b>File name: </b> YellowTank.java
 * @version 1.1
 * @since 28.09.2018
 * <p><b>Last modification date: </b> 05.10.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>YellowTank.java is part of Panzer Batallion.
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
 * <p> This class models a yellow enemy tank for a Greenfoot recreation of 
 * the Wii Tanks game for the Nintendo Wii. It is a tank with a medium 
 * speed, agile, that specialises in laying mines. It lays four mines randomly
 * and it can also do a suicide attack on the player.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Made Yellow tanks do a suicide attack on the player where they
 * get close to the player, lay a mine then step on it to set it off.
 */

public class YellowTank extends MobileEnemyTank
{
	/**The distance measured in cell-size units by which this tank moves each 
	 * time it acts. Its value is {@value}.*/
    private static final int SPEED=2;
    
    /**The maximum number of degrees by which this tank can turn each time the
     * act() method is called. Its value is {@value}.*/
    private static final int MAX_TURN_SPEED=4;
    
    /**
     * A real number that will be multiplied by the length of this 
     * tank and cast as an integer to get the mine avoidance distance of this
     * tank. Its value is {@value}.
     */
    private static final double MINE_LENGTH_MULTIPLIER = 1.5;
    
    /**
     * A real number that will be multiplied by the length of this 
     * tank and cast as an integer to get the mine avoidance distance of this
     * tank. Its value is {@value}.
     */
    private static final double SHELL_LENGTH_MULTIPLIER = 1.5;
    
    /**The maximum number of mines this type of tank can lay in one level. Its
     * value is {@value}.*/
    private static final int NR_OF_MINES=4;
    
    /**The maximum period in milliseconds between when mines 
     * are laid by this tank. Its value is {@value}.*/
    private static final int MINE_LAYING_PERIOD = 3000;
    
    /**The distance in cells from the player tank that this tank needs to be in 
     * order for it to lay a mine and then try to trigger it. Its value is 
     * {@value}.*/
    private static final int SUICIDE_ATTACK_DISTANCE=100;
    
    /**A boolean flag that indicates if this tank is doing a move where it gets 
     * close to the player in order to lay a mine and then set it off.*/
    private boolean suicideAttack;
    
    /**A flag that indicates if this tank has gotten close enough to the player
     * and now needs to set off the mine it has just laid.*/
    private boolean triggerSuicideMine;
    
    /**A reference to the mine that will be stepped on in order for the player
     * to be destroyed.*/
    private LandMine suicideMine;
    
	/**
     * Make a new TurquoiseTank whose starting position will be at the given
     * coordinates.
     * @param startX The x coordinate the tank will be at the beginning of the
     * level.
     * @param startY The y coordinate the tank will be at the beginning of the
     * level.
	 * @param startRotation The starting rotation of this tank in the world.
     */
    public YellowTank(int startX, int startY, int startRotation)
    {
    	//simply calls the constructor of the superclass.
        super(startX, startY, startRotation);
        
        //initialise instance variables
        suicideMine=null;
        suicideAttack=false;
        triggerSuicideMine=false;
    }
    
    /**
   	 * Prepares the turquoise tank for the beginning of the game. Sets the correct
   	 * starting orientation and gives this tank a TurquoiseTurret.
	 * @param world The game world this tank has just been added to.
   	 */
    @Override
    protected void addedToWorld(World world)
    {
    	this.setRotation(startRotation);
    	tankTurret=new YellowTurret(this);
    }
    
    /**
	 * Act - do whatever the Tank wants to do. This method is called whenever the
	 * 'Act' or 'Run' button gets pressed in the environment. In this case, this
	 * method makes the tank do a suicide attack on the player if it wants to, or
	 * do what any other mobile enemy tank does.
	 */
    @Override
    public void act()
    {
    	/*Check if this tank is supposed to do a suicide attack on the player.*/
    	if(suicideAttack)
    	{
    		/*Check if this tank needs to trigger a mine it has just set in
    		 * order to destroy the player.*/
    		if(triggerSuicideMine)
    		{
    			//make sure to move so that the mine will be stepped on and triggered
    			suicide();
    			
    			/*Meanwhile do what any other tank does.*/
    			pushOtherTanks();
    			
    			tankTurret.aim();
    			tankTurret.fire();
    		}
    		/*If not, then this tank needs to get close enough to the player.*/
    		else
    		{
    			/*Check if a new path needs to be generated.*/
    			if(path==null)
        		{
    				//generate new path that gets this tank to the player
        			generateAttackPath();
        		}
        		else
        		{
        			//get a reference to the player tank
        			PlayerTank playerTank=getWorldOfType(TankWorld.class).getPlayerTank();
        			
        			/*Check if this tank is not close enough to the player tank.*/
        			if(getDistanceFrom(playerTank)>SUICIDE_ATTACK_DISTANCE)
        			{
        				//if so, move towards it like any other mobile enemy tank
        				super.act();
        			}
        			/*Else, this tank is close enough to destroy the player by setting 
        			 * off a mine of it's own.*/
        			else
        			{
        				//get a reference to the world the tank is ins
        				TankWorld world= getWorldOfType(TankWorld.class);
        				
        				//make a new land mine and put it in the game world
        				LandMine mine=new LandMine(this);
        				//the rotation of this tank in radians
        				double radians=Math.toRadians(getRotation());
        				
        				//the maximum speed of this type of tank
        				int tankSpeed=getSpeed();
        				
        				/*The mine is laid slightly behind the current location of the tank,
        				 * so that the tank will ignore the new mine it has laid when it 
        				 * checks if it needs to avoid mines.*/
        				world.addObject(mine, getX()-(int)(tankSpeed*Math.cos(radians)),
        						getY()-(int)(tankSpeed*Math.sin(radians)));
        				minesLaid++;
        				
        				/*set the reference to the suicide mine and the flag 
        				 * for triggering the mine correctly*/
        				suicideMine=mine;
        				triggerSuicideMine=true;
        			}
        		}
    		}
    	}
    	/*If not, this tank acts like any other mobile enemy tank.*/
    	else
    	{
    		super.act();
    	}
    }
    
    /**Generates a path that leads to the current location of the player tank 
     * and sets it as the path of this tank.*/
    private void generateAttackPath()
    {
    	//get a reference to the payer tank of the world this tank is in
    	PlayerTank playerTank=getWorldOfType(TankWorld.class).getPlayerTank();
    	
    	//the graph of points the tank can travel through of the current level
	    Graph worldGraph=getWorldOfType(TankWorld.class).getWorldGraph();
	    
	   	//Get the node/point at the calculated indexes from the graph
	    GraphPoint destination=worldGraph.getPoint(playerTank.getX(), playerTank.getY());
	    
    	/* use the world's graph to get a path to it he destination.*/
    	path=worldGraph.getShortestPath(getX(), getY(), destination);
    }
    
    /**Makes the tank move in reverse until it no longer overlaps the mine
     * it has just laid, then makes it move forward so that the mine will be 
     * stepped on and it will explode.*/
    private void suicide()
    {
    	/*Check if the mine that will be set off ignores it's parent tank when 
    	 * checking if it is being stepped on by a tank.*/
    	if(!suicideMine.getDestroyParent())
    	{
    		//if it does, move backwards
    		move(-getSpeed());
    	}
    	/*Else, the mine can now be set off, so the tank moves forwards.*/
    	else
    	{
    		move(getSpeed());
    	}
    }
    
    /**Makes the tank lay down mines randomly or try to start a suicide attack 
     * on the player.*/
    protected void layMine()
    {
    	/*Check if this tank has laid all but one of it's mines.*/
    	if(minesLaid==NR_OF_MINES-1)
    	{
    		//if so, set the flag to true so this tank will start the suicide attack
    		suicideAttack=true;
    	}
    	/*Check if this tank 2 more mines to lay and the next mine laying time 
    	 * is divisible by 3. This is so that the time the tank will start the
    	 * suicide attack may be random.*/
    	else if((minesLaid==NR_OF_MINES-2) && (nextMineLayingTime%4==0))
    	{
    		//if so, set the flag to true so this tank will start the suicide attack
    		suicideAttack=true;
    	}
    	/*Else, lay down mines randomly.*/
    	else
    	{
    		super.layMine();
    	}
    }
    
    /**Method reloads this tank into the game world to prepare it for another start
	 * of the current level, meaning it resets the position and orientation of this
	 * tank and it's turret, and all other instance variables. */
    public void reloadTank()
    {
    	//reset instance variables unique to this class
    	suicideAttack=false;
    	triggerSuicideMine=false;
    	suicideMine=null;
    	
    	//call superclass reload method to reset all other variables
    	super.reloadTank();
    }
    
    /**
	 * Getter for the speed of this tank, meaning the distance in cells that 
	 * the tank move each time the move(int) method is called.
	 * @return 	The speed of this type of tank tank.
	 */
    @Override
    public int getSpeed()
    {
    	return SPEED;
    }
    
    /**
	 * Getter The maximum number of degrees by which this tank can turn each 
	 * time the act() method is called.
	 * @return 	The maximum turn speed of this type of tank.
	 */
    @Override
    public int getMaxTurnSpeed()
    {
    	return MAX_TURN_SPEED;
    }
    
    /**
     * Indicates the safe distance this type of tank will keep from a mine when
     * it is avoiding a mine.
     * @return the safe distance this type of tank will keep from a mine when
     * it is avoiding a mine.
     */
    @Override
    public double getMineLengthMultiplier()
    {
    	return MINE_LENGTH_MULTIPLIER;
    }
    
    /**
     * Indicates the safe distance this type of tank will keep from a shell when
     * it is avoiding a mine.
     * @return the safe distance this type of tank will keep from a shell when
     * it is avoiding a mine.
     */
    @Override
    public double getShellLengthMultiplier()
    {
    	return SHELL_LENGTH_MULTIPLIER;
    }
    
    /**
	 * The number of mines this tank can lay in one level.
	 * @return 0, unless overridden, since a default tank does not lay any mines.
	 * This method should always be overridden.
	 */
    @Override
	public int getNumberOfMines()
	{
		return NR_OF_MINES;
	}
	
    /**
     * Gets a number that indicates the maximum period in milliseconds
     * between when mines are laid. The higher the number is , the tank will
     * lay mines more rarely.  
     * @return The maximum period in milliseconds between when mines 
     * are laid by this tank.
     */
    @Override
    public int getMineLayingPeriod()
    {
    	return MINE_LAYING_PERIOD;
    }
}

