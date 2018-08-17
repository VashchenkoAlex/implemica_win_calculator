package win_calculator.controller.view_handlers;

import javafx.scene.layout.Pane;

public class StylesHandler {

    private static final String FULL_SCREEN_CSS = "/styles/full_screen_style.css";
    private static final String MAIN_CSS = "/styles/styles.css";

    public void changeStyle(Pane pane,String style){

        String object = getClass().getResource(style).toExternalForm();
        pane.getStylesheets().add(object);
    }
}
