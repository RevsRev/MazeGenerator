import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class MazeWindow extends JPanel {

  public Maze maze;
  public int window_x;
  public int window_y;

  public MazeWindow(int window_x, int window_y, Maze maze) {
    super();
    this.window_x = window_x;
    this.window_y = window_y;
    this.maze = maze;

    setSize(window_x, window_y);
    setLayout(null);
    setVisible(true);
  }

  public void paintMaze(Graphics g) {
    boolean[][] grid = maze.mazeGrid.grid;
    int boxWidth = window_x/maze.width;
    int boxHeight = window_y/maze.height;
    int xSlack = (window_x - boxWidth*maze.width)/2;
    int ySlack = (window_y - boxHeight*maze.height)/2;

    for (int i = 0; i < maze.height; i++) {
      for (int j=0; j < maze.width; j++) {
        if (grid[i][j]) {
          g.setColor(Color.WHITE);
        } else {
          g.setColor(Color.BLACK);

        }
        g.fillRect(xSlack+j*boxWidth, ySlack+i*boxHeight, boxWidth, boxHeight);
      }
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    BufferedImage bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics g_buff = (Graphics) bufferImage.getGraphics();
    paintMaze(g_buff);

    g.drawImage(bufferImage,0,0,null);
  }

}
