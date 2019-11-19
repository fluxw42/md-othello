package be.dechamps.web;

import be.dechamps.model.OthelloEngine;
import be.dechamps.model.Position;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OthelloRestcontroller {


    @GetMapping("/home")
    public String home() {
        return "Welcome! This app server the game Othello as a REST service.<br>" +
                "Othello is played on a 8x8 board.<br>" +
                "Use the rest api to play this engine. '0' is an empty field." +
                " '1' is white and '2' is black.";
    }

    @GetMapping("/start")
    public Position start(Model model) {
        OthelloEngine engine = new OthelloEngine();
        model.addAttribute("engine", engine);
        return engine.getCurrentPosition();
    }

    @GetMapping("/move")
    public Position move(@RequestParam("x") int x, @RequestParam("y") int y,@ModelAttribute("engine") OthelloEngine engine) {
        engine.makeMove(x, y);
        engine.thinkAndMove();
        return engine.getCurrentPosition();
    }
}