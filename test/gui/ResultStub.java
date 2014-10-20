package gui;

import java.util.List;

import logic.CommandType;
import logic.Result;

/*
 * This class is to be used for testing ONLY.
 * ResultStub allows for dependency injection of Result objects.
 * 
 */

public class ResultStub extends Result {

    @SuppressWarnings("rawtypes")
    private List outputs = null;
    private boolean success = false;
    private CommandType cmdType;
    private boolean confirmation = false;
    private ResultType resultType;

    public ResultStub() {
        super();
    }

    @SuppressWarnings("rawtypes")
    public ResultStub(List outputs, boolean success, CommandType cmdType,
            ResultType resultType) {
        setOutputs(outputs);
        setSuccess(success);
        setCmdType(cmdType);
        setResultType(resultType);
    }

    /*** MUTATORS ***/

    @SuppressWarnings("rawtypes")
    public List getOutputs() {
        return outputs;
    }

    public CommandType getCmdType() {
        return cmdType;
    }

    public ResultType getResultType() {
        return this.resultType;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    /*** ACCESSORS ***/

    @SuppressWarnings("rawtypes")
    public void setOutputs(List outputs) {
        this.outputs = outputs;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public void setCmdType(CommandType cmdType) {
        this.cmdType = cmdType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

}
