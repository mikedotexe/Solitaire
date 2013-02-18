/*
 * What would you do for a Klondike AI program?
 * (AI) hit the deck first
 * (AI) smart history is working against itself
 * (AI) don't move a card that's the last on a pile unless there's a King present, is the king covering other cards
 * moves is adding attempted stuff, have it pop it if it goes on to continue after seeing if its in the history
 *  look for moves.pop();
 * reconsider implementing //continue; // dont keep checking the same card once it's moved
 */

package solitaire;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mike
 */
public class Solitaire {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String choice = null;
        ArrayList<String> deck = null; // the deck we make the game from. random or CSV
        int thousand = 0;
        do{
            Scanner sysIn = new Scanner(System.in);
            System.out.println("Would you like to (O) open a deck file, (R) randomize, (Q) quit? Add a (V) to see Verbose Mode, all the comparisons. (rv, ov)");
            
            choice = "r";
            //choice = sysIn.nextLine();
            
            
            boolean verbose = true;
            if (choice.toLowerCase().indexOf("v") == -1) verbose = false;
                        
            if ("r".equals(choice.substring(0, 1).toLowerCase())){
                // they want a randomized deck
                deck = shuffledDeck();
                Iterator it = deck.iterator();
                System.out.println("Shuffled the random deck:");
                while (it.hasNext()){
                    System.out.print(it.next().toString()+", ");
                }
                System.out.println();
                System.out.println();
                SGame play = new SGame(deck);
                
                SSmartPlayer mike = new SSmartPlayer(play, verbose);
                //SBlindPlayer young_mike = new SBlindPlayer(play, verbose);
                
            }
            else if ("o".equals(choice.substring(0, 1).toLowerCase())){
                // open a file
                deck = openDeck();
                Iterator it = deck.iterator();
                System.out.println("READ FROM FILE");
                System.out.println();
                while (it.hasNext()){
                    System.out.print(it.next().toString()+", ");
                }                
                System.out.println();
                SGame play = new SGame(deck);    
                SSmartPlayer mike = new SSmartPlayer(play, verbose);
                //SBlindPlayer young_mike = new SBlindPlayer(play, verbose);
            }
            else {
                // they entered something weird or "Q", "q"
                // Pop off to the user unless they're trying to quit
                if (!"q".equals(choice.toLowerCase()))
                    System.out.println("Good for you and your "+choice+", let me ask again...");
            }
            thousand++;
            
        } while (thousand < 1000); // keep replaying the games until they Queet, brah
        //} while (!"q".equals(choice.toLowerCase())); // keep replaying the games until they Queet, brah
        
        System.out.println("------------------------");
        System.out.println("Yoo queet, brah.");
    }
    
    public static ArrayList<String> openDeck() throws FileNotFoundException, IOException{
        System.out.println();
        System.out.println("HIT ALT+TAB TO SEE JFILECHOOSER DIALOG");
        ArrayList<String> toReturn = new ArrayList<String>();
			
        JFileChooser fileopen = new JFileChooser();
        //FileFilter filter = new FileNameExtensionFilter("dat files", "dat");
        fileopen.addChoosableFileFilter(new FileNameExtensionFilter("Deck files", "txt", "csv"));
        File file = null;
        int ret = fileopen.showDialog(null, "Open deck file");
        // For some reason it shows up behind NetBeans, must Alt+Tab

        if (ret == JFileChooser.APPROVE_OPTION) {
          file = fileopen.getSelectedFile();
          System.out.println("Chose to open file " + file);
        }               
        else 
                System.out.println("Error, OPEN FILE NOT APPROVED");           
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        StringTokenizer st = null;
        
        while ((line = br.readLine()) != null){
            st = new StringTokenizer(line, ",");
            while (st.hasMoreTokens()){
                toReturn.add(st.nextToken());
            }
        }
        br.close();
        return toReturn;
    }
            
    public static ArrayList<String> shuffledDeck(){
        Stack tempSuits = new Stack<String>();
        Stack tempFace = new Stack<String>();
        
        for (int i=0;i<13;i++){
            // d = diamond, s = spades, h = hearts c = clubs
            tempSuits.push("d"); 
        }
        for (int i=0;i<13;i++){
            // d = diamond, s = spades, h = hearts c = clubs
            tempSuits.push("s");
        }
        for (int i=0;i<13;i++){
            // d = diamond, s = spades, h = hearts c = clubs
            tempSuits.push("h");
        }
        for (int i=0;i<13;i++){
            // d = diamond, s = spades, h = hearts c = clubs
            tempSuits.push("c");
        }
        for (int i=0;i<4;i++){
            tempFace.push("K");
            tempFace.push("Q");
            tempFace.push("J");
            tempFace.push("T");
            tempFace.push("9");
            tempFace.push("8");
            tempFace.push("7");
            tempFace.push("6");
            tempFace.push("5");
            tempFace.push("4");
            tempFace.push("3");
            tempFace.push("2");
            tempFace.push("A");
        }

        // pop them into an array
        List toReturn = new ArrayList<String>();
        Iterator it = tempSuits.iterator();
        while (it.hasNext()){
            //System.out.println(it.next());
            String combined = tempFace.pop().toString() + tempSuits.pop().toString();
            toReturn.add(combined);
            //System.out.println("unique? "+combined);
        }
        
        // shuffle them
        Collections.shuffle(toReturn);
        
        return (ArrayList<String>) toReturn;
    }
}

            // Play the game
            //playa = new BlindPlayer(input.toString());
            //playa.play();           
            // Lay the cards out into stacks after converting them into Card classes
            
            //SCard king = new SCard(SCard.Rank.KING, SCard.Suit.HEARTS);
            //SCard queen = new SCard(SCard.Rank.QUEEN, SCard.Suit.HEARTS);
            //System.out.println("king "+test2.rank().ordinal());
            
            /*
             * 
            Stack test = new Stack();
            Stack testTo = new Stack();
            int Mike = 2;
            for (int i=0; i<5; i++){
                test.add(i*5);
            }
            System.out.println("haz");
            for (int i=0; i<5; i++){
                System.out.println(test.get(i));
            }
            //System.out.println(Mike+" is "+test.get(Mike));
            System.out.println();
            //for (int i=Mike;i<test.size();i++){
            while (test.size() > Mike){
                testTo.add(test.get(Mike));
                test.remove(Mike);
            }
             * 
             */
                
            //}
            
            //test.remove(Mike);
            //System.out.println(Mike+" is "+test.get(Mike));
            /*
            Iterator it0 = test.iterator();
            while (it0.hasNext()){
                System.out.println("Original Test is: "+it0.next());
            }
            Iterator it = testTo.iterator();
            while (it.hasNext()){
                System.out.println("TestTo is: "+it.next());
            }
             * 
             */

            /*
            for (int i=0;i<test.size();i++){
                System.out.println("test["+i+"] = "+test.get(i));
            }
             */