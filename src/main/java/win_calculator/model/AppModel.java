package win_calculator.model;

import win_calculator.DTOs.ResponseDTO;
import win_calculator.exceptions.MyException;
import win_calculator.model.nodes.actions.Action;
import win_calculator.model.nodes.actions.enter.Enter;
import win_calculator.model.nodes.actions.extra_operations.ExtraOperation;
import win_calculator.model.nodes.actions.extra_operations.Percent;
import win_calculator.model.nodes.actions.main_operations.MainOperation;
import win_calculator.model.nodes.actions.numbers.Coma;
import win_calculator.model.nodes.actions.numbers.Number;
import win_calculator.model.nodes.actions.numbers.ZeroNumber;
import win_calculator.model.response_handlers.HistoryHandler;
import win_calculator.model.button_handlers.ExtraOperationHandler;
import win_calculator.model.button_handlers.MainOperationHandler;
import win_calculator.model.button_handlers.PercentHandler;

public class AppModel {

    private HistoryHandler historyHandler = new HistoryHandler();
    private MainOperationHandler mOperationHandler = new MainOperationHandler(historyHandler);
    private ExtraOperationHandler eOperationHandler = new ExtraOperationHandler();
    private PercentHandler percentHandler = new PercentHandler(this);

    public HistoryHandler getHistoryHandler() {
        return historyHandler;
    }

    public ResponseDTO toDo(Action action) throws MyException {

        switch (action.getType()){
            case NUMBER:{
                if (".".equals(action.getValue())){
                    doComa();
                }else {
                    doNumber((Number) action);
                }
                break;
            }
            case MAIN_OPERATION:{
                mOperationHandler.doOperation((MainOperation) action);
                break;
            }
            case ENTER:{
                mOperationHandler.doEnter();
                break;
            }
            case EXTRA_OPERATION:{
                eOperationHandler.doOperation(historyHandler.getLastAssembledNumber(),(ExtraOperation) action);
                historyHandler.addActionToHistory(action);
                break;
            }
            case PERCENT:{
                percentHandler.doOperation((Percent) action);
                historyHandler.addActionToHistory(action);
                break;
            }
            case CLEAR:{
                historyHandler.clearHistory();
                mOperationHandler.resetValues();
                break;
            }
        }
        return new ResponseDTO(historyHandler.getResultNumberForResponse(),historyHandler.getHistoryString());
    }

    private void doNumber(Number number){

        if (historyHandler.isZeroNotFirst() || historyHandler.isComaLast()){
            historyHandler.addActionToHistory(number);
        }else {
            historyHandler.changeLastAction(number);
        }
    }

    private void doComa(){

        if (historyHandler.isComaFirst()){
            historyHandler.addActionToHistory(new ZeroNumber());
            historyHandler.addActionToHistory(new Coma());
        }else {
            if (historyHandler.isLastNumberWhole()){
                historyHandler.addActionToHistory(new Coma());
            }
        }
    } //DONE
}
