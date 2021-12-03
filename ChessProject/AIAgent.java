import java.util.*;

public class AIAgent {

    static int PieceValue = 0;

    public int Value(){

        Square FindBlack = null;
        String PieceName;

        PieceName = FindBlack.getPieceName();

        if(PieceName.contains("Knight")){
            PieceValue = 3;
        }

        else if(PieceName.contains("Rook")){
            PieceValue = 5;
        }

        else if(PieceName.contains("King")){
            PieceValue = 10;
        }

        else if(PieceName.contains("Queen")){
            PieceValue = 9;
        }

        else if(PieceName.contains("Bishup")){
            PieceValue = 3;
        }

        else if(PieceName.contains("Pawn")){
            PieceValue = 1;
        }
        return PieceValue;
    }

    Random random;

    public AIAgent() {

        random = new Random();

    }

    public Move randomMove(Stack possibilities) {

        int moveID = random.nextInt(possibilities.size());

        System.out.println("Agent randomly selected move : " + moveID);
        for (int i = 1; i < (possibilities.size() - (moveID)); i++) {
            possibilities.pop();
        }

        Move selectedMove = (Move) possibilities.pop();
        return selectedMove;

    }

    public Move nextBestMove(Stack WhiteMovementPossibilities, Stack BlackMovementPossibilities) {

        Stack FinalMove = (Stack) WhiteMovementPossibilities.clone();
        Stack BlackMove = (Stack) BlackMovementPossibilities.clone();

        Move BestMove = null;
        Move CurrentMove;
        Move WhiteMove;

        Square BlackPosition = null;


        int CompareValue;
        int CurrentValue = 0;

        while (!WhiteMovementPossibilities.empty()) {

            WhiteMove = (Move) WhiteMovementPossibilities.pop();
            CurrentMove = WhiteMove;

            if ((CurrentMove.getStart().getYcoordinate() < CurrentMove.getLanding().getYcoordinate())
                    && (CurrentMove.getLanding().getXcoordinate() == 3) && (CurrentMove.getLanding().getYcoordinate() == 3)
                    || (CurrentMove.getLanding().getXcoordinate() == 4) && (CurrentMove.getLanding().getYcoordinate() == 3)
                    || (CurrentMove.getLanding().getXcoordinate() == 3) && (CurrentMove.getLanding().getYcoordinate() == 4)
                    || (CurrentMove.getLanding().getXcoordinate() == 4) && (CurrentMove.getLanding().getYcoordinate() == 4)) {

                CompareValue = 1;

                if (CompareValue > CurrentValue) {
                    CurrentValue = CompareValue;
                    BestMove = CurrentMove;
                }

            }

            while (!BlackMove.empty()) {

                if ((CurrentMove.getLanding().getXcoordinate() == BlackPosition.getXcoordinate()) && (CurrentMove.getLanding().getYcoordinate() == BlackPosition.getYcoordinate())) {


                    int value = Value();
                    CurrentValue = 0;
                    CompareValue = value;
                    System.out.println(value);

                    if (CompareValue > CurrentValue) {
                        CurrentValue = CompareValue;
                        BestMove = CurrentMove;
                    }

                }

                BlackMove = (Stack) BlackMovementPossibilities.clone();

            }
        }

        if (CurrentValue > 0) {
            System.out.println("Selected Best Move : " + CurrentValue);
            return BestMove;
        }

        return randomMove(FinalMove);
    }


    public Stack TwoLevelDeep(Stack WhiteMovementPossibilities, Stack BlackMovementPossibilities){


        Stack FinalMove = (Stack) WhiteMovementPossibilities.clone();
        Stack BlackMove = (Stack) BlackMovementPossibilities.clone();

        return FinalMove;

    }




}
