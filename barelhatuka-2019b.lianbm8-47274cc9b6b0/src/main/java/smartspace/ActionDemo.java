package smartspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

//import org.springframework.stereotype.Repository;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

//@Repository
public class ActionDemo implements ActionDao {
	
	private List<ActionEntity> actions;
	private AtomicLong ID;
	
	public ActionDemo() {
		this.actions = Collections.synchronizedList(new ArrayList<>());
		this.ID = new AtomicLong(1L);
	}

	@Override
	public ActionEntity create(ActionEntity action) {
		action.setKey(action.getElementSmartspace() + "#" + this.ID.getAndIncrement());
		this.actions.add(action);
		return action;
	}

	@Override
	public List<ActionEntity> readAll() {
		return actions;
	}

	@Override
	public void deleteAll() {
		this.actions.clear();

	}

}
