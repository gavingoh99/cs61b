package creatures;

import huglife.HugLifeUtils;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;

import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;

    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }
    public Clorus() {
        this(1);
    }
    public Color color() {
        return color(r, g, b);
    }
    public void attack(Creature c) {
        energy += c.energy();
    }
    public void move() {
        energy -= 0.03;
    }
    public void stay() {
        energy -= 0.01;
    }
    public Clorus replicate() {
        energy = energy / 2;
        return new Clorus(energy);
    }
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            Direction attDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, attDir);
        } else if (energy > 1) {
            Direction repDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, repDir);
        } else {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, moveDir);
        }
    }
}
