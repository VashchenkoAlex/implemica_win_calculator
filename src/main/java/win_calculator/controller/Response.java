package win_calculator.controller;

public class Response {

    private String display;
    private String history;

    public Response(String display, String history) {

        this.display = display;
        this.history = history;
    }

    public String getDisplay() {

        if (display == null) {
            display = "0";
        }
        return display;
    }

    public String getHistory() {

        if (history == null) {
            history = "";
        }
        return history;
    }

}
