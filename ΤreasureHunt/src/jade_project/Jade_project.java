/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jade_project;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 *
 */
public class Jade_project {

    public static ArrayList<Coord> lakes = new ArrayList<>();
    public static Coord treasure;
    public static final int MAXX = 100;
    public static final int MAXY = 100;
    public static Map map;
    protected void Generate_map() {
        map=new Map();
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {    //we want 3 lakes

            int x1 = rand.nextInt(10) + 20* i+1;
            int y1 = rand.nextInt(10) + 20* i+1;
            int m = rand.nextInt(20) + 10;
            int p = rand.nextInt(20) + 10;

            for (int j = x1; j <= x1 + m; j++) {
                for (int jj = y1; jj <= y1 + p; jj++) {  //list with lake position coordinates
                    lakes.add(new Coord(j, jj));
                    map.setLake(j,jj);
                }
            }
        }
        sea(99);
        sea(98);
        sea(97);
        sea(96);

            //setting treasure point 
        boolean flag;
        int tx, ty;
        do {
            flag = false;
            tx = rand.nextInt(MAXX);
            ty = rand.nextInt(MAXY);
            for (Coord c : lakes) {
                if (c.x == tx && c.y == ty || (tx + ty == 0)) {
                    flag = true;
                }
            }
        } while (flag);
        treasure = new Coord(tx, ty);
        map.setTreasure(tx,ty);
    }
    private void sea(int x){
        for (int jj = 0; jj <= 99; jj++) {  //list with lake position coordinates
            lakes.add(new Coord(x, jj));
            map.setSea(x,jj);
        }
    }
    public static void main(String[] args) {

        Runtime runtime = Runtime.instance();
        Profile config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "true");
        AgentContainer mc = runtime.createMainContainer(config);
        AgentController red1;
        AgentController red2,white1,white2;

        Jade_project project = new Jade_project();
        try {
            System.out.println("start");
            String[] arg1 = new String[2];
            arg1[0] = "red";
            arg1[1]="player2";
            red1 = mc.createNewAgent("player1", MyAgent.class.getName(), arg1);
            String[] arg2 = new String[2];
            arg2[0] = "red";
            arg2[1]="player1";
            red2 = mc.createNewAgent("player2", MyAgent.class.getName(), arg2);
            String[] arg3 = new String[2];
            arg3[0] = "white";
            arg3[1]="player4";
            white1 = mc.createNewAgent("player3", MyAgent.class.getName(), arg3);
            String[] arg4 = new String[2];
            arg4[0] = "white";
            arg4[1]="player3";
            white2 = mc.createNewAgent("player4", MyAgent.class.getName(), arg4);
            System.out.println("created agents");
            project.Generate_map();
            System.out.println("created map");
            JFrame frame = new JFrame("Game");
            frame.add(map);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            red1.start();
            red2.start();
            white1.start();
            white2.start();
            System.out.println("agents start");
        } catch (StaleProxyException e) {
        }

    }

}
