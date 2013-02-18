/*
 * The main point of this class is to be a superclass for SmartPlayer and BlindPlayer
 * You could also have different players with different strategies
 * The key function they'll share in common is moveCardtoStack methods, and makeMove methods
 */
package solitaire;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import solitaire.SCard.Suit;

/**
 *
 * @author Mike
 */
public class SPlayer {
    Stack<String> moves = new Stack<String>(); // this will keep track of all the moves
    public SGame game; // stored in super so that both classes can access it
    
    // empty constructor
    public SPlayer(SGame game){
        this.game = game;
    }
    
    /*
     * This method is for moving a card to a SUIT STACK
     * It returns an integer of the correct stack pile IF the card can be moved there
     *  otherwise it returns -1
     */
    //public int moveCardtStack(SCard card, SStack[] sstack){
    public int moveCardtStack(SStack fromStack, SStack[] sstack){
        if (fromStack.up.isEmpty()) return -1;
        SCard card = fromStack.up.peek();
        //boolean toReturn = false; // it'll get set to true "if can"
        int toReturn = -1; // it'll get set to true "if can"
        //System.out.println("LENGTH IS "+sstack.length);
        // This is where we're seeing if a card can be moved from (anywhere) to the suit stack
        // check to see suit space for card is empty, if so we're looking for an ACE
        Suit forCard = card.suit();
        // Loop through the Suit stack to see which one matches the suit
        for (int i=0; i<4;i++){
            if (sstack[i].up.isEmpty()) continue;
            if (forCard == sstack[i].up.peek().suit()){
                // found it!!!
                //System.out.println("found out which stack it is: "+i);
                // this looks tricky, but it's basically saying:
                // if the highest card in the suit stack is of ONE LESS ordinal value, add it
                if (card.rank().ordinal() - sstack[i].up.peek().rank().ordinal() == 1){
                    // this is where it CAN be done
                    //System.out.println("YOU CAN PUT THIS ON THE SUIT STACK");
                    toReturn = i;
                }
            }
        }
        return toReturn;
    }

    /*
     * This method is for moving a card to the 7 PILES
     */
    //boolean moveCardtStack(SCard card, SStack sstack) {
    boolean moveCardtStack(SStack fromStack, SStack sstack) {
        boolean toReturn = false;
        if (fromStack.up.isEmpty()) return false;
        SCard card = fromStack.up.peek();
        // Right off the bat, need to make exception for when a KING may go into EMPTY PILE
        if (sstack.up.isEmpty()) {
            // Need to get in here so we don't throw errors for the rest of the cases
            if (card.rank() == SCard.Rank.KING) {
                // We CAN move this card to the empty stack
                // Also, get the hell out of this method before it breaks stuff
                return true;
            }
            else{
                // Get the hell out of this method before it breaks anything
                return false;
            }
        }
        
        // This is a fancy if statement that says, if the rank on the stack is ONE HIGHER than 
        //  the card, AND one is BLACK and the other is RED
        if ((sstack.up.peek().rank().ordinal() - card.rank().ordinal() == 1) && 
                (card.isBlack() == sstack.up.peek().isRed())){
            //System.out.println("ATTENTION: Card "+card+" will fit under "+sstack.up.peek());
            toReturn = true;
        }
        return toReturn;
    }
    
    /*
     * This method is for moving cards in the middle of stacks
     * (Added later)
     */
    boolean moveMiddle(SStack fromStack, SStack sstack, int fromPilenum, int toPilenum, boolean smart) {
        boolean toReturn = false;
        // Take care of King contingency first
        if (sstack.up.isEmpty()){
            for (int i=0; i < fromStack.up.size(); i++){
                if (fromStack.up.get(i).rank() == SCard.Rank.KING) {
                    // We can move the King and all other cards
                    while (fromStack.up.size() > i){
                        //System.out.println("herehuh");
                        moves.add("<5) MOVE Pile["+fromPilenum+"] "+fromStack.up.get(i) +" to Pile["+toPilenum+"]>");
                        // if this has already been added to history, get outta here!
                        if (game.history.containsKey(moves.peek())){
                            moves.pop();
                            return false;
                        }
                        System.out.println(moves.peek()); 
                        // Add to history
                        game.history.put(moves.peek(), toPilenum); // the second argument is inconsequential
                        // ACTUALLY MOVE THE CARD
                        sstack.up.add(fromStack.up.get(i));
                        fromStack.up.remove(i);
                    }                    
                    //return true;
                }
                else
                    return false;
            }
        } // Done with King contingency
        
       SCard card;
        for (int i=0;i<fromStack.up.size()-1;i++){
            // Get a Card class from every Card in the fromStack, check it against sstacks's top card
            card = fromStack.up.get(i);
            if ((sstack.up.peek().rank().ordinal() - card.rank().ordinal() == 1) && 
                    (card.isBlack() == sstack.up.peek().isRed())){
                    // For this method, we go ahead and do the move here
                    while (fromStack.up.size() > i){
                        //System.out.println("whats i??? "+i+" size of fromStack.up is "+fromStack.up.size());
                        moves.add("<5) MOVE Pile["+fromPilenum+"] "+fromStack.up.get(i) +" to Pile["+toPilenum+"]'s "+sstack.up.peek()+">");
                        // if this has already been added to history, get outta here!
                        // This is where we move all the cards leftwards
                        if (game.history.containsKey(moves.peek())){
                            if (smart){
                                // this is unique to the SmartPlayer class
                                if (i<toPilenum){
                                    moves.pop();
                                    return false;
                                }
                            }
                            else{
                                moves.pop();
                                return false;
                            }
                        }
                        System.out.println(moves.peek()); 
                        // Add to history
                        game.history.put(moves.peek(), toPilenum); // the second argument is inconsequential
                        // ACTUALLY MOVE THE CARD
                        sstack.up.add(fromStack.up.get(i));
                        fromStack.up.remove(i);
                    }                        
                    //toReturn = true;
            }            
        }
         
        return toReturn;
    }
    
}