package solitaire;

import java.util.List;
import java.util.ArrayList;

public class Player {
    protected Game gameboard;
    protected List<Game> history;
    protected List<Move> mhistory;

    /**
     * constructor
     */
    public Player(String indeck){
        gameboard = new Game(indeck);
        mhistory = new ArrayList<Move>();
    }
    public void play(){
        List<Move> moves = legal_moves();
        boolean playing = true;
        while(playing) {
            while(!moves.isEmpty()) {
                make_move(moves.get(moves.size()-1));
                mhistory.add(moves.get(moves.size()-1));
                System.out.println(moves.get(moves.size()-1));
                moves = legal_moves();
                for (Move m: moves) {
                    if (mhistory.contains(m)) {
                        m = null;
                    }
                }
                while (moves.contains(null)) {
                    moves.remove(null);
                }
            }
            List<Card> select = gameboard.draw();
            if (select == null) {
                playing = false;
            }
        }
        System.out.println(gameboard.score());
       
    }

    /**
     * Puts into effect the move m on gameboard
     * @param m Make sure this move is valid before you use it
     */
    public void make_move(Move m){
        gameboard.move(m.from, m.to);
    }

    /**
     * Puts into effect the move m on a gameboard "game"
     * @param game A game to make the move on
     * @param m Make sure this move is valid before you use it
     */
    public void make_move(Game game, Move m){
        game.move(m.from, m.to);
    }
    
    /**
     * @return A list of legal move structs
     * @calls move_check()
     */
    protected List<Move> legal_moves(){
        List<Move>  legalMove = new ArrayList<Move>();
        List<Card> to = new ArrayList<Card>();
        List<Move.Loc> going = new ArrayList<Move.Loc>();

        for (int i = 0; i < 7; i++)
        {
            if (gameboard.board[i].size() > 0)
            {
                to.add(gameboard.board[i].visible.get(gameboard.board[i].visible.size()-1));
                going.add(Move.Loc.BOARD);
            }
            else
            {
                to.add(null);
                going.add(Move.Loc.BOARD);
            }
        }
        for (int i = 0; i < 4; i++)
        {
            to.add(gameboard.foundations[i].visible.get(gameboard.foundations[i].visible.size()-1));
            going.add(Move.Loc.FOUNDATION);
        }
        for (int i = 0; i < 7; ++i)
        {

            {
            for (int j = 0; j < gameboard.board[i].visible.size(); ++j)
            {
                for (int k = 0; k < to.size(); ++k)
                {
                    legalMove.add(new Move(gameboard.board[i].visible.get(j), to.get(k), Move.Loc.BOARD, going.get(k),gameboard.board[i].size()));

                }
            }
            }
        }
        for (int k = 0; k < to.size(); ++k)
           if (gameboard.pile != null && !gameboard.pile.isEmpty())
           {
                legalMove.add(new Move(gameboard.pile.get(gameboard.pile.size()-1), to.get(k), Move.Loc.PILE, going.get(k), gameboard.pile.size()));
           }
        for (int i = 0; i < 4; ++i)
        {
            for (int k = 0; k < to.size(); ++k)
                if (gameboard.foundations[i].size() > 0)
                {
                    if (gameboard.foundations[i].visible.get(gameboard.foundations[i].size()-1).rank() != Card.Rank.FOUNDATION)
                        legalMove.add(new Move(gameboard.foundations[i].visible.get(gameboard.foundations[i].size()-1), to.get(k), Move.Loc.FOUNDATION, going.get(k), gameboard.pile.size()));
                }
        }

        for (int i = 0; i < legalMove.size(); ++i)
        {
            if (!move_check(legalMove.get(i)))
            {
                legalMove.set(i, null);
            }
        }

        while(legalMove.contains(null))
        {
            legalMove.remove(null);
        }
        return legalMove;
    }

    /**
     * @return True if the inputted Move is valid
     * @param m A Move that needs validation
     */
    
    public boolean move_check(Move m){
        boolean success = false;
        if (m.going == Move.Loc.FOUNDATION && m.to.follows(m.from))
        {
            if ((m.from.suit() == m.to.suit()))
                return true;
        }
        else if (m.from.follows(m.to))
            return true;
        else if (m.from.rank() == Card.Rank.KING && m.to == null)
            return true;
        return false;
    }
     
}
