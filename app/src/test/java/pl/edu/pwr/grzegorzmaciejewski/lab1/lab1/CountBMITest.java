package pl.edu.pwr.grzegorzmaciejewski.lab1.lab1;

import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by stud on 15.03.2017.
 */

public class CountBMITest {
    @Test
    public void massUnderZeroIsInvalid()
    {
        //GIVEN
        float testMass=-1.00f;
        //WHEN
        ICountBMI countBmi=new CountBMIForKgM();
        //THEN
        boolean actual=countBmi.isMassValid(testMass);
        assertFalse(actual);

    }

    @Test
    public void bmiIsValid()
    {
        //GIVEN
        float testMass=50;
        float testHeigh=1.70f;
        //WHEN
        ICountBMI countBmi=new CountBMIForKgM();
        //THEN
        float actual=countBmi.countBMI(testMass,testHeigh);
        assertEquals(17.3f,actual,0.1f);

    }
}
