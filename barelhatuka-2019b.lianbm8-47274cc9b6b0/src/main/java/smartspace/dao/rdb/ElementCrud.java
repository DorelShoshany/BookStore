package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {
	
	public List<ElementEntity> findAllByLocation_XBetweenAndLocation_YBetween(double minX, double maxX,
			double minY, double maxY, Pageable pageable);
	
	public List<ElementEntity> findAllByType(@Param("type") String type, Pageable pageable);
	
	public List<ElementEntity> findAllByName(@Param("name") String name, Pageable pageable);
	
	//added this one for MarkPagePulgin action 
	public List<ElementEntity> findAllByMoreAttributesLike(@Param("pattern") String pattern, Pageable pageable);

}
