package com.jcrawleydev.gemsdrop.game.grid;

import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.NullGem;

import java.util.List;

public class SectionEvaluator {

    private final int MINIMUM_MATCH_NUMBER;

    public SectionEvaluator(int minMatchNumber){
        this.MINIMUM_MATCH_NUMBER = minMatchNumber;
    }


    public void evaluateGemsIn(List<Gem> gems){
        int currentIndex = 0;
        while( currentIndex < gems.size() - (MINIMUM_MATCH_NUMBER - 1)){
            currentIndex = evaluateSection(gems, currentIndex);
        }
    }


    private int evaluateSection(List<Gem> section, int currentIndex){
        var currentGem = section.get(currentIndex);
        int nextIndex = currentIndex + 1;
        if(currentGem instanceof NullGem){
            return nextIndex;
        }

        mark(currentGem);
        int matchingGemsCount = 1;

        for(int comparisonIndex = nextIndex; comparisonIndex < section.size(); comparisonIndex++){
            var comparisonGem = section.get(comparisonIndex);
            if(comparisonGem.isNotTheSameColorAs(currentGem)){
                if(matchingGemsCount >= MINIMUM_MATCH_NUMBER){
                    markForDeletionInRange(currentIndex, comparisonIndex, section);
                    return comparisonIndex;
                }
                unmarkAllInRange(currentIndex, comparisonIndex, section);
                return nextIndex;
            }
            matchingGemsCount++;
            mark(comparisonGem);
        }

        markForDeletionInRange(currentIndex,section.size()-1, section);
        return section.size();
    }


    private void mark(Gem gem){
        gem.setDeleteCandidateFlag();
    }


    private void unmarkAllInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i=startIndex; i<= endIndex; i++){
            gems.get(i).resetDeleteCandidateFlag();
        }
    }


    private void markForDeletionInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i = startIndex; i <= endIndex; i++){
            gems.get(i).setMarkedForDeletion();
        }
    }

}
