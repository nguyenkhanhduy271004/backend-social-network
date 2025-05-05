package com.project.social_network.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class for handling HTTP requests and rendering the Tic-Tac-Toe
 * game page.
 *
 * @author Joabson Arley do Nascimento
 */
@Controller
@RequestMapping("/")
@Tag(name = "Game Controller")
public class TicTacToeController {

    /**
     * Renders the Tic-Tac-Toe game page with an empty board.
     *
     * @return the model and view for the Tic-Tac-Toe game page
     */
    @GetMapping
    public ModelAndView index() {
        return ticTacToe();
    }

    /**
     * Renders the Tic-Tac-Toe game page with an empty board.
     *
     * @return the model and view for the Tic-Tac-Toe game page
     */
    @RequestMapping("/index")
    public ModelAndView ticTacToe() {
        ModelAndView modelAndView = new ModelAndView("index");
        String[][] board = new String[3][3];
        Arrays.stream(board).forEach(row -> Arrays.fill(row, " "));
        modelAndView.addObject("board", board);
        return modelAndView;
    }
}