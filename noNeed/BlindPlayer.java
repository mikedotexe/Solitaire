/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solitaire;

import java.util.ArrayList;
import java.util.List;

public class BlindPlayer extends Player {

    /**
     * constructor
     */
    public BlindPlayer(String indeck) {
        super(indeck);
        history = new ArrayList<Game>();
    }

    /**
     * Blind play
     */
    @Override
    public void play(){
        //System.out.println(gameboard.toString());
        boolean playing = true;
        while(playing){
            playing = blindMove();
            while(playing)
            {
                playing = blindMove();
            }
            List<Card> select = gameboard.draw();
            if (select == null)
            {
                playing = false;
            }
            else
                playing = true;
        }
        System.out.print(gameboard.score());
    }

    /**
     * Selects a move from the legal_moves() and makes sure it isn't a loop
     * @return False when it can't make any more moves
     */
    public boolean blindMove() {
        //Grab the moves list
        List<Move> moves = legal_moves();
        Game gState = new Game(gameboard);

        //Apply a move to the gamestate until it the new state is not found
        //in the history
        for(int i=0 ;i<moves.size(); i++) {
            make_move(gState, moves.get(i));    //Apply the move
            if(!history.contains(gState))       //Check if it's in the history
            {
                this.gameboard = gState;        //It isn't in our history so
                                                //make the new gameboard our
                                                //current board.
                history.add(gState);            //Add the move to the history
                return true;
            }
        }
        return false;
    }
}
