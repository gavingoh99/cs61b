package creatures;
import huglife.Direction;
import huglife.Occupant;
import huglife.Action;
import huglife.Impassible;
import huglife.Empty;
import org.junit.Test;

import java.awt.Color;
import java.util.HashMap;

import static org.junit.Assert.*;
public class TestClorus {
    @Test
    public void testBasics() {
        Clorus test = new Clorus(2);
        assertEquals(2, test.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), test.color());
        test.move();
        assertEquals(1.97, test.energy(), 0.01);
        test.stay();
        assertEquals(1.96, test.energy(), 0.01);
        Plip food = new Plip();
        test.attack(food);
        assertEquals(2.96, test.energy(), 0.01);
    }
    @Test
    public void testReplicate() {
        Clorus c = new Clorus(1.2);
        Clorus child = c.replicate();
        assertNotSame(child, c);
        assertEquals(0.6, c.energy(), 0.01);
        assertEquals(0.6, child.energy(), 0.01);
    }
    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        Clorus other = new Clorus(1.2);
        HashMap<Direction, Occupant> surroundings = new HashMap<>();
        surroundings.put(Direction.TOP, new Empty());
        surroundings.put(Direction.BOTTOM, new Empty());
        surroundings.put(Direction.LEFT, new Plip());
        surroundings.put(Direction.RIGHT, new Empty());

        Action actual1 = other.chooseAction(surroundings);
        Action expected1 = new Action(Action.ActionType.ATTACK, Direction.LEFT);
        assertEquals(expected1, actual1);

        Clorus test = new Clorus(1.2);
        HashMap<Direction, Occupant> surround = new HashMap<>();
        surround.put(Direction.TOP, new Empty());
        surround.put(Direction.BOTTOM, new Impassible());
        surround.put(Direction.LEFT, new Impassible());
        surround.put(Direction.RIGHT, new Impassible());

        Action actual2 = test.chooseAction(surround);
        Action expected2 = new Action(Action.ActionType.REPLICATE, Direction.TOP);
        assertEquals(expected2, actual2);

        Clorus lazy = new Clorus(0.9);
        HashMap<Direction, Occupant> around = new HashMap<>();
        around.put(Direction.TOP, new Empty());
        around.put(Direction.BOTTOM, new Impassible());
        around.put(Direction.LEFT, new Impassible());
        around.put(Direction.RIGHT, new Impassible());

        Action actual3 = lazy.chooseAction(surround);
        Action expected3 = new Action(Action.ActionType.MOVE, Direction.TOP);
        assertEquals(expected3, actual3);
    }
}
