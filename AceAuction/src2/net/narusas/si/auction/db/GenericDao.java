package net.narusas.si.auction.db;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {
	T get(PK id);

	List<T> get(T t);

	List<T> get(T t, String order);

	List<T> getAll();

	List<T> getAll(String order);

	void save(T t);
	
	void saveOrUpdate(T t);
	
	void merge(T t);

	void update(T t);

	void remove(PK id);

	void remove(T t);
}