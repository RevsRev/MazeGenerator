import javax.swing.*;

public class MazeGenerator {

  JFrame mFrame = new JFrame("Maze");

  public static void main(String []args) {
    Maze maze = new Maze(200,200);
    MazeWindow mW = new MazeWindow(800,800,maze);
    JFrame mFrame = new JFrame("Maze");

    mFrame.setSize(800,800+20);
    mFrame.setLayout(null);
    mFrame.setVisible(true);
    mFrame.add(mW);
    mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mW.repaint();
  }

}
