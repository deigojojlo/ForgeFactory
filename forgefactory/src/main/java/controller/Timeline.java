package main.java.controller;

import java.util.LinkedList;
import javax.swing.*;
import main.java.model.util.Objet;
import main.java.model.util.Task;

public class Timeline {
    private static final LinkedList<Task> tasklist = new LinkedList<>(); // la liste de tache
    private static final LinkedList<Task> toAdd = new LinkedList<>();
    private static final LinkedList<Task> craftQueue = new LinkedList<>();
    private static final LinkedList<Objet> resultCraftQueue = new LinkedList<>();
    private static final int interval = 125; // Intervalle en millisecondes 4 fois par seconde
    private static boolean isRunning = false;
    private static boolean isCrafting = false;
    private static final Timer timer = new Timer(interval, e -> run());

    /**
     * run is the main function. She is call every $interval ms and manage every
     * task
     */
    private static void run() {
        LinkedList<Task> toRemove = new LinkedList<>();
        for (Task runnable : tasklist) {
            if (runnable.getTime() == 0) {
                runnable.getTask().run();
                toRemove.add(runnable);
            } else {
                runnable.decreaseTime();
            }
        }
        // Evite les modifications en même temps qu'une lecture
        for (Task rm : toRemove) {
            tasklist.remove(rm);
        }
        // Evite les modifications en même temps qu'une lecture
        for (Task add : toAdd) {
            tasklist.add(add);
        }
        toAdd.clear();
    }

    public static void add(Task task) {
        // gestion en différer pour ne pas "casser" les noeuds de la liste iterative
        toAdd.add(task);
        // si le timer n'a pas été lancer
        if (!isRunning) {
            timer.start();
            isRunning = true;
        }
    }

    /**
     * addCraft est appele par un event de la partie craft du jeu
     * elle permet au joueur de crafter un objet en respectant une file d'attente
     * pour un jeu plus réaliste
     * 
     * Les machines gère elle meme leurs craft, elle n'interagissent jamais avec
     * cette file d'attente
     * 
     * @param result the result
     * @param timeInSecond the duration of the task 
     * @param runnable the task to run
     */
    public static void addCraft(Objet result, int timeInSecond, Runnable runnable) {
        // demarre le timer si rien n'a encore lancer le timer
        if (!isRunning) {
            timer.start();
            isRunning = true;
        }

        // on "reecrit" la tache avec des choses en plus pour pouvoir donné au joueur le
        // resultat de sa recette ainsi que gerer la file d'attente
        Task newTask = new Task(timeInSecond, () -> {
            runnable.run();
            resultCraftQueue.pollLast();
            System.out.println(resultCraftQueue);
            System.out.println("run");
            if (craftQueue.size() != 0) {
                isCrafting = true;
                toAdd.add(craftQueue.pollLast());
            } else {
                isCrafting = false;
            }
        });
        if (!isCrafting) {
            isCrafting = true;
            toAdd.add(newTask);
        } else {
            craftQueue.add(newTask);
        }
        resultCraftQueue.add(result);
    }

    public static int getIntervale() {
        return interval;
    }

    public static LinkedList<Objet> getResultCraftQueue() {
        return resultCraftQueue;
    }

}
