class Square{

    public int Xcoordinate;
    public int Ycoordinate;
    public String PieceName;

    public Square (int X, int Y, String name){
        Xcoordinate = X;
        Ycoordinate = Y;
        PieceName = name;
    }

    public Square(int X, int Y){
        Xcoordinate =X;
        Ycoordinate =Y;
        PieceName = "";
    }

    public int getXcoordinate(){
        return Xcoordinate;
    }

    public int getYcoordinate() {
        return Ycoordinate;
    }

    public String getPieceName() {
        return PieceName;
    }
}