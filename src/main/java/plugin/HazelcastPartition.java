package plugin;

import com.facebook.presto.spi.ConnectorColumnHandle;
import com.facebook.presto.spi.ConnectorPartition;
import com.facebook.presto.spi.ConnectorTableHandle;
import com.facebook.presto.spi.TupleDomain;

public class HazelcastPartition implements ConnectorPartition {

	private ConnectorTableHandle table;

	public HazelcastPartition(ConnectorTableHandle table) {
		this.table = table;
	}

	@Override
	public String getPartitionId() {
		return "partition";
	}

	@Override
	public TupleDomain<ConnectorColumnHandle> getTupleDomain() {
		return TupleDomain.all();
	}

	public ConnectorTableHandle getTable() {
		return table;
	}
}
