/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jade_project;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

import java.util.Random;

import static jade_project.Jade_project.lakes;
import static jade_project.Jade_project.treasure;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 *
 */
public class MyAgent extends Agent {

    private Coord oldPos;
    private Coord pos;
    private String playmate;
    private String team;
    private ArrayList<Coord> older = new ArrayList<>();
	private boolean win=false;
    private static final String BEHAVIOUR_INIT = "init";
    private static final String BEHAVIOUR_PLAY = "play";
    private static final String BEHAVIOUR_END = "end";

    @Override
    protected void setup() {
        playmate=getArguments()[1].toString();
        team=getArguments()[0].toString();
        System.out.println(team+" "+getLocalName());

        FSMBehaviour behaviour = new FSMBehaviour(this);

        //States
        behaviour.registerFirstState(new InitBehaviour(this), BEHAVIOUR_INIT);
        behaviour.registerState(new PlayBehaviour(this), BEHAVIOUR_PLAY);
        behaviour.registerLastState(new EndBehaviour(this), BEHAVIOUR_END);

        //Transitions
        behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_PLAY);
        behaviour.registerTransition(BEHAVIOUR_PLAY, BEHAVIOUR_PLAY, 1);
        behaviour.registerTransition(BEHAVIOUR_PLAY, BEHAVIOUR_END, 0);

