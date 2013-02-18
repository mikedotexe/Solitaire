package solitaire;

import java.util.ArrayList;
import java.util.List;

public class Card {

    public enum Suit { HEARTS, CLUBS, DIAMONDS, SPADES }
    public enum Rank { KING (13), QUEEN (12), JACK (10), TEN (9), NINE (8), EIGHT (7),  SEVEN (6), 
        SIX (5), FIVE (4), FOUR (3), THREE (2), TWO (1), ACE (0), FOUNDATION (-1);

        private int here; // not sure why we'd need this, but it was in a java tutorial

        Rank(int p){
            this.here = p;
        }
    }
    private final Suit suit;
    private final Rank rank;
    
    // constructor
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    //accessors
    public Rank rank(){ 
        return rank; }
    public Suit suit(){ 
        return suit; }

    @Override
    public String toString() { 
        String whatisit = null;
        switch (rank)
        {
            case ACE:
                whatisit = "[A]";
                break;
            case TWO:
                whatisit = "[2]";
                break;
            case THREE:
                whatisit = "[3]";
                break;
            case FOUR:
                whatisit =  "[4]";
                break;
            case FIVE:
                whatisit =  "[5]";
                break;
            case SIX:
                whatisit =  "[6]";
                break;
            case SEVEN:
                whatisit = "[7]";
                break;
            case EIGHT:
                whatisit =  "[8]";
                break;
            case NINE:
                whatisit =  "[9]";
                break;
            case TEN:
                whatisit =  "[10]";
                break;
            case JACK:
                whatisit =  "[J]";
                break;
            case QUEEN:
                whatisit =  "[Q]";
                break;
            case KING:
                whatisit =  "[K]";
                break;
            case FOUNDATION:
                whatisit = "[F]";
                break;
        }
        return whatisit;
    }

    //!!!WHAT? i think this is kind of a 'random' deck
    
    private static final List<Card> protoDeck = new ArrayList<Card>();

    // Initialize prototype deck
    static {
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                protoDeck.add(new Card(rank, suit));
    }

    public static ArrayList<Card> newDeck() {
        return new ArrayList<Card>(protoDeck); // Return copy of prototype deck

    }
    public boolean follows(Card to){
        //!!!NEEDSWORK
        boolean follow = false;
        if (to.rank.here == this.rank.here+1)
        {
            if ((to.suit == Suit.CLUBS || to.suit == Suit.SPADES)&&(this.suit == Suit.DIAMONDS || this.suit== Suit.HEARTS))
                follow = true;
            else if ((this.suit == Suit.CLUBS || this.suit == Suit.SPADES)&&(to.suit == Suit.DIAMONDS || to.suit== Suit.HEARTS))
                 follow = true;
        }
        return follow;
    }


}
