package gui;

import java.util.ArrayList;
import java.util.List;

import logic.CommandType;
import logic.Result;
import logic.Result.ResultType;

import org.junit.Test;

public class ResultGeneratorTest {

    @Test
    public void test_Add_All_Para() {
        CommandType commandDone = CommandType.ADD;
        List<TaskStub> outputs = new ArrayList<TaskStub>();
        DateTimeStub due = new DateTimeStub();
        DateTimeStub start = new DateTimeStub();
        DateTimeStub end = new DateTimeStub();
        outputs.add(new TaskStub("finish testing", null, null, null, null));
        Result result = new Result(outputs, true, commandDone, ResultType.TASK);

        ResultGenerator.getResultMessage(userInput);
    }

}