        addBehaviour(behaviour);
    }

    @Override
    protected void takeDown() {
        System.out.println(getLocalName()+" Bye Bye!");
    }

    public void initGame() {
        if(getLocalName().equalsIgnoreCase("player1"))
        {
            setPos(new Coord(0, 0));
        }
        else if(getLocalName().equals("player2"))
        {
            setPos(new Coord(0, 98));
        }
        else if(getLocalName().equalsIgnoreCase("player3"))
        {

            setPos(new Coord(0, 1));
        }
        else {
            setPos(new Coord(0, 99));
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //int x = Jade_project.lakes_pos[0][0];
    }

    public boolean determineNextMove() {
        win = false;
        eliminateCandidates();
        if (pos.x == treasure.x && pos.y == treasure.y) {
            win = true;
        }
        return win;
        
    }

    private void eliminateCandidates() {
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        boolean found_treasure = false;
        /*Adding elements to HashMap*/
        hmap.put("up", checkState("up"));
        hmap.put("down", checkState("down"));
        hmap.put("right", checkState("right"));
        hmap.put("left", checkState("left"));
        
        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry m = (Map.Entry) iterator.next();

            if (Integer.parseInt(m.getValue().toString()) == 2) {
                move(m.getKey().toString());
                found_treasure = true;
                break;
            }
        }
        ArrayList<String> list = new ArrayList<>();
        if (!found_treasure) {

            Set set1 = hmap.entrySet();
            Iterator iterator1 = set1.iterator();
            while (iterator1.hasNext()) {
                Map.Entry m = (Map.Entry) iterator1.next();
                if (Integer.parseInt(m.getValue().toString()) == 3) {
                    list.add(m.getKey().toString());

                }
            }
            Random rand = new Random();
            if (list.size() > 0) {
                move(list.get(rand.nextInt(list.size()))); // free state choose randomly

            } 
            else {
                list.clear();
                Set set2 = hmap.entrySet();
                Iterator iterator2 = set2.iterator();
                while (iterator2.hasNext()) {
                    Map.Entry m = (Map.Entry) iterator2.next();
                    if (Integer.parseInt(m.getValue().toString()) == 1) {
                        list.add(m.getKey().toString());

                    }
                }
                
                move(list.get(rand.nextInt(list.size())));

            }
            checkOlderList(oldPos);

            
        }
        Jade_project.map.setAgent(pos.x,pos.y,getLocalName());
        Jade_project.map.setOld(oldPos.x,oldPos.y);
        Jade_project.map.repaint();

    }
    public void checkOlderList(Coord c)
    {
        boolean f=true;
        for (Coord anOlder : older) {
            if (c.x == anOlder.x && anOlder.y == c.y) {
                f = false;
                break;
            }
        }
        if(f)
        {
            older.add(oldPos);
        }
    }
    public int checkState(String x) {
        Coord p = pos.clone();      //out of bound + lakes -->0
        // again -->1    , treasure-->2   , free state--> 3
        if (x.equals("up")) {
            p.y--;
            for (int i = 0; i < lakes.size(); i++) {

                if (lakes.get(i).x == p.x && lakes.get(i).y == p.y) {
                    return 0;
                }
            }
            if (p.x < 0 || p.x >= Jade_project.MAXX || p.y < 0 || p.y >= Jade_project.MAXY) {
                return 0;
            }
            if (p.x == treasure.x && p.y == treasure.y) {
                return 2;
            }

            for (int i = 0; i < older.size(); i++) {

                        if (older.get(i).x == p.x && older.get(i).y == p.y) {
                            return 1;
                        }
            }

        }
        if (x.equals("down")) {
            p.y++;
            for (int i = 0; i < lakes.size(); i++) {

                if (lakes.get(i).x == p.x && lakes.get(i).y == p.y) {
                    return 0;
                }
            }
            if (p.x < 0 || p.x >= Jade_project.MAXX || p.y < 0 || p.y >= Jade_project.MAXY) {
                return 0;
            }
            if (p.x == treasure.x && p.y == treasure.y) {
                return 2;
            }
            
           for (int i = 0; i < older.size(); i++) {

                        if (older.get(i).x == p.x && older.get(i).y == p.y) {
                            return 1;
                        }
            }
            
        }
        if (x.equals("left")) {
            p.x--;
            for (int i = 0; i < lakes.size(); i++) {

                if (lakes.get(i).x == p.x && lakes.get(i).y == p.y) {
                    return 0;
                }
            }
            if (p.x < 0 || p.x >= Jade_project.MAXX || p.y < 0 || p.y >= Jade_project.MAXY) {
                return (0);
            }
            if (p.x == treasure.x && p.y == treasure.y) {
                return 2;
            }
            for (int i = 0; i < older.size(); i++) {

                        if (older.get(i).x == p.x && older.get(i).y == p.y) {
                            return 1;
                        }
            }
        }
        if (x.equals("right")) {
            p.x++;
            for (int i = 0; i < lakes.size(); i++) {

                if (lakes.get(i).x == p.x && lakes.get(i).y == p.y) {
                    return 0;
                }
            }
            if (p.x < 0 || p.x >= Jade_project.MAXX || p.y < 0 || p.y >= Jade_project.MAXY) {
                return (0);
            }
            if (p.x == treasure.x && p.y == treasure.y) {
                return 2;
            }
            for (int i = 0; i < older.size(); i++) {

                        if (older.get(i).x == p.x && older.get(i).y == p.y) {
                            return 1;
                        }
            }
        }
        return 3;
    }

    public void move(String direction) {
        oldPos = getPos().clone();
        getPos().move(direction);
    }

    public Coord getPos() {
        return pos;
    }

    public void setPos(Coord pos) {
        this.pos = pos;
    }

    public class PlayBehaviour extends OneShotBehaviour {

        private static final long serialVersionUID = -2850535188580377701L;
        MyAgent player;
        int nextState;

        public PlayBehaviour(MyAgent p) {
            this.player = p;
            nextState = 1;
        }

        @Override
        public void action() {
			ACLMessage msg=receive();
            if(msg!=null)
            {
                if(msg.getContent().equalsIgnoreCase("I find the treasure"))
                {
                    nextState=0;
                    return;
                }
                else
                {
                    Coord k=new Coord();
                    k.x=Integer.parseInt(msg.getContent().split(" ")[0]);
                    k.y=Integer.parseInt(msg.getContent().split(" ")[1]);
                    checkOlderList(k);
                }

            }
            boolean win = determineNextMove();

            if (win) {
                ACLMessage mg=new ACLMessage(ACLMessage.INFORM);
                for (int i = 1; i<=4; i++)
                    mg.addReceiver( new AID( "player" + i, AID.ISLOCALNAME) );
                mg.setLanguage("English");
                mg.setOntology("treasure");
                mg.setContent("I find the treasure");
                send(mg);
                nextState = 0;

            }
            else{
                ACLMessage mg=new ACLMessage(ACLMessage.INFORM);
                mg.addReceiver( new AID( playmate, AID.ISLOCALNAME) );
                mg.setLanguage("English");
                mg.setOntology("Information");
                mg.setContent(oldPos.x+" "+oldPos.y);
                send(mg);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int onEnd() {
            return nextState;
        }

    }

    public class InitBehaviour extends OneShotBehaviour {

        private static final long serialVersionUID = -4395908709321689561L;
        MyAgent player;

        public InitBehaviour(MyAgent p) {
            player = p;
        }

        @Override
        public void action() {
            //Wait for message to start
            player.initGame();
        }

    }

    public class EndBehaviour extends OneShotBehaviour {

        private static final long serialVersionUID = 3144287550765838023L;
        MyAgent player;

        public EndBehaviour(MyAgent p) {
            player = p;
        }

        @Override
        public void action() {
            if(win) {
                System.out.println(player.getAID().getLocalName() + "> I found the treasure at " + player.getPos() + "!");
                Jade_project.map.setWinner(team);
            }

            player.doDelete();
        }

    }

}
