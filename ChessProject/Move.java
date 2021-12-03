public class Move {

    Square start;
    Square landing;
    String PieceName;
    int PieceValue;



    public Move(Square x, Square y){

        start = x;
        landing = y;

    }

    public Move(Square x, Square y, String name){

        start = x;
        landing = y;
        PieceName = name;

    }

    public Square getStart(){

        return start;
    }

    public Square getLanding(){

        return landing;
    }

    public int getPieceValue(){



    return PieceValue;

    }


}
