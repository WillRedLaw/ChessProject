import java.awt.*;
import java.awt.event.*;
import java.time.temporal.Temporal;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;


/*
	This class can be used as a starting point for creating your Chess game project. The only piece that
	has been coded is a white pawn...a lot done, more to do!
*/

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
	int startX;
	int startY;
	int initialX;
	int initialY;

	JPanel panels;
	JLabel pieces;
	String KingRemovedFromBoard;
	Boolean PlayerTurn = false;

	Stack Temporary;

	AIAgent agent;
	Boolean agentwins;
	Boolean whiteTwoMove = false;

	public static int n;



	public ChessProject(){
        Dimension boardSize = new Dimension(600, 600);

        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a chess board to the Layered Pane
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout( new GridLayout(8, 8) );
        chessBoard.setPreferredSize( boardSize );
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel( new BorderLayout() );
            chessBoard.add( square );

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground( i % 2 == 0 ? Color.white : Color.gray );
            else
                square.setBackground( i % 2 == 0 ? Color.gray : Color.white );
        }

        // Setting up the Initial Chess board.
		for(int i=8;i < 16; i++){
       		pieces = new JLabel( new ImageIcon("WhitePawn.png") );
			panels = (JPanel)chessBoard.getComponent(i);
	        panels.add(pieces);
		}
		pieces = new JLabel( new ImageIcon("WhiteRook.png") );
		panels = (JPanel)chessBoard.getComponent(0);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKnight.png") );
		panels = (JPanel)chessBoard.getComponent(1);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKnight.png") );
		panels = (JPanel)chessBoard.getComponent(6);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteBishup.png") );
		panels = (JPanel)chessBoard.getComponent(2);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteBishup.png") );
		panels = (JPanel)chessBoard.getComponent(5);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteKing.png") );
		panels = (JPanel)chessBoard.getComponent(3);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
		panels = (JPanel)chessBoard.getComponent(4);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("WhiteRook.png") );
		panels = (JPanel)chessBoard.getComponent(7);
	    panels.add(pieces);
		for(int i=48;i < 56; i++){
       		pieces = new JLabel( new ImageIcon("BlackPawn.png") );
			panels = (JPanel)chessBoard.getComponent(i);
	        panels.add(pieces);
		}
		pieces = new JLabel( new ImageIcon("BlackRook.png") );
		panels = (JPanel)chessBoard.getComponent(56);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKnight.png") );
		panels = (JPanel)chessBoard.getComponent(57);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKnight.png") );
		panels = (JPanel)chessBoard.getComponent(62);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackBishup.png") );
		panels = (JPanel)chessBoard.getComponent(58);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackBishup.png") );
		panels = (JPanel)chessBoard.getComponent(61);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackKing.png") );
		panels = (JPanel)chessBoard.getComponent(59);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackQueen.png") );
		panels = (JPanel)chessBoard.getComponent(60);
	    panels.add(pieces);
		pieces = new JLabel( new ImageIcon("BlackRook.png") );
		panels = (JPanel)chessBoard.getComponent(63);
	    panels.add(pieces);

		agent = new AIAgent();
		agentwins = false;
		Temporary = new Stack();
    }

	/*
		This method checks if there is a piece present on a particular square.
	*/


	private Stack FindWhitePieces(){

		Stack squares = new Stack();
		String icon;
		int x;
		int y;
		String pieceName;

		for(int i = 0; i < 600; i+=75){
			for(int j = 0; j <600; j+=75){
				y = i/75;
				x = j/75;

				Component tmp = chessBoard.findComponentAt(j, i);
				if(tmp instanceof JLabel){
					chessPiece = (JLabel)tmp;
					icon = chessPiece.getIcon().toString();
					pieceName = icon.substring(0,(icon.length()-4));
					if(pieceName.contains("White")){
						Square stmp = new Square(x,y, pieceName);
						squares.push(stmp);
					}
				}
			}
		}

		return squares;

	}

	private Stack FindBlackPieces(){

		Stack squares = new Stack();
		String icon;
		int x;
		int y;
		String pieceName;

		for(int i = 0; i < 600; i+=75) {
			for (int j = 0; j < 600; j += 75) {
				y = i / 75;
				x = j / 75;

				Component tmp = chessBoard.findComponentAt(j,i);
				if(tmp instanceof JLabel ){
					chessPiece = (JLabel) tmp;
					icon = chessPiece.getIcon().toString();
					pieceName = icon.substring(0, (icon.length()-4));

					if(pieceName.contains("Black")) {
						Square stmp = new Square(x, y, pieceName);
						squares.push(stmp);
					}
				}
			}
		}

		return squares;

	}


	private Stack getWhiteAttackingSquares(Stack pieces){

		Stack temppiece;
		while(!pieces.empty()){
			Square s = (Square)pieces.pop();
			String tmpString = s.getPieceName();


			if(tmpString.contains("Knight")){


				temppiece = getKnightMoves(s.getXcoordinate(), s.getYcoordinate(), s.getPieceName());
				{
					while(!temppiece.empty()){
						Square tempKnight = (Square)temppiece.pop();
						temppiece.push(tempKnight);
					}
				}
			}

			else if(tmpString.contains("Bishup")){

			}

			else if(tmpString.contains("Pawn")){

			}

			else if(tmpString.contains("Rook")){

			}


		}
		return pieces;
	}

	//getKnightMoves
	private Stack getKnightMoves(int x, int y, String pieces) {

		Stack moves = new Stack();
		Stack attacking = new Stack();

		Square s = new Square(x + 1, y + 2);
		moves.push(s);

		Square s1 = new Square(x + 1, y - 2);
		moves.push(s1);

		Square s2 = new Square(x - 1, y + 2);
		moves.push(s2);

		Square s3 = new Square(x - 1, y - 2);
		moves.push(s3);

		Square s4 = new Square(x + 2, y + 1);
		moves.push(s4);

		Square s5 = new Square(x + 2, y - 1);
		moves.push(s5);

		Square s6 = new Square(x - 2, y + 1);
		moves.push(s6);

		Square s7 = new Square(x - 2, y - 1);
		moves.push(s7);

		for (int i = 0; i < 8; i++) {

			Square tmp = (Square) moves.pop();

			if ((tmp.getXcoordinate() < 0) || ((tmp.getXcoordinate() > 7) || ((tmp.getYcoordinate() < 0) || tmp.getYcoordinate() > 7))) {}

			else if (piecePresent(((tmp.getXcoordinate() * 75) + 20), ((tmp.getYcoordinate() * 75) + 20))) {
				if (pieces.contains("White")) {
					if (checkWhiteOponent(((tmp.getXcoordinate() * 75) + 20), ((tmp.getYcoordinate() * 75) + 20))) {
						attacking.push(tmp);
					}
					else {
						System.out.println("Own Piece");
					}
				}
				else {
					if (checkBlackOponent(tmp.getXcoordinate(), tmp.getYcoordinate())){
						attacking.push(tmp);
					}
				}
			}

			else {
				attacking.push(tmp);
			}
		}


		Stack tmp = attacking;
		colourSquares(tmp);
		return attacking;

	}

	//getPawnMoves
	private Stack getRookMoves(int x, int y, String piece){

		Square startingSquare = new Square(x,y,piece);
		Stack moves = new Stack();
		Move validM, validM2, validM3, validM4;


		return moves;
	}

	private void colourSquares(Stack squares){

		Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
		while(!squares.empty()){
			Square s = (Square)squares.pop();
			int location = s.getXcoordinate() + ((s.getYcoordinate())*8);
			JPanel panel = (JPanel)chessBoard.getComponent(location);
			panel.setBorder(greenBorder);
		}

	}

	private  void resetBorders(){

		Border empty = BorderFactory.createEmptyBorder();

		for(int i  = 0; i < 64; i++) {

			JPanel tmppanel = (JPanel) chessBoard.getComponent(i);
			tmppanel.setBorder(empty);

		}
	}

	private void getLandingSqaures(Stack found){
		Move tmp;
		Square Landing;
		Stack squares = new Stack();
		while(!found.empty()){
			tmp = (Move) found.pop();
			Landing = (Square)tmp.getLanding();
			squares.push(Landing);
		}

		colourSquares(squares);

	}

	private void PrintStack(Stack input){
		Move m;
		Square s, l;
		while(!input.empty()){
			m = (Move)input.pop();
			s = (Square)m.getStart();
			l = (Square)m.getLanding();

			System.out.println("The possible move that was found is : ("+s.getXcoordinate()+" , "+s.getYcoordinate()+"), landing at ("+l.getXcoordinate()+" , "+l.getYcoordinate()+")");
		}
	}

	private void makeAIMove(){

		resetBorders();
		layeredPane.validate();
		layeredPane.repaint();

		Stack white = FindWhitePieces();
		Stack black = FindBlackPieces();
		Stack CompleteMoves = new Stack();
		Move tmp;


		while(!white.empty()){

			Square s = (Square) white.pop();
			String tmpString = s.getPieceName();
			Stack tmpMove = new Stack();
			Stack temporary = new Stack();



			if(tmpString.contains("Knight")){
				tmpMove = getKnightMoves(s.getXcoordinate(), s.getYcoordinate(), s.getPieceName());
			}

			else if(tmpString.contains("Rook")){
				tmpMove = getRookMoves(s.getXcoordinate(), s.getYcoordinate(), s.getPieceName());
			}

			//The rest

			while(!tmpMove.empty()){
				tmp = (Move)tmpMove.pop();
				CompleteMoves.push(tmp);
			}
		}

		Temporary = (Stack)CompleteMoves.clone();
		getLandingSqaures(Temporary);
		PrintStack(Temporary);

		//If a valid move cannot be made, it is a stalemate so the came ends
		if(CompleteMoves.size() == 0){
			JOptionPane.showMessageDialog(null, "Cogratulations, you have placed the AI component in a Stale Mate Position");
			System.exit(0);
		}

		else{
			System.out.println("=============================================================");
			Stack testing = new Stack();
			while(!CompleteMoves.empty()){
				Move tmpMove = (Move)CompleteMoves.pop();
				Square s1 = (Square)tmpMove.getStart();
				Square s2 = (Square)tmpMove.getLanding();
				System.out.println("The "+s1.getPieceName()+" can move from ("+s1.getXcoordinate()+", "+s1.getYcoordinate()+") to the following square: ("+s2.getXcoordinate()+", "+s2.getYcoordinate()+")");
				testing.push(tmpMove);
			}
			System.out.println("=============================================================");
			Border redBorder = BorderFactory.createLineBorder(Color.RED, 3);
			Move selectedMove = null;
			if(n==1){
				selectedMove = agent.nextBestMove(testing, black);
			}

			else{
				selectedMove= agent.randomMove(testing);
			}
			Square startingPoint = (Square)selectedMove.getStart();
			Square landingPoint = (Square)selectedMove.getLanding();
			int startX1 = (startingPoint.getXcoordinate()*75)+20;
			int startY1 = (startingPoint.getYcoordinate()*75)+20;
			int landingX1 = (landingPoint.getXcoordinate()*75)+20;
			int landingY1 = (landingPoint.getYcoordinate()*75)+20;
			System.out.println("-------- Move "+startingPoint.getPieceName()+" ("+startingPoint.getXcoordinate()+", "+startingPoint.getYcoordinate()+") to ("+landingPoint.getXcoordinate()+", "+landingPoint.getYcoordinate()+")");

			Component c  = (JLabel)chessBoard.findComponentAt(startX1, startY1);
			Container parent = c.getParent();
			parent.remove(c);
			int panelID = (startingPoint.getYcoordinate() * 8)+startingPoint.getXcoordinate();
			panels = (JPanel)chessBoard.getComponent(panelID);
			panels.setBorder(redBorder);
			parent.validate();

			Component l = chessBoard.findComponentAt(landingX1, landingY1);
			if(l instanceof JLabel){
				Container parentlanding = l.getParent();
				JLabel awaitingName = (JLabel)l;
				String agentCaptured = awaitingName.getIcon().toString();
				if(agentCaptured.contains("King")){
					agentwins = true;
				}
				parentlanding.remove(l);
				parentlanding.validate();
				pieces = new JLabel( new ImageIcon(startingPoint.getPieceName()+".png") );
				int landingPanelID = (landingPoint.getYcoordinate()*8)+landingPoint.getXcoordinate();
				panels = (JPanel)chessBoard.getComponent(landingPanelID);
				panels.add(pieces);
				panels.setBorder(redBorder);
				layeredPane.validate();
				layeredPane.repaint();

				if(agentwins){
					JOptionPane.showMessageDialog(null, "The AI Agent has won!");
					System.exit(0);
				}
			}
			else{
				pieces = new JLabel( new ImageIcon(startingPoint.getPieceName()+".png") );
				int landingPanelID = (landingPoint.getYcoordinate()*8)+landingPoint.getXcoordinate();
				panels = (JPanel)chessBoard.getComponent(landingPanelID);
				panels.add(pieces);
				panels.setBorder(redBorder);
				layeredPane.validate();
				layeredPane.repaint();
			}
		}

		whiteTwoMove = false;

	}

	private Boolean piecePresent(int x, int y){
		Component c = chessBoard.findComponentAt(x, y);
		return !(c instanceof JPanel);
	}

	private String pieceAround(int x, int y){
		Component c1 = chessBoard.findComponentAt(x,y);
		if(c1 instanceof JPanel){
			return "empty";
		}

		else if (c1 instanceof JLabel){
			JLabel PieceName = (JLabel) c1;
			String tmp1 = PieceName.getIcon().toString();
			return tmp1;
		}
		else{
			return "empty";
		}
	}


	private Boolean checkWhiteOponent(int newX, int newY){
		Boolean oponent;
		Component c1 = chessBoard.findComponentAt(newX, newY);
		JLabel awaitingPiece = (JLabel)c1;
		String tmp1 = awaitingPiece.getIcon().toString();
		if(((tmp1.contains("Black")))){
			if(tmp1.contains("King")){
				KingRemovedFromBoard = "White player has won the game.";
			}
			oponent = true;
		}
		else{
			oponent = false;
		}
		return oponent;
	}
	/*
		This is a method to check if a piece is a Black piece.
	*/
	private Boolean checkBlackOponent(int newX, int newY){
		Boolean oponent;
		Component c1 = chessBoard.findComponentAt(newX, newY);
		JLabel awaitingPiece = (JLabel) c1;
		String tmp1 = awaitingPiece.getIcon().toString();
		if(((tmp1.contains("White")))){
			if(tmp1.contains("King")){
				KingRemovedFromBoard ="Black player has won the game.";
			}
			oponent = true;
		}
		else{
			oponent = false;
		}
		return oponent;
	}
	
	/*
		This method is called when we press the Mouse. So we need to find out what piece we have
		selected. We may also not have selected a piece!
	*/
    public void mousePressed(MouseEvent e){
        chessPiece = null;
        Component c =  chessBoard.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel)
			return;

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (JLabel)c;
		initialX = e.getX();
		initialY = e.getY();
		startX = (e.getX()/75);
		startY = (e.getY()/75);
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);


    }

    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;
         chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
     }

 	/*
		This method is used when the Mouse is released...we need to make sure the move was valid before
		putting the piece back on the board.
	*/
    public void mouseReleased(MouseEvent e) {

		if (chessPiece == null) return;

		chessPiece.setVisible(false);
		Boolean successWhitePawn = false;
		Boolean successBlackPawn = false;
		Component c = chessBoard.findComponentAt(e.getX(), e.getY());
		String tmp = chessPiece.getIcon().toString();
		String pieceName = tmp.substring(0, (tmp.length() - 4));
		Boolean validMove = false;


		/*
			The only piece that has been enabled to move is a White Pawn...but we should really have this is a separate
			method somewhere...how would this work.

			So a Pawn is able to move two squares forward one its first go but only one square after that.
			The Pawn is the only piece that cannot move backwards in chess...so be careful when committing
			a pawn forward. A Pawn is able to take any of the opponent’s pieces but they have to be one
			square forward and one square over, i.e. in a diagonal direction from the Pawns original position.
			If a Pawn makes it to the top of the other side, the Pawn can turn into any other piece, for
			demonstration purposes the Pawn here turns into a Queen.
		*/

// Variables
		int LandingX = (e.getX() / 75);
		int LandingY = (e.getY() / 75);
		int MovementX = Math.abs((e.getX() / 75) - startX);
		int MovementY = Math.abs((e.getY() / 75) - startY);
		Scanner PawnUpgrade = new Scanner(System.in);

// Variables
//Test Output
		System.out.println("---------------");
		System.out.println("The Moved piece is : " + pieceName);
		System.out.println("The starting coordinates are : " + startX + "," + startY);
		System.out.println("The MovementX is : " + MovementX);
		System.out.println("The MovementY is : " + MovementY);
		System.out.println("The Landing coordinates are : " + ("( " + LandingX + "," + LandingY + " )"));
		System.out.println("---------------");
//Test Output

//Queen ---------------------------------------------------------------------------------------------
			if (pieceName.contains("Queen")) {

				boolean PieceInTheWay = false;
				int DistanceX = Math.abs(startX - LandingX);
				int DistanceY = Math.abs(startY - LandingY);

				if ((((MovementX >= 1) && MovementY >= 1)) && (MovementX != MovementY))
//				LandingX >= 1 && Landing >=2 && LandingX != LandingY
				{
					validMove = false;
				}
				// Forward Back LEFT RIGHT movement
				else {
					if (((Math.abs(startX - LandingX) != 0) && (Math.abs(startY - LandingY) == 0)) || ((Math.abs(startX - LandingX) == 0) && (Math.abs(LandingY - startY) != 0))) {
						if (Math.abs(startX - LandingX) != 0) {
							if (startX - LandingX > 0) {
								for (int i = 0; i < DistanceX; i++) {
									if (piecePresent(initialX - (i * 75), e.getY())) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							} else {
								for (int i = 0; i < DistanceX; i++) {
									if (piecePresent(initialX + (i * 75), e.getY())) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							}
						} else {
							if (startY - LandingY > 0) {
								for (int i = 0; i < DistanceY; i++) {
									if (piecePresent(e.getX(), initialY - (i * 75))) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							} else {
								for (int i = 0; i < DistanceY; i++) {
									if (piecePresent(e.getX(), initialY + (i * 75))) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							}
						}
					}//End of Forward Back Left Right Movement
					//Diagnoal movement
					if ((Math.abs(startX - LandingX) == Math.abs(startY - LandingY))) {
						if ((startX - LandingX < 0) && (startY - LandingY < 0)) {
							for (int i = 0; i < DistanceX; i++) {
								if (piecePresent((initialX + (i * 75)), (initialY + (i * 75)))) {
									PieceInTheWay = true;

								}
							}
						} else if ((startX - LandingX < 0) && (startY - LandingY > 0)) {
							for (int i = 0; i < DistanceX; i++) {
								if (piecePresent((initialX + (i * 75)), (initialY - (i * 75)))) {
									PieceInTheWay = true;
								}
							}
						} else if ((startX - LandingX > 0) && (startY - LandingY > 0)) {
							for (int i = 0; i < DistanceX; i++) {
								if (piecePresent((initialX - (i * 75)), (initialY - (i * 75)))) {
									PieceInTheWay = true;
								}
							}
						} else if ((startX - LandingX > 0) && (startY - LandingY < 0)) {
							for (int i = 0; i < DistanceX; i++) {
								if (piecePresent((initialX - (i * 75)), (initialY + (i * 75)))) {
									PieceInTheWay = true;
								}
							}
						}
					}//End of Diagnoal Movement

					if (PieceInTheWay) {
						validMove = false;
					} else {
						if (piecePresent(e.getX(), (e.getY()))) {
							if (pieceName.contains("White")) {
								if (checkWhiteOponent(e.getX(), e.getY())) {
									validMove = true;
								} else {
									validMove = false;
								}
							} else {
								if (checkBlackOponent(e.getX(), e.getY())) {
									validMove = true;
								} else {
									validMove = false;
								}
							}
						} else {
							validMove = true;
						}
					}


				}//END OF FIRST ELSE
			}// END OF QUEEN LINE
//King ----------------------------------------------------------------------------------------------
			else if (pieceName.contains("King")) {
				if (!piecePresent(e.getX(), e.getY())) {
					if (pieceName.contains("BlackKing")) {

						if ((MovementX >= 2) || (MovementY >= 2)) {
							validMove = false;
						} else if (((MovementX == 1) && (MovementY == 0)) || ((MovementX == 0) && (MovementY == 1))) {

							if ((pieceAround((e.getX() + 75), e.getY()).contains("White")) || (pieceAround((e.getX() - 75), e.getY()).contains("White")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("White")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("White")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("White")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("White")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("White")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("White"))) {
								if ((pieceAround((e.getX() + 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX() - 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Bishup"))) {
									validMove = false;
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Rook")) || (pieceAround((e.getX() - 75), e.getY()).contains("Rook")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Rook"))) {
									validMove = false;
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Knight")) || (pieceAround((e.getX() - 75), e.getY()).contains("Knight")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Knight"))) {
									validMove = true;
								} else {
									validMove = false;
								}
							} else if (!piecePresent(e.getX(), e.getY())) {
								validMove = true;
							}
						} else if ((MovementX == 1) && (MovementY == 1)) {

							if ((pieceAround((e.getX() + 75), e.getY()).contains("White")) || (pieceAround((e.getX() - 75), e.getY()).contains("White")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("White")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("White")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("White")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("White")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("White")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("White"))) {
								if ((pieceAround((e.getX() + 75), e.getY()).contains("Rook")) || (pieceAround((e.getX() - 75), e.getY()).contains("Rook")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Rook"))) {
									validMove = true;
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX() - 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Bishup"))) {
									if ((pieceAround((e.getX() + 75), e.getY()).contains("WhitePawn")) || (pieceAround((e.getX() - 75), e.getY()).contains("WhitePawn")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("WhitePawn")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("WhitePawn")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("WhitePawn")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("WhitePawn")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("WhitePawn")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("WhitePawn"))) {
										validMove = false;
									} else {
										validMove = false;
									}
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Knight")) || (pieceAround((e.getX() - 75), e.getY()).contains("Knight")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Knight"))) {
									validMove = true;
								} else {
									validMove = false;
								}
							} else if (!piecePresent(e.getX(), e.getY())) {
								validMove = true;
							}
						}
					}

					if (pieceName.contains(("WhiteKing"))) {
						if ((MovementX >= 2) || (MovementY >= 2)) {
							validMove = false;
						} else if (((MovementX == 1) && (MovementY == 0)) || ((MovementX == 0) && (MovementY == 1))) {
							if ((pieceAround((e.getX() + 75), e.getY()).contains("Black")) || (pieceAround((e.getX() - 75), e.getY()).contains("Black")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Black")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Black")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Black")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Black")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Black")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Black"))) {
								if ((pieceAround((e.getX() + 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX() - 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Bishup"))) {
									validMove = false;
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Rook")) || (pieceAround((e.getX() - 75), e.getY()).contains("Rook")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Rook"))) {
									validMove = false;
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Knight")) || (pieceAround((e.getX() - 75), e.getY()).contains("Knight")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Knight"))) {
									validMove = true;
								} else {
									validMove = false;
								}
							} else if (!piecePresent(e.getX(), e.getY())) {
								validMove = true;
							}
						} else if ((MovementX == 1) && (MovementY == 1)) {
							if ((pieceAround((e.getX() + 75), e.getY()).contains("Black")) || (pieceAround((e.getX() - 75), e.getY()).contains("Black")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Black")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Black")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Black")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Black")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Black")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Black"))) {
								if ((pieceAround((e.getX() + 75), e.getY()).contains("Rook")) || (pieceAround((e.getX() - 75), e.getY()).contains("Rook")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Rook")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Rook")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Rook"))) {
									validMove = true;
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX() - 75), e.getY()).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Bishup")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Bishup")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Bishup"))) {
									if ((pieceAround((e.getX() + 75), e.getY()).contains("BlackPawn")) || (pieceAround((e.getX() - 75), e.getY()).contains("BlackPawn")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("BlackPawn")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("BlackPawn")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("BlackPawn")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("BlackPawn")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("BlackPawn")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("BlackPawn"))) {
										validMove = false;
									} else {
										validMove = false;
									}
								} else if ((pieceAround((e.getX() + 75), e.getY()).contains("Knight")) || (pieceAround((e.getX() - 75), e.getY()).contains("Knight")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("Knight")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("Knight")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("Knight"))) {
									validMove = true;
								}
							} else if (!piecePresent(e.getX(), e.getY())) {
								validMove = true;
							}
						}
					}
				}
				if (piecePresent(e.getX(), e.getY())) {
					if ((MovementX >= 2) || (MovementY >= 2)) {
						validMove = false;
					} else {
						if (piecePresent(e.getX(), e.getY())) {
							if (pieceName.contains("White")) {
								if (checkWhiteOponent(e.getX(), e.getY())) {
									validMove = true;
									if ((pieceAround((e.getX() + 75), e.getY()).contains("King")) || (pieceAround((e.getX() - 75), e.getY()).contains("King")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("King")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("King")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("King")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("King")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("King")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("King"))) {
										validMove = false;
									}

								} else {
									validMove = false;
								}
							} else {
								if (checkBlackOponent(e.getX(), e.getY())) {
									validMove = true;
									if ((pieceAround((e.getX() + 75), e.getY()).contains("King")) || (pieceAround((e.getX() - 75), e.getY()).contains("King")) || (pieceAround((e.getX()), (e.getY() + 75)).contains("King")) || (pieceAround((e.getX()), (e.getY() - 75)).contains("King")) || (pieceAround((e.getX() + 75), (e.getY() + 75)).contains("King")) || (pieceAround((e.getX() - 75), (e.getY() + 75)).contains("King")) || (pieceAround((e.getX() + 75), (e.getY() - 75)).contains("King")) || (pieceAround((e.getX() - 75), (e.getY() - 75)).contains("King"))) {
										validMove = false;
									}
								} else {
									validMove = false;
								}
							}
						} else {
							validMove = true;
						}
					}
				}
			}//End of King lines
