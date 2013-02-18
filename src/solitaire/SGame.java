/*
 * Initialize the game, by spreading out the deck
 * to the proper 7 piles
 */
package solitaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import solitaire.SStack;

/**
 *
 * @author Mike
 */
public class SGame {
    public SStack[] suit = new SStack[4];
    public SStack[] pile = new SStack[7];
    public SStack reserve = new SStack();
    public SStack reserveUsed = new SStack(); // use this to make sure we traverse Reserve thrice
    Map<String, Integer> history = new HashMap<String, Integer>(); // mitigate infinite loops
    public int RESERVE_COUNT = 3;
    
    //accessor for RESERVE_COUNT
    public int reserveCount(){
        return this.RESERVE_COUNT;
    }
    
    // constructor, called from Solitaire.java
    public SGame(ArrayList<String> arrDeck){
        // right now it's an array, make it into stack of card classes
        Stack<SCard> rawStack = new Stack<SCard>();
        
        // Initialize Suit stacks
        // Here we make a "fake" card with Enum of Zero so we can keep track of suits when empty
        // like at the beginning of the game.
        int i = 0; // we'll be using a lot of iterations right now
        
        
        for (i=0;i<4;i++){
            suit[i] = new SStack();
        }
        /*
        //SCard zeroSuit = new SCard(SCard.Rank.ZERO_SUIT_STACK, SCard.Suit.SPADES);
        SCard zeroSuit = new SCard(SCard.Rank.ZERO, SCard.Suit.SPADES);
        suit[0].up.add(zeroSuit);
        zeroSuit = new SCard(SCard.Rank.ZERO, SCard.Suit.CLUBS);
        //zeroSuit = new SCard(SCard.Rank.ZERO_SUIT_STACK, SCard.Suit.CLUBS);
        suit[1].up.add(zeroSuit);
        //zeroSuit = new SCard(SCard.Rank.ZERO_SUIT_STACK, SCard.Suit.DIAMONDS);
        zeroSuit = new SCard(SCard.Rank.ZERO, SCard.Suit.DIAMONDS);
        suit[2].up.add(zeroSuit);
        //zeroSuit = new SCard(SCard.Rank.ZERO_SUIT_STACK, SCard.Suit.HEARTS);
        zeroSuit = new SCard(SCard.Rank.ZERO, SCard.Suit.HEARTS);
        suit[3].up.add(zeroSuit);        
         */
        
        String cardStr = null, tmpFace = null, tmpSuit = null;
        Iterator it = arrDeck.iterator();        
        while (it.hasNext()){
            cardStr = (String) it.next().toString().trim();
            tmpFace = cardStr.substring(0, 1);
            tmpSuit = cardStr.substring(1, 2);
            SCard tmpSCard = new SCard(tmpFace, tmpSuit);
            //System.out.println(tmpFace+" of "+tmpSuit);
            rawStack.add(tmpSCard);
        }
        
        SCard tmpZero = new SCard("0","h");
        rawStack.add(tmpZero);
        tmpZero = new SCard("0","c");
        rawStack.add(tmpZero);
        tmpZero = new SCard("0","s");
        rawStack.add(tmpZero);
        tmpZero = new SCard("0","d");
        rawStack.add(tmpZero);
        
        
        //for debugging, this prints out the cards as toStrings
        /*
        System.out.println("test has "+ rawStack.size());
        for (i=0; i<rawStack.size();i++){
            //if (test.get(i) != null)
            System.out.println("WHATWEGOT: "+rawStack.get(i).toString());
        }
         */
        
        suit[0].up.add(rawStack.pop());
        suit[1].up.add(rawStack.pop());
        suit[2].up.add(rawStack.pop());
        suit[3].up.add(rawStack.pop());
        
        // now that the deck is in SCard classes, make SStacks
        
        for (i = 0;i<7;i++){
            pile[i] = new SStack();
        }
        
        
        pile[0] = new SStack();
        
        // Give the piles their cards, designating face up/down
        
        // Pile 1
        pile[0].up.add(rawStack.pop());
        // Pile 2
        for (i=0;i<1;i++){
            pile[1].down.add(rawStack.pop());
        }
        pile[1].up.add(rawStack.pop());
        // Pile 3
        for (i=0;i<2;i++){
            pile[2].down.add(rawStack.pop());
        }
        pile[2].up.add(rawStack.pop());
        // Pile 4
        for (i=0;i<3;i++){
            pile[3].down.add(rawStack.pop());
        }
        pile[3].up.add(rawStack.pop());
        // Pile 5
        for (i=0;i<4;i++){
            pile[4].down.add(rawStack.pop());
        }
        pile[4].up.add(rawStack.pop());
        // Pile 6
        for (i=0;i<5;i++){
            pile[5].down.add(rawStack.pop());
        }
        pile[5].up.add(rawStack.pop());
        // Pile 7
        for (i=0;i<6;i++){
            pile[6].down.add(rawStack.pop());
        }
        pile[6].up.add(rawStack.pop());
        
        /* for debugging, to confirm all the piles have the right amount
        for (i=0;i<7;i++){
            System.out.println("pile "+i+" has "+pile[i].down.size() +" down and "+pile[i].up.size()+" up");
        }
        // This shows there are 24 cards left for the reserve
        //System.out.println("the rest in rawStack is "+rawStack.size());
         */
        
        // Put the rest of the 24 cards in the reserve
        while (!rawStack.empty()){
            reserve.down.add(rawStack.pop());
        }
    }

