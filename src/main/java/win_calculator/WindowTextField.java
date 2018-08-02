package win_calculator;

import javafx.scene.control.TextField;

public class WindowTextField extends TextField {
    public WindowTextField(){

    }

    @Override
    public void replaceText(int start,int end,String text){

        if (text.matches("[ ]") || text.isEmpty()){
            super.replaceText(start,end,text);
        }
    }

    @Override
    public void replaceSelection(String replacement){
        super.replaceSelection(replacement);
    }
}