//King ----------------------------------------------------------------------------------------------
//Rook ----------------------------------------------------------------------------------------------
			else if (pieceName.contains("Rook")) {
				boolean PieceInTheWay = false;
				int DistanceX = Math.abs(startX - LandingX);
				int DistanceY = Math.abs(startY - LandingY);


				if (((LandingX < 0) || LandingX > 7) || ((LandingY < 0) || (LandingY > 7))) {
					validMove = false;
				} else {
					if (((Math.abs(startX - LandingX) != 0) && (Math.abs(startY - LandingY) == 0)) || ((Math.abs(startX - LandingX) == 0) && (Math.abs(LandingY - startY) != 0))) {
						if (Math.abs(startX - LandingX) != 0) {
							if (startX - LandingX > 0) {
								for (int i = 0; i < DistanceX; i++) {
									if (piecePresent(initialX - (i * 75), e.getY())) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							} else {
								for (int i = 0; i < DistanceX; i++) {
									if (piecePresent(initialX + (i * 75), e.getY())) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							}
						} else {
							if (startY - LandingY > 0) {
								for (int i = 0; i < DistanceY; i++) {
									if (piecePresent(e.getX(), initialY - (i * 75))) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							} else {
								for (int i = 0; i < DistanceY; i++) {
									if (piecePresent(e.getX(), initialY + (i * 75))) {
										PieceInTheWay = true;
										break;

									} else {
										PieceInTheWay = false;
									}
								}
							}
						}
						if (PieceInTheWay) {
							validMove = false;
						} else {
							if (piecePresent(e.getX(), e.getY())) {
								if (pieceName.contains("White")) {
									if (checkWhiteOponent(e.getX(), e.getY())) {
										validMove = true;
									} else {
										validMove = false;
									}
								} else {
									if (checkBlackOponent(e.getX(), e.getY())) {
										validMove = true;
									} else {
										validMove = false;
									}
								}
							} else {
								validMove = true;
							}
						}
					} else {
						validMove = false;
					}
				}//END IF MATHS.ABS
			}//END ELSE
			//END ELSE IF CONTAINS
//Rook ----------------------------------------------------------------------------------------------
//Bishop --------------------------------------------------------------------------------------------
			else if (pieceName.contains("Bishup")) {

				boolean PieceInTheWay = false;
				int Distance = Math.abs(startX - LandingX);

				if (((LandingX < 0) || (LandingX > 7)) || ((LandingY < 0) || (LandingY > 7))) {
					validMove = false;
				} else if (Math.abs(startX - LandingX) == Math.abs(startY - LandingY)) {
					if ((startX - LandingX < 0) && (startY - LandingY < 0)) {
						for (int i = 0; i < Distance; i++) {
							if (piecePresent((initialX + (i * 75)), (initialY + (i * 75)))) {
								PieceInTheWay = true;

							}
						}
					} else if ((startX - LandingX < 0) && (startY - LandingY > 0)) {
						for (int i = 0; i < Distance; i++) {
							if (piecePresent((initialX + (i * 75)), (initialY - (i * 75)))) {
								PieceInTheWay = true;
							}
						}
					} else if ((startX - LandingX > 0) && (startY - LandingY > 0)) {
						for (int i = 0; i < Distance; i++) {
							if (piecePresent((initialX - (i * 75)), (initialY - (i * 75)))) {
								PieceInTheWay = true;
							}
						}
					} else if ((startX - LandingX > 0) && (startY - LandingY < 0)) {
						for (int i = 0; i < Distance; i++) {
							if (piecePresent((initialX - (i * 75)), (initialY + (i * 75)))) {
								PieceInTheWay = true;
							}
						}
					}

					if (PieceInTheWay) {
						validMove = false;
					} else {
						if (piecePresent(e.getX(), (e.getY()))) {
							if (pieceName.contains("White")) {
								if (checkWhiteOponent(e.getX(), e.getY())) {
									validMove = true;
								} else {
									validMove = false;
								}
							} else {
								if (checkBlackOponent(e.getX(), e.getY())) {
									validMove = true;
								} else {
									validMove = false;
								}
							}
						} else {
							validMove = true;
						}
					}
				}// End for ELSE IF MATHS.ABS
			}//End of Bishop
//Bishop --------------------------------------------------------------------------------------------
//KNIGHT --------------------------------------------------------------------------------------------
			else if (pieceName.contains("Knight")) {
				if ((((MovementX == 1) && MovementY == 2)) || ((MovementX == 2) && (MovementY == 1))) {
					if (!piecePresent(e.getX(), e.getY())) {
						validMove = true;
					} else {
						if (pieceName.contains("White")) {
							if (checkWhiteOponent(e.getX(), e.getY())) {
								validMove = true;
							}
						} else {
							if (checkBlackOponent(e.getX(), e.getY())) {
								validMove = true;
							}
						}
					}
				}
			}
//KNIGHT --------------------------------------------------------------------------------------------
//BLACK PAWN ----------------------------------------------------------------------------------------
			else if (pieceName.equals("BlackPawn")) {
				if (startY == 6) {
					if (((MovementY == 1) || (MovementY == 2)) && (startY > LandingY) && (MovementX == 0)) {
						if (MovementY == 2) {
							if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), e.getY() + 75))) {
								validMove = true;
							}
						} else {
							if (!piecePresent(e.getX(), e.getY())) {
								validMove = true;
							}
						}
					} else if ((MovementY == 1) && (startY > LandingY) && (MovementX == 1)) {
						if (piecePresent(e.getX(), e.getY())) {
							if (checkBlackOponent(e.getX(), e.getY())) {
								validMove = true;
								if (startY == 1) {
									successBlackPawn = true;

								}
							}
						}
					}
				} else {
					if (((MovementY == 1)) && (startY > LandingY) && (MovementX == 0)) {
						if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), e.getY() + 75))) {
							validMove = true;
						}
					} else if ((MovementY == 1) && (startY > LandingY) && (MovementX == 1)) {
						if (piecePresent(e.getX(), e.getY())) {
							if (checkBlackOponent(e.getX(), e.getY())) {
								validMove = true;
								if (startY == 1) {
									successBlackPawn = true;
								}
							}
						}
					}
				}
			}
