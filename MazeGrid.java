/*A maze of size (m,n) is defined to be an mxn array of booleans. A section of the maze is
traversible if the boolean value is true, else it is not traversible */

public class MazeGrid {
  boolean[][] grid;

  public MazeGrid(int height, int width) {
    initializeGrid(height, width);
  }

  public void initializeGrid(int height, int width) {
    grid = new boolean[height][width];
    for (int i=0; i<height; i++) {
      for (int j=0; j<width; j++) {
        grid[i][j] = false;
      }
    }
  }

}
