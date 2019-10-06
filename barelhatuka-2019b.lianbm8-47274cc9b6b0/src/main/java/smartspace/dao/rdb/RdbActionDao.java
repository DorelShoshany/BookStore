package smartspace.dao.rdb;

import smartspace.dao.AdvancedActionDao;
import smartspace.data.ActionEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RdbActionDao implements AdvancedActionDao {
	private ActionCrud actionCrud;
	private String smartspcae;
	private IdCreatorCrud idCreatorCrud;
	
	@Autowired
	public RdbActionDao(ActionCrud actionCrud, IdCreatorCrud idCreatorCrud) {
		this.actionCrud = actionCrud;
		this.idCreatorCrud = idCreatorCrud;
	}
	
	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspcae = smartspace;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity action) {
		// TODO - replace by key generated using DB
		action.setActionSmartspace(smartspcae);
		action.setElementSmartspace(smartspcae);
		action.setPlayerSmartspace(smartspcae);
		IdCreator idCreator = this.idCreatorCrud.save(new IdCreator());
		action.setKey(action.getActionSmartspace() + "#" + idCreator.getId());
		
		// SQL: INSERT
		if(!this.actionCrud.existsById(action.getKey())) {
			return this.actionCrud.save(action);
		}
		else {
			throw new RuntimeException("action already exists");
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll() {
		// SQL: SELECT
		List<ActionEntity> rv = new ArrayList<>();
		this.actionCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	public void deleteAll() {
		// SQL: DELETE
		this.actionCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(int size, int page) {
		return this.actionCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(int size, int page, String sortAttr) {
		return this.actionCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr))
				.getContent();
	}

	@Override
	@Transactional
	public ActionEntity createFromImport(ActionEntity action) {
		action.setKey(action.getActionSmartspace() + "#" + action.getActionId());
		
		// SQL: INSERT
		if(!this.actionCrud.existsById(action.getKey())) {
			return this.actionCrud.save(action);
		}
		else {
			throw new RuntimeException("action already exists");
		}
	}

}
