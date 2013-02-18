/*
 * This contains information and a couple of helper functions for the given cards
 */
package solitaire;

/**
 *
 * @author Mike
 */
public class SCard {
    public enum Suit { HEARTS, CLUBS, DIAMONDS, SPADES }
    public enum Rank { ZERO, ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }
    
    private final Suit suit;
    private final Rank rank;
    
    // constructor
    public SCard(Rank ranks, Suit suits) {
        this.rank = ranks;
        this.suit = suits;
    }
    
    // this constructor is for parsing the CSV values, "T" and "s" will make TEN of SPADES
    public SCard(String rank, String suit) {
        Rank tmpRank = null; //enum
        Suit tmpSuit = null; //enum
        
        // Long, but I dont think there's a quicker way. Can't switch Strings        
        // Determine rank, or face value, whatever you call it
        if ("K".equals(rank)){
                tmpRank = Rank.KING; }
        else if ("Q".equals(rank)){
                tmpRank = Rank.QUEEN; }
        else if ("J".equals(rank)){
                tmpRank = Rank.JACK;}
        else if ("T".equals(rank)){
                tmpRank = Rank.TEN;}
        else if ("9".equals(rank)){
                tmpRank = Rank.NINE; }
        else if ("8".equals(rank)){
                tmpRank = Rank.EIGHT; }
        else if ("7".equals(rank)){
                tmpRank = Rank.SEVEN; }
        else if ("6".equals(rank)){
                tmpRank = Rank.SIX; }
        else if ("5".equals(rank)){
                tmpRank = Rank.FIVE; }
        else if ("4".equals(rank)){
                tmpRank = Rank.FOUR; }
        else if ("3".equals(rank)){
                tmpRank = Rank.THREE; }
        else if ("2".equals(rank)){
                tmpRank = Rank.TWO; }
        else if ("A".equals(rank)){
                tmpRank = Rank.ACE; }
        else if ("0".equals(rank)){
                tmpRank = Rank.ZERO; }
        
        // Determine Suit
        if ("h".equals(suit)){
            tmpSuit = Suit.HEARTS; }
        else if ("c".equals(suit)){
            tmpSuit = Suit.CLUBS; }
        else if ("s".equals(suit)){
            tmpSuit = Suit.SPADES; }
        else if ("d".equals(suit)){
            tmpSuit = Suit.DIAMONDS; }

        // Set it, now we're done parsing, our class knows what Enum it is
        this.rank = tmpRank;
        this.suit = tmpSuit;
    }

    //accessors
    public Rank rank() {
        return rank; }
    public Suit suit() { 
        return suit; }    
    
    // these will make it easier to know whether we have a legal move
    public boolean isRed(){
        if (suit == Suit.HEARTS || suit == Suit.DIAMONDS) 
            return true;
        else
            return false;
    } 
    public boolean isBlack(){
        if (suit == Suit.CLUBS || suit == Suit.SPADES) 
            return true;
        else
            return false;
    }
    
    @Override
    public String toString() { 
        // this is a really handy way of not having to do a bunch of switches
        // and a good reason to use descriptive Enums
        return this.rank.toString() + " of " + this.suit.toString();
    }    
    
}
