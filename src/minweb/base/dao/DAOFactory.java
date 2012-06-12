package minweb.base.dao;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public final class DAOFactory {
	private static LoadingCache<Class<? extends DAO<?>>, DAO<?>> daos =
			CacheBuilder.newBuilder().build(new CacheLoader<Class<? extends DAO<?>>, DAO<?>>() {
				@Override
				public DAO<?> load(Class<? extends DAO<?>> cls) throws Exception {
					return cls.newInstance();
				}
			});
	
	@SuppressWarnings("unchecked")
	public static <T extends DAO<?>> T getDAO(Class<T> cls) throws ExecutionException {
		return (T)daos.get(cls);
	}
	
	private DAOFactory() { }
}