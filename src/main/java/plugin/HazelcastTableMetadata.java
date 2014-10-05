package plugin;

import com.facebook.presto.spi.ConnectorTableMetadata;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastTableMetadata extends ConnectorTableMetadata {

	public HazelcastTableMetadata(HazelcastInstance hz,
			HazelcastTableHandle tableHandle) {

		super(tableHandle.getTable(), HazelcastConnectorUtil.getColumns(tableHandle.getTable(), hz));
	}

}
