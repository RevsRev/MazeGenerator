# MazeGenerator
A Maze Generator written in Java.

NOTE: This was an experiment using a probablistic algorithm. It is almost certainly not as fast as other maze generators people may have written.

INSTRUCTIONS FOR USE
Compile all files in the same dirrectory in this order: ArrayMethods, MazeGrid, Maze, MazeWindow, MazeGenerator. Then run the compiled Maze Generator file to generate your maze! The start of the maze is in the bottom left corner, and the end is at the top right corner (or vice versa if you like). Provided you pick the parameters appropriately, the generation time is reasonable even for large mazes (more on parameters below).

DESCRIPTION OF PARAMETERS
In the Maze.java class, there are several parameters listed towards the top of the file which are important for Maze generation.
  
  bendyness - 100 is a very bendy maze, 0 is a not so bendy maze. Sweet spot seems to be around 25.
  globalRejectLoopProbability - If you are standing at a point in a maze and may return to this point without traversing the same part of the route twice, we call 
                                this route a loop. If we don't want any loops in our maze, set the globalRejectLoopProbability to 100. If you want lots of loops (
                                an easy maze!) set it to 0.
  slackRejectLoopProbability - Not really important anymore. May as well set to be equal to the globalRejectLoopProbability.
  workOnPreviousProbablity - When a new part of the maze is generated, the algorithm will generate another part at this new part with this probability (value in 0 
                              to 100). Thus, for a natural looking maze with many tunnels, it is best to set this to a reasonably high number.
   saturationLimit - VERY IMPORTANT! Once saturationLimit % of the squares in our maze are made into traversable regions (ie coloured white), the algorithm will 
                    stop. This parameter needs to be tuned based on the size of the maze (best done by trial and error). IF THE MAZE IS TAKING TOO LONG TO,
                    GENERATE, THEN YOU NEED TO DECREASE THIS PARAMETER. (ALT. you could decrease reject Loop prob, but this won't always work).
                    
BRIEF DESCRIPTION OF ALGORITHM
This maze is generated using a probabilistic algorithm. The parameters above need to be chosen properly to prevent generation taking too long (or never(!) terminating). A generated part of the maze is deemed "workable" if more parts of the maze can be generated in the north, east, south and west squares adjacent to it (and the square is not on the edge of the maze). All workable squares are kept in a list, and if a square becomes unworkable it is removed from this list. At each step, the algorithm will randomly sample from this list to find a new workable square, and then it may generate another workable square next to it (if the generation of a new square would result in a Loop in the maze, it will reject generating this square with a probability "globalRejectLoopProbability". Thus, not every choice of workable square leads to a successful generation of a new maze part). The choice of generated square is influenced by the "bendyness" parameter, i.e. how bendy we want the maze to be. At each step, the total number of white squares generated in the maze is counted, and so is the length of the array containing the workable squares. If the total number of white squares exceeds or is equal to saturationLimit% of the total squares of the maze (height x width of maze), or the length of the working array is zero, the algorithm terminates. It is thus very important to make sure the saturationLimit is appropriately chosen (especially for high/certain rejectLoopProbablity) so that the algorithm does indeed terminate.

KNOWN ISSUES:
For some reason the maze prefers to generate horizontal rather than vertical straights (easiest seen when bendyness is low) - I'm not sure why but will fix at some point.
