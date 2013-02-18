/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solitaire;

public class Move {
    public enum Loc{PILE, BOARD, FOUNDATION};
    public Card from, to;
    public Loc where;//Where the from card is from
    public Loc going;//Where the to card is from
    public int size;
    public Move(Card new_from, Card new_to) {
        from = new_from;
        to = new_to;
    }
    public Move(Card new_from, Card new_to, Loc new_w,Loc new_g, int nsize) {
        from = new_from;
        to = new_to;
        size = nsize;
        where = new_w;
        going = new_g;
    }
    @Override
    public String toString()
    {
        return new String("Moving from: " + from + " to " + to);
    }
}
