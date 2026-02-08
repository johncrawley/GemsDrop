package com.jcrawleydev.gemsdrop.service.records;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.game.score.ScoreRecordsUtils;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class ScoreRecordsUtilsTest {

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
    public void orderedHighScoresCanBeObtained(){
        var highScores = Set.of("100", "40", "200", "5", "15");
        var expected = List.of("200", "100", "40", "15", "5");

        var sorted = ScoreRecordsUtils.getOrderedHighScores(highScores);

        assertEquals(expected.size(), sorted.size());

        for(int i = 0; i < expected.size(); i++){
            assertEquals(expected.get(i), sorted.get(i));
        }
    }
}
