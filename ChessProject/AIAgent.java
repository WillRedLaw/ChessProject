import java.util.*;

public class AIAgent {

    Random random;

    public AIAgent(){

        random = new Random();

    }

    public Move randomMove(Stack possibilities){

        int moveID = random.nextInt(possibilities.size());

        System.out.println("Agent randomly selected move : " + moveID);
        for(int i = 1; i < (possibilities.size() -(moveID)); i++){
            possibilities.pop();
        }

        Move selectedMOve = (Move)possibilities.pop();
        return selectedMOve;

    }

}
