package plugin;

import static com.facebook.presto.util.Types.checkType;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

import java.util.*;
import java.util.Map.Entry;

import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.RecordCursor;
import com.facebook.presto.spi.type.Type;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class HazelcastCursor implements RecordCursor {

	private HazelcastSplit split;
	private List<HazelcastColumnHandle> columns;

	private Set<Entry<Object, Object>> entries;
	private Iterator<Entry<Object, Object>> iter;
	private List<Object> values;

	public HazelcastCursor(ConnectorSplit split, List<HazelcastColumnHandle> columns, HazelcastInstance hz) {
		this.split = checkType(split, HazelcastSplit.class, "split");
		this.columns = columns;

		IMap<Object, Object> map = hz.getMap(this.split.getTable().getTableName() );
		this.entries = map.entrySet(); //TODO: paging?

		this.iter = entries.iterator();
	}

	@Override
	public long getTotalBytes() {
		return Integer.MAX_VALUE;
	}

	@Override
	public long getCompletedBytes() {
		return Integer.MAX_VALUE;
	}

	@Override
	public long getReadTimeNanos() {
		return 0;
	}

	@Override
	public Type getType(int field) {
		return columns.get(field).getMetadata().getType();
	}

	@Override
	public boolean advanceNextPosition() {
		if( !iter.hasNext() )
			return false;
		Entry<Object, Object> cur = iter.next();
		values = new ArrayList<>();
		Map<String, Object> vals = HazelcastConnectorUtil.toMap(cur.getValue());
		for(HazelcastColumnHandle col : columns) {
			values.add( vals.get(col.getMetadata().getName()) );
		}
		return true;
	}

	private <T> T getVal(int field, Class<T> clazz) {
		Object val = values.get(field);
		return checkType(val, clazz, "field " + field);
	}

	@Override
	public boolean getBoolean(int field) {
		return getVal(field, Boolean.class);
	}

	@Override
	public long getLong(int field) {
		return getVal(field, Number.class).longValue();
	}

	@Override
	public double getDouble(int field) {
		return getVal(field, Number.class).doubleValue();
	}

	@Override
	public Slice getSlice(int field) {
		return Slices.utf8Slice( values.get(field).toString() );
	}

	@Override
	public boolean isNull(int field) {
		return values.get(field) == null;
	}

	@Override
	public void close() {
	}

}