//BLACK PAWN ----------------------------------------------------------------------------------------
//White Pawn ----------------------------------------------------------------------------------------
			else if (pieceName.equals("WhitePawn")) {
				if (startY == 1) {
					if (((MovementY == 1) || (MovementY == 2)) && (startY < LandingY) && (MovementX == 0)) {
						if (MovementY == 2) {
							if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), e.getY() - 75))) {
								validMove = true;
							}
						} else {
							if (!piecePresent(e.getX(), e.getY())) {
								validMove = true;
							}
						}
					} else if ((MovementY == 1) && (startY < LandingY) && (MovementX == 1)) {
						if (piecePresent(e.getX(), e.getY())) {
							if (checkWhiteOponent(e.getX(), e.getY())) {
								validMove = true;
								if (startY == 6) {
									successWhitePawn = true;

								}
							}
						}
					}
				} else {
					if (((MovementY == 1)) && (startY < LandingY) && (MovementX == 0)) {
						if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), e.getY() - 75))) {
							validMove = true;
						}
					} else if ((MovementY == 1) && (startY < LandingY) && (MovementX == 1)) {
						if (piecePresent(e.getX(), e.getY())) {
							if (checkWhiteOponent(e.getX(), e.getY())) {
								validMove = true;
								if (startY == 6) {
									successWhitePawn = true;
								}
							}
						}
					}
				}

			}
