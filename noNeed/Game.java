
package solitaire;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Card> deck;//This is the list of cards from which cards are drawn
    public List<Card> pile;//This is the list of cards onto which cards are drawn
    private int draws = 0;//The number of times the deck has been gone through
    public CardStack[] board = new CardStack[7];//The seven initial stacks of cards
    public CardStack[] foundations = new CardStack[4];//The four stacks of cards we want to build

    /**
     * Copy Constructor
     * @param g A game to copy
     */
    public Game(Game g) {
        //Copying deck, pile, draws
        this.deck = new ArrayList<Card>(g.deck);
        this.pile = new ArrayList<Card>(g.pile);
        this.draws = g.draws;

        //Copying the arrays, element by element
        for(int i=0; i<g.board.length; i++) {
            this.board[i] = g.board[i];
        }
        for(int i=0; i<g.foundations.length; i++) {
            this.foundations[i] = g.foundations[i];
        }
    }

    /**
     * Generates a game state
     * @param inDeck
     */
    public Game(String inDeck){
        //Initializing the foundation piles.  Note that the rank of the initial
        //card in each foundation is of Rank FOUNDATION.  As defined in the Card
        //class, ACE follows FOUNDATION, but cards of rank FOUNDATION cannot
        //be moved.
        for (int i = 0; i <4; ++i) {
            foundations[i] = new CardStack();
        }
        foundations[0].visible.add(new Card(Card.Rank.FOUNDATION, Card.Suit.SPADES));
        foundations[1].visible.add(new Card(Card.Rank.FOUNDATION, Card.Suit.HEARTS));
        foundations[2].visible.add(new Card(Card.Rank.FOUNDATION, Card.Suit.CLUBS));
        foundations[3].visible.add(new Card(Card.Rank.FOUNDATION, Card.Suit.DIAMONDS));
        for(int i = 0; i < 7; ++i) {
            board[i] = new CardStack();
        }
        //Initializing the pile
        pile = new ArrayList<Card>();
        //Preparing the input string to be parsed.
        //Rather than being comma delimited, this replace will make it whitespace
        //delimited, allowing easier tokenization.
        inDeck = inDeck.replaceAll(",", "\n");
        //Opening the string for parsing as a scanner.
        Scanner input = new Scanner(inDeck);
        //initializing the deck
        deck = new ArrayList<Card>();
        //Creating two maps to convert the input notation to their Enum forms.
        Map<Character, Card.Rank> convertRank = new HashMap<Character, Card.Rank>();
        Map<Character, Card.Suit> convertSuit = new HashMap<Character, Card.Suit>();
        //Initializing the map to convert from characters to Rank
        convertRank.put('A', Card.Rank.ACE);
        convertRank.put('2', Card.Rank.TWO);
        convertRank.put('3', Card.Rank.THREE);
        convertRank.put('4', Card.Rank.FOUR);
        convertRank.put('5', Card.Rank.FIVE);
        convertRank.put('6', Card.Rank.SIX);
        convertRank.put('7', Card.Rank.SEVEN);
        convertRank.put('8', Card.Rank.EIGHT);
        convertRank.put('9', Card.Rank.NINE);
        convertRank.put('T', Card.Rank.TEN);
        convertRank.put('J', Card.Rank.JACK);
        convertRank.put('Q', Card.Rank.QUEEN);
        convertRank.put('K', Card.Rank.KING);
        //Initializing the map to convert from character to Suit.
        convertSuit.put('d', Card.Suit.DIAMONDS);
        convertSuit.put('h', Card.Suit.HEARTS);
        convertSuit.put('c', Card.Suit.CLUBS);
        convertSuit.put('s', Card.Suit.SPADES);
        //While the input scanner has another card in it.
        while(input.hasNext()) {
            String temp;
            temp = input.next();//The card is read in
            temp = temp.trim();//Whitespace is removed.
            if (!temp.matches(""))
            {
                //The card is added to the deck.
                Card card = new Card(convertRank.get(temp.charAt(0)), convertSuit.get(temp.charAt(1)));
                System.out.println(card);
                deck.add(card);
            }
        }
        Collections.shuffle(deck);
        System.out.print(deck);
        deal();
   }
    /**
     * Deals the deck at the beginning of the game.
     */
    private void deal() {
        //Making sure that there are enough cards in the deck to deal.
        if (deck.size() > 27)
        {
            for (int i = 1; i <= 7; i++)
            {
                board[i - 1].visible.add(deck.get(0));
                deck.remove(0);
                for(int j = i; j < 7; j++)
                {
                    board[j].hidden.add(deck.get(0));
                    deck.remove(0);
                }
            }
        }
    }
    /**
     * This function draws three cards from the deck, and returns them as a list.  The last element
     * in the list is the one that can be played.
     * @return The three cards just drawn
     */
    public List<Card> draw() {
        if (deck.size() != 0)//If there remain cards to be drawn
        {
            for (int i = 0; (i < 3 && deck.size() > 0); i++)
            {
                pile.add(deck.get(0));
                deck.remove(0);
            }
        }
        else if (draws < 3 )//If the deck is empty and we have another draw, the
            //pile is turned to use as the deck.
        {
            deck.addAll(pile);//The pile is added to the deck
            pile.clear();//It is cleared
            ++draws;//Draws is incremented
            for (int i = 0; (i < 3 && deck.size() > 0); i++)
            {
                pile.add(deck.get(0));
                deck.remove(0);
            }
        }
        if (deck.size() == 0 && draws > 2)
        {
            return null;
        }
        if (pile.size() > 3)
            return pile.subList(pile.size()-4, pile.size()-1);//Returning the top three cards
        else
            return pile;
    }

    /**
     * Moves a card or several cards from one place to another
     * @param from The card at the base of what you are moving
     * @param to The card to which you are moving them
     * @return Whether the move could be done.
     */
    public boolean move(Card from, Card to) {
        List<Card> to_move = null;//This is the list of cards that will be moved.
        int moveback = -1;
        int where = -1;
        if (from.equals(to))
            return false;//Can't move a card to itself.
        else
        {
            for (int i = 0; i < 7; i++)//Here we look for the card in every
            {
                if (board[i].visible.contains(from))
                {
                    to_move = new ArrayList<Card>(board[i].visible.subList(board[i].visible.indexOf(from), board[i].visible.size()));
                    for (int j = board[i].visible.size()-1; j > board[i].visible.indexOf(from); j-- )
                        board[i].visible.remove(j);
                    if (board[i].visible.size() == 0 && board[i].hidden.size() != 0)
                    {
                        board[i].visible.add(board[i].hidden.get(board[i].hidden.size()-1));
                        board[i].hidden.remove(board[i].hidden.size()-1);
                    }
                    moveback = i;
                    where = 1;
                    break;
                }

            }
            if (pile.size() > 0 && pile.get(pile.size()-1).equals(from))
            {
                to_move = new ArrayList<Card>(pile.subList(pile.size()-1, pile.size()));
                pile.remove(pile.size()-1);
                where = 2;
                moveback = pile.size()-1;
            }
            else
            {
                for (int i = 0; i < 4; i++)
                {
                    if (foundations[i].visible.contains(from))
                    {
                        to_move = new ArrayList<Card>(foundations[i].visible);
                        foundations[i].visible.clear();
                        where = 3;
                        moveback = i;
                    }

                }
            }
        }
        if (to_move == null)
        {
            return false;
        }
        else
        {
            boolean found = false;
            for (int i= 0; i < 7; i++)
            {
                if (board[i].size() == 0 && to == null)
                {
                    board[i].visible.addAll(to_move);
                    found = true;
                    break;
                }
                else if (board[i].visible.get(board[i].visible.size()-1).equals(to))
                {
                    board[i].visible.addAll(to_move);
                    found = true;
                    break;
                }
            }
            if (found)
            {
                for (int i = 0; i < 7; i++)
                {
                    if (board[i].visible.size() == 0 && board[i].hidden.size() != 0)
                    {
                        board[i].visible.add(board[i].hidden.get(board[i].hidden.size()-1));
                        board[i].hidden.remove(board[i].hidden.size()-1);
                    }
                }
                for (int i= 0; i < 4; ++i)
                {
                    if (foundations[i].visible.size() == 0 && foundations[i].hidden.size() != 0)
                    {
                       foundations[i].visible.add(foundations[i].hidden.get(foundations[i].hidden.size() - 1));
                       foundations[i].hidden.remove(foundations[i].hidden.size()-1);
                    }
                }
            }
            else
            {
                switch (where)
                {
                    case 1:
                       board[moveback].visible.addAll(to_move);
                    case 2:
                       pile.add(from);
                    case 3:
                        foundations[moveback].visible.add(from);
                }
            }
            return found;
        }

    }
    public int score() {
        return (-55 + 5*(foundations[0].size() + foundations[1].size() + foundations[2].size() + foundations[3].size() - 4));
    }
    
    @Override
    public String toString() {
       StringBuilder rep = new StringBuilder();
       if (pile.size() > 0)
            rep.append("[~]" + "[" + pile.get(0) + "]");
       for (CardStack f: foundations)
       {
           if (f.size() > 1)
           {
               rep.append(f.visible.get(f.visible.size()-1));
           }
           else
               rep.append("\t");
       }
       rep.append("\n");
       for (int i = 0; i < 7; i++)
           rep.append(" " + this.board[i].visible.get(this.board[i].visible.size()-1));

       return rep.toString();
    }

    /**
     * Unsure if we need to add hashCode()
     */
    @Override
    public boolean equals(Object o) {
        Game g;
        if(o instanceof Game) {
            g = (Game)o;
        }
        else {
            System.out.println("Invalid type passed to Game.equals()");
            return false;
        }

        if((this.deck == g.deck) &&
           (this.pile == g.pile) &&
           (this.draws == g.draws) &&
           (this.board == g.board) &&
           (this.foundations == g.foundations)
           ) {
            return true;
        }
        return false;
    }
}
