package states;

import java.util.ArrayList;
import java.util.List;

public abstract class StateBasedGame {
	List <State> states = new ArrayList<State>();
	
	public void addState(State state) {
		states.add(state);
	}
	
}