    public void drawThree(){
        // This method draws three cards from the reserve pile (if there are three left)
        //System.out.println("inhere? DrawThree()");        
        if (reserve.down.size() >= 3){
            System.out.println("we in the TOP block");
            // should almost always land in this category
            // if there are already cards faced UP, put them into the bottom of reserveUsed stack
            if (reserve.up.size() > 0){ // if there are face up cards
                while (!reserve.up.empty()){
                    // here's how you add it to the "bottom" of the stack
                    // using .add or .push would not be proper
                    reserveUsed.down.insertElementAt(reserve.up.pop(), 0);
                }
            }
            // now pop three from the faced DOWN section to the UP section
            for(int i=0;i<3;i++){
                reserve.up.push(reserve.down.pop());
            }
        }
        else{
            System.out.println("we in the BOTTOM block");
            // There's not 3 cards left in the RESERVE DOWN, add whatever's left to the RESERVE USED
            if (reserve.up.size() > 0){ // if there are face up cards
                while (!reserve.up.empty()){
                    // here's how you add it to the "bottom" of the stack
                    // using .add or .push would not be proper
                    reserveUsed.down.insertElementAt(reserve.up.pop(), 0);
                }
            // Decrement counter for reserve
            this.RESERVE_COUNT--;
            // Move all RESERVED USED to RESERVE and reset RESERVE USED
            if (reserveUsed.down.size() > 0){
                while (!reserveUsed.down.isEmpty()){
                    reserve.down.add(reserveUsed.down.pop());
                }
            }
                // draw three just like they do in the computer game
                // unless there aren't that many cards, then set threeIfCan to the number of cards
                int threeIfCan = 3;
                if (reserve.down.size() < 3) threeIfCan = reserve.down.size();
                
                for(int i=0;i<threeIfCan;i++){
                    reserve.up.push(reserve.down.pop());
                }
                System.out.println("got past this");
            }
            else{
                // there are NO face UP cards and there are 2 or less DOWN cards
                // move all down cards to reserveUsed, then move all that to the new reserve
                while (!reserve.down.isEmpty()){
                    // put at bottom of stack.
                    reserveUsed.down.insertElementAt(reserve.down.pop(), 0);
                }
                this.RESERVE_COUNT--; //you get a pretty high score with this off
                while (!reserveUsed.down.isEmpty()){
                    reserve.down.add(reserveUsed.down.pop());
                }
                // draw three to make sure we have UP cards in the reserve

                for(int i=0;i<reserve.down.size();i++){
                    reserve.up.push(reserve.down.pop());
                }
            }
        }
    }
    
    public int getScore(){
        int pilesTotal = 0;
        for (int i=0;i<7;i++){
            pilesTotal += pile[i].down.size() + pile[i].up.size();
        }
        pilesTotal += reserve.down.size() + reserve.up.size();
        return 52-pilesTotal;
    }
}

//System.out.println("score "+getScore());
//System.out.println("reserve up and down "+reserve.up.size()+" and "+reserve.down.size());
//System.out.println("reserveUsed up and down "+reserveUsed.up.size()+" and "+reserveUsed.down.size());
