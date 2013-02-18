/*
 * Heres the moves that the smart player makes
 */
package solitaire;
import java.io.*;
import java.util.Stack;

public class SBlindPlayer extends SPlayer {
    Stack<String> recipMoves = new Stack<String>(); // Reciprocal moves, so we don't move it to where it was
    public boolean QCDebug = false; // Quality Control, this will output every comparison to make sure it's working properly. Feel free to check it out.
  
    public SBlindPlayer(SGame game, boolean verbose){
        super(game);
        if (verbose) QCDebug = true;
        play();
    }
    public void play(){

        // Blind Player doesn't hit the deck first like the Smart Player, so the sequence below if different

        // Sequence of moves
        // 3) See if items on Piles can go to Suit stacks
        // 4) See if items on Piles can go to other Piles
        // 5) Test cards in middle of piles
        // 1) See if Reserve can go on Suit stacks
        // 2) See if Reserve can go on Piles       

        boolean canMove = false;
        int canSuit = -1;
        // before we loop through these steps, we check to see if we're in a deadlock.
        //
        boolean deadLock; // to be used in the do while below
        
        do{
            // Set deadlock to TRUE inside loop, then anytime a card is moved, it'll FALSE it
            deadLock = true;

            // 3) See if items on Piles can go to Suit stacks
            do{
                // Can an item on a pile go in the Suit Stack?
                for (int i=0;i<7;i++){
                        //Quality Control Debugging
                        if (QCDebug){
                            if (game.pile[i].up.isEmpty())
                                System.out.println("\tTESTING 3): Pile["+i+"] EMPTY to SUIT STACK");
                            else
                                System.out.println("\tTESTING 3): Pile["+i+"] "+game.pile[i].up.peek()+" to SUIT STACK");
                        }
                    //canSuit = super.moveCardtStack(game.pile[i].up.peek(), game.suit);
                    canSuit = super.moveCardtStack(game.pile[i], game.suit);
                    if (canSuit != -1){
                        // Found a match, so move it
                        moves.add("<3) PILE["+i+"]: "+game.pile[i].up.peek()+" to Suit STACK["+canSuit+"]>");
                        // if this has already been added to history, get outta here!
                        if (game.history.containsKey(moves.peek())){
                            canSuit = -1;
                            moves.pop();
                            continue;
                        }
                        System.out.println(moves.peek()); 
                        // Add to history
                        game.history.put(moves.peek(), canSuit); // the second argument is inconsequential
                        // ACTUALLY MOVE THE CARD
                        game.suit[canSuit].up.add(game.pile[i].up.pop());
                        // Flip 'um?
                        needFlipUm(i);
                        deadLock = false;
                    }
                }
            } while (canSuit != -1);

            // 4) See if items on Piles can go to other Piles
            boolean breakme = false;
            do{
                //System.out.println("db.stuck here? (canMove is "+canMove+")");
                // Can items on Piles go to other Piles?
                // Notice the SmartPlayer does this part in REVERSE ORDER
                //System.out.println("break is" + breakme);
                if (breakme){
                    canMove = false;
                    break;
                }
                int i=0;
                for (i=0;i<7;i++){
                    //System.out.println("db.getting here in i?"+ breakme);
                    if (breakme) break;
                    for (int j=0;j<7;j++){
                        //System.out.println("db.getting here in j?"+ breakme);
                        if (game.pile[i].up.isEmpty()){
                            if (i > 0){
                                breakme=true;
                                canMove = false;
                                continue; // cant move crap from an empty pile
                            }
                            else{
                                breakme = true;
                                canMove = false;
                                continue;
                            }
                        } 

                        //if (j==i) continue; // don't test against itself
                            //Quality Control Debugging
                            if (QCDebug){
                                if (game.pile[j].up.isEmpty())
                                        System.out.println("\tTESTING 4): Pile["+i+"] "+game.pile[i].up.peek()+" to Pile["+j+"] (EMPTY)");
                                else
                                        System.out.println("\tTESTING 4): Pile["+i+"] "+game.pile[i].up.peek()+" to Pile["+j+"]'s "+game.pile[j].up.peek());
                            }
                            //canMove = super.moveCardtStack(game.pile[i].up.peek(), game.pile[j]);
                            canMove = super.moveCardtStack(game.pile[i], game.pile[j]);
                        if (canMove){
                            // Found a match, so move it
                                moves.add("<4) Pile["+i+"] "+game.pile[i].up.peek()+" to Pile["+j+"]>");
                                //recipMoves.add("<4) Pile["+j+"] "+game.pile[j].up.peek()+" to Pile["+i+"] "+game.pile[i].up.peek()+">");
                                // if this has already been added to history, get outta here!
                                if (game.history.containsKey(moves.peek())){
                                        canMove = false;
                                        moves.pop();
                                        continue;
                                }
                                
                                System.out.println(moves.peek()); 
                                // Add to history
                                game.history.put(moves.peek(), canSuit); // the second argument is inconsequential
                                // ACTUALLY MOVE THE CARD
                                game.pile[j].up.add(game.pile[i].up.pop());
                                // Flip 'um?
                                needFlipUm(i);   
                                deadLock = false;
                                //continue; // dont keep checking the same card once it's moved
                        }
                    }
                    //System.out.println("db.4)canmove is "+canMove);
                }
            } while (canMove);
            
            
            // 5) Test cards in middle of piles
            do{
                for (int i=0;i<7;i++){
                    if (game.pile[i].up.size() > 1){
                        for (int j=0;j<7;j++){
                            if (QCDebug) System.out.println("\tTESTING 5): MidPile["+i+"] to Pile["+j+"]");
                            canMove = super.moveMiddle(game.pile[i], game.pile[j], i, j, true);
                            if (canMove) {
                                // By this time the cards have already been moved       
                                needFlipUm(i);   
                                //deadLock = false;                                
                            }
                        }
                    }
                }
            } while (canMove);

            // 1) See if Reserve can go on Suit stacks
            do{
                // Can you move the Reserve top card to the Suit Stack?
                        //Quality Control Debugging
                        if (QCDebug) System.out.println("\tTESTING 1): RESERVE "+game.reserve.up.peek()+" to SUIT STACK");
                //canSuit = super.moveCardtStack(game.reserve.up.peek(), game.suit);
                canSuit = super.moveCardtStack(game.reserve, game.suit);
                if (canSuit != -1){
                    // Found a match, so move it
                    moves.add("<1) RESERVE: "+game.reserve.up.peek()+" to <Suit STACK["+canSuit+"]>");
                    // if this has already been added to history, get outta here!
                    if (game.history.containsKey(moves.peek())){
                        canSuit = -1;
                        moves.pop();
                        continue;
                    }
                    System.out.println(moves.peek()); 
                    // Add to history
                    game.history.put(moves.peek(), canSuit); // the second argument is inconsequential
                    // ACTUALLY MOVE THE CARD
                    game.suit[canSuit].up.add(game.reserve.up.pop());
                    // Check to see if we need to deal 3 more cards from Reserve
                    need3More();
                    deadLock = false;
                }
            } while (canSuit != -1); // keep going until you can't anymore, then to next intelligent move

            // 2) See if Reserve can go on Piles
            do{
                // Can you move the Reserve top card to one of the 7 Piles?
                for (int i=0;i<7;i++){
                        //Quality Control Debugging
                        if (QCDebug) System.out.println("\tTESTING 2): "+game.reserve.up.peek()+" to "+ game.pile[i]);
                    //canMove = super.moveCardtStack(game.reserve.up.peek(), game.pile[i]);
                    canMove = super.moveCardtStack(game.reserve, game.pile[i]);
                    if (canMove){
                        // Found a match, so move it
                        moves.add("<2) RESERVE: "+game.reserve.up.peek()+" to Pile["+ i+"]>");
                        // if this has already been added to history, get outta here!
                        if (game.history.containsKey(moves.peek())){
                            canMove = false;
                            moves.pop();
                            continue;
                        }
                        System.out.println(moves.peek()); 
                        
                        // Add to history
                        game.history.put(moves.peek(), canSuit); // the second argument is inconsequential
                        // ACTUALLY MOVE THE CARD
                        game.pile[i].up.add(game.reserve.up.pop());
                        // Check to see if we need to deal 3 more cards from Reserve
                        need3More();
                        //System.out.println("db-error before this?");
                        deadLock = false;
                    }
                }
            } while (canMove); // keep going until you can't anymore, then to next intelligent move            
            
            if (deadLock == false) {
                System.out.println("Trying AI techniques again...");
            }
            else{
                // if deadlock is coming up true, make sure we've traversed the entire Reserve thrice
                if (game.RESERVE_COUNT > 0) deadLock = false;
                System.out.println("\tDrawing three");
                
                //System.out.println("score "+game.getScore());
                //System.out.println("reserve up and down "+game.reserve.up.size()+" and "+game.reserve.down.size());
                //System.out.println("reserveUsed up and down "+game.reserveUsed.up.size()+" and "+game.reserveUsed.down.size());
                
                game.drawThree();                
            }
            
        } while (deadLock == false);
        
        System.out.println("Exited with deadLock: "+deadLock+" and RESERVE_COUNT as "+game.RESERVE_COUNT);
        int Score = game.getScore();
        System.out.println("-------------------------------------------------------");
        System.out.println("SCORE: "+Score+" cards in Suit Stacks");
        System.out.println("-------------------------------------------------------");
        System.out.println();

        // Output this to a file with the name as a timestamp.
        System.out.println("TIME");
        java.util.Date today = new java.util.Date();
        System.out.println(new java.sql.Timestamp(today.getTime()));        
        String timeStamp = new java.sql.Timestamp(today.getTime()).toString();
        System.out.println("as variable is "+timeStamp);

        try{
            // Create file for results
             String text = Integer.toString(Score);
             FileWriter fstream = new FileWriter("BlindPlayer_output.txt",true);
             BufferedWriter out = new BufferedWriter(fstream);

             out.write(text);// save the score to the output file for each game.
             out.newLine();
             out.close();

         } catch (Exception e){//Catch exception if any
              System.err.println("Error: " + e.getMessage());
         }
        //System.out.println("Weve made a new Smart Player");
        //System.out.println("he sees that the size of the reserve is "+game.reserve.up.size());
    }
    
    public void need3More(){
        if (game.reserve.up.size() == 0){ // && game.reserve.down.size() > 0){
            // Deal three cards from the Reserve
            game.drawThree();
            // mark it in the moves Stack and print to screen
            moves.add("<Deal 3 cards from Reserve, RESERVE_COUNT = "+game.RESERVE_COUNT+">"); System.out.println(moves.peek());
            System.out.println("debug size of reserve up is "+game.reserve.up.size());
            System.out.println("debug size of reserve down is "+game.reserve.down.size());
        }        
    }
    
    public void needFlipUm(int n){
        // Will always refer to a Pile
        if (game.pile[n].up.size() == 0 && game.pile[n].down.size() > 0){
            moves.add("Flip <Pile["+n+"]>");
            System.out.println(moves.peek());
            game.pile[n].up.add(game.pile[n].down.pop());
        }
    }
}

//System.out.println("score "+game.getScore());
//System.out.println("reserve up and down "+game.reserve.up.size()+" and "+game.reserve.down.size());
//System.out.println("reserveUsed up and down "+game.reserveUsed.up.size()+" and "+game.reserveUsed.down.size());
