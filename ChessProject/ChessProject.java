import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

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
    }

	/*
		This method checks if there is a piece present on a particular square.
	*/
	private Boolean piecePresent(int x, int y){
		Component c = chessBoard.findComponentAt(x, y);
		return !(c instanceof JPanel);
	}

	/*
		This is a method to check if a piece is a Black piece.
	*/
	private Boolean checkWhiteOponent(int newX, int newY){
		Boolean oponent;
		Component c1 = chessBoard.findComponentAt(newX, newY);
		JLabel awaitingPiece = (JLabel)c1;
		String tmp1 = awaitingPiece.getIcon().toString();
		if(((tmp1.contains("Black")))){
			oponent = true;
		}
		else{
			oponent = false;
		}
		return oponent;
	}

	private Boolean checkBlackOponent(int newX, int newY){
		Boolean oponent;
		Component c1 = chessBoard.findComponentAt(newX, newY);
		JLabel awaitingPiece = (JLabel) c1;
		String tmp1 = awaitingPiece.getIcon().toString();
		if(((tmp1.contains("White")))){
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
		Boolean success = false;
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


		if (pieceName.contains("Queen")) {

			boolean PieceInTheWay = false;
			int DistanceX = Math.abs(startX - LandingX);
			int DistanceY = Math.abs(startY - LandingY);

			if((((MovementX >=1) && MovementY >= 1)) && (MovementX != MovementY))
//				LandingX >= 1 && Landing >=2 && LandingX != LandingY
			{
				validMove = false;
			}
			// Forward Back LEFT RIGHT movement
			else{
				if(((Math.abs(startX - LandingX) !=0) &&(Math.abs(startY - LandingY) ==0)) ||((Math.abs(startX - LandingX) ==0)&&(Math.abs(LandingY - startY)!=0))){
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
						}
						else {
							for (int i = 0; i < DistanceX; i++) {
								if (piecePresent(initialX + (i * 75), e.getY())) {
									PieceInTheWay = true;
									break;

								} else {
									PieceInTheWay = false;
								}
							}
						}
					}
					else {
						if (startY - LandingY > 0) {
							for (int i = 0; i < DistanceY; i++) {
								if (piecePresent(e.getX(), initialY - (i * 75))) {
									PieceInTheWay = true;
									break;

								} else {
									PieceInTheWay = false;
								}
							}
						}

						else {
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
				if((Math.abs(startX-LandingX) == Math.abs(startY - LandingY))){
					if((startX- LandingX < 0) &&(startY - LandingY < 0)){
						for(int i = 0; i < DistanceX; i++){
							if(piecePresent((initialX + (i*75)), (initialY + (i*75)))){
								PieceInTheWay = true;

							}
						}
					}
					else if((startX-LandingX<0)&&(startY-LandingY > 0)){
						for(int i = 0; i < DistanceX; i++){
							if(piecePresent((initialX + (i*75)), (initialY - (i*75)))){
								PieceInTheWay = true;
							}
						}
					}
					else if((startX - LandingX > 0) &&(startY - LandingY > 0)){
						for(int i = 0; i < DistanceX; i++){
							if(piecePresent((initialX - (i*75)), (initialY - (i*75)))){
								PieceInTheWay = true;
							}
						}
					}
					else if((startX - LandingX >0) && (startY - LandingY < 0)){
						for(int i = 0; i < DistanceX; i++){
							if(piecePresent((initialX - (i*75)), (initialY + (i*75)))){
								PieceInTheWay = true;
							}
						}
					}
				}//End of Diagnoal Movement

				if(PieceInTheWay){
					validMove = false;
				}

				else{
					if(piecePresent(e.getX(), (e.getY()))){
						if(pieceName.contains("White")){
							if(checkWhiteOponent(e.getX(), e.getY())){
								validMove = true;
							}

							else{
								validMove = false;
							}
						}

						else{
							if(checkBlackOponent(e.getX(), e.getY())){
								validMove = true;
							}
							else{
								validMove = false;
							}
						}
					}
					else{
						validMove = true;
					}
				}


			}//END OF FIRST ELSE
		}// END OF QUEEN LINE
//King ----------------------------------------------------------------------------------------------
		else if (pieceName.contains("King")) {

			Boolean PieceInTheWay = false;

			if((MovementX >=2) || (MovementY >=2)){
				validMove = false;
			}

			else if(((MovementX == 1) && (MovementY == 0 )) || ((MovementX == 0) && (MovementY ==1))){
					validMove = true;
			}


		else if((MovementX == 1) && (MovementY == 1)) {
			validMove = true;
		}

		}
//King ----------------------------------------------------------------------------------------------
//Rook ----------------------------------------------------------------------------------------------
		else if (pieceName.contains("Rook")) {
			boolean PieceInTheWay = false;
			int DistanceX = Math.abs(startX - LandingX);
			int DistanceY = Math.abs(startY - LandingY);


			if(((LandingX <0) || LandingX > 7)||((LandingY <0) || (LandingY >7))){
					validMove = false;
			}

			else{
				if(((Math.abs(startX - LandingX) !=0) &&(Math.abs(startY - LandingY) ==0)) ||((Math.abs(startX - LandingX) ==0)&&(Math.abs(LandingY - startY)!=0))) {
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
					}

					else {
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
				}
					else{
						validMove = false;
					}
				}//END IF MATHS.ABS
			}//END ELSE
		//END ELSE IF CONTAINS
//Rook ----------------------------------------------------------------------------------------------
//Bishop --------------------------------------------------------------------------------------------

		else if(pieceName.contains("Bishup")){

			boolean PieceInTheWay = false;
			int Distance = Math.abs(startX-LandingX);

			if(((LandingX < 0 ) ||(LandingX > 7)) || ((LandingY < 0) || (LandingY > 7))){
				validMove = false;
			}
			else if (Math.abs(startX-LandingX) == Math.abs(startY - LandingY)){
				if((startX- LandingX < 0) &&(startY - LandingY < 0)){
					for(int i = 0; i < Distance; i++){
						if(piecePresent((initialX + (i*75)), (initialY + (i*75)))){
							PieceInTheWay = true;

						}
					}
				}


				else if((startX-LandingX<0)&&(startY-LandingY > 0)){
					for(int i = 0; i < Distance; i++){
						if(piecePresent((initialX + (i*75)), (initialY - (i*75)))){
							PieceInTheWay = true;
						}
					}
				}

				else if((startX - LandingX > 0) &&(startY - LandingY > 0)){
					for(int i = 0; i < Distance; i++){
						if(piecePresent((initialX - (i*75)), (initialY - (i*75)))){
							PieceInTheWay = true;
						}
					}
				}

				else if((startX - LandingX >0) && (startY - LandingY < 0)){
					for(int i = 0; i < Distance; i++){
						if(piecePresent((initialX - (i*75)), (initialY + (i*75)))){
							PieceInTheWay = true;
						}
					}
				}

				if(PieceInTheWay){
					validMove = false;
				}

				else{
					if(piecePresent(e.getX(), (e.getY()))){
						if(pieceName.contains("White")){
							if(checkWhiteOponent(e.getX(), e.getY())){
								validMove = true;
							}

							else{
								validMove = false;
							}
						}

						else{
							if(checkBlackOponent(e.getX(), e.getY())){
								validMove = true;
							}
							else{
								validMove = false;
							}
						}
					}
					else{
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
		else if (pieceName.equals("BlackPawn")){
			if(startY ==6){
				if(((MovementY==1)||(MovementY ==2))&&(startY > LandingY)&&(MovementX ==0)){
					if(MovementY == 2){
						if((!piecePresent(e.getX(), e.getY()))&&(!piecePresent(e.getX(), e.getY()+75))){
							validMove = true;
						}
					}
					else{
						if(!piecePresent(e.getX(), e.getY())){
							validMove = true;
						}
					}
				}
				else if((MovementY ==1)&&(startY > LandingY)&&(MovementX ==1)){
					if(piecePresent(e.getX(), e.getY())){
						if(checkBlackOponent(e.getX(), e.getY())){
							validMove = true;
						}
					}
				}
			}
			else{
				if(((MovementY==1))&&(startY > LandingY)&&(MovementX ==0)){
					if((!piecePresent(e.getX(), e.getY()))&&(!piecePresent(e.getX(), e.getY()+75))){
						validMove = true;
					}
				}
				else if((MovementY ==1)&&(startY > LandingY)&&(MovementX ==1)){
					if(piecePresent(e.getX(), e.getY())){
						if(checkBlackOponent(e.getX(), e.getY())){
							validMove = true;
						}
					}
				}
			}
		}
//BLACK PAWN ----------------------------------------------------------------------------------------
//White Pawn ----------------------------------------------------------------------------------------
		else if (pieceName.equals("WhitePawn")){
			if(startY == 1)
			{
				if((startX == (e.getX()/75))&&((((e.getY()/75)-startY)==1)||((e.getY()/75)-startY)==2))
				{
					if((((e.getY()/75)-startY)==2)){
						validMove = (!piecePresent(e.getX(), (e.getY()))) && (!piecePresent(e.getX(), (e.getY() + 75)));
					}
					else{
						validMove = !piecePresent(e.getX(), (e.getY()));
					}
				}
				else{
					validMove = false;
				}
			}
			else{
				int newY = e.getY()/75;
				int newX = e.getX()/75;
				if((startX-1 >=0)||(startX +1 <=7))
				{
					if((piecePresent(e.getX(), (e.getY())))&&((((newX == (startX+1)&&(startX+1<=7)))||((newX == (startX-1))&&(startX-1 >=0)))))
					{
						if(checkWhiteOponent(e.getX(), e.getY())){
							validMove = true;
							if(startY == 6){
								success = true;
							}
						}

						else{
							validMove = false;
						}
					}
					else{
						if(!piecePresent(e.getX(), (e.getY()))){
							if((startX == (e.getX()/75))&&((e.getY()/75)-startY)==1){
								if(startY == 6){
									success = true;
								}
								validMove = true;
							}
							else{
								validMove = false;
							}
						}
						else{
							validMove = false;
						}
					}
				}
				else{
					validMove = false;
				}
			}

		}
//White Pawn ----------------------------------------------------------------------------------------
		if(!validMove){
			int location=0;
			if(startY ==0){
				location = startX;
			}
			else{
				location  = (startY*8)+startX;
			}
			String pieceLocation = pieceName+".png";
			pieces = new JLabel( new ImageIcon(pieceLocation) );
			panels = (JPanel)chessBoard.getComponent(location);
		    panels.add(pieces);
		}
		else{
			if(success){
				int location = 56 + (e.getX()/75);
				if (c instanceof JLabel){
	            	Container parent = c.getParent();
	            	parent.remove(0);
					pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
					parent = (JPanel)chessBoard.getComponent(location);
			    	parent.add(pieces);
				}
				else{
					Container parent = (Container)c;
	            	pieces = new JLabel( new ImageIcon("WhiteQueen.png") );
					parent = (JPanel)chessBoard.getComponent(location);
			    	parent.add(pieces);
				}
			}
			else{
				if (c instanceof JLabel){
	            	Container parent = c.getParent();
	            	parent.remove(0);
	            	parent.add( chessPiece );
	        	}
	        	else {
	            	Container parent = (Container)c;
	            	parent.add( chessPiece );
	        	}
	    		chessPiece.setVisible(true);
			}
		}
    }

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
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE );
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
     }
}
