import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * <p><b>File name: </b> Graph.java
 * @version 1.1
 * @since 27.07.2018
 * <p><b>Last modification date: </b> 14.09.2018
 * @author Alexandru F. Dascalu
 * <p><b>Copyright: </b>
 * <p>No copyright.
 * 
 * <p><b>Purpose: </b>
 * <p> This class describes a graph used for finding the shortest path for a Greenfoot
 * recreation of the Wii Tanks game for the Nintendo Wii. It builds a network of
 * nodes base on an instance of a TankWorld so that nodes are where a tank can move
 * safely without hitting walls.
 * 
 * <p><b>Version History</b>
 * <p>	-1.0 - Created the class.
 * <p>	-1.1 - Added code to generate paths that avoid land mines.
 */
public class Graph  
{
	/**The matrix of graph points which contains this graph. Since nodes are points
	 * on the game world, and each point has 8 neighbours which are the closest point
	 * to that point in the world, the graph is stored as a 2 dimensional array.*/
    private GraphPoint[][] pointMatrix;

    /**
     * Makes a new Graph that matches the layout of the given instance of TankWorld.
     * This means it will make a graph where if a point in the world is too close to
     * a wall, it will be a null value in the matrix so that the tank will not 
     * pass through walls. It also addes the edges between neighbouring nodes while
     * making new nodes.
     * @param tankWorld The world this graph is built for.
     */
    public Graph(TankWorld tankWorld)
    {
    	/*Since no nodes are created where a tank can not pass, no nodes need to 
    	 * be created on the edges of the world where the exterior walls are, so
    	 * the matrix is only big enough to hold points that would fill the are 
    	 * inside the world's exterior walls. Which is why it subtracts the length
    	 * of 2 wall blocks from the length and width of the world.*/
    	
    	/*The length of the matrix is the length of world without the length of the 
    	 * exterior walls divided by the interval at which graph points are made.*/
    	int matrixLength=(TankWorld.LENGTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	/*The width of the matrix is the length of world without the length of the 
    	 * exterior walls divided by the interval at which graph points are made.*/
    	int matrixWidth=(TankWorld.WIDTH-2*WallBlock.SIDE)/GraphPoint.INTERVAL+1;
    	
    	//make a new matrix with the calculated sizes
    	pointMatrix=new GraphPoint[matrixWidth][matrixLength];
    
    	//variables for the coordinates where each point will be made
    	int x, y;
   
    	/*For loop for creating each row of the matrix, loops for as many times
    	 * as the width of the matrix.*/
    	for(int i=0;i<matrixWidth;i++)
    	{
    		/*The y coordinate of the points in each row is the length of the wall
    		 * block on the left edge of the world, with half the length between
    		 * points(from the wall to the centre of the point) and the index of
    		 * this matrix row multiplied by the distance between points.*/
    		y=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+i*GraphPoint.INTERVAL;
    		
    		/*For loop for creating each element of a row of the matrix, loops 
    		 * for as many times as the length of the matrix.*/
    		for(int j=0;j<matrixLength;j++)
    		{
    			/*The x coordinate of the points in each row is the length of the wall
        		 * block on the left edge of the world, with half the length between
        		 * points(from the wall to the centre of the point) and the index of
        		 * this matrix column multiplied by the distance between points.*/
    			x=WallBlock.SIDE+(GraphPoint.INTERVAL/2)+j*GraphPoint.INTERVAL;
    			
    			/*We need to check if a tank can pass through the centre of this
    			 * point and not hit a wall, to know if we need to add a point that
    			 * belong to a path a tank can follow.*/
    			if(!GraphPoint.isIntersectingWall(x, y, tankWorld))
    			{
    				/*If a tank that would through this point would not intersect 
    				 * with a wall, we make a new point here and add it's edges with
    				 * it's neighbours.*/
    				pointMatrix[i][j]=new GraphPoint(x,y);
    				
    				/*After putting a new node in the graph, we need to make the
    				 * edges to it's neighbours. Since nodes are built from top 
    				 * to bottom, left to right, when a new point is made it can 
    				 * only above, in it's upper left, upper right and to it's left.
    				 * Neighbours that are below it or to the right are added to the
    				 * new point's array of neighbours when those new neighbours are made.*/
    				
    				//Check if this point has a neighbour above it.
    				if(i!=0)
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j], "upper");
    				}
    				
    				//Check if this point has a neighbour to it's lefts.
    				if(j!=0)
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i][j-1], "left");
    				}
    				
    				//Check if this point has a neighbour to it's upper left.
    				if(j!=0 && i!=0)
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j-1], "upper left");
    				}
    				
    				//Check if this point has a neighbour to it's upper right.
    				if(i!=0 && j!=(matrixLength-1))
    				{
    					//If it has, add it to it's array of neighbours in the correct place
    					pointMatrix[i][j].addNeighbour(pointMatrix[i-1][j+1], "upper right");
    				}
    			}
    			/*If a tank can not pass through this point on the world because
    			 * of a wall, set this place in the matrix to null so the shortest 
    			 * path algorithm will avoid it.*/
    			else
    			{
    				pointMatrix[i][j]=null;
    			}
    		}
    	}
    }
    
    /**
     * Getter for individual nodes of this graph. Returns the node at the given
     * index in the matrix.
     * @param rowIndex The row index of the node to be returned.
     * @param columnIndex The column index of the node to be returned.
     * @return The point in the matrix at the given indexes.
     */
    public GraphPoint getPoint(int rowIndex, int columnIndex)
    {
    	return pointMatrix[rowIndex][columnIndex];
    }
    
    /**
     * Computes the shortest path between the given coordinates in the game 
     * world and the target node in this graph using Diejkstr'a algorithm.
     * @param startX The starting x coordinate.
     * @param startY The starting y coordinate.
     * @param target The node in the graph that is the destination.
     * @return A LinkedList of graph nodes (GraphPoint objects) that is the 
     * shortest path to the target node.
     */
    public LinkedList<GraphPoint> getShortestPath(int startX, int startY,
    		GraphPoint target)
    {
    	/*Find out the node in the graph closest to the starting coordinates, 
    	 * which will be our source node and initialise it's tentative distance.*/
    	initializeSourcePoint(startX, startY);
    	
    	/*make a new priority queue, which will be used to get the node with 
    	 *the shortest tentative distance*/
    	PriorityQueue<GraphPoint> unvisitedNodes=new PriorityQueue<>();
    	
    	//add all nodes in the graph matrix to the priority queue
    	for(GraphPoint[] pointRow: pointMatrix)
    	{
    		for(GraphPoint point: pointRow)
    		{
    			//Check if that place in the matrix is populated by a node.
    			if(point!=null)
    			{
    				//if it is, add the node to the priority queue
    				unvisitedNodes.add(point);
    			}
    		}
    	}
    	
    	//the current node we are visiting
    	GraphPoint current;
    	
    	//Visit all nodes and stop when the priority queue is empty.
    	while(!unvisitedNodes.isEmpty())
    	{
    		//get the unvisited node with the smallest tentative distance
    		current=unvisitedNodes.poll();
    		
    		//mark it as visited
    		current.setVisited(true);
    		
    		/*Check if the node we are visiting is the destination node.*/
    		if(current.equals(target))
    		{
    			/*If it is, then we have a shortest path to it so the search is 
    			 * finished and the loop is terminated.*/
    			break;
    		}
    		
    		/*Check if the unvisited node with smallest tentative distance is 
    		 * still the maximum value set initially. */
    		if(current.getDistance()==Integer.MAX_VALUE)
    		{
    			/*If it is,then this node and all other unvisited nodes is separated
    			 * by a wall from the source point and no path to the destination exists.*/
    			return null;
    		}
    		
    		/*Go through each neighbour of the current node and update the 
    		 * tentative distances of the unvisited ones.*/
    		for(GraphPoint neighbour: current.getNeighbours())
    		{
    			/*Check if there is a neighbour at the current index and if that
    			 * neighbour is not visited.*/
    			if(neighbour!=null && !neighbour.isVisited())
    			{
    				/*calculate the length of the path through this neighbour
    				 * between this node and the source point.*/
    				double tentativeDistance = current.getDistance()+
    						GraphPoint.getDistance(current, neighbour);
    				
    				/*Check if the calculated value if smaller than the current 
    				 * tentative value of the selected neighbour,*/
    				if(tentativeDistance<neighbour.getDistance())
    				{
    					/*If it is, update the value, set the best previous node
    					 * to the current node remove and re-add the neighbour
    					 * to the priority queue so that it's place corresponds
    					 * with the new value.*/
    					unvisitedNodes.remove(neighbour);
    					neighbour.setDistance(tentativeDistance);
    					neighbour.setBestPrevious(current);
    					
    					unvisitedNodes.add(neighbour);
    				}
    			}
    		}
    	}
    	
    	//construct the linked list of nodes that is the path
    	LinkedList<GraphPoint> path=getPath(target);
    	
    	/*since the computation is done, reset the tentative distance and 
    	bestPrevious instance to be used for the next call of this method.*/
    	resetGraph();
    	return path;
    }
    
    /**
     * Computes the shortest path between the given coordinates in the game 
     * world and the target node in this graph using Diejkstr'a algorithm, 
     * while keeping a safe distance from the given land mine.
     * @param startX The starting x coordinate.
     * @param startY The starting y coordinate.
     * @param target The node in the graph that is the destination.
     * @param tank The tank for which this path is computed.
     * @param mine The mine that will be avoided by the returned path.
     * @return A LinkedList of graph nodes (GraphPoint objects) that is the 
     * shortest path to the target node.
     */
    public LinkedList<GraphPoint> getPathAvoidingMine(MobileEnemyTank tank, 
    		LandMine mine, int startX, int startY, GraphPoint target)
    {
    	/*Find out the node in the graph closest to the starting coordinates, 
    	 * which will be our source node and initialise it's tentative distance.*/
    	initializeSourcePoint(startX, startY);
    	
    	/*make a new priority queue, which will be used to get the node with 
    	 *the shortest tentative distance*/
    	PriorityQueue<GraphPoint> unvisitedNodes=new PriorityQueue<>();
    	
    	//add all nodes in the graph matrix to the priority queue
    	for(GraphPoint[] pointRow: pointMatrix)
    	{
    		for(GraphPoint point: pointRow)
    		{
    			//Check if that place in the matrix is populated by a node.
    			if(point!=null)
    			{
    				//if it is, add the node to the priority queue
    				unvisitedNodes.add(point);
    			}
    		}
    	}
    	
    	//the current node we are visiting
    	GraphPoint current;
    	
    	//Visit all nodes and stop when the priority queue is empty.
    	while(!unvisitedNodes.isEmpty())
    	{
    		//get the unvisited node with the smallest tentative distance
    		current=unvisitedNodes.poll();
    		
    		//mark it as visited
    		current.setVisited(true);
    		
    		/*Check if the node we are visiting is the destination node.*/
    		if(current.equals(target))
    		{
    			/*If it is, then we have a shortest path to it so the search is 
    			 * finished and the loop is terminated.*/
    			break;
    		}
    		
    		/*Check if the unvisited node with smallest tentative distance is 
    		 * still the maximum value set initially. */
    		if(current.getDistance()==Integer.MAX_VALUE)
    		{
    			/*If it is,then this node and all other unvisited nodes is separated
    			 * by a wall from the source point and no path to the destination exists.*/
    			return null;
    		}
    		
    		/*Go through each neighbour of the current node and update the 
    		 * tentative distances of the unvisited ones.*/
    		for(GraphPoint neighbour: current.getNeighbours())
    		{
    			/*Check if there is a neighbour at the current index and if that
    			 * neighbour is not visited.*/
    			if(neighbour!=null && !neighbour.isVisited())
    			{
    				//System.out.println(tank.getMineAvoidanceDistance());
    				if(neighbour.getDistanceFrom(mine)>tank.getMineAvoidanceDistance())
    				{
    					/*calculate the length of the path through this neighbour
        				 * between this node and the source point.*/
        				double tentativeDistance = current.getDistance()+
        						GraphPoint.getDistance(current, neighbour);
        				
        				/*Check if the calculated value if smaller than the current 
        				 * tentative value of the selected neighbour,*/
        				if(tentativeDistance<neighbour.getDistance())
        				{
        					/*If it is, update the value, set the best previous node
        					 * to the current node remove and re-add the neighbour
        					 * to the priority queue so that it's place corresponds
        					 * with the new value.*/
        					unvisitedNodes.remove(neighbour);
        					neighbour.setDistance(tentativeDistance);
        					neighbour.setBestPrevious(current);
        					
        					unvisitedNodes.add(neighbour);
        				}
    				}
    				else
    				{
    					neighbour.setVisited(true);
    				}
    			}
    		}
    	}
    	
    	//construct the linked list of nodes that is the path
    	LinkedList<GraphPoint> path=getPath(target);
    	
    	/*since the computation is done, reset the tentative distance and 
    	bestPrevious instance to be used for the next call of this method.*/
    	resetGraph();
    	return path;
    }
    
    /**
     * Finds the node in the graph closest to the given coordinates in the game
     * world and initialises it's tentative distance.
     * @param startX The starting x coordinate.
     * @param startY The starting y coordinate.
     */
    private void initializeSourcePoint(int startX, int startY)
    {
    	/*The nodes in the graph are all within the exterior walls of the level, 
    	 * so the top leftmost node' top left corner is at coordinates WallBock.SIDE
    	 * and WallBock.SIDE. e subtract these values to account for this.*/
    	startX-=WallBlock.SIDE;
    	startY-=WallBlock.SIDE;
    	
    	/*Calculate the indexes of the approximate node these coordinates are over 
    	 *by dividing them with the horizontal/vertical distance between nodes.*/
    	int rowIndex=startY/GraphPoint.INTERVAL;
    	int columnIndex=startX/GraphPoint.INTERVAL;
    	
    	//get the node at the calculated indexes
    	GraphPoint approximateSource=pointMatrix[rowIndex][columnIndex];
    	
    	/*Check if the slot at the calculated indexes is null. Very rarely, if 
    	 * the enemy tank moved while turning it might get to a point for which
    	 * there is no node in the graph (because that point is close to a wall,
    	 * since if a slot in the graph matrix corresponds to a place in the game
    	 * world covered by wall that slot is set to null so it will be ignored by
    	 * the path finding algorithm).*/
    	if(approximateSource==null)
    	{
    		/*If so, we need to select a neighbouring existing node to be our 
    		 * source node. A node has 8 neighbours, like each square has on 
    		 * a chess board. We cycle through the differences between the 
    		 * neighbour's indexes and the indexes of the null slot (so -1, 
    		 * 0 and 1).*/
    			
    		/*Cycle through the values of the difference between the row index 
    		 * of the null slot and it's neighbours. We use a label to break out
    		 * of 2 nested for loops.*/
    		outerLoop:
    		for(int i=-1; i<=1; i++)
    		{
    			/*Cycle through the values of the difference between the column index 
        		 * of the null slot and it's neighbours.*/
    			for(int j=-1 ; j<=1 ; j++)
        		{
    				/*If at the indexes of the neighbour there is a node in the 
    				 * graph, it is our source point and we terminate the nested 
    				 * loops.*/
        			if(pointMatrix[rowIndex+i][columnIndex+j]!=null)
        			{
        				approximateSource=pointMatrix[rowIndex+i][columnIndex+j];
        				break outerLoop;
        			}
        		}
    		}
    	}
    	
    	//initialize the tentative distance of the source node to 0, necessary
    	//for Diejkstra's algorithm
    	approximateSource.setDistance(0);
    }
    
    /**
     * Builds and returns the linked list that is the path between the source
     * point and the target node.
     * @param target The target node the tank will move to.
     * @return The linked list of nodes in the graph starting from the source
     * to the destination node.
     */
    private LinkedList<GraphPoint> getPath(GraphPoint target)
    {
    	//the linked list that will be returned
    	LinkedList<GraphPoint> path=new LinkedList<>();
    	
    	/*The current node that is to be added to the list, starting with the 
    	 * target node.*/
    	GraphPoint current=target;
    	
    	/*Build the path by adding the current node to the path and then make the
    	 * best previous node of it the new current node. Since the source node
    	 * is the first that was visited, it's bestPrevious value is null. So when
    	 * the current node's bestPrevious is null, we reached the source node and
    	 * stop the loop.*/
    	while(current.getBestPrevious()!=null)
    	{
    		path.addFirst(current);
    		current=current.getBestPrevious();
    	}
    	
    	//the loop was terminated before adding the source point, so we add it now.
    	path.addFirst(current);
    	
    	//return the path
    	return path;
    }
    
    /**Resets the graph to prepare for the next call of the getShortestPath.*/
    private void resetGraph()
    {
    	/*Go through each node of this graph and reset it to prepare it for next 
    	 * use of Diejkstra's algorithm.*/
    	for(GraphPoint[] pointRow: pointMatrix)
    	{
    		for(GraphPoint point: pointRow)
    		{
    			/*Check if a node actually exists at that slot in the matrix.*/
    			if(point!=null)
    			{
    				point.reset();
    			}
    		}
    	}
    }
}