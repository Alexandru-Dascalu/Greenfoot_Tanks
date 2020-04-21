import greenfoot.World;
import java.lang.IllegalArgumentException;

/**
 * <p><b>File name: </b> GraphPoint.java
 * @version 1.0
 * @since 26.08.2018
 * <p><b>Last modification date: </b> 05.10.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright (C)</b> 2018  Alexandru F. Dascalu
 * 
 * <p>GreenPoint.java is part of Panzer Batallion.
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
 * <p> This class describes a graph node used for finding the shortest path for a
 * Greenfoot recreation of the Wii Tanks game for the Nintendo Wii. It is a node 
 * in the graph of the game world. It has 8 neighbours, corresponding coordinates
 * in the game world, and class variables that allow it to be used for Diejkstra's
 * algorithm. These nodes are not meant to be placed in the game world in points 
 * through which a tank can not pass without hitting a wall.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 */

public class GraphPoint implements Comparable<GraphPoint>
{
	/**The vertical/horizontal interval or distance between the points in the 
	 * world that these nodes represent. Its value is {@value}.*/
	public static final int INTERVAL=30;
	
	/**The side of an imaginary square with it's centre in the coordinates of
	 * this node that should not intersect a wall so that tanks will not drive
	 * into walls. Its value is {@value}.*/
	private static final int WALL_DISTANCE=63;
	
	/**The x coordinate of the point in the world this node represents.*/
    private final int x;
    
    /**The y coordinate of the point in the world this node represents.*/
    private final int y;

    /**The best previous node through each the shortest path so far to 
     * the source point is.*/
    private GraphPoint bestPrevious;
    
    /**The length of the current shortest path through the best previous
     * node to the source point.*/
    private double tentativeDistance;
    
    /**The array of neighbours of this node. Each node has 8 neighbours, since 
     * nodes in the world are placed like squares on a chess board.*/
    private GraphPoint[] neighbours;
    
    /**Indicates whether this node has been visited or not in the current 
     * execution of Diejkstra's algorithm.*/
    private boolean visited;
    
    /**
     * Makes a new graph point representing the point in the game world at 
     * the given coordinates.
     * @param x The x coordinate of the point this node represents.
     * @param y The y coordinate of the point this node represents.
     */
    public GraphPoint(int x, int y)
    {
    	//initialise the x and y coordinates and the other variables.
    	this.x=x;
    	this.y=y;
    	bestPrevious=null;
    	visited=false;
    	neighbours=new GraphPoint[8];
    	
    	/*According to shortest path algorithm, the tentative distance of 
    	 * nodes should be initialised as infinite. Since computers are finite,
    	 * we set it to the largest integer value possible.*/
    	tentativeDistance=Integer.MAX_VALUE;
    }

    /**
     * Getter for the x coordinate of the point in the game world this node 
     * represents.
     * @return The x coordinate of this node.
     */
    public int getX()
    {
    	return x;
    }
    
    /**
     * Getter for the y coordinate of the point in the game world this node 
     * represents.
     * @return The y coordinate of this node.
     */
    public int getY()
    {
    	return y;
    }
    
    /**
     * Getter for the tentative distance of this node.
     * @return The tentative distance of this node.
     */
    public double getDistance()
    {
    	return tentativeDistance;
    }
    
    /**
     * Getter for the best previous node of this node.
     * @return The best previous node of this node.
     */
    public GraphPoint getBestPrevious()
    {
    	return bestPrevious;
    }
    
    /**
     * Getter for the array of neighbours of this node.
     * @return The array of neighbours of this node.
     */
    public GraphPoint[] getNeighbours()
    {
    	return neighbours;
    }
    
    /**
     * Says if this node was visited or not in the current execution of the 
     * shortest path algorithm.
     * @return True if this node has been visited, false if not.
     */
    public boolean isVisited()
    {
    	return visited;
    }
    
    /**
     * Getter for the instance of a specific neighbour of this node. The 8 
     * neighbours of this node are arranged like the neighbours of a square
     * on a chess board: one in the upper left, one directly above, one in 
     * the upper right, one to the left, one to the right, one in the lower
     * left, one directly below, one to the lower right. This method returns
     * a specific neighbour based on the string argument which specifies it's
     * position relative to this node. If the string argument does not match 
     * any of the predetermined acceptable string arguments ("upper left", 
     * "upper", "upper right","left","right","lower left","lower","lower right"),
     * it throws an exception.
     * @param position The position of the neighbour relative to this node.
     * @return The neighbour specified by the string argument.
     * @throws IllegalArgumentException
     */
    public GraphPoint getNeighbour(String position) throws IllegalArgumentException
    {
    	/*Based on the value of the string argument, return the corresponding 
    	 * neighbour reference from the array of 8 neighbours.*/
    	if(position.equals("upper left"))
    	{
    		return neighbours[0];
    	}
    	else if(position.equals("upper"))
    	{
    		return neighbours[1];
    	}
    	else if(position.equals("upper right"))
    	{
    		return neighbours[2];
    	}
    	else if(position.equals("left"))
    	{
    		return neighbours[3];
    	}
    	else if(position.equals("right"))
    	{
    		return neighbours[4];
    	}
    	else if(position.equals("lower left"))
    	{
    		return neighbours[5];
    	}
    	else if(position.equals("lower"))
    	{
    		return neighbours[6];
    	}
    	else if(position.equals("lower right"))
    	{
    		return neighbours[7];
    	}
    	/*If the argument does not match any of the predetermined  acceptable
		 * arguments, throw an exception.*/
    	else
    	{
    		throw new IllegalArgumentException("The location string argument"
					+ " has a value that is not one of: upper left, upper, "
					+ "upper right, left, right, lower left, lower or lower right.");
    	}
    }
    
