/*
 * This sets up the stacks for the seven piles in the Tableau
 */
package solitaire;

import java.util.Stack;

/**
 *
 * @author Mike
 */

public class SStack {
    Stack<SCard> down = new Stack<SCard>(); // the cards facing down 
    Stack<SCard> up = new Stack<SCard>();   // the cards facing up that you can see
    
    public boolean stackEmpty(){
        // check to see if this is an empty stack
        if (down.size() + up.size() == 0)
            return true;
        else
            return false;
    }
    
    @Override
    public String toString(){
        if (up.size() == 0)
            return "NO CARD";
        else
            return up.get(up.size()-1).toString();
    }
    
}
