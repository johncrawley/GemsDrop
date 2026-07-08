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
        var currentGem = markAndGetGemFrom(columns, currentGemCoordinates);
        int matchingGemsCount = 1;

        for(int comparisonIndex = nextIndex; comparisonIndex < section.size(); comparisonIndex++){
            var comparisonGem = getComparisonGemFrom(section, columns, comparisonIndex);
            if(comparisonGem.isNotTheSameColorAs(currentGem)){
                if(matchingGemsCount >= MINIMUM_MATCH_NUMBER){
                    markForDeletionInRange(section, currentIndex, comparisonIndex, columns);
                    return comparisonIndex;
                }
                unmarkAllGemsInRange(section, currentIndex, comparisonIndex, columns);
                return nextIndex;
            }
            matchingGemsCount++;
            markGem(comparisonGem);
        }
        markForDeletionInRange(section, currentIndex,section.size()-1, columns);
        return section.size();
    }


    private Gem markAndGetGemFrom(List<List<Gem>> columns, GridEvaluator.GemCoordinates coordinates){
        var gem =  columns.get(coordinates.column()).get(coordinates.row());
        markGem(gem);
        return gem;
    }


    private Gem getComparisonGemFrom(List<GridEvaluator.GemCoordinates> section, List<List<Gem>> columns, int index){
        int colIndex = section.get(index).column();
        int rowIndex = section.get(index).row();
        return columns.get(colIndex).get(rowIndex);
    }


    private void unmarkAllGemsInRange(List<GridEvaluator.GemCoordinates> section, int startIndex, int endIndex, List<List<Gem>> columns){
        for(int i = startIndex; i <= endIndex; i++){
            var coordinates =  section.get(i);
            if(coordinates.column() >= 0){
                columns.get(coordinates.column()).get(coordinates.row()).resetDeleteCandidateFlag();
            }
        }
    }


    private void markForDeletionInRange(List<GridEvaluator.GemCoordinates> section, int startIndex, int endIndex, List<List<Gem>> columns){
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
            if(comparisonGem.isNotTheSameColorAs(currentGem)){
                if(matchingGemsCount >= MINIMUM_MATCH_NUMBER){
                    markForDeletionInRange(currentIndex, comparisonIndex, section);
                    return comparisonIndex;
                }
                unmarkAllGemsInRange(currentIndex, comparisonIndex, section);
                return nextIndex;
            }
            matchingGemsCount++;
            markGem(comparisonGem);
        }

        markForDeletionInRange(currentIndex,section.size()-1, section);
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


    private void markForDeletionInRange(int startIndex, int endIndex, List<Gem> gems){
        for(int i = startIndex; i <= endIndex; i++){
            gems.get(i).setMarkedForDeletion();
        }
    }

}
