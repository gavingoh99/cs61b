package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class   
 *  @authr FIXME
 */

public class TestPlip {

    /* Replace with the magic word given in lab.
     * If you are submitting early, just put in "early" */
    public static final String MAGIC_WORD = "";

    @Test
    public void testBasics() {
        Plip p = new Plip(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(99, 255, 76), p.color());
        p.move();
        assertEquals(1.85, p.energy(), 0.01);
        p.move();
        assertEquals(1.70, p.energy(), 0.01);
        p.stay();
        assertEquals(1.90, p.energy(), 0.01);
        p.stay();
        assertEquals(2.00, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Plip p = new Plip(1.2);
        Plip child = p.replicate();
        assertNotSame(child, p);
        assertEquals(0.6, p.energy(), 0.01);
        assertEquals(0.6, child.energy(), 0.01);
    }

    @Test
    public void testChoose() {
        Plip p = new Plip(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!  

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        Plip other = new Plip(1.2);
        HashMap<Direction, Occupant> surroundings = new HashMap<>();
        surroundings.put(Direction.TOP, new Impassible());
        surroundings.put(Direction.BOTTOM, new Impassible());
        surroundings.put(Direction.LEFT, new Impassible());
        surroundings.put(Direction.RIGHT, new Empty());

        Action actual1 = other.chooseAction(surroundings);
        Action expected1 = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);

        assertEquals(expected1, actual1);

        Plip run = new Plip(0.9);
        HashMap<Direction, Occupant> around = new HashMap<>();
        around.put(Direction.TOP, new Clorus());
        around.put(Direction.BOTTOM, new Impassible());
        around.put(Direction.LEFT, new Impassible());
        around.put(Direction.RIGHT, new Empty());

        Action actual2 = run.chooseAction(around);
        Action expected2 = new Action(Action.ActionType.MOVE, Direction.RIGHT);

        assertEquals(expected2, actual2);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }
} 
