package plugin;

import com.hazelcast.core.HazelcastInstance;

import java.util.List;

import com.facebook.presto.spi.ColumnHandle;
import com.facebook.presto.spi.ConnectorRecordSetProvider;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.RecordSet;

public class HazelcastRecordSetProvider implements ConnectorRecordSetProvider {

	private String connectorId;
	private HazelcastInstance hz;

	public HazelcastRecordSetProvider(String connectorId, HazelcastInstance hz) {
		this.connectorId = connectorId;
		this.hz = hz;
	}

	@Override
	public RecordSet getRecordSet(ConnectorSplit split, List<? extends ColumnHandle> columns) {
		return new HazelcastRecordSet(split, columns, hz);
	}
}
