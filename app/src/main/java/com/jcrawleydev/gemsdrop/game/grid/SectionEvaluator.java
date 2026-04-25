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


    public void evaluate(List<GridEvaluator.GemCoordinates> section, List<List<Gem>> columns){
        int currentIndex = 0;
        while( currentIndex < section.size() - (MINIMUM_MATCH_NUMBER - 1)){
            currentIndex = evaluateSection(section, columns, currentIndex);
        }
    }


    private int evaluateSection(List<GridEvaluator.GemCoordinates> section, List<List<Gem>> columns, int currentIndex){
        var currentGemCoordinates = section.get(currentIndex);
        int nextIndex = currentIndex + 1;
        if(currentGemCoordinates.column() < 0){
            return nextIndex;
        }
        var currentGem = columns.get(currentGemCoordinates.column()).get(currentGemCoordinates.row());
        markGem(currentGem);
        int matchingGemsCount = 1;

        for(int comparisonIndex = nextIndex; comparisonIndex < section.size(); comparisonIndex++){
            int colIndex = section.get(comparisonIndex).column();
            int rowIndex = section.get(comparisonIndex).row();
            Gem comparisonGem = columns.get(colIndex).get(rowIndex);
            if(comparisonGem.isNotSameColorAs(currentGem)){
                if(matchingGemsCount >= MINIMUM_MATCH_NUMBER){
                    markAllCandidatesForDeletionInRange(section, currentIndex, comparisonIndex, columns);
                    return comparisonIndex;
                }
                unmarkAllGemsInRange(section, currentIndex, comparisonIndex, columns);
                return nextIndex;
            }
            matchingGemsCount++;
            markGem(comparisonGem);
        }

        markAllCandidatesForDeletionInRange(section, currentIndex,section.size()-1, columns);
        return section.size();
    }


    private void unmarkAllGemsInRange(List<GridEvaluator.GemCoordinates> section, int startIndex, int endIndex, List<List<Gem>> columns){
        for(int i = startIndex; i <= endIndex; i++){
            var coordinates =  section.get(i);
            if(coordinates.column() >= 0){
                columns.get(coordinates.column()).get(coordinates.row()).resetDeleteCandidateFlag();
            }
        }
    }


    private void markAllCandidatesForDeletionInRange(List<GridEvaluator.GemCoordinates> section, int startIndex, int endIndex, List<List<Gem>> columns){
        for(int i = startIndex; i <= endIndex; i++){
            var coordinates =  section.get(i);
            if(coordinates.column() >= 0){
                columns.get(coordinates.column()).get(coordinates.row()).setMarkedForDeletion();
            }
        }
    }


    private int evaluateSection(List<Gem> section, int currentIndex){
        var currentGem = section.get(currentIndex);
        int nextIndex = currentIndex + 1;
        if(currentGem instanceof NullGem){
            return nextIndex;
        }

        markGem(currentGem);
        int matchingGemsCount = 1;

        for(int comparisonIndex = nextIndex; comparisonIndex < section.size(); comparisonIndex++){
            Gem comparisonGem = section.get(comparisonIndex);
            if(comparisonGem.isNotSameColorAs(currentGem)){
                if(matchingGemsCount >= MINIMUM_MATCH_NUMBER){
                    markAllCandidatesForDeletionInRange(currentIndex, comparisonIndex, section);
                    return comparisonIndex;
                }
                unmarkAllGemsInRange(currentIndex, comparisonIndex, section);
                return nextIndex;
            }
            matchingGemsCount++;
            markGem(comparisonGem);
        }

        markAllCandidatesForDeletionInRange(currentIndex,section.size()-1, section);
        return section.size();
    }


    private void markGem(Gem gem){
        gem.setDeleteCandidateFlag();
    }


    private void unmarkAllGemsInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i=startIndex; i<= endIndex; i++){
            gems.get(i).resetDeleteCandidateFlag();
        }
    }


    private void markAllCandidatesForDeletionInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i = startIndex; i <= endIndex; i++){
            gems.get(i).setMarkedForDeletion();
        }
    }

}
