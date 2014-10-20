package gui;

import static org.junit.Assert.*;
import gui.ResultGenerator;

import org.junit.Test;

public class ResultGeneratorTest {

    @Test
    public void test_Add_All_Para() {
        String userInput = "add buy fish for party due: 15:25 19/10/2014 start: 13:00 19/10/2014 end: 1500 19/10/2014 #fishy";
        String actual = ResultGenerator.sendInput(userInput);
        String expected = "Added buy fish for party!";
        
        assertEquals(expected, actual);
    }

}
