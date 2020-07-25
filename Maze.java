import java.util.Random;

public class Maze {
  public int height;
  public int width;
  public MazeGrid mazeGrid;
  public int bendyness =30;
  public int globalRejectLoopProbability = 100;
  public int slackRejectLoopProbability = 50; //this is quite redundant now.
  public int workOnPreviousProbability =90;
  public int saturationLimit = 55; //once saturationLimit% of squares are white (ie traversible), program stops

  private Random rand = new Random();

  private int[] north = new int[] {1,0};
  private int[] south = new int[] {-1,0};
  private int[] east = new int[] {0,1};
  private int[] west = new int[] {0,-1};

  private ArrayMethods aM = new ArrayMethods();

  public Maze(int height, int width) {
    this.height = height;
    this.width = width;
    mazeGrid = new MazeGrid(height, width);
    GenerateMaze();
  }

  private boolean ValidGenerated(int [] location) {
    if (location[0]>=height-1 || location[0]<= 0 || location[1]>=width-1 || location[1]<=0) {
      return false;
    }
    if (mazeGrid.grid[location[0]][location[1]]) {
      return false; //also not valid if this square has also been generated
    }
    for (int i=0; i<2; i++) {
      for (int j=0; j<2; j++) {
        try {
          if (mazeGrid.grid[location[0] + 2*i - 1][location[1]+2*j-1]) {
            if (mazeGrid.grid[location[0] + 2*i-1][location[1]] && mazeGrid.grid[location[0]][location[1]+2*j-1]) {
              return false;
            }
          }
        } catch(Exception e) {
          continue;
        }
      }
    }
    return true;
  }

  private int CountStraights(int[] location) {
    int counter = 0;
    for (int i=0; i<2; i++) {
      if (mazeGrid.grid[location[0]][location[1]+2*i-1]) {
        counter+=1;
      }
      if (mazeGrid.grid[location[0]+2*i-1][location[1]]) {
        counter+=1;
      }
    }
    return counter;
  }

  private boolean UseGenerated(int [] location, int workingSquares) {

    int rejectLoopProbability = globalRejectLoopProbability;
    if (workingSquares < (width+height)/5) {
      rejectLoopProbability = slackRejectLoopProbability; //this speeds up our algorithm (at the start and) at the final stages.
    }

    if (location[0]>=height-1 || location[0]<= 0 || location[1]>=width-1 || location[1]<=0) {
      return false;
    }
    if (mazeGrid.grid[location[0]][location[1]]) {
      return false; //also not valid if this square has also been generated
    }
    try {
      if (CountStraights(location) >= 2) {
        if (rand.nextInt(101) <= rejectLoopProbability) {
          return false;
        }
      }
    } catch(Exception e) {
      return true;
    }
    return true;
  }

  private boolean[][] GetDirrections(int [] location, int workingSquares) {
    boolean[][] dirrections = new boolean[4][4];
    /*the first row encodes whether that dirrection is possible to generate.
    the second row encodes whether we want to generate these possibilities (for instance,
    we may not wish to generate some of the possibilities because it will form loops).
    the third row encodes whether that dirrection will be straight, and the fourth row
    encodes whether that dirrection will be a bend.
    column order is [north, east, south, west]. */

    dirrections[0][0] = ValidGenerated(aM.addVectors(location, north));
    dirrections[0][1] = ValidGenerated(aM.addVectors(location, east));
    dirrections[0][2] = ValidGenerated(aM.addVectors(location, south));
    dirrections[0][3] = ValidGenerated(aM.addVectors(location, west));

    dirrections[1][0] = UseGenerated(aM.addVectors(location, north), workingSquares);
    dirrections[1][1] = UseGenerated(aM.addVectors(location, east), workingSquares);
    dirrections[1][2] = UseGenerated(aM.addVectors(location, south), workingSquares);
    dirrections[1][3] = UseGenerated(aM.addVectors(location, west), workingSquares);

    try { //north will give straight
      if (mazeGrid.grid[location[0]-1][location[1]] && dirrections[1][0]) {
        dirrections[2][0] = true;
      } else {
        dirrections[2][0] = false;
      }
    } catch(Exception e) {
      dirrections[2][0] = false;
    }

    try{
      if (mazeGrid.grid[location[0]][location[1]-1] && dirrections[1][1]) {
        dirrections[2][1] = true;
      } else {
        dirrections[2][1] = false;
      }
    } catch(Exception e) {
      dirrections[2][1] = false;
    }

    try{
      if (mazeGrid.grid[location[0]+1][location[1]] && dirrections[1][2]) {
        dirrections[2][2] = true;
      } else {
        dirrections[2][2] = false;
      }
    } catch(Exception e) {
      dirrections[2][2]= false;
    }

    try {
      if (mazeGrid.grid[location[0]][location[1]+1] && dirrections[1][3]) {
        dirrections[2][3] = true;
      } else {
        dirrections[2][3] = false;
      }
      return dirrections;
    } catch(Exception e) {
      dirrections[2][3] = false;
    }

    //the above works out the straights. Now we do bends:

    for (int i=0; i<4; i++) {
      if (dirrections[1][i] && !dirrections[2][i]) {
        dirrections[3][i] = true;
      } else {
        dirrections[3][i] = false;
      }
    }

    return dirrections;
  }

