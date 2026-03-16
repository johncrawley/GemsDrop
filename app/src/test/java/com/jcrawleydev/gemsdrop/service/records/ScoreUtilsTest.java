package com.jcrawleydev.gemsdrop.service.records;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.game.score.ScoreUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScoreUtilsTest {

    @Test
    public void highScoreCanBeMerged(){
        var existingHighScores = Set.of("100", "50", "40", "30", "10", "55");
        var unchangedHighScores = ScoreUtils.mergeHighScores(5, existingHighScores);

        assertTrue(unchangedHighScores.containsAll(existingHighScores));
        assertEquals(unchangedHighScores.size(), existingHighScores.size());

        var score = 90;
        var amendedHighScores = ScoreUtils.mergeHighScores(score, existingHighScores);
        assertFalse(amendedHighScores.containsAll(existingHighScores));
        assertEquals(amendedHighScores.size(), existingHighScores.size());
        assertTrue(amendedHighScores.contains(String.valueOf(score)));
    }


    @Test
    public void scoreIsAddedToHighScores(){
        var existingHighScores = List.of("100", "55", "50", "40", "30", "10");
        var unchangedHighScores = ScoreUtils.addToHighScores(5, existingHighScores);
        for(var score: unchangedHighScores){
            log("unchangedScore: " + score);
        }
        assertTrue(unchangedHighScores.containsAll(existingHighScores));
        assertEquals(unchangedHighScores.size(), existingHighScores.size());

        var score = 90;
        var amendedHighScores = ScoreUtils.addToHighScores(score, existingHighScores);
        assertFalse(amendedHighScores.containsAll(existingHighScores));
        assertEquals(amendedHighScores.size(), existingHighScores.size());
        assertTrue(amendedHighScores.contains(String.valueOf(score)));
        var expectedList = List.of("100", "90", "55", "50", "40", "30");
        for(int i = 0; i < expectedList.size(); i++){
            assertEquals(expectedList.get(i), amendedHighScores.get(i));
        }
    }

    private void log(String msg){
        System.out.println("ScoreUtilsTest: " + msg);
    }

    @Test
    public void canCreateStringFromScores(){

        var highScores = List.of("100", "100", "40", "30");
        var output = ScoreUtils.createPropStrFrom(highScores);
        var expected = "100,100,40,30";
        assertEquals(expected, output);

        List<String> amendedScores = new ArrayList<>(highScores);
        amendedScores.add("200");
        var output2 = ScoreUtils.createPropStrFrom(amendedScores);
        assertEquals("200,100,100,40,30", output2);
    }

}
