package be.dechamps.web;

import be.dechamps.model.OthelloEngine;
import be.dechamps.model.Position;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OthelloRestcontroller {


    @RequestMapping("/")
    public Position home(Model model) {
        OthelloEngine engine = new OthelloEngine();
        model.addAttribute("engine", engine);
        return engine.getCurrentPosition();
    }
}
