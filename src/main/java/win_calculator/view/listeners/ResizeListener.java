package win_calculator.view.listeners;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Listener of mouse events on FXApp
 * Changes window size and mouse cursor depends on mouse positions and mouse events
 * <p>
 * was taken from https://stackoverflow.com/a/24017605 and was modified
 */
public class ResizeListener {

   public static void addResizeListener(Stage stage) {
      Resizer resizer = new Resizer(stage);
      Scene scene = stage.getScene();
      scene.addEventHandler(MouseEvent.MOUSE_MOVED, resizer);
      scene.addEventHandler(MouseEvent.MOUSE_PRESSED, resizer);
      scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizer);
      scene.addEventHandler(MouseEvent.MOUSE_EXITED, resizer);
      scene.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizer);

   }

   private static class Resizer implements EventHandler<MouseEvent> {
      private Stage stage;
      private Scene scene;
      private Cursor cursorEvent = Cursor.DEFAULT;
      private static final int border = 4;
      private double startX = 0;
      private double startY = 0;
      private double mouseX;
      private double mouseY;

      Resizer(Stage stage) {
         this.stage = stage;
         scene = stage.getScene();
      }

      @Override
      public void handle(MouseEvent mouseEvent) {
         EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
         mouseX = mouseEvent.getSceneX();
         mouseY = mouseEvent.getSceneY();
         if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
            setCursorForScene(mouseEvent);
         } else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType)) {
            scene.setCursor(Cursor.DEFAULT);
         } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
            startX = stage.getWidth() - mouseX;
            startY = stage.getHeight() - mouseY;
         } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
            resize(mouseEvent);
            fixSize();
         }
      }

      private void resize(MouseEvent mouseEvent) {

         if (!Cursor.DEFAULT.equals(cursorEvent)) {
            if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
               double minHeight = stage.getMinHeight();
               if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) {
                  if (stage.getHeight() > minHeight || mouseY < 0) {
                     stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                     stage.setY(mouseEvent.getScreenY());
                  }
               } else {
                  if (stage.getHeight() > minHeight || mouseY + startY - stage.getHeight() > 0) {
                     stage.setHeight(mouseY + startY);
                  }
               }
            }
            if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
               double minWidth = stage.getMinWidth();
               if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {
                  if (stage.getWidth() > minWidth || mouseX < 0) {
                     stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                     stage.setX(mouseEvent.getScreenX());
                  }
               } else {
                  if (stage.getWidth() > minWidth || mouseX + startX - stage.getWidth() > 0) {
                     stage.setWidth(mouseX + startX);
                  }
               }
            }
         }
      }

      private void setCursorForScene(MouseEvent event) {

         double eventX = event.getSceneX();
         double eventY = event.getSceneY();
         double width = scene.getWidth();
         double height = scene.getHeight();

         if (eventX < border && eventY < border) {
            cursorEvent = Cursor.NW_RESIZE;
         } else if (eventX < border && eventY > height - border) {
            cursorEvent = Cursor.SW_RESIZE;
         } else if (eventX > width - border && eventY < border) {
            cursorEvent = Cursor.NE_RESIZE;
         } else if (eventX > width - border && eventY > height - border) {
            cursorEvent = Cursor.SE_RESIZE;
         } else if (eventX < border) {
            cursorEvent = Cursor.W_RESIZE;
         } else if (eventX > width - border) {
            cursorEvent = Cursor.E_RESIZE;
         } else if (eventY < border) {
            cursorEvent = Cursor.N_RESIZE;
         } else if (eventY > height - border) {
            cursorEvent = Cursor.S_RESIZE;
         } else {
            cursorEvent = Cursor.DEFAULT;
         }
         scene.setCursor(cursorEvent);
      }

      private void fixSize() {

         if (stage.getWidth() < stage.getMinWidth()) {
            stage.setWidth(stage.getMinWidth());
         }
         if (stage.getHeight() < stage.getMinHeight()) {
            stage.setHeight(stage.getMinHeight());
         }
      }
   }
}
