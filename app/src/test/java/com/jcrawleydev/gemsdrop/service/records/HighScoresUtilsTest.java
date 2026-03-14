package com.jcrawleydev.gemsdrop.service.records;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.game.score.ScoreRecordsUtils;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class HighScoresUtilsTest {

    @Test
    public void highScoreCanBeMerged(){
        var existingHighScores = Set.of("100", "50", "40", "30", "10", "55");
        var unchangedHighScores = ScoreRecordsUtils.mergeHighScores(5, existingHighScores);

        assertTrue(unchangedHighScores.containsAll(existingHighScores));
        assertEquals(unchangedHighScores.size(), existingHighScores.size());

        var score = 90;
        var amendedHighScores = ScoreRecordsUtils.mergeHighScores(score, existingHighScores);
        assertFalse(amendedHighScores.containsAll(existingHighScores));
        assertEquals(amendedHighScores.size(), existingHighScores.size());
        assertTrue(amendedHighScores.contains(String.valueOf(score)));
    }


    @Test
    public void canCreateStringFromScores(){

        var highScores = List.of("100", "100", "40", "30");
        var output = ScoreRecordsUtils.createPropStrFrom(highScores);
        var expected = "100,100,40,30";
        assertEquals(expected, output);
    }

}
