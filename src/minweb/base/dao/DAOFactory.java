package minweb.base.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public final class DAOFactory {
	private static final Log log = LogFactory.getLog(DAOFactory.class);
	private static final LoadingCache<Class<? extends DAO<?>>, DAO<?>> daos =
			CacheBuilder.newBuilder().build(new CacheLoader<Class<? extends DAO<?>>, DAO<?>>() {
				@Override
				public DAO<?> load(Class<? extends DAO<?>> cls) {
					try {
						return cls.newInstance();
					} catch (InstantiationException iex) {
						log.error("Não foi possível instanciar o DAO", iex);
					} catch (IllegalAccessException iaex) {
						log.error("Não foi possível instanciar o DAO", iaex);
					}
					return null;
				}
			});
	
	@SuppressWarnings("unchecked")
	public static <T extends DAO<?>> T getDAO(Class<T> cls) {
		return (T)daos.getUnchecked(cls);
	}
	
	private DAOFactory() { }
}