  private boolean FurtherMovesPossible(boolean[] moves, boolean boolIfPossible) {
    //the first row of the output of the GetDirrections Method is to be fed into this.
    for (int i=0; i<moves.length; i++) {
      if (moves[i]) {
        return boolIfPossible;
      }
    }
    return !boolIfPossible;
  }

  private boolean VoidMove(int[] location) {
    try {
      if (CountStraights(location)<=1) {
        return true;
      } else {
        return false;
      }
    } catch(Exception e) {
      return false;
    }
  }

  private boolean VoidMovePossible(boolean[][] dirrections, int[] location) {
    int[] moveLocation= {0, 0};
    for (int i=0; i<4; i++) {
      if (dirrections[0][i]) {
        switch(i) {
          case 0:
            moveLocation = north;
          case 1:
            moveLocation = east;
          case 2:
            moveLocation = south;
          case 3:
            moveLocation = west;
        }

        if (VoidMove(aM.addVectors(location, moveLocation))) {
          return true;
        }
      }
    }
    return false;
  }

  public void GenerateMaze() {
    /* We generate by starting with one end and growing it, occasionally branching
    We do this successively until there are no new areas to populate*/

    int saturationThreshold = saturationLimit*height*width/100;
    int saturation = 1; //counts the number of squares.
    int choiceIndex;
    int[] choiceElement;
    boolean looping;
    boolean[][] dirrections = new boolean[4][4];
    int[][] currentChoices = new int[1][2];

    int[] startPos = {height-1, 1};
    mazeGrid.grid[height-1][1] = true;
    currentChoices[0] = startPos;


    while (currentChoices.length > 0 && saturation<saturationThreshold) {
      if (rand.nextInt(101) <= workOnPreviousProbability) {
        choiceIndex = currentChoices.length-1; //since the last element is always appended, this is its index.
      } else {
        choiceIndex = rand.nextInt(currentChoices.length);
      }
      choiceElement = currentChoices[choiceIndex];
      //aM.printArray(mazeGrid.grid);
      //aM.printArray(currentChoices);
      //System.out.println(currentChoices.length);
      dirrections = GetDirrections(choiceElement, currentChoices.length);

      if (!FurtherMovesPossible(dirrections[0], true)) {
        currentChoices = aM.removeElement(choiceIndex, currentChoices);
      } else if (FurtherMovesPossible(dirrections[1], true)) {
        //we prioritise building into empty space, ie space without bends or straights to start with.
        if (VoidMovePossible(dirrections, choiceElement)) {
          looping = true;
          //System.out.println("A void move is possible");
          while (looping) {
            choiceIndex = rand.nextInt(dirrections[1].length);
            switch (choiceIndex) {
              case 0:
                if (VoidMove(aM.addVectors(choiceElement, north))) {
                  mazeGrid.grid[choiceElement[0]+1][choiceElement[1]] = true;
                  currentChoices = aM.Append(aM.addVectors(north, choiceElement), currentChoices);
                  saturation +=1;
                  looping = false;
                  break;
                }
              case 1:
                if (VoidMove(aM.addVectors(choiceElement, east))) {
                  mazeGrid.grid[choiceElement[0]][choiceElement[1]+1] = true;
                  currentChoices = aM.Append(aM.addVectors(east, choiceElement), currentChoices);
                  saturation +=1;
                  looping = false;
                  break;
                }
              case 2:
                if (VoidMove(aM.addVectors(choiceElement, south))) {
                  mazeGrid.grid[choiceElement[0]-1][choiceElement[1]] = true;
                  currentChoices = aM.Append(aM.addVectors(south, choiceElement), currentChoices);
                  saturation+=1;
                  looping = false;
                  break;
                }
              case 3:
                if (VoidMove(aM.addVectors(choiceElement, west))) {
                  mazeGrid.grid[choiceElement[0]][choiceElement[1]-1] = true;
                  currentChoices = aM.Append(aM.addVectors(west, choiceElement), currentChoices);
                  saturation+=1;
                  looping = false;
                  break;
                }
            }
          }
        } else if (rand.nextInt(101) <= bendyness) {
          if (FurtherMovesPossible(dirrections[3], true)) { //bendy option exists!
            while (true) {
              choiceIndex = rand.nextInt(dirrections[1].length);
              if (dirrections[1][choiceIndex] && dirrections[3][choiceIndex]) {
                switch(choiceIndex) {
                  case 0:
                    mazeGrid.grid[choiceElement[0]+1][choiceElement[1]] = true;
                    currentChoices = aM.Append(aM.addVectors(north, choiceElement), currentChoices);
                    saturation += 1;
                    break;
                  case 1:
                    mazeGrid.grid[choiceElement[0]][choiceElement[1]+1] = true;
                    currentChoices = aM.Append(aM.addVectors(east, choiceElement), currentChoices);
                    saturation += 1;
                    break;
                  case 2:
                    mazeGrid.grid[choiceElement[0]-1][choiceElement[1]] = true;
                    currentChoices = aM.Append(aM.addVectors(south, choiceElement), currentChoices);
                    saturation += 1;
                    break;
                  case 3:
                    mazeGrid.grid[choiceElement[0]][choiceElement[1]-1] = true;
                    currentChoices = aM.Append(aM.addVectors(west, choiceElement), currentChoices);
                    saturation +=1;
                    break;
                }
                break;
              }
            }
          } else { //bendy option doesn't exist, so do anything
            while (true) {
              choiceIndex = rand.nextInt(dirrections[1].length);
              if (dirrections[1][choiceIndex]) {
                switch(choiceIndex) {
                  case 0:
                    mazeGrid.grid[choiceElement[0]+1][choiceElement[1]] = true;
                    currentChoices = aM.Append(aM.addVectors(north, choiceElement), currentChoices);
                    saturation += 1;
                    break;
                  case 1:
                    mazeGrid.grid[choiceElement[0]][choiceElement[1]+1] = true;
                    currentChoices = aM.Append(aM.addVectors(east, choiceElement), currentChoices);
                    saturation += 1;
                    break;
                  case 2:
                    mazeGrid.grid[choiceElement[0]-1][choiceElement[1]] = true;
                    currentChoices = aM.Append(aM.addVectors(south, choiceElement), currentChoices);
                    saturation +=1;
                    break;
                  case 3:
                    mazeGrid.grid[choiceElement[0]][choiceElement[1]-1] = true;
                    currentChoices = aM.Append(aM.addVectors(west, choiceElement), currentChoices);
                    saturation += 1;
                    break;
                  }
                  break;
                }
              }
            }
          } else {
            if (FurtherMovesPossible(dirrections[2], true)) { //straight option exists!
              while (true) {
                choiceIndex = rand.nextInt(dirrections[1].length);
                if (dirrections[1][choiceIndex] && dirrections[2][choiceIndex]) {
                  switch(choiceIndex) {
                    case 0:
                      mazeGrid.grid[choiceElement[0]+1][choiceElement[1]] = true;
                      currentChoices = aM.Append(aM.addVectors(north, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    case 1:
                      mazeGrid.grid[choiceElement[0]][choiceElement[1]+1] = true;
                      currentChoices = aM.Append(aM.addVectors(east, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    case 2:
                      mazeGrid.grid[choiceElement[0]-1][choiceElement[1]] = true;
                      currentChoices = aM.Append(aM.addVectors(south, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    case 3:
                      mazeGrid.grid[choiceElement[0]][choiceElement[1]-1] = true;
                      currentChoices = aM.Append(aM.addVectors(west, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                  }
                  break;
                }
              }
            } else { //straight option doesn't exist, so do anything
              while (true) {
                choiceIndex = rand.nextInt(dirrections[1].length);
                if (dirrections[1][choiceIndex]) {
                  switch(choiceIndex) {
                    case 0:
                      mazeGrid.grid[choiceElement[0]+1][choiceElement[1]] = true;
                      currentChoices = aM.Append(aM.addVectors(north, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    case 1:
                      mazeGrid.grid[choiceElement[0]][choiceElement[1]+1] = true;
                      currentChoices = aM.Append(aM.addVectors(east, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    case 2:
                      mazeGrid.grid[choiceElement[0]-1][choiceElement[1]] = true;
                      currentChoices = aM.Append(aM.addVectors(south, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    case 3:
                      mazeGrid.grid[choiceElement[0]][choiceElement[1]-1] = true;
                      currentChoices = aM.Append(aM.addVectors(west, choiceElement), currentChoices);
                      saturation += 1;
                      break;
                    }
                    break;
                  }
                }
              }
            }
          }
        }
        //now we will put in the end
        var i=0;
        while (!mazeGrid.grid[i][width-2]) {
          mazeGrid.grid[i][width-2] = true;
          i+=1;
        }
      }

}
