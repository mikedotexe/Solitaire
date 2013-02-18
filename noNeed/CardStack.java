/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package solitaire;

import java.util.ArrayList;
import java.util.List;

public class CardStack {
    public List<Card> visible;
    public List<Card> hidden;
    public CardStack()
    {
        hidden = new ArrayList<Card>();
        visible = new ArrayList<Card>();
    }
    public CardStack(List<Card> new_stack)
    {
        if (new_stack.size() == 0)
        {
            hidden = new ArrayList<Card>();
            visible = new ArrayList<Card>();
        }
        else if (new_stack.size() == 1)
        {
            hidden = new ArrayList<Card>();
            visible = new ArrayList<Card>();
            visible.add(new_stack.get(0));
        }
        else
        {
            hidden = new ArrayList<Card>(new_stack.subList(0, new_stack.size() - 2));
            visible = new ArrayList<Card>();
            visible.add(new_stack.get(new_stack.size()-1));
        }
    }
    public int numHidden()
    {
        return hidden.size();
    }
    public int numVis()
    {
        return visible.size();
    }
    public int size()
    {
        return visible.size() + hidden.size();
    }



}