    /**
     * Sets the tentative distance of this node to a new value.
     * @param distance The new value of this node's tentative distance.
     */
    public void setDistance(double distance)
    {
    	tentativeDistance=distance;
    }
    
    /**
     * Sets the boolean value that indicates whether this node has been visited
     * or not in the current execution of the shortest path algorithm.
     * @param visited True if this node has been visited, false if not
     */
    public void setVisited(boolean visited)
    {
    	this.visited=visited;
    }
    
    /**
     * Sets the reference of the best previous neighbour of this node to a new value.
     * @param bestPrevious The new best previous neighbour of this node.
     */
    public void setBestPrevious(GraphPoint bestPrevious)
    {
    	this.bestPrevious=bestPrevious;
    }
    
    /**
     * Setter for the instance of a specific neighbour of this node. The 8 
     * neighbours of this node are arranged like the neighbours of a square
     * on a chess board: one in the upper left, one directly above, one in 
     * the upper right, one to the left, one to the right, one in the lower
     * left, one directly below, one to the lower right. This method sets
     * a specific neighbour reference to a new value based on the string 
     * argument which specifies it's position relative to this node. If the 
     * string argument does not match any of the predetermined acceptable 
     * string arguments ("upper left", "upper", "upper right","left","right",
     * "lower left","lower","lower right"), it throws an exception.
     * @param neighbour The new neighbour of this node.
     * @param location The location of this new neighbour relative to this node.
     * @throws IllegalArgumentException
     */
    public void addNeighbour(GraphPoint neighbour, String location) 
    		throws IllegalArgumentException
    {
    	/*Based on the value of the string argument, set the corresponding 
    	 * neighbour reference from the array of 8 neighbours to the new value.
    	 * Since neighbourhood is a two way relation, we need to ensure the new
    	 * neighbour has this node in it's correct place in it's array of neighbours.
    	 * Therefore, for each case we check if the neighbour parameter is not null 
    	 * (to avoid an exception) and if the new neighbour does not have this this 
    	 * node in it's correct place in the neighbours array (to avoid an infinite
    	 * recursion where the addNeighbour methods of the 2 nodes call each other
    	 * infinitely). If it passes the check, we set this node to the appropiate 
    	 * place in the neighbouring node's array.*/
    	if(location.equals("upper left"))
    	{
    		neighbours[0]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("lower right")!=this))
			{
				neighbour.addNeighbour(this, "lower right");
			}
    	}
    	else if(location.equals("upper"))
    	{
    		neighbours[1]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("lower")!=this))
			{
				neighbour.addNeighbour(this, "lower");
			}
    	}
    	else if(location.equals("upper right"))
    	{
    		neighbours[2]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("lower left")!=this))
			{
				neighbour.addNeighbour(this, "lower left");
			}
    	}
    	else if(location.equals("left"))
    	{
    		neighbours[3]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("right")!=this))
			{
				neighbour.addNeighbour(this, "right");
			}
    	}
    	else if(location.equals("right"))
    	{
    		neighbours[4]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("left")!=this))
			{
				neighbour.addNeighbour(this, "left");
			}
    	}
    	else if(location.equals("lower left"))
    	{
    		neighbours[5]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("upper right")!=this))
			{
				neighbour.addNeighbour(this, "upper right");
			}
    	}
    	else if(location.equals("lower"))
    	{
    		neighbours[6]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("upper")!=this))
			{
				neighbour.addNeighbour(this, "upper");
			}
    	}
    	else if(location.equals("lower right"))
    	{
    		neighbours[7]=neighbour;
			
			if(neighbour!=null && (neighbour.getNeighbour("upper left")!=this))
			{
				neighbour.addNeighbour(this, "upper left");
			}
    	}
    	/*If the argument does not match any of the predetermined  acceptable
    	 * arguments, throw an exception.*/
    	else
    	{
			throw new IllegalArgumentException("The location string argument"
					+ " has a value that is not one of: upper left, upper, "
					+ "upper right, left, right, lower left, lower or lower right.");
    	}
    }
    
    /**Resets this graph point to prepare it to be used for another execution 
     * of the shortest path algorithm.*/
    public void reset()
    {
    	/*At the beginning of the shortest path algorithm, nodes have no best 
    	 * previous neighbour, their tentative distance is infinite and they 
    	 * are not visited.*/
    	bestPrevious=null;
    	tentativeDistance=Integer.MAX_VALUE;
    	visited=false;
    }
   
    /**
     * Compares this node with other nodes in the graph. This method implements
     * the method in the Comparable interface to be used in the shortest path 
     * algorithm in a priority queue. A node is considered to be greater than 
     * another node if it's tentative distance is smaller than the other's. As
     * a result, nodes will be ordered in a priority queue so that at the head
     * of the queue is the node with the smallest tentative distance.
     * @param otherPoint Another node that this node is compared against.
     */
    @Override
    public int compareTo(GraphPoint otherPoint)
    {
    	//calculate the difference between the tentative distances of the 2 nodes
    	double difference=this.getDistance()-otherPoint.getDistance();
    	
    	/*Check if the difference is positive, negative, or 0, to see if this
    	 * node's distance is greater, lesser or equal.*/
    	if(difference>0)
    	{
    		/*if the difference is positive, then this node is considered lesser
    		 * than the other*/
    		return 1;
    	}
    	else if(difference<0)
    	{
    		/*if the difference is negative, then this node is considered 
    		 * lesser than the other*/
    		return -1;
    	}
    	else
    	/*if the difference is 0, the nodes are considered equal*/
    	{
    		return 0;
    	}
    }
    
    /**
     * Determines whether a node could not be safely placed at a certain position 
     * in the given game world so that a tank can drive through it .
     * @param x The x coordinate of that position in the game world.
     * @param y The y coordinate of that position in the game world.
     * @param world A reference to the game world where a node is meant to be 
     * placed.
     * @return True if the position in the game world could not be passed through
     * by a tank without it hitting a wall, false if it could.
     */
    public static boolean isIntersectingWall(int x, int y, World world)
    {
    	/*Since a tank is 51 by 49 pixels big, a tank can drive through a point
    	 * if a square with a side of 52 (slightly larger than the length of 
    	 * the tank) with the centre in the given position does not intersect 
    	 * with a wall. The offsets are the horizontal and vertical distances
    	 * between the centre of the square and one of it's corners.*/
    	int xOffset=(int) Math.ceil(WALL_DISTANCE/2.0);
    	int yOffset=(int) Math.ceil(WALL_DISTANCE/2.0);
    	
    	/*Check if the bottom right corner of the square intersects a wall.*/
    	if(!world.getObjectsAt(x+xOffset, y+yOffset, WallBlock.class).isEmpty())
    	{
    		//if so, this node would intersect a wall
    		return true;
    	}
    	/*Check if the top right corner of the square intersects a wall.*/
    	else if(!world.getObjectsAt(x+xOffset, y-yOffset, WallBlock.class).isEmpty())
    	{
    		//if so, this node would intersect a wall
    		return true;
    	}
    	/*Check if the top left corner of the square intersects a wall.*/
    	else if(!world.getObjectsAt(x-xOffset, y-yOffset, WallBlock.class).isEmpty())
    	{
    		//if so, this node would intersect a wall
    		return true;
    	}
    	/*Check if the bottom left corner of the square intersects a wall.*/
    	else if(!world.getObjectsAt(x-xOffset, y+yOffset, WallBlock.class).isEmpty())
    	{
    		//if so, this node would intersect a wall
    		return true;
    	}
    	/*If all 4 corners do not intersect a wall, then this square also does 
    	 * not, so this node will not be too close to a wall for a tank to 
    	 * pass through.*/
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * Calculates the distance between 2 nodes.
     * @param a A GraphPoint object.
     * @param b Another GraphPoint
     * @return The distance between the 2 nodes.
     */
    public static double getDistance(GraphPoint a, GraphPoint b)
    {
    	//get the horizontal and vertical distances based on the coordinates 
    	//of each node.
    	int xDistance=a.getX()-b.getX();
    	int yDistance=a.getY()-b.getY();
    	
    	/*Calculate the distance using Pythagora's theorem.*/
    	double distance=Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
    	return distance;
    }
    
    /**
     * Calculates the distance between this node and the given land mine.
     * @param mine The mine whose distance to this node will be calculated.
     * @return The distance between this node and the given mine, as a double.
     */
    public double getDistanceFrom(LandMine mine)
    {
    	//get the horizontal and vertical distances based on the coordinates 
    	//of the node and the mine.
    	int xDistance=mine.getX()-x;
    	int yDistance=mine.getY()-y;
    	
    	/*Calculate the distance using Pythagora's theorem.*/
    	double distance=Math.sqrt((xDistance*xDistance)+(yDistance*yDistance));
    	return distance;
    }
}