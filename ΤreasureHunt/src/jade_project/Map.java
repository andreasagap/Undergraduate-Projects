package jade_project;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    public static final Color AGENT1 = new Color(214,217,223);
    public static final Color TREASURE = new Color(255,204,102);
    public static final Color OTHER = new Color(29,153,0);
    public static final Color AGENT2 = new Color(153,0,0);
    public static final Color LAKE = new Color(26,194,255);
    public static final Color SEA = new Color(0,3,153);
    private boolean flag=true;
    public static final Color[] TERRAIN = {
            AGENT1,
            TREASURE,
            OTHER,
            AGENT2,
            LAKE
    };

    public static final int NUM_ROWS = 100;
    public static final int NUM_COLS = 100;

    public static final int PREFERRED_GRID_SIZE_PIXELS = 10;

    // In reality you will probably want a class here to represent a map tile,
    // which will include things like dimensions, color, properties in the
    // game world.  Keeping simple just to illustrate.
    private final Color[][] terrainGrid;

    public Map(){
        this.terrainGrid = new Color[NUM_ROWS][NUM_COLS];
        // Randomize the terrain
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                this.terrainGrid[i][j] = OTHER;
            }
        }
        int preferredWidth = NUM_COLS * PREFERRED_GRID_SIZE_PIXELS;
        int preferredHeight = NUM_ROWS * PREFERRED_GRID_SIZE_PIXELS;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));


    }
    public void setLake(int i,int j)
    {
        this.terrainGrid[i][j] = LAKE;
    }
    public void setSea(int i,int j)
    {
        this.terrainGrid[i][j] = SEA;
    }
    public void setTreasure(int i,int j)
    {
        this.terrainGrid[i][j] = TREASURE;
    }
    public void setAgent(int i,int j,String t)
    {
        if(t.equalsIgnoreCase("player1") || t.equalsIgnoreCase("player2"))
        {
            this.terrainGrid[i][j] = AGENT2;
        }
        else{
            this.terrainGrid[i][j] = AGENT1;
        }

    }
    public void setWinner(String team)
    {
        if(flag)
        {
            flag=false;
            JOptionPane.showMessageDialog(null, "The "+team + " team won!", "Winner", JOptionPane.INFORMATION_MESSAGE);
        }

    }
    public void setOld(int i,int j)
    {

            this.terrainGrid[i][j] = OTHER;


    }
    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        int rectWidth = getWidth() / NUM_COLS;
        int rectHeight = getHeight() / NUM_ROWS;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                // Upper left corner of this terrain rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                Color terrainColor = terrainGrid[i][j];
                g.setColor(terrainColor);
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
        g.dispose();
    }


}