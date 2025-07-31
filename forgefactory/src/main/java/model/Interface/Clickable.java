package main.java.model.Interface;

import main.java.view.GUI;

/**
 * Clickable class provides a simple abstraction for storing clickable elements
 * in the game.
 */
public interface Clickable {

    /**
     * Performs an action when the element is clicked.
     *
     * @param frame The game map.
     */
    public void action(GUI frame);

}