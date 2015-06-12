package plugin;

import com.hazelcast.core.HazelcastInstance;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.presto.util.Types.checkType;

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.RecordCursor;
import com.facebook.presto.spi.RecordSet;
import com.facebook.presto.spi.type.Type;
import com.facebook.presto.util.Types;

public class HazelcastRecordSet implements RecordSet {

	private HazelcastSplit split;
	private List<HazelcastColumnHandle> columns = new ArrayList<>();
	private List<Type> types = new ArrayList<>();
	private HazelcastInstance hz;

	public HazelcastRecordSet(ConnectorSplit split, List<? extends ColumnHandle> columnHandles, HazelcastInstance hz) {
		this.split = checkType(split, HazelcastSplit.class, "split");
		this.hz = hz;

		for (ColumnHandle col : columnHandles) {
			HazelcastColumnHandle column = Types.checkType(col, HazelcastColumnHandle.class, "col");
			this.columns.add( column );
			types.add( column.getMetadata().getType() );
		}
	}

	@Override
	public List<Type> getColumnTypes() {
		return types ;
	}

	@Override
	public RecordCursor cursor() {
		return new HazelcastCursor(split, columns, hz);
	}

}