//White Pawn ----------------------------------------------------------------------------------------

// ChessPiece Movement ------------------------------------------------------------------------------

		if (!validMove) {
			int location = 0;
			if (startY == 0) {
				location = startX;
			} else {
				location = (startY * 8) + startX;
			}
			String pieceLocation = pieceName + ".png";
			pieces = new JLabel(new ImageIcon(pieceLocation));
			panels = (JPanel) chessBoard.getComponent(location);
			panels.add(pieces);

		} else {
			if (successWhitePawn) {
				int location = 56 + (e.getX() / 75);

				System.out.println("What Piece do you want to change the Pawn into ?");
				System.out.println("Queen : 1");
				System.out.println("Bishop : 2");
				System.out.println("Knight : 3");
				System.out.println("Rook : 4");
				int UserInput = PawnUpgrade.nextInt();

				if (UserInput == 1) {

					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}
				}

				if (UserInput == 2) {

					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("WhiteBishup.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("WhiteBishup.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}
				}

				if (UserInput == 3) {

					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}
				}

				if (UserInput == 4) {

					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("WhiteRook.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("WhiteRook.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}
				}
			} else if (successBlackPawn) {
				int location = 0 + (e.getX() / 75);
				System.out.println("What Piece do you want to change the Pawn into ?");
				System.out.println("Queen : 1");
				System.out.println("Bishop : 2");
				System.out.println("Knight : 3");
				System.out.println("Rook : 4");
				int UserInput = PawnUpgrade.nextInt();


				if (UserInput == 1) {
					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("BlackQueen.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("BlackQueen.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}

				}
				if (UserInput == 2) {
					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("BlackBishup.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("BlackBishup.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}

				}

				if (UserInput == 3) {
					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("BlackKnight.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("BlackKnight.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}

				}

				if (UserInput == 4) {
					if (c instanceof JLabel) {
						Container parent = c.getParent();
						parent.remove(0);
						pieces = new JLabel(new ImageIcon("BlackRook.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);
					} else {
						Container parent = (Container) c;
						pieces = new JLabel(new ImageIcon("BlackRook.png"));
						parent = (JPanel) chessBoard.getComponent(location);
						parent.add(pieces);

					}

				}
			} else {
				if (c instanceof JLabel) {
					Container parent = c.getParent();
					parent.remove(0);
					parent.add(chessPiece);
				} else {
					Container parent = (Container) c;
					parent.add(chessPiece);
				}
				chessPiece.setVisible(true);

				if (KingRemovedFromBoard != null) {
					JOptionPane.showMessageDialog(null, KingRemovedFromBoard);
					System.exit(0);
				}
			}
	}
    }//On Mouse release

    public void mouseClicked(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e) {
   }
    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e) {

    }

	/*
		Main method that gets the ball moving.
	*/
    public static void main(String[] args) {




        ChessProject frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);

		Object[] options = {"Random Moves","Best Next Move","Based on Opponents Moves"};
		 int n = JOptionPane.showOptionDialog(frame,"Lets play some Chess, choose your AI opponent","Introduction to AI Continuous Assessment", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
		System.out.println("The selected variable is : "+n);
		frame.makeAIMove();





     }
}




