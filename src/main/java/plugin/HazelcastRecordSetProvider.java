package plugin;

import java.util.List;

import com.facebook.presto.spi.ConnectorColumnHandle;
import com.facebook.presto.spi.ConnectorRecordSetProvider;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.RecordSet;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastRecordSetProvider implements ConnectorRecordSetProvider {

	private String connectorId;
	private HazelcastInstance hz;

	public HazelcastRecordSetProvider(String connectorId, HazelcastInstance hz) {
		this.connectorId = connectorId;
		this.hz = hz;
	}

	@Override
	public RecordSet getRecordSet(ConnectorSplit split, List<? extends ConnectorColumnHandle> columns) {
		return new HazelcastRecordSet(split, columns, hz);
	}
}
