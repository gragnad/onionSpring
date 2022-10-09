import org.junit.Before;
import org.junit.Test;
import legacy.study1.Calculator;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    Calculator calculator;
    @Before
    public void setup() {
        calculator = new Calculator();
    }
    @Test
    public void add() {
//        System.out.println(calculator.add(1,7));
        assertEquals(8 , calculator.add(1,7));
    }

}
