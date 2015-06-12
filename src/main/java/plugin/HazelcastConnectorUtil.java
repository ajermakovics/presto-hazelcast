package plugin;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.impl.AttributeType;
import com.hazelcast.query.impl.ReflectionHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.collect.Iterables.getFirst;
import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.facebook.presto.spi.ColumnMetadata;
import com.facebook.presto.spi.SchemaTableName;
import com.facebook.presto.spi.type.BigintType;
import com.facebook.presto.spi.type.BooleanType;
import com.facebook.presto.spi.type.DoubleType;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.spi.type.VarcharType;
import com.facebook.presto.type.UnknownType;
import com.google.common.collect.ImmutableList;

import org.apache.commons.beanutils.PropertyUtils;

public class HazelcastConnectorUtil {

	public static List<ColumnMetadata> getColumns(SchemaTableName tableHandle, HazelcastInstance hz) {
		return getColumns(hz, tableHandle.getTableName());
	}

	public static List<ColumnMetadata> getColumns(HazelcastInstance hz,
			String tableName) {
		IMap<Object, Object> map = hz.getMap(tableName);

		Collection<Object> vals = map.values( new PagingPredicate(1) );
		if( vals.isEmpty() )
			return ImmutableList.of();

		Object first = checkNotNull( getFirst(vals, null) );

		try {
			return toColumns(first);

		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static List<ColumnMetadata> toColumns(Object bean) {
		Map<String, Object> props = toMap(bean);

		List<ColumnMetadata> metaData = new ArrayList<ColumnMetadata>();
		int pos = -1;
		for(Entry<String, Object> e : props.entrySet())
			metaData.add( toMetaData(e, ++pos) );

		return metaData;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object bean) {
		Map<String, Object> props;
		try {
			props = PropertyUtils.describe(bean);
			props.remove("class"); // bean class itself
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return props;
	}

	private static ColumnMetadata toMetaData(Entry<String, Object> e, int pos) {
		Class<? extends Object> clazz = e.getValue().getClass();
		AttributeType attr = ReflectionHelper.getAttributeType(clazz);
		Type type = toType(attr);
		return new ColumnMetadata(e.getKey(), type, false);
	}

	static Type toType(AttributeType attr) {
		switch (attr) {
			case BOOLEAN: return BooleanType.BOOLEAN;
			case LONG:
			case BIG_INTEGER:
			case INTEGER: return BigintType.BIGINT;
			case FLOAT:
			case DOUBLE: return DoubleType.DOUBLE;
			case STRING: return VarcharType.VARCHAR;
			default:
				break;
		}
		return UnknownType.UNKNOWN;
	}

}